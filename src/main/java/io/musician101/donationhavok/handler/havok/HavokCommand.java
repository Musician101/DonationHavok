package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.havok.HavokCommand.Serializer;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class HavokCommand extends Havok {

    private final String command;

    public HavokCommand() {
        super(0);
        this.command = "/say Quack \\_o<";
    }

    public HavokCommand(int delay, String command) {
        super(delay);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        MinecraftServer server = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        server.getCommandManager().handleCommand(server.getCommandSource(), command);
    }

    public static class Serializer extends BaseSerializer<HavokCommand> {

        @Override
        public HavokCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            String command = deserialize(jsonObject, context, Keys.COMMAND, "/say Quack \\_o<");
            return new HavokCommand(delay, command);
        }

        @Override
        public JsonElement serialize(HavokCommand src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.COMMAND, src.getCommand());
            return jsonObject;
        }
    }
}
