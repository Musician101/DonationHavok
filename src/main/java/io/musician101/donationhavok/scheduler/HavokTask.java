package io.musician101.donationhavok.scheduler;

final class HavokTask {

    private int delayLeft;
    private final Runnable runnable;
    private final String name;

    public HavokTask(String name, int delay, Runnable runnable) {
        this.name = name;
        this.delayLeft = delay;
        this.runnable = runnable;
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

    public int getDelayLeft() {
        return delayLeft;
    }
}
