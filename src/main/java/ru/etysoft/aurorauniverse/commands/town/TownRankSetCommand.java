package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

public class TownRankSetCommand {

    public TownRankSetCommand(Resident resident, String[] args, CommandSender sender)
    {
        if (args.length > 3)
        {
            String string = "";

            for (int i = 3; i < args.length; i++)
            {
                if (i == args.length - 1)
                {
                    string += args[i];
                }
                else
                {
                    string += args[i] + " ";
                }
            }

            if (AuroraUniverse.matchesNameRegex(string))
            {
                if (string.length() <= AuroraUniverse.getInstance().getConfig().getInt("max-rank-length", 10))
                {
                    if (resident != null)
                    {
                        Resident resident1 = Residents.getResident(args[2]);

                        if (resident1 != null)
                        {
                            try {
                                if (sender.hasPermission(Permissions.TOWN_RANK))
                                {
                                    if (resident.getTown().getResidents().contains(resident1))
                                    {
                                        resident1.setTownRank(string);
                                    }
                                    else
                                    {
                                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("not-registered-resident"), sender);
                                    }
                                }
                                else
                                {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                                }

                            } catch (TownNotFoundedException e) {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("name-invalid").replace("%s", args[2]), sender);
                        }
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                    }
                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("name-invalid").replace("%s", string), sender);
                }
            }
            else
            {

            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-townrank-command-incorrect"), sender);
        }
    }
}
