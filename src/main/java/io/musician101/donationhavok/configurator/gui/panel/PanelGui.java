package io.musician101.donationhavok.configurator.gui.panel;

import io.musician101.donationhavok.configurator.gui.Gui;
import javax.swing.JPanel;

public abstract class PanelGui extends Gui {

    protected JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    protected abstract JPanel parsePanel();
}
