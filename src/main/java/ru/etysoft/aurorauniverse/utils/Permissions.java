package ru.etysoft.aurorauniverse.utils;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.data.Messages;

public class Permissions {

    public static boolean isAdmin(CommandSender sender, boolean sendMessage)
    {
        if(!sender.hasPermission("aun.admin"))
        {
            if(sendMessage)
            {
                sender.sendMessage(Messages.noPermission());
            }
           return false;
        }
        return true;
    }

}
