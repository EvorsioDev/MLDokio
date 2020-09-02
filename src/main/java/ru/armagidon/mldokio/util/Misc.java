package ru.armagidon.mldokio.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public class Misc
{
    public static Set<Player> getNear(float radius, Location source){
        return Bukkit.getOnlinePlayers().stream().filter(p->p.getWorld().equals(source.getWorld())).filter(p->p.getLocation().distance(source)<=radius).collect(Collectors.toSet());
    }
}
