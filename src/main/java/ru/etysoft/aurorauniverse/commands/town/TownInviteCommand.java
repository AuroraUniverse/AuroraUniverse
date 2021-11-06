package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownInviteCommand {

    public TownInviteCommand(String[] args, Resident resident, CommandSender sender) {
        if (args.length > 1) {
            if (resident != null) {
                Player de = (Player) Bukkit.getPlayer(args[1]);
                Resident resident2 = Residents.getResident(de);
                try {
                    if (resident2 == null) throw new TownNotFoundedException();
                    Town t = resident.getTown();
                    if (Permissions.canInviteResident(sender)) {
                        if (!t.getInvitedResidents().contains(resident2)) {
                            t.getInvitedResidents().add(resident2);
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-invite").replace("%s", resident2.getName()), sender);
                            if (Bukkit.getPlayer(sender.getName()) != null) {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-request").replace("%s", t.getName()), Bukkit.getPlayer(resident2.getName()));
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-accept-err"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException ignored) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }

}
