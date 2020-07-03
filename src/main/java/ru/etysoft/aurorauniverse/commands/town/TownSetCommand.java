package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownSetCommand {

    private Resident resident;
    private CommandSender sender;
    private Player player;
    private String[] args;

    public TownSetCommand(String[] args, Resident resident, CommandSender sender, Player pl) {
        this.resident = resident;
        this.sender = sender;
        this.player = pl;
        this.args = args;

        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("spawn")) {
                if (resident == null) {
                    Messaging.mess(Messages.cantConsole(), sender);
                } else {
                    setSpawn();
                }
            }
            if (args[1].equalsIgnoreCase("perm")) {
                if (args.length > 3)
                    if (resident == null) {
                        Messaging.mess(Messages.cantConsole(), sender);
                    } else {
                        setPermission();
                    }
            } else {
                Messaging.mess(Messages.wrongArgs(), sender);
            }
        }


    }

    private void setPermission() {
        if (resident.hasTown()) {
            Town t = resident.getTown();

            String groupname = args[2];
            String permisson = args[3];

            if (Permissions.canSetPermissions(sender)) {
                try {
                    if (AuroraPermissions.getGroups().containsKey(groupname)) {
                        if (permisson.equals("build")) {
                            if (t.toggleBuild(AuroraPermissions.getGroup(groupname))) {
                                Messaging.mess(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.mess(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("destroy")) {
                            if (t.toggleDestroy(AuroraPermissions.getGroup(groupname))) {
                                Messaging.mess(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.mess(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("use")) {
                            if (t.toggleUse(AuroraPermissions.getGroup(groupname))) {
                                Messaging.mess(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.mess(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("switch")) {
                            if (t.toggleSwitch(AuroraPermissions.getGroup(groupname))) {
                                Messaging.mess(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.mess(Messages.disablePerm(groupname, permisson), sender);
                            }
                        }
                    } else {
                        Messaging.mess(Messages.cantSetPerm(groupname, permisson), sender);
                    }

                } catch (Exception e) {
                    Messaging.mess(Messages.cantSetPerm(groupname, permisson), sender);

                }

            } else {
                Messaging.mess(AuroraConfiguration.getColorString("access-denied-message"), player);
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
