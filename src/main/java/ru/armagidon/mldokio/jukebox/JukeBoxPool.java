package ru.armagidon.mldokio.jukebox;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.sound.SoundBuffer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JukeBoxPool {

    private final Map<UUID, JukeBox> pool = new HashMap<>();

    public JukeBoxPool(int initialCapacity) {
        for (int i = 0; i < initialCapacity; i++) {
            UUID id = UUID.randomUUID();
            pool.put(id, new JukeBox(id));
        }
    }

    public UUID dedicateJukeBox(Object point, SoundBuffer buffer, boolean dynamic){
        Player dynamicPoint = null;
        Location staticPoint = null;

        if(dynamic) dynamicPoint = (Player) point;
        else staticPoint = (Location) point;

        for (Map.Entry<UUID, JukeBox> entry : pool.entrySet()) {
            if(!entry.getValue().isPlaying()){
                if(dynamic){
                    entry.getValue().setDynamicPosition(dynamicPoint);
                } else {
                    entry.getValue().setStaticPosition(staticPoint);
                }
                entry.getValue().setBuffer(buffer);
                return entry.getKey();
            }
        }
        UUID randomId = UUID.randomUUID();
        JukeBox jukeBox = new JukeBox(randomId);
        if(dynamic){
            jukeBox.setDynamicPosition(dynamicPoint);
        } else {
            jukeBox.setStaticPosition(staticPoint);
        }
        jukeBox.setBuffer(buffer);
        pool.put(randomId, jukeBox);
        return randomId;
    }

    public JukeBox getJukeBoxByIdOrNullIfNotFound(UUID id){
        return pool.get(id);
    }
}
