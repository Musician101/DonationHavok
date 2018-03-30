package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokItemStack;
import java.util.List;
import java.util.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;

public class HavokItemStackTableModel extends ListTableModel<HavokItemStack> {

    public HavokItemStackTableModel(List<HavokItemStack> elements) {
        super(elements, "Delay", "ID", "Count", "Damage/Meta", "Tags");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokItemStack havokItemStack = elements.get(rowIndex);
        ItemStack itemStack = havokItemStack.getItemStack();
        switch (columnIndex) {
            case 0:
                return havokItemStack.getDelay();
            case 1:
                return Objects.requireNonNull(Item.REGISTRY.getNameForObject(itemStack.getItem())).toString();
            case 2:
                return itemStack.getCount();
            case 3:
                return itemStack.getItemDamage();
            case 4:
                NBTTagCompound tag = itemStack.getTagCompound();
                if (tag == null) {
                    return "None";
                }

                return StringUtils.join(tag.getKeySet(), ", ");
            default:
                return null;
        }
    }
}
