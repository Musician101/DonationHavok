package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.musician101.donationhavok.DonationHavok;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class NonReplaceableBlocksTypeAdapter implements JsonDeserializer<List<IBlockState>>, JsonSerializer<List<IBlockState>> {

    @Override
    public List<IBlockState> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<IBlockState> list = new ArrayList<>();
        json.getAsJsonArray().forEach(id -> {
            Block block = Block.getBlockFromName(id.getAsString());
            if (block == null) {
                DonationHavok.INSTANCE.getLogger().warn("Block with id " + id.getAsString());
            }
            else {
                list.add(block.getDefaultState());
            }
        });
        return list;
    }

    @Override
    public JsonElement serialize(List<IBlockState> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        src.forEach(blockState -> jsonArray.add(Block.REGISTRY.getNameForObject(blockState.getBlock()).toString()));
        return jsonArray;
    }
}
