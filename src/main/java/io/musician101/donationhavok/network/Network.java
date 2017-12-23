package io.musician101.donationhavok.network;

import io.musician101.donationhavok.Reference;
import io.musician101.donationhavok.network.message.CloseGUIMessage;
import io.musician101.donationhavok.network.message.JsonMessage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Network {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
    private static int id = 0;

    private Network() {

    }

    public static void init() {
        INSTANCE.registerMessage(JsonMessage.MessageHandler.class, JsonMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(JsonMessage.MessageHandler.class, JsonMessage.class, id++, Side.SERVER);
        INSTANCE.registerMessage(CloseGUIMessage.MessageHandler.class, CloseGUIMessage.class, id++, Side.CLIENT);
    }
}
