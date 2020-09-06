package ru.armagidon.mldokio.recorder;

import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.util.Map;
import java.util.UUID;

public class Recordings
{
    private final Map<UUID, String> tracks;

    public Recordings(Map<UUID, String> tracks) {
        this.tracks = tracks;
    }


    public void saveTrack(UUID author, UUID idOfMagnetTape, SoundBuffer data, String label){
        MLDokio.getInstance().getIOWorker().serialize(new SoundTrack(data, label, idOfMagnetTape, author));
        tracks.put(idOfMagnetTape, label);
    }

    public void changeLabel(SoundTrack track, String newName){
        tracks.put(track.getId(), newName);
        MLDokio.getInstance().getIOWorker().renameSoundTrack(track.getLabel(), newName);
    }

    public boolean contains(String label){
        return getTrack(label)!=null;
    }

    public SoundTrack getTrack(String label){
        return MLDokio.getInstance().getIOWorker().deserialize(label);
    }

    public SoundTrack getTrack(UUID uuid){
        return getTrack(tracks.get(uuid));
    }
}
