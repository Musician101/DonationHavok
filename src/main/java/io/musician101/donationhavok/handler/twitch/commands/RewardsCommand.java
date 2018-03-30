package io.musician101.donationhavok.handler.twitch.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.discovery.Discovery;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.handler.havok.HavokRewards;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class RewardsCommand extends Command {

    RewardsCommand() {
        this(false, Collections.singletonList(CommandPermission.BROADCASTER));
    }

    public RewardsCommand(boolean enabled, @Nonnull List<CommandPermission> permission) {
        super("rewards", "List the available reward tiers.", "!rewards [amount]", enabled, permission);
    }

    @Override
    public void executeCommand(String user, String channel, String[] args) {
        HavokRewardsHandler hrh = DonationHavok.INSTANCE.getRewardsHandler();
        String atUser = "@" + user;
        if (args.length != 0) {
            double tier;
            try {
                tier = Double.valueOf(args[0]);
            }
            catch (NumberFormatException e) {
                bot.sendMessage(channel, atUser + ", " + args[0] + " is not a number.");
                return;
            }

            DiscoveryHandler discoveryHandler = DonationHavok.INSTANCE.getDiscoveryHandler();
            Optional<HavokRewards> rewards = hrh.getRewards(tier);
            if (discoveryHandler.hideCurrentUntilDiscovered()) {
                Optional<Discovery> discovery = discoveryHandler.getCurrentDiscovery(tier);
                if (discovery.isPresent()) {
                    bot.sendMessage(atUser + ", for " + tier + " you can trigger"  + discovery.get().getRewardName() + ".", channel);
                }
                else {
                    if (rewards.isPresent()) {
                        bot.sendMessage(atUser + ", that reward hasn't been discovered yet.", channel);
                    }
                    else {
                        bot.sendMessage(atUser + ", that amount is not enough to trigger any rewards.", channel);
                    }
                }
            }
            else {
                if (rewards.isPresent()) {
                    bot.sendMessage(atUser + ", for " + tier + " you can trigger " + rewards.get().getName() + ".", channel);
                }
                else {
                    bot.sendMessage(atUser + ", that amount is not enough to trigger any rewards.", channel);
                }
            }

            return;
        }

        bot.sendMessage(atUser + ", here are the available tiers: " + StringUtils.join(hrh.getRewards().keySet(), ", "), channel);
    }

    public static class Serializer extends BaseSerializer<RewardsCommand> {

        @Override
        public RewardsCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new RewardsCommand(deserialize(jsonObject, context, Keys.ENABLE, false), deserialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, Collections.singletonList(CommandPermission.BROADCASTER)));
        }

        @Override
        public JsonElement serialize(RewardsCommand src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, new ArrayList<>(src.getRequiredPermissions()));
            return jsonObject;
        }
    }
}
