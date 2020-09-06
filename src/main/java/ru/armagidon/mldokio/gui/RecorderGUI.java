package ru.armagidon.mldokio.gui;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.armagidon.armagidonapi.gui.GUIHolder;
import ru.armagidon.armagidonapi.gui.buttons.ActionButton;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.util.DiscFactory;
import ru.armagidon.mldokio.util.ItemUtils;
import ru.armagidon.mldokio.util.observer.Observer;

import javax.annotation.Nullable;
import java.io.File;

public class RecorderGUI extends GUIHolder implements Observer<Boolean>
{

    private final Recorder recorder;

    private final int SOURCE_INPUT = 8;
    private final int COPY_INPUT = 17;
    private final int WRITE_FROM_RECORDER = 7;
    private final int WRITE_FROM_DISC = 16;


    public RecorderGUI(Recorder recorder, ItemStack recorderItem) {
        super("Recorder", 2);
        this.recorder = recorder;
        init();
        addBlockedItem(recorderItem);
    }

    private void init(){
        //First row
        {
            //Play button
            {
                ItemStack playButtonItem = ItemUtils.create(new ItemStack(Material.LIME_CONCRETE)).setName("&a&l\u25B6").build();
                ActionButton playButton = new ActionButton(playButtonItem, (e) -> {
                    if (recorder.playRecorded()) {
                        MLDokio.getMessages().send(e.getWhoClicked(), "recorder.play-recorded");
                        setButtonActivation(0, true);
                    }
                });
                addButton(0, playButton);
            }
            //Stop playing button
            {
                ItemStack stopButtonItem = ItemUtils.create(new ItemStack(Material.WHITE_CONCRETE)).setName("&f||").build();
                ActionButton playButton = new ActionButton(stopButtonItem, (e) -> {
                    if (recorder.stopRecorded())
                        MLDokio.getMessages().send(e.getWhoClicked(), "recorder.stop-playing-recorded");
                });
                addButton(1, playButton);
            }
            //Record button
            {
                ItemStack recordButtonItem = ItemUtils.create(new ItemStack(Material.RED_CONCRETE)).setName("&c\u23FA").build();
                ActionButton playButton = new ActionButton(recordButtonItem, (e) -> {
                    Player player = (Player) e.getWhoClicked();
                    if (recorder.startRecording(player)) {
                        MLDokio.getMessages().send(player, "recorder.recording-started");
                        setButtonActivation(2, true);
                    }
                });
                addButton(2, playButton);
            }
            //Stop recording button
            {
                ItemStack stopButtonItem = ItemUtils.create(new ItemStack(Material.BLACK_CONCRETE)).setName("&f\u23F9").build();
                ActionButton playButton = new ActionButton(stopButtonItem, (e) -> {
                    Player player = (Player) e.getWhoClicked();
                    player.updateInventory();
                    if (recorder.stopRecording()) {
                        MLDokio.getMessages().send(player, "recorder.recording-stopped");
                        setButtonActivation(2, false);
                    }
                });
                addButton(3, playButton);
            }
            //Write
            {
                ItemStack writeButtonItem = ItemUtils.create(new ItemStack(Material.YELLOW_CONCRETE)).setName("&eWrite from recorder").build();
                ActionButton writeButton = new ActionButton(writeButtonItem, (e) -> {
                    final int slot = 8;
                    ItemStack item = e.getInventory().getItem(slot);
                    if (item != null) {
                        if (!item.getType().isRecord()) return;
                        String name = item.getItemMeta().getDisplayName();
                        if (name.isEmpty()) name = createLabelForTrack();
                        recorder.save(name, MLDokio.getInstance().getRecordings());
                        SoundTrack track = MLDokio.getInstance().getRecordings().getTrack(name);
                        item = DiscFactory.createDisc(item, track);
                        getInventory().setItem(slot, item);
                        e.getWhoClicked().sendMessage("Music written on a disc with label: " + name);
                    }
                });
                addButton(WRITE_FROM_RECORDER, writeButton);
            }
            makeFreeSpace(SOURCE_INPUT);
        }
        //Second row
        {
            {
                ItemStack writeButton = ItemUtils.create(new ItemStack(Material.CYAN_DYE)).setName("&9Write from disc").build();
                ActionButton button = new ActionButton(writeButton, (e)->{
                    ItemStack source = getInventory().getItem(SOURCE_INPUT);
                    ItemStack copy = getInventory().getItem(COPY_INPUT);
                    if(copy==null||source==null||!copy.getType().isRecord()||!source.getType().isRecord()) return;
                    if(!source.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MLDokio.getInstance(),"DiscData"), PersistentDataType.STRING)) return;
                    getInventory().setItem(COPY_INPUT, DiscFactory.copy(copy, source));
                    e.getWhoClicked().sendMessage("Music copied");
                });
                addButton(WRITE_FROM_DISC, button);
            }
            makeFreeSpace(COPY_INPUT);
        }
    }

    @Override
    public void update(@Nullable Player player, Boolean data) {
        setButtonActivation(0, data);
    }

    private String createLabelForTrack(){
        File musicFolder = new File(MLDokio.getInstance().getDataFolder(), "music");
        File[] unnamedMusic = musicFolder.listFiles((dir, name) -> name.startsWith("Unnamed"));
        if(unnamedMusic==null) return "Unnamed";
        int musicQuantity = unnamedMusic.length;
        String pattern = musicQuantity>0?"("+musicQuantity+")":"";
        return "Unnamed"+pattern;
    }
}
