package ru.armagidon.mldokio.recorder;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.handlers.PacketRecorder;
import ru.armagidon.mldokio.soundplayer.SoundPlayer;
import ru.armagidon.mldokio.sound.SoundBuffer;

import java.util.*;

public class Recorder
{
    private final Map<Player, SoundBuffer> recordings = new HashMap<>();
    private final Map<Player, UUID> recordingsIds = new HashMap<>();
    private final Set<Player> recordersNow = new HashSet<>();

    public Recorder() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketRecorder(this));
    }

    public void startRecording(Player player){
        if(isRecording(player)) stopRecording(player);
        SoundBuffer buffer = new SoundBuffer();
        buffer.getCounter().start();
        recordings.put(player, buffer);
        recordersNow.add(player);
    }

    public SoundBuffer getBuffer(Player player){
        return recordings.get(player);
    }

    public void stopRecording(Player player){
        if(!isRecording(player)) return;
        SoundBuffer buffer = recordings.get(player);
        buffer.getCounter().stop();
        recordersNow.remove(player);
        recordingsIds.put(player, UUID.randomUUID());
    }

    public void playRecorded(Player recorder){
        SoundPlayer recordedSoundPlayer = SoundPlayer.createSoundPlayer(recordingsIds.get(recorder), recordings.get(recorder), recorder);
        recordedSoundPlayer.play();
    }

    public void stopPlayingRecorded(Player recorder){
        SoundPlayer recordedSoundPlayer = SoundPlayer.getSoundPlayer(recordingsIds.get(recorder));
        recordedSoundPlayer.stop();
    }

    public void clearTape(Player player){
        recordings.remove(player);
    }

    public boolean isRecording(Player player){
        return recordersNow.contains(player);
    }

    public boolean hasRecordings(Player player){
        return recordings.containsKey(player);
    }

    public UUID getIdOfRecording(Player player){
        return recordingsIds.get(player);
    }
}
