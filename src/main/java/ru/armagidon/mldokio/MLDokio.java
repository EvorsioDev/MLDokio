package ru.armagidon.mldokio;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.mldokio.commands.RecorderCommand;
import ru.armagidon.mldokio.commands.RecordingsCommand;
import ru.armagidon.mldokio.handlers.JukeboxPacketHandler;
import ru.armagidon.mldokio.handlers.PluginListener;
import ru.armagidon.mldokio.io.IOWorker;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MLDokio extends JavaPlugin {

    /**Steps to save sounds!
     * 1. Catch all sound packets
     * 2. Filter if sound isn't note
     * 3. Buffer all sounds with timings*/

    @Getter private static MLDokio instance;
    @Getter private final IOWorker IOWorker;
    private final Recordings recordings;

    public MLDokio() {
        instance = this;
        IOWorker = new IOWorker(this);
        Map<UUID, String> correspondingUUIDs = new HashMap<>();
        IOWorker.labels().forEach(l->{
            try {
                correspondingUUIDs.put(IOWorker.deserialize(l).getId(), l);
            } catch (FileNotFoundException ignored) {}
        });
        recordings = new Recordings(correspondingUUIDs);
    }

    @Override
    public void onEnable() {
        Recorder recorder = new Recorder();
        new RecorderCommand(recorder, recordings).register();
        new RecordingsCommand(recordings).register();
        new PluginListener(recordings);
        ProtocolLibrary.getProtocolManager().addPacketListener(new JukeboxPacketHandler());
    }

    @Override
    public void onDisable() {

    }
}
