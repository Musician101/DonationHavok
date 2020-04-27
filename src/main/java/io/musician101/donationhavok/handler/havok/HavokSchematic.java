package io.musician101.donationhavok.handler.havok;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.Reference;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.apache.logging.log4j.Logger;

public class HavokSchematic extends HavokIntegerOffset {

    @Nonnull
    private final String relativePath;

    public HavokSchematic() {
        this(0, 0, 0, 0, "quack.schem");
        File file = new File("config/" + Reference.MOD_ID + "/quack.schem");
        if (file.exists()) {
            return;
        }

        try {
            Files.copy(ClassLoader.getSystemClassLoader().getResourceAsStream("quack.schem"), file.toPath());
        }
        catch (IOException e) {
            DonationHavok.getInstance().getLogger().warn("Failed to save " + relativePath + ".");
        }
    }

    public HavokSchematic(int delay, int xOffset, int yOffset, int zOffset, @Nonnull String relativePath) {
        super(delay, xOffset, yOffset, zOffset);
        this.relativePath = relativePath;
    }

    @Nonnull
    public String getRelativePath() {
        return relativePath;
    }

    private Rotation getRotation(Direction direction) {
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

    private CompoundNBT getTileEntity(int x, int y, int z, ListNBT tileEntities) {
        for (int i = 0; i < tileEntities.size(); i++) {
            CompoundNBT nbt = tileEntities.getCompound(i);
            if (nbt.getInt("x") == x && nbt.getInt("y") == y && nbt.getInt("z") == z) {
                return nbt;
            }
        }

        return new CompoundNBT();
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

    @Override
    public void wreak(PlayerEntity player, BlockPos originalPos) {
        Logger logger = DonationHavok.getInstance().getLogger();
        CompoundNBT schematic;
        try (FileInputStream input = new FileInputStream(new File("config/" + Reference.MOD_ID + "/" + relativePath))) {
            schematic = CompressedStreamTools.readCompressed(input);
        }
        catch (IOException e) {
            logger.warn(relativePath + " failed to load.");
            return;
        }

        if (schematic.size() == 0) {
            logger.warn(relativePath + " is empty.");
            return;
        }

        Optional<Direction> facingOptional = Stream.of(Direction.getFacingDirections(player)).filter(Objects::nonNull).filter(facing -> facing.getAxis() != Axis.Y).findFirst();
        if (!facingOptional.isPresent()) {
            logger.error(relativePath + " failed to parse Direction for player " + player.getName());
            return;
        }

        Direction direction = facingOptional.get();
        short width = schematic.getShort("Width");
        short height = schematic.getShort("Height");
        short length = schematic.getShort("Length");
        Rotation rotation = getRotation(direction);
        Vec3i rotatedDimensions = rotateDimensions(rotation, width, height, length);

        int paletteMax = schematic.getInt("PaletteMax");
        CompoundNBT paletteNBT = schematic.getCompound("Palette");
        if (paletteMax != paletteNBT.size()) {
            logger.warn(relativePath + " PaletteMax does not match actual palette size.");
            return;
        }

        Map<Integer, BlockState> palette = new HashMap<>();
        for (String part : paletteNBT.keySet()) {
            int id = paletteNBT.getInt(part);
            BlockStateParser parser = new BlockStateParser(new StringReader(part), true);
            try {
                parser.readBlock();
            }
            catch (CommandSyntaxException e) {
                logger.warn(relativePath + " contains invalid BlockState: " + part);
                return;
            }

            BlockState blockState = parser.getState();
            palette.put(id, blockState);
        }

        byte[] blockData = schematic.getByteArray("BlockData");
        ListNBT tileEntities = schematic.getList("TileEntities", NBT.TAG_COMPOUND);
        int index = 0;
        int i = 0;
        int value;
        int varIntLength;
        short halfLength = (short) (rotatedDimensions.getZ() / 2);
        short halfWidth = (short) (rotatedDimensions.getX() / 2);
        World world = LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getWorld(player.dimension);
        List<Runnable> blocks = new ArrayList<>();
        while (i < blockData.length) {
            value = 0;
            varIntLength = 0;
            while (true) {
                value |= (blockData[i] & 127) << (varIntLength++ * 7);
                if (varIntLength > 5) {
                    logger.warn(relativePath + " possibly contains corrupted data");
                    return;
                }

                if ((blockData[i] & 128) != 128) {
                    i++;
                    break;
                }
                i++;
            }

            int x = (index % (width * length)) % width;
            int y = index / (width * length);
            int z = (index % (width * length)) / width;
            BlockState blockState = palette.get(value);
            BlockPos rotatedPos = rotatePos(new BlockPos(x, y, z), rotation, rotatedDimensions);
            BlockPos pos = originalPos.add(-(halfWidth - rotatedPos.getX()), rotatedPos.getY(), -(halfLength - rotatedPos.getZ())).add(getXOffset(), getYOffset(), getZOffset());
            CompoundNBT nbt = getTileEntity(x, y, z, tileEntities);
            blocks.add(() -> {
                world.playSound(null, pos, blockState.getSoundType(world, pos, null).getPlaceSound(), SoundCategory.MASTER, 1F, 1F);
                world.setBlockState(pos, blockState.rotate(world, pos, rotation));
                TileEntity tileEntity = blockState.createTileEntity(world);
                if (tileEntity != null) {
                    tileEntity.read(nbt);
                    tileEntity.rotate(rotation);
                    world.setTileEntity(pos, tileEntity);
                }
            });
            index++;
        }


        List<List<Runnable>> lines = Lists.partition(blocks, 10);
        wreak("HavokSchematicDelay:" + getDelay(), () -> IntStream.range(0, lines.size()).forEach(j -> DonationHavok.getInstance().getScheduler().scheduleTask("HavokSchematicPartDelay:" + j, j, () -> lines.get(j).forEach(Runnable::run))));
    }

    public static class Serializer extends BaseSerializer<HavokSchematic> {

        @Override
        public HavokSchematic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String relativePath = deserialize(jsonObject, context, Keys.RELATIVE_PATH, "quack.schem");
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
