package ru.etysoft.aurorauniverse;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class Messaging {

    //Send information from plugin
    public static void plinfo(CommandSender p, AuroraUniverse pl)
    {
        p.sendMessage(">>" + ChatColor.AQUA + " AuroraUniverse" + ChatColor.GRAY + " | A good towns game mode plugin");
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("authors-string")  + ChatColor.WHITE + pl.getDescription().getAuthors());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("version-string") + ChatColor.WHITE + pl.getDescription().getVersion());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("apiversion-string") + ChatColor.WHITE + pl.getDescription().getAPIVersion());

    }


    public static void towninfo(CommandSender p, Town town)
    {
        p.sendMessage(">> " + ChatColor.AQUA + town.getName() + ChatColor.GRAY);
        p.sendMessage(AuroraUniverse.getLanguage().getString("town-mayor") + ChatColor.AQUA + town.getMayor().getName());
        p.sendMessage(AuroraUniverse.getLanguage().getString("town-residents") +town.getMembersCount()  + "): " + ChatColor.AQUA + town.getMembersList());
        p.sendMessage(AuroraUniverse.getLanguage().getString("town-chunks") + ChatColor.AQUA +town.getChunksCount());
        p.sendMessage(AuroraUniverse.getLanguage().getString("town-balance") + ChatColor.AQUA +town.getBank());
    }
    //Simple send color message with prefix
    public static void mess(String message, CommandSender sender)
    {
        if(AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("use-prefix"))
        {
            sender.sendMessage(fun.cstring(AuroraUniverse.prefix + " " + message));
        }
        else
        {
            sender.sendMessage(fun.cstring("&b" + message));
        }

    }


}
