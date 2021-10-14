package ru.etysoft.aurorauniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class EconomyTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();
        if (Permissions.canBalance(sender)) {
            firstPossibleArg.add("balance");
        }

        if (Permissions.canPay(sender)) {
            firstPossibleArg.add("pay");
        }


        if (Permissions.canGive(sender)) {
            firstPossibleArg.add("give");
        }
        if (Permissions.canTake(sender)) {
            firstPossibleArg.add("take");
        }


        if (args.length >= 1) {
            if (args.length == 1) {
                possibleArgs.addAll(firstPossibleArg);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    possibleArgs.add(player.getName());
                }
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
