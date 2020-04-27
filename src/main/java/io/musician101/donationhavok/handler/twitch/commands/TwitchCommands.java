package io.musician101.donationhavok.handler.twitch.commands;

import io.musician101.donationhavok.handler.twitch.TwitchBot;
import javax.annotation.Nonnull;

public class TwitchCommands {

    @Nonnull
    private final DiscoveryCommand discoveryCommand = new DiscoveryCommand();
    @Nonnull
    private final PlayersCommand playersCommand = new PlayersCommand();
    @Nonnull
    private final RewardsCommand rewardsCommand = new RewardsCommand();


    public TwitchCommands() {

    }

    @Nonnull
    public DiscoveryCommand getDiscoveryCommand() {
        return discoveryCommand;
    }

    @Nonnull
    public PlayersCommand getPlayersCommand() {
        return playersCommand;
    }

    @Nonnull
    public RewardsCommand getRewardsCommand() {
        return rewardsCommand;
    }

    public void register(TwitchBot twitchBot) {
        CommandHandler ch = twitchBot.getCommandHandler();
        if (discoveryCommand.isEnabled()) {
            discoveryCommand.setBot(twitchBot);
            ch.registerCommand(discoveryCommand);
        }

        if (playersCommand.isEnabled()) {
            playersCommand.setBot(twitchBot);
            ch.registerCommand(playersCommand);
        }

        if (rewardsCommand.isEnabled()) {
            rewardsCommand.setBot(twitchBot);
            ch.registerCommand(rewardsCommand);
        }
    }
}
