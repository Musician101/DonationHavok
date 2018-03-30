package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.list.ListSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class NonReplaceableBlocksSerializer implements ListSerializer<IBlockState> {

    @Override
    public List<IBlockState> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsString).map(ResourceLocation::new).map(Block.REGISTRY::getObject).map(Block::getDefaultState).collect(Collectors.toList());
    }

    @Override
    public JsonElement serialize(List<IBlockState> src, Type typeOfSrc, JsonSerializationContext context) {
        return src.stream().map(IBlockState::getBlock).map(Block.REGISTRY::getNameForObject).map(ResourceLocation::toString).map(JsonPrimitive::new).collect(Keys.jsonArrayCollector());
    }
}
