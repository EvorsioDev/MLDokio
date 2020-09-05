package ru.armagidon.mldokio.util.observer;

import org.bukkit.entity.Player;

public interface Observer<T>
{
    void update(Player player, T data);
}
