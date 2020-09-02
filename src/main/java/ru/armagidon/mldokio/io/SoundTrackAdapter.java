package ru.armagidon.mldokio.io;


import com.google.gson.*;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.lang.reflect.Type;
import java.util.UUID;

public class SoundTrackAdapter implements JsonSerializer<SoundTrack>, JsonDeserializer<SoundTrack>
{
    @Override
    public SoundTrack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonObject()) return null;
        JsonObject compound = json.getAsJsonObject();
        UUID uuid = UUID.fromString(compound.getAsJsonPrimitive("uuid").getAsString());
        String label = compound.getAsJsonPrimitive("label").getAsString();
        SoundBuffer buffer = context.deserialize(compound.getAsJsonArray("buffer"), SoundBuffer.class);
        return new SoundTrack(buffer, label, uuid);
    }

    @Override
    public JsonElement serialize(SoundTrack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject compound = new JsonObject();
        compound.addProperty("uuid", src.getId().toString());
        compound.addProperty("label",src.getLabel());
        compound.add("buffer", context.serialize(src.getBuffer()));
        return compound;
    }
}
