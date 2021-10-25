package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownSpawnCommand {
    public TownSpawnCommand(Resident resident, CommandSender sender, Player pl) {
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            if (resident.hasTown()) {
                if (Permissions.canTeleportSpawn(pl)) {
                    Town t = resident.getTown();
                    t.teleportToTownSpawn(pl);
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-teleported-to-spawn"), sender);
                    pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100.0f, 1.0f);
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), sender);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        }
    }
}
