package ru.armagidon.mldokio.sound;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class SoundTrack
{
    private @Getter final SoundBuffer buffer;
    private @Getter @Setter String label;
    private @Getter final UUID id;
    private @Getter final UUID authorId;

    public SoundTrack(SoundBuffer buffer, String label, UUID id, UUID authorId) {
        this.buffer = buffer;
        this.label = label;
        this.id = id;
        this.authorId = authorId;
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
