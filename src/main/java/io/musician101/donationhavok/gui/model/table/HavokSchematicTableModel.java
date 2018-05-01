package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokSchematic;
import java.util.List;

public class HavokSchematicTableModel extends ListTableModel<HavokSchematic> {

    public HavokSchematicTableModel(List<HavokSchematic> elements) {
        super(elements, "Delay", "X Offset", "Y Offset", "Z Offset", "Relative Path");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokSchematic schematic = elements.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return schematic.getDelay();
            case 1:
                return schematic.getXOffset();
            case 2:
                return schematic.getYOffset();
            case 3:
                return schematic.getZOffset();
            case 4:
                return schematic.getRelativePath();
            default:
                return null;
        }
    }
}
