package ru.etysoft.aurorauniverse.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;

import java.util.ArrayList;
import java.util.List;

public class PluginCommands implements CommandExecutor {

    public static List<String> debugHand = new ArrayList<>();

    public void setDebug(CommandSender sender, String[] args) {
        if (args.length > 1) {
            if (Permissions.isAdmin(sender, true)) {

                if (args[1].equalsIgnoreCase("on")) {
                    // TODO: add in .yml
                    AuroraConfiguration.setDebugMode(true);
                    Messaging.sendPrefixedMessage("Now debug mode is &aenabled", sender);
                } else if (args[1].equalsIgnoreCase("off")) {
                    AuroraConfiguration.setDebugMode(false);
                    Messaging.sendPrefixedMessage("Now debug mode is &cdisabled", sender);
                } else {
                    sender.sendMessage(Messages.wrongArgs());
                }
            }
        } else {
            Messaging.sendPrefixedMessage(Messages.wrongArgs(), sender);
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
            } else if (args[0].equalsIgnoreCase("debug")) {
                setDebug(sender, args);
            } else if (args[0].equalsIgnoreCase("dhand")) {
                if (AuroraConfiguration.getDebugMode()) {
                    if (debugHand.contains(sender.getName())) {
                        debugHand.remove(sender.getName());
                    } else {
                        debugHand.add(sender.getName());
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
