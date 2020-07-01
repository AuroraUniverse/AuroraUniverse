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

public class PluginCommands implements CommandExecutor {

    public void setDebug(CommandSender sender, String[] args)
    {
        if(args.length > 1)
        {
            if(Permissions.isAdmin(sender,true)) {

                if (args[1].equalsIgnoreCase("on")) {
                    // TODO: add in .yml
                    AuroraConfiguration.setDebugMode(true);
                    Messaging.mess("Now debug mode is &aenabled", sender);
                } else if (args[1].equalsIgnoreCase("off")) {
                    AuroraConfiguration.setDebugMode(false);
                    Messaging.mess("Now debug mode is &cdisabled", sender);
                } else {
                    sender.sendMessage(Messages.wrongArgs());
                }
            }
        }
        else
        {
            Messaging.mess(Messages.wrongArgs(), sender);
        }
    }

    public void reload(CommandSender sender, String[] args) {
        if(Permissions.isAdmin(sender, true))
        {
            Logger.debug("Reloading configuration...");
            AuroraUniverse.getInstance().reloadConfig();
            Messaging.mess(Messages.reload(), sender);
            Logger.debug("Reloading permission system...");
            AuroraPermissions.clear();
            AuroraPermissions.initialize();
        }
        else
        {
            Logger.info(sender.getName() + " tried to use RELOAD command. Access denied.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0)
        {
            //Has arguments

            if(args[0].equalsIgnoreCase("reload"))
            {
                reload(sender, args);
            }
            else if(args[0].equalsIgnoreCase("debug"))
            {
                setDebug(sender, args);
            }
        }
        else
        {
            //No arguments message
            Messaging.plinfo(sender, AuroraUniverse.getInstance());
        }
        return true;
    }
}
