package io.musician101.donationhavok;

import com.google.gson.JsonPrimitive;
import io.musician101.donationhavok.command.DHCommands;
import io.musician101.donationhavok.config.Config;
import io.musician101.donationhavok.handler.StreamlabsHandler;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.scheduler.Scheduler;
import io.musician101.donationhavok.util.json.Keys;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.annotation.Nonnull;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Reference.MOD_ID)
public final class DonationHavok {

    private Config config;
    private DiscoveryHandler discoveryHandler;
    private final Logger logger = LogManager.getLogger(Reference.MOD_ID);
    private final Scheduler scheduler = new Scheduler();
    @Nonnull
    private final StreamlabsHandler streamLabsHandler = new StreamlabsHandler();
    @Nonnull
    private final TwitchHandler twitchHandler = new TwitchHandler();

    public DonationHavok() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStop);
    }

    public static DonationHavok getInstance() {
        return ModList.get().<DonationHavok>getModObjectById(Reference.MOD_ID).orElseThrow(() -> new IllegalStateException("Mod " + Reference.MOD_ID + " does not exist."));
    }

    @Nonnull
    public Config getConfig() {
        return config;
    }

    @Nonnull
    public DiscoveryHandler getDiscoveryHandler() {
        return discoveryHandler;
    }

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    private void onServerStart(FMLServerStartingEvent event) {
        MinecraftForge.EVENT_BUS.register(twitchHandler);
        MinecraftForge.EVENT_BUS.register(scheduler);
        MinecraftForge.EVENT_BUS.register(streamLabsHandler);
        twitchHandler.connect();
        event.getCommandDispatcher().register(DHCommands.dh());
    }

    private void onServerStop(FMLServerStoppingEvent event) {
        twitchHandler.disconnect();
        scheduler.purge();
        MinecraftForge.EVENT_BUS.unregister(twitchHandler);
        MinecraftForge.EVENT_BUS.unregister(scheduler);
        MinecraftForge.EVENT_BUS.unregister(streamLabsHandler);
    }

    private void postInit(FMLLoadCompleteEvent event) {
        config = new Config();
        generateConfiguratorFiles();
    }

    private void generateConfiguratorFiles() {
        File dir = new File("config/" + Reference.MOD_ID, "configurator");
        dir.mkdirs();
        RegistryManager.getRegistryNamesForSyncToClient().forEach(name -> {
            File file = new File(dir, name.toString().replace(":", "_") + ".json");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                ForgeRegistry<?> forgeRegistry = RegistryManager.ACTIVE.getRegistry(name);
                FileWriter fw = new FileWriter(file);
                Keys.GSON.toJson(forgeRegistry.getKeys().stream().map(entry -> new JsonPrimitive(entry.toString())).collect(Keys.jsonArrayCollector()), fw);
                fw.close();
            }
            catch (IOException e) {
                logger.error("An error occurred while trying to save \"" + file.getPath() + "\"", e);
            }
        });

        File file = new File(dir, "block_states.json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            Keys.GSON.toJson(ForgeRegistries.BLOCKS.getValues().stream().map(value -> new JsonPrimitive(BlockStateParser.toString(value.getDefaultState()))).collect(Keys.jsonArrayCollector()), fw);
            fw.close();
        }
        catch (IOException e) {
            logger.error("An error occurred while trying to save \"" + file.getPath() + "\"", e);
        }
    }

    private void preInit(FMLCommonSetupEvent event) {
        Network.init();
        discoveryHandler = DiscoveryHandler.load();
        logger.info("\\_o<");
    }

    @Nonnull
    public StreamlabsHandler getStreamLabsHandler() {
        return streamLabsHandler;
    }
}
