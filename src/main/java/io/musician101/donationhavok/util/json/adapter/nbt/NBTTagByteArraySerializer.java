package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.util.json.adapter.AdapterOf;
import io.musician101.donationhavok.util.json.adapter.AdapterType;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.NBTTagByteArray;

import static io.musician101.donationhavok.util.json.Keys.BYTE;

@AdapterOf
public class NBTTagByteArraySerializer extends BaseSerializer<NBTTagByteArray> {

    @AdapterType
    public static final TypeToken<NBTTagByteArray> TYPE = TypeToken.get(NBTTagByteArray.class);

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
