package io.musician101.donationhavok.havok;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class HavokSound extends DoubleOffsetHavok {

    private final float pitch;
    private final float volume;
    private final SoundEvent soundEvent;

    public HavokSound() {
        super(0, 0D, 0D, 0D);
        this.pitch = 1;
        this.volume = 1;
        this.soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.generic.explode"));
    }

    public HavokSound(int delay, double xOffset, double yOffset, double zOffset, float pitch, float volume, SoundEvent soundEvent) {
        super(delay, xOffset, yOffset, zOffset);
        this.soundEvent = soundEvent;
        this.pitch = pitch;
        this.volume = volume;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        wreak("HavokParticle-Delay:" + getDelay(), () -> ((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound(soundEvent.getRegistryName().toString(), SoundCategory.MASTER, originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset(), volume, pitch)));
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
}
