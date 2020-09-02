package ru.armagidon.mldokio.soundplayer;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.player.MusicListener;
import ru.armagidon.mldokio.util.Misc;
import ru.armagidon.mldokio.sound.SoundBuffer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class SoundPlayer
{

    /**Represents sound player, is created when needs to play track */

    private @Getter final Set<MusicListener> listeners = ConcurrentHashMap.newKeySet();
    private @Getter final SoundBuffer buffer;
    private final List<BukkitTask> beats;

    private final Location staticPos;
    private final Player dynamicPos;
    private final BukkitTask ticker;
    private @Getter final UUID id;
    private @Getter boolean playing = false;

    private SoundPlayer(SoundBuffer buffer, Player dynamicPos, UUID id) {
        this(buffer, dynamicPos,null,id);
    }

    private SoundPlayer(SoundBuffer buffer, Location staticPos, UUID id) {
        this(buffer, null,staticPos,id);
    }

    private SoundPlayer(SoundBuffer buffer, Player dynamicPos, Location staticPos, UUID id){
        this.buffer = buffer;
        this.beats = new ArrayList<>();
        this.staticPos = staticPos;
        this.dynamicPos = dynamicPos;
        this.ticker = ticker();
        this.id = id;
    }

    public void play(){
        synchronized (listeners) {
            buffer.forEach(pair -> beats.add(Bukkit.getScheduler().runTaskLaterAsynchronously(MLDokio.getInstance(), () ->
                    listeners.forEach(p -> pair.getFirst().play(staticPos==null?dynamicPos.getLocation():staticPos, p.getHandle())), pair.getSecond())));
            playing = true;
        }
    }

    public void stop(){
        beats.forEach(task->{
            if(!task.isCancelled()) task.cancel();
        });
        MusicListener.musicListeners.values().forEach(musicListener ->
                musicListener.getSoundPlayers().remove(getId()));
        ticker.cancel();
        playing = false;
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












    private final static Map<UUID, SoundPlayer> soundPlayerTable = new HashMap<>();

    public static SoundPlayer createSoundPlayer(UUID id,SoundBuffer buffer, Player source){
        SoundPlayer soundPlayer = new SoundPlayer(buffer, source, id);
        soundPlayerTable.put(id,soundPlayer);
        return soundPlayer;
    }

    public static SoundPlayer createSoundPlayer(UUID id,SoundBuffer buffer, Location source){
        SoundPlayer soundPlayer = new SoundPlayer(buffer, source,id);
        soundPlayerTable.put(id,soundPlayer);
        return soundPlayer;
    }

    //Param: id of the disc
    public static SoundPlayer getSoundPlayer(UUID id){
        return soundPlayerTable.get(id);
    }
}
