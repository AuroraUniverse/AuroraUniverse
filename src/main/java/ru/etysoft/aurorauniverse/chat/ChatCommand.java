package ru.etysoft.aurorauniverse.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

import java.util.HashSet;

public class ChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Resident resident = Residents.getResident(sender.getName());

        if(resident != null) {
            if (label.equalsIgnoreCase("g") | label.equalsIgnoreCase("global")) {
                resident.setChatMode(AuroraChat.Channels.GLOBAL);
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("chat.changed")
                        .replace("%s", AuroraConfiguration.getColorString("chat.channels.global")), sender);
            }
            else  if (label.equalsIgnoreCase("local") | label.equalsIgnoreCase("lc")) {
                resident.setChatMode(AuroraChat.Channels.LOCAL);
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("chat.changed")
                        .replace("%s", AuroraConfiguration.getColorString("chat.channels.local")), sender);
            }
            else  if (label.equalsIgnoreCase("tc")) {
                resident.setChatMode(AuroraChat.Channels.TOWN);
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("chat.changed")
                        .replace("%s", AuroraConfiguration.getColorString("chat.channels.town")), sender);
            }
            if(args.length > 0)
            {
                String message = Messaging.getStringFromArgs(args, 0);
                if(sender instanceof Player)
                {
                  AuroraChat.processMessage(message, Bukkit.getPlayer(sender.getName()), new HashSet<Player>(Bukkit.getOnlinePlayers()));
                }
            }
        }
        return true;
    }
}
