package io.musician101.donationhavok.gui.model.table;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
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

public class BlockStatePropertiesTableModel extends AbstractTableModel {

    private IBlockState blockState;

    public BlockStatePropertiesTableModel(IBlockState blockState) {
        this.blockState = blockState;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(IBlockState blockState) {
        this.blockState = blockState;
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Value";
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return blockState.getProperties().size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IProperty key = new ArrayList<>(blockState.getPropertyKeys()).get(rowIndex);
        switch (columnIndex) {
            case 0:
                return key.getName();
            case 1:
                if (key instanceof PropertyInteger) {
                    return blockState.getValue((PropertyInteger) key);
                }
                else if (key instanceof PropertyBool) {
                    return blockState.getValue((PropertyBool) key);
                }
                else if (key instanceof PropertyEnum) {
                    return ((IStringSerializable) blockState.getValue(key)).getName();
                }
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return true;
        }

        return super.isCellEditable(rowIndex, columnIndex);
    }

    private <P extends PropertyEnum<T>, T extends Enum<T> & IStringSerializable> void parseEnum(P property, String valueName) {
        property.getAllowedValues().stream().filter(value -> valueName.equals(value.getName())).findFirst().ifPresent(value -> blockState = blockState.withProperty(property, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            IProperty key = new ArrayList<>(blockState.getPropertyKeys()).get(rowIndex);
            if (key instanceof PropertyInteger) {
                blockState = blockState.withProperty(key, Integer.valueOf(aValue.toString()));
            }
            else if (key instanceof PropertyBool) {
                blockState = blockState.withProperty(key, Boolean.valueOf(aValue.toString()));
            }
            else if (key instanceof PropertyDirection) {
                EnumFacing facing = EnumFacing.byName(aValue.toString());
                if (facing != null) {
                    blockState = blockState.withProperty(key, facing);
                }
            }
            else if (key instanceof PropertyEnum) {
                String name = aValue.toString();
                if (key == BlockBed.PART) {
                    parseEnum(BlockBed.PART, name);
                }
                else if (key == BlockCarpet.COLOR) {
                    parseEnum(BlockCarpet.COLOR, name);
                }
                else if (key == BlockColored.COLOR) {
                    parseEnum(BlockColored.COLOR, name);
                }
                else if (key == BlockConcretePowder.COLOR) {
                    parseEnum(BlockConcretePowder.COLOR, name);
                }
                else if (key == BlockDirt.VARIANT) {
                    parseEnum(BlockDirt.VARIANT, name);
                }
                else if (key == BlockDoor.HINGE) {
                    parseEnum(BlockDoor.HINGE, name);
                }
                else if (key == BlockDoor.HALF) {
                    parseEnum(BlockDoublePlant.HALF, name);
                }
                else if (key == BlockDoublePlant.VARIANT) {
                    parseEnum(BlockDoublePlant.VARIANT, name);
                }
                else if (key == BlockDoublePlant.HALF) {
                    parseEnum(BlockDoublePlant.HALF, name);
                }
                else if (key == Blocks.RED_FLOWER.getTypeProperty()) {
                    parseEnum((PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.RED_FLOWER.getTypeProperty(), name);
                }
                else if (key == Blocks.YELLOW_FLOWER.getTypeProperty()) {
                    parseEnum((PropertyEnum<BlockFlower.EnumFlowerType>) Blocks.YELLOW_FLOWER.getTypeProperty(), name);
                }
                else if (key == BlockFlowerPot.CONTENTS) {
                    parseEnum(BlockFlowerPot.CONTENTS, name);
                }
                else if (key == BlockHugeMushroom.VARIANT) {
                    parseEnum(BlockHugeMushroom.VARIANT, name);
                }
                else if (key == BlockLever.FACING) {
                    parseEnum(BlockLever.FACING, name);
                }
                else if (key == BlockLog.LOG_AXIS) {
                    parseEnum(BlockLog.LOG_AXIS, name);
                }
                else if (key == BlockNewLeaf.VARIANT) {
                    parseEnum(BlockNewLeaf.VARIANT, name);
                }
                else if (key == BlockNewLog.VARIANT) {
                    parseEnum(BlockNewLog.VARIANT, name);
                }
                else if (key == BlockOldLog.VARIANT) {
                    parseEnum(BlockOldLog.VARIANT, name);
                }
                else if (key == BlockOldLeaf.VARIANT) {
                    parseEnum(BlockOldLeaf.VARIANT, name);
                }
                else if (key == BlockPistonExtension.TYPE) {
                    parseEnum(BlockPistonExtension.TYPE, name);
                }
                else if (key == BlockPlanks.VARIANT) {
                    parseEnum(BlockPlanks.VARIANT, name);
                }
                else if (key == BlockPortal.AXIS) {
                    parseEnum(BlockPortal.AXIS, name);
                }
                else if (key == BlockPrismarine.VARIANT) {
                    parseEnum(BlockPrismarine.VARIANT, name);
                }
                else if (key == BlockPurpurSlab.VARIANT) {
                    parseEnum(BlockPurpurSlab.VARIANT, name);
                }
                else if (key == BlockQuartz.VARIANT) {
                    parseEnum(BlockQuartz.VARIANT, name);
                }
                else if (key == BlockRail.SHAPE) {
                    parseEnum(BlockRail.SHAPE, name);
                }
                else if (key == BlockRailDetector.SHAPE) {
                    parseEnum(BlockRailDetector.SHAPE, name);
                }
                else if (key == BlockRailPowered.SHAPE) {
                    parseEnum(BlockRailPowered.SHAPE, name);
                }
                else if (key == BlockRedSandstone.TYPE) {
                    parseEnum(BlockRedSandstone.TYPE, name);
                }
                else if (key == BlockRedstoneComparator.MODE) {
                    parseEnum(BlockRedstoneComparator.MODE, name);
                }
                else if (key == BlockRedstoneWire.NORTH) {
                    parseEnum(BlockRedstoneWire.NORTH, name);
                }
                else if (key == BlockRedstoneWire.EAST) {
                    parseEnum(BlockRedstoneWire.EAST, name);
                }
                else if (key == BlockRedstoneWire.SOUTH) {
                    parseEnum(BlockRedstoneWire.SOUTH, name);
                }
                else if (key == BlockRedstoneWire.WEST) {
                    parseEnum(BlockRedstoneWire.WEST, name);
                }
                else if (key == BlockRotatedPillar.AXIS) {
                    parseEnum(BlockRotatedPillar.AXIS, name);
                }
                else if (key == BlockSand.VARIANT) {
                    parseEnum(BlockSand.VARIANT, name);
                }
                else if (key == BlockSandStone.TYPE) {
                    parseEnum(BlockSandStone.TYPE, name);
                }
                else if (key == BlockSapling.TYPE) {
                    parseEnum(BlockSapling.TYPE, name);
                }
                else if (key == BlockSilverfish.VARIANT) {
                    parseEnum(BlockSilverfish.VARIANT, name);
                }
                else if (key == BlockSlab.HALF) {
                    parseEnum(BlockSlab.HALF, name);
                }
                else if (key == BlockStainedGlass.COLOR) {
                    parseEnum(BlockStainedGlass.COLOR, name);
                }
                else if (key == BlockStainedGlassPane.COLOR) {
                    parseEnum(BlockStainedGlassPane.COLOR, name);
                }
                else if (key == BlockStairs.HALF) {
                    parseEnum(BlockStairs.HALF, name);
                }
                else if (key == BlockStairs.SHAPE) {
                    parseEnum(BlockStairs.SHAPE, name);
                }
                else if (key == BlockStone.VARIANT) {
                    parseEnum(BlockStone.VARIANT, name);
                }
                else if (key == BlockStoneBrick.VARIANT) {
                    parseEnum(BlockStoneBrick.VARIANT, name);
                }
                else if (key == BlockStoneSlab.VARIANT) {
                    parseEnum(BlockStoneSlab.VARIANT, name);
                }
                else if (key == BlockStoneSlabNew.VARIANT) {
                    parseEnum(BlockStoneSlabNew.VARIANT, name);
                }
                else if (key == BlockStructure.MODE) {
                    parseEnum(BlockStructure.MODE, name);
                }
                else if (key == BlockTallGrass.TYPE) {
                    parseEnum(BlockTallGrass.TYPE, name);
                }
                else if (key == BlockTrapDoor.HALF) {
                    parseEnum(BlockTrapDoor.HALF, name);
                }
                else if (key == BlockWall.VARIANT) {
                    parseEnum(BlockWall.VARIANT, name);
                }
                else if (key == BlockWoodSlab.VARIANT) {
                    parseEnum(BlockStructure.MODE, name);
                }
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
