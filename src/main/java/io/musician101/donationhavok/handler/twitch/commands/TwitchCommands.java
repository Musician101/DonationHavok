package io.musician101.donationhavok.handler.twitch.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.handler.twitch.TwitchBot;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import javax.annotation.Nonnull;

public class TwitchCommands {

    @Nonnull
    private final DiscoveryCommand discoveryCommand;
    private final boolean enabled;
    @Nonnull
    private final PlayersCommand playersCommand;
    @Nonnull
    private final RewardsCommand rewardsCommand;

    public TwitchCommands() {
        this(false, new DiscoveryCommand(), new PlayersCommand(), new RewardsCommand());
    }

    private TwitchCommands(boolean enabled, @Nonnull DiscoveryCommand discoveryCommand, @Nonnull PlayersCommand playersCommand, @Nonnull RewardsCommand rewardsCommand) {
        this.enabled = enabled;
        this.discoveryCommand = discoveryCommand;
        this.playersCommand = playersCommand;
        this.rewardsCommand = rewardsCommand;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void register(TwitchBot twitchBot) {
        if (enabled) {
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

    public static class Serializer extends BaseSerializer<TwitchCommands> {

        @Override
        public TwitchCommands deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            boolean enabled = deserialize(jsonObject, context, Keys.ENABLE, false);
            DiscoveryCommand discoveryCommand = deserialize(jsonObject, context, Keys.DISCOVERY_COMMAND, new DiscoveryCommand());
            PlayersCommand playerListCommand = deserialize(jsonObject, context, Keys.PLAYERS_COMMAND, new PlayersCommand());
            RewardsCommand rewardsListCommand = deserialize(jsonObject, context, Keys.REWARDS_COMMAND, new RewardsCommand());
            return new TwitchCommands(enabled, discoveryCommand, playerListCommand, rewardsListCommand);
        }

        @Override
        public JsonElement serialize(TwitchCommands src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.DISCOVERY_COMMAND, src.discoveryCommand);
            serialize(jsonObject, context, Keys.PLAYERS_COMMAND, src.playersCommand);
            serialize(jsonObject, context, Keys.REWARDS_COMMAND, src.rewardsCommand);
            return jsonObject;
        }
    }
}
