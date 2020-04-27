package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants.NBT;

public class ListNBTSerializer extends BaseSerializer<ListNBT> {

    @Override
    public ListNBT deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();
        if (jsonArray.size() == 0) {
            return new ListNBT();
        }

        JsonElement jsonElement = jsonArray.get(0);
        ListNBT nbtTagList = new ListNBT();
        if (jsonElement.isJsonArray()) {
            jsonArray.forEach(array -> nbtTagList.add(deserialize(array, typeOfT, context)));
        }
        else if (jsonElement.isJsonObject()) {
            jsonArray.forEach(object -> nbtTagList.add(context.deserialize(object, CompoundNBT.class)));
        }
        else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isBoolean() || jsonPrimitive.isNumber()) {
                StreamSupport.stream(jsonArray.spliterator(), false).forEach(jp -> nbtTagList.add(context.deserialize(jp, NumberNBT.class)));
            }
            else {
                jsonArray.forEach(s -> nbtTagList.add(new StringNBT(s.getAsString())));
            }
        }

        return nbtTagList;
    }

    @Override
    public JsonElement serialize(ListNBT src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        if (src.getId() == NBT.TAG_LIST) {
            src.forEach(tag -> {
                switch (tag.getId()) {
                    case NBT.TAG_BYTE:
                        jsonArray.add(((NumberNBT) tag).getByte());
                        break;
                    case NBT.TAG_BYTE_ARRAY:
                        jsonArray.add(context.serialize(tag, ByteArrayNBT.class));
                        break;
                    case NBT.TAG_COMPOUND:
                        jsonArray.add(context.serialize(tag, CompoundNBT.class));
                        break;
                    case NBT.TAG_DOUBLE:
                        jsonArray.add(((NumberNBT) tag).getDouble());
                        break;
                    case NBT.TAG_FLOAT:
                        jsonArray.add(((NumberNBT) tag).getFloat());
                        break;
                    case NBT.TAG_INT:
                        jsonArray.add(((NumberNBT) tag).getInt());
                        break;
                    case NBT.TAG_INT_ARRAY:
                        jsonArray.add(context.serialize(tag, IntArrayNBT.class));
                        break;
                    case NBT.TAG_LIST:
                        jsonArray.add(serialize((ListNBT) tag, typeOfSrc, context));
                        break;
                    case NBT.TAG_LONG:
                        jsonArray.add(((NumberNBT) tag).getLong());
                        break;
                    case NBT.TAG_SHORT:
                        jsonArray.add(((NumberNBT) tag).getShort());
                        break;
                    case NBT.TAG_STRING:
                        jsonArray.add(tag.getString());
                        break;
                }
            });
        }

        return jsonArray;
    }
}
