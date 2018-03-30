package io.musician101.donationhavok.gui.model.table;

import io.musician101.donationhavok.handler.havok.HavokRewards;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang3.StringUtils;

public class HavokRewardsTableModel extends AbstractTableModel {

    private final Map<Double, HavokRewards> rewards;

    public HavokRewardsTableModel(Map<Double, HavokRewards> rewards) {
        this.rewards = new HashMap<>(rewards);
    }

    public void addReward(double minAmount, HavokRewards rewards) {
        this.rewards.put(minAmount, rewards);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Min. Amount";
            case 2:
                return "Delay";
            case 3:
                return "Parts";
            default:
                return null;
        }
    }

    public double getMinAmountAt(int rowIndex) {
        return new ArrayList<>(rewards.keySet()).get(rowIndex);
    }

    public Map<Double, HavokRewards> getRewards() {
        return rewards;
    }

    public HavokRewards getRewardsAt(int rowIndex) {
        return new ArrayList<>(rewards.values()).get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return rewards.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<Entry<Double, HavokRewards>> tiers = new ArrayList<>(rewards.entrySet());
        try {
            Entry<Double, HavokRewards> tier = tiers.get(rowIndex);
            HavokRewards rewards = tier.getValue();
            switch (columnIndex) {
                case 0:
                    return rewards.getName();
                case 1:
                    return tier.getKey();
                case 2:
                    return rewards.getDelay();
                case 3:
                    List<String> parts = new ArrayList<>();
                    if (!rewards.getBlocks().isEmpty()) {
                        parts.add("Blocks");
                    }

                    if (!rewards.getCommands().isEmpty()) {
                        parts.add("Commands");
                    }

                    if (!rewards.getEntities().isEmpty()) {
                        parts.add("Entities");
                    }

                    if (!rewards.getItems().isEmpty()) {
                        parts.add("Items");
                    }

                    if (!rewards.getMessages().isEmpty()) {
                        parts.add("Messages");
                    }

                    if (!rewards.getParticles().isEmpty()) {
                        parts.add("Particles");
                    }

                    if (!rewards.getTargetPlayers().isEmpty()) {
                        parts.add("Target Players");
                    }

                    if (!rewards.getTriggerTiers().isEmpty()) {
                        parts.add("Trigger Tiers");
                    }

                    if (!rewards.getSounds().isEmpty()) {
                        parts.add("Sounds");
                    }

                    return StringUtils.join(parts, ", ");
            }
        }
        catch (IndexOutOfBoundsException ignored) {

        }

        return null;
    }

    public void remove(int rowIndex) {
        rewards.remove(getMinAmountAt(rowIndex));
        fireTableDataChanged();
    }
}
