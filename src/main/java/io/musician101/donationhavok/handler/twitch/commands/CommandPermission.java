package io.musician101.donationhavok.handler.twitch.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.twitch.commands.CommandPermission.Serializer;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
import java.util.stream.Stream;

@TypeOf(Serializer.class)
public enum CommandPermission {

    EVERYONE,
    PRIME_TURBO,
    PARTNER,
    SUBSCRIBER,
    MODERATOR,
    BROADCASTER,
    OWNER;

    public static class Serializer extends BaseSerializer<CommandPermission> {

        public Serializer() {
            super();
        }

        @Override
        public CommandPermission deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Stream.of(CommandPermission.values())
                    //.filter(permission -> permission == CommandPermission.PRIME_TURBO || permission == CommandPermission.PARTNER || permission == CommandPermission.OWNER)
                    .filter(permission -> permission.toString().equalsIgnoreCase(json.getAsString())).findFirst().orElse(CommandPermission.BROADCASTER);
        }

        @Override
        public JsonElement serialize(CommandPermission src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
