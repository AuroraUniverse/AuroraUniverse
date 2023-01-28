package ru.etysoft.aurorauniverse.commands.nation;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;

public class NationRenameCommand
{

    public NationRenameCommand(Resident resident, String[] args, CommandSender sender)
    {
        if (args.length > 1)
        {
            if (resident == null)
            {
                Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            }
            else
            {
                String newName = "";

                for (int i = 1; i < args.length; i++)
                {
                    if (i < args.length - 1)
                    {
                        newName += args[i] + " ";
                    }
                    else
                    {
                        newName += args[i];
                    }
                }

                if (Nations.isNameValid(newName) && !AuroraUniverse.getNationList().containsKey(newName))
                {

                    try
                    {
                        Nation nation = resident.getTown().getNation();

                        if (nation == null)
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), sender);
                        }
                        else
                        {
                            if (nation.getCapital().getMayor().getName().equals(resident.getName()))
                            {
                                nation.rename(newName);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-rename"), sender);
                                resident.getTown().getNation().sendMessage(AuroraLanguage.getColorString("nation-rename-others").
                                        replace("%p", resident.getName()).replace("%n", newName));
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-not-capital-mayor"), sender);
                            }
                        }

                    }
                    catch (TownNotFoundedException e) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                    }

                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("name-invalid").replace("%s", newName), sender);
                }
            }

        }
    }

}
