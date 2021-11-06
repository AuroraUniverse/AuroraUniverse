package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;

public class OutpostCommands extends CommandPattern {
    public OutpostCommands(CommandSender sender, Resident resident, String[] args) {
        super(sender, resident, args);
        if(Permissions.canTeleportOutpost(sender))
        {
            if(sender instanceof Player)
            {
                // /t outpost(0) 1(1)
                if(resident.hasTown())
                {

                        Town t = null;
                        try {

                            t = resident.getTown();
                            int outpostIndex = 0;
                            if (args.length > 1) {
                                outpostIndex = Integer.parseInt(args[1]);
                            }

                            t.teleportToOutpost((Player) sender, outpostIndex);

                        } catch (TownNotFoundedException e) {
                            e.printStackTrace();
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                        } catch (Exception e)
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-no-outpost"), sender);
                        }


                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
        }
    }
}
