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
import java.util.List;
import javax.annotation.Nonnull;

public class TwitchCommandConfig {

    private final boolean enabled;
    @Nonnull
    private final List<CommandPermission> permissions;

    TwitchCommandConfig() {
        this(false, Collections.singletonList(CommandPermission.BROADCASTER));
    }

    public TwitchCommandConfig(boolean enabled, @Nonnull List<CommandPermission> permissions) {
        this.enabled = enabled;
        this.permissions = permissions;
    }

    @Nonnull
    public List<CommandPermission> getPermissions() {
        return permissions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static class Serializer extends BaseSerializer<TwitchCommandConfig> {

        @Override
        public TwitchCommandConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            List<CommandPermission> permissions = deserialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, Collections.singletonList(CommandPermission.BROADCASTER));
            return new TwitchCommandConfig(enabled, permissions);
        }

        @Override
        public JsonElement serialize(TwitchCommandConfig src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, src.getPermissions());
            return jsonObject;
        }
    }
}
