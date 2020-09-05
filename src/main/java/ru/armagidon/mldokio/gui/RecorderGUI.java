package ru.armagidon.mldokio.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.armagidonapi.gui.GUIHolder;
import ru.armagidon.armagidonapi.gui.buttons.ActionButton;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.util.DiscFactory;
import ru.armagidon.mldokio.util.ItemUtils;
import ru.armagidon.mldokio.util.observer.Observer;

import javax.annotation.Nullable;

public class RecorderGUI extends GUIHolder implements Observer<Boolean>
{

    private final Recorder recorder;

    public RecorderGUI(Recorder recorder, ItemStack recorderItem) {
        super("Recorder", 1);
        this.recorder = recorder;
        init();
        addBlockedItem(recorderItem);
    }

    private void init(){
        //Play button
        {
            ItemStack playButtonItem = ItemUtils.create(new ItemStack(Material.LIME_CONCRETE)).setName("&aPlay").build();
            ActionButton playButton = new ActionButton(playButtonItem, (e)-> {
                if(recorder.playRecorded()) {
                    MLDokio.getMessages().send(e.getWhoClicked(), "recorder.play-recorded");
                    setButtonActivation(0, true);
                }
            });
            addButton(0, playButton);
        }
        //Stop playing button
        {
            ItemStack stopButtonItem = ItemUtils.create(new ItemStack(Material.WHITE_CONCRETE)).setName("&cStop").build();
            ActionButton playButton = new ActionButton(stopButtonItem, (e)->{
                if(recorder.stopRecorded())
                    MLDokio.getMessages().send(e.getWhoClicked(), "recorder.stop-playing-recorded");
            });
            addButton(1, playButton);
        }
        //Record button
        {
            ItemStack recordButtonItem = ItemUtils.create(new ItemStack(Material.RED_CONCRETE)).setName("&cRecord").build();
            ActionButton playButton = new ActionButton(recordButtonItem, (e)->{
                Player player = (Player) e.getWhoClicked();
                if(recorder.startRecording(player)) {
                    MLDokio.getMessages().send(player, "recorder.recording-started");
                    setButtonActivation(2, true);
                }
            });
            addButton(2, playButton);
        }
        //Stop recording button
        {
            ItemStack stopButtonItem = ItemUtils.create(new ItemStack(Material.BLACK_CONCRETE)).setName("&cStop record").build();
            ActionButton playButton = new ActionButton(stopButtonItem, (e)->{
                Player player = (Player) e.getWhoClicked();
                player.updateInventory();
                if(recorder.stopRecording()) {
                    MLDokio.getMessages().send(player, "recorder.recording-stopped");
                    setButtonActivation(2, false);
                }
            });
            addButton(3, playButton);
        }
        //Write
        {
            ItemStack writeButtonItem = ItemUtils.create(new ItemStack(Material.YELLOW_CONCRETE)).setName("&eWrite").build();
            ActionButton writeButton = new ActionButton(writeButtonItem, (e)->{
                final int slot = 8;
                ItemStack item = e.getInventory().getItem(slot);
                if(item!=null){
                    if(!item.getType().equals(Material.MUSIC_DISC_STAL)) return;
                    String name = item.getItemMeta().getDisplayName();
                    recorder.save(name, MLDokio.getInstance().getRecordings());
                    SoundTrack track = MLDokio.getInstance().getRecordings().getTrack(name);
                    item = DiscFactory.createDisc(track);
                    getInventory().setItem(slot, item);
                    e.getWhoClicked().sendMessage("Music written on a disc with label: "+name);
                }
            });
            addButton(7, writeButton);
        }
        makeFreeSpace(8);
    }

    public void open(Player who){
        who.openInventory(getInventory());
    }

    @Override
    public void update(@Nullable Player player, Boolean data) {
        setButtonActivation(0, data);
    }
}
