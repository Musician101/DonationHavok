package io.musician101.donationhavok.configurator.gui.model.table;

public class HavokItemStackTableModel {/*extends ListTableModel<HavokItemStack> {

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
                return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString();
            case 2:
                return itemStack.getCount();
            case 3:
                return itemStack.getDamage();
            case 4:
                NBTTagCompound tag = itemStack.getTag();
                if (tag == null) {
                    return "None";
                }

                return StringUtils.join(tag.keySet(), ", ");
            default:
                return null;
        }
    }*/
}
