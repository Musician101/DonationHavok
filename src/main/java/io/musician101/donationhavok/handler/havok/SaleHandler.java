package io.musician101.donationhavok.handler.havok;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

class SaleHandler {

    private double discount = -1D;
    private int saleLength;
    private int startLength;

    private void broadcast(ITextComponent message) {
        FMLCommonHandler fml = FMLCommonHandler.instance();
        if (fml.getSide().isServer()) {
            fml.getMinecraftServerInstance().getPlayerList().sendMessage(message);
        }
        else {
            Minecraft.getMinecraft().player.sendMessage(message);
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
        if (event.side == Side.SERVER && event.type == TickEvent.Type.SERVER && event.phase == Phase.START) {
            if (saleLength != startLength) {
                ITextComponent message = new TextComponentString("The current sale will end in ").setStyle(new Style().setColor(TextFormatting.WHITE)).appendSibling(new TextComponentString(saleLength / 20 + " seconds.").setStyle(new Style().setColor(TextFormatting.AQUA)));
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
                stopSale();
            }
        }
    }

    public void startSale(double discount, int saleLength) {
        if (isRunning()) {
            return;
        }

        this.discount = discount;
        this.saleLength = saleLength;
        this.startLength = saleLength;
        broadcast(new TextComponentString("A sale has been triggered! All rewards are now ").setStyle(new Style().setColor(TextFormatting.WHITE)).appendSibling(new TextComponentString((discount * 100) + "% off").setStyle(new Style().setColor(TextFormatting.AQUA))).appendSibling(new TextComponentString(" for the next ").setStyle(new Style().setColor(TextFormatting.WHITE))).appendSibling(new TextComponentString((saleLength / 20) + " seconds!").setStyle(new Style().setColor(TextFormatting.AQUA))));
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stopSale() {
        MinecraftForge.EVENT_BUS.unregister(this);
        discount = -1D;
        broadcast(new TextComponentString("The sale has ended."));
    }
}
