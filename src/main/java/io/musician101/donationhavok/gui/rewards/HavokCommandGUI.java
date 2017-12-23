package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.model.table.HavokCommandTableModel;
import io.musician101.donationhavok.havok.HavokCommand;
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

public class HavokCommandGUI extends BaseGUI<RewardsGUI> {

    private final int index;
    private JFormattedTextField delayTextField;
    private JTextField commandTextField;

    public HavokCommandGUI(HavokCommand command, int index, RewardsGUI prevGUI) {
        this.index = index;
        String name = "Havok Command";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, command, prevGUI));
    }

    @Override
    protected void update(RewardsGUI prevGUI) {
        JTable commands = prevGUI.commandsTable;
        HavokCommandTableModel model = (HavokCommandTableModel) commands.getModel();
        HavokCommand havokCommand = new HavokCommand(Integer.valueOf(delayTextField.getText()), commandTextField.getText());
        if (index == -1) {
            model.add(havokCommand);
        }
        else {
            model.replace(index, havokCommand);
        }

        resizeTable(commands);
    }

    private JPanel mainPanel(JFrame frame, HavokCommand command, RewardsGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(command.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Command:", SwingConstants.LEFT), gbc(0, 2));
        commandTextField = new JTextField(command.getCommand());
        panel.add(commandTextField, gbc(0, 3));
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 4));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(0, 5));
        return flowLayoutPanel(panel);
    }
}
