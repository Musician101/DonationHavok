package io.musician101.donationhavok.handler.twitch.event;

public class Subscription {

    private final String channel;
    private final boolean isResub;
    private final String message;
    private final int streak;
    private final SubPlan subPlan;
    private final String user;

    public Subscription(String channel, String user, SubPlan subPlan, boolean isResub, int streak, String message) {
        this.channel = channel;
        this.user = user;
        this.subPlan = subPlan;
        this.isResub = isResub;
        this.streak = streak;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public int getStreak() {
        return streak;
    }

    public SubPlan getSubPlan() {
        return subPlan;
    }

    public String getUser() {
        return user;
    }

    public boolean isResub() {
        return isResub;
    }
}
