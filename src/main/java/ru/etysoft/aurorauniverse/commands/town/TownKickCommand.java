package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
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
                Resident resident2 = Residents.getResident(args[1]);
                try {
                    if(resident2 == null)
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("not-registered-resident"), sender);
                        return;
                    }
                    Town t = resident.getTown();
                    if (Permissions.canKickResident(sender)) {
                        t.removeResident(resident2);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-kick").replace("%s", resident2.getName()), sender);


                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException ignored){
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
        }
    }

}
