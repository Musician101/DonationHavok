package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.discovery.Discovery;
import java.util.List;

public class DiscoveryTableModel extends ListTableModel<Discovery> {

    public DiscoveryTableModel(List<Discovery> elements) {
        super(elements, "Donor", "Reward Name", "Amount");
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Discovery discovery = elements.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return discovery.getDonorName();
            case 1:
                return discovery.getRewardName();
            case 2:
                return discovery.getAmount();
            default:
                return null;
        }
    }
}
