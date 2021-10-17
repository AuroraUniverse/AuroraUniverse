package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

public class DeleteTownCommand {

    public DeleteTownCommand(Resident resident, Player pl, String[] args, CommandSender sender) {
        try {
            if (resident != null) {
                if (Permissions.canDeleteTown(pl)) {
                    if(args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
                        if (resident.getTown().delete()) {
                            Messaging.sendPrefixedMessage(ColorCodes.toColor(AuroraConfiguration.getColorString("town-deleted-message")), pl);
                        }
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(ColorCodes.toColor(AuroraConfiguration.getColorString("town-delete-confirm")), pl);
                    }

                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            } else {
                if (Permissions.canDeleteTown(sender)) {
                    if (args.length > 1) {
                        if (Towns.getTown(args[1]).delete()) {
                            Messaging.sendPrefixedMessage(Messages.adminTownDelete(args[1]), sender);
                        } else {
                            Messaging.sendPrefixedMessage(Messages.adminCantTownDelete(args[1]), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-arguments"), sender);
                    }
                }
            }
        } catch (Exception e) {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantcreate-message").replace("%s", e.getMessage()), pl);
        }
    }

}
