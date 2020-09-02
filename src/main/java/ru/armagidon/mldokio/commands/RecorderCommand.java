package ru.armagidon.mldokio.commands;

import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.player.MusicListener;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.recorder.Recordings;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RecorderCommand extends BukkitCommand
{

    private final Recorder recorder;
    private final Recordings recordings;

    public RecorderCommand(Recorder recorder, Recordings recordings) {
        super("recorder");
        this.recordings = recordings;
        this.recorder = recorder;
    }

    @Override
    protected boolean execute(Player player, String label, String[] args) {
        if(args.length==0) return false;
        String sub = args[0];
        if(sub.equalsIgnoreCase("start")){
            if(recorder.isRecording(player)){
                player.sendMessage("You're already recording!");
                return true;
            }
            recorder.startRecording(player);
            player.sendMessage("Recording started");
        } else if(sub.equalsIgnoreCase("stop")){
            if(MusicListener.musicListeners.get(player).isListening()){
                recorder.stopPlayingRecorded(player);
                return true;
            } else if(!recorder.isRecording(player)){
                player.sendMessage("You're not recording");
                return true;
            }
            recorder.stopRecording(player);
            player.sendMessage("Recording stopped");
        } else if(sub.equalsIgnoreCase( "play")){
            if(!recorder.hasRecordings(player)) {
                player.sendMessage("You didn't record anything");
                return true;
            }
            recorder.playRecorded(player);
            player.sendMessage("Playing recording");
        } else if(sub.equalsIgnoreCase("save")){
            //TODO SAVE RECORDING
            if(args.length<2) return false;
            if(!recorder.hasRecordings(player)) {
                player.sendMessage("You didn't record anything");
                return true;
            }
            String lbl = Joiner.on(" ").join(Arrays.copyOfRange(args, 1,args.length));
            if(recordings.contains(lbl)){
                player.sendMessage("This recording already exists!");
                return true;
            }
            UUID uuid = recorder.getIdOfRecording(player);
            recordings.saveTrack(uuid, recorder.getBuffer(player), lbl);
            recorder.clearTape(player);
            player.sendMessage("Track saved as: "+lbl);
        }
        return true;
    }

    @Override
    protected List<String> tabComplete(Player player, String[] args) {
        if(args.length==1){
            return Arrays.stream(new String[]{"start","stop","play","save"}).sorted(String.CASE_INSENSITIVE_ORDER).filter(s->s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        return super.tabComplete(player, args);
    }
}
