package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.commands.town.TownSellCommand;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.gui.GUIAuction;

public class AuctionCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("sell")) {
                new TownSellCommand(commandSender, Residents.getResident(commandSender.getName()), args);
            }
        } else {
            new GUIAuction(Residents.getResident(commandSender.getName()), (Player) commandSender, commandSender, 1);
        }

        return true;
    }
}
