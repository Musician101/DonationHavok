package io.musician101.donationhavok.handler.havok;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

public class HavokBlock extends HavokIntegerOffset {

    private final BlockState blockState;
    private final CompoundNBT nbtTagCompound;

    public HavokBlock() {
        super(0, 1, 0, 0);
        this.blockState = Blocks.CHEST.getDefaultState();
        CompoundNBT nbtTagCompound = new CompoundNBT();
        nbtTagCompound.put("Items", items());
        this.nbtTagCompound = nbtTagCompound;
    }

    public HavokBlock(int delay, int xOffset, int yOffset, int zOffset, BlockState blockState, CompoundNBT nbtTagCompound) {
        super(delay, xOffset, yOffset, zOffset);
        this.blockState = blockState;
        this.nbtTagCompound = nbtTagCompound;
    }

    private static void addItem(ItemStack defaultItemStack, int slot, ListNBT list) {
        CompoundNBT item = defaultItemStack.write(new CompoundNBT());
        item.putByte("Slot", (byte) slot);
        list.add(item);
    }

    private static ListNBT items() {
        ItemStack stone = new ItemStack(Blocks.STONE);
        stone.setDisplayName(new StringTextComponent("Oh Hai ;D"));
        ItemStack apple = new ItemStack(Items.APPLE);
        apple.setDisplayName(new StringTextComponent("Oh Hai ;D"));
        ListNBT items = new ListNBT();
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

    public BlockState getBlockState() {
        return blockState;
    }

    public CompoundNBT getCompoundNBT() {
        return nbtTagCompound;
    }

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        BlockPos blockPos = originalPos.add(getXOffset(), getYOffset(), getZOffset());
        if (DonationHavok.getInstance().getConfig().getGeneralConfig().isReplaceable(player.getEntityWorld(), blockPos)) {
            wreak("HavokBlockDelay:" + getDelay(), () -> {
                World world = LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getWorld(player.dimension);
                world.playSound(null, blockPos, blockState.getBlock().getSoundType(blockState, world, blockPos, null).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
                world.setBlockState(blockPos, blockState);
                TileEntity tileEntity = blockState.getBlock().createTileEntity(blockState, world);
                if (tileEntity != null) {
                    tileEntity.read(nbtTagCompound);
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
            BlockState blockState = deserialize(jsonObject, context, Keys.BLOCK_STATE, Blocks.CHEST.getDefaultState());
            CompoundNBT defaultNBT = new CompoundNBT();
            defaultNBT.put("Items", items());
            CompoundNBT nbtTagCompound = deserialize(jsonObject, context, Keys.TILE_ENTITY, defaultNBT);
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
            serialize(jsonObject, context, Keys.TILE_ENTITY, src.getCompoundNBT());
            return jsonObject;
        }
    }
}
