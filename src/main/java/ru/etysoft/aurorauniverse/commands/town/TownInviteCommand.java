package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
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
                if (resident.hasTown() && resident2 != null) {
                    Town t = resident.getTown();
                    if (Permissions.canInviteResident(sender)) {
                        t.addResident(resident2);
                        Messaging.mess(AuroraConfiguration.getColorString("town-invite").replace("%s", resident2.getName()), sender);

                    } else {
                        Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), sender);
                    }
                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                }
            }
        } else {
            Messaging.mess(Messages.wrongArgs(), sender);
        }
    }

}
