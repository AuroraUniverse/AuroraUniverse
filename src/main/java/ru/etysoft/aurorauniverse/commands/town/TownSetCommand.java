package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
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

        try
        {
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
                } else if (args[1].equalsIgnoreCase("mayor")) {
                    if (args.length > 2) {
                        if (resident == null) {
                            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
                        } else {
                            setMayor();
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
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }


    }

    private void setTax() throws TownNotFoundedException {
        if (Permissions.canSetTax(sender)) {
            Town t = resident.getTown();
            try {
                double resTax = Double.parseDouble(args[2]);
                t.setResTax(resTax);
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-settax"), player);
            } catch (Exception e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-tax-error"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    private void setPermission() throws TownNotFoundedException {
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
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        }
    }

    private void setSpawn() throws TownNotFoundedException {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetSpawn(sender)) {
                try {
                    t.setSpawn(player.getLocation());
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-setspawn"), sender);
                } catch (TownException e) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantsetspawn").replace("%s", e.getErrorMessage()), player);

                }

            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        }
    }

    private void setMayor() throws TownNotFoundedException {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetMayor(sender)) {
                String newMayorName = args[2];

                Resident mayor = Residents.getResident(newMayorName);


                if (mayor != null) {
                    if (mayor.getTown() == t) {
                        t.setMayor(mayor);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-setmayor"), sender);
                        return;
                    }
                }

                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-cantsetmayor"), sender);


            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        }
    }

    private void setGroup() throws TownNotFoundedException {
        if (resident.hasTown()) {
            Town t = resident.getTown();
            if (Permissions.canSetGroup(sender)) {

                String groupName = args[2];
                String residentNickname = args[3];
                if (!groupName.equals("mayor") && t.getMayor() != Residents.getResident(residentNickname)) {
                    if (Residents.getResident(residentNickname) != null && t.getResidents().contains(Residents.getResident(residentNickname))) {
                        if (!groupName.equals("newbies") && AuroraPermissions.getGroups().keySet().contains(groupName)) {
                            Residents.getResident(residentNickname).setPermissionGroup(groupName);
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-group-set")
                                    .replace("%s", groupName)
                                    .replace("%p", residentNickname), sender);
                            try {
                                AuroraPermissions.setPermissions(residentNickname, AuroraPermissions.getGroup(groupName));
                            } catch (Exception ignored) {
                            }

                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-group-does-not-exists"), sender);
                        }
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-resident-not-in-town"), sender);
                    }


                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-group-mayor"), sender);
                }


            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
            }
        }
    }
}
