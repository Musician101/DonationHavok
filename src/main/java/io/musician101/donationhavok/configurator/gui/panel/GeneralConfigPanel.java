package io.musician101.donationhavok.configurator.gui.panel;

import io.musician101.donationhavok.configurator.gui.model.SortedListModel;
import io.musician101.donationhavok.configurator.serialization.GeneralConfig;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.annotation.Nonnull;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GeneralConfigPanel extends PanelGui {

    private final GeneralConfig config;
    private JCheckBox generateBook;
    private JCheckBox hideDiscoveries;
    private JCheckBox replaceUnbreakableBlocks;
    private JTextField mcName;
    private JList<String> nonReplaceableBlocks;
    private final JFormattedTextField rewardsDelay = new JFormattedTextField(NumberFormat.getIntegerInstance());

    public GeneralConfigPanel(@Nonnull GeneralConfig config) {
        this.config = config;
        this.panel = parsePanel();
    }

    @Override
    protected JPanel parsePanel() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 0));
        rewardsDelay.setValue(config.getDelay());
        panel.add(rewardsDelay, gbc(0, 1));
        panel.add(parseJLabel("Minecraft Name:", SwingConstants.LEFT), gbc(0, 2));
        panel.add(mcName = new JTextField(config.getMCName()), gbc(0, 3));
        panel.add(generateBook = new JCheckBox("Generate Book?", config.generateBook()), gbc(0, 4));
        panel.add(hideDiscoveries = new JCheckBox("Hide Discoveries Until Discovered?", config.hideCurrentUntilDiscovered()), gbc(0, 5));
        panel.add(replaceUnbreakableBlocks = new JCheckBox("Replace Unbreakable Blocks?", config.replaceUnbreakableBlocks()), gbc(0, 6));
        panel.add(nonReplaceableBlocksPanel(), gbc(1, 0));
        return panel;
    }

    private JPanel nonReplaceableBlocksPanel() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Added Blocks", SwingConstants.CENTER));
        panel.add(new JScrollPane(nonReplaceableBlocks = new JList<>(new SortedListModel<>(config.getNonReplaceableBlocks(), String::compareTo))), gbc(1, 1));
        //nonReplaceableBlocks.setCellRenderer();
        nonReplaceableBlocks.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    //TODO transfer
                }
            }
        });
        return flowLayoutPanel(panel);
    }

    public boolean generateBook() {
        return generateBook.isSelected();
    }

    public boolean hideDiscoveries() {
        return hideDiscoveries.isSelected();
    }

    public boolean replaceUnbreakableBlocks() {
        return replaceUnbreakableBlocks.isSelected();
    }

    public int getRewardsDelay() {
        return (int) rewardsDelay.getValue();
    }

    public String getMCName() {
        return mcName.getName();
    }
}
