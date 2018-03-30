package io.musician101.donationhavok.handler.twitch.event;

public class Cheer {

    private final int bits;
    private final String channel;
    private final String message;
    private final String user;

    public Cheer(String channel, String user, int bits, String message) {
        this.channel = channel;
        this.user = user;
        this.bits = bits;
        this.message = message;
    }

    public int getBits() {
        return bits;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}
