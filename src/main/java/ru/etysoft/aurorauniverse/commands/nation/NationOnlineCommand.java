package ru.etysoft.aurorauniverse.commands.nation;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;

public class NationOnlineCommand {
    public NationOnlineCommand(Resident resident, String[] args, CommandSender sender) throws TownNotFoundedException {
        if (args.length <= 1) {
            if (resident.hasTown() && resident.getTown().hasNation()) {
                Nation nation = resident.getTown().getNation();
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-online")
                        .replace("%n", String.valueOf(nation.getOnline())).replace("%res", String.join(", ", nation.getResidentsOnline())), sender);
                return;
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), sender);
                return;
            }
        }
        if (args.length >= 2) {
            String string = String.join(" ", args);
            string = string.replace("online ", "");
            System.out.println(string);
            if (Nations.getNation(string) != null) {
                Nation nation = Nations.getNation(string);
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-online")
                        .replace("%n", String.valueOf(nation.getOnline())).replace("%res", String.join(", ", nation.getResidentsOnline())), sender);
                return;
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
                return;
            }
        }
    }
}
