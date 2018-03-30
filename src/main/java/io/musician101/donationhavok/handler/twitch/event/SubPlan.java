package io.musician101.donationhavok.handler.twitch.event;

import java.util.Arrays;
import java.util.Optional;

public enum SubPlan {

    UNKNOWN("Unknown"),
    PRIME("Prime"),
    TIER_1("1000"),
    TIER_2("2000"),
    TIER_3("3000");

    private final String key;

    SubPlan(String key) {
        this.key = key;
    }

    public static Optional<SubPlan> fromString(String text) {
        return Arrays.stream(SubPlan.values()).filter(subPlan -> text.equalsIgnoreCase(subPlan.key)).findFirst();
    }

    public String getKey() {
        return key;
    }

    public String getReadableName() {
        switch (this) {
            case TIER_1:
                return "Tier 1";
            case TIER_2:
                return "Tier 2";
            case TIER_3:
                return "Tier 3";
            case PRIME:
                return "Twitch Prime";
            default:
                return getKey();

        }
    }

    @Override
    public String toString() {
        return key;
    }
}
