package io.musician101.donationhavok.network.message;

import io.musician101.donationhavok.DonationHavok;
import io.netty.buffer.ByteBuf;
import java.awt.Frame;
import java.util.Arrays;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CloseGUIMessage implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(0);
    }

    public static class MessageHandler implements IMessageHandler<CloseGUIMessage, IMessage> {

        @Override
        public IMessage onMessage(CloseGUIMessage message, MessageContext ctx) {
            Arrays.stream(Frame.getFrames()).forEach(Frame::dispose);
            DonationHavok.INSTANCE.getLogger().warn("test");
            return null;
        }
    }
}
