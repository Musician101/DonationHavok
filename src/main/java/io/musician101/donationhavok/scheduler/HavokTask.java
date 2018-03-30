package io.musician101.donationhavok.scheduler;

final class HavokTask {

    private final String name;
    private final Runnable runnable;
    private int delayLeft;

    public HavokTask(String name, int delay, Runnable runnable) {
        this.name = name;
        this.delayLeft = delay;
        this.runnable = runnable;
    }

    public int getDelayLeft() {
        return delayLeft;
    }

    public String getName() {
        return name;
    }

    public void tick() {
        if (delayLeft == 0) {
            runnable.run();
        }
        delayLeft--;
    }
}
