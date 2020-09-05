package ru.armagidon.mldokio.jukebox;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.player.MusicListener;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.util.Misc;
import ru.armagidon.mldokio.util.data.Pair;
import ru.armagidon.mldokio.util.data.SoundContainer;
import ru.armagidon.mldokio.util.observer.Observable;
import ru.armagidon.mldokio.util.observer.Observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class JukeBox implements Observable<Boolean>
{

    /**Represents sound player, is created when needs to play track */

    private @Getter final Set<Observer<Boolean>> observers = new HashSet<>();
    private @Getter final Set<MusicListener> listeners = ConcurrentHashMap.newKeySet();
    private @Getter final SoundBuffer buffer;
    private final List<BukkitTask> beats;

    private Location staticPos;
    private Player dynamicPos;
    private BukkitTask ticker;
    private @Getter final UUID id;
    private final AtomicBoolean playing = new AtomicBoolean(false);

    JukeBox(UUID id){
        this.beats = new ArrayList<>();
        this.id = id;
        this.buffer = new SoundBuffer();
    }

    public void play(){
        ticker = ticker();
        synchronized (listeners) {
            for (AtomicInteger i = new AtomicInteger(0); i.get()<buffer.getSoundsAndDelays().size(); i.incrementAndGet()) {
                Pair<SoundContainer, Long> pair = buffer.getSoundsAndDelays().get(i.get());
                Runnable resetPlayingFlag;
                if(i.get()==buffer.getSoundsAndDelays().size()-1) {
                    resetPlayingFlag = () -> {
                        playing.lazySet(false);
                        notifyObservers(null, isPlaying());
                    };
                }
                else resetPlayingFlag = ()->{};

                beats.add(Bukkit.getScheduler().runTaskLaterAsynchronously(MLDokio.getInstance(), () ->{
                    Location location = staticPos==null ? dynamicPos.getLocation() : staticPos;
                    listeners.forEach(p->pair.getFirst().play(location, p.getHandle()));
                    resetPlayingFlag.run();
                },pair.getSecond()));
            }
            playing.set(true);
        }
    }

    public void stop(){
        beats.forEach(task->{
            if(!task.isCancelled()) task.cancel();
        });
        if(ticker!=null) ticker.cancel();
        MusicListener.musicListeners.values().forEach(musicListener ->
                musicListener.getSoundPlayers().remove(getId()));
        playing.lazySet(false);
        buffer.reset();
        notifyObservers(null, isPlaying());
    }

    private BukkitTask ticker(){
        return Bukkit.getScheduler().runTaskTimer(MLDokio.getInstance(), ()->{
            synchronized (listeners){
                Location location;
                if(staticPos==null)
                    location = dynamicPos.getLocation();
                else if(dynamicPos==null)
                    location = staticPos;
                else return;

                Set<Player> detectedPlayers = Misc.getNear(50, location);

                //Check if some of them aren't trackers
                for (Player detectedPlayer : detectedPlayers) {
                    if(!MusicListener.musicListeners.containsKey(detectedPlayer)) break;
                    MusicListener listener = MusicListener.musicListeners.get(detectedPlayer);
                    listeners.add(listener);
                }
                //Check if some of trackers aren't in view distance
                listeners.removeIf(listener -> !detectedPlayers.contains(listener.getHandle()));
            }
        },1,1);
    }

    public boolean isPlaying(){
        return id!=null&&playing.get();
    }

    void setStaticPosition(final Location location){
        dynamicPos  =  null;
        staticPos  = location;
    }

    void setDynamicPosition(final Player dynamicPos){
        staticPos = null;
        this.dynamicPos =  dynamicPos;
    }

    void setBuffer(SoundBuffer buffer){
        this.buffer.copyFromAnother(buffer);
    }
}
