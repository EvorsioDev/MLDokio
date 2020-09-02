package ru.armagidon.mldokio.commands;

import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.soundplayer.SoundPlayer;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.util.DiscFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordingsCommand extends BukkitCommand
{

    private final Recordings recordings;

    public RecordingsCommand(Recordings recordings) {
        super("recordings");
        this.recordings = recordings;
    }

    @Override
    protected boolean execute(Player player, String label, String[] args) {
        if(args.length==0) return false;
        String sub = args[0];

        if(args.length>=2){
            String trackLabel = Joiner.on(" ").join(Arrays.copyOfRange(args, 1,args.length));
            SoundTrack cassette = recordings.getTrack(trackLabel);
            if(cassette==null){
                player.sendMessage("There's no track called "+trackLabel);
                return true;
            }
            if(sub.equalsIgnoreCase("play")){
                SoundPlayer.createSoundPlayer(cassette.getId(), cassette.getBuffer(), player).play();
            } else if(sub.equalsIgnoreCase("stop")){
                SoundPlayer.getSoundPlayer(cassette.getId()).stop();
            } else if(sub.equalsIgnoreCase("get")){
                player.getInventory().addItem(DiscFactory.createDisc(cassette));
            }
        }
        return true;
    }

    @Override
    protected List<String> tabComplete(Player player, String[] args) {
        if(args.length==1){
            return Stream.of("play","stop","get").sorted(String.CASE_INSENSITIVE_ORDER).filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if(args[0].equalsIgnoreCase("stop")||args[0].equalsIgnoreCase("play")||args[0].equalsIgnoreCase("get")) {
            String l = Joiner.on(" ").join(Arrays.copyOfRange(args,1, args.length));
            return MLDokio.getInstance().getIOWorker().labels().stream().sorted(String.CASE_INSENSITIVE_ORDER).filter(s -> s.toLowerCase().startsWith(l.toLowerCase())).map(s-> s.substring(l.length())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
