package ru.armagidon.mldokio.commands;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.armagidon.mldokio.MLDokio;

import java.lang.reflect.Constructor;
import java.util.List;

public abstract class BukkitCommand
{

    private final PluginCommand command;

    @SneakyThrows
    public BukkitCommand(String name) {
        Constructor<PluginCommand> pluginCMDConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        pluginCMDConstructor.setAccessible(true);
        command = pluginCMDConstructor.newInstance(name, MLDokio.getInstance());
        command.setExecutor((sender, command, label, args) -> {
            if(!(sender instanceof Player)) return true;
            return execute((Player) sender,label, args);
        });
        command.setTabCompleter((sender, command, alias, args) -> {
            if(sender instanceof Player) return tabComplete(((Player) sender), args);
            return ImmutableList.of();
        });
    }

    protected abstract boolean execute(Player player,String label,String[] args);

    protected List<String> tabComplete(Player player, String[] args) {return ImmutableList.of();}

    public final BukkitCommand register() {
        Bukkit.getCommandMap().register("mldokio",command);
        return this;
    }

    public final void unregister(){
        command.unregister(Bukkit.getCommandMap());
    }

    public PluginCommand getCommand() {
        return command;
    }
}
