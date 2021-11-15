package ru.etysoft.aurorauniverse.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.HashSet;

public class ChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Resident resident = Residents.getResident(sender.getName());

        if(resident != null) {
            int selectedChat;
          if (label.equalsIgnoreCase("chat") && args.length == 2) {
                if(args[0].equalsIgnoreCase("ignore"))
                {
                    int chatMode = AuroraChat.Channels.GLOBAL;
                    switch (args[1]){
                        case "nation":
                            chatMode = AuroraChat.Channels.NATION;
                            break;
                        case "town":
                            chatMode = AuroraChat.Channels.TOWN;
                            break;
                        case "local":
                            chatMode = AuroraChat.Channels.LOCAL;
                            break;
                    }

                    boolean isIgnore = resident.toggleIgnore(chatMode);
                    if(isIgnore)
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.ignore")
                                .replace("%s", AuroraChat.getChannelName(chatMode)), sender);
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.no-ignore")
                                .replace("%s", AuroraChat.getChannelName(chatMode)), sender);
                    }

                }
                return true;
            }
            if (label.equalsIgnoreCase("g") | label.equalsIgnoreCase("global")) {

                if(resident.getChatMode() != AuroraChat.Channels.GLOBAL) {
                    resident.setChatMode(AuroraChat.Channels.GLOBAL);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.changed")
                            .replace("%s", AuroraLanguage.getColorString("chat.channels.global")), sender);
                }
            }
            else  if (label.equalsIgnoreCase("local") | label.equalsIgnoreCase("lc")) {
                if(resident.getChatMode() != AuroraChat.Channels.LOCAL) {
                    resident.setChatMode(AuroraChat.Channels.LOCAL);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.changed")
                            .replace("%s", AuroraLanguage.getColorString("chat.channels.local")), sender);
                }
            }
            else  if (label.equalsIgnoreCase("tc")) {
                if(resident.getChatMode() != AuroraChat.Channels.TOWN) {
                    resident.setChatMode(AuroraChat.Channels.TOWN);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.changed")
                            .replace("%s", AuroraLanguage.getColorString("chat.channels.town")), sender);
                }
            }
            else  if (label.equalsIgnoreCase("nc")) {
                if(resident.getChatMode() != AuroraChat.Channels.NATION) {
                    resident.setChatMode(AuroraChat.Channels.NATION);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("chat.changed")
                            .replace("%s", AuroraLanguage.getColorString("chat.channels.nation")), sender);
                }
            }

            selectedChat = resident.getChatMode();
            if(args.length > 0)
            {
                String message = Messaging.getStringFromArgs(args, 0);
                if(sender instanceof Player)
                {
                    if(!resident.getIgnoreChannels().contains(selectedChat))
                    {
                        AuroraChat.processMessage(message, Bukkit.getPlayer(sender.getName()), new HashSet<Player>(Bukkit.getOnlinePlayers()), true);
                    }
                }
            }
        }
        return true;
    }



}
