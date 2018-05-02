package io.musician101.donationhavok;

import io.musician101.donationhavok.command.DHCommand;
import io.musician101.donationhavok.handler.StreamLabsHandler;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.scheduler.Scheduler;
import io.musician101.donationhavok.util.json.JsonKeyProcessor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, useMetadata = true)
public class DonationHavok {

    @Instance(Reference.MOD_ID)
    public static DonationHavok INSTANCE;
    private File configDir;
    private File configFile;
    private DiscoveryHandler discoveryHandler;
    private HavokRewardsHandler havokRewardsHandler;
    private Logger logger;
    private Scheduler scheduler;
    private StreamLabsHandler streamLabsHandler;
    private TwitchHandler twitchHandler;

    @Nonnull
    public File getConfigDir() {
        return configDir;
    }

    @Nonnull
    public DiscoveryHandler getDiscoveryHandler() {
        return discoveryHandler;
    }

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    @Nonnull
    public HavokRewardsHandler getRewardsHandler() {
        return havokRewardsHandler;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Nonnull
    public StreamLabsHandler getStreamLabsHandler() {
        return streamLabsHandler;
    }

    @Nonnull
    public TwitchHandler getTwitchHandler() {
        return twitchHandler;
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        scheduler = new Scheduler();
        MinecraftForge.EVENT_BUS.register(scheduler);
        twitchHandler.connect();
        event.registerServerCommand(new DHCommand());
    }

    @EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        twitchHandler.disconnect();
        scheduler.purge();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            reload();
        }
        catch (Exception e) {
            logger.warn("There was an issue loading the config. No rewards for you :c", e);
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configDir = event.getSuggestedConfigurationFile().getParentFile();
        configFile = new File(configDir, "donation_havok.json");
        MinecraftForge.EVENT_BUS.register(new DisconnectListener());
        Network.init();
        JsonKeyProcessor.init();
        logger.info("\\_o<");
    }

    public void reload() throws IOException {
        if (!configFile.exists()) {
            configDir.mkdirs();
            configFile.createNewFile();
            FileWriter fileWriter = new FileWriter(configFile);
            GSON.toJson(new Config(), fileWriter);
            fileWriter.close();
        }

        if (streamLabsHandler != null) {
            MinecraftForge.EVENT_BUS.unregister(streamLabsHandler);
        }

        FileReader fileReader = new FileReader(configFile);
        Config config = GSON.fromJson(fileReader, Config.class);
        fileReader.close();
        discoveryHandler = config.getDiscoveryHandler();
        havokRewardsHandler = config.getHavokRewardsHandler();
        streamLabsHandler = config.getStreamLabsHandler();
        if (twitchHandler != null) {
            twitchHandler.disconnect();
        }

        twitchHandler = config.getTwitchHandler();
        if (streamLabsHandler.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(streamLabsHandler);
        }

        if (FMLCommonHandler.instance().getSide().isServer()) {
            twitchHandler.connect();
        }
    }

    public void saveConfig(@Nonnull DiscoveryHandler discoveryHandler, @Nonnull HavokRewardsHandler havokRewardsHandler, @Nonnull StreamLabsHandler streamLabsHandler, @Nonnull TwitchHandler twitchHandler) throws IOException {
        if (!configFile.exists()) {
            configDir.mkdirs();
            configFile.createNewFile();
        }

        MinecraftForge.EVENT_BUS.unregister(this.streamLabsHandler);
        MinecraftForge.EVENT_BUS.unregister(this.twitchHandler);
        FileWriter fileWriter = new FileWriter(configFile);
        GSON.toJson(new Config(discoveryHandler, havokRewardsHandler, streamLabsHandler, twitchHandler), fileWriter);
        fileWriter.close();
        this.discoveryHandler = discoveryHandler;
        this.havokRewardsHandler = havokRewardsHandler;
        this.streamLabsHandler = streamLabsHandler;
        this.twitchHandler.disconnect();
        this.twitchHandler = twitchHandler;
        if (this.streamLabsHandler.isEnabled()) {
            MinecraftForge.EVENT_BUS.register(this.streamLabsHandler);
        }

        if (FMLCommonHandler.instance().getSide().isServer()) {
            this.twitchHandler.connect();
        }
    }

    @NetworkCheckHandler
    public boolean versionCheck(Map<String, String> versions, Side side) {
        if (side.isServer()) {
            return !versions.containsKey(Reference.MOD_ID) || versions.get(Reference.MOD_ID).equals(Reference.VERSION);
        }

        return true;
    }
}
