package io.musician101.donationhavok.havok;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HavokItemStack extends Havok {

    private final ItemStack itemStack;

    public HavokItemStack() {
        super(0);
        this.itemStack = new ItemStack(Items.WHEAT_SEEDS);
        itemStack.setStackDisplayName("Duck Feed");
    }

    public HavokItemStack(int delay, ItemStack itemStack) {
        super(delay);
        this.itemStack = itemStack;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        World world = player.getEntityWorld();
        EntityItem item = new EntityItem(world);
        item.setItem(itemStack);
        item.setPositionAndRotation(originalPos.getX(), originalPos.getY() + 1, originalPos.getZ(), 0, 0);
        world.spawnEntity(item);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
