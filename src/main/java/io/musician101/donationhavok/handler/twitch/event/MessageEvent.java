package io.musician101.donationhavok.handler.twitch.event;

import io.musician101.donationhavok.handler.twitch.commands.CommandPermission;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class MessageEvent {

    private final Set<CommandPermission> clientPermissions = new HashSet<>();
    private final String rawMessage;
    private Map<String, String> badges = new HashMap<>();
    private String channelName = null;
    private String commandType = "UNKNOWN";
    private String message = null;
    private Map<String, String> tags = new HashMap<>();

    public MessageEvent(String rawMessage) {
        this.rawMessage = rawMessage;

        this.parseRawMessage();
        this.parsePermissions();
    }

    public Map<String, String> getBadges() {
        return badges;
    }

    public Optional<String> getChannelName() {
        return Optional.ofNullable(channelName);
    }

    public Set<CommandPermission> getClientPermissions() {
        return clientPermissions;
    }

    public String getCommandType() {
        return commandType;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public Set<CommandPermission> getPermissions() {
        return clientPermissions;
    }

    public Optional<String> getTagValue(String tagName) {
        if (tags.containsKey(tagName)) {
            String value = tags.get(tagName);
            if (StringUtils.isBlank(value)) {
                return Optional.empty();
            }

            value = value.replaceAll("\\\\s", " ");
            return Optional.of(value);
        }

        return Optional.empty();
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Optional<String> getUser() {
        return getTagValue("display-name");
    }

    public Boolean isValid() {
        return !commandType.equals("UNKNOWN");
    }

    private Map<String, String> parseBadges(String raw) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(raw)) {
            raw = raw.replace("\\s", " ");

            for (String tag : raw.split(",")) {
                String[] val = tag.split("/");
                final String key = val[0];
                String value = (val.length > 1) ? val[1] : null;
                map.put(key, value);
            }
        }

        return Collections.unmodifiableMap(map);
    }

    private void parsePermissions() {
        if (tags.containsKey("badges")) {
            boolean isChannelOwner = tags.containsKey("user-id") && tags.containsKey("room-id") && tags.get("user-id").equals(tags.get("room-id"));

            if (badges.containsKey("broadcaster") || isChannelOwner) {
                clientPermissions.add(CommandPermission.BROADCASTER);
                clientPermissions.add(CommandPermission.MODERATOR);
            }

            if (badges.containsKey("turbo")) {
                clientPermissions.add(CommandPermission.PRIME_TURBO);
            }

            if (badges.containsKey("partner")) {
                clientPermissions.add(CommandPermission.PARTNER);
            }
        }

        if (tags.containsKey("moderator") && tags.get("moderator").equals("1")) {
            clientPermissions.add(CommandPermission.MODERATOR);
        }

        if (tags.containsKey("turbo") && tags.get("turbo").equals("1")) {
            clientPermissions.add(CommandPermission.PRIME_TURBO);
        }

        if (tags.containsKey("subscriber") && tags.get("subscriber").equals("1")) {
            clientPermissions.add(CommandPermission.SUBSCRIBER);
        }

        clientPermissions.add(CommandPermission.EVERYONE);
    }

    private void parseRawMessage() {
        Pattern pattern = Pattern.compile("^(?:@(?<tags>.+?) )?(?<clientName>.+?)(?: (?<command>[A-Z0-9]+) )(?:#(?<channel>.*?) ?)?(?<payload>[:\\-\\+](?<message>.+))?$");
        Matcher matcher = pattern.matcher(rawMessage);

        if (matcher.matches()) {
            tags = parseTags(matcher.group("tags"));
            if (tags.containsKey("badges")) {
                badges = parseBadges(tags.get("badges"));
            }

            commandType = matcher.group("command");
            channelName = matcher.group("channel");
            message = matcher.group("message");
        }
    }

    private Map<String, String> parseTags(String raw) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(raw)) {
            for (String tag : raw.split(";")) {
                String[] val = tag.split("=");
                final String key = val[0];
                String value = (val.length > 1) ? val[1] : null;
                map.put(key, value);
            }
        }

        return Collections.unmodifiableMap(map);
    }

}
