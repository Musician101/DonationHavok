package io.musician101.donationhavok.configurator.gui.rewards;

public class HavokParticleGui {/*extends BaseGui {

    private final int index;
    private JFormattedTextField delayTextField;
    private JComboBox<ParticleType<?>> particleComboBox;
    private JFormattedTextField xTextField;
    private JFormattedTextField xVelocityTextField;
    private JFormattedTextField yTextField;
    private JFormattedTextField yVelocityTextField;
    private JFormattedTextField zTextField;
    private JFormattedTextField zVelocityTextField;

    public HavokParticleGui(HavokParticle particle, int index, RewardsGui prevGUI) {
        this.index = index;
        String name = "Havok Particle";
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, particle, prevGUI));
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

    private JPanel delayPanel(HavokParticle particle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Delay: ", SwingConstants.LEFT), gbc(0, 0));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(particle.getDelay());
        delayTextField.setPreferredSize(new Dimension(100, delayTextField.getPreferredSize().height));
        panel.add(flowLayoutPanel(delayTextField), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, HavokParticle particle, RewardsGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(topPanel(particle), gbc(0, 0));
        panel.add(parseJLabel("Offset", SwingConstants.CENTER), gbc(0, 1));
        panel.add(offsetPanel(particle), gbc(0, 2));
        panel.add(parseJLabel("Velocity", SwingConstants.CENTER), gbc(0, 3));
        panel.add(velocityPanel(particle), gbc(0, 4));
        panel.add(buttons(frame, prevGUI), gbc(0, 5));
        return flowLayoutPanel(panel);
    }

    private JPanel offsetPanel(HavokParticle particle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xTextField = new JFormattedTextField(new DecimalFormat());
        xTextField.setValue(particle.getXOffset());
        Dimension xDim = xTextField.getPreferredSize();
        xDim.width = 100;
        xTextField.setPreferredSize(xDim);
        panel.add(xTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yTextField = new JFormattedTextField(new DecimalFormat());
        yTextField.setValue(particle.getYOffset());
        Dimension yDim = yTextField.getPreferredSize();
        yDim.width = 100;
        yTextField.setPreferredSize(yDim);
        panel.add(yTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zTextField = new JFormattedTextField(new DecimalFormat());
        zTextField.setValue(particle.getYOffset());
        Dimension zDim = zTextField.getPreferredSize();
        zDim.width = 100;
        zTextField.setPreferredSize(zDim);
        panel.add(zTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel particlePanel(HavokParticle particle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Particle: ", SwingConstants.CENTER), gbc(0, 0));
        particleComboBox = new JComboBox<>(new SortedComboBoxModel<>(IRegistry.field_212632_u.stream().collect(Collectors.toList()), Comparator.comparing(particleType -> particleType.getId().toString())));
        particleComboBox.setSelectedItem(particle.getParticle());
        particleComboBox.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(((ParticleType) value).getId().toString());
                return label;
            }
        });
        panel.add(particleComboBox, gbc(1, 0));
        return panel;
    }

    private JPanel topPanel(HavokParticle particle) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(delayPanel(particle), gbc(0, 0));
        panel.add(particlePanel(particle), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    @Override
    protected void update(RewardsGui prevGUI) {
        JTable particlesTable = prevGUI.particlesTable;
        HavokParticleTableModel model = (HavokParticleTableModel) particlesTable.getModel();
        HavokParticle havokParticle = new HavokParticle(Integer.valueOf(delayTextField.getValue().toString()), Double.valueOf(xTextField.getValue().toString()), Double.valueOf(yTextField.getValue().toString()), Double.valueOf(zTextField.getValue().toString()), Double.valueOf(xVelocityTextField.getValue().toString()), Double.valueOf(yVelocityTextField.getValue().toString()), Double.valueOf(zVelocityTextField.getValue().toString()), (ParticleType) particleComboBox.getSelectedItem());
        if (index == -1) {
            model.add(havokParticle);
        }
        else {
            model.replace(index, havokParticle);
        }
    }

    private JPanel velocityPanel(HavokParticle entity) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJLabel("X:", SwingConstants.LEFT)), gbc(0, 0));
        xVelocityTextField = new JFormattedTextField(new DecimalFormat());
        xVelocityTextField.setValue(entity.getXOffset());
        Dimension xDim = xVelocityTextField.getPreferredSize();
        xDim.width = 100;
        xVelocityTextField.setPreferredSize(xDim);
        panel.add(xVelocityTextField, gbc(1, 0));
        panel.add(flowLayoutPanel(parseJLabel("Y:", SwingConstants.LEFT)), gbc(2, 0));
        yVelocityTextField = new JFormattedTextField(new DecimalFormat());
        yVelocityTextField.setValue(entity.getYOffset());
        Dimension yDim = yVelocityTextField.getPreferredSize();
        yDim.width = 100;
        yVelocityTextField.setPreferredSize(yDim);
        panel.add(yVelocityTextField, gbc(3, 0));
        panel.add(flowLayoutPanel(parseJLabel("Z:", SwingConstants.LEFT)), gbc(4, 0));
        zVelocityTextField = new JFormattedTextField(new DecimalFormat());
        zVelocityTextField.setValue(entity.getYOffset());
        Dimension zDim = zVelocityTextField.getPreferredSize();
        zDim.width = 100;
        zVelocityTextField.setPreferredSize(zDim);
        panel.add(zVelocityTextField, gbc(5, 0));
        return flowLayoutPanel(panel);
    }*/
}
