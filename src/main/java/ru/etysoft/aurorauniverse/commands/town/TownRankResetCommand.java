package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

public class TownRankResetCommand {

    public TownRankResetCommand(Resident resident, String[] args, CommandSender sender)
    {
        if (args.length == 3)
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
                                resident1.setTownRank("");
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
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-resettownrank-incorrect"), sender);
        }
    }
}
