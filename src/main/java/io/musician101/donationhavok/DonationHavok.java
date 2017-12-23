package io.musician101.donationhavok;

import io.musician101.donationhavok.command.DHCommand;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.scheduler.Scheduler;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

//TODO for the future, add pubsub like in https://github.com/twitch4j/twitch4j for bits and subs
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, acceptableRemoteVersions = "*")
public class DonationHavok {

    @Instance(Reference.MOD_ID)
    public static DonationHavok INSTANCE;

    private File configDir;
    private File configFile;
    private Logger logger;
    private Scheduler scheduler;
    private StreamLabsTracker streamLabsTracker;

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    public StreamLabsTracker getStreamLabsTracker() {
        return streamLabsTracker;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        configDir = event.getSuggestedConfigurationFile().getParentFile();
        configFile = new File(configDir, "donation_havok.json");
        MinecraftForge.EVENT_BUS.register(new DisconnectListener());
        Network.init();
        logger.info("\\_o<");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        try {
            reload();
        }
        catch (Exception e) {
            logger.warn("There was an issue loading the config. No rewards for you :c");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        scheduler = new Scheduler();
        MinecraftForge.EVENT_BUS.register(scheduler);
        event.registerServerCommand(new DHCommand());
    }

    @EventHandler
    public void onServerStop(FMLServerStoppingEvent event) {
        scheduler.purge();
    }

    public void reload() throws IOException {
        if (!configFile.exists()) {
            configDir.mkdirs();
            configFile.createNewFile();
            OutputStream os = new FileOutputStream(configFile);
            os.write(GSON.toJson(new StreamLabsTracker()).getBytes());
            os.close();
        }

        if (streamLabsTracker != null) {
            MinecraftForge.EVENT_BUS.unregister(streamLabsTracker);
        }

        streamLabsTracker = GSON.fromJson(new FileReader(configFile), StreamLabsTracker.class);
        MinecraftForge.EVENT_BUS.register(streamLabsTracker);
    }

    public void saveConfig(StreamLabsTracker streamLabsTracker) throws IOException {
        if (!configFile.exists()) {
            configDir.mkdirs();
            configFile.createNewFile();
        }

        OutputStream os = new FileOutputStream(configFile);
        os.write(GSON.toJson(streamLabsTracker).getBytes());
        os.close();
        MinecraftForge.EVENT_BUS.unregister(this.streamLabsTracker);
        this.streamLabsTracker = streamLabsTracker;
        MinecraftForge.EVENT_BUS.register(this.streamLabsTracker);
    }
}
