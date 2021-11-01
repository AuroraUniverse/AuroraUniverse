package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Sound;
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
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                Town t = resident.getTown();
                if (Permissions.canUnClaim(pl)) {

                    if (t.unclaimChunk(pl.getLocation().getChunk())) {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-unclaim"), sender);
                        resident.setLastwild(true);
                        Towns.handleChunkChange(pl, pl.getLocation().getChunk());
                        pl.playSound(pl.getLocation(), Sound.ENTITY_CAT_HURT, 100.0f, 1.0f);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantunclaim"), sender);
                    }

                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            }
        }
    }

}
