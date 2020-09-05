package ru.armagidon.mldokio.util;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemUtils
{
    private final ItemStack source;
    private final ItemMeta meta;

    private ItemUtils(ItemStack source){
        this.source = source;
        this.meta = source.getItemMeta();
    }

    public ItemUtils setName(String name){
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        return this;
    }

    public ItemUtils setLore(List<String> lore){
        meta.setLore(lore);
        return this;
    }

    public ItemUtils setEnchanted(boolean enchanted){
        if(enchanted){
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.DURABILITY);
        }
        return this;
    }

    public ItemStack build(){
        ItemStack s = source.clone();
        s.setItemMeta(meta);
        return s;
    }

    public <T, Z> ItemUtils addTag(NamespacedKey key, PersistentDataType<T, Z> type, Z value){
        meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public void modify(){
        source.setItemMeta(meta);
    }

    public static ItemUtils create(ItemStack source){
        return new ItemUtils(source);
    }
}
