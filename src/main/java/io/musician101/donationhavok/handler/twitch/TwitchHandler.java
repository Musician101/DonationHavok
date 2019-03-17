package io.musician101.donationhavok.handler.twitch;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.twitch.commands.TwitchCommands;
import io.musician101.donationhavok.handler.twitch.event.Cheer;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.handler.twitch.event.Subscription;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class TwitchHandler {

    private final boolean bitsTrigger;
    private final boolean enabled;
    private final boolean factorSubStreak;
    private final boolean roundSubs;
    @Nonnull
    private final String streamerName;
    private final boolean subsTrigger;
    @Nonnull
    private final TwitchBot twitchBot;
    @Nonnull
    private final TwitchCommands twitchCommands;

    public TwitchHandler() {
        this.bitsTrigger = false;
        this.enabled = false;
        this.roundSubs = true;
        this.twitchCommands = new TwitchCommands();
        this.factorSubStreak = false;
        this.subsTrigger = false;
        this.streamerName = "Your Twitch name here!";
        twitchBot = new TwitchBot(streamerName);
    }

    private TwitchHandler(boolean bitsTrigger, boolean enabled, boolean factorSubStreak, boolean roundSubs, boolean subsTrigger, @Nonnull String streamerName, @Nonnull TwitchCommands twitchCommands) {
        this.bitsTrigger = bitsTrigger;
        this.enabled = enabled;
        this.factorSubStreak = factorSubStreak;
        this.roundSubs = roundSubs;
        this.subsTrigger = subsTrigger;
        this.twitchCommands = twitchCommands;
        this.streamerName = streamerName;
        this.twitchBot = new TwitchBot(streamerName);
        twitchBot.setListeningForSubs(subsTrigger);
        twitchBot.setIsListeningForBits(bitsTrigger);
        if (twitchCommands.isEnabled()) {
            twitchCommands.register(twitchBot);
        }
    }

    public boolean bitsTrigger() {
        return bitsTrigger;
    }

    public void connect() {
        if (enabled && !twitchBot.isRunning()) {
            Executors.defaultThreadFactory().newThread(twitchBot).start();
        }
    }

    public void disconnect() {
        if (twitchBot.isRunning()) {
            synchronized (twitchBot) {
                twitchBot.leaveChannel(streamerName);
                twitchBot.stop();
                twitchBot.disconnect();
            }
        }
    }

    public boolean factorSubStreak() {
        return factorSubStreak;
    }

    public String getChannelName() {
        return streamerName;
    }

    @Nonnull
    public TwitchCommands getTwitchCommands() {
        return twitchCommands;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerLoggedInEvent event) {
        if (DonationHavok.INSTANCE.getRewardsHandler().getMCName().equals(event.player.getName())) {
            if (enabled) {
                connect();
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerLoggedOutEvent event) {
        if (DonationHavok.INSTANCE.getRewardsHandler().getMCName().equals(event.player.getName())) {
            if (enabled) {
                disconnect();
            }
        }
    }

    public boolean roundSubs() {
        return roundSubs;
    }

    public void runCheer(int amount) {
        Cheer cheer = new Cheer(streamerName, streamerName, amount, "This is a test cheer.");
        twitchBot.runCheer(cheer);
    }

    public void runSubscription(@Nonnull SubPlan subPlan, int streak) {
        Subscription subscription = new Subscription(streamerName, streamerName, subPlan, streak == 1, streak, "This is a test subscription.");
        twitchBot.runSubscription(subscription);
    }

    public boolean subsTrigger() {
        return subsTrigger;
    }

    public static class Serializer extends BaseSerializer<TwitchHandler> {

        @Override
        public TwitchHandler deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean bitsTrigger = deserialize(jsonObject, context, Keys.BITS_TRIGGER, false);
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            boolean factorSubStreak = deserialize(jsonObject, context, Keys.FACTOR_SUB_STREAK, false);
            boolean roundSubs = deserialize(jsonObject, context, Keys.ROUND_SUBS, false);
            boolean subsTrigger = deserialize(jsonObject, context, Keys.SUBS_TRIGGER, false);
            String twitchName = deserialize(jsonObject, context, Keys.TWITCH_NAME, "Your Twitch name here!");
            TwitchCommands twitchCommands = deserialize(jsonObject, context, Keys.TWITCH_COMMANDS, new TwitchCommands());
            return new TwitchHandler(bitsTrigger, enabled, factorSubStreak, roundSubs, subsTrigger, twitchName, twitchCommands);
        }

        @Override
        public JsonElement serialize(TwitchHandler src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.BITS_TRIGGER, src.bitsTrigger());
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.FACTOR_SUB_STREAK, src.factorSubStreak());
            serialize(jsonObject, context, Keys.ROUND_SUBS, src.roundSubs());
            serialize(jsonObject, context, Keys.SUBS_TRIGGER, src.subsTrigger());
            serialize(jsonObject, context, Keys.TWITCH_COMMANDS, src.twitchCommands);
            serialize(jsonObject, context, Keys.TWITCH_NAME, src.getChannelName());
            return jsonObject;
        }
    }
}
