package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownSpawnCommand extends CommandPattern {
    public TownSpawnCommand(CommandSender sender, Resident resident, String[] args) {
        super(sender, resident, args);
        if (resident == null) {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
        } else {
            try {
                Player pl = (Player) sender;
                if (args.length < 2) {
                    if (Permissions.canTeleportSpawn(pl)) {
                        Town t = resident.getTown();
                        t.teleportToTownSpawn(pl);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-teleported-to-spawn"), sender);
                        pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100.0f, 1.0f);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                }
                else
                {
                    if (Permissions.canTeleportOnTowns(pl)) {
                        try
                        {
                            Town t = Towns.getTown(Messaging.getStringFromArgs(args, 1));
                            t.teleportToTownSpawn(pl);
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-teleported-to-spawn"), sender);
                            pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100.0f, 1.0f);
                        }
                        catch (TownNotFoundedException ignored)
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_TOWN), sender);
                        }

                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                }
            } catch (TownNotFoundedException ignored){
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        }
    }
}
