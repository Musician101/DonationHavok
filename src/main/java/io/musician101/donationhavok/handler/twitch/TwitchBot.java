package io.musician101.donationhavok.handler.twitch;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.havok.HavokRewardsHandler;
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
import java.util.concurrent.Executors;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

public final class TwitchBot implements Runnable {

    private final CommandHandler commandHandler = new CommandHandler();
    private final String streamerName;
    private boolean listenForBits = false;
    private boolean listenForSubs = false;
    private BufferedReader reader;
    private boolean stopped = true;
    private BufferedWriter writer;

    TwitchBot(String streamerName) {
        this.streamerName = streamerName;
    }

    public void connect() {
        if (isRunning()) {
            return;
        }

        Logger logger = DonationHavok.INSTANCE.getLogger();
        try {
            @SuppressWarnings("resource") Socket socket = new Socket("irc.twitch.tv", 6667);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.write("PASS @TOKEN@ \r\n");
            writer.write("NICK DonationHavokBot\r\n");
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
            DonationHavok.INSTANCE.getLogger().error("Bot failed to close read/write!", e);
        }
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    private void handleEvent(MessageEvent event) {
        Map<String, String> tags = event.getTags();
        String commandType = event.getCommandType();
        if (commandType.equals("USERNOTICE") && tags.containsKey("msg-id")) {
            String msgId = tags.get("msg-id");
            if (listenForSubs) {
                switch (msgId) {
                    case "sub":
                    case "resub":
                    case "anonsubgift":
                    case "submysterygift":
                    case "giftpaidupgrade":
                    case "rewardgift":
                    case "anongiftpaidupgrade":
                        String channel = event.getChannelName().orElse("");
                        String user = event.getTagValue("display-name").orElse("");
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

                        Subscription subscription = new Subscription(channel, user, subPlan, isResub, streak, event.getMessage().orElse(""));
                        runSubscription(subscription);
                }
            }
        }
        else if (event.getCommandType().equals("PRIVMSG")) {
            if (event.getTags().containsKey("bits") && listenForBits) {
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
                Cheer cheer = new Cheer(channel, user, bits, message);
                runCheer(cheer);
            }

            event.getMessage().filter(m -> m.startsWith("!")).ifPresent(m -> commandHandler.processCommand(m, event.getTagValue("display-name").orElse(""), event.getPermissions()));
        }
    }

    public boolean isListeningForBits() {
        return listenForBits;
    }

    public boolean isListeningForSubs() {
        return listenForSubs;
    }

    public void setListeningForSubs(boolean listenForSubs) {
        this.listenForSubs = listenForSubs;
    }

    public boolean isRunning() {
        return !stopped;
    }

    public void joinChannel(String channel) {
        sendRawMessage("JOIN #" + channel + "\r\n");
        DonationHavok.INSTANCE.getLogger().info("> JOIN " + channel);
    }

    public void leaveChannel(String channel) {
        sendRawMessage("PART #" + channel);
        DonationHavok.INSTANCE.getLogger().info("> PART " + channel);
    }

    @Override
    public void run() {
        connect();
        joinChannel(streamerName);
        start();
    }

    public void runCheer(Cheer cheer) {
        FMLCommonHandler.instance().getMinecraftServerInstance().callFromMainThread(Executors.callable(() -> {
            HavokRewardsHandler handler = DonationHavok.INSTANCE.getRewardsHandler();
            handler.getPlayer().ifPresent(player -> handler.getRewards(cheer.getBits() / 100D).ifPresent(rewards -> {
                DonationHavok.INSTANCE.getDiscoveryHandler().rewardsDiscovered(handler.getRewards().floorKey(cheer.getBits() / 100D), cheer.getUser(), rewards);
                handler.generateBook(player, cheer.getUser(), cheer.getMessage(), rewards);
                rewards.wreak(cheer);
            }));
        }));
    }

    public void runSubscription(Subscription subscription) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
            TwitchHandler twitchHandler = DonationHavok.INSTANCE.getTwitchHandler();
            boolean factorSubStreak = twitchHandler.factorSubStreak();
            boolean roundSubs = twitchHandler.roundSubs();
            double amount;
            int streak = subscription.getStreak();
            switch (subscription.getSubPlan()) {
                case TIER_2:
                    amount = Math.max(roundSubs ? 10 : 9.99, (roundSubs ? 10 : 9.99) * (factorSubStreak ? 1 : streak / 2));
                    break;
                case TIER_3:
                    amount = Math.max(roundSubs ? 25 : 24.99, (roundSubs ? 25 : 24.99) * (factorSubStreak ? 1 : streak / 6));
                    break;
                default:
                    amount = Math.max(roundSubs ? 5 : 4.99, (roundSubs ? 5 : 4.99) * (factorSubStreak ? 1 : streak));
                    break;
            }

            HavokRewardsHandler handler = DonationHavok.INSTANCE.getRewardsHandler();
            handler.getPlayer().ifPresent(player -> handler.getRewards(factorSubStreak ? amount * subscription.getStreak() : amount).ifPresent(rewards -> {
                DonationHavok.INSTANCE.getDiscoveryHandler().rewardsDiscovered(handler.getRewards().floorKey(factorSubStreak ? amount * subscription.getStreak() : amount), subscription.getUser(), rewards);
                handler.generateBook(player, subscription.getUser(), subscription.getMessage(), rewards);
                rewards.wreak(subscription);
            }));
        });
    }

    public void sendMessage(Object message, String channel) {
        try {
            writer.write("PRIVMSG #" + channel + " :" + message.toString() + "\r\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        DonationHavok.INSTANCE.getLogger().info("> MSG " + channel + " : " + message.toString());
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

    public void setIsListeningForBits(boolean listenForBits) {
        this.listenForBits = listenForBits;
    }

    public void start() {
        if (isRunning()) {
            return;
        }

        Logger logger = DonationHavok.INSTANCE.getLogger();
        String line;
        stopped = false;
        try {
            while ((line = reader.readLine()) != null) {
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

                if (stopped) {
                    break;
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
