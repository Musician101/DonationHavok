package io.musician101.donationhavok.havok;

import io.musician101.donationhavok.DonationHavok;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HavokBlock extends OffsetHavok<Integer> {

    private final IBlockState blockState;
    private final NBTTagCompound nbtTagCompound;

    public HavokBlock() {
        super(0, 1, 0, 0);
        this.blockState = Blocks.CHEST.getDefaultState();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setTag("Items", items());
        this.nbtTagCompound = nbtTagCompound;
    }

    private NBTTagList items() {
        ItemStack stone = new ItemStack(Blocks.STONE);
        stone.setStackDisplayName("Oh Hai ;D");
        ItemStack apple = new ItemStack(Items.APPLE);
        apple.setStackDisplayName("Oh Hai ;D");
        NBTTagList items = new NBTTagList();
        for (int x = 0; x < 27; x++) {
            switch (x) {
                case 0:
                case 2:
                case 4:
                case 8:
                case 9:
                case 13:
                case 14:
                case 16:
                case 17:
                case 18:
                case 20:
                case 22:
                case 26:
                    addItem(stone, x, items);
                    break;
                default:
                    addItem(apple, x, items);
                    break;
            }
        }

        return items;
    }

    private void addItem(ItemStack defaultItemStack, int slot, NBTTagList list) {
        NBTTagCompound item = defaultItemStack.writeToNBT(new NBTTagCompound());
        item.setByte("Slot", (byte) slot);
        list.appendTag(item);
    }

    public HavokBlock(int delay, int xOffset, int yOffset, int zOffset, IBlockState blockState, NBTTagCompound nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.blockState = blockState;
        this.nbtTagCompound = nbtTagCompound;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        BlockPos blockPos = originalPos.add(getXOffset(), getYOffset(), getZOffset());
        if (DonationHavok.INSTANCE.getStreamLabsTracker().isReplaceable(player.getEntityWorld(), blockPos)) {
            wreak("HavokBlockDelay:" + getDelay(), () -> {
                World world = player.getEntityWorld();
                world.setBlockState(blockPos, blockState);
                TileEntity tileEntity = blockState.getBlock().createTileEntity(world, blockState);
                if (tileEntity != null) {
                    tileEntity.readFromNBT(nbtTagCompound);
                    world.setTileEntity(blockPos, tileEntity);
                }
            });
        }
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public NBTTagCompound getNBTTagCompound() {
        return nbtTagCompound;
    }
}
