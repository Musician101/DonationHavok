package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.havok.HavokBlock;
import java.lang.reflect.Type;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

import static io.musician101.donationhavok.util.json.JsonUtils.BLOCK_STATE;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.TILE_ENTITY;
import static io.musician101.donationhavok.util.json.JsonUtils.X_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Y_OFFSET;
import static io.musician101.donationhavok.util.json.JsonUtils.Z_OFFSET;

public class HavokBlockTypeAdapter implements JsonDeserializer<HavokBlock>, JsonSerializer<HavokBlock> {

    @Override
    public HavokBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int delay = jsonObject.get(DELAY).getAsInt();
        int xOffset = jsonObject.get(X_OFFSET).getAsInt();
        int yOffset = jsonObject.get(Y_OFFSET).getAsInt();
        int zOffset = jsonObject.get(Z_OFFSET).getAsInt();
        IBlockState blockState = GSON.fromJson(jsonObject.getAsJsonObject(BLOCK_STATE), IBlockState.class);
        NBTTagCompound nbtTagCompound = GSON.fromJson(jsonObject.getAsJsonObject(TILE_ENTITY), NBTTagCompound.class);
        return new HavokBlock(delay, xOffset, yOffset, zOffset, blockState, nbtTagCompound);
    }

    @Override
    public JsonElement serialize(HavokBlock src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DELAY, src.getDelay());
        jsonObject.addProperty(X_OFFSET, src.getXOffset());
        jsonObject.addProperty(Y_OFFSET, src.getYOffset());
        jsonObject.addProperty(Z_OFFSET, src.getZOffset());
        jsonObject.add(BLOCK_STATE, GSON.toJsonTree(src.getBlockState(), IBlockState.class));
        jsonObject.add(TILE_ENTITY, GSON.toJsonTree(src.getNBTTagCompound(), NBTTagCompound.class));
        return jsonObject;
    }
}
