package io.musician101.donationhavok.configurator.gui.model.table;

public class HavokBlockTableModel {/*extends ListTableModel<HavokBlock> {

    public HavokBlockTableModel(List<HavokBlock> elements) {
        super(elements, "Delay", "X Offset", "Y Offset", "Z Offset", "ID", "BlockState", "TileEntity");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokBlock block = elements.get(rowIndex);
        IBlockState blockState = block.getBlockState();
        switch (columnIndex) {
            case 0:
                return block.getDelay();
            case 1:
                return block.getXOffset();
            case 2:
                return block.getYOffset();
            case 3:
                return block.getZOffset();
            case 4:
                ResourceLocation rs = ForgeRegistries.BLOCKS.getKey(blockState.getBlock());
                return rs == null ? "null" : rs.toString();
            case 5:
                return StringUtils.join(blockState.getProperties().stream().map(IProperty::getName).collect(Collectors.toList()), ", ");
            case 6:
                return StringUtils.join(block.getNBTTagCompound().keySet(), ", ");
            default:
                return null;
        }
    }*/
}
