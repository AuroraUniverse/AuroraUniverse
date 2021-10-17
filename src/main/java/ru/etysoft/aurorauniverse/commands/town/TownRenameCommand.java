package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownRenameCommand {

    public TownRenameCommand(Resident resident, String[] args, CommandSender sender) {
        if (args.length > 1) {
            if (resident == null) {
                Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            } else {
                StringBuilder name = new StringBuilder();
                int i = 0;
                for (String arg :
                        args) {
                    if (!arg.equals(args[0])) {
                        if (i != args.length - 1) {
                            name.append(arg).append(" ");
                        } else {
                            name.append(arg);
                        }
                    }
                    i++;
                }

                String newName = name.toString().replace("&", "").replace("%", "");
                if(Towns.isNameValid(newName)) {


                    if (resident.hasTown()) {
                        Town t = resident.getTown();
                        if (Permissions.canRenameTown(sender)) {
                            t.rename(newName);
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-rename").replace("%s", newName), sender);
                        } else {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                    }
                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("name-invalid").replace("%s", newName), sender);
                }
            }
        }
    }

}
