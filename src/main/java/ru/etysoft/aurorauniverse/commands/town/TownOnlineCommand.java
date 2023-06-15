package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownOnlineCommand {
    public TownOnlineCommand (Resident resident, String[] args, CommandSender sender) throws TownNotFoundedException {
        if (args.length <= 1) {
            if (!resident.hasTown()) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                return;
            }
            Town town = resident.getTown();
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-online")
                    .replace("%n", String.valueOf(town.getOnline())).replace("%res", String.join(", ", town.getResidentsOnline())), sender);
            return;
        }
        if (args.length >= 2) {
            if (Towns.getTown(args[1]) != null) {
                Town town = Towns.getTown(args[1]);
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-online")
                        .replace("%n", String.valueOf(town.getOnline())).replace("%res", String.join(", ", town.getResidentsOnline())), sender);
                return;
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
                return;
            }
        }

    }
}
