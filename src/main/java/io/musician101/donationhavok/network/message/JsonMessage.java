package io.musician101.donationhavok.network.message;

import com.google.gson.JsonObject;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.gui.ConfigGUI;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import static io.musician101.donationhavok.util.json.JsonUtils.GSON;

public class JsonMessage implements IMessage {

    @Nonnull
    private JsonObject jsonObject;

    public JsonMessage() {
        this.jsonObject = new JsonObject();
    }

    public JsonMessage(@Nonnull JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        jsonObject = GSON.fromJson(ByteBufUtils.readUTF8String(buf), JsonObject.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, jsonObject.toString());
    }

    public static class MessageHandler implements IMessageHandler<JsonMessage, IMessage> {

        @Override
        public IMessage onMessage(JsonMessage message, MessageContext ctx) {
            StreamLabsTracker slt = GSON.fromJson(message.jsonObject, StreamLabsTracker.class);
            if (ctx.side == Side.CLIENT) {
                new ConfigGUI(slt, false);
            }
            else {
                DonationHavok instance = DonationHavok.INSTANCE;
                EntityPlayerMP player = ctx.getServerHandler().player;
                try {
                    instance.saveConfig(slt);
                    player.sendMessage(new TextComponentString("Config saved."));
                }
                catch (IOException e) {
                    player.sendMessage(new TextComponentString("An error occurred while trying to save the config!"));
                    instance.getLogger().error("An error occurred while trying to save the config!");
                }
            }

            return null;
        }
    }
}
