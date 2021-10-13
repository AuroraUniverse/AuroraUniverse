package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownKickCommand {

    public TownKickCommand(Resident resident, String[] args, CommandSender sender) {
        if (args.length > 1) {
            if (resident == null) {
                Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            } else {
                Player de = (Player) Bukkit.getPlayer(args[1]);
                Resident resident2 = Residents.getResident(de);
                if (resident.hasTown() && resident2 != null) {
                    Town t = resident.getTown();
                    if (Permissions.canKickResident(sender)) {
                        t.removeResident(resident2);
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-kick").replace("%s", resident2.getName()), sender);


                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                }
            }
        }
    }

}
