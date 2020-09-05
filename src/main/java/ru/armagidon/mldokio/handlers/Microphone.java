package ru.armagidon.mldokio.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.apache.commons.lang.Validate;
import org.bukkit.Sound;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.util.data.SoundContainer;
import ru.armagidon.mldokio.util.observer.Observable;
import ru.armagidon.mldokio.util.observer.Observer;

import java.util.HashSet;
import java.util.Set;

public class Microphone extends PacketAdapter implements Observable<SoundContainer>
{
    private final Set<Observer<SoundContainer>> observers;

    public Microphone() {
        super(MLDokio.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.NAMED_SOUND_EFFECT);
        this.observers = new HashSet<>();
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Sound soundType = event.getPacket().getSoundEffects().read(0);
        if(!soundType.name().startsWith("BLOCK_NOTE_BLOCK")) return;
        float pitch = event.getPacket().getFloat().read(1);
        SoundContainer container = new SoundContainer(soundType, pitch);
        notifyObservers(event.getPlayer(), container);
    }

    @Override
    public Set<Observer<SoundContainer>> getObservers() {
        return observers;
    }

    @Override
    public void registerObserver(Observer<SoundContainer> observer) {
        Validate.notNull(observer);
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer<SoundContainer> observer) {
        Validate.notNull(observer);
        observers.remove(observer);
    }
}
