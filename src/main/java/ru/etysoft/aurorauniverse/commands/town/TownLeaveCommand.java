package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownLeaveCommand {

    public TownLeaveCommand(Resident resident, CommandSender sender) {
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                Town t = resident.getTown();
                if (Permissions.canLeaveTown(sender) && t.getMayor() != resident) {

                    t.removeResident(resident);
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-leave").replace("%s", t.getName()), sender);
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            }
        }
    }

}
