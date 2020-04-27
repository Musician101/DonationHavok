package io.musician101.donationhavok.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

public class TwitchConfig extends AbstractConfig<TwitchConfig> {

    private final boolean bitsTrigger;
    private final boolean enabled;
    private final boolean factorSubStreak;
    private final boolean roundSubs;
    private final boolean subsTrigger;
    @Nonnull
    private final String twitchName;
    @Nonnull
    private final TwitchCommandsConfig twitchCommandsConfig;

    TwitchConfig() {
        this(false, false, false, false, false, "Your Twitch name here!", new TwitchCommandsConfig());
    }

    public TwitchConfig(boolean bitsTrigger, boolean enabled, boolean roundSubs, boolean factorSubStreak, boolean subsTrigger, @Nonnull String twitchName, @Nonnull TwitchCommandsConfig twitchCommandsConfig) {
        this.bitsTrigger = bitsTrigger;
        this.enabled = enabled;
        this.factorSubStreak = factorSubStreak;
        this.roundSubs = roundSubs;
        this.subsTrigger = subsTrigger;
        this.twitchName = twitchName;
        this.twitchCommandsConfig = twitchCommandsConfig;
    }

    public boolean doBitsTrigger() {
        return bitsTrigger;
    }

    @Nonnull
    @Override
    protected File getFile() {
        return new File(getDir(), "twitch.json");
    }

    @Nonnull
    public TwitchCommandsConfig getTwitchCommandsConfig() {
        return twitchCommandsConfig;
    }

    @Nonnull
    public String getTwitchName() {
        return twitchName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean factorSubStreak() {
        return factorSubStreak;
    }

    public boolean doSubsTrigger() {
        return subsTrigger;
    }

    public boolean roundSubs() {
        return roundSubs;
    }

    public static class Serializer extends BaseSerializer<TwitchConfig> {

        @Override
        public TwitchConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean bitTrigger = deserialize(jsonObject, context, Keys.BITS_TRIGGER, false);
            boolean enabled = deserialize(jsonObject, context, Keys.BITS_TRIGGER, false);
            boolean factorSubStreak = deserialize(jsonObject, context, Keys.FACTOR_SUB_STREAK, false);
            boolean roundSubs = deserialize(jsonObject, context, Keys.ROUND_SUBS, false);
            boolean subsTrigger = deserialize(jsonObject, context, Keys.SUBS_TRIGGER, false);
            String twitchName = deserialize(jsonObject, context, Keys.TWITCH_NAME, "Your Twitch name here!");
            TwitchCommandsConfig twitchCommandsConfig = deserialize(jsonObject, context, Keys.TWITCH_COMMANDS, new TwitchCommandsConfig());
            return new TwitchConfig(bitTrigger, enabled, factorSubStreak, roundSubs, subsTrigger, twitchName, twitchCommandsConfig);
        }

        @Override
        public JsonElement serialize(TwitchConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.BITS_TRIGGER, src.doBitsTrigger());
            serialize(jsonObject, context, Keys.ENABLE, src.doBitsTrigger());
            serialize(jsonObject, context, Keys.FACTOR_SUB_STREAK, src.factorSubStreak());
            serialize(jsonObject, context, Keys.ROUND_SUBS, src.roundSubs());
            serialize(jsonObject, context, Keys.SUBS_TRIGGER, src.doSubsTrigger());
            serialize(jsonObject, context, Keys.TWITCH_NAME, src.getTwitchName());
            serialize(jsonObject, context, Keys.TWITCH_COMMANDS, src.getTwitchCommandsConfig());
            return jsonObject;
        }
    }
}
