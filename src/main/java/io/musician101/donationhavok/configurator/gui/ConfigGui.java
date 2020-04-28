package io.musician101.donationhavok.configurator.gui;

import io.musician101.donationhavok.configurator.gui.panel.GeneralConfigPanel;
import io.musician101.donationhavok.configurator.serialization.GeneralConfig;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ConfigGui extends BaseGui {

    private final GeneralConfigPanel generalConfigPanel;

    public ConfigGui(GeneralConfig generalConfig) {
        this.generalConfigPanel = new GeneralConfigPanel(generalConfig);
        parseJFrame("DonationHavok Configurator", this::mainPanel);
    }

    @Override
    protected JPanel mainPanel(JFrame frame) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(tabbedPane());
        return panel;
    }

    private JTabbedPane tabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("General", generalConfigPanel.getPanel());
        return tabbedPane;
    }

    @Override
    protected void update() {

    }
}
