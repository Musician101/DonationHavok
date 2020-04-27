package io.musician101.donationhavok.util.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.Type;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.BlockStateParser;

public class BlockStateSerializer extends BaseSerializer<BlockState> {

    @Override
    public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        BlockStateParser parser = new BlockStateParser(new StringReader(json.getAsString()), false);
        try {
            parser.readBlock();
        }
        catch (CommandSyntaxException e) {
            return Blocks.AIR.getDefaultState();
        }

        return parser.getState();
    }

    @Override
    public JsonElement serialize(BlockState src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(BlockStateParser.toString(src));
    }
}
