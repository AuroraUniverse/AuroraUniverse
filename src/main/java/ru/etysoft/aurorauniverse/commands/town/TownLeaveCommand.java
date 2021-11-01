package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.concurrent.ExecutionException;

public class TownLeaveCommand {

    public TownLeaveCommand(Resident resident, CommandSender sender) {
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            try {
                Town t = resident.getTown();
                if (Permissions.canLeaveTown(sender) && t.getMayor() != resident) {

                    t.removeResident(resident);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-leave").replace("%s", t.getName()), sender);
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                }
            }
            catch (TownNotFoundedException ignored)
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        }
    }

}
