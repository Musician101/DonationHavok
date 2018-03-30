package io.musician101.donationhavok.handler.twitch.commands;

import io.musician101.donationhavok.DonationHavok;
import io.musician101.donationhavok.handler.twitch.event.MessageEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.Logger;

public class CommandHandler {

    private final Map<String, Command> commandMap = new HashMap<>();

    public Collection<Command> getAllCommands() {
        return getCommandMap().values();
    }

    public Optional<Command> getCommand(String name) {
        return Optional.ofNullable(commandMap.get(name));
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public void processCommand(MessageEvent messageEvent) {
        String message = messageEvent.getMessage().orElse("");
        String commandTrigger = "!";
        String cmdTrigger = message.substring(0, commandTrigger.length());
        String cmdName;

        if (!message.startsWith(commandTrigger)) {
            return;
        }

        if (message.contains(" ")) {
            cmdName = message.substring(commandTrigger.length(), message.indexOf(" "));
        }
        else {
            cmdName = message.substring(commandTrigger.length(), message.length());
        }

        Optional<Command> cmd = getCommand(cmdName);
        if (cmd.isPresent()) {
            Command command = cmd.get();
            Logger logger = DonationHavok.INSTANCE.getLogger();
            if (command.getEnabled()) {
                if (!cmdTrigger.equals(commandTrigger) && !cmdTrigger.equals("")) {
                    return;
                }

                if (command.hasPermissions(messageEvent)) {
                    logger.info("Received command {} from user {}.!", message, messageEvent.getUser().orElse(""));

                    cmd.get().executeCommand(messageEvent.getUser().orElse(""), messageEvent.getChannelName().orElse(""), messageEvent.getMessage().map(m -> {
                        List<String> args = Arrays.asList(m.split(" "));
                        if (args.isEmpty()) {
                            return new String[0];
                        }

                        List<String> shiftedArgs = new ArrayList<>(args);
                        shiftedArgs.remove(0);
                        return shiftedArgs.toArray(new String[0]);
                    }).orElse(new String[0]));
                }
                else {
                    logger.info("Access to command {} denied for user {}! (Missing Permissions)", cmdName, messageEvent.getUser());
                }
            }
            else {
                logger.info("Access to command {} denied for user {}.! (Command Disabled)", message, messageEvent.getUser());
            }
        }
    }

    public void registerCommand(Command command) {
        Logger logger = DonationHavok.INSTANCE.getLogger();
        if (getCommandMap().containsKey(command.getCommand())) {
            logger.error("Can't register Command! {}! Error: Command was already registered!", command.getCommand());
            return;
        }

        if (command.getCommand().length() > 0) {
            getCommandMap().put(command.getCommand(), command);
        }

        logger.info("Registered new Command {}!", command.getCommand());
    }

    public void unregisterCommand(Command command) {
        getCommandMap().remove(command.getCommand(), command);
    }
}
