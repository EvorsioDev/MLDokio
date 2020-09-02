package ru.armagidon.mldokio.sound;

import lombok.Getter;

import java.util.UUID;

public class SoundTrack
{
    private @Getter final SoundBuffer buffer;
    private @Getter final String label;
    private @Getter final UUID id;

    public SoundTrack(SoundBuffer buffer, String label, UUID id) {
        this.buffer = buffer;
        this.label = label;
        this.id = id;
    }

    @Override
    public String toString() {
        return "SoundTrack{" +
                "buffer=" + buffer +
                ", label='" + label + '\'' +
                ", id=" + id +
                '}';
    }
}
