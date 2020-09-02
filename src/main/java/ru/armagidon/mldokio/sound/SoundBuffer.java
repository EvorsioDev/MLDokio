package ru.armagidon.mldokio.sound;

import lombok.Getter;
import ru.armagidon.mldokio.util.TickCounter;
import ru.armagidon.mldokio.util.data.Pair;
import ru.armagidon.mldokio.util.data.SoundContainer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SoundBuffer
{
    private @Getter final List<Pair<SoundContainer, Long>> soundsAndDelays;
    private @Getter final TickCounter counter;

    public SoundBuffer() {
        this.soundsAndDelays = new LinkedList<>();
        this.counter = new TickCounter();
    }

    public synchronized void addNew(SoundContainer sound, long delay){
        soundsAndDelays.add(Pair.of(sound, delay));
    }

    public synchronized void forEach(Consumer<Pair<SoundContainer, Long>> action) {
        soundsAndDelays.forEach(action);
    }
}
