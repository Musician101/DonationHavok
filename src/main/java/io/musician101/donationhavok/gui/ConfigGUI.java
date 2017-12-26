package io.musician101.donationhavok.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.gui.model.SortedListModel;
import io.musician101.donationhavok.gui.model.table.HavokRewardsTableModel;
import io.musician101.donationhavok.gui.render.IBlockStateCellRenderer;
import io.musician101.donationhavok.gui.rewards.RewardsGUI;
import io.musician101.donationhavok.havok.HavokRewards;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.network.message.JsonMessage;
import io.musician101.donationhavok.streamlabs.StreamLabsTracker;
import io.musician101.donationhavok.util.IBlockStateIDComparator;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import static io.musician101.donationhavok.util.json.JsonUtils.ACCESS_TOKEN;
import static io.musician101.donationhavok.util.json.JsonUtils.DELAY;
import static io.musician101.donationhavok.util.json.JsonUtils.GENERATE_BOOK;
import static io.musician101.donationhavok.util.json.JsonUtils.GSON;
import static io.musician101.donationhavok.util.json.JsonUtils.MC_NAME;
import static io.musician101.donationhavok.util.json.JsonUtils.NON_REPLACEABLE_BLOCKS;
import static io.musician101.donationhavok.util.json.JsonUtils.REPLACE_UNBREAKABLE_BLOCKS;
import static io.musician101.donationhavok.util.json.JsonUtils.REWARDS;
import static io.musician101.donationhavok.util.json.JsonUtils.STREAMER_NAME;

public final class ConfigGUI extends BaseGUI<BaseGUI> {

    private final boolean isClientConfig;
    private JCheckBox generateBookCheckBox;
    private JCheckBox replaceUnbreakableBlocksCheckBox;
    private JCheckBox showAccessTokenCheckBox;
    private JFormattedTextField delayTextField;
    JList<IBlockState> nonReplaceableBlockList;
    private JPasswordField accessTokenPasswordField;
    public JTable rewardsTable;
    private JTextField mcNameTextField;
    private JTextField streamerNameTextField;

    public ConfigGUI(StreamLabsTracker slt, boolean isClientConfig) {
        this.isClientConfig = isClientConfig;
        parseJFrame("Donation Havok Configurator", null, f -> mainPanel(f, slt));
    }

