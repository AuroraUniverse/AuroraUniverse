package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownWithdrawCommand {
    public TownWithdrawCommand(String[] args, Resident resident, CommandSender sender) {
        if (args.length > 1) {
            if (resident == null) {
                if (args.length > 2) {
                    try {
                    Town town = Towns.getTown(args[1]);
                    if (town == null) {
                        Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                    } else {

                            double towithdraw = Double.valueOf(args[2]);
                            town.withdrawBank(towithdraw);

                    }
                    } catch (Exception e) {
                        Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                    }
                } else {
                    Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                }
            } else {
               try {
                    if (Permissions.canWithdrawTown(sender)) {
                        Town t = resident.getTown();
                        double d = 0;
                        try {
                            d = Double.valueOf(args[1]);

                        } catch (Exception e) {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
                        }

                        if (t.withdrawBank(d, resident)) {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-withdraw").replace("%s", d + ""), sender);
                            resident.giveBalance(d);
                            t.sendMessage(AuroraLanguage.getColorString("town-withdraw-other").replace("%s", d + "")
                                    .replace("%r", resident.getName()));
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantwithdraw").replace("%s", d + ""), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException ignored){
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
        }
    }
}
