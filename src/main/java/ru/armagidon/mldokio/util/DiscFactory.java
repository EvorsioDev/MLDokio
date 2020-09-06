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
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.sound.SoundTrack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DiscFactory
{
    public static ItemStack createDisc(ItemStack input, SoundTrack track){
        ItemStack disc = input.clone();
        ItemMeta meta = disc.getItemMeta();
        List<String> lore = Collections.singletonList(String.valueOf(ChatColor.ITALIC) + ChatColor.GRAY + track.getLabel());
        meta.setLore(lore);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey tagKey = new NamespacedKey(MLDokio.getInstance(), "DiscData");
        container.set(tagKey, PersistentDataType.STRING, track.getId().toString());
        disc.setItemMeta(meta);
        return disc;
    }

    public static ItemStack copy(ItemStack input, ItemStack source){
        ItemMeta meta = source.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey tagKey = new NamespacedKey(MLDokio.getInstance(), "DiscData");
        String rawUUID = container.get(tagKey, PersistentDataType.STRING);
        if(rawUUID==null) return input;
        UUID uuid = UUID.fromString(rawUUID);
        SoundTrack track = MLDokio.getInstance().getRecordings().getTrack(uuid);
        return createDisc(input, track);
    }
}
