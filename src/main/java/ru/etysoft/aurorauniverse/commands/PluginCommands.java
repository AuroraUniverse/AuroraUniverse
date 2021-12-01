package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.town.AdminCommands;
import ru.etysoft.aurorauniverse.data.DataManager;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.gulag.StalinNPC;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.security.Permission;
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
                }  else {
                    sender.sendMessage(Messages.wrongArgs());
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
        }
    }


    public void save(CommandSender sender, String[] args) {
        if (Permissions.isAdmin(sender, true)) {
            DataManager.saveData();
        } else {
            Logger.info(sender.getName() + " tried to use SAVE-ALL command. Access denied.");
        }
    }

    public void reload(CommandSender sender, String[] args) {
        if (Permissions.isAdmin(sender, true)) {
            Logger.debug("Reloading configuration...");
            AuroraUniverse.getInstance().reloadConfig();

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
            }
           else if (args[0].equalsIgnoreCase("save-all")) {
                    save(sender, args);
            } else if (args[0].equalsIgnoreCase("town")) {
                new AdminCommands(sender, Residents.getResident(sender.getName()), args);
            } else if (args[0].equalsIgnoreCase("debug")) {
                setDebug(sender, args);
            } else if (args[0].equalsIgnoreCase("prices")) {
                Messaging.sendPrices(sender);
            } else if (args[0].equalsIgnoreCase("res")) {
               if(args.length > 1)
               {

                   if(Permissions.canSeeResidentInfo(sender)) {
                       Resident resident = Residents.getResident(args[1]);

                       if (resident != null) {
                           Messaging.sendResidentInfo(sender, resident);
                       } else {
                           sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.NOT_REGISTERED_RESIDENT));
                       }
                   }
                   else
                   {
                       sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED));
                   }


               }
               else
               {
                   sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.WRONG_ARGS));
               }
            } else if (args[0].equalsIgnoreCase("stalin")) {
                if (Permissions.isAdmin(sender, true)) {
                    StalinNPC.create(((Player) sender).getLocation());
                }
            } else if (args[0].equalsIgnoreCase("dhand")) {
                if (AuroraLanguage.getDebugMode()) {
                    if (debugHand.contains(sender.getName())) {
                        debugHand.remove(sender.getName());
                    } else {
                        debugHand.add(sender.getName());
                    }

                }
            }
            else if (args[0].equalsIgnoreCase("givebonus")) {
                if(args.length > 2)
                {
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
