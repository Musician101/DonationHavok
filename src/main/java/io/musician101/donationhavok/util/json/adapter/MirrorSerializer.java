package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.stream.Stream;
import net.minecraft.util.Mirror;

public class MirrorSerializer extends BaseSerializer<Mirror> {

    @Override
    public Mirror deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Stream.of(Mirror.values()).filter(mirror -> mirror.toString().equals(json.getAsString())).findFirst().orElse(Mirror.NONE);
    }

    @Override
    public JsonElement serialize(Mirror src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
