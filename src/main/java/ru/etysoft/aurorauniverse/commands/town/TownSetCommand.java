package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;

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

        if (resident.hasTown()) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("spawn")) {
                    if (resident == null) {
                        Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                    } else {
                        setSpawn();
                    }
                } else if (args[1].equalsIgnoreCase("perm")) {
                    if (args.length > 3) {
                        if (resident == null) {
                            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                        } else {
                            setPermission();
                        }
                    }
                } else if (args[1].equalsIgnoreCase("tax")) {
                    if (args.length > 2) {
                        if (resident == null) {
                            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                        } else {
                            setTax();
                        }
                    }
                } else if (args[1].equalsIgnoreCase("group")) {
                    if (args.length > 3) {
                        if (resident == null) {
                            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                        } else {
                            setGroup();
                        }
                    } else {
                        Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                    }
                }
            } else {
                Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
        }


    }

    private void setTax() {
        if(Permissions.canSetTax(sender)) {
            Town t = resident.getTown();
            try {
                double resTax = Double.parseDouble(args[2]);
                t.setResTax(resTax);
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-settax"), player);
            }
            catch (Exception e)
            {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-tax-error"), player);
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
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
                                Messaging.sendPrefixedMessage(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.sendPrefixedMessage(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("destroy")) {
                            if (t.toggleDestroy(AuroraPermissions.getGroup(groupname))) {
                                Messaging.sendPrefixedMessage(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.sendPrefixedMessage(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("use")) {
                            if (t.toggleUse(AuroraPermissions.getGroup(groupname))) {
                                Messaging.sendPrefixedMessage(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.sendPrefixedMessage(Messages.disablePerm(groupname, permisson), sender);
                            }
                        } else if (permisson.equals("switch")) {
                            if (t.toggleSwitch(AuroraPermissions.getGroup(groupname))) {
                                Messaging.sendPrefixedMessage(Messages.enablePerm(groupname, permisson), sender);
                            } else {
                                Messaging.sendPrefixedMessage(Messages.disablePerm(groupname, permisson), sender);
                            }
                        }
                    } else {
                        Messaging.sendPrefixedMessage(Messages.cantSetPerm(groupname, permisson), sender);
                    }

                } catch (Exception e) {
                    Messaging.sendPrefixedMessage(Messages.cantSetPerm(groupname, permisson), sender);

                }

            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        }
    }

    private void setSpawn() {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetSpawn(sender)) {
                try {
                    t.setSpawn(player.getLocation());
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-setspawn"), sender);
                } catch (TownException e) {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-cantsetspawn").replace("%s", e.getErrorMessage()), player);

                }

            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        }
    }

    private void setGroup() {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetGroup(sender)) {

                String groupName = args[2];
                String residentNickname = args[3];
                if (!groupName.equals("mayor")) {
                    if (Residents.getResident(residentNickname) != null && t.getResidents().contains(Residents.getResident(residentNickname))) {
                        if (AuroraPermissions.getGroups().keySet().contains(groupName)) {
                            Residents.getResident(residentNickname).setPermissonGroup(groupName);
                            AuroraPermissions.setPermissions(player, AuroraPermissions.getGroup(groupName));
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-group-set")
                                    .replace("%s", groupName)
                                    .replace("%s1", residentNickname), sender);
                        } else {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-group-does-not-exists"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-resident-not-in-town"), sender);
                    }


                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-group-mayor"), sender);
                }


            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
            }
        }
    }
}
