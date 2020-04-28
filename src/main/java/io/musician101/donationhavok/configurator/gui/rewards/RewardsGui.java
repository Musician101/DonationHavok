package io.musician101.donationhavok.configurator.gui.rewards;

public class RewardsGui {/*extends BaseGui {

    private final int index;
    JTable blocksTable;
    JTable commandsTable;
    JTable entitiesTable;
    JTable itemsTable;
    JTable messagesTable;
    JTable particlesTable;
    JTable schematicsTable;
    JTable soundsTable;
    JTable structuresTable;
    private JCheckBox allowTargetViaNoteCheckBox;
    private JFormattedTextField delayTextField;
    private JFormattedTextField discount;
    private JFormattedTextField minAmountTextField;
    private JTextField nameTextField;
    private JFormattedTextField saleLength;
    private JCheckBox targetAllPlayersCheckBox;
    private JList<String> targetPlayers;
    private JList<Double> tierTriggers;
    private JCheckBox triggersSale;

    public RewardsGui(double minAmount, HavokRewards rewards, int index, OldConfigGui prevGUI) {
        this.index = index;
        String name = "Rewards for " + minAmount;
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f -> mainPanel(f, minAmount, rewards, prevGUI));
    }

    private JPanel buttonPanel(ActionListener addObject, ActionListener removeObject) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(flowLayoutPanel(parseJButton("Add", addObject)), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Remove", removeObject)), gbc(1, 0));
        return panel;
    }

    private JPanel generalInfo(double minAmount, HavokRewards rewards) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(parseJLabel("Minimum Amount:", SwingConstants.LEFT), gbc(0, 0));
        minAmountTextField = new JFormattedTextField(new DecimalFormat());
        minAmountTextField.setValue(minAmount);
        panel.add(minAmountTextField, gbc(0, 1));
        allowTargetViaNoteCheckBox = new JCheckBox("Allow target players via donation notes?", rewards.allowTargetViaNote());
        panel.add(allowTargetViaNoteCheckBox, gbc(0, 2));
        targetAllPlayersCheckBox = new JCheckBox("Target all online players?", rewards.targetAllPlayers());
        panel.add(targetAllPlayersCheckBox, gbc(0, 3));
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 4));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(rewards.getDelay());
        panel.add(delayTextField, gbc(0, 5));
        panel.add(parseJLabel("Name:", SwingConstants.LEFT), gbc(0, 6));
        nameTextField = new JTextField(rewards.getName());
        panel.add(nameTextField, gbc(0, 7));
        triggersSale = new JCheckBox("Triggers a sale?", rewards.triggersSale());
        panel.add(triggersSale, gbc(0, 8));
        panel.add(parseJLabel("Discount:", SwingConstants.LEFT), gbc(0, 9));
        discount = new JFormattedTextField(new DecimalFormat());
        discount.setValue(rewards.getDiscount());
        panel.add(discount, gbc(0, 10));
        panel.add(parseJLabel("Sale Duration:", SwingConstants.LEFT), gbc(0, 11));
        saleLength = new JFormattedTextField(NumberFormat.getIntegerInstance());
        saleLength.setValue(rewards.getSaleLength());
        panel.add(saleLength, gbc(0, 12));
        return panel;
    }

    private JPanel mainButtonPanel(JFrame frame, OldConfigGui prevGUI) {
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

    private JPanel mainPanel(JFrame frame, double minAmount, HavokRewards rewards, OldConfigGui prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", generalInfo(minAmount, rewards));
        TableGui<HavokBlockTableModel, HavokBlock> blocks = new TableGui<>(new HavokBlockTableModel(rewards.getBlocks()), new HavokBlock(), (block, index) -> new HavokBlockGui(block, index, this));
        blocksTable = blocks.getTable();
        tabbedPane.addTab("Blocks", blocks.getPanel());
        TableGui<HavokCommandTableModel, HavokCommand> commands = new TableGui<>(new HavokCommandTableModel(rewards.getCommands()), new HavokCommand(), (command, index) -> new HavokCommandGui(command, index, this));
        commandsTable = commands.getTable();
        tabbedPane.addTab("Commands", commands.getPanel());
        TableGui<HavokEntityTableModel, HavokEntity> entities = new TableGui<>(new HavokEntityTableModel(rewards.getEntities()), new HavokEntity(), (entity, index) -> new HavokEntityGui(entity, index, this));
        entitiesTable = entities.getTable();
        tabbedPane.addTab("Entities", entities.getPanel());
        TableGui<HavokItemStackTableModel, HavokItemStack> itemStacks = new TableGui<>(new HavokItemStackTableModel(rewards.getItems()), new HavokItemStack(), (itemStack, index) -> new HavokItemStackGui(itemStack, index, this));
        itemsTable = itemStacks.getTable();
        tabbedPane.addTab("ItemStacks", itemStacks.getPanel());
        TableGui<HavokMessageTableModel, HavokMessage> messages = new TableGui<>(new HavokMessageTableModel(rewards.getMessages()), new HavokMessage(), (message, index) -> new HavokMessageGui(message, index, this));
        messagesTable = messages.getTable();
        messagesTable.getColumnModel().getColumn(2).setCellRenderer(new ITextComponentTableCellRenderer());
        tabbedPane.addTab("Messages", messages.getPanel());
        TableGui<HavokParticleTableModel, HavokParticle> particles = new TableGui<>(new HavokParticleTableModel(rewards.getParticles()), new HavokParticle(), (particle, index) -> new HavokParticleGui(particle, index, this));
        particlesTable = particles.getTable();
        tabbedPane.addTab("Particles", particles.getPanel());
        TableGui<HavokSchematicTableModel, HavokSchematic> schematics = new TableGui<>(new HavokSchematicTableModel(rewards.getSchematics()), new HavokSchematic(), (schematic, index) -> new HavokSchematicGui(schematic, index, this));
        schematicsTable = schematics.getTable();
        tabbedPane.addTab("Schematics", schematics.getPanel());
        TableGui<HavokSoundTableModel, HavokSound> sounds = new TableGui<>(new HavokSoundTableModel(rewards.getSounds()), new HavokSound(), (sound, index) -> new HavokSoundGui(sound, index, this));
        soundsTable = sounds.getTable();
        tabbedPane.addTab("Sounds", sounds.getPanel());
        TableGui<HavokStructureTableModel, HavokStructure> structures = new TableGui<>(new HavokStructureTableModel(rewards.getStructures()), new HavokStructure(), (structure, index) -> new HavokStructureGui(structure, index, this));
        structuresTable = structures.getTable();
        tabbedPane.addTab("Structures", structures.getPanel());
        tabbedPane.addTab("Target Players", targetPlayers(rewards));
        tabbedPane.addTab("Trigger Tiers", triggerTiers(rewards));
        panel.add(tabbedPane, gbc(0, 0));
        panel.add(mainButtonPanel(frame, prevGUI), gbc(0, 1));
        return panel;
    }

    private JPanel targetPlayers(HavokRewards rewards) {
        JPanel panel = new JPanel(new GridBagLayout());
        targetPlayers = new JList<>(new SortedListModel<>(rewards.getTargetPlayers(), String::compareTo));
        panel.add(new JScrollPane(targetPlayers), gbc(0, 0));
        panel.add(flowLayoutPanel(buttonPanel(l -> ((SortedListModel<String>) targetPlayers.getModel()).addElement(JOptionPane.showInputDialog("Enter target player's name.", "")), l -> {
            SortedListModel<String> model = (SortedListModel<String>) targetPlayers.getModel();
            targetPlayers.remove(targetPlayers.getSelectedIndex());
        })), gbc(0, 1));
        return panel;
    }

    private JPanel triggerTiers(HavokRewards rewards) {
        JPanel panel = gridBagLayoutPanel();
        tierTriggers = new JList<>(new SortedListModel<>(rewards.getTriggerTiers(), Double::compareTo));
        panel.add(new JScrollPane(tierTriggers), gbc(0, 0));
        panel.add(flowLayoutPanel(buttonPanel(l -> {
            String string = JOptionPane.showInputDialog("Enter a reward tier. (Remember to be exact!)");
            try {
                double d = Double.valueOf(string);
                ((SortedListModel<Double>) tierTriggers.getModel()).addElement(d);
            }
            catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, string + " is not a valid input.");
            }
        }, l -> {
            SortedListModel<Double> model = (SortedListModel<Double>) tierTriggers.getModel();
            model.remove(tierTriggers.getSelectedIndex());
        })), gbc(0, 1));
        return panel;
    }

    @Override
    protected final void update(OldConfigGui prevGUI) {
        HavokRewards havokRewards = new HavokRewards(allowTargetViaNoteCheckBox.isSelected(), targetAllPlayersCheckBox.isSelected(), triggersSale.isSelected(), Double.valueOf(discount.getValue().toString()), Integer.valueOf(delayTextField.getValue().toString()), Integer.valueOf(saleLength.getValue().toString()), nameTextField.getText(), ((SortedListModel<Double>) tierTriggers.getModel()).getElements(), ((HavokBlockTableModel) blocksTable.getModel()).getElements(), ((HavokCommandTableModel) commandsTable.getModel()).getElements(), ((HavokEntityTableModel) entitiesTable.getModel()).getElements(), ((HavokItemStackTableModel) itemsTable.getModel()).getElements(), ((HavokMessageTableModel) messagesTable.getModel()).getElements(), ((HavokParticleTableModel) particlesTable.getModel()).getElements(), ((HavokSchematicTableModel) schematicsTable.getModel()).getElements(), ((HavokSoundTableModel) soundsTable.getModel()).getElements(), ((HavokStructureTableModel) structuresTable.getModel()).getElements(), ((SortedListModel<String>) targetPlayers.getModel()).getElements());
        JTable rewards = prevGUI.rewards;
        HavokRewardsTableModel model = (HavokRewardsTableModel) rewards.getModel();
        if (index > -1) {
            model.remove(index);
        }

        model.addReward(Double.parseDouble(minAmountTextField.getValue().toString()), havokRewards);
    }*/
}
