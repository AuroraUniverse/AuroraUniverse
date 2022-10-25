package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownEmbargoCommand extends CommandPattern{
    public TownEmbargoCommand(CommandSender sender, Resident resident, String[] args) {
        super(sender, resident, args);

        if(sender.hasPermission(Permissions.EMBARGO)) {
            if (args.length > 2)
            {
                if(resident.hasTown())
                {
                    try {
                        Town town = resident.getTown();
                        Town townEmbargo =  Towns.getTown(args[2]);
                        if(townEmbargo == null || townEmbargo.getName().equals(town.getName()))
                        {

                            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                            return;
                        }
                        if(args[1].equalsIgnoreCase("add"))
                        {
                            if(!town.hasEmbargoForTown(townEmbargo))
                            {
                                town.addEmbargoTown(townEmbargo);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("embargo-add").replace("%s", townEmbargo.getName()), sender);
                            }
                            else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("embargo-contains"), sender);
                            }

                        }
                        else if(args[1].equalsIgnoreCase("remove"))
                        {
                            if(town.hasEmbargoForTown(townEmbargo)) {
                                town.removeEmbargoTown(townEmbargo);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("embargo-remove").replace("%s", townEmbargo.getName()), sender);
                            }
                            else
                            {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("embargo-not-contains"), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                        }

                    } catch (TownNotFoundedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
            else
            {
                Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(Messages.noPermission(), sender);
        }





    }
}
