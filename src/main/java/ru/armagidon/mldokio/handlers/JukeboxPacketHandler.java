package ru.armagidon.mldokio.handlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import ru.armagidon.mldokio.MLDokio;
import ru.armagidon.mldokio.events.JukeboxSongPlayEvent;
import ru.armagidon.mldokio.events.JukeboxSongStopEvent;

import java.lang.reflect.Method;

public class JukeboxPacketHandler extends PacketAdapter
{

    public JukeboxPacketHandler() {
        super(MLDokio.getInstance(), ListenerPriority.HIGHEST,PacketType.Play.Server.WORLD_EVENT);
    }

    @SneakyThrows
    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Effect effect = Effect.getById(packet.getIntegers().read(0));
        if(effect==null) return;
        if(!effect.equals(Effect.RECORD_PLAY)) return;
        int data = packet.getIntegers().read(1);

        Material material = getMaterialByItem(data);

        BlockPosition position = packet.getBlockPositionModifier().read(0);
        Block block = event.getPlayer().getWorld().getBlockAt(position.toLocation(event.getPlayer().getWorld()));

        if(block.getType().equals(Material.JUKEBOX)) {
            Jukebox jukebox = (Jukebox) block.getState();
            if(material.equals(Material.MUSIC_DISC_STAL)) {
                boolean cancelled = !new JukeboxSongPlayEvent(jukebox, jukebox.getRecord()).callEvent();
                if (cancelled) {

                    PacketContainer cloned = event.getPacket().deepClone();
                    cloned.getIntegers().write(1,getAirId());
                    event.setPacket(cloned);
                }
            } else if(material.equals(Material.AIR)){
                new JukeboxSongStopEvent(jukebox, jukebox.getRecord()).callEvent();
            }
        }
    }

    @SneakyThrows
    private Class<?> getNmsClass(String nmsClassName)  {
        return Class.forName("net.minecraft.server." +nmsVersion() + "." + nmsClassName);
    }

    @SneakyThrows
    private Class<?> getCraftBukkitClass(String name){
        return Class.forName("org.bukkit.craftbukkit."+nmsVersion()+"."+name);
    }

    private static String nmsVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    @SneakyThrows
    private Object getItemById(int id){
        Class<?> itemClass = getNmsClass("Item");
        Method getById = itemClass.getDeclaredMethod("getById",int.class);
        return getById.invoke(null,id);
    }

    @SneakyThrows
    private int getAirId(){
        Class<?> craftEffectClass = getCraftBukkitClass("CraftEffect");
        Method getDataValue = craftEffectClass.getDeclaredMethod("getDataValue", Effect.class, Object.class);
        return (int) getDataValue.invoke(null, Effect.RECORD_PLAY, Material.AIR);
    }

    @SneakyThrows
    private Material getMaterialByItem(int id){
        Class<?> cmnClass = getCraftBukkitClass("util.CraftMagicNumbers");
        Method getMaterial = cmnClass.getDeclaredMethod("getMaterial",getNmsClass("Item"));
        return (Material) getMaterial.invoke(null, getItemById(id));
    }
}
