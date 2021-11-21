package ru.etysoft.aurorauniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();

        firstPossibleArg.add("prices");

        if(Permissions.isAdmin(sender, false))
        {
            firstPossibleArg.add("town");
            firstPossibleArg.add("stalin");
            firstPossibleArg.add("reload");
            firstPossibleArg.add("debug");
            firstPossibleArg.add("dhand");
        }



        if (args.length >= 1) {
            if (args.length == 1) {
                possibleArgs.addAll(firstPossibleArg);
            } else if (args.length == 2) {
                if (args[0].equals("town")) {
                    possibleArgs.add("delete");
                    possibleArgs.add("remove");
                    possibleArgs.add("outpost");
                    possibleArgs.add("spawn");
                    possibleArgs.add("add");
                    possibleArgs.add("deposit");
                    possibleArgs.add("withdraw");
                    possibleArgs.add("givebonus");
                }

            } else if (args.length == 3) {
                if (args[1].equals("delete")) {
                    for (Town r : Towns.getTowns()) {
                        possibleArgs.add(r.getName());
                    }
                }

                if ((args[1].equals("remove") || (args[1].equals("add")))) {
                    for (Resident r : Residents.getList()) {
                        possibleArgs.add(r.getName());
                    }
                }


            } else if (args.length == 4) {
                if (args[1].equals("deposit") | args[1].equals("withdraw") | args[1].equals("add") | args[1].equals("remove") | args[1].equals("givebonus")) {
                    for (Town r : Towns.getTowns()) {
                        possibleArgs.add(r.getName());
                    }
                }
            }
        } else {

        }

        for (String arg : possibleArgs) {
            if (arg.contains(args[args.length - 1])) {
                result.add(arg);
            }
        }
        return result;
    }
}
