package ru.armagidon.mldokio.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.util.Collections;
import java.util.List;

public class DiscFactory
{
    public static ItemStack createDisc(SoundTrack track){
        ItemStack disc = new ItemStack(Material.MUSIC_DISC_STAL);
        ItemMeta meta = disc.getItemMeta();
        List<String> lore = Collections.singletonList(String.valueOf(ChatColor.ITALIC) + ChatColor.GRAY + track.getLabel());
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey tagKey = new NamespacedKey(MLDokio.getInstance(), "DiscData");
        container.set(tagKey, PersistentDataType.STRING, track.getId().toString());
        disc.setItemMeta(meta);
        return disc;
    }
}
