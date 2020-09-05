package ru.armagidon.mldokio.util.observer;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.Set;

public interface Observable<T>
{
    Set<Observer<T>> getObservers();
    default void registerObserver(Observer<T> observer){
        Validate.notNull(observer);
        getObservers().add(observer);
    }
    default void unregisterObserver(Observer<T> observer){
        Validate.notNull(observer);
        if(!getObservers().contains(observer)) return;
        getObservers().remove(observer);
    }
    default void notifyObservers(Player player, T data){
        getObservers().forEach(observer -> observer.update(player,data));
    }
}
