package io.musician101.donationhavok.handler.twitch.commands;

import io.musician101.donationhavok.handler.twitch.TwitchBot;
import io.musician101.donationhavok.handler.twitch.event.MessageEvent;
import java.util.Collections;
import java.util.List;

public abstract class Command {

    private final String command;

    private final String description;
    private final boolean enabled;
    private final List<CommandPermission> requiredPermissions;
    private final String usage;
    TwitchBot bot;

    Command(String command, String description, String usage, boolean enabled, List<CommandPermission> permissions) {
        this.command = command;
        this.description = description;
        this.usage = usage;
        this.enabled = enabled;
        this.requiredPermissions = Collections.unmodifiableList(permissions);
    }

    public abstract void executeCommand(String user, String channel, String[] args);

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public List<CommandPermission> getRequiredPermissions() {
        return requiredPermissions;
    }

    String getUsage() {
        return usage;
    }

    public Boolean hasPermissions(MessageEvent messageEvent) {
        for (CommandPermission permission : messageEvent.getPermissions()) {
            if (getRequiredPermissions().contains(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setBot(TwitchBot bot) {
        if (this.bot == null) {
            this.bot = bot;
        }
    }
}
