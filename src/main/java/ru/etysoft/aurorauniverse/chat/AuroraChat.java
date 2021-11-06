package ru.etysoft.aurorauniverse.chat;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.chat.listeners.ChatListener;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.HashSet;
import java.util.Set;

public class AuroraChat {

    private static AuroraChat instance;
    private static ChatCommand chatCommand;

    private AuroraChat() {
    }

    public static void initialize() {

        if (instance == null) {
            instance = new AuroraChat();
        }
        AuroraUniverse.registerListener(new ChatListener());


    }

    public static String processMessage(String message, Player playerSender, Set<Player> allRecipients, boolean sendConsole)
    {
        if(!AuroraUniverse.matchesRegex(message)) return null;
        Set<Player> finalRecipients = new HashSet<>(allRecipients);
        Resident resident = Residents.getResident(playerSender);
        if (resident != null) {
            int channel = resident.getChatMode();
            if (channel == AuroraChat.Channels.GLOBAL) {
                message = AuroraLanguage.getColorString("chat.global").replace("%sender%", playerSender.getName())
                        .replace("%message%", message);
            } else if (channel == AuroraChat.Channels.LOCAL) {
                message = AuroraLanguage.getColorString("chat.local").replace("%sender%", playerSender.getName())
                        .replace("%message%",message);
                Set<Player> recipients = new HashSet<>();
                for (Player player : Bukkit.getOnlinePlayers()) {

                    if(player.getLocation().getWorld() == playerSender.getLocation().getWorld()) {
                        if (playerSender.getLocation().distance(player.getLocation()) < 20) {
                            recipients.add(player);
                        }
                    }
                }
                if (recipients.size() == 1) {
                    playerSender.sendMessage(AuroraLanguage.getColorString("chat.local-nobody"));
                }
                finalRecipients = recipients;

            } else if (channel == AuroraChat.Channels.TOWN) {

                message = AuroraLanguage.getColorString("chat.town").replace("%sender%", playerSender.getName())
                        .replace("%message%",message);
                Set<Player> recipients = new HashSet<>();
                if(!resident.hasTown())
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), playerSender);
                    return null;
                }
                try {
                    for (Resident townResident : resident.getTown().getResidents()) {
                        Player player = Bukkit.getPlayer(townResident.getName());
                        if(player != null)
                        {
                            recipients.add(Bukkit.getPlayer(townResident.getName()));
                        }

                    }
                } catch (TownNotFoundedException e) {
                    e.printStackTrace();
                }

                finalRecipients = recipients;


            }

        }

        if(Permissions.canSendColorCodes(playerSender))
        {
            message = ColorCodes.toColor(message);
        }

        for(Player player : finalRecipients)
        {
            player.spigot().sendMessage(getPreparedChatMessage(message, playerSender));
        }
        if(sendConsole)
        {
            Bukkit.getConsoleSender().sendMessage(message);
        }
        return message;
    }

    public static TextComponent getPreparedChatMessage(String message, Player player)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                PlaceholderFormatter.process(AuroraLanguage.getColorString("chat.hover-text"), player)).create() ) );
        return textComponent;
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
