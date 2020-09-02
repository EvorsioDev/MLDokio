package ru.armagidon.mldokio.io;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Sound;
import ru.armagidon.mldokio.util.data.SoundContainer;

import java.io.IOException;

public class SoundContainerAdapter extends TypeAdapter<SoundContainer>
{

    @Override
    public void write(JsonWriter out, SoundContainer value) throws IOException{
        out.beginObject();

        out.name("sound").value(value.getSound().name());
        out.name("pitch").value(value.getPitch());

        out.endObject();
    }

    @Override
    public SoundContainer read(JsonReader in) throws IOException {
        in.beginObject();
        in.nextName();
        Sound sound = Sound.valueOf(in.nextString());
        in.nextName();
        float pitch = (float) in.nextDouble();
        return new SoundContainer(sound, pitch);
    }
}
