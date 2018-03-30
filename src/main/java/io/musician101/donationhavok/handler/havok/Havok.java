package io.musician101.donationhavok.handler.havok;

import io.musician101.donationhavok.DonationHavok;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public abstract class Havok {

    private final int delay;

    Havok(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public abstract void wreak(EntityPlayer player, BlockPos originalPos);

    final void wreak(String taskName, Runnable runnable) {
        DonationHavok.INSTANCE.getScheduler().scheduleTask(taskName, delay, runnable);
    }
}