    private JPanel leftPanel(JFrame frame, StreamLabsTracker slt) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Access Token:", SwingConstants.LEFT), gbc(0, 0));
        accessTokenPasswordField = new JPasswordField(slt.getAccessToken());
        accessTokenPasswordField.setEchoChar('*');
        panel.add(accessTokenPasswordField, gbc(0, 1));
        showAccessTokenCheckBox = new JCheckBox("Show access token?");
        showAccessTokenCheckBox.addActionListener(e -> accessTokenPasswordField.setEchoChar(showAccessTokenCheckBox.isSelected() ? (char) 0 : '*'));
        panel.add(showAccessTokenCheckBox, gbc(0, 2));
        panel.add(parseJLabel("Minecraft Name:", SwingConstants.LEFT), gbc(0, 3));
        mcNameTextField = new JTextField(slt.getMCName());
        panel.add(mcNameTextField, gbc(0, 4));
        panel.add(parseJLabel("Streamer Name:", SwingConstants.LEFT), gbc(0, 5));
        streamerNameTextField = new JTextField(slt.getStreamerName());
        panel.add(streamerNameTextField, gbc(0, 6));
        generateBookCheckBox = new JCheckBox("Generate book?", slt.generateBook());
        panel.add(generateBookCheckBox, gbc(0, 7));
        replaceUnbreakableBlocksCheckBox = new JCheckBox("Replace Unbreakable Blocks?", slt.replaceUnbreakableBlocks());
        panel.add(replaceUnbreakableBlocksCheckBox, gbc(0, 8));
        panel.add(parseJLabel("Check Delay:", SwingConstants.LEFT), gbc(0, 9));
        delayTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        delayTextField.setValue(slt.getDelay());
        panel.add(delayTextField, gbc(0, 10));
        panel.add(parseJLabel("Non-Replaceable Blocks", SwingConstants.CENTER), gbc(0, 11));
        nonReplaceableBlockList = new JList<>(new SortedListModel<>(slt.getNonReplaceableBlocks(), new IBlockStateIDComparator()));
        nonReplaceableBlockList.setCellRenderer(new IBlockStateCellRenderer());
        panel.add(new JScrollPane(nonReplaceableBlockList), gbc(0, 12));
        JButton editButton = parseJButton("^^^ Edit ^^^", l -> new AddNonReplaceableBlocksGUI(this));
        editButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(editButton), gbc(0, 13));
        JButton saveButton = parseJButton("Save", l -> {
            update(null);
            frame.setVisible(false);
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 14));
        JButton cancelButton = parseJButton("Cancel", l -> frame.setVisible(false));
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(0, 15));
        return panel;
    }

    private JPanel mainPanel(JFrame frame, StreamLabsTracker slt) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(leftPanel(frame, slt)), gbc(0, 0));
        panel.add(flowLayoutPanel(rightPanel(slt)), gbc(1, 0));
        return panel;
    }

    private JPanel rightPanel(StreamLabsTracker slt) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Double click any row to edit.", SwingConstants.CENTER), gbc(0, 0));
        rewardsTable = parseJTable(new HavokRewardsTableModel(slt.getRewards()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rewardsTable.getSelectedRow();
                HavokRewardsTableModel model = (HavokRewardsTableModel) rewardsTable.getModel();
                if (row == -1) {
                    return;
                }

                double amount = model.getMinAmountAt(row);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new RewardsGUI(amount, model.getRewardsAt(row), ConfigGUI.this);
                }
            }
        });
        panel.add(new JScrollPane(rewardsTable), gbc(0, 1));
        JPanel rewardsTableButtons = gridBagLayoutPanel();
        JButton newButton = parseJButton("New", l -> new RewardsGUI(0, new HavokRewards(), this));
        rewardsTableButtons.add(flowLayoutPanel(newButton), gbc(0, 0));
        JButton editButton = parseJButton("Edit", l -> {
            int row = rewardsTable.getSelectedRow();
            HavokRewardsTableModel model = (HavokRewardsTableModel) rewardsTable.getModel();
            if (row == -1) {
                return;
            }

            double amount = model.getMinAmountAt(row);
            new RewardsGUI(amount, model.getRewardsAt(row), ConfigGUI.this);
        });
        rewardsTableButtons.add(flowLayoutPanel(editButton), gbc(1, 0));
        JButton deleteButton = parseJButton("Delete", l -> {
            int row = rewardsTable.getSelectedRow();
            if (row == -1) {
                return;
            }

            ((HavokRewardsTableModel) rewardsTable.getModel()).remove(row);
            rewardsTable.validate();
        });
        rewardsTableButtons.add(flowLayoutPanel(deleteButton), gbc(2, 0));
        panel.add(flowLayoutPanel(rewardsTableButtons), gbc(0, 2));
        return panel;
    }

    @Override
    protected final void update(BaseGUI prevGUI) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(ACCESS_TOKEN, new String(accessTokenPasswordField.getPassword()));
        jsonObject.addProperty(GENERATE_BOOK, generateBookCheckBox.isSelected());
        jsonObject.addProperty(DELAY, delayTextField.getValue().toString());
        jsonObject.addProperty(MC_NAME, mcNameTextField.getText());
        jsonObject.addProperty(STREAMER_NAME, streamerNameTextField.getText());
        jsonObject.addProperty(REPLACE_UNBREAKABLE_BLOCKS, replaceUnbreakableBlocksCheckBox.isSelected());
        JsonArray nonReplaceableBlocks = new JsonArray();
        ((SortedListModel<IBlockState>) nonReplaceableBlockList.getModel()).getElements().forEach(blockState -> nonReplaceableBlocks.add(Block.REGISTRY.getNameForObject(blockState.getBlock()).toString()));
        jsonObject.add(NON_REPLACEABLE_BLOCKS, nonReplaceableBlocks);
        jsonObject.add(REWARDS, GSON.toJsonTree(new TreeMap<>(((HavokRewardsTableModel) rewardsTable.getModel()).getRewards()), new TypeToken<TreeMap<Double, HavokRewards>>(){}.getType()));
        if (isClientConfig) {
            try {
                DonationHavok.INSTANCE.saveConfig(GSON.fromJson(jsonObject, StreamLabsTracker.class));
            }
            catch (Exception e) {
                JOptionPane.showConfirmDialog(null, "An error occurred while saving the config.\n" + e.getMessage(), "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
            }
        }
        else {
            Network.INSTANCE.sendToServer(new JsonMessage(jsonObject));
        }
    }
}
