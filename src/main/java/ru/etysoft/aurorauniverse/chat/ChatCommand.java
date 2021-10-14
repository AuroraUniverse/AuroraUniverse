package ru.etysoft.aurorauniverse.chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

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
        }
        return true;
    }
}
