package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.util.json.adapter.AdapterOf;
import io.musician101.donationhavok.util.json.adapter.AdapterType;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.util.Constants.NBT;

@AdapterOf
public class NBTPrimitiveSerializer extends BaseSerializer<NBTPrimitive> {

    @AdapterType
    public static final TypeToken<NBTPrimitive> TYPE = TypeToken.get(NBTPrimitive.class);

    @Override
    public NBTPrimitive deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            if (number instanceof Byte) {
                return new NBTTagByte(number.byteValue());
            }
            else if (number instanceof Double) {
                return new NBTTagDouble(number.doubleValue());
            }
            else if (number instanceof Float) {
                return new NBTTagFloat(number.floatValue());
            }
            else if (number instanceof Long) {
                return new NBTTagLong(number.longValue());
            }
            else if (number instanceof Short) {
                return new NBTTagShort(number.shortValue());
            }
            else {
                return new NBTTagInt(number.intValue());
            }
        }
        else if (jsonPrimitive.isBoolean()) {
            return new NBTTagByte((byte) (jsonPrimitive.getAsBoolean() ? 1 : 0));
        }

        throw new JsonParseException(json.toString() + " is not a number or boolean!");
    }

    @Override
    public JsonElement serialize(NBTPrimitive src, Type typeOfSrc, JsonSerializationContext context) {
        switch (src.getId()) {
            case NBT.TAG_BYTE:
                return new JsonPrimitive(src.getByte());
            case NBT.TAG_DOUBLE:
                return new JsonPrimitive(src.getDouble());
            case NBT.TAG_FLOAT:
                return new JsonPrimitive(src.getFloat());
            case NBT.TAG_INT:
                return new JsonPrimitive(src.getInt());
            case NBT.TAG_LONG:
                return new JsonPrimitive(src.getLong());
            case NBT.TAG_SHORT:
                return new JsonPrimitive(src.getShort());
        }

        throw new JsonParseException(src.toString() + " is not a number!");
    }
}
