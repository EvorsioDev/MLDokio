package ru.armagidon.mldokio;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.armagidon.armagidonapi.gui.InventoryListener;
import ru.armagidon.mldokio.commands.RecorderCommand;
import ru.armagidon.mldokio.commands.RecordingsCommand;
import ru.armagidon.mldokio.handlers.JukeboxPacketHandler;
import ru.armagidon.mldokio.handlers.Microphone;
import ru.armagidon.mldokio.handlers.PluginListener;
import ru.armagidon.mldokio.io.IOWorker;
import ru.armagidon.mldokio.jukebox.JukeBox;
import ru.armagidon.mldokio.jukebox.JukeBoxPool;
import ru.armagidon.mldokio.messaging.MessageManager;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.survival.ItemRegistry;
import ru.armagidon.mldokio.survival.RecorderItemListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MLDokio extends JavaPlugin {

    /**Steps to save sounds!
     * 1. Catch all sound packets
     * 2. Filter if sound isn't note
     * 3. Buffer all sounds with timings*/

    @Getter private static MLDokio instance;
    @Getter private final IOWorker IOWorker;
    @Getter private static MessageManager messages;
    @Getter private final Recordings recordings;
    @Getter private Microphone microphone;
    @Getter private final JukeBoxPool jukeBoxPool;

    public MLDokio() {
        instance = this;
        this.jukeBoxPool = new JukeBoxPool(100);
        this.IOWorker = new IOWorker(this);
        Map<UUID, String> correspondingUUIDs = new HashMap<>();
        this.IOWorker.labels().forEach(l->{
            correspondingUUIDs.put(IOWorker.deserialize(l).getId(), l);
        });
        recordings = new Recordings(correspondingUUIDs);
        messages = new MessageManager();
    }

    @Override
    public void onEnable() {
        microphone = new Microphone();
        new RecorderCommand(recordings).register();
        new RecordingsCommand(recordings).register();
        new PluginListener(recordings);
        new RecorderItemListener();
        createCrafts();
    }

    @Override
    public void onDisable() {

    }

    private void createCrafts(){
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "recorder"),ItemRegistry.RECORDER);
        recipe.shape("RD ","Ir ","IY ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('D',Material.MUSIC_DISC_STAL);
        recipe.setIngredient('r',Material.REDSTONE);
        recipe.setIngredient('Y', Material.YELLOW_WOOL);
        recipe.setIngredient('R', Material.RED_WOOL);
        Bukkit.addRecipe(recipe);
    }
}
