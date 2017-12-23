package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokEntity;
import java.lang.reflect.Type;
import net.minecraft.nbt.NBTTagCompound;

import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.NBT;
import static io.musician101.donationhavok.util.json.JsonUtils.X_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Y_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Z_OFFSET;

public class HavokEntityTypeAdapter implements JsonDeserializer<HavokEntity>, JsonSerializer<HavokEntity> {

    @Override
    public HavokEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        double xOffset = jsonObject.get(X_OFFSET).getAsDouble();
        double yOffset = jsonObject.get(Y_OFFSET).getAsDouble();
        double zOffset = jsonObject.get(Z_OFFSET).getAsDouble();
        NBTTagCompound nbt = GSON.fromJson(jsonObject.getAsJsonObject(NBT), NBTTagCompound.class);
        return new HavokEntity(delay, xOffset, yOffset, zOffset, nbt);
    }

    @Override
    public JsonElement serialize(HavokEntity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(X_OFFSET, src.getXOffset());
        jsonObject.addProperty(Y_OFFSET, src.getYOffset());
        jsonObject.addProperty(Z_OFFSET, src.getZOffset());
        jsonObject.add(NBT, GSON.toJsonTree(src.getNBTTagCompound(), NBTTagCompound.class));
        return jsonObject;
    }
}
