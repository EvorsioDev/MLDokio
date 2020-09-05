package ru.armagidon.mldokio.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.jukebox.JukeBox;

import java.util.*;

public class MusicListener
{

    public static final Map<Player, MusicListener> musicListeners = new HashMap<>();

    private @Getter final Player handle;
    private @Getter final Map<UUID, JukeBox> soundPlayers;

    public MusicListener(Player handle) {
        this.handle = handle;
        this.soundPlayers = new HashMap<>();
    }

    public boolean isListening(){
        return soundPlayers.size()!=0;
    }

    public static MusicListener addMusicListener(Player player){
        MusicListener listener = new MusicListener(player);
        musicListeners.put(player, listener);
        return listener;
    }
}
