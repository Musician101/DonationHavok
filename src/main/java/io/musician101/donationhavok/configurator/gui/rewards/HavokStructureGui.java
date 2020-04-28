package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokStructureGui {/*extends BaseGui {

    private final int index;
    private JFormattedTextField delayTextField;
    private JFormattedTextField integrity;
    private JComboBox<Mirror> mirror;
    private JComboBox<Rotation> rotation;
    private JFormattedTextField seed;
    private JTextField structureName;
    private JFormattedTextField xTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField zTextField;

    public HavokStructureGui(HavokStructure structure, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Structure";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, structure, prevGUI));
    }

    private JPanel leftPlacementSettingsPanel(PlacementSettings placementSettings) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Mirror:", SwingConstants.LEFT), gbc(0, 0));
        mirror = new JComboBox<>(new SortedComboBoxModel<>(Arrays.asList(Mirror.values()), Comparator.naturalOrder()));
        mirror.setSelectedItem(placementSettings.getMirror());
        panel.add(flowLayoutPanel(mirror), gbc(0, 1));
        panel.add(parseJLabel("Integrity:", SwingConstants.LEFT), gbc(0, 2));
        integrity = new JFormattedTextField(new DecimalFormat());
        integrity.setValue(placementSettings.getIntegrity());
        panel.add(integrity, gbc(0, 3));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokStructure structure, RewardsGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(structure.getDelay());
        panel.add(delayTextField, gbc(0, 1));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 2));
        panel.add(offsetPanel(structure), gbc(0, 4));
        //panel.add(structurePanel(structure), gbc(0, 5));
        panel.add(parseJLabel("Structure Name:", SwingConstants.LEFT), gbc(0, 5));
        structureName = new JTextField(structure.getStructureName());
        panel.add(structureName, gbc(0, 6));
        panel.add(parseJLabel("Placement Settings", SwingConstants.CENTER), gbc(0, 7));
        panel.add(placementSettingsPanel(structure), gbc(0, 8));
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
        panel.add(saveButtons, gbc(0, 9));
        return flowLayoutPanel(panel);
    }

    private JPanel offsetPanel(HavokStructure structure) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        xTextField.setValue(structure.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        yTextField.setValue(structure.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        zTextField.setValue(structure.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel placementSettingsPanel(HavokStructure structure) {
        JPanel panel = gridBagLayoutPanel();
        PlacementSettings placementSettings = structure.getPlacementSettings();
        panel.add(leftPlacementSettingsPanel(placementSettings), gbc(0, 0));
        panel.add(rightPlacementSettingsPanel(placementSettings, structure.getPlacementSettingsSeed()), gbc(1, 0));
        return panel;
    }

    private JPanel rightPlacementSettingsPanel(PlacementSettings placementSettings, long placementSettingsSeed) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Rotation:", SwingConstants.LEFT), gbc(0, 0));
        rotation = new JComboBox<>(new SortedComboBoxModel<>(Arrays.asList(Rotation.values()), Comparator.naturalOrder()));
        rotation.setSelectedItem(placementSettings.getRotation());
        panel.add(flowLayoutPanel(rotation), gbc(0, 1));
        panel.add(parseJLabel("Seed:", SwingConstants.LEFT), gbc(0, 2));
        seed = new JFormattedTextField(NumberFormat.getIntegerInstance());
        seed.setValue(placementSettingsSeed);
        panel.add(seed, gbc(0, 3));
        return flowLayoutPanel(panel);
    }

    private JPanel structurePanel(HavokStructure structure) {
        JPanel panel = gridBagLayoutPanel();
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGui prevGUI) {
        JTable structures = prevGUI.structuresTable;
        HavokStructureTableModel model = (HavokStructureTableModel) structures.getModel();
        PlacementSettings placementSettings = new PlacementSettings().setIntegrity(Float.valueOf(integrity.getText())).setSeed(Long.valueOf(seed.getText())).setMirror((Mirror) mirror.getSelectedItem()).setRotation((Rotation) rotation.getSelectedItem());
        HavokStructure havokStructure = new HavokStructure(Integer.valueOf(delayTextField.getText()), Integer.valueOf(xTextField.getText()), Integer.valueOf(yTextField.getText()), Integer.valueOf(zTextField.getText()), structureName.getText(), placementSettings, Long.valueOf(seed.getText()));
        if (index == -1) {
            model.add(havokStructure);
        }
        else {
            model.replace(index, havokStructure);
        }
    }*/
}
