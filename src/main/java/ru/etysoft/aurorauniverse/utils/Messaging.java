package ru.etysoft.aurorauniverse.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;
import ru.etysoft.aurorauniverse.world.Nation;
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
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.AQUA + town.getName() + ChatColor.GRAY + "]_o0o");
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-mayor").replace("%s", town.getMayor().getName())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-residents").replace("%s1",
                String.valueOf(town.getMembersCount())).replace("%s2",town.getMembersList())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-chunks").replace("%s", String.valueOf(town.getChunksCount()))
                .replace("%m", String.valueOf(town.getMaxChunks())).replace("%b", String.valueOf(town.getBonusChunks()))).replace("%o", String.valueOf(town.getOutPosts().size()))
        .replace("%p", String.valueOf(AuroraUniverse.getMaxOutposts())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-chunk-price") .replace("%s", String.valueOf(town.getNewChunkPrice()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-outpost-price") .replace("%s", String.valueOf(town.getNewOutpostPrice()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-tax") .replace("%s", String.valueOf(town.getTownTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-res-tax") .replace("%s", String.valueOf(town.getResTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-balance").replace("%s",
                String.valueOf(town.getBank().getBalance()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-build") .replace("%s", String.join(", ", town.getBuildGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-destroy") .replace("%s", String.join(", ", town.getDestroyGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-use") .replace("%s", String.join(", ", town.getUseGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-switch") .replace("%s", String.join(", ", town.getSwitchGroups()))));
    }

    public static void sendNationInfo(CommandSender p, Nation nation)
    {
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.GOLD + nation.getName() + ChatColor.GRAY + "]_o0o");

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.capital") .replace("%s",  nation.getCapital().getName())));

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.members") .replace("%s1", String.join(", ", nation.getTownNames())))
        .replace("%s", String.valueOf(nation.getTownNames().size())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.tax") .replace("%s",  String.valueOf(nation.getTax()))));
    }

    //Simple send color message with prefix
    public static void sendPrefixedMessage(String message, CommandSender sender)
    {
        if(AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("use-prefix"))
        {
            if(sender instanceof Player)
            {
                message = PlaceholderFormatter.process(message, Bukkit.getPlayer(sender.getName()));
            }
            sender.sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " " + message));
        }
        else
        {
            sender.sendMessage(ColorCodes.toColor("&b" + message));
        }

    }

    public static String getStringFromArgs(String[] args, int fromIndex)
    {
        StringBuilder name = new StringBuilder();
        int i = 0;
        for (String arg :
                args) {
            if (i >= fromIndex) {
                if (i != args.length - 1) {
                    name.append(arg).append(" ");
                } else {
                    name.append(arg);
                }
            }
            i++;
        }
        Logger.debug("Constructor new name " + name.toString());
        return name.toString().replace("%", "").replace("&", "");
    }


}
