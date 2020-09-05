package ru.armagidon.mldokio.sound;

import lombok.Getter;
import ru.armagidon.mldokio.util.data.Pair;
import ru.armagidon.mldokio.util.data.SoundContainer;

import java.util.LinkedList;
import java.util.List;

public class SoundBuffer
{
    private @Getter final List<Pair<SoundContainer, Long>> soundsAndDelays;

    public SoundBuffer() {
        this.soundsAndDelays = new LinkedList<>();
    }

    public void addNew(SoundContainer sound, long delay){
        soundsAndDelays.add(Pair.of(sound, delay));
    }

    public void reset(){
        soundsAndDelays.clear();
    }

    public void copyFromAnother(SoundBuffer buffer){
        soundsAndDelays.addAll(buffer.getSoundsAndDelays());
    }
}
