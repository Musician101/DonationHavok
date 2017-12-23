package io.musician101.donationhavok.havok;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HavokEntity extends DoubleOffsetHavok {

    private final NBTTagCompound nbtTagCompound;

    public HavokEntity() {
        super(0, 0D, 0D, 0D);
        this.nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("id", "minecraft:chicken");
        nbtTagCompound.setBoolean("CustomNameVisible", true);
        nbtTagCompound.setString("CustomName", "Duck");
    }

    public HavokEntity(int delay, double xOffset, double yOffset, double zOffset, NBTTagCompound nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.nbtTagCompound = nbtTagCompound;
        nbtTagCompound.removeTag("UUID");
        nbtTagCompound.removeTag("UUIDMost");
        nbtTagCompound.removeTag("UUIDLeast");
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        wreak("HavokEntity-Delay:" + getDelay(), () -> {
            World world = player.getEntityWorld();
            Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(nbtTagCompound.getString("id")), world);
            if (entity != null) {
                entity.readFromNBT(nbtTagCompound);
                entity.setPosition(originalPos.getX() + getXOffset(), originalPos.getY() + getYOffset(), originalPos.getZ() + getZOffset());
                world.spawnEntity(entity);
            }
        });
    }

    public NBTTagCompound getNBTTagCompound() {
        return nbtTagCompound;
    }
}
