package ru.armagidon.mldokio.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.util.data.SoundContainer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOWorker
{
    private static final SoundContainerAdapter soundContainerAdapter = new SoundContainerAdapter();
    private static final SoundBufferAdapter soundBufferAdapter = new SoundBufferAdapter();
    private static final SoundTrackAdapter soundTrackAdapter = new SoundTrackAdapter();
    private final File musicFolder;

    public IOWorker(Plugin plugin) {
        musicFolder = new File(plugin.getDataFolder(), "/music");
        if(!musicFolder.exists()){
            if(musicFolder.mkdirs()) plugin.getLogger().info("Music folder created!");
        }

    }

    public void serialize(SoundTrack soundTrack) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(SoundContainer.class, soundContainerAdapter)
                .registerTypeAdapter(SoundBuffer.class, soundBufferAdapter).registerTypeAdapter(SoundTrack.class, soundTrackAdapter).create();
        String json = gson.toJson(soundTrack);
        File file = new File(musicFolder, soundTrack.getLabel()+".mcsound");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(json);
        }
    }

    public SoundTrack deserialize(String label) throws FileNotFoundException {
        /*Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SoundContainer.class, soundContainerAdapter)
                .registerTypeAdapter(SoundBuffer.class, soundBufferAdapter).registerTypeAdapter(SoundTrack.class, soundTrackAdapter).create();
        String json = gson.toJson(soundTrack);
        File file = new File(musicFolder, soundTrack.getLabel()+".mcsound");
        if(file.exists()){
            throw new IllegalStateException();
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(json);
        }*/
        File file = new File(musicFolder, label+".mcsound");
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SoundContainer.class, soundContainerAdapter)
                    .registerTypeAdapter(SoundBuffer.class, soundBufferAdapter).registerTypeAdapter(SoundTrack.class, soundTrackAdapter).create();
            return gson.fromJson(reader, SoundTrack.class);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<String> labels(){
        List<String> labels = new ArrayList<>();
        File[] compositions = musicFolder.listFiles((dir, name) -> name.endsWith(".mcsound"));
        if(compositions==null) return labels;
        Arrays.stream(compositions).map(name->{
            int index = name.getName().lastIndexOf('.');
            return name.getName().substring(0, index);
        }).forEach(labels::add);
        return labels;
    }
}
