package ru.armagidon.mldokio.events;

import lombok.Getter;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class JukeboxSongStopEvent extends Event
{
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private @Getter final Jukebox jukebox;
    private @Getter final ItemStack disc;

    public JukeboxSongStopEvent(Jukebox jukebox, ItemStack disc) {
        this.disc = disc;
        this.jukebox = jukebox;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
