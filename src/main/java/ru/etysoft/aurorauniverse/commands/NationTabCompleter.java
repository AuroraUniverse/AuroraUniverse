package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;
import java.util.List;

public class NationTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();

        firstPossibleArg.add("new");
        firstPossibleArg.add("delete");
        firstPossibleArg.add("invite");
        firstPossibleArg.add("kick");
        firstPossibleArg.add("leave");
        firstPossibleArg.add("spawn");
        firstPossibleArg.add("accept");
        firstPossibleArg.add("tax");
        firstPossibleArg.add("list");

        for(Nation nation : Nations.getNations())
        {
            firstPossibleArg.add(nation.getName());
        }


        if (args.length >= 1) {
            if (args.length == 1) {
                possibleArgs.addAll(firstPossibleArg);
            } else if (args.length == 2) {
                if (args[0].equals("invite")) {
                    for (String t : AuroraUniverse.getTownList().keySet()) {
                        possibleArgs.add(t);
                    }
                } else if (args[0].equals("accept")) {
                    for (String t : AuroraUniverse.nationList.keySet()) {
                        possibleArgs.add(t);
                    }
                } else if (args[0].equals("kick")) {
                    Resident resident = Residents.getResident(sender.getName());
                    if (resident != null) {
                        try {
                            if (resident.getTown().getNation() != null) {
                                for (String t : resident.getTown().getNation().getTownNames()) {
                                    possibleArgs.add(t);
                                }
                            }
                        }
                        catch (Exception ignored)
                        {

                        }
                    }
                }

            }


        }


        for (String arg : possibleArgs) {
            if (arg.toLowerCase().contains(args[args.length - 1].toLowerCase())) {
                result.add(arg);
            }
        }
        return result;
    }
}
