package ru.etysoft.aurorauniverse.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.listeners.ChatListener;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;

public class AuroraChat {

    private static AuroraChat instance;
    private static ChatCommand chatCommand;

    private AuroraChat(){}

    public static void initialize()
    {

        if(instance == null)
        {
            instance = new AuroraChat();
        }
        AuroraUniverse.registerListener(new ChatListener());


    }

    public static void sendGlobalMessage(String string)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(string);
        }
    }

    public ChatCommand getChatCommand()
    {
        if(chatCommand == null)
        {
           chatCommand = new ChatCommand();
        }
        return chatCommand;
    }

    public static AuroraChat getInstance() {
        return instance;
    }

    public static class Channels
    {
        public static final int GLOBAL = 0;
        public static final int LOCAL = 1;
        public static final int TOWN = 2;
    }
}
