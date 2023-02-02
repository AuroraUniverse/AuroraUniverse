package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
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
                Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            } else {
                if (args[1].equalsIgnoreCase("pvp")) {
                    togglePvP();
                } else if (args[1].equalsIgnoreCase("fire")) {
                    toggleFire();

                } else if (args[1].equalsIgnoreCase("mobs")) {
                    toggleMobs();

                } else if (args[1].equalsIgnoreCase("explosions")) {
                    toggleExplosions();
                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
        }
    }

    private void toggleFire() {
        try {
            Town t = resident.getTown();
            if (Permissions.canToggleFire(player)) {
                if (args[2].equals("on")) {
                    t.setFireAllowed(true);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-fireon"), player);
                } else if (args[2].equals("off")) {
                    t.setFireAllowed(false);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-fireoff"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

    private void toggleExplosions() {
        try {
            Town t = resident.getTown();
            if (Permissions.canToggleExplosions(player)) {
                if (args[2].equals("on")) {
                    t.setExplosionEnabled(true);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-expon"), player);
                } else if (args[2].equals("off")) {
                    t.setExplosionEnabled(false);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-expoff"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

    private void toggleMobs() {
        try {
            Town t = resident.getTown();
            if (Permissions.canToggleMobs(player)) {
                if (args[2].equals("on")) {
                    t.setMobs(true);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-mobson"), player);
                } else if (args[2].equals("off")) {
                    t.setMobs(false);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-mobsoff"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

    private void togglePvP() {
        try {
            Town t = resident.getTown();
            if (Permissions.canTogglePvP(player)) {
                if (args[2].equals("on")) {
                    t.setPvP(true);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-pvpon"), player);
                } else if (args[2].equals("off")) {
                    t.setPvP(false);
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-pvpoff"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

}
