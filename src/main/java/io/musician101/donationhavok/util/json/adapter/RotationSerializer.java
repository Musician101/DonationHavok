package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.stream.Stream;
import net.minecraft.util.Rotation;

public class RotationSerializer extends BaseSerializer<Rotation> {

    @Override
    public Rotation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Stream.of(Rotation.values()).filter(rotation -> rotation.toString().equals(json.getAsString())).findFirst().orElse(Rotation.NONE);
    }

    @Override
    public JsonElement serialize(Rotation src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
