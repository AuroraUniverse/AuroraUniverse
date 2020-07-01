package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownSetCommand {

    private Resident resident;
    private CommandSender sender;
    private Player player;

    public TownSetCommand(String[] args, Resident resident, CommandSender sender, Player pl) {
        this.resident = resident;
        this.sender = sender;
        this.player = pl;
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("spawn")) {
                if (resident == null) {
                    Messaging.mess(Messages.cantConsole(), sender);
                } else {
                    setSpawn();
                }
            } else {
                Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
            }
        }


    }

    private void setSpawn() {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetSpawn(sender)) {
                try {
                    t.setSpawn(player.getLocation());
                    Messaging.mess(AuroraConfiguration.getColorString("town-setspawn"), sender);
                } catch (TownException e) {
                    Messaging.mess(AuroraConfiguration.getColorString("town-cantsetspawn").replace("%s", e.getMessageErr()), player);

                }

            } else {
                Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        }


    }
}
