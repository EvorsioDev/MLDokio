package ru.armagidon.mldokio.commands;

import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.recorder.Recordings;
import ru.armagidon.mldokio.sound.SoundTrack;
import ru.armagidon.mldokio.util.DiscFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordingsCommand extends BukkitCommand
{

    private final Recordings recordings;
    private UUID jukeBoxID;

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
                MLDokio.getMessages().sendWithTrackLabel(player, "recordings.no-track-found",trackLabel);
                return true;
            }
            if(sub.equalsIgnoreCase("play")){
                if(!player.hasPermission("mldokio.recordings.play")) {
                    player.sendMessage(Bukkit.getPermissionMessage());
                    return true;
                }
                jukeBoxID =  MLDokio.getInstance().getJukeBoxPool().dedicateJukeBox(player, cassette.getBuffer(),  true);
                MLDokio.getInstance().getJukeBoxPool().getJukeBoxByIdOrNullIfNotFound(jukeBoxID).play();
                MLDokio.getMessages().sendWithTrackLabel(player, "recordings.playing-recording", trackLabel);
            } else if(sub.equalsIgnoreCase("stop")){
                if(!player.hasPermission("mldokio.recordings.stop")) {
                    player.sendMessage(Bukkit.getPermissionMessage());
                    return true;
                }
                if(jukeBoxID!=null)
                    MLDokio.getInstance().getJukeBoxPool().getJukeBoxByIdOrNullIfNotFound(jukeBoxID).stop();
                MLDokio.getMessages().sendWithTrackLabel(player, "recordings.stopped-playing-recording",trackLabel);
            } else if(sub.equalsIgnoreCase("get")){
                if(!player.hasPermission("mldokio.recordings.get")) {
                    player.sendMessage(Bukkit.getPermissionMessage());
                    return true;
                }
                player.getInventory().addItem(DiscFactory.createDisc(new ItemStack(Material.MUSIC_DISC_STAL),cassette));
                MLDokio.getMessages().sendWithTrackLabel(player, "recordings.disc-given",trackLabel);
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
