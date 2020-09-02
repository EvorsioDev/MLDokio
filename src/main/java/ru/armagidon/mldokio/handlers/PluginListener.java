package ru.armagidon.mldokio.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.events.JukeboxSongPlayEvent;
import ru.armagidon.mldokio.events.JukeboxSongStopEvent;
import ru.armagidon.mldokio.player.MusicListener;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.soundplayer.SoundPlayer;

import java.util.UUID;

public class PluginListener implements Listener
{
    private final Recordings recordings;

    public PluginListener(Recordings recordings) {
        Bukkit.getPluginManager().registerEvents(this, MLDokio.getInstance());
        Bukkit.getOnlinePlayers().forEach(MusicListener::addMusicListener);
        this.recordings = recordings;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        MusicListener.addMusicListener(event.getPlayer());
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event){
        MusicListener.musicListeners.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlaySong(JukeboxSongPlayEvent event){
        ItemStack disc = event.getDisc();
        ItemMeta meta = disc.getItemMeta();
        NamespacedKey tagKey = new NamespacedKey(MLDokio.getInstance(), "DiscData");
        if(meta.getPersistentDataContainer().has(tagKey, PersistentDataType.STRING)){
            String uuidString = meta.getPersistentDataContainer().get(tagKey, PersistentDataType.STRING);
            if(uuidString==null) return;
            UUID uuid = UUID.fromString(uuidString);
            SoundTrack trackToPlay = recordings.getTrack(uuid);
            if(trackToPlay==null) return;
            SoundPlayer player = SoundPlayer.createSoundPlayer(uuid, trackToPlay.getBuffer(), event.getJukebox().getLocation());
            player.play();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStopSong(JukeboxSongStopEvent event){
        ItemStack disc = event.getDisc();
        ItemMeta meta = disc.getItemMeta();
        NamespacedKey tagKey = new NamespacedKey(MLDokio.getInstance(), "DiscData");
        if(meta.getPersistentDataContainer().has(tagKey, PersistentDataType.STRING)){
            String uuidString = meta.getPersistentDataContainer().get(tagKey, PersistentDataType.STRING);
            if(uuidString==null) return;
            UUID uuid = UUID.fromString(uuidString);
            SoundPlayer player = SoundPlayer.getSoundPlayer(uuid);
            if(player==null) return;
            player.stop();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getBlock().getType().equals(Material.JUKEBOX)){
            Jukebox jukebox = (Jukebox) event.getBlock().getState();
            new JukeboxSongStopEvent(jukebox, jukebox.getRecord()).callEvent();
        }
    }

}
