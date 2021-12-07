package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
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
                            Messaging.sendPrefixedMessage(ColorCodes.toColor(AuroraLanguage.getColorString("town-deleted-message")), pl);
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantdelete"), sender);
                        }
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(ColorCodes.toColor(AuroraLanguage.getColorString("town-delete-confirm")), pl);
                    }

                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                }
            }
            else
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-delete-canceled"), sender);
            }
        } catch (TownNotFoundedException e) {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        } catch (Exception e) {
            e.printStackTrace();
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("admin.town-cantdelete").replace("%s", e.getMessage()), sender);
        }
    }

}
