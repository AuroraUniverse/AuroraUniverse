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
            Messaging.mess(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                if (Permissions.canLeaveTown(sender)) {
                    Town t = resident.getTown();
                    t.removeResident(resident);
                    Messaging.mess(AuroraConfiguration.getColorString("town-leave").replace("%s", t.getName()), sender);
                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            }
        }
    }

}
