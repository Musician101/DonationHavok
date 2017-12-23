package io.musician101.donationhavok.havok;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.server.FMLServerHandler;

public class HavokMessage extends Havok {

    private final boolean broadcast;
    private final ITextComponent message;

    public HavokMessage() {
        super(0);
        this.broadcast = true;
        this.message = new TextComponentString("Hey look, a default message :D");
    }

    public HavokMessage(int delay, boolean broadcast, ITextComponent message) {
        super(delay);
        this.broadcast = broadcast;
        this.message = message;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public boolean broadcastEnabled() {
        return broadcast;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        wreak("HavokMessage-Delay:" + getDelay(), () -> {
            if (broadcast && player.getEntityWorld().isRemote) {
                FMLServerHandler.instance().getServer().getPlayerList().sendMessage(message, false);
            }
            else {
                player.sendMessage(message);
            }
        });
    }
}
