package ru.armagidon.mldokio.util.data;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Data
public class SoundContainer
{
    private final Sound sound;
    private final float pitch;

    public void play(Location location, Player player){
        player.playSound(location, sound, 1,pitch);
    }

    public String toString(){
        return "Sound="+sound.name()+"; Pitch="+pitch;
    }
}
