package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.etysoft.aurorauniverse.AuroraUniverse;

import java.util.ArrayList;
import java.util.List;

public class AuctionTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();

        firstPossibleArg.add("sell");
        firstPossibleArg.add("inv");

        if (args.length == 1)
        {
            possibleArgs.addAll(firstPossibleArg);
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("inv"))
            {
                possibleArgs.addAll(AuroraUniverse.residentlist.keySet());
            }
        }

        for (String arg : possibleArgs) {
            if (arg.contains(args[args.length - 1])) {
                result.add(arg);
            }
        }

        return result;
    }
}