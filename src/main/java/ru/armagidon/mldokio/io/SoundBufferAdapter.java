package ru.armagidon.mldokio.io;

import com.google.gson.*;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.util.data.SoundContainer;

import java.lang.reflect.Type;

public class SoundBufferAdapter implements JsonSerializer<SoundBuffer>, JsonDeserializer<SoundBuffer>
{

    /**
     * {
     *     "uuid": "uuid"
     *     "label": "label"
     *     "buffer": [{"interval":10, container: {"sound":"SOUND", "pitch": 4}}]
     * }
     * */

    @Override
    public SoundBuffer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(!json.isJsonArray()) return null;
        JsonArray rawBuffer = json.getAsJsonArray();
        SoundBuffer soundBuffer = new SoundBuffer();
        rawBuffer.forEach(jsonElement -> {
            if(!jsonElement.isJsonObject()) return;
            JsonObject compound = jsonElement.getAsJsonObject();
            long interval = compound.get("interval").getAsLong();
            JsonObject containerObject = compound.getAsJsonObject("container");
            SoundContainer soundContainer = context.deserialize(containerObject, SoundContainer.class);
            soundBuffer.addNew(soundContainer, interval);
        });
        return soundBuffer;
    }

    @Override
    public JsonElement serialize(SoundBuffer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray rawBuffer = new JsonArray();
        src.getSoundsAndDelays().forEach(pair->{
            JsonObject compound = new JsonObject();
            compound.addProperty("interval", pair.getSecond());
            compound.add("container", context.serialize(pair.getFirst()));
            rawBuffer.add(compound);
        });
        return rawBuffer;
    }
}
