package io.musician101.donationhavok.thread;

import io.musician101.donationhavok.DonationHavok;

public abstract class ThreadSocket extends Thread {

    private final String token;

    public ThreadSocket(String token) {
        setName("DonationHavok-" + getType());
        setDaemon(true);
        this.token = token;
    }

    @Override
    public void run() {
        try {
            //if (DonationHavok.getInstance().getConfig().getRewardsConfig().isEnabled(getType().toLowerCase())) {

            //}
        }
        catch (Exception e) {
            DonationHavok.getInstance().getLogger().error("An error has occurred while attempting to create a connection for " + getType() + ".", e);
        }
    }

    public abstract String getType();
}
