package io.musician101.donationhavok.configurator.gui;

public class TwitchCommandGui {/*extends BaseGui {

    private final int index;
    private final String name;
    private JCheckBox broadcaster;
    private JCheckBox enabled;
    private JCheckBox everyone;
    private JCheckBox moderator;
    private JCheckBox partner;
    private JCheckBox primeTurbo;
    private JCheckBox subscriber;

    public TwitchCommandGui(String name, TwitchCommandConfig command, int index, OldConfigGui prevGUI) {
        this.index = index;
        this.name = name;
        parseJFrame("!" + name + " Command Settings", prevGUI, f -> mainPanel(f, command, prevGUI));
    }

    private JPanel buttonsPanel(JFrame frame, OldConfigGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        JButton saveButton = parseJButton("Save", l -> {
            update(prevGUI);
            frame.dispose();
        });
        panel.add(flowLayoutPanel(saveButton), gbc(0, 0));
        JButton cancelButton = parseJButton("Cancel", l -> frame.dispose());
        panel.add(flowLayoutPanel(cancelButton), gbc(1, 0));
        return flowLayoutPanel(panel);
    }

    private JPanel mainPanel(JFrame frame, TwitchCommandConfig command, OldConfigGui prevGUI) {
        JPanel panel = gridBagLayoutPanel();
        enabled = new JCheckBox("Enabled?", command.isEnabled());
        panel.add(enabled, gbc(0, 0));
        List<CommandPermission> permissions = command.getPermissions();
        everyone = new JCheckBox("Allow everyone to use this command?", permissions.contains(CommandPermission.EVERYONE));
        panel.add(everyone, gbc(0, 1));
        primeTurbo = new JCheckBox("Allow Twitch Prime/Twitch Turbo users to use this command?", permissions.contains(CommandPermission.PRIME_TURBO));
        panel.add(primeTurbo, gbc(0, 2));
        partner = new JCheckBox("Allow Twitch partners to use this command?", permissions.contains(CommandPermission.PARTNER));
        panel.add(partner, gbc(0, 3));
        subscriber = new JCheckBox("Allow your subscribers to use this command?", permissions.contains(CommandPermission.SUBSCRIBER));
        panel.add(subscriber, gbc(0, 4));
        moderator = new JCheckBox("Allow your moderators to use this command?", permissions.contains(CommandPermission.MODERATOR));
        panel.add(moderator, gbc(0, 5));
        broadcaster = new JCheckBox("Allow only you to use this command?", permissions.contains(CommandPermission.BROADCASTER));
        panel.add(broadcaster, gbc(0, 6));
        panel.add(buttonsPanel(frame, prevGUI), gbc(0, 7));
        return panel;
    }

    private List<CommandPermission> permissions() {
        List<CommandPermission> list = new ArrayList<>();
        if (everyone.isSelected()) {
            list.add(CommandPermission.EVERYONE);
        }

        if (primeTurbo.isSelected()) {
            list.add(CommandPermission.PRIME_TURBO);
        }

        if (partner.isSelected()) {
            list.add(CommandPermission.PARTNER);
        }

        if (subscriber.isSelected()) {
            list.add(CommandPermission.SUBSCRIBER);
        }

        if (moderator.isSelected()) {
            list.add(CommandPermission.MODERATOR);
        }

        if (broadcaster.isSelected()) {
            list.add(CommandPermission.BROADCASTER);
        }

        return list;
    }

    @Override
    protected void update(OldConfigGui prevGUI) {
        TwitchCommandsModel model = (TwitchCommandsModel) prevGUI.twitchCommands.getModel();
        TwitchCommandConfig command = model.getObjectAt(index).getValue();
        model.replace(index, new SimpleEntry<>(name, command));
    }*/
}
