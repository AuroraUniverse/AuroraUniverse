package ru.etysoft.aurorauniverse.commands.nation;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class NationDepositCommand {
    public NationDepositCommand(Resident resident, String[] args, CommandSender sender) {
        if (args.length > 1) {
            if (resident == null) {
                if (args.length > 2) {

                        Nation nation = Nations.getNation(args[1]);
                        try {
                            double todeposit = Double.valueOf(args[2]);
                            nation.getBank().deposit(todeposit);
                        } catch (Exception e) {
                            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                        }

                } else {
                    Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                }
            } else {
                try {
                    if (Permissions.canDepositNation(sender)) {
                        Town t = resident.getTown();
                        if(t.hasNation()) {
                            double d = 0;
                            try {
                                d = Double.parseDouble(args[1]);


                            if (resident.takeBalance(d)) {
                                t.getNation().getBank().deposit(d);
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-deposit").replace("%s", d + ""), sender);
//                                t.getNation().sendMessage(AuroraLanguage.getColorString("town-deposit-other").replace("%s", d + "")
//                                        .replace("%r", resident.getName()));

                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-cantdeposit").replace("%s", d + ""), sender);
                            }
                            } catch (Exception e) {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
                            }
                        }
                        else
                        {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), sender);
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
