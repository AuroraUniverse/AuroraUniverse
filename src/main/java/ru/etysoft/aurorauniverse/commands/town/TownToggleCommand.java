package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownToggleCommand {

    private Resident resident;
    private CommandSender sender;
    private Player player;
    private String[] args;

    public TownToggleCommand(String[] args, Resident resident, CommandSender sender, Player pl) {
        this.resident = resident;
        this.sender = sender;
        this.player = pl;
        this.args = args;

        if (args.length > 2) {
            if (resident == null) {
                Messaging.mess(Messages.cantConsole(), sender);
            } else {
                if (args[1].equalsIgnoreCase("pvp")) {
                    togglePvP();
                } else if (args[1].equalsIgnoreCase("fire")) {
                    toggleFire();
                }
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
        }
    }

    private void toggleFire() {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canToggleFire(player)) {
                if (args[2].equals("on")) {
                    t.isFire(true);
                    Messaging.mess(AuroraConfiguration.getColorString("town-fireon"), player);
                } else if (args[2].equals("off")) {
                    t.isFire(false);
                    Messaging.mess(AuroraConfiguration.getColorString("town-fireoff"), player);
                }
            } else {
                Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
        }
    }

    private void togglePvP() {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canTogglePvP(player)) {
                if (args[2].equals("on")) {
                    t.setPvP(true);
                    Messaging.mess(AuroraConfiguration.getColorString("town-pvpon"), player);
                } else if (args[2].equals("off")) {
                    t.setPvP(false);
                    Messaging.mess(AuroraConfiguration.getColorString("town-pvpoff"), player);
                }
            } else {
                Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        } else {
            Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
        }
    }

}
