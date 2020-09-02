package ru.armagidon.mldokio.recorder;

import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Recordings
{
    private final Map<UUID, String> tracks;

    public Recordings(Map<UUID, String> tracks) {
        this.tracks = tracks;
    }


    public void saveTrack(UUID idOfMagnetTape, SoundBuffer data, String label){
        try {
            MLDokio.getInstance().getIOWorker().serialize(new SoundTrack(data, label, idOfMagnetTape));
            tracks.put(idOfMagnetTape, label);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean contains(String label){
        return getTrack(label)!=null;
    }

    public SoundTrack getTrack(String label){
        try {
            return MLDokio.getInstance().getIOWorker().deserialize(label);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public SoundTrack getTrack(UUID uuid){
        return getTrack(tracks.get(uuid));
    }
}
