package io.musician101.donationhavok.gui;

import com.google.gson.JsonObject;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.gui.model.SortedListModel;
import io.musician101.donationhavok.gui.model.table.DiscoveryTableModel;
import io.musician101.donationhavok.gui.model.table.HavokRewardsTableModel;
import io.musician101.donationhavok.gui.model.table.TwitchCommandsModel;
import io.musician101.donationhavok.gui.render.IBlockStateCellRenderer;
import io.musician101.donationhavok.gui.rewards.RewardsGUI;
import io.musician101.donationhavok.handler.StreamLabsHandler;
import io.musician101.donationhavok.handler.discovery.Discovery;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.handler.havok.TempHavokRewardsStorage;
import io.musician101.donationhavok.handler.twitch.TwitchHandler;
import io.musician101.donationhavok.handler.twitch.commands.Command;
import io.musician101.donationhavok.handler.twitch.commands.DiscoveryCommand;
import io.musician101.donationhavok.handler.twitch.commands.PlayersCommand;
import io.musician101.donationhavok.handler.twitch.commands.RewardsCommand;
import io.musician101.donationhavok.handler.twitch.commands.TwitchCommands;
import io.musician101.donationhavok.network.Network;
import io.musician101.donationhavok.network.message.JsonMessage;
import io.musician101.donationhavok.util.BlockStateIDComparator;
import io.musician101.donationhavok.util.json.Keys;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.minecraft.block.state.IBlockState;

import static io.musician101.donationhavok.util.json.JsonKeyProcessor.GSON;

public final class ConfigGUI extends BaseGUI<BaseGUI> {

    private final boolean isClientConfig;
    public JTable currentDiscoveries;
    public JTable legendaryDiscoveries;
    public JTable rewards;
    public JTable twitchCommands;
    JList<IBlockState> nonReplaceableBlocks;
    private JPasswordField accessToken;
    private JCheckBox bitsTrigger;
    private JTextField channelName;
    private JCheckBox factorSubStreak;
    private JCheckBox generateBook;
    private JCheckBox hideUntilDiscovered;
    private JTextField mcName;
    private JCheckBox replaceUnbreakableBlocks;
    private JFormattedTextField rewardsDelay = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JCheckBox roundSubs;
    private JFormattedTextField streamLabsDelay;
    private JCheckBox streamLabsEnabled;
    private JCheckBox subsTrigger;
    private JCheckBox twitchCommandsEnabled;
    private JCheckBox twitchEnabled;

    public ConfigGUI(@Nonnull DiscoveryHandler discoveryHandler, @Nonnull HavokRewardsHandler hrh, @Nonnull StreamLabsHandler slh, @Nonnull TwitchHandler th, boolean isClientConfig) {
        this.isClientConfig = isClientConfig;
        parseJFrame("Donation Havok Configurator", null, f -> mainPanel(f, discoveryHandler, hrh, slh, th));
    }

