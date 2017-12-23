package io.musician101.donationhavok.gui.rewards;

import io.musician101.donationhavok.gui.BaseGUI;
import io.musician101.donationhavok.gui.ConfigGUI;
import io.musician101.donationhavok.gui.model.SortedListModel;
import io.musician101.donationhavok.gui.model.table.HavokBlockTableModel;
import io.musician101.donationhavok.gui.model.table.HavokCommandTableModel;
import io.musician101.donationhavok.gui.model.table.HavokEntityTableModel;
import io.musician101.donationhavok.gui.model.table.HavokItemStackTableModel;
import io.musician101.donationhavok.gui.model.table.HavokMessageTableModel;
import io.musician101.donationhavok.gui.model.table.HavokParticleTableModel;
import io.musician101.donationhavok.gui.model.table.HavokRewardsTableModel;
import io.musician101.donationhavok.gui.model.table.HavokSoundTableModel;
import io.musician101.donationhavok.gui.render.ITextComponentTableCellRenderer;
import io.musician101.donationhavok.havok.HavokBlock;
import io.musician101.donationhavok.havok.HavokCommand;
import io.musician101.donationhavok.havok.HavokEntity;
import io.musician101.donationhavok.havok.HavokItemStack;
import io.musician101.donationhavok.havok.HavokMessage;
import io.musician101.donationhavok.havok.HavokParticle;
import io.musician101.donationhavok.havok.HavokRewards;
import io.musician101.donationhavok.havok.HavokSound;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RewardsGUI extends BaseGUI<ConfigGUI> {

    private JCheckBox allowTargetViaNoteCheckBox;
    private JCheckBox targetAllPlayersCheckBox;
    private JFormattedTextField delayTextField;
    private JFormattedTextField minAmountTextField;
    public JList<String> targetPlayers;
    public JList<Double> tierTriggers;
    JTable blocksTable;
    JTable commandsTable;
    JTable entitiesTable;
    JTable itemsTable;
    JTable messagesTable;
    JTable particlesTable;
    JTable soundsTable;
    private JTextField nameTextField;

    public RewardsGUI(double minAmount, HavokRewards rewards, ConfigGUI prevGUI) {
        String name = "Rewards for " + minAmount;
        if (isFrameActive(name)) {
            JOptionPane.showConfirmDialog(null, "That window is already open.", "Already open!", JOptionPane.DEFAULT_OPTION);
            return;
        }

        parseJFrame(name, prevGUI, f-> mainPanel(f, minAmount, rewards, prevGUI));
    }

    @Override
    protected final void update(ConfigGUI prevGUI) {
        HavokRewards havokRewards = new HavokRewards(allowTargetViaNoteCheckBox.isSelected(), targetAllPlayersCheckBox.isSelected(), Integer.valueOf(delayTextField.getText()), nameTextField.getText(), ((SortedListModel<Double>) tierTriggers.getModel()).getElements(), ((HavokBlockTableModel) blocksTable.getModel()).getElements(), ((HavokCommandTableModel) commandsTable.getModel()).getElements(), ((HavokEntityTableModel) entitiesTable.getModel()).getElements(), ((HavokItemStackTableModel) itemsTable.getModel()).getElements(), ((HavokMessageTableModel) messagesTable.getModel()).getElements(), ((HavokParticleTableModel) particlesTable.getModel()).getElements(), ((HavokSoundTableModel) soundsTable.getModel()).getElements(), ((SortedListModel<String>) targetPlayers.getModel()).getElements());
        JTable rewards = prevGUI.rewardsTable;
        ((HavokRewardsTableModel) rewards.getModel()).addReward(Double.parseDouble(minAmountTextField.getText()), havokRewards);
        resizeTable(rewards);
    }

    private JPanel targetPlayers(HavokRewards rewards) {
        JPanel panel = new JPanel(new GridBagLayout());
        targetPlayers = new JList<>(new SortedListModel<>(rewards.getTargetPlayers(), String::compareTo));
        panel.add(new JScrollPane(targetPlayers), gbc(0, 0));
        panel.add(flowLayoutPanel(buttonPanel(l -> ((SortedListModel<String>) targetPlayers.getModel()).addElement(JOptionPane.showInputDialog("Enter target player's name.", "")), l -> {
            SortedListModel<String> model = (SortedListModel<String>) targetPlayers.getModel();
            Arrays.stream(targetPlayers.getSelectedIndices()).forEach(model::remove);
        })), gbc(0, 1));
        return panel;
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
            Arrays.stream(tierTriggers.getSelectedIndices()).forEach(model::remove);
        })), gbc(0, 1));
        return panel;
    }

    private JPanel mainPanel(JFrame frame, double minAmount, HavokRewards rewards, ConfigGUI prevGUI) {
        JPanel panel = new JPanel(new GridBagLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", generalInfo(minAmount, rewards));
        TableGUI<HavokBlockTableModel, HavokBlock> blocks = new TableGUI<>(new HavokBlockTableModel(rewards.getBlocks()), new HavokBlock(), (block, index) -> new HavokBlockGUI(block, index, this));
        blocksTable = blocks.getTable();
        tabbedPane.addTab("Blocks", blocks.getPanel());
        TableGUI<HavokCommandTableModel, HavokCommand> commands = new TableGUI<>(new HavokCommandTableModel(rewards.getCommands()), new HavokCommand(), (command, index) -> new HavokCommandGUI(command, index, this));
        commandsTable = commands.getTable();
        tabbedPane.addTab("Commands", commands.getPanel());
        TableGUI<HavokEntityTableModel, HavokEntity> entities = new TableGUI<>(new HavokEntityTableModel(rewards.getEntities()), new HavokEntity(), (entity, index) -> new HavokEntityGUI(entity, index, this));
        entitiesTable = entities.getTable();
        tabbedPane.addTab("Entities", entities.getPanel());
        TableGUI<HavokItemStackTableModel, HavokItemStack> itemStacks = new TableGUI<>(new HavokItemStackTableModel(rewards.getItems()), new HavokItemStack(), (itemStack, index) -> new HavokItemStackGUI(itemStack, index, this));
        itemsTable = itemStacks.getTable();
        tabbedPane.addTab("ItemStacks", itemStacks.getPanel());
        TableGUI<HavokMessageTableModel, HavokMessage> messages = new TableGUI<>(new HavokMessageTableModel(rewards.getMessages()), new HavokMessage(), (message, index) -> new HavokMessageGUI(message, index, this));
        messagesTable = messages.getTable();
        messagesTable.getColumnModel().getColumn(2).setCellRenderer(new ITextComponentTableCellRenderer());
        tabbedPane.addTab("Messages", messages.getPanel());
        TableGUI<HavokParticleTableModel, HavokParticle> particles = new TableGUI<>(new HavokParticleTableModel(rewards.getParticles()), new HavokParticle(), (particle, index) -> new HavokParticleGUI(particle, index, this));
        particlesTable = particles.getTable();
        tabbedPane.addTab("Particles", particles.getPanel());
        TableGUI<HavokSoundTableModel, HavokSound> sounds = new TableGUI<>(new HavokSoundTableModel(rewards.getSounds()), new HavokSound(), (sound, index) -> new HavokSoundGUI(sound, index, this));
        soundsTable = sounds.getTable();
        tabbedPane.addTab("Sounds", sounds.getPanel());
        tabbedPane.addTab("Target Players", targetPlayers(rewards));
        tabbedPane.addTab("Trigger Tiers", triggerTiers(rewards));
        panel.add(tabbedPane, gbc(0, 0));
        panel.add(mainButtonPanel(frame, prevGUI), gbc(0, 1));
        return panel;
    }

    private JPanel mainButtonPanel(JFrame frame, ConfigGUI prevGUI) {
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
}
