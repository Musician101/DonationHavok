package io.musician101.donationhavok.handler.twitch;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.Reference;
import io.musician101.donationhavok.config.Config;
import io.musician101.donationhavok.config.RewardsConfig;
import io.musician101.donationhavok.config.TwitchConfig;
import io.musician101.donationhavok.handler.SaleHandler;
import io.musician101.donationhavok.handler.twitch.commands.CommandHandler;
import io.musician101.donationhavok.handler.twitch.event.Cheer;
import io.musician101.donationhavok.handler.twitch.event.MessageEvent;
import io.musician101.donationhavok.handler.twitch.event.SubPlan;
import io.musician101.donationhavok.handler.twitch.event.Subscription;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import org.apache.logging.log4j.Logger;

public final class TwitchBot implements Runnable {

    private final CommandHandler commandHandler = new CommandHandler();
    private BufferedReader reader;
    private boolean stopped = true;
    private BufferedWriter writer;

    TwitchBot() {

    }

    public void connect() {
        if (isRunning()) {
            return;
        }

        Logger logger = DonationHavok.getInstance().getLogger();
        try {
            Socket socket = new Socket("irc.twitch.tv", 6667);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.write("PASS oauth:fuyawivjrn1wubw9ublrqm1auc36x3 \r\n");
            writer.write("NICK DonationHavokBot\r\n");
            writer.write("USER " + Reference.MOD_NAME + "-" + Reference.VERSION + " \r\n");
            writer.write("CAP REQ :twitch.tv/tags \r\n");
            writer.write("CAP REQ :twitch.tv/commands \r\n");
            writer.write("CAP REQ :twitch.tv/membership \r\n");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("004")) {
                    logger.info("Connected >> DonationHavokBot ~ irc.twitch.tv");
                    break;
                }
                else {
                    logger.info(line);
                }
            }

            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        catch (IOException e) {
            DonationHavok.getInstance().getLogger().error("Bot failed to close read/write!", e);
        }
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void handleEvent(MessageEvent event) {
        DonationHavok instance = DonationHavok.getInstance();
        Config config = instance.getConfig();
        TwitchConfig twitchConfig = config.getTwitchConfig();
        Map<String, String> tags = event.getTags();
        String commandType = event.getCommandType();
        if (commandType.equals("USERNOTICE") && tags.containsKey("msg-id")) {
            String msgId = tags.get("msg-id");
            if ((msgId.equals("sub") || msgId.equals("resub") || msgId.equals("subgift")) && twitchConfig.doSubsTrigger()) {
                String channel = event.getChannelName().orElse("");
                String subBuyer = event.getTagValue("display-name").orElse("");
                String subReceiver = event.getTagValue("msg-param-recipient-id").orElse(subBuyer);
                SubPlan subPlan = event.getTagValue("msg-param-sub-plan").flatMap(SubPlan::fromString).orElse(SubPlan.UNKNOWN);
                boolean isResub = event.getTagValue("msg-id").filter(id -> id.equalsIgnoreCase("resub")).isPresent();
                int streak = event.getTagValue("msg-param-months").map(s -> {
                    try {
                        return Integer.parseInt(s);
                    }
                    catch (NumberFormatException e) {
                        return 1;
                    }
                }).filter(i -> i > 0).orElse(1);

                runSubscription(new Subscription(channel, subBuyer, subReceiver, subPlan, isResub, streak, event.getMessage().orElse("")));
            }
        }
        else if (event.getCommandType().equals("PRIVMSG")) {
            if (event.getTags().containsKey("bits") && twitchConfig.doBitsTrigger()) {
                String channel = event.getChannelName().orElse("");
                String user = event.getTagValue("display-name").orElse("");
                String message = event.getMessage().orElse("");
                int bits = event.getTagValue("bits").map(s -> {
                    try {
                        return Integer.parseInt(s);
                    }
                    catch (NumberFormatException e) {
                        return 1;
                    }
                }).filter(i -> i > 0).orElse(1);
                runCheer(new Cheer(channel, user, bits, message));
            }
            else if (event.getMessage().filter(m -> m.startsWith("!")).isPresent()) {
                commandHandler.processCommand(event);
            }
        }
    }