    private JPanel buttonsPanel(JFrame frame) {
        JPanel panel = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update(null);
            frame.setVisible(false);
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.setVisible(false));
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return panel;
    }

    private JPanel centerGeneralPanel(TwitchHandler th) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Twitch", SwingConstants.CENTER), gbc(0, 0));
        twitchEnabled = new JCheckBox("Enable Twitch integration?", th.isEnabled());
        panel.add(twitchEnabled, gbc(0, 1));
        panel.add(parseJLabel("Channel name:", SwingConstants.LEFT), gbc(0, 2));
        channelName = new JTextField(th.getChannelName());
        panel.add(channelName, gbc(0, 3));
        bitsTrigger = new JCheckBox("Bits trigger rewards?", th.bitsTrigger());
        panel.add(bitsTrigger, gbc(0, 4));
        subsTrigger = new JCheckBox("Subscriptions trigger rewards?", th.subsTrigger());
        panel.add(subsTrigger, gbc(0, 5));
        roundSubs = new JCheckBox("Round subscriptions up to the nearest dollar?", th.roundSubs());
        panel.add(roundSubs, gbc(0, 6));
        factorSubStreak = new JCheckBox("Multiple subscription tier by streak?", th.factorSubStreak());
        panel.add(factorSubStreak, gbc(0, 7));
        TwitchCommands tc = th.getTwitchCommands();
        twitchCommandsEnabled = new JCheckBox("Enable Twitch chat commands?", tc.isEnabled());
        panel.add(twitchCommandsEnabled, gbc(0, 8));
        twitchCommands = parseJTable(new TwitchCommandsModel(Arrays.asList(tc.getDiscoveryCommand(), tc.getPlayersCommand(), tc.getRewardsCommand())), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = twitchCommands.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new TwitchCommandGUI(((TwitchCommandsModel) twitchCommands.getModel()).getObjectAt(row), row, ConfigGUI.this);
                }
            }
        });
        panel.add(new JScrollPane(twitchCommands), gbc(0, 9));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = twitchCommands.getSelectedRow();
            if (row == -1) {
                return;
            }

            new TwitchCommandGUI(((TwitchCommandsModel) twitchCommands.getModel()).getObjectAt(row), row, ConfigGUI.this);
        })), gbc(0, 19));
        return panel;
    }

    private JPanel currentDiscoveriesButtons() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(parseJButton("New", l -> new DiscoveryGUI(new Discovery(5D, "Musician101", "A whole new mod!"), ConfigGUI.this, -1, false))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Set as Legendary", l -> {
            int row = currentDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            DiscoveryTableModel current = (DiscoveryTableModel) currentDiscoveries.getModel();
            Discovery discovery = current.getObjectAt(row);
            ((DiscoveryTableModel) legendaryDiscoveries.getModel()).add(discovery);
            current.remove(row);
        })), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = currentDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            new DiscoveryGUI(((DiscoveryTableModel) currentDiscoveries.getModel()).getObjectAt(row), ConfigGUI.this, row, false);
        })), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", l -> IntStream.of(currentDiscoveries.getSelectedRows()).forEach(row -> ((DiscoveryTableModel) currentDiscoveries.getModel()).remove(row)))), gbc(3, 0));
        return flowLayoutPanel(panel);
    }

    private JsonObject discovery() {
        JsonObject jsonObject = new JsonObject();
        Keys.CURRENT.serialize(((DiscoveryTableModel) currentDiscoveries.getModel()).getElements(), jsonObject);
        Keys.HIDE_CURRENT_UNTIL_DISCOVERED.serialize(hideUntilDiscovered.isSelected(), jsonObject);
        Keys.LEGENDARY.serialize(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getElements(), jsonObject);
        return jsonObject;
    }

    private JPanel discoveryHandlerPanel(DiscoveryHandler dh) {
        JPanel panel = gridBagLayoutPanel();
        hideUntilDiscovered = new JCheckBox("Hide rewards from viewers until they are first triggered?", dh.hideCurrentUntilDiscovered());
        panel.add(hideUntilDiscovered, gbc(0, 0));
        JPanel discoveriesPanel = gridBagLayoutPanel();
        discoveriesPanel.add(parseJLabel("Current", SwingConstants.CENTER), gbc(0, 0));
        currentDiscoveries = parseJTable(new DiscoveryTableModel(dh.getCurrentDiscoveries()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = currentDiscoveries.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new DiscoveryGUI(((DiscoveryTableModel) currentDiscoveries.getModel()).getObjectAt(row), ConfigGUI.this, row, false);
                }
            }
        });
        discoveriesPanel.add(flowLayoutPanel(new JScrollPane(currentDiscoveries)), gbc(0, 1));
        discoveriesPanel.add(currentDiscoveriesButtons(), gbc(0, 2));
        discoveriesPanel.add(parseJLabel("Legendary", SwingConstants.CENTER), gbc(1, 0));
        legendaryDiscoveries = parseJTable(new DiscoveryTableModel(dh.getLegendaryDiscoveries()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = legendaryDiscoveries.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new DiscoveryGUI(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getObjectAt(row), ConfigGUI.this, row, true);
                }
            }
        });
        discoveriesPanel.add(flowLayoutPanel(new JScrollPane(legendaryDiscoveries)), gbc(1, 1));
        discoveriesPanel.add(legendaryDiscoveriesButtons(), gbc(1, 2));
        panel.add(discoveriesPanel, gbc(0, 1));
        return flowLayoutPanel(panel);
    }

    private JsonObject general() {
        JsonObject jsonObject = new JsonObject();
        Keys.DELAY.serialize(Integer.valueOf(rewardsDelay.getValue().toString()), jsonObject);
        Keys.GENERATE_BOOK.serialize(generateBook.isSelected(), jsonObject);
        Keys.MC_NAME.serialize(mcName.getText(), jsonObject);
        Keys.NON_REPLACEABLE_BLOCKS.serialize(((SortedListModel<IBlockState>) nonReplaceableBlocks.getModel()).getElements(), jsonObject);
        Keys.REPLACE_UNBREAKABLE_BLOCKS.serialize(replaceUnbreakableBlocks.isSelected(), jsonObject);
        TreeMap<Double, HavokRewards> rewards = new TreeMap<>(Double::compare);
        rewards.putAll(((HavokRewardsTableModel) this.rewards.getModel()).getRewards());
        Keys.REWARDS.serialize(new TempHavokRewardsStorage(rewards), jsonObject);
        return jsonObject;
    }

    private JPanel generalPanel(HavokRewardsHandler hrh, StreamLabsHandler slh, TwitchHandler th) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(leftGeneralPanel(hrh, slh), gbc(0, 0));
        panel.add(centerGeneralPanel(th), gbc(1, 0));
        panel.add(rightGeneralPanel(hrh), gbc(2, 0));
        return panel;
    }

    private JPanel leftGeneralPanel(HavokRewardsHandler hrh, StreamLabsHandler slh) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("General", SwingConstants.CENTER), gbc(0, 0));
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 1));
        rewardsDelay = new JFormattedTextField(NumberFormat.getIntegerInstance());
        rewardsDelay.setValue(hrh.getDelay());
        panel.add(rewardsDelay, gbc(0, 2));
        panel.add(parseJLabel("Minecraft Name:", SwingConstants.LEFT), gbc(0, 3));
        mcName = new JTextField(hrh.getMCName());
        panel.add(mcName, gbc(0, 4));
        generateBook = new JCheckBox("Generate book?", hrh.generateBook());
        panel.add(generateBook, gbc(0, 5));
        replaceUnbreakableBlocks = new JCheckBox("Replace unbreakable blocks?", hrh.replaceUnbreakableBlocks());
        panel.add(replaceUnbreakableBlocks, gbc(0, 6));
        panel.add(parseJLabel("Non-Replaceable Blocks", SwingConstants.CENTER), gbc(0, 7));
        nonReplaceableBlocks = new JList<>(new SortedListModel<>(hrh.getNonReplaceableBlocks(), new BlockStateIDComparator()));
        nonReplaceableBlocks.setCellRenderer(new IBlockStateCellRenderer());
        panel.add(new JScrollPane(nonReplaceableBlocks), gbc(0, 8));
        JButton editButton = parseJButton("Edit", l -> new AddNonReplaceableBlocksGUI(this));
        editButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(editButton), gbc(0, 9));
        panel.add(streamLabsPanel(slh), gbc(0, 10));
        return flowLayoutPanel(panel);
    }

    private JPanel legendaryDiscoveriesButtons() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(flowLayoutPanel(parseJButton("New", l -> new DiscoveryGUI(new Discovery(5D, "Musician101", "A whole new mod!"), ConfigGUI.this, -1, true))), gbc(0, 0));
        panel.add(flowLayoutPanel(parseJButton("Set as Current", l -> {
            int row = legendaryDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            DiscoveryTableModel legendary = (DiscoveryTableModel) legendaryDiscoveries.getModel();
            Discovery discovery = legendary.getObjectAt(row);
            ((DiscoveryTableModel) currentDiscoveries.getModel()).add(discovery);
            legendary.remove(row);
        })), gbc(1, 0));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = legendaryDiscoveries.getSelectedRow();
            if (row == -1) {
                return;
            }

            new DiscoveryGUI(((DiscoveryTableModel) legendaryDiscoveries.getModel()).getObjectAt(row), ConfigGUI.this, row, false);
        })), gbc(2, 0));
        panel.add(flowLayoutPanel(parseJButton("Delete", l -> IntStream.of(legendaryDiscoveries.getSelectedRows()).forEach(row -> ((DiscoveryTableModel) legendaryDiscoveries.getModel()).remove(row)))), gbc(3, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, DiscoveryHandler dh, HavokRewardsHandler hrh, StreamLabsHandler slh, TwitchHandler th) {
        JPanel panel = gridBagLayoutPanel();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("General", generalPanel(hrh, slh, th));
        pane.addTab("Discovery", discoveryHandlerPanel(dh));
        panel.add(pane, gbc(0, 0));
        panel.add(buttonsPanel(frame), gbc(0, 1));
        return panel;
    }

    private JPanel rightGeneralPanel(HavokRewardsHandler hrh) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Double click any row to edit.", SwingConstants.CENTER), gbc(0, 0));
        rewards = parseJTable(new HavokRewardsTableModel(hrh.getRewards()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rewards.getSelectedRow();
                HavokRewardsTableModel model = (HavokRewardsTableModel) rewards.getModel();
                if (row == -1) {
                    return;
                }

                double amount = model.getMinAmountAt(row);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new RewardsGUI(amount, model.getRewardsAt(row), row, ConfigGUI.this);
                }
            }
        });
        panel.add(new JScrollPane(rewards), gbc(0, 1));
        JPanel rewardsTableButtons = gridBagLayoutPanel();
        JButton newButton = parseJButton("New", l -> new RewardsGUI(0, new HavokRewards(), -1, this));
        rewardsTableButtons.add(flowLayoutPanel(newButton), gbc(0, 0));
        JButton editButton = parseJButton("Edit", l -> {
            int row = rewards.getSelectedRow();
            HavokRewardsTableModel model = (HavokRewardsTableModel) rewards.getModel();
            if (row == -1) {
                return;
            }

            double amount = model.getMinAmountAt(row);
            new RewardsGUI(amount, model.getRewardsAt(row), row, ConfigGUI.this);
        });
        rewardsTableButtons.add(flowLayoutPanel(editButton), gbc(1, 0));
        JButton deleteButton = parseJButton("Delete", l -> {
            int row = rewards.getSelectedRow();
            if (row == -1) {
                return;
            }

            ((HavokRewardsTableModel) rewards.getModel()).remove(row);
            rewards.validate();
        });
        rewardsTableButtons.add(flowLayoutPanel(deleteButton), gbc(2, 0));
        panel.add(flowLayoutPanel(rewardsTableButtons), gbc(0, 2));
        return flowLayoutPanel(panel);
    }

    private JsonObject streamLabs() {
        JsonObject jsonObject = new JsonObject();
        Keys.ACCESS_TOKEN.serialize(new String(accessToken.getPassword()), jsonObject);
        Keys.DELAY.serialize(Integer.valueOf(streamLabsDelay.getValue().toString()), jsonObject);
        Keys.ENABLE.serialize(streamLabsEnabled.isSelected(), jsonObject);
        return jsonObject;
    }

    private JPanel streamLabsPanel(StreamLabsHandler slh) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("StreamLabs", SwingConstants.CENTER), gbc(0, 0));
        streamLabsEnabled = new JCheckBox("Enable StreamLabs donations?", slh.isEnabled());
        panel.add(streamLabsEnabled, gbc(0, 1));
        panel.add(parseJLabel("Access Token:", SwingConstants.LEFT), gbc(0, 2));
        accessToken = new JPasswordField(slh.getAccessToken());
        accessToken.setEchoChar('*');
        panel.add(accessToken, gbc(0, 3));
        JCheckBox showAccessToken = new JCheckBox("Show access token?");
        showAccessToken.addActionListener(l -> accessToken.setEchoChar(showAccessToken.isSelected() ? (char) 0 : '*'));
        panel.add(showAccessToken, gbc(0, 4));
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 5));
        streamLabsDelay = new JFormattedTextField(NumberFormat.getIntegerInstance());
        streamLabsDelay.setValue(slh.getDelay());
        panel.add(streamLabsDelay, gbc(0, 6));
        return flowLayoutPanel(panel);
    }

    private JsonObject twitch() {
        JsonObject jsonObject = new JsonObject();
        Keys.ENABLE.serialize(twitchEnabled.isSelected(), jsonObject);
        Keys.TWITCH_NAME.serialize(channelName.getText(), jsonObject);
        Keys.BITS_TRIGGER.serialize(bitsTrigger.isSelected(), jsonObject);
        Keys.ROUND_SUBS.serialize(roundSubs.isSelected(), jsonObject);
        jsonObject.add(Keys.TWITCH_COMMANDS.getKey(), twitchCommands());
        Keys.FACTOR_SUB_STREAK.serialize(factorSubStreak.isSelected(), jsonObject);
        Keys.SUBS_TRIGGER.serialize(subsTrigger.isSelected(), jsonObject);
        return jsonObject;
    }

    private JsonObject twitchCommand(Command command) {
        JsonObject jsonObject = new JsonObject();
        Keys.ENABLE.serialize(command.isEnabled(), jsonObject);
        Keys.COMMAND_PERMISSIONS.serialize(new ArrayList<>(command.getRequiredPermissions()), jsonObject);
        return jsonObject;
    }

    private JsonObject twitchCommands() {
        JsonObject jsonObject = new JsonObject();
        Keys.ENABLE.serialize(twitchCommandsEnabled.isSelected(), jsonObject);
        ((TwitchCommandsModel) twitchCommands.getModel()).getElements().forEach(command -> {
            if (command instanceof DiscoveryCommand) {
                jsonObject.add(Keys.DISCOVERY_COMMAND.getKey(), twitchCommand(command));
            }
            else if (command instanceof PlayersCommand) {
                jsonObject.add(Keys.PLAYERS_COMMAND.getKey(), twitchCommand(command));
            }
            else if (command instanceof RewardsCommand) {
                jsonObject.add(Keys.REWARDS_COMMAND.getKey(), twitchCommand(command));
            }
        });
        return jsonObject;
    }

    @Override
    protected final void update(BaseGUI prevGUI) {
        JsonObject jsonObject = new JsonObject();
        JsonObject discovery = discovery();
        jsonObject.add(Keys.DISCOVERY.getKey(), discovery);
        JsonObject general = general();
        jsonObject.add(Keys.GENERAL.getKey(), general);
        JsonObject streamLabs = streamLabs();
        jsonObject.add(Keys.STREAM_LABS.getKey(), streamLabs);
        JsonObject twitch = twitch();
        jsonObject.add(Keys.TWITCH.getKey(), twitch);
        if (isClientConfig) {
            try {
                DonationHavok.INSTANCE.saveConfig(GSON.fromJson(discovery, DiscoveryHandler.class), GSON.fromJson(general, HavokRewardsHandler.class), GSON.fromJson(jsonObject, StreamLabsHandler.class), GSON.fromJson(twitch, TwitchHandler.class));
                DonationHavok.INSTANCE.getTwitchHandler().connect();
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
