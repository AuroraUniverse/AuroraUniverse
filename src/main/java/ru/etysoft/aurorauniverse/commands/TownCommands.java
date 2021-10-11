package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.commands.town.*;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

public class TownCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player pl = null;
        Resident resident = null;
        if (sender instanceof Player) {
            pl = (Player) sender;
            resident = Residents.getResident(pl);
        }

        if(args.length > 0) {
           if(args[0].equalsIgnoreCase("new")) {
               new NewTownCommand(sender, args);
           }
           else if(args[0].equalsIgnoreCase("delete")) {
               new DeleteTownCommand(resident, pl, args, sender);
           }
           else if(args[0].equalsIgnoreCase("spawn")) {
               new TownSpawnCommand(resident, sender, pl);
           }
           else if(args[0].equalsIgnoreCase("list")) {
               new TownListCommand(sender, args);
           }
           else if(args[0].equalsIgnoreCase("claim")) {
               new TownClaimCommand(resident, sender, pl);
           }
           else if(args[0].equalsIgnoreCase("unclaim")) {
               new TownUnclaimCommand(resident, sender, pl);
           }
           else if(args[0].equalsIgnoreCase("leave")) {
               new TownLeaveCommand(resident, sender);
           }
           else if(args[0].equalsIgnoreCase("deposit")) {
               new TownDepositCommand(resident, args, sender);
           }
           else if(args[0].equalsIgnoreCase("withdraw")) {
               new TownWithdrawCommand(args, resident, sender);
           }
           else if(args[0].equalsIgnoreCase("set")) {
               new TownSetCommand(args, resident, sender, pl);
           }
           else if(args[0].equalsIgnoreCase("giverg")) {
              TownRegionCommand.giveRegion(sender, resident, args);
           }
           else if(args[0].equalsIgnoreCase("resetrg")) {
               TownRegionCommand.resetRegion(sender, resident, args);
           }
           else  if(args[0].equalsIgnoreCase("kick")) {
               new TownKickCommand(resident, args, sender);
           }
           else if(args[0].equalsIgnoreCase("invite")) {
               new TownInviteCommand(args, resident, sender);
           }
           else if(args[0].equalsIgnoreCase("toggle")) {
               new TownToggleCommand(args, resident, sender, pl);
           }
           else
           {
               Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
           }
        }
        else {
            if (resident == null) {
                Messaging.mess(Messages.wrongArgs(), sender);
            } else {
                if (resident.hasTown()) {
                    Messaging.towninfo(sender, resident.getTown());
                } else {
                    Messaging.mess(AuroraConfiguration.getColorString("town-dont-belong"), sender);
                }
            }
        }
        return true;
    }
}
