package io.musician101.donationhavok.configurator.gui;

import java.text.NumberFormat;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;

public final class OldConfigGui extends BaseGui {

    public JTable rewards;
    public JTable twitchCommands;
    //JList<IBlockState> nonReplaceableBlocks;
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

    public OldConfigGui() {
        parseJFrame("DonationHavok Configurator", this::mainPanel);
    }

    /*private JPanel buttonsPanel(JFrame frame) {
        JPanel panel = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update();
            frame.setVisible(false);
        });
        saveButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.setVisible(false));
        cancelButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return panel;
    }

    private JPanel centerGeneralPanel(TwitchConfig tc) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Twitch", SwingConstants.CENTER), gbc(0, 0));
        twitchEnabled = new JCheckBox("Enable Twitch integration?", tc.isEnabled());
        panel.add(twitchEnabled, gbc(0, 1));
        panel.add(parseJLabel("Channel name:", SwingConstants.LEFT), gbc(0, 2));
        channelName = new JTextField(tc.getTwitchName());
        panel.add(channelName, gbc(0, 3));
        bitsTrigger = new JCheckBox("Bits trigger rewards?", tc.doBitsTrigger());
        panel.add(bitsTrigger, gbc(0, 4));
        subsTrigger = new JCheckBox("Subscriptions trigger rewards?", tc.doSubsTrigger());
        panel.add(subsTrigger, gbc(0, 5));
        roundSubs = new JCheckBox("Round subscriptions up to the nearest dollar?", tc.roundSubs());
        panel.add(roundSubs, gbc(0, 6));
        factorSubStreak = new JCheckBox("Multiply subscription tier by streak?", tc.factorSubStreak());
        panel.add(factorSubStreak, gbc(0, 7));
        TwitchCommandsConfig tCmd = tc.getTwitchCommandsConfig();
        twitchCommandsEnabled = new JCheckBox("Enable Twitch chat commands?", tc.isEnabled());
        panel.add(twitchCommandsEnabled, gbc(0, 8));
        twitchCommands = parseJTable(new TwitchCommandsModel(new ArrayList<>(ImmutableMap.<String, TwitchCommandConfig>builder().put("discovery", tCmd.getDiscoveryCommandConfig()).put("players", tCmd.getPlayersCommandConfig()).put("rewards", tCmd.getRewardsCommandConfig()).build().entrySet())), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = twitchCommands.getSelectedRow();
                if (row == -1) {
                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    Entry<String, TwitchCommandConfig> command = ((TwitchCommandsModel) twitchCommands.getModel()).getObjectAt(row);
                    new TwitchCommandGui(command.getKey(), command.getValue(), row, OldConfigGui.this);
                }
            }
        });
        panel.add(new JScrollPane(twitchCommands), gbc(0, 9));
        panel.add(flowLayoutPanel(parseJButton("Edit", l -> {
            int row = twitchCommands.getSelectedRow();
            if (row == -1) {
                return;
            }

            Entry<String, TwitchCommandConfig> command = ((TwitchCommandsModel) twitchCommands.getModel()).getObjectAt(row);
            new TwitchCommandGui(command.getKey(), command.getValue(), row, OldConfigGui.this);
        })), gbc(0, 19));
        return panel;
    }

    private JsonObject rewards() {
        JsonObject jsonObject = new JsonObject();
        "delay".serialize(Integer.valueOf(rewardsDelay.getValue().toString()), jsonObject);
        Keys.GENERATE_BOOK.serialize(generateBook.isSelected(), jsonObject);
        Keys.MC_NAME.serialize(mcName.getText(), jsonObject);
        Keys.NON_REPLACEABLE_BLOCKS.serialize(((SortedListModel<IBlockState>) nonReplaceableBlocks.getModel()).getElements().stream().map(IBlockState::getBlock).map(ForgeRegistries.BLOCKS::getKey).filter(Objects::nonNull).map(ResourceLocation::toString).collect(Collectors.toList()), jsonObject);
        Keys.REPLACE_UNBREAKABLE_BLOCKS.serialize(replaceUnbreakableBlocks.isSelected(), jsonObject);
        TreeMap<Double, HavokRewards> rewards = new TreeMap<>(Double::compare);
        rewards.putAll(((HavokRewardsTableModel) this.rewards.getModel()).getRewards());
        Keys.REWARDS.serialize(new TempHavokRewardsStorage(rewards), jsonObject);
        return jsonObject;
    }

    private JPanel generalPanel() {
        JPanel panel = gridBagLayoutPanel();
        panel.add(leftGeneralPanel(config.getRewardsConfig(), config.getStreamlabsConfig()), gbc(0, 0));
        panel.add(centerGeneralPanel(config.getTwitchConfig()), gbc(1, 0));
        panel.add(rightGeneralPanel(config.getRewardsConfig()), gbc(2, 0));
        return panel;
    }

    private JPanel leftGeneralPanel(RewardsConfig rc, StreamlabsConfig slc) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("General", SwingConstants.CENTER), gbc(0, 0));
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 1));
        rewardsDelay.setValue(rc.getDelay());
        panel.add(rewardsDelay, gbc(0, 2));
        panel.add(parseJLabel("Minecraft Name:", SwingConstants.LEFT), gbc(0, 3));
        mcName = new JTextField(rc.getMCName());
        panel.add(mcName, gbc(0, 4));
        generateBook = new JCheckBox("Generate book?", rc.generateBook());
        panel.add(generateBook, gbc(0, 5));
        hideUntilDiscovered = new JCheckBox("Hide Rewards Until Discovered?");
        panel.add(hideUntilDiscovered, gbc(0, 6));
        replaceUnbreakableBlocks = new JCheckBox("Replace unbreakable blocks?", rc.replaceUnbreakableBlocks());
        panel.add(replaceUnbreakableBlocks, gbc(0, 7));
        panel.add(parseJLabel("Non-Replaceable Blocks", SwingConstants.CENTER), gbc(0, 8));
        nonReplaceableBlocks = new JList<>(new SortedListModel<>(rc.getNonReplaceableBlocks(), new BlockStateIDComparator()));
        nonReplaceableBlocks.setCellRenderer(new IBlockStateCellRenderer());
        panel.add(new JScrollPane(nonReplaceableBlocks), gbc(0, 9));
        JButton editButton = parseJButton("Edit", l -> new AddNonReplaceableBlocksGui(this));
        editButton.setPreferredSize(new Dimension(195, 26));
        panel.add(flowLayoutPanel(editButton), gbc(0, 10));
        panel.add(streamLabsPanel(slc), gbc(0, 11));
        return flowLayoutPanel(panel);
    }*/

