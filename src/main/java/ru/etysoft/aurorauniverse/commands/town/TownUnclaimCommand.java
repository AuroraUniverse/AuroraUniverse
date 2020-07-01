package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownUnclaimCommand {

    public TownUnclaimCommand(Resident resident, CommandSender sender, Player pl) {
        if (resident == null) {
            Messaging.mess(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                Town t = resident.getTown();
                if (Permissions.canUnClaim(pl)) {

                    if (t.unclaimChunk(pl.getLocation().getChunk())) {
                        Messaging.mess(AuroraConfiguration.getColorString("town-unclaim"), sender);
                        resident.setLastwild(true);
                        Towns.ChangeChunk(pl, pl.getLocation().getChunk());
                    } else {
                        Messaging.mess(AuroraConfiguration.getColorString("town-cantunclaim"), sender);
                    }

                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            }
        }
    }

}
