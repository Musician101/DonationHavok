package io.musician101.donationhavok.configurator.gui;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

public abstract class DiscoveryGui extends BaseGui {

    //private final int index;
    //private final boolean isLegendary;
    private JFormattedTextField amount;
    private JTextField donorName;
    private JTextField rewardName;

    /*public DiscoveryGui(Discovery discovery, DiscoveriesGui prevGUI, int index, boolean isLegendary) {
        this.index = index;
        this.isLegendary = isLegendary;
        parseJFrame("Discovery", prevGUI, f -> mainPanel(f, discovery, prevGUI));
    }

    private JPanel buttonsPanel(JFrame frame, DiscoveriesGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, Discovery discovery, DiscoveriesGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Amount:", SwingConstants.LEFT), gbc(0, 0));
        amount = new JFormattedTextField(new DecimalFormat());
        amount.setValue(discovery.getAmount());
        panel.add(amount, gbc(0, 1));
        panel.add(parseJLabel("Donor:", SwingConstants.LEFT), gbc(0, 2));
        donorName = new JTextField(discovery.getDonorName());
        panel.add(donorName, gbc(0, 3));
        panel.add(parseJLabel("Reward Name:", SwingConstants.LEFT), gbc(0, 4));
        rewardName = new JTextField(discovery.getRewardName());
        panel.add(rewardName, gbc(0, 5));
        panel.add(buttonsPanel(frame, prevGUI), gbc(0, 6));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(DiscoveriesGui prevGUI) {
        Discovery discovery = new Discovery(Double.valueOf(amount.getValue().toString()), donorName.getText(), rewardName.getText());
        DiscoveryTableModel model = (DiscoveryTableModel) (isLegendary ? prevGUI.legendaryDiscoveries.getModel() : prevGUI.currentDiscoveries.getModel());
        if (index == -1) {
            model.add(discovery);
        }
        else {
            model.replace(index, discovery);
        }
    }*/
}
