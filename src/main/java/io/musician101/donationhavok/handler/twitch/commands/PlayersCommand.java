package io.musician101.donationhavok.handler.twitch.commands;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.musician101.donationhavok.util.json.Keys;
import io.musician101.donationhavok.util.json.adapter.BaseSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.StringUtils;

public class PlayersCommand extends Command {

    PlayersCommand() {
        this(false, Collections.singletonList(CommandPermission.BROADCASTER));
    }

    public PlayersCommand(boolean enabled, @Nonnull List<CommandPermission> permissions) {
        super("players", "Show a list of players that are currently on the server or if the specified player is online.", "!players [name]", enabled, permissions);
    }

    @Override
    public void executeCommand(String user, String channel, String[] args) {
        String atUser = "@" + user;
        FMLCommonHandler fml = FMLCommonHandler.instance();
        if (fml.getSide().isServer()) {
            if (args.length == 0) {
                bot.sendMessage(atUser + ", here's the current list of online players: " + StringUtils.join(getOnlinePlayerNames(), ", "), channel);
            }
            else {
                String name = args[0];
                boolean isOnline = Stream.of(getOnlinePlayerNames()).anyMatch(s -> s.equalsIgnoreCase(name));
                bot.sendMessage(atUser + ", " + name + " is " + (isOnline ? "online" : "offline") + ".", channel);
            }
        }
        else {
            bot.sendMessage(atUser + ", this command only works if the streamer is on a server that has DonationHavok installed.", channel);
        }
    }

    private String[] getOnlinePlayerNames() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOnlinePlayerNames();
    }

    public static class Serializer extends BaseSerializer<PlayersCommand> {

        @Override
        public PlayersCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new PlayersCommand(deserialize(jsonObject, context, Keys.ENABLE, false), deserialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, Collections.singletonList(CommandPermission.BROADCASTER)));
        }

        @Override
        public JsonElement serialize(PlayersCommand src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            serialize(jsonObject, context, Keys.ENABLE, src.isEnabled());
            serialize(jsonObject, context, Keys.COMMAND_PERMISSIONS, new ArrayList<>(src.getRequiredPermissions()));
            return jsonObject;
        }
    }
}
