package ru.armagidon.mldokio.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.util.data.SoundContainer;

public class PacketRecorder extends PacketAdapter
{
    private final Recorder recordings;

    public PacketRecorder(Recorder recordings) {
        super(MLDokio.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_SOUND_EFFECT);
        this.recordings = recordings;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Sound soundType = event.getPacket().getSoundEffects().read(0);
        if(!soundType.name().startsWith("BLOCK_NOTE_BLOCK")) return;
        float pitch = event.getPacket().getFloat().read(1);
        if(!recordings.isRecording(event.getPlayer())) return;
        SoundBuffer buffer = recordings.getBuffer(event.getPlayer());
        buffer.addNew(new SoundContainer(soundType, pitch),buffer.getCounter().getTicks());
    }
}
