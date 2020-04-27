package io.musician101.donationhavok.handler.twitch.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.discovery.Discovery;
import io.musician101.donationhavok.handler.discovery.DiscoveryHandler;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class DiscoveryCommand extends Command {

    DiscoveryCommand() {
        this(false, Collections.singletonList(CommandPermission.BROADCASTER));
    }

    public DiscoveryCommand(boolean enabled, @Nonnull List<CommandPermission> permissions) {
        super("discovery", "View a list of donors who discovered rewards of the past and present.", "!discovery <current | legendary>", enabled, permissions);
    }

    @Override
    public void executeCommand(String user, String channel, String[] args) {
        DiscoveryHandler discoveryHandler = DonationHavok.getInstance().getDiscoveryHandler();
        String atUser = "@" + user;
        if (args.length != 0) {
            String type = args[0];
            if (type.equalsIgnoreCase("current")) {
                sendDiscoveries(channel, atUser, discoveryHandler.getCurrentDiscoveries());
                return;
            }
            else if (type.equalsIgnoreCase("legendary")) {
                sendDiscoveries(channel, atUser, discoveryHandler.getLegendaryDiscoveries());
                return;
            }
        }

        bot.sendMessage(atUser + ", invalid usage: " + getUsage(), channel);
    }

    private void sendDiscoveries(String channel, String user, List<Discovery> discoveries) {
        if (discoveries.isEmpty()) {
            bot.sendMessage(user + ", no one has discovered any of the current rewards yet.", channel);
        }
        else {
            bot.sendMessage(user + ", " + StringUtils.join(discoveries.stream().map(discovery -> discovery.getDonorName() + ": " + discovery.getRewardName() + " ($" + discovery.getAmount() + ")").collect(Collectors.toList()), ", "), channel);
        }
    }

    public static class Serializer extends BaseSerializer<DiscoveryCommand> {

        @Override
        public DiscoveryCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new DiscoveryCommand(deserialize(jsonObject, context, Keys.ENABLE, false), deserialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, Collections.singletonList(CommandPermission.BROADCASTER)));
        }

        @Override
        public JsonElement serialize(DiscoveryCommand src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, new ArrayList<>(src.getRequiredPermissions()));
            return jsonObject;
        }
    }
}
