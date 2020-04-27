package io.musician101.donationhavok.handler.havok;

import io.musician101.donationhavok.DonationHavok;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public abstract class Havok {

    private final int delay;

    Havok(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public abstract void wreak(PlayerEntity player, BlockPos originalPos);

    final void wreak(String taskName, Runnable runnable) {
        DonationHavok.getInstance().getScheduler().scheduleTask(taskName, delay, runnable);
    }
}
