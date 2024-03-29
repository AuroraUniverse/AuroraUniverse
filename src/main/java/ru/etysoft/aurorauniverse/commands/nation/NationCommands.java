package ru.etysoft.aurorauniverse.commands.nation;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.AuroraEconomy;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
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
                    new NationTeleportCommand(player, resident, args, sender);
                } else if (args[0].equalsIgnoreCase("invite")) {
                    inviteTown();
                } else if (args[0].equalsIgnoreCase("accept")) {
                    acceptInvite();
                } else if (args[0].equalsIgnoreCase("leave")) {
                    leave();
                } else if (args[0].equalsIgnoreCase("online")) {
                    try {
                        new NationOnlineCommand(resident, args, sender);
                    } catch (TownNotFoundedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    new NationDepositCommand(resident, args, sender);
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    new NationWithdrawCommand(resident, args, sender);
                } else if (args[0].equalsIgnoreCase("kick")) {
                    kick();
                } else if (args[0].equalsIgnoreCase("list")) {
                    list();
                } else if (args[0].equalsIgnoreCase("tax")) {
                    setTax();
                } else if (args[0].equalsIgnoreCase("rename")) {
                    new NationRenameCommand(resident, args, sender);
                } else
                {
                    Nation nation = Nations.getNation(Messaging.getStringFromArgs(args, 0));
                    if(nation != null)
                    {
                        Messaging.sendNationInfo(sender, nation);
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                    }
                }
            } else {
                if (resident == null) {
                    Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                } else {

                    try {
                        if (resident.getTown().getNation() != null) {

                            Messaging.sendNationInfo(sender, resident.getTown().getNation());
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), sender);
                        }
                    } catch (TownNotFoundedException e) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
                    }

                }
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }

        return true;
    }

    public void leave() {
        if (Permissions.canLeaveNation(player)) {

            try {
                if (resident.getTown().getNation() != null) {
                    Nation nation = resident.getTown().getNation();

                    nation.removeTown(resident.getTown());

                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-leave"), player);
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void list()
    {
        player.sendMessage(ColorCodes.toColor(AuroraLanguage.getColorString("nation-list")));
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), player);
            }
        }

        int townOnPage = 8;
        int index = 0;
        double d = Double.parseDouble("" + Nations.getNations().size());
        double maxPage = Math.ceil((double) d / townOnPage);
        int fromIndex = (page - 1) * townOnPage;
        int toIndex = fromIndex + townOnPage;


        for (Nation nation : Nations.getNationsFromBiggest()) {
            if (index >= fromIndex && index < toIndex) {
                try {
                    player.sendMessage(ChatColor.AQUA + nation.getName() + ChatColor.GOLD + "(" + (nation.getTowns().size() + 1) + ", " + nation.getCapital().getName() + ")");
                }
                catch (Exception e)
                {
                    player.sendMessage(nation.getName() + " (bad nation)");
                }
            }
            index++;
        }

        player.sendMessage(AuroraLanguage.getColorString("town-pages").replace("%s", String.valueOf(page)).replace("%y", ((int)maxPage) + ""));
    }

    public void kick() {
        if (Permissions.canKickNation(player)) {

            try {
                if (resident.getTown().getNation() != null) {

                    if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                        String townName = Messaging.getStringFromArgs(args, 1);
                        try {
                            if (resident.getTown().getNation().getTownNames().contains(townName)) {
                                try {
                                    resident.getTown().getNation().removeTown(Towns.getTown(townName));
                                } catch (TownNotFoundedException e) {
                                    e.printStackTrace();
                                }
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-kick").replace("%s", townName), player);
                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-wrong-town").replace("%s", townName), player);
                            }
                        } catch (TownNotFoundedException e) {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
                        }

                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-no-capital"), player);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void acceptInvite() {
        if (Permissions.canAcceptNationInvite(player)) {
            String nationName = Messaging.getStringFromArgs(args, 1);
            if (Nations.getNation(nationName) != null) {
                Nation nation = Nations.getNation(nationName);

                try {
                    if (nation.getInvitedTowns().contains(resident.getTown())) {
                        nation.getInvitedTowns().remove(resident.getTown());
                        nation.addTown(resident.getTown());
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-accepted"), player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-no-invite"), player);
                    }
                } catch (TownNotFoundedException e) {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-wrong-town").replace("%s", resident.getTownName()), player);
                }
            } else {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("wrong-nation"), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void inviteTown() {
        if (Permissions.canInviteNation(player)) {
            String townName = Messaging.getStringFromArgs(args, 1);
            try{
                if (!resident.getTown().getNation().getInvitedTowns().contains(Towns.getTown(townName)) | !resident.getTown().getNation().hasTown(Towns.getTown(townName))) {
                    if (resident.getTown().getNation().getCapital() == resident.getTown()) {
                        if (Towns.getTown(townName) != resident.getTown().getNation().getCapital()) {
                            resident.getTown().getNation().getInvitedTowns().add(Towns.getTown(townName));
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-invite-sent").
                                    replace("%s", townName), player);

                            TextComponent msgJSON = new TextComponent(AuroraLanguage.getColorString("accept"));
                            msgJSON.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AuroraLanguage.getColorString("nation-accept-json")).create()));
                            msgJSON.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/n accept " + resident.getTown().getNationName()));

                            TextComponent msg = new TextComponent(AuroraUniverse.getPrefix() + " " + AuroraLanguage.getColorString("nation-invitation").replace("%s", resident.getTown().getNationName()));

                            ComponentBuilder builder = new ComponentBuilder();

                            builder.append(msg);
                            builder.append("\n");
                            builder.append(msgJSON);

                            Towns.getTown(townName).getMayor().getPlayer().spigot().sendMessage(builder.create());

                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-capital-edit"), player);
                        }

                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-no-capital"), player);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-wrong-town").replace("%s", townName), player);
                }
            } catch (TownNotFoundedException ignored){
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-wrong-town").replace("%s", townName), player);
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void createNation() {
        if (Permissions.canCreateNation(player)) {
            try {
                if (resident.getTown().getNation() == null) {
                    String name = Messaging.getStringFromArgs(args, 1);
                    if (!Nations.isNameValid(name)) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("name-invalid").replace("%s", name), player);
                        return;
                    }
                    double priceNewNation = AuroraUniverse.getInstance().getConfig().getDouble("price-new-nation");
                    if (AuroraEconomy.canPay(resident, priceNewNation)) {
                        resident.getBank().withdraw(priceNewNation);

                        Nation nation = new Nation(name, resident.getTown());

                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-created").replace("%s", name), player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("price-error").replace("%s", String.valueOf(priceNewNation)), player);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-already"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
                e.printStackTrace();
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void spawn() {
        if (Permissions.canTeleportNationSpawn(player)) {
            try {
                if (resident.getTown().getNation() != null) {
                    resident.getTown().getNation().getCapital().teleportToTownSpawn(player);

                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-teleport"), player);
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
                e.printStackTrace();
            }
        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void setTax() {
        if (Permissions.canDeleteNation(player)) {
            try {
                if (resident.getTown().getNation() != null) {
                    try {
                        double tax = Double.parseDouble(args[1]);
                        if(tax < 0)  Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-tax-error"), player);
                        if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                            resident.getTown().getNation().setTax(tax);
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-tax")
                                    .replace("%s", String.valueOf(tax)), player);
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-no-capital"), player);
                        }
                    } catch (Exception e) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-tax-error"), player);
                    }

                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
            }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }

    public void deleteNation() {
        if (Permissions.canDeleteNation(player)) {
            try {
                if (resident.getTown().getNation() != null) {
                    if (resident.getTown() == resident.getTown().getNation().getCapital()) {
                        resident.getTown().getNation().delete();
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-deleted"), player);
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("nation-no-capital"), player);
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-nation"), player);
                }
            } catch (TownNotFoundedException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), player);
            }

        } else {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), player);
        }
    }


}
