package ru.armagidon.mldokio.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.block.Jukebox;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class JukeboxSongPlayEvent extends Event implements Cancellable
{

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private @Getter @Setter boolean cancelled;

    private @Getter final Jukebox jukebox;
    private @Getter final ItemStack disc;

    public JukeboxSongPlayEvent(Jukebox jukebox, ItemStack disc) {
        this.jukebox = jukebox;
        this.disc = disc;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
