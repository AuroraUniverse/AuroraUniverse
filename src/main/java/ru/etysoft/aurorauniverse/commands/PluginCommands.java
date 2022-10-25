package ru.etysoft.aurorauniverse.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.town.TownAdminCommands;
import ru.etysoft.aurorauniverse.data.*;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.exceptions.WorldNotFoundedException;
import ru.etysoft.aurorauniverse.gulag.StalinNPC;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.placeholders.AuroraPlaceholdersExpansion;
import ru.etysoft.aurorauniverse.structures.*;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;
import ru.etysoft.aurorauniverse.world.WorldTimer;

import java.util.ArrayList;
import java.util.List;

public class PluginCommands implements CommandExecutor {

    public static List<String> debugHand = new ArrayList<>();

    public void setDebug(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (Permissions.isAdmin(sender, true)) {

                if (args[1].equalsIgnoreCase("on")) {
                    AuroraLanguage.setDebugMode(true);
                    Messaging.sendPrefixedMessage("Now debug mode is &aenabled", sender);
                } else if (args[1].equalsIgnoreCase("off")) {
                    AuroraLanguage.setDebugMode(false);
                    Messaging.sendPrefixedMessage("Now debug mode is &cdisabled", sender);
                } else {
                    sender.sendMessage(Messages.wrongArgs());
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }

    public void struct(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (Permissions.isAdmin(sender, true)) {
                if (sender instanceof Player) {
                    Location location = ((Player) sender).getLocation();

                    Structure structure = new Structure(location.getBlockX(), location.getBlockY(), location.getBlockZ(), args[1], location.getWorld().getName());
                    try {
                        structure.build(new Runnable() {
                            @Override
                            public void run() {
                                sender.sendMessage("Successfully built!");
                            }
                        });
                    } catch (WorldNotFoundedException e) {
                        e.printStackTrace();
                    } catch (StructureBuildException e) {
                        e.printStackTrace();
                    }
                } else {
                    Messaging.sendPrefixedMessage(Messages.Keys.ACCESS_DENIED, sender);
                }
            } else {
                Messaging.sendPrefixedMessage(Messages.Keys.ACCESS_DENIED, sender);
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.Keys.WRONG_ARGS, sender);
        }
    }

    public void saveStruct(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (Permissions.isAdmin(sender, true)) {
                int fromX = StructurePatterns.bufferFrom.getBlockX();
                int fromY = StructurePatterns.bufferFrom.getBlockY();
                int fromZ = StructurePatterns.bufferFrom.getBlockZ();

                int toX = StructurePatterns.bufferTo.getBlockX();
                int toY = StructurePatterns.bufferTo.getBlockY();
                int toZ = StructurePatterns.bufferTo.getBlockZ();


                if (fromX > toX) {
                    int tempX = toX;
                    toX = fromX;
                    fromX = tempX;
                }

                if (fromY > toY) {
                    int tempY = toY;
                    toY = fromY;
                    fromY = tempY;
                }

                if (fromZ > toZ) {
                    int tempZ = toZ;
                    toZ = fromZ;
                    fromZ = tempZ;
                }

                JSONArray jsonArray = new JSONArray();

                World world = StructurePatterns.bufferFrom.getWorld();

                Logger.debug(fromX + "; " + toX + ". " + fromY + "; " + toY + ". " + fromZ + "; " + toZ);

                for (int x = fromX; x <= toX; x++) {
                    for (int y = fromY; y <= toY; y++) {
                        for (int z = fromZ; z <= toZ; z++) {

                            (new Location(world, x, y, z)).getChunk().load();
                            Block block = world.getBlockAt(x, y, z);

                            if (block.getType() != Material.AIR) {
                                try {
                                    StructBlock structBlock = new StructBlock(x - fromX, y - fromY, z - fromZ, block);
                                    jsonArray.add(structBlock.toJson());

                                } catch (StructureWrongCoordsException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                StructurePatterns.savePattern(args[1], jsonArray);
                sender.sendMessage("Saved structure at structures/" + args[1] + ".json (" + jsonArray.size() + " blocks)");
            } else {
                Messaging.sendPrefixedMessage(Messages.Keys.ACCESS_DENIED, sender);
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.Keys.WRONG_ARGS, sender);
        }
    }


    public void save(CommandSender sender, String[] args) {
        if (Permissions.isAdmin(sender, true)) {
            DataManager.saveData();
            Auction.saveListings();
        } else {
            Logger.info(sender.getName() + " tried to use SAVE-ALL command. Access denied.");
        }
    }

    public void reload(CommandSender sender, String[] args) {
        if (Permissions.isAdmin(sender, true)) {
            Logger.debug("Reloading configuration...");
            AuroraUniverse.getInstance().reloadConfig();
            StructurePatterns.loadPatterns();

            AuroraUniverse.getInstance().saveDefaultConfig();
            AuroraUniverse.getInstance().setupLanguageFile();
            Messaging.sendPrefixedMessage(Messages.reload(), sender);
            Logger.debug("Reloading permission system...");
            AuroraPermissions.clear();
            AuroraPermissions.initialize();
        } else {
            Logger.info(sender.getName() + " tried to use RELOAD command. Access denied.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            //Has arguments

            if (args[0].equalsIgnoreCase("reload")) {
                reload(sender, args);
            } else if (args[0].equalsIgnoreCase("save-all")) {
                save(sender, args);
            } else if (args[0].equalsIgnoreCase("town")) {
                new TownAdminCommands(sender, Residents.getResident(sender.getName()), args);
            } else if (args[0].equalsIgnoreCase("debug")) {
                setDebug(sender, args);
            } else if (args[0].equalsIgnoreCase("struct")) {
                struct(sender, args);
            } else if (args[0].equalsIgnoreCase("save-struct")) {
                saveStruct(sender, args);
            } else if (args[0].equalsIgnoreCase("prices")) {
                Messaging.sendPrices(sender);
            } else if (args[0].equalsIgnoreCase("res")) {
                if (args.length > 1) {

                    if (args.length > 2) {
                        if (Permissions.canChangeResidentRep(sender)) {
                            Resident resident = Residents.getResident(args[1]);
                            Resident residentSelf = Residents.getResident(sender.getName());


                            if (resident != null && residentSelf != null) {

                                if(residentSelf.getHoursPlayed() >
                                        AuroraUniverse.getInstance().getConfig().getInt("reputation-min-hours"))
                                {
                                    if (args[2].equals("+rep")) {
                                        if (!resident.getRepPlus().contains(sender.getName())) {
                                            resident.addRepPlus(sender.getName());
                                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.REP_PLUS).replace("%s", resident.getName())
                                                    .replace("%rep", String.valueOf(resident.getRep())), sender);
                                        } else {
                                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.REP_CANT_CHANGE), sender);
                                        }
                                    } else if (args[2].equals("-rep")) {
                                        if (!resident.getRepMinus().contains(sender.getName())) {
                                            resident.addRepMinus(sender.getName());
                                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.REP_MINUS).replace("%s", resident.getName())
                                                    .replace("%rep", String.valueOf(resident.getRep())), sender);
                                        } else {
                                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.REP_CANT_CHANGE),
                                                    sender);
                                        }
                                    } else {
                                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                                    }
                                }
                                else
                                {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.REP_CANT_CHANGE), sender);
                                }



                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_RESIDENT), sender);
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED), sender);
                        }
                    } else {
                        if (Permissions.canSeeResidentInfo(sender)) {
                            Resident resident = Residents.getResident(args[1]);

                            if (resident != null) {
                                Messaging.sendResidentInfo(sender, resident);
                            } else {
                                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_RESIDENT), sender);
                            }
                        } else {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED), sender);
                        }
                    }


                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS), sender);
                }
            } else if (args[0].equalsIgnoreCase("stalin")) {
                if (Permissions.isAdmin(sender, true)) {
                    StalinNPC.create(((Player) sender).getLocation());
                }
            } else if (args[0].equalsIgnoreCase("newday")) {
                if (Permissions.isAdmin(sender, true)) {
                    WorldTimer.getInstance().initNewDay();
                }
            } else if (args[0].equalsIgnoreCase("dhand")) {
                if (AuroraLanguage.getDebugMode()) {
                    if (debugHand.contains(sender.getName())) {
                        debugHand.remove(sender.getName());
                    } else {
                        debugHand.add(sender.getName());
                    }

                }
            } else if (args[0].equalsIgnoreCase("givebonus")) {
                if (args.length > 2) {
                    if (Permissions.isAdmin(sender, true)) {
                        Town town = null;
                        try {
                            town = Towns.getTown(args[1]);
                            town.setBonusChunks(town.getBonusChunks() + Integer.parseInt(args[2]));
                            Messaging.sendPrefixedMessage("Successfully gave " + args[1] + " " + args[2] + " bonus chunks", sender);
                        } catch (TownNotFoundedException e) {
                            e.printStackTrace();
                            sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_TOWN));
                        }

                    }
                }

            }
        } else {
            //No arguments message
            Messaging.sendPluginInfo(sender, AuroraUniverse.getInstance());
        }
        return true;
    }
}
