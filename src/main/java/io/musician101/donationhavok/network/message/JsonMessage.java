package io.musician101.donationhavok.network.message;

import com.google.gson.JsonObject;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.ConfigGUI;
import io.musician101.donationhavok.handler.StreamLabsHandler;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.util.json.Keys;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

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

        @SuppressWarnings("MethodCallSideOnly")
        @Override
        public IMessage onMessage(JsonMessage message, MessageContext ctx) {
            DiscoveryHandler dh = Keys.DISCOVERY.deserializeFromParent(message.jsonObject).orElse(new DiscoveryHandler());
            HavokRewardsHandler hrh = Keys.GENERAL.deserializeFromParent(message.jsonObject).orElse(new HavokRewardsHandler());
            StreamLabsHandler slh = Keys.STREAM_LABS.deserializeFromParent(message.jsonObject).orElse(new StreamLabsHandler());
            TwitchHandler th = Keys.TWITCH.deserializeFromParent(message.jsonObject).orElse(new TwitchHandler());
            if (ctx.side == Side.CLIENT) {
                if (BaseGUI.isFrameActive("Donation Havok Configurator")) {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("The configurator is already open.").setStyle(new Style().setColor(TextFormatting.RED)));
                }
                else {
                    new ConfigGUI(dh, hrh, slh, th, false);
                }
            }
            else {
                DonationHavok instance = DonationHavok.INSTANCE;
                EntityPlayerMP player = ctx.getServerHandler().player;
                try {
                    instance.saveConfig(dh, hrh, slh, th);
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
