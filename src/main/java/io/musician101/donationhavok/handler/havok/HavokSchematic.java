package io.musician101.donationhavok.handler.havok;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import io.musician101.donationhavok.util.json.adapter.TypeOf;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @deprecated This will most likely break when 1.13 hits.
 */
@Deprecated
@TypeOf(HavokSchematic.Serializer.class)
public class HavokSchematic extends HavokIntegerOffset {

    @Nonnull
    private final String relativePath;

    public HavokSchematic() {
        this(0, 0, 0, 0, "quack.schematic");
        File file = new File(DonationHavok.INSTANCE.getConfigDir(), "quack.schematic");
        if (file.exists()) {
            return;
        }

        try {
            Files.copy(getClass().getClassLoader().getResourceAsStream("quack.schematic"), file.toPath());
        }
        catch (IOException e) {
            DonationHavok.INSTANCE.getLogger().warn("Failed to save " + relativePath + ".");
        }
    }

    public HavokSchematic(int delay, int xOffset, int yOffset, int zOffset, @Nonnull String relativePath) {
        super(delay, xOffset, yOffset, zOffset);
        this.relativePath = relativePath;
    }

    @Override
    public void wreak(EntityPlayer player, BlockPos originalPos) {
        NBTTagCompound schematic;
        try (FileInputStream input = new FileInputStream(new File(DonationHavok.INSTANCE.getConfigDir(), relativePath))) {
            schematic = CompressedStreamTools.readCompressed(input);
        }
        catch (IOException e) {
            DonationHavok.INSTANCE.getLogger().warn(relativePath + " failed to load.");
            return;
        }

        if (schematic.getSize() == 0) {
            DonationHavok.INSTANCE.getLogger().warn(relativePath + "is empty.");
            return;
        }

        EnumFacing direction = EnumFacing.getDirectionFromEntityLiving(originalPos, player).getOpposite();
        short width = schematic.getShort("Width");
        short height = schematic.getShort("Height");
        short length = schematic.getShort("Length");
        Rotation rotation = getRotation(direction);
        Vec3i rotatedDimensions = rotateDimensions(rotation, width, height, length);

        byte[] blocks = schematic.getByteArray("Blocks");
        byte[] data = schematic.getByteArray("Data");

        NBTTagList tileEntities = schematic.getTagList("TileEntities", NBT.TAG_COMPOUND);

        int i = 0;
        short halfLength = (short) (rotatedDimensions.getZ() / 2);
        short halfWidth = (short) (rotatedDimensions.getX() / 2);
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        List<List<Runnable>> lines = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                List<Runnable> line = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int j = blocks[i];
                    if (j < 0) {
                        j = 128 + (128 + j);
                    }

                    Block b = Block.getBlockById(j);
                    if (b != Blocks.AIR) {
                        IBlockState blockState = b.getStateFromMeta(data[i]);
                        BlockPos rotatedPos = rotatePos(new BlockPos(x, y, z), rotation, rotatedDimensions);
                        BlockPos pos = originalPos.add(-(halfWidth - rotatedPos.getX()), rotatedPos.getY(), -(halfLength - rotatedPos.getZ())).add(getXOffset(), getYOffset(), getZOffset());
                        NBTTagCompound nbt = getTileEntity(x, y, z, tileEntities);
                        line.add(() -> {
                            world.playSound(null, pos, b.getSoundType(blockState, world, pos, null).getPlaceSound(), SoundCategory.BLOCKS, 1F, 1F);
                            world.setBlockState(pos, blockState.withRotation(rotation));
                            TileEntity tileEntity = b.createTileEntity(world, blockState);
                            if (tileEntity != null) {
                                tileEntity.readFromNBT(nbt);
                                tileEntity.rotate(rotation);
                                world.setTileEntity(pos, tileEntity);
                            }
                        });
                    }

                    i++;
                }

                lines.addAll(Lists.partition(line, 10));
            }
        }

        wreak("HavokSchematicDelay:" + getDelay(), () -> IntStream.range(0, lines.size()).forEach(j -> DonationHavok.INSTANCE.getScheduler().scheduleTask("HavokSchematicPartDelay:" + j, j, () -> lines.get(j).forEach(Runnable::run))));
    }

    private Vec3i rotateDimensions(Rotation rotation, int width, int height, int length) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case NONE:
                return new Vec3i(width, height, length);
            default:
                return new Vec3i(length, height, width);
        }
    }

    private BlockPos rotatePos(BlockPos blockPos, Rotation rotation, Vec3i dimensions) {
        int rotateCount = 0;
        switch (rotation) {
            case CLOCKWISE_90:
                rotateCount = 1;
                break;
            case CLOCKWISE_180:
                rotateCount = 2;
                break;
            case COUNTERCLOCKWISE_90:
                rotateCount = 3;
                break;
        }

        int x = blockPos.getX();
        int z = blockPos.getZ();
        for (int i = 0; i < rotateCount; i++) {
            int rx = dimensions.getX() - 1 - z;
            z = x;
            x = rx;
        }

        return new BlockPos(x, blockPos.getY(), z);
    }

    private Rotation getRotation(EnumFacing direction) {
        switch (direction) {
            case WEST:
                return Rotation.CLOCKWISE_90;
            case NORTH:
                return Rotation.CLOCKWISE_180;
            case EAST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    private NBTTagCompound getTileEntity(int x, int y, int z, NBTTagList tileEntities) {
        for (int i = 0; i < tileEntities.tagCount(); i++) {
            NBTTagCompound nbt = tileEntities.getCompoundTagAt(i);
            if (nbt.getInteger("x") == x && nbt.getInteger("y") == y && nbt.getInteger("z") == z) {
                return nbt;
            }
        }

        return new NBTTagCompound();
    }

    @Nonnull
    public String getRelativePath() {
        return relativePath;
    }

    public static class Serializer extends BaseSerializer<HavokSchematic> {

        @Override
        public HavokSchematic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String relativePath = deserialize(jsonObject, context, Keys.RELATIVE_PATH, "quack.schematic");
            int delay = deserialize(jsonObject, context, Keys.DELAY, 0);
            int xOffset = deserialize(jsonObject, context, Keys.X_OFFSET_INT, 0);
            int yOffset = deserialize(jsonObject, context, Keys.Y_OFFSET_INT, 0);
            int zOffset = deserialize(jsonObject, context, Keys.Z_OFFSET_INT, 0);
            return new HavokSchematic(delay, xOffset, yOffset, zOffset, relativePath);
        }

        @Override
        public JsonElement serialize(HavokSchematic src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.RELATIVE_PATH, src.getRelativePath());
            serialize(jsonObject, context, Keys.DELAY, src.getDelay());
            serialize(jsonObject, context, Keys.X_OFFSET_INT, src.getXOffset());
            serialize(jsonObject, context, Keys.Y_OFFSET_INT, src.getYOffset());
            serialize(jsonObject, context, Keys.Z_OFFSET_INT, src.getZOffset());
            return jsonObject;
        }
    }
}
