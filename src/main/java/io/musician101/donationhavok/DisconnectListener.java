package io.musician101.donationhavok;

import java.awt.Frame;
import java.util.Arrays;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class DisconnectListener {

    @SubscribeEvent
    public void disconnect(ClientDisconnectionFromServerEvent event) {
        Arrays.stream(Frame.getFrames()).forEach(Frame::dispose);
    }
}
