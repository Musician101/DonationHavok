package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.lang.reflect.Type;
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
import net.minecraftforge.fml.common.FMLCommonHandler;

@TypeOf(HavokBlock.Serializer.class)
public class HavokBlock extends HavokOffset<Integer> {

    private final IBlockState blockState;
    private final NBTTagCompound nbtTagCompound;

    public HavokBlock() {
        super(0, 1, 0, 0);
        this.blockState = Blocks.CHEST.getDefaultState();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setTag("Items", items());
        this.nbtTagCompound = nbtTagCompound;
    }

    public HavokBlock(int delay, int xOffset, int yOffset, int zOffset, IBlockState blockState, NBTTagCompound nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.blockState = blockState;
        this.nbtTagCompound = nbtTagCompound;
    }

    private static void addItem(ItemStack defaultItemStack, int slot, NBTTagList list) {
        NBTTagCompound item = defaultItemStack.writeToNBT(new NBTTagCompound());
        item.setByte("Slot", (byte) slot);
        list.appendTag(item);
    }

    private static NBTTagList items() {
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

    public IBlockState getBlockState() {
        return blockState;
    }

    public NBTTagCompound getNBTTagCompound() {
        return nbtTagCompound;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        BlockPos blockPos = originalPos.add(getXOffset(), getYOffset(), getZOffset());
        if (DonationHavok.INSTANCE.getRewardsHandler().isReplaceable(player.getEntityWorld(), blockPos)) {
            wreak("HavokBlockDelay:" + getDelay(), () -> {
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
                world.setBlockState(blockPos, blockState);
                TileEntity tileEntity = blockState.getBlock().createTileEntity(world, blockState);
                if (tileEntity != null) {
                    tileEntity.readFromNBT(nbtTagCompound);
                    world.setTileEntity(blockPos, tileEntity);
                }
            });
        }
    }

    public static class Serializer extends BaseSerializer<HavokBlock> {

        @Override
        public HavokBlock deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            int xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_INT, 1);
            int yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_INT, 0);
            int zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_INT, 0);
            IBlockState blockState = deserialize(jsonObject, context, Keys.BLOCK_STATE, Blocks.CHEST.getDefaultState());
            NBTTagCompound defaultNBT = new NBTTagCompound();
            defaultNBT.setTag("Items", items());
            NBTTagCompound nbtTagCompound = deserialize(jsonObject, context, Keys.TILE_ENTITY, defaultNBT);
            return new HavokBlock(delay, xOffset, yOffset, zOffset, blockState, nbtTagCompound);
        }

        @Override
        public JsonElement serialize(HavokBlock src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_INT, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_INT, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_INT, src.getZOffset());
            serialize(jsonObject, context, Keys.BLOCK_STATE, src.getBlockState());
            serialize(jsonObject, context, Keys.TILE_ENTITY, src.getNBTTagCompound());
            return jsonObject;
        }
    }
}
