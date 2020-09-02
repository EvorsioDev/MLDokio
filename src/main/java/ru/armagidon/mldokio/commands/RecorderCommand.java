package ru.armagidon.mldokio.commands;

import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.MLDokio;
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
                MLDokio.getMessages().send(player, "recorder.you-are-already-recording");
                return true;
            }
            recorder.startRecording(player);
            MLDokio.getMessages().send(player, "recorder.recording-started");
        } else if(sub.equalsIgnoreCase("stop")){
            if(MusicListener.musicListeners.get(player).isListening()){
                recorder.stopPlayingRecorded(player);
                MLDokio.getMessages().send(player, "recorder.stop-playing-recorded");
                return true;
            } else if(!recorder.isRecording(player)){
                MLDokio.getMessages().send(player, "recorder.you-are-not-recording");
                return true;
            }
            recorder.stopRecording(player);
            MLDokio.getMessages().send(player, "recorder.recording-stopped");
        } else if(sub.equalsIgnoreCase( "play")){
            if(!recorder.hasRecordings(player)) {
                MLDokio.getMessages().send(player, "recorder.you-didnt-record-anything");
                return true;
            }
            recorder.playRecorded(player);
            MLDokio.getMessages().send(player, "recorder.play-recorded");
        } else if(sub.equalsIgnoreCase("save")){
            //TODO SAVE RECORDING
            if(args.length<2) return false;
            if(!recorder.hasRecordings(player)) {
                MLDokio.getMessages().send(player, "recorder.you-didnt-record-anything");
                return true;
            }
            String trackLabel = Joiner.on(" ").join(Arrays.copyOfRange(args, 1,args.length));
            if(recordings.contains(trackLabel)){
                MLDokio.getMessages().send(player, "recorder.recording-exists");
                return true;
            }
            UUID uuid = recorder.getIdOfRecording(player);
            recordings.saveTrack(uuid, recorder.getBuffer(player), trackLabel);
            recorder.clearTape(player);
            MLDokio.getMessages().sendWithTrackLabel(player, "recorder.recording-saved",trackLabel);
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
