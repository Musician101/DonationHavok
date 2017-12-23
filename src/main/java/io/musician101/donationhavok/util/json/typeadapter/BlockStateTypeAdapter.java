package io.musician101.donationhavok.util.json.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import io.musician101.donationhavok.DonationHavok;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import static io.musician101.donationhavok.util.json.JsonUtils.ID;

public class BlockStateTypeAdapter implements JsonDeserializer<IBlockState>, JsonSerializer<IBlockState> {

    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String id = jsonObject.get(ID).getAsString();
        Block block = Block.getBlockFromName(id);
        if (block == null) {
            throw new JsonSyntaxException("Could not find block with id " + id);
        }

        IBlockState blockState = block.getDefaultState();
        for (IProperty<?> key : blockState.getPropertyKeys().stream().filter(key -> jsonObject.has(key.getName())).collect(Collectors.toList())) {
            if (key instanceof PropertyInteger) {
                blockState.withProperty((PropertyInteger) key, jsonObject.get(key.getName()).getAsInt());
            }
            else if (key instanceof PropertyBool) {
                blockState.withProperty((PropertyBool) key, jsonObject.get(key.getName()).getAsBoolean());
            }
            else if (key instanceof PropertyDirection) {
                EnumFacing facing = EnumFacing.byName(jsonObject.get("facing").getAsString());
                if (facing != null) {
                    blockState.withProperty((PropertyDirection) key, facing);
                }
            }
            else if (key instanceof PropertyEnum) {
                String name = jsonObject.get(key.getName()).getAsString();
                if (key == BlockBed.PART) {
                    blockState = parseEnum(blockState, BlockBed.PART, name);
                }
                else if (key == BlockCarpet.COLOR) {
                    blockState = parseEnum(blockState, BlockCarpet.COLOR, name);
                }
                else if (key == BlockColored.COLOR) {
                    blockState = parseEnum(blockState, BlockColored.COLOR, name);
                }
                else if (key == BlockConcretePowder.COLOR) {
                    blockState = parseEnum(blockState, BlockConcretePowder.COLOR, name);
                }
                else if (key == BlockDirt.VARIANT) {
                    blockState = parseEnum(blockState, BlockDirt.VARIANT, name);
                }
                else if (key == BlockDoor.HINGE) {
                    blockState = parseEnum(blockState, BlockDoor.HINGE, name);
                }
                else if (key == BlockDoor.HALF) {
                    blockState = parseEnum(blockState, BlockDoublePlant.HALF, name);
                }
                else if (key == BlockDoublePlant.VARIANT) {
                    blockState = parseEnum(blockState, BlockDoublePlant.VARIANT, name);
                }
                else if (key == BlockDoublePlant.HALF) {
                    blockState = parseEnum(blockState, BlockDoublePlant.HALF, name);
                }
                else if (key == Blocks.RED_FLOWER.getTypeProperty()) {
                    blockState = parseEnum(blockState, (PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.RED_FLOWER.getTypeProperty(), name);
                }
                else if (key == Blocks.YELLOW_FLOWER.getTypeProperty()) {
                    blockState = parseEnum(blockState, (PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.YELLOW_FLOWER.getTypeProperty(), name);
                }
                else if (key == BlockFlowerPot.CONTENTS) {
                    blockState = parseEnum(blockState, BlockFlowerPot.CONTENTS, name);
                }
                else if (key == BlockHugeMushroom.VARIANT) {
                    blockState = parseEnum(blockState, BlockHugeMushroom.VARIANT, name);
                }
                else if (key == BlockLever.FACING) {
                    blockState = parseEnum(blockState, BlockLever.FACING, name);
                }
                else if (key == BlockLog.LOG_AXIS) {
                    blockState = parseEnum(blockState, BlockLog.LOG_AXIS, name);
                }
                else if (key == BlockNewLeaf.VARIANT) {
                    blockState = parseEnum(blockState, BlockNewLeaf.VARIANT, name);
                }
                else if (key == BlockNewLog.VARIANT) {
                    blockState = parseEnum(blockState, BlockNewLog.VARIANT, name);
                }
                else if (key == BlockOldLog.VARIANT) {
                    blockState = parseEnum(blockState, BlockOldLog.VARIANT, name);
                }
                else if (key == BlockOldLeaf.VARIANT) {
                    blockState = parseEnum(blockState, BlockOldLeaf.VARIANT, name);
                }
                else if (key == BlockOldLeaf.VARIANT) {
                    blockState = parseEnum(blockState, BlockOldLeaf.VARIANT, name);
                }
                else if (key == BlockPistonExtension.TYPE) {
                    blockState = parseEnum(blockState, BlockPistonExtension.TYPE, name);
                }
                else if (key == BlockPlanks.VARIANT) {
                    blockState = parseEnum(blockState, BlockPlanks.VARIANT, name);
                }
                else if (key == BlockPortal.AXIS) {
                    blockState = parseEnum(blockState, BlockPortal.AXIS, name);
                }
                else if (key == BlockPrismarine.VARIANT) {
                    blockState = parseEnum(blockState, BlockPrismarine.VARIANT, name);
                }
                else if (key == BlockPurpurSlab.VARIANT) {
                    blockState = parseEnum(blockState, BlockPurpurSlab.VARIANT, name);
                }
                else if (key == BlockQuartz.VARIANT) {
                    blockState = parseEnum(blockState, BlockQuartz.VARIANT, name);
                }
                else if (key == BlockRail.SHAPE) {
                    blockState = parseEnum(blockState, BlockRail.SHAPE, name);
                }
                else if (key == BlockRailDetector.SHAPE) {
                    blockState = parseEnum(blockState, BlockRailDetector.SHAPE, name);
                }
                else if (key == BlockRailPowered.SHAPE) {
                    blockState = parseEnum(blockState, BlockRailPowered.SHAPE, name);
                }
                else if (key == BlockRedSandstone.TYPE) {
                    blockState = parseEnum(blockState, BlockRedSandstone.TYPE, name);
                }
                else if (key == BlockRedstoneComparator.MODE) {
                    blockState = parseEnum(blockState, BlockRedstoneComparator.MODE, name);
                }
                else if (key == BlockRedstoneWire.NORTH) {
                    blockState = parseEnum(blockState, BlockRedstoneWire.NORTH, name);
                }
                else if (key == BlockRedstoneWire.EAST) {
                    blockState = parseEnum(blockState, BlockRedstoneWire.EAST, name);
                }
                else if (key == BlockRedstoneWire.SOUTH) {
                    blockState = parseEnum(blockState, BlockRedstoneWire.SOUTH, name);
                }
                else if (key == BlockRedstoneWire.WEST) {
                    blockState = parseEnum(blockState, BlockRedstoneWire.WEST, name);
                }
                else if (key == BlockRotatedPillar.AXIS) {
                    blockState = parseEnum(blockState, BlockRotatedPillar.AXIS, name);
                }
                else if (key == BlockSand.VARIANT) {
                    blockState = parseEnum(blockState, BlockSand.VARIANT, name);
                }
                else if (key == BlockSandStone.TYPE) {
                    blockState = parseEnum(blockState, BlockSandStone.TYPE, name);
                }
                else if (key == BlockSapling.TYPE) {
                    blockState = parseEnum(blockState, BlockSapling.TYPE, name);
                }
                else if (key == BlockSilverfish.VARIANT) {
                    blockState = parseEnum(blockState, BlockSilverfish.VARIANT, name);
                }
                else if (key == BlockSlab.HALF) {
                    blockState = parseEnum(blockState, BlockSlab.HALF, name);
                }
                else if (key == BlockStainedGlass.COLOR) {
                    blockState = parseEnum(blockState, BlockStainedGlass.COLOR, name);
                }
                else if (key == BlockStainedGlassPane.COLOR) {
                    blockState = parseEnum(blockState, BlockStainedGlassPane.COLOR, name);
                }
                else if (key == BlockStairs.HALF) {
                    blockState = parseEnum(blockState, BlockStairs.HALF, name);
                }
                else if (key == BlockStairs.SHAPE) {
                    blockState = parseEnum(blockState, BlockStairs.SHAPE, name);
                }
                else if (key == BlockStone.VARIANT) {
                    blockState = parseEnum(blockState, BlockStone.VARIANT, name);
                }
                else if (key == BlockStoneBrick.VARIANT) {
                    blockState = parseEnum(blockState, BlockStoneBrick.VARIANT, name);
                }
                else if (key == BlockStoneSlab.VARIANT) {
                    blockState = parseEnum(blockState, BlockStoneSlab.VARIANT, name);
                }
                else if (key == BlockStoneSlabNew.VARIANT) {
                    blockState = parseEnum(blockState, BlockStoneSlabNew.VARIANT, name);
                }
                else if (key == BlockStructure.MODE) {
                    blockState = parseEnum(blockState, BlockStructure.MODE, name);
                }
                else if (key == BlockTallGrass.TYPE) {
                    blockState = parseEnum(blockState, BlockTallGrass.TYPE, name);
                }
                else if (key == BlockTrapDoor.HALF) {
                    blockState = parseEnum(blockState, BlockTrapDoor.HALF, name);
                }
                else if (key == BlockWall.VARIANT) {
                    blockState = parseEnum(blockState, BlockWall.VARIANT, name);
                }
                else if (key == BlockWoodSlab.VARIANT) {
                    blockState = parseEnum(blockState, BlockStructure.MODE, name);
                }
            }
            else {
                DonationHavok.INSTANCE.getLogger().info(key.getName() + " is not a recognized property name.");
            }
        }

        return blockState;
    }

    private static <P extends PropertyEnum<T>, T extends Enum<T> & IStringSerializable> IBlockState parseEnum(IBlockState blockState, P property, String valueName) {
        return property.getAllowedValues().stream().filter(value -> valueName.equals(value.getName())).findFirst().map(value -> blockState.withProperty(property, value)).orElse(blockState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", Block.REGISTRY.getNameForObject(src.getBlock()).toString());
        src.getPropertyKeys().forEach(key -> {
            if (key instanceof PropertyInteger) {
                jsonObject.addProperty(key.getName(), src.getValue((PropertyInteger) key));
            }
            else if (key instanceof PropertyBool) {
                jsonObject.addProperty(key.getName(), src.getValue((PropertyBool) key));
            }
            else if (key instanceof PropertyEnum) {
                jsonObject.addProperty(key.getName(), ((IStringSerializable) src.getValue((PropertyEnum) key)).getName());
            }
        });

        return jsonObject;
    }
}
