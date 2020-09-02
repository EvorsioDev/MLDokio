package ru.armagidon.mldokio.messaging;

import lombok.SneakyThrows;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.armagidon.mldokio.MLDokio;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Set;

public class MessageManager
{

    private final File messageFile;
    private FileConfiguration messagesConfig;

    public MessageManager() {
        if(!MLDokio.getInstance().getDataFolder().exists()) {
            if(MLDokio.getInstance().getDataFolder().mkdirs()){
                MLDokio.getInstance().getLogger().info("Making plugin directory!");
            } else {
                MLDokio.getInstance().getLogger().warning("Failed to make plugin directory!");
            }
        }
        this.messageFile = new File(MLDokio.getInstance().getDataFolder(), "messages.yml");
        this.messagesConfig = pullDefaultConfig();
        repair();
    }


    private FileConfiguration pullDefaultConfig(){
        if(!messageFile.exists()) {
            try {
                Files.copy(getClass().getResourceAsStream("/messages.yml"),
                        messageFile.toPath());
            } catch (IOException e){
                MLDokio.getInstance().getLogger().severe("Error occurred while coping locale file!");
            }
        }
        return YamlConfiguration.loadConfiguration(messageFile);
    }

    @SneakyThrows
    private void repair(){
        YamlConfiguration original = YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getResourceAsStream("/messages.yml")));
        Set<String> originalKeys = original.getKeys(true);
        Set<String> currentKeys = messagesConfig.getKeys(true);
        int counter = 0;
        for (String key : originalKeys) {
            if(!currentKeys.contains(key)){
                counter++;
                messagesConfig.set(key, original.get(key));
            }
        }
        if(counter!=0) messagesConfig.save(messageFile);
    }

    public void reload(){
        messagesConfig = pullDefaultConfig();
        repair();
    }

    public void send(CommandSender recipient, String path){
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&',messagesConfig.getString(path, "null")));
    }

    public void sendWithTrackLabel(CommandSender recipient, String path, String label){
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&',messagesConfig.getString(path, "null").replace("{track.label}",label)));
    }
}
