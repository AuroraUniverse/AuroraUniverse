package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.commands.town.*;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.AuroraEconomy;
import ru.etysoft.aurorauniverse.placeholders.AuroraPlaceholdersExpansion;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;

public class NationCommands implements CommandExecutor {

    private Resident resident;
    private Player player;
    private String[] args;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player pl = null;
        resident = null;
        this.args = args;
        if (sender instanceof Player) {
            pl = (Player) sender;
            player = pl;
            resident = Residents.getResident(pl);
        } else {
            Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            return true;
        }
        if (resident.hasTown()) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("new")) {
                    createNation();
                } else if (args[0].equalsIgnoreCase("delete")) {
                    deleteNation();
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    if (resident.getTown().getNation() != null) {
                        resident.getTown().getNation().getCapital().teleportToTownSpawn(player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), sender);
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    inviteTown();
                } else if (args[0].equalsIgnoreCase("accept")) {
                    acceptInvite();

                } else if (args[0].equalsIgnoreCase("leave")) {
                    leave();
                } else if (args[0].equalsIgnoreCase("kick")) {
                    kick();

                } else if (args[0].equalsIgnoreCase("tax")) {
                    setTax();
                }
            } else {
                if (resident == null) {
                    Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                } else {

                    if (resident.getTown().getNation() != null) {

                        Messaging.sendNationInfo(sender, resident.getTown().getNation());
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), sender);
                    }

                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("town-dont-belong"), sender);
        }

        return true;
    }

    public void leave() {
        if (Permissions.canLeaveNation(player)) {

            if (resident.getTown().getNation() != null) {
                Nation nation = resident.getTown().getNation();

                nation.removeTown(resident.getTown());

                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-leave"), player);
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void kick() {
        if (Permissions.canKickNation(player)) {

            if (resident.getTown().getNation() != null) {

                if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                    String townName = Messaging.getNameFromArgs(args, 1);
                    if (resident.getTown().getNation().getTownNames().contains(townName)) {
                        resident.getTown().getNation().removeTown(Towns.getTown(townName));
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-kick").replace("%s", townName), player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-wrong-town").replace("%s", townName), player);
                    }

                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-no-capital"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void acceptInvite() {
        if (Permissions.canAcceptNationInvite(player)) {
            String nationName = Messaging.getNameFromArgs(args, 1);
            if (Nations.getNation(nationName) != null) {
                Nation nation = Nations.getNation(nationName);

                if (nation.getInvitedTowns().contains(resident.getTown())) {
                    nation.getInvitedTowns().remove(resident.getTown());
                    nation.addTown(resident.getTown());
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-accepted"), player);
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-no-invite"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("wrong-nation"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void inviteTown() {
        if (Permissions.canInviteNation(player)) {
            String townName = Messaging.getNameFromArgs(args, 1);
            if (Towns.getTown(townName) != null) {
                if (!resident.getTown().getNation().getInvitedTowns().contains(Towns.getTown(townName)) | !resident.getTown().getNation().hasTown(Towns.getTown(townName))) {
                    if (resident.getTown().getNation().getCapital() == resident.getTown()) {
                        if (Towns.getTown(townName) != resident.getTown().getNation().getCapital()) {
                            resident.getTown().getNation().getInvitedTowns().add(Towns.getTown(townName));
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-invite-sent").replace("%s", townName), player);
                        } else {
                            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-capital-edit"), player);
                        }

                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-no-capital"), player);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-wrong-town").replace("%s", townName), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-wrong-town").replace("%s", townName), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void createNation() {
        if (Permissions.canCreateNation(player)) {
            if (resident.getTown().getNation() == null) {
                String name = Messaging.getNameFromArgs(args, 1);
                if (!Towns.isNameValid(name)) {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("name-invalid").replace("%s", name), player);
                    return;
                }
                double priceNewNation = AuroraUniverse.getInstance().getConfig().getDouble("price-new-nation");
                if (AuroraEconomy.canPay(resident, priceNewNation)) {
                    resident.getBank().withdraw(priceNewNation);

                    Nation nation = new Nation(name, resident.getTown());

                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-created").replace("%s", name), player);
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("price-error").replace("%s", String.valueOf(priceNewNation)), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-already"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void setTax() {
        if (Permissions.canDeleteNation(player)) {
            if (resident.getTown().getNation() != null) {
                try {
                    double tax = Double.valueOf(args[1]);
                    if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                        resident.getTown().getNation().setTax(tax);
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-tax")
                                .replace("%s", String.valueOf(tax)), player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-no-capital"), player);
                    }
                } catch (Exception e) {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-tax-error"), player);
                }

            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), player);
            }

        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }

    public void deleteNation() {
        if (Permissions.canSetTax(player)) {
            if (resident.getTown().getNation() != null) {
                if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                    resident.getTown().getNation().delete();
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-deleted"), player);
                } else {
                    Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("nation-no-capital"), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("no-nation"), player);
            }

        } else {
            Messaging.sendPrefixedMessage(AuroraConfiguration.getColorString("access-denied-message"), player);
        }
    }
}
