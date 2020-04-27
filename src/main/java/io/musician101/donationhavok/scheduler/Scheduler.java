package io.musician101.donationhavok.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.Type;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public final class Scheduler {

    private final List<HavokTask> tasks = new ArrayList<>();

    public Scheduler() {

    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.type == Type.SERVER && event.phase == Phase.START) {
            List<HavokTask> tempTasks = tasks.stream().filter(task -> task.getDelayLeft() >= 0).collect(Collectors.toList());
            tasks.clear();
            tasks.addAll(tempTasks);
            tasks.forEach(HavokTask::tick);
        }
    }

    public void purge() {
        tasks.clear();
    }

    public void scheduleTask(String name, int delay, Runnable runnable) {
        if (delay == 0) {
            runnable.run();
        }
        else {
            tasks.add(new HavokTask(name, delay, runnable));
        }
    }
}
