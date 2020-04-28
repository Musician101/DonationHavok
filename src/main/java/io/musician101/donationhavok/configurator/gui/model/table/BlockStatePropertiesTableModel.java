package io.musician101.donationhavok.configurator.gui.model.table;

import javax.swing.table.AbstractTableModel;

public abstract class BlockStatePropertiesTableModel extends AbstractTableModel {

    //private IBlockState blockState;

    /*public BlockStatePropertiesTableModel(IBlockState blockState) {
        this.blockState = blockState;
    }*/

    /*public IBlockState getBlockState() {
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
        IProperty key = new ArrayList<>(blockState.getProperties()).get(rowIndex);
        switch (columnIndex) {
            case 0:
                return key.getName();
            case 1:
                if (key instanceof IntegerProperty) {
                    return blockState.get((IntegerProperty) key);
                }
                else if (key instanceof BooleanProperty) {
                    return blockState.get((BooleanProperty) key);
                }
                else if (key instanceof EnumProperty) {
                    return ((IStringSerializable) blockState.get(key)).getName();
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

    private <P extends EnumProperty<T>, T extends Enum<T> & IStringSerializable> void parseEnum(P property, String valueName) {
        property.getAllowedValues().stream().filter(value -> valueName.equals(value.getName())).findFirst().ifPresent(value -> blockState = blockState.with(property, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            IProperty key = new ArrayList<>(blockState.getProperties()).get(rowIndex);
            if (key instanceof IntegerProperty) {
                blockState = blockState.with(key, Integer.valueOf(aValue.toString()));
            }
            else if (key instanceof BooleanProperty) {
                blockState = blockState.with(key, Boolean.valueOf(aValue.toString()));
            }
            else if (key instanceof DirectionProperty) {
                EnumFacing facing = EnumFacing.byName(aValue.toString());
                if (facing != null) {
                    blockState = blockState.with(key, facing);
                }
            }
            else if (key instanceof EnumProperty) {
                String name = aValue.toString();
                if (key == BlockBed.PART) {
                    parseEnum(BlockBed.PART, name);
                }
                else if (key == BlockDoor.HINGE) {
                    parseEnum(BlockDoor.HINGE, name);
                }
                else if (key == BlockDoor.HALF) {
                    parseEnum(BlockDoublePlant.HALF, name);
                }
                else if (key == BlockDoublePlant.HALF) {
                    parseEnum(BlockDoublePlant.HALF, name);
                }
                else if (key == BlockLog.AXIS) {
                    parseEnum(BlockLog.AXIS, name);
                }
                else if (key == BlockPistonExtension.TYPE) {
                    parseEnum(BlockPistonExtension.TYPE, name);
                }
                else if (key == BlockPortal.AXIS) {
                    parseEnum(BlockPortal.AXIS, name);
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
                else if (key == BlockStairs.HALF) {
                    parseEnum(BlockStairs.HALF, name);
                }
                else if (key == BlockStairs.SHAPE) {
                    parseEnum(BlockStairs.SHAPE, name);
                }
                else if (key == BlockStructure.MODE) {
                    parseEnum(BlockStructure.MODE, name);
                }
                else if (key == BlockTrapDoor.HALF) {
                    parseEnum(BlockTrapDoor.HALF, name);
                }
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }*/
}
