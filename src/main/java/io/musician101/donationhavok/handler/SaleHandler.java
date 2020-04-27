package io.musician101.donationhavok.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class SaleHandler {

    private static final SaleHandler SALE_HANDLER = new SaleHandler();

    public static void startSale(double discount, int saleLength) {
        SALE_HANDLER.start(discount, saleLength);
    }

    public static void stopSale() {
        SALE_HANDLER.stop();
    }

    public static double getDiscountedAmount(double tier) {
        return SALE_HANDLER.isRunning() ? tier / SALE_HANDLER.getDiscount() : tier;
    }

    private double discount = -1D;
    private int saleLength;
    private int startLength;

    private void broadcast(ITextComponent message) {
        LogicalSidedProvider workQueue = LogicalSidedProvider.WORKQUEUE;
        MinecraftServer server = workQueue.get(LogicalSide.SERVER);
        if (server.isDedicatedServer()) {
            server.getPlayerList().sendMessage(message);
        }
        else {
            workQueue.<Minecraft>get(LogicalSide.CLIENT).player.sendMessage(message);
        }
    }

    public double getDiscount() {
        return Math.max(discount, 1D);
    }

    public boolean isRunning() {
        return discount != -1D;
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.type == TickEvent.Type.SERVER && event.phase == Phase.START) {
            if (saleLength != startLength) {
                ITextComponent message = new StringTextComponent("The current sale will end in ").setStyle(new Style().setColor(TextFormatting.WHITE)).appendSibling(new StringTextComponent(saleLength / 20 + " seconds.").setStyle(new Style().setColor(TextFormatting.AQUA)));
                switch (saleLength) {
                    case 100:
                    case 200:
                    case 300:
                    case 600:
                    case 1200:
                        broadcast(message);
                        break;
                    default:
                        if (saleLength % 6000 == 0) {
                            broadcast(message);
                        }
                }
            }

            saleLength--;
            if (saleLength <= 0) {
                stop();
            }
        }
    }

    public void start(double discount, int saleLength) {
        if (isRunning()) {
            return;
        }

        this.discount = discount;
        this.saleLength = saleLength;
        this.startLength = saleLength;
        broadcast(new StringTextComponent("A sale has been triggered! All rewards are now ").setStyle(new Style().setColor(TextFormatting.WHITE)).appendSibling(new StringTextComponent((discount * 100) + "% off").setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new StringTextComponent(" for the next ").setStyle(new Style().setColor(TextFormatting.WHITE))).appendSibling(new StringTextComponent((saleLength / 20) + " seconds!").setStyle(new Style().setColor(TextFormatting.AQUA))));
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stop() {
        MinecraftForge.EVENT_BUS.unregister(this);
        discount = -1D;
        broadcast(new StringTextComponent("The sale has ended."));
    }
}
