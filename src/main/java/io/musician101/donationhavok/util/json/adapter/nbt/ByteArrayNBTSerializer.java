package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.ByteArrayNBT;

import static io.musician101.donationhavok.util.json.Keys.BYTE;

public class ByteArrayNBTSerializer extends BaseSerializer<ByteArrayNBT> {

    @Override
    public ByteArrayNBT deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(BYTE);
        return new ByteArrayNBT(StreamSupport.stream(jsonArray.spliterator(), false).map(JsonElement::getAsByte).collect(Collectors.toList()));
    }

    @Override
    public JsonElement serialize(ByteArrayNBT src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i : src.getByteArray()) {
            jsonArray.add(i);
        }

        jsonObject.add(BYTE, jsonArray);
        return jsonObject;
    }
}
