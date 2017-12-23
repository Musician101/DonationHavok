package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class NBTTagCompoundTypeAdapter implements JsonDeserializer<NBTTagCompound>, JsonSerializer<NBTTagCompound> {

    @Override
    public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        NBTTagCompound nbt = new NBTTagCompound();
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.entrySet().forEach(entry -> {
            JsonElement jsonElement = entry.getValue();
            String key = entry.getKey();
            if (jsonElement.isJsonObject()) {
                nbt.setTag(key, deserialize(jsonElement.getAsJsonObject(), typeOfT, context));
            }
            else if (jsonElement.isJsonArray()) {
                nbt.setTag(key, GSON.fromJson(jsonElement, NBTTagList.class));
            }
            else if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (jsonPrimitive.isNumber() || jsonPrimitive.isBoolean()) {
                    nbt.setTag(key, GSON.fromJson(jsonPrimitive, NBTPrimitive.class));
                }
                else {
                    nbt.setString(key, jsonPrimitive.getAsString());
                }
            }
            else {
                nbt.setString(key, jsonObject.toString());
            }
        });

        return nbt;
    }

    @Override
    public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        src.getKeySet().forEach(key -> {
            NBTBase nbt = src.getTag(key);
            switch (nbt.getId()) {
                case NBT.TAG_BYTE:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getByte());
                    break;
                case NBT.TAG_BYTE_ARRAY:
                    jsonObject.add(key, GSON.toJsonTree(nbt, NBTTagByteArray.class));
                    break;
                case NBT.TAG_COMPOUND:
                    jsonObject.add(key, GSON.toJsonTree(nbt, NBTTagCompound.class));
                    break;
                case NBT.TAG_DOUBLE:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getDouble());
                    break;
                case NBT.TAG_FLOAT:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getFloat());
                    break;
                case NBT.TAG_INT:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getInt());
                    break;
                case NBT.TAG_INT_ARRAY:
                    jsonObject.add(key, GSON.toJsonTree(nbt, NBTTagIntArray.class));
                    break;
                case NBT.TAG_LIST:
                    jsonObject.add(key, GSON.toJsonTree(nbt, NBTTagList.class));
                    break;
                case NBT.TAG_LONG:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getLong());
                    break;
                case NBT.TAG_SHORT:
                    jsonObject.addProperty(key, ((NBTPrimitive) nbt).getShort());
                    break;
                case NBT.TAG_STRING:
                    jsonObject.addProperty(key, ((NBTTagString) nbt).getString());
                    break;
            }
        });

        return jsonObject;
    }
}
