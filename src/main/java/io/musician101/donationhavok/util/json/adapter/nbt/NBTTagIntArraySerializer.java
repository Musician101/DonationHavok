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
import net.minecraft.nbt.NBTTagIntArray;

import static io.musician101.donationhavok.util.json.Keys.INTEGER;

@AdapterOf
public class NBTTagIntArraySerializer extends BaseSerializer<NBTTagIntArray> {

    @AdapterType
    public static final TypeToken<NBTTagIntArray> TYPE = TypeToken.get(NBTTagIntArray.class);

    @Override
    public NBTTagIntArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(INTEGER);
        return new NBTTagIntArray(StreamSupport.stream(jsonArray.spliterator(), false).map(JsonElement::getAsInt).collect(Collectors.toList()));
    }

    @Override
    public JsonElement serialize(NBTTagIntArray src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (int i : src.getIntArray()) {
            jsonArray.add(i);
        }

        jsonObject.add(INTEGER, jsonArray);
        return jsonObject;
    }
}
