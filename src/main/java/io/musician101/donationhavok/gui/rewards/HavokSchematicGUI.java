package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.table.HavokSchematicTableModel;
import io.musician101.donationhavok.handler.havok.HavokSchematic;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class HavokSchematicGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JFormattedTextField delayTextField;
    private JTextField relativePath;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;

    public HavokSchematicGUI(HavokSchematic schematic, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Schematic";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, schematic, prevGUI));
    }

    private JPanel mainPanel(JFrame frame, HavokSchematic schematic, RewardsGUI prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(schematic.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(schematic), gbc(0, 3));
        panel.add(parseJLabel("Path Relative To Config Folder:", SwingConstants.LEFT), gbc(0, 4));
        relativePath = new JTextField(schematic.getRelativePath());
        panel.add(relativePath, gbc(0, 5));
        JPanel saveButtons = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        saveButtons.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        saveButtons.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        panel.add(saveButtons, gbc(0, 6));
        return flowLayoutPanel(panel);
    }

    private JPanel offsetPanel(HavokSchematic schematic) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        xTextField.setValue(schematic.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        yTextField.setValue(schematic.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        zTextField.setValue(schematic.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGUI prevGUI) {
        JTable schematics = prevGUI.schematicsTable;
        HavokSchematicTableModel model = (HavokSchematicTableModel) schematics.getModel();
        HavokSchematic havokSchematic = new HavokSchematic(Integer.valueOf(delayTextField.getText()), Integer.valueOf(xTextField.getText()), Integer.valueOf(yTextField.getText()), Integer.valueOf(zTextField.getText()), relativePath.getText());
        if (index == -1) {
            model.add(havokSchematic);
        }
        else {
            model.replace(index, havokSchematic);
        }
    }
}
