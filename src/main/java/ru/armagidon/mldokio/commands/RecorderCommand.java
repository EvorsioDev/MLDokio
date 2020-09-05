package ru.armagidon.mldokio.commands;

import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.player.MusicListener;
import ru.armagidon.mldokio.recorder.Recorder;
import ru.armagidon.mldokio.recorder.Recordings;

import java.util.*;
import java.util.stream.Collectors;

public class RecorderCommand extends BukkitCommand
{

    private final Recordings recordings;
    private final Map<Player, Recorder> recordingPlayers;

    public RecorderCommand(Recordings recordings) {
        super("recorder");
        this.recordings = recordings;
        recordingPlayers = new HashMap<>();
    }

    @Override
    protected boolean execute(Player player, String label, String[] args) {
        if(args.length==0) return false;
        String sub = args[0];
        Recorder recorder = recordingPlayers.get(player);
        if(sub.equalsIgnoreCase("start")){
            if(recorder!=null) {
                if (recorder.isRecording()) {
                    MLDokio.getMessages().send(player, "recorder.you-are-already-recording");
                    return true;
                }
            } else {
                recorder = new Recorder();
                recordingPlayers.put(player, recorder);
            }
            recorder.startRecording(player);
            MLDokio.getMessages().send(player, "recorder.recording-started");
        } else if(sub.equalsIgnoreCase("stop")){
            if(MusicListener.musicListeners.get(player).isListening()){
                recorder.stopRecorded();
                MLDokio.getMessages().send(player, "recorder.stop-playing-recorded");
                return true;
            } else if(!recorder.stopRecording()){
                MLDokio.getMessages().send(player, "recorder.you-are-not-recording");
                return true;
            } else MLDokio.getMessages().send(player, "recorder.recording-stopped");
        } else if(sub.equalsIgnoreCase( "play")){
            if(recorder == null) {
                MLDokio.getMessages().send(player, "recorder.you-didnt-record-anything");
                return true;
            }
            recorder.playRecorded();
            MLDokio.getMessages().send(player, "recorder.play-recorded");
        } else if(sub.equalsIgnoreCase("save")){
            //TODO SAVE RECORDING
            if(args.length<2) return false;
            if(recorder == null) {
                MLDokio.getMessages().send(player, "recorder.you-didnt-record-anything");
                return true;
            }
            String trackLabel = Joiner.on(" ").join(Arrays.copyOfRange(args, 1,args.length));
            if(recordings.contains(trackLabel)){
                MLDokio.getMessages().send(player, "recorder.recording-exists");
                return true;
            }
            recorder.save(trackLabel, recordings);
            recorder.clearTape();
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
