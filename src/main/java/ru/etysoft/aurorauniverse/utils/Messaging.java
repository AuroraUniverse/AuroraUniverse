package ru.etysoft.aurorauniverse.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.world.Town;

public class Messaging {

    //Send information from plugin
    public static void sendPluginInfo(CommandSender p, AuroraUniverse pl)
    {
        p.sendMessage(">>" + ChatColor.AQUA + " AuroraUniverse" + ChatColor.GRAY + " | A good towns game mode plugin");
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("authors-string")  + ChatColor.WHITE + pl.getDescription().getAuthors());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("version-string") + ChatColor.WHITE + pl.getDescription().getVersion());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("apiversion-string") + ChatColor.WHITE + pl.getDescription().getAPIVersion());

    }


    public static void sendTownInfo(CommandSender p, Town town)
    {
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.AQUA + " " + town.getName() + ChatColor.GRAY + "]_o0o");
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-mayor").replace("%s", town.getMayor().getName())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-residents").replace("%s1",
                String.valueOf(town.getMembersCount())).replace("%s2",town.getMembersList())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-chunks") .replace("%s", String.valueOf(town.getChunksCount()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-balance").replace("%s",
                String.valueOf(town.getBank().getBalance()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-build") .replace("%s", String.join(", ", town.getBuildGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-destroy") .replace("%s", String.join(", ", town.getDestroyGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-use") .replace("%s", String.join(", ", town.getUseGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-switch") .replace("%s", String.join(", ", town.getSwitchGroups()))));
    }

    //Simple send color message with prefix
    public static void sendPrefixedMessage(String message, CommandSender sender)
    {
        if(AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("use-prefix"))
        {
            sender.sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " " + message));
        }
        else
        {
            sender.sendMessage(ColorCodes.toColor("&b" + message));
        }

    }


}
