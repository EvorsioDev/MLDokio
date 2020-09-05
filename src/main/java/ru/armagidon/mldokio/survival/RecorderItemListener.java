package ru.armagidon.mldokio.survival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.armagidonapi.gui.InventoryListener;
import ru.armagidon.mldokio.MLDokio;

import static ru.armagidon.mldokio.survival.ItemRegistry.RECORDERS;

public class RecorderItemListener implements Listener
{

    public RecorderItemListener() {
        Bukkit.getPluginManager().registerEvents(this, MLDokio.getInstance());
        new InventoryListener(MLDokio.getInstance());
    }

    @EventHandler
    public void onEvent(PlayerInteractEvent event){
        ItemStack hand = event.getItem();
        if(hand==null||hand.getType().equals(Material.AIR)) return;
        //Check if contains in recorders map

        if(!RECORDERS.containsKey(hand)) {
            if (hand.isSimilar(ItemRegistry.RECORDER)) {
                RECORDERS.put(hand, new RecorderItem());
            }else return;
        }

        RecorderItem item = RECORDERS.get(hand);
        item.openMenu(event.getPlayer());
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();

        for (RecorderItem value : RECORDERS.values()) {
            if(item.isSimilar(value.getRecorderItem())){
                value.stop();
            }
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e){
        if(!e.getPlugin().equals(MLDokio.getInstance())) return;
        RECORDERS.values().forEach(RecorderItem::stop);
    }
}
