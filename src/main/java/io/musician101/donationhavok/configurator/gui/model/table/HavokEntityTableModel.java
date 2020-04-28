package io.musician101.donationhavok.configurator.gui.model.table;

public class HavokEntityTableModel {// extends ListTableModel<HavokEntity> {

    /*public HavokEntityTableModel(List<HavokEntity> elements) {
        super(elements, "Delay", "X Offset", "Y Offset", "Z Offset", "ID", "Raw NBT_COMPOUND");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        /*HavokEntity entity = elements.get(rowIndex);
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
                Set<String> keys = new HashSet<>(nbt.keySet());
                keys.removeIf(key -> key.equals("id"));
                return StringUtils.join(keys, ", ");
            default:
                return null;
        }
    }*/
}
