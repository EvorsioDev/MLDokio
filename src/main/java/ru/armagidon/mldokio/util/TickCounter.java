package ru.armagidon.mldokio.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import ru.armagidon.mldokio.MLDokio;

import java.util.concurrent.atomic.AtomicLong;

public class TickCounter
{
    private BukkitTask task;
    private final AtomicLong counter;

    public TickCounter() {
        counter = new AtomicLong(0);
    }

    public void start(){
        task = Bukkit.getScheduler().runTaskTimer(MLDokio.getInstance(), counter::incrementAndGet,1,1);
    }

    public void stop(){
        if(task!=null&&!task.isCancelled()) task.cancel();
    }

    public long getTicks(){
        synchronized (counter) {
            return counter.get();
        }
    }

    public void reset(){
        counter.set(0);
    }
}
