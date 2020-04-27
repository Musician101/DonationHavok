package io.musician101.donationhavok.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.Collections;
import javax.annotation.Nonnull;

public class TwitchCommandsConfig {

    private final boolean enabled;
    @Nonnull
    private final TwitchCommandConfig discoveryCommandConfig;
    @Nonnull
    private final TwitchCommandConfig playersCommandConfig;
    @Nonnull
    private final TwitchCommandConfig rewardsCommandConfig;
    private static final TwitchCommandConfig defaultCommandConfig = new TwitchCommandConfig(false, Collections.singletonList(CommandPermission.BROADCASTER));

    TwitchCommandsConfig() {
        this(false, defaultCommandConfig, defaultCommandConfig, defaultCommandConfig);
    }

    public TwitchCommandsConfig(boolean enabled, @Nonnull TwitchCommandConfig discoveryCommandConfig, @Nonnull TwitchCommandConfig playersCommandConfig, @Nonnull TwitchCommandConfig rewardsCommandConfig) {
        this.enabled = enabled;
        this.discoveryCommandConfig = discoveryCommandConfig;
        this.playersCommandConfig = playersCommandConfig;
        this.rewardsCommandConfig = rewardsCommandConfig;
    }

    @Nonnull
    public TwitchCommandConfig getDiscoveryCommandConfig() {
        return discoveryCommandConfig;
    }

    @Nonnull
    public TwitchCommandConfig getPlayersCommandConfig() {
        return playersCommandConfig;
    }

    @Nonnull
    public TwitchCommandConfig getRewardsCommandConfig() {
        return rewardsCommandConfig;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static class Serializer extends BaseSerializer<TwitchCommandsConfig> {

        @Override
        public TwitchCommandsConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            TwitchCommandConfig discoveryCommandConfig = deserialize(jsonObject, context, Keys.DISCOVERY_COMMAND, new TwitchCommandConfig());
            TwitchCommandConfig playerListCommandConfig = deserialize(jsonObject, context, Keys.PLAYERS_COMMAND, new TwitchCommandConfig());
            TwitchCommandConfig rewardsListCommandConfig = deserialize(jsonObject, context, Keys.REWARDS_COMMAND, new TwitchCommandConfig());
            return new TwitchCommandsConfig(enabled, discoveryCommandConfig, playerListCommandConfig, rewardsListCommandConfig);
        }

        @Override
        public JsonElement serialize(TwitchCommandsConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.DISCOVERY_COMMAND, src.getDiscoveryCommandConfig());
            serialize(jsonObject, context, Keys.PLAYERS_COMMAND, src.getPlayersCommandConfig());
            serialize(jsonObject, context, Keys.REWARDS_COMMAND, src.getRewardsCommandConfig());
            return jsonObject;
        }
    }
}
