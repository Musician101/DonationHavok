package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokEntityGui {/*extends BaseGui {

    private final int index;
    private JFormattedTextField delayTextField;
    private JComboBox<ResourceLocation> entityComboBox;
    private JTree nbtTree;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;

    public HavokEntityGui(HavokEntity entity, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Entity";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, entity, prevGUI));
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

    private JPanel leftPanel(JFrame frame, HavokEntity entity, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(entity.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(entity), gbc(0, 3));
        panel.add(parseJLabel("Entity", SwingConstants.LEFT), gbc(0, 4));
        entityComboBox = new JComboBox<>(new SortedComboBoxModel<>(ForgeRegistries.ENTITIES.getValues().stream().map(EntityType::getRegistryName).collect(Collectors.toList()), Comparator.comparing(ResourceLocation::toString)));
        entityComboBox.setSelectedItem(new ResourceLocation(entity.getNBTTagCompound().getString("id")));
        entityComboBox.addActionListener(l -> {
            Entity e = EntityType.create(LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).getWorld(DimensionType.OVERWORLD), (ResourceLocation) Objects.requireNonNull(entityComboBox.getSelectedItem()));
            Objects.requireNonNull(e).read(GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class));
            DefaultTreeModel model = (DefaultTreeModel) nbtTree.getModel();
            NBTTagCompound nbt = e.writeWithoutTypeId(new NBTTagCompound());
            nbt.removeTag("UUID");
            nbt.removeTag("UUIDMost");
            nbt.removeTag("UUIDLeast");
            model.setRoot(new HavokMapTreeNode(GSON.toJson(nbt)));
        });
        panel.add(entityComboBox, gbc(0, 5));
        panel.add(buttonPanel(frame, prevGUI), gbc(0, 6));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokEntity entity, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(leftPanel(frame, entity, prevGUI), gbc(0, 0));
        panel.add(rightPanel(entity), gbc(1, 0));
        return panel;
    }

    private JPanel offsetPanel(HavokEntity entity) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(new DecimalFormat());
        xTextField.setValue(entity.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(new DecimalFormat());
        yTextField.setValue(entity.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(new DecimalFormat());
        zTextField.setValue(entity.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel rightPanel(HavokEntity entity) {
        JPanel panel = new JPanel(new GridBagLayout());
        NBTTagCompound nbt = entity.getNBTTagCompound().copy();
        nbt.removeTag(Keys.ID.getKey());
        JsonPanel jsonPanel = new JsonPanel(nbt);
        nbtTree = jsonPanel.getTree();
        panel.add(parseJLabel("NBT_COMPOUND", SwingConstants.CENTER), gbc(0, 0));
        panel.add(jsonPanel.getScrollPane(), gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGui prevGUI) {
        JTable entities = prevGUI.entitiesTable;
        HavokEntityTableModel model = (HavokEntityTableModel) entities.getModel();
        NBTTagCompound nbt = GSON.fromJson(((HavokMapTreeNode) nbtTree.getModel().getRoot()).serialize(), NBTTagCompound.class);
        nbt.setString(Keys.ID.getKey(), entityComboBox.getSelectedItem().toString());
        HavokEntity entity = new HavokEntity(Integer.valueOf(delayTextField.getValue().toString()), Double.valueOf(xTextField.getValue().toString()), Double.valueOf(yTextField.getValue().toString()), Double.valueOf(zTextField.getValue().toString()), nbt);
        if (index == -1) {
            model.add(entity);
        }
        else {
            model.replace(index, entity);
        }
    }*/
}
