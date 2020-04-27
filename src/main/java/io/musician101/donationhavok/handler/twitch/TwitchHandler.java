package io.musician101.donationhavok.handler.twitch;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.twitch.commands.TwitchCommands;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TwitchHandler {

    @Nonnull
    private final TwitchBot twitchBot = new TwitchBot();
    @Nonnull
    private final TwitchCommands twitchCommands = new TwitchCommands();

    public void connect() {
        if (DonationHavok.getInstance().getConfig().getTwitchConfig().isEnabled() && !twitchBot.isRunning()) {
            if (DonationHavok.getInstance().getConfig().getTwitchConfig().getTwitchCommandsConfig().isEnabled()) {
                twitchCommands.register(twitchBot);
            }

            Executors.defaultThreadFactory().newThread(twitchBot).start();
        }
    }

    public void disconnect() {
        if (twitchBot.isRunning()) {
            synchronized (twitchBot) {
                twitchBot.leaveChannel(DonationHavok.getInstance().getConfig().getTwitchConfig().getTwitchName());
                twitchBot.stop();
                twitchBot.disconnect();
            }
        }
    }

    @Nonnull
    public TwitchCommands getTwitchCommands() {
        return twitchCommands;
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerLoggedInEvent event) {
        if (DonationHavok.getInstance().getConfig().getGeneralConfig().getMCName().equals(event.getPlayer().getName().getString())) {
            if (DonationHavok.getInstance().getConfig().getTwitchConfig().isEnabled()) {
                connect();
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerLoggedOutEvent event) {
        if (DonationHavok.getInstance().getConfig().getGeneralConfig().getMCName().equals(event.getPlayer().getName().getString())) {
            if (DonationHavok.getInstance().getConfig().getTwitchConfig().isEnabled()) {
                disconnect();
            }
        }
    }
}
