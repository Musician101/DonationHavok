package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.havok.HavokEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

public class HavokEntityTableModel extends ListTableModel<HavokEntity> {

    public HavokEntityTableModel(List<HavokEntity> elements) {
        super(elements, "Delay", "X Offset", "Y Offset", "Z Offset", "ID", "Raw NBT_COMPOUND");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokEntity entity = elements.get(rowIndex);
        NBTTagCompound nbt = entity.getNBTTagCompound();
        switch (columnIndex) {
            case 0:
                return entity.getDelay();
            case 1:
                return entity.getXOffset();
            case 2:
                return entity.getYOffset();
            case 3:
                return entity.getZOffset();
            case 4:
                return nbt.getString("id");
            case 5:
                Set<String> keys = new HashSet<>(nbt.getKeySet());
                keys.removeIf(key -> key.equals("id"));
                return StringUtils.join(keys, ", ");
            default:
                return null;
        }
    }
}