    public static void runCheer(Cheer cheer) {
        LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).runImmediately(() -> {
            Config config = DonationHavok.getInstance().getConfig();
            RewardsConfig rewardsConfig = config.getRewardsConfig();
            config.getGeneralConfig().getPlayer().ifPresent(player -> rewardsConfig.getRewardContents(SaleHandler.getDiscountedAmount(cheer.getBits() / 100D)).ifPresent(rewards -> {
                DonationHavok.getInstance().getDiscoveryHandler().rewardsDiscovered(rewardsConfig.getRewards().floorKey(cheer.getBits() / 100D), cheer.getUser(), rewards);
                rewards.wreak(cheer);
            }));
        });
    }

    public static void runSubscription(Subscription subscription) {
        LogicalSidedProvider.WORKQUEUE.<MinecraftServer>get(LogicalSide.SERVER).runImmediately(() -> {
            TwitchConfig twitchConfig = DonationHavok.getInstance().getConfig().getTwitchConfig();
            boolean factorSubStreak = twitchConfig.factorSubStreak();
            boolean roundSubs = twitchConfig.roundSubs();
            double amount;
            int streak = subscription.getStreak();
            switch (subscription.getSubPlan()) {
                case TIER_2:
                    amount = Math.max(roundSubs ? 10 : 9.99, (roundSubs ? 10 : 9.99) * (factorSubStreak ? 1 : streak / 2D));
                    break;
                case TIER_3:
                    amount = Math.max(roundSubs ? 25 : 24.99, (roundSubs ? 25 : 24.99) * (factorSubStreak ? 1 : streak / 6D));
                    break;
                default:
                    amount = Math.max(roundSubs ? 5 : 4.99, (roundSubs ? 5 : 4.99) * (factorSubStreak ? 1 : streak));
                    break;
            }

            Config config = DonationHavok.getInstance().getConfig();
            RewardsConfig rewardsConfig = DonationHavok.getInstance().getConfig().getRewardsConfig();
            config.getGeneralConfig().getPlayer().ifPresent(player -> rewardsConfig.getRewardContents(SaleHandler.getDiscountedAmount(amount * (factorSubStreak ? subscription.getStreak() : 1))).ifPresent(rewards -> {
                DonationHavok.getInstance().getDiscoveryHandler().rewardsDiscovered(rewardsConfig.getRewards().floorKey(factorSubStreak ? amount * subscription.getStreak() : amount), subscription.getSubBuyer(), rewards);
                rewards.wreak(subscription);
            }));
        });
    }

    public boolean isRunning() {
        return !stopped;
    }

    public void joinChannel(String channel) {
        sendRawMessage("JOIN #" + channel + "\r\n");
        DonationHavok.getInstance().getLogger().info("> JOIN " + channel);
    }

    public void leaveChannel(String channel) {
        sendRawMessage("PART #" + channel);
        DonationHavok.getInstance().getLogger().info("> PART " + channel);
    }

    @Override
    public void run() {
        connect();
        joinChannel(DonationHavok.getInstance().getConfig().getTwitchConfig().getTwitchName());
        start();
    }

    public void sendMessage(Object message, String channel) {
        try {
            writer.write("PRIVMSG " + channel + " :" + message.toString() + "\r\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        DonationHavok.getInstance().getLogger().info("> MSG " + channel + " : " + message.toString());
    }

    public void sendRawMessage(Object message) {
        if (writer == null) {
            return;
        }

        try {
            writer.write(message + " \r\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (isRunning()) {
            return;
        }

        Logger logger = DonationHavok.getInstance().getLogger();
        String line;
        stopped = false;
        try {
            while ((line = reader.readLine()) != null && !stopped) {
                if (line.contains("PING :tmi.twitch.tv")) {
                    logger.debug("> PING");
                    logger.debug("< PONG " + line.substring(5));
                    writer.write("PONG " + line.substring(5) + "\r\n");
                    writer.flush();
                }
                else if (line.equals(":tmi.twitch.tv NOTICE * :Login authentication failed")) {
                    logger.error("Invalid IRC Credentials. Login failed!");
                }
                else if (line.toLowerCase().contains("disconnected")) {
                    logger.info(line);
                    connect();
                }
                else {
                    logger.debug("> " + line);
                    MessageEvent event = new MessageEvent(line);
                    handleEvent(event);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (!stopped) {
            stopped = true;
            sendRawMessage("Stopping");
        }
    }
}
