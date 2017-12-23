package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.NBTTagByteArray;

import static io.musician101.donationhavok.util.json.JsonUtils.BYTE;

public class NBTTagByteArrayTypeAdapter implements JsonDeserializer<NBTTagByteArray>, JsonSerializer<NBTTagByteArray> {

    @Override
    public NBTTagByteArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(BYTE);
        return new NBTTagByteArray(StreamSupport.stream(jsonArray.spliterator(), false).map(JsonElement::getAsByte).collect(Collectors.toList()));
    }

    @Override
    public JsonElement serialize(NBTTagByteArray src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i : src.getByteArray()) {
            jsonArray.add(i);
        }

        jsonObject.add(BYTE, jsonArray);
        return jsonObject;
    }
}
