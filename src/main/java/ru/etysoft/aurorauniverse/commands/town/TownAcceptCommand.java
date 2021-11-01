package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownAcceptCommand {

    public TownAcceptCommand(String[] args, Resident resident, CommandSender sender) {
        if (args.length > 1) {
            if (resident != null) {
                String townName = Messaging.getStringFromArgs(args, 1);
                Town town = null;
                try {
                    town = Towns.getTown(townName);
                } catch (TownNotFoundedException e) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_TOWN), sender);
                }
                if (!resident.hasTown() && town != null) {

                    if (town.getInvitedResidents().contains(resident)) {
                        town.getInvitedResidents().remove(resident);
                        town.addResident(resident);
                        town.sendMessage(AuroraLanguage.getColorString("town-accepted").replace("%s", resident.getName()));

                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-accepted-nothing"), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-accept-err"), sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }
}
