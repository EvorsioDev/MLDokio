package ru.armagidon.mldokio.survival;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.util.ItemUtils;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry
{
    public final static Map<ItemStack, RecorderItem> RECORDERS = new HashMap<>();

    public static final ItemStack RECORDER = ItemUtils.create(new ItemStack(Material.IRON_INGOT)).
            addTag(new NamespacedKey(MLDokio.getInstance(), "MLDokioItem"), PersistentDataType.STRING, "RECORDER").setName("Recorder").build();
}
