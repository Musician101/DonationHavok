package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokItemStackGui {/*extends BaseGui {

    private final int index;
    private JFormattedTextField amountTextField;
    private JFormattedTextField delayTextField;
    private JComboBox<ResourceLocation> itemComboBox;
    private JTree nbtTree;

    public HavokItemStackGui(HavokItemStack itemStack, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Item Stack";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, itemStack, prevGUI));
    }

    private JPanel buttonPanel(JFrame frame, RewardsGui prevGUI) {
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
        return flowLayoutPanel(panel);
    }

    private JPanel delayPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(itemStack.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        return panel;
    }

    private JPanel idPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("ID:", SwingConstants.LEFT), gbc(0, 0));
        itemComboBox = new JComboBox<>(new SortedComboBoxModel<>(new ArrayList<>(ForgeRegistries.ITEMS.getKeys()), Comparator.comparing(ResourceLocation::toString)));
        itemComboBox.setSelectedItem(ForgeRegistries.ITEMS.getKey(itemStack.getItemStack().getItem()));
        panel.add(flowLayoutPanel(itemComboBox), gbc(1, 0));
        return panel;
    }

    private JPanel leftPanel(JFrame frame, HavokItemStack itemStack, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(delayPanel(itemStack), gbc(0, 0));
        panel.add(idPanel(itemStack), gbc(0, 1));
        panel.add(numericalPanel(itemStack), gbc(0, 2));
        panel.add(buttonPanel(frame, prevGUI), gbc(0, 3));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokItemStack itemStack, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(frame, itemStack, prevGUI), gbc(0, 0));
        panel.add(rightPanel(itemStack), gbc(1, 0));
        return panel;
    }

    private JPanel numericalPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Amount:", SwingConstants.LEFT), gbc(0, 0));
        ItemStack is = itemStack.getItemStack();
        amountTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        amountTextField.setValue(is.getCount());
        Dimension yDim = amountTextField.getPreferredSize();
        yDim.width = 100;
        amountTextField.setPreferredSize(yDim);
        panel.add(flowLayoutPanel(amountTextField), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel rightPanel(HavokItemStack itemStack) {
        JPanel panel = new JPanel(new GridBagLayout());
        NBTTagCompound nbt = itemStack.getItemStack().getTag();
        JsonPanel jsonPanel = new JsonPanel(nbt == null ? new NBTTagCompound() : nbt);
        nbtTree = jsonPanel.getTree();
        panel.add(parseJLabel("Tag", SwingConstants.CENTER), gbc(0, 0));
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGui prevGUI) {
        JOptionPane.showConfirmDialog(null, "NBT doesn't support booleans. Any booleans will be converted to bytes.", "Alert!", JOptionPane.DEFAULT_OPTION);
        JTable items = prevGUI.itemsTable;
        HavokItemStackTableModel model = (HavokItemStackTableModel) items.getModel();
        ItemStack is = new ItemStack(ForgeRegistries.ITEMS.getValue((ResourceLocation) itemComboBox.getSelectedItem()), Integer.valueOf(amountTextField.getValue().toString()));
        is.setTag(GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
        HavokItemStack itemStack = new HavokItemStack(Integer.valueOf(delayTextField.getValue().toString()), is);
        if (index == -1) {
            model.add(itemStack);
        }
        else {
            model.replace(index, itemStack);
        }
    }*/
}
