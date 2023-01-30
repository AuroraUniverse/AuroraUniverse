package ru.etysoft.aurorauniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.ChunkPair;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.ResidentRegion;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class TownTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();

        ArrayList<String> possibleArgs = new ArrayList<>();

        ArrayList<String> firstPossibleArg = new ArrayList<>();

        firstPossibleArg.add("new");
        firstPossibleArg.add("toggle");
        firstPossibleArg.add("set");
        firstPossibleArg.add("delete");
        firstPossibleArg.add("deposit");
        firstPossibleArg.add("invite");
        firstPossibleArg.add("kick");
        firstPossibleArg.add("list");
        firstPossibleArg.add("embargo");
        firstPossibleArg.add("leave");
        firstPossibleArg.add("rename");
        firstPossibleArg.add("spawn");
        firstPossibleArg.add("region");
        firstPossibleArg.add("claim");
        firstPossibleArg.add("unclaim");
        firstPossibleArg.add("withdraw");
        firstPossibleArg.add("gui");
        firstPossibleArg.add("outpost");
        firstPossibleArg.add("accept");
        firstPossibleArg.add("rank");

        for (Town r : Towns.getTowns()) {
            if(Permissions.canSeeTownInfo(sender)) {
                firstPossibleArg.add(r.getName());
            }
        }

        if (args.length >= 1) {
            if (args.length == 1) {
                possibleArgs.addAll(firstPossibleArg);
            } else if (args.length == 2) {
                if (args[0].equals("set")) {
                    possibleArgs.add("spawn");
                    possibleArgs.add("perm");
                    possibleArgs.add("group");
                    possibleArgs.add("mayor");
                    possibleArgs.add("tax");
                } else if (args[0].equals("toggle")) {
                    possibleArgs.add("pvp");
                    possibleArgs.add("fire");
                    possibleArgs.add("mobs");
                    possibleArgs.add("explosions");
                } else if (args[0].equals("region")) {
                    possibleArgs.add("add");
                    possibleArgs.add("kick");
                    possibleArgs.add("give");
                    possibleArgs.add("reset");
                    possibleArgs.add("pvp");
                } else if (args[0].equals("accept")) {
                    for (Town r : Towns.getTowns()) {
                        possibleArgs.add(r.getName());
                    }
                } else if (args[0].equals("embargo")) {
                    possibleArgs.add("add");
                    possibleArgs.add("remove");
                } else if (args[0].equals("outpost")) {
                    try {
                        Resident resident = Residents.getResident(sender.getName());
                        if (resident != null && resident.hasTown()) {
                            Town town = resident.getTown();

                            possibleArgs.add(String.valueOf(town.getOutPosts().size() - 1));

                        }
                    } catch (Exception ignored) {
                    }
                } else if (args[0].equals("spawn") && Permissions.canTeleportOnTowns(sender)) {
                    for (Town r : Towns.getTowns()) {
                        possibleArgs.add(r.getName());
                    }
                } else if (args[0].equals("invite")) {
                    for (Player r : Bukkit.getOnlinePlayers()) {
                        possibleArgs.add(r.getName());
                    }
                } else if (args[0].equals("kick")) {
                    try {
                        Resident resident = Residents.getResident(sender.getName());
                        if (resident != null && resident.hasTown()) {
                            Town town = resident.getTown();
                            for (Resident r : town.getResidents()) {
                                possibleArgs.add(r.getName());
                            }
                        }
                    } catch (Exception ignored) {
                    }
                } else if (args[0].equals("rank"))
                {
                    possibleArgs.add("set");
                    possibleArgs.add("reset");
                }

            } else if (args.length == 3) {
                if (args[0].equals("toggle")) {
                    possibleArgs.add("on");
                    possibleArgs.add("off");
                } else if (args[1].equals("perm")) {

                    for (String groupName : AuroraPermissions.getGroups().keySet()) {
                        possibleArgs.add(groupName);
                    }
                }
                if ((args[0].equals("embargo") && (args[1].equals("add")))) {
                    for (Town r : Towns.getTowns()) {
                        possibleArgs.add(r.getName());
                    }
                }

                if ((args[0].equals("embargo") && (args[1].equals("remove")))) {
                    Player player = (Player) sender;
                    Resident resident = Residents.getResident(player.getName());
                    if (resident != null) {
                        if (resident.hasTown()) {

                            try {
                                Town town = resident.getTown();
                                for (Town r : town.getEmbargoList()) {
                                    possibleArgs.add(r.getName());
                                }
                            } catch (TownNotFoundedException ignored) {

                            }

                        }
                    }

                }
                if ((args[0].equals("region") && (args[1].equals("pvp")))) {
                    possibleArgs.add("on");
                    possibleArgs.add("off");
                }

                if ((args[0].equals("rank") && args[1].equals("set")) | (args[0].equals("rank") && args[1].equals("reset")))
                {
                    Player player = (Player) sender;
                    Resident resident = Residents.getResident(player.getName());
                    try {
                        Town town = resident.getTown();
                        for (Resident r : town.getResidents()) {
                            possibleArgs.add(r.getName());
                        }
                    } catch (TownNotFoundedException ignored) {

                    }
                }

                if ((args[0].equals("region") && (args[1].equals("give") | args[1].equals("add") | args[1].equals("kick")))) {
                    Player player = (Player) sender;
                    Resident resident = Residents.getResident(player.getName());
                    if (resident != null) {
                        boolean onlyResidents = !AuroraUniverse.getInstance().getConfig().getBoolean("region-allow-noresidents");
                        if(args[1].equals("give"))
                        {
                            if (resident.hasTown()) {

                                try {
                                    Town town = resident.getTown();
                                    for (Resident r : town.getResidents()) {
                                        possibleArgs.add(r.getName());
                                    }
                                } catch (TownNotFoundedException ignored) {

                                }

                            }
                        }
                        else  if(args[1].equals("add"))
                        {

                            if (resident.hasTown()) {

                                if(onlyResidents)
                                {
                                    try {
                                        Town town = resident.getTown();
                                        for (Resident r : town.getResidents()) {
                                            possibleArgs.add(r.getName());
                                        }
                                    } catch (TownNotFoundedException ignored) {

                                    }
                                }
                                else
                                {

                                        for (Resident r : Residents.getList()) {
                                            possibleArgs.add(r.getName());
                                        }

                                }


                            }
                        }
                        else  if(args[1].equals("kick"))
                        {

                            if (resident.hasTown()) {


                                    try {


                                        Town town = resident.getTown();

                                        possibleArgs.addAll(((ResidentRegion) town.getTownChunks().get(ChunkPair.fromChunk(player.getLocation().getChunk()))).getMembers());
                                    } catch (Exception ignored) {

                                    }




                            }
                        }

                    }
                }
                if (args[1].equals("group")) {
                    Player player = (Player) sender;
                    Resident resident = Residents.getResident(player.getName());
                    if (resident != null) {
                        if (resident.hasTown()) {
                            for (String groupName : AuroraPermissions.getGroups().keySet()) {
                                possibleArgs.add(groupName);
                            }
                        }
                    }
                }


            } else if (args.length == 4) {
                if (args[1].equals("perm")) {
                    possibleArgs.add("build");
                    possibleArgs.add("destroy");
                    possibleArgs.add("use");
                    possibleArgs.add("switch");
                }
                if (args[1].equals("group")) {
                    Player player = (Player) sender;
                    Resident resident = Residents.getResident(player.getName());
                    if (resident != null) {
                        if (resident.hasTown()) {

                            try {
                                Town town = resident.getTown();
                                for (Resident r : town.getResidents()) {
                                    possibleArgs.add(r.getName());
                                }
                            } catch (TownNotFoundedException e) {
                                e.printStackTrace();
                            }

                        }
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
