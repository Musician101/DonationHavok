package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokBlock;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.apache.commons.lang3.StringUtils;

public class HavokBlockTableModel extends ListTableModel<HavokBlock> {

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
                return Block.REGISTRY.getNameForObject(blockState.getBlock()).toString();
            case 5:
                return StringUtils.join(blockState.getPropertyKeys().stream().map(IProperty::getName).collect(Collectors.toList()), ", ");
            case 6:
                return StringUtils.join(block.getNBTTagCompound().getKeySet(), ", ");
            default:
                return null;
        }
    }
}
