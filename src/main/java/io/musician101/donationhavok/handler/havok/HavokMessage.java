package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import static io.musician101.donationhavok.util.json.Keys.MESSAGE;

public class HavokMessage extends Havok {

    private final boolean broadcast;
    private final ITextComponent message;

    public HavokMessage() {
        super(0);
        this.broadcast = true;
        this.message = new StringTextComponent("Hey look, a default message :D");
    }

    public HavokMessage(int delay, boolean broadcast, ITextComponent message) {
        super(delay);
        this.broadcast = broadcast;
        this.message = message;
    }

    public boolean broadcastEnabled() {
        return broadcast;
    }

    public ITextComponent getMessage() {
        return message;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        wreak("HavokMessage-Delay:" + getDelay(), () -> LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getPlayerList().sendMessage(message, false));
    }

    public static class Serializer extends BaseSerializer<HavokMessage> {

        @Override
        public HavokMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            boolean broadcast = deserialize(jsonObject, context, Keys.BROADCAST, true);
            ITextComponent message = new ITextComponent.Serializer().deserialize(jsonObject.get(MESSAGE), typeOfT, context);
            return new HavokMessage(delay, broadcast, message);
        }

        @Override
        public JsonElement serialize(HavokMessage src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.BROADCAST, src.broadcastEnabled());
            jsonObject.add(MESSAGE, new ITextComponent.Serializer().serialize(src.getMessage(), typeOfSrc, context));
            return jsonObject;
        }
    }
}
