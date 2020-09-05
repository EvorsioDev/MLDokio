package ru.armagidon.mldokio.recorder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.sound.SoundBuffer;
import ru.armagidon.mldokio.jukebox.JukeBox;
import ru.armagidon.mldokio.util.TickCounter;
import ru.armagidon.mldokio.util.data.SoundContainer;
import ru.armagidon.mldokio.util.observer.Observer;

import java.util.UUID;

public class Recorder implements Observer<SoundContainer>
{
    private final SoundBuffer tape;
    private UUID idOfTape;
    private UUID jukeBoxID;
    private @Setter Player soundSource;
    private @Getter @Setter boolean recording;
    private final TickCounter counter;
    private @Setter RecorderCallBack callBack;

    public Recorder() {
        this.tape = new SoundBuffer();
        this.counter = new TickCounter();
        MLDokio.getInstance().getMicrophone().registerObserver(this);
    }

    public boolean startRecording(Player source){
        if(isRecording()) return false;
        soundSource = source;
        clearTape();
        counter.start();
        idOfTape = UUID.randomUUID();
        setRecording(true);
        return true;
    }

    public boolean stopRecording(){
        if(!isRecording()) return false;
        setRecording(false);
        counter.stop();
        return true;
    }

    public void save(String label, Recordings recordings){
        recordings.saveTrack(idOfTape, tape, label);
    }

    public void clearTape(){
        tape.reset();
        counter.reset();
    }

    public boolean playRecorded() {
        if(idOfTape==null||tape==null||soundSource==null) return false;
        if(isPlaying()) return false;
        jukeBoxID = MLDokio.getInstance().getJukeBoxPool().dedicateJukeBox(soundSource, tape,true);
        JukeBox jukeBox = getJukeBox();
        jukeBox.play();
        callBack.onStartPlaying();
        return true;
    }

    public boolean stopRecorded(){
        if(idOfTape==null||tape==null||soundSource==null) return false;
        if(!isPlaying()) return false;
        JukeBox jukeBox = getJukeBox();
        jukeBox.stop();
        callBack.onStopPlaying();
        return true;
    }

    public boolean isPlaying() {
        return jukeBoxID!=null && getJukeBox()!=null && getJukeBox().isPlaying();
    }

    @Override
    public void update(Player player, SoundContainer data) {
        if(!(player.equals(soundSource) && isRecording())) return;
        tape.addNew(data, counter.getTicks());
    }

    public JukeBox getJukeBox(){
        return MLDokio.getInstance().getJukeBoxPool().getJukeBoxByIdOrNullIfNotFound(jukeBoxID);
    }
}
