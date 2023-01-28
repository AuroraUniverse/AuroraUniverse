package ru.etysoft.aurorauniverse.commands.nation;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

public class NationTeleportCommand {

    public NationTeleportCommand(Player player, Resident resident, String[] args, CommandSender sender)
    {
        try {
            if (resident.getTown().getNation() != null)
            {
                if (args.length == 1)
                {
                    resident.getTown().getNation().getCapital().teleportToTownSpawn(player);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-teleport"), sender);
                }
                else if (args.length == 2)
                {
                    if (resident.getTown().getNation().getTownNames().contains(args[1]))
                    {
                        AuroraUniverse.getTownList().get(args[1]).teleportToTownSpawn(player);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-teleport-to-city").replace("%t", args[1]), sender);
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-teleport-incorrect"), sender);
                    }
                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-teleport-incorrect"), sender);
                }
            }
            else
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), sender);
            }
        } catch (TownNotFoundedException e) {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
        }
    }
}