    @Override
    protected JPanel mainPanel(JFrame frame) {
        JPanel panel = gridBagLayoutPanel();
        //panel.add(generalPanel(), gbc(0, 0));
        //panel.add(buttonsPanel(frame), gbc(0, 1));
        return panel;
    }

    /*private JPanel rightGeneralPanel(RewardsConfig rc) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("Rewards (Double click any row to edit)", SwingConstants.CENTER), gbc(0, 0));
        rewards = parseJTable(new HavokRewardsTableModel(rc.getRewards()), new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rewards.getSelectedRow();
                HavokRewardsTableModel model = (HavokRewardsTableModel) rewards.getModel();
                if (row == -1) {
                    return;
                }

                double amount = model.getMinAmountAt(row);
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    new RewardsGui(amount, model.getRewardsAt(row), row, OldConfigGui.this);
                }
            }
        });
        panel.add(new JScrollPane(rewards), gbc(0, 1));
        JPanel rewardsTableButtons = gridBagLayoutPanel();
        JButton newButton = parseJButton("New", l -> new RewardsGui(0, new HavokRewards(), -1, this));
        rewardsTableButtons.add(flowLayoutPanel(newButton), gbc(0, 0));
        JButton editButton = parseJButton("Edit", l -> {
            int row = rewards.getSelectedRow();
            HavokRewardsTableModel model = (HavokRewardsTableModel) rewards.getModel();
            if (row == -1) {
                return;
            }

            double amount = model.getMinAmountAt(row);
            new RewardsGui(amount, model.getRewardsAt(row), row, OldConfigGui.this);
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

    private JsonObject streamlabs() {
        JsonObject jsonObject = new JsonObject();
        Keys.ACCESS_TOKEN.serialize(new String(accessToken.getPassword()), jsonObject);
        "delay".serialize(Integer.valueOf(streamLabsDelay.getValue().toString()), jsonObject);
        Keys.ENABLE.serialize(streamLabsEnabled.isSelected(), jsonObject);
        return jsonObject;
    }

    private JPanel streamLabsPanel(StreamlabsConfig slc) {
        JPanel panel = gridBagLayoutPanel();
        panel.add(parseJLabel("StreamLabs", SwingConstants.CENTER), gbc(0, 0));
        streamLabsEnabled = new JCheckBox("Enable StreamLabs donations?", slc.isEnabled());
        panel.add(streamLabsEnabled, gbc(0, 1));
        panel.add(parseJLabel("Access Token:", SwingConstants.LEFT), gbc(0, 2));
        accessToken = new JPasswordField(slc.getAccessToken());
        accessToken.setEchoChar('*');
        panel.add(accessToken, gbc(0, 3));
        JCheckBox showAccessToken = new JCheckBox("Show access token?");
        showAccessToken.addActionListener(l -> accessToken.setEchoChar(showAccessToken.isSelected() ? (char) 0 : '*'));
        panel.add(showAccessToken, gbc(0, 4));
        panel.add(parseJLabel("Delay:", SwingConstants.LEFT), gbc(0, 5));
        streamLabsDelay = new JFormattedTextField(NumberFormat.getIntegerInstance());
        streamLabsDelay.setValue(slc.getDelay());
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

    private JsonObject twitchCommands() {
        JsonObject jsonObject = new JsonObject();
        Keys.ENABLE.serialize(twitchCommandsEnabled.isSelected(), jsonObject);
        ((TwitchCommandsModel) twitchCommands.getModel()).getElements().forEach(command -> {
            switch (command.getKey()) {
                case "discovery":
                    Keys.DISCOVERY_COMMAND.serialize(command.getValue(), jsonObject);
                    break;
                case "players":
                    Keys.PLAYERS_COMMAND.serialize(command.getValue(), jsonObject);
                    break;
                case "rewards":
                    Keys.REWARDS_COMMAND.serialize(command.getValue(), jsonObject);
                    break;
            }
        });
        return jsonObject;
    }

    private JsonObject config() {
        JsonObject jsonObject = new JsonObject();
        Keys.ENABLE.serialize(hideUntilDiscovered.isSelected(), jsonObject);
        jsonObject.add(Keys.REWARDS.getKey(), rewards());
        jsonObject.add(Keys.STREAMLABS_CONFIG.getKey(), streamlabs());
        jsonObject.add(Keys.TWITCH_CONFIG.getKey(), twitch());
        return jsonObject;
    }*/

    @Override
    protected final void update() {
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.add(Keys.CONFIG.getKey(), config());
        if (isClientConfig) {
            try {
                Optional<Config> config = Keys.CONFIG.deserialize(jsonObject);
                if (config.isPresent()) {
                    DonationHavok.getInstance().setConfig(config.get());
                    return;
                }

                JOptionPane.showConfirmDialog(null, "An error occurred while saving the config.", "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
            }
            catch (Exception e) {
                JOptionPane.showConfirmDialog(null, "An error occurred while saving the config.\n" + e.getMessage(), "Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
            }
        }
        else {
            Network.INSTANCE.sendToServer(new JsonMessage(jsonObject));
        }*/
    }
}
