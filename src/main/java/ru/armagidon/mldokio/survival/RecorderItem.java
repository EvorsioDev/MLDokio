package ru.armagidon.mldokio.survival;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mldokio.gui.RecorderGUI;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.recorder.RecorderCallBack;

public class RecorderItem
{
    private @Getter final ItemStack recorderItem;

    private @Getter final Recorder recorder;
    private @Getter final RecorderGUI gui;

    public RecorderItem() {
        this.recorderItem = ItemRegistry.RECORDER.clone();
        this.recorder = new Recorder();
        this.gui = new RecorderGUI(recorder,recorderItem);
        recorder.setCallBack(new RecorderCallBack() {
            @Override
            public void onStopPlaying() {
                recorder.getJukeBox().unregisterObserver(gui);
            }

            @Override
            public void onStartPlaying() {
                recorder.getJukeBox().registerObserver(gui);
            }
        });
    }

    public void stop(){
        if(recorder.isRecording()){
            recorder.stopRecording();
        }
        if(recorder.isPlaying()){
            recorder.stopRecorded();
        }
    }

    public void openMenu(Player who){
        gui.open(who);
    }
}
