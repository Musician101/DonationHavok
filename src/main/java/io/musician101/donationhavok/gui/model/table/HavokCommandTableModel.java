package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokCommand;
import java.util.List;

public class HavokCommandTableModel extends ListTableModel<HavokCommand> {

    public HavokCommandTableModel(List<HavokCommand> elements) {
        super(elements, "Delay", "Command");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HavokCommand block = elements.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return block.getDelay();
            case 1:
                return block.getCommand();
            default:
                return null;
        }
    }
}
