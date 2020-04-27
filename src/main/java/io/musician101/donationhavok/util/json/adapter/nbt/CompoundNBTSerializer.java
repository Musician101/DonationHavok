package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants.NBT;

public class CompoundNBTSerializer extends BaseSerializer<CompoundNBT> {

    @Override
    public CompoundNBT deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CompoundNBT nbt = new CompoundNBT();
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
            JsonElement jsonElement = entry.getValue();
            String key = entry.getKey();
            if (jsonElement.isJsonObject()) {
                nbt.put(key, context.deserialize(jsonElement.getAsJsonObject(), CompoundNBT.class));
            }
            else if (jsonElement.isJsonArray()) {
                nbt.put(key, context.deserialize(jsonElement, ListNBT.class));
            }
            else if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (jsonPrimitive.isNumber() || jsonPrimitive.isBoolean()) {
                    nbt.put(key, context.deserialize(jsonPrimitive, NumberNBT.class));
                }
                else {
                    nbt.putString(key, jsonPrimitive.getAsString());
                }
            }
            else {
                nbt.putString(key, jsonObject.toString());
            }
        });

        return nbt;
    }

    @Override
    public JsonElement serialize(CompoundNBT src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        src.keySet().forEach(key -> {
            INBT nbt = src.get(key);
            switch (nbt.getId()) {
                case NBT.TAG_BYTE:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getByte());
                    break;
                case NBT.TAG_BYTE_ARRAY:
                    jsonObject.add(key, context.serialize(nbt, ByteArrayNBT.class));
                    break;
                case NBT.TAG_COMPOUND:
                    jsonObject.add(key, context.serialize(nbt, CompoundNBT.class));
                    break;
                case NBT.TAG_DOUBLE:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getDouble());
                    break;
                case NBT.TAG_FLOAT:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getFloat());
                    break;
                case NBT.TAG_INT:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getInt());
                    break;
                case NBT.TAG_INT_ARRAY:
                    jsonObject.add(key, context.serialize(nbt, IntArrayNBT.class));
                    break;
                case NBT.TAG_LIST:
                    jsonObject.add(key, context.serialize(nbt, ListNBT.class));
                    break;
                case NBT.TAG_LONG:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getLong());
                    break;
                case NBT.TAG_SHORT:
                    jsonObject.addProperty(key, ((NumberNBT) nbt).getShort());
                    break;
                case NBT.TAG_STRING:
                    jsonObject.addProperty(key, nbt.getString());
                    break;
            }
        });

        return jsonObject;
    }
}
