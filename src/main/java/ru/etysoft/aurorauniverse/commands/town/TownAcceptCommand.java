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

public class TownAcceptCommand {

    public TownAcceptCommand(String[] args, Resident resident, CommandSender sender) {
        if (args.length > 1) {
            if (resident != null) {
                String townName = Messaging.getNameFromArgs(args, 1);
                Town town = Towns.getTown(townName);
                if (!resident.hasTown() && town != null) {

                    if (town.getInvitedResidents().contains(resident)) {
                        town.getInvitedResidents().remove(resident);
                        town.addResident(resident);
                        town.sendMessage(AuroraConfiguration.getColorString("town-accepted").replace("%s", resident.getName()));

                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-accepted-nothing"), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-accept-err"), sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }
}
