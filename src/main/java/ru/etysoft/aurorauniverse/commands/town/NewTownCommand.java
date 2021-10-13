package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Town;

public class NewTownCommand {

    public NewTownCommand(CommandSender sender, String[] args) {
        FileConfiguration language = AuroraUniverse.getLanguage();
        if (args.length > 1) {
            try {
                Player pl = (Player) sender;
                if (Permissions.canCreateTown(pl)) {
                    StringBuilder name = new StringBuilder();
                    for (String arg :
                            args) {
                        if (!arg.equals(args[0])) {
                            name.append(arg).append(" ");
                        }
                    }
                    try {
                        String townname = name.toString().replace("&", "");
                        if (Towns.createTown(townname, pl)) {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-created-message").replace("%s", name), sender);

                            Residents.getResident(pl).setLastwild(false);
                        }

                    } catch (TownException e) {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantcreate-message").replace("%s", e.getErrorMessage()), pl);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            } catch (Exception e) {

                if (!(sender instanceof Player)) {
                    Town newtown = new Town();


                    AuroraUniverse.getTownlist().put(args[1], newtown);
                } else {
                    Logger.error("Town creating error: ");
                    e.printStackTrace();
                    Messaging.sendPrefixedMessage("Error!", sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-arguments"), sender);
        }
    }
}
