package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokMessageGui {/*extends BaseGui {

    private final int index;
    private JCheckBox broadcastCheckBox;
    private JFormattedTextField delayTextField;
    private JTree messageTree;

    public HavokMessageGui(HavokMessage message, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Message";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, message, prevGUI));
    }

    private JPanel buttons(JFrame frame, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return panel;
    }

    private JPanel delayPanel(HavokMessage message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(message.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        broadcastCheckBox = new JCheckBox("Broadcast Message?", message.broadcastEnabled());
        panel.add(flowLayoutPanel(broadcastCheckBox), gbc(2, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel jsonPanel(HavokMessage message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Message", SwingConstants.CENTER), gbc(0, 0));
        JsonPanel jsonPanel = new JsonPanel(GSON.fromJson(ITextComponent.Serializer.toJson(message.getMessage()), JsonObject.class));
        messageTree = jsonPanel.getTree();
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokMessage message, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(delayPanel(message), gbc(0, 0));
        panel.add(jsonPanel(message), gbc(0, 1));
        panel.add(buttons(frame, prevGUI), gbc(0, 2));
        return panel;
    }

    @Override
    protected void update(RewardsGui prevGUI) {
        JTable messagesTable = prevGUI.messagesTable;
        HavokMessageTableModel model = (HavokMessageTableModel) messagesTable.getModel();
        HavokMessage havokMessage = new HavokMessage(Integer.valueOf(delayTextField.getValue().toString()), broadcastCheckBox.isSelected(), ITextComponent.Serializer.fromJson(((HavokMapTreeNode) messageTree.getModel().getRoot()).serialize().toString()));
        if (index == -1) {
            model.add(havokMessage);
        }
        else {
            model.replace(index, havokMessage);
        }
    }*/
}
