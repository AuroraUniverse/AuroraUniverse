package ru.etysoft.aurorauniverse.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.auction.AuctionItem;
import ru.etysoft.aurorauniverse.commands.town.*;
import ru.etysoft.aurorauniverse.data.Auction;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.gui.GUIAuction;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;

public class TownCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player pl = null;
            Resident resident = null;
            if (sender instanceof Player) {
                pl = (Player) sender;
                resident = Residents.getResident(pl);
            }

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("new")) {
                    new NewTownCommand(sender, args);
                } else if (args[0].equalsIgnoreCase("delete")) {
                    new DeleteTownCommand(resident, pl, args, sender);
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    new TownSpawnCommand(sender, resident, args);
                } else if (args[0].equalsIgnoreCase("list")) {
                    new TownListCommand(sender, args);
                } else if (args[0].equalsIgnoreCase("rename")) {
                    new TownRenameCommand(resident, args, sender);
                } else if (args[0].equalsIgnoreCase("claim")) {
                    new TownClaimCommand(resident, sender, pl);
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    new TownUnclaimCommand(resident, sender, pl);
                } else if (args[0].equalsIgnoreCase("leave")) {
                    new TownLeaveCommand(resident, sender);
                } else if (args[0].equalsIgnoreCase("deposit")) {
                    new TownDepositCommand(resident, args, sender);
                } else if (args[0].equalsIgnoreCase("withdraw")) {
                    new TownWithdrawCommand(args, resident, sender);
                } else if (args[0].equalsIgnoreCase("set")) {
                    new TownSetCommand(args, resident, sender, pl);
                } else if (args[0].equalsIgnoreCase("outpost")) {
                    new OutpostCommands(sender, resident, args);
                } else if (args[0].equalsIgnoreCase("region")) {
                    new TownRegionCommand(sender, resident, args);
                } else if (args[0].equalsIgnoreCase("kick")) {
                    new TownKickCommand(resident, args, sender);
                } else if (args[0].equalsIgnoreCase("invite")) {
                    new TownInviteCommand(args, resident, sender);
                } else if (args[0].equalsIgnoreCase("accept")) {
                    new TownAcceptCommand(args, resident, sender);
                } else if (args[0].equalsIgnoreCase("gui")) {
                    new TownGuiCommand(resident, pl, args, sender);
                } else if (args[0].equalsIgnoreCase("auction")) {
                        new GUIAuction(resident, pl, sender, 1);
                } else if (args[0].equalsIgnoreCase("sell")) {
                    new TownSellCommand(sender, resident, args);
                } else if (args[0].equalsIgnoreCase("toggle")) {
                    new TownToggleCommand(args, resident, sender, pl);
                } else {

                    try
                    {
                        if(Permissions.canSeeTownInfo(sender)) {
                            Town town = Towns.getTown(Messaging.getStringFromArgs(args, 0));
                            Messaging.sendTownInfo(sender, town);
                        }
                        else
                        {
                            sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED));
                        }
                    }
                    catch (TownNotFoundedException ignored)
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                    }
                }
            } else {
                if (resident == null) {
                    Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
                } else {
                    if (resident.hasTown()) {
                        Messaging.sendTownInfo(sender, resident.getTown());
                    } else {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                    }
                }
            }
            return true;
        }
        catch (Exception e)
        {
            Logger.error("Error processing command!");
            e.printStackTrace();
            return true;
        }
    }

}
