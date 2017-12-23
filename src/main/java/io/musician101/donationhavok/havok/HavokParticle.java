package io.musician101.donationhavok.havok;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class HavokParticle extends DoubleOffsetHavok {

    private final EnumParticleTypes particle;
    private final double xVelocity;
    private final double yVelocity;
    private final double zVelocity;

    public HavokParticle() {
        super(0, 0D, 0D, 0D);
        this.particle = EnumParticleTypes.EXPLOSION_HUGE;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.zVelocity = 0;
    }

    public HavokParticle(int delay, double xOffset, double yOffset, double zOffset, double xVelocity, double yVelocity, double zVelocity, EnumParticleTypes particle) {
        super(delay, xOffset, yOffset, zOffset);
        this.particle = particle;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        wreak("HavokParticle-Delay:" + getDelay(), () -> ((WorldServer) player.getEntityWorld()).spawnParticle(particle, true, originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset(), 1, xVelocity, yVelocity, zVelocity, 1D));
    }

    public EnumParticleTypes getParticle() {
        return particle;
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public double getZVelocity() {
        return zVelocity;
    }
}
