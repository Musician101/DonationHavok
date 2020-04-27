package io.musician101.donationhavok.util.json.adapter.nbt;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.ShortNBT;
import net.minecraftforge.common.util.Constants.NBT;

public class NumberNBTSerializer extends BaseSerializer<NumberNBT> {

    @Override
    public NumberNBT deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
        if (jsonPrimitive.isNumber()) {
            Number number = jsonPrimitive.getAsNumber();
            if (number instanceof Byte) {
                return new ByteNBT(number.byteValue());
            }
            else if (number instanceof Double) {
                return new DoubleNBT(number.doubleValue());
            }
            else if (number instanceof Float) {
                return new FloatNBT(number.floatValue());
            }
            else if (number instanceof Long) {
                return new LongNBT(number.longValue());
            }
            else if (number instanceof Short) {
                return new ShortNBT(number.shortValue());
            }
            else {
                return new IntNBT(number.intValue());
            }
        }
        else if (jsonPrimitive.isBoolean()) {
            return new ByteNBT((byte) (jsonPrimitive.getAsBoolean() ? 1 : 0));
        }

        throw new JsonParseException(json.toString() + " is not a number or boolean!");
    }

    @Override
    public JsonElement serialize(NumberNBT src, Type typeOfSrc, JsonSerializationContext context) {
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
