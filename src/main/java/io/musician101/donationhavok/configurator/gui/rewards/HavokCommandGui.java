package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokCommandGui {/*extends BaseGui {

    private final int index;
    private JTextField commandTextField;
    private JFormattedTextField delayTextField;

    public HavokCommandGui(HavokCommand command, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Command";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, command, prevGUI));
    }

    private JPanel mainPanel(JFrame frame, HavokCommand command, RewardsGui prevGUI) {
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

    @Override
    protected void update(RewardsGui prevGUI) {
        JTable commands = prevGUI.commandsTable;
        HavokCommandTableModel model = (HavokCommandTableModel) commands.getModel();
        HavokCommand havokCommand = new HavokCommand(Integer.valueOf(delayTextField.getValue().toString()), commandTextField.getText());
        if (index == -1) {
            model.add(havokCommand);
        }
        else {
            model.replace(index, havokCommand);
        }
    }*/
}
