package ru.etysoft.aurorauniverse.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.placeholders.AuroraPlaceholdersExpansion;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;
import ru.etysoft.aurorauniverse.world.WorldTimer;

import java.security.Permission;

public class Messaging {

    //Send information from plugin
    public static void sendPluginInfo(CommandSender p, AuroraUniverse pl) {
        p.sendMessage(">>" + ChatColor.AQUA + " AuroraUniverse" + ChatColor.GRAY + " | A good towns game mode plugin");
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("authors-string") + ChatColor.WHITE + pl.getDescription().getAuthors());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("version-string") + ChatColor.WHITE + pl.getDescription().getVersion());
        p.sendMessage(ChatColor.AQUA + AuroraUniverse.getLanguage().getString("apiversion-string") + ChatColor.WHITE + pl.getDescription().getAPIVersion());

    }


    public static void sendTownInfo(CommandSender p, Town town) {
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.AQUA + town.getName() + ChatColor.GRAY + "]_o0o");
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-mayor").replace("%s", town.getMayor().getName())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-residents").replace("%s1",
                String.valueOf(town.getMembersCount())).replace("%s2", town.getMembersList())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-chunks").replace("%s", String.valueOf(town.getChunksCount()))
                .replace("%m", String.valueOf(town.getMaxChunks())).replace("%b", String.valueOf(town.getBonusChunks()))).replace("%o", String.valueOf(town.getOutPosts().size()))
                .replace("%p", String.valueOf(AuroraUniverse.getMaxOutposts())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-chunk-price").replace("%s", String.valueOf(town.getNewChunkPrice()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-outpost-price").replace("%s", String.valueOf(town.getNewOutpostPrice()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-tax").replace("%s", String.valueOf(town.getTownTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-res-tax").replace("%s", String.valueOf(town.getResTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-auction-tax").replace("%s", String.valueOf(town.getAuctionTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-balance").replace("%s",
                String.valueOf(town.getBank().getBalance()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-build").replace("%s", String.join(", ", town.getBuildGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-destroy").replace("%s", String.join(", ", town.getDestroyGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-use").replace("%s", String.join(", ", town.getUseGroups()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("town-switch").replace("%s", String.join(", ", town.getSwitchGroups()))));
    }

    public static void sendNationInfo(CommandSender p, Nation nation) {
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.GOLD + nation.getName() + ChatColor.GRAY + "]_o0o");

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.capital").replace("%s", nation.getCapital().getName())));

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.members").replace("%s1", String.join(", ", nation.getTownNames())))
                .replace("%s", String.valueOf(nation.getTownNames().size())));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.tax").replace("%s", String.valueOf(nation.getTax()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.bank").replace("%s", String.valueOf(nation.getBank().getBalance()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("nation.tax-nation").replace("%s", String.valueOf(nation.getTowns().size() *
                AuroraUniverse.getInstance().getConfig().getDouble("nation-town-tax")))));
    }

    public static void sendResidentInfo(CommandSender p, Resident resident) {
        p.sendMessage(ChatColor.GRAY + "o0o_[" + ChatColor.GOLD + resident.getName() + ChatColor.GRAY + "]_o0o");

        try {
            p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("resident-town").replace("%s", resident.getTown().getName())));
        } catch (TownNotFoundedException e) {
            p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("resident-town").replace("%s",
                    AuroraUniverse.getLanguage().getString("resident-none"))));
        }
        if(Permissions.isAdmin(p, false))
        {
            p.sendMessage("[admin info] PERM GROUP: " + resident.getPermissionGroupName());
        }
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("resident-balance")
                .replace("%s", String.valueOf(resident.getBalance()))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("resident-reputation")
                .replace("%aurorauniverse_rep%", AuroraPlaceholdersExpansion.getReputationPlaceholder(resident))));

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("resident-hours-played")
                .replace("%s", String.valueOf(resident.getHoursPlayed()))));
    }


    public static void sendPrices(CommandSender p) {
        double newTownPrice = AuroraUniverse.getInstance().getConfig().getDouble("price-new-town");
        float defChunkPrice = AuroraUniverse.getInstance().getConfig().getLong("town-chunk-price");
        double modifierChunk = AuroraUniverse.getInstance().getConfig().getDouble("town-new-chunk-tax-mod");

        float defOutpostPrice = AuroraUniverse.getInstance().getConfig().getLong("town-outpost-price");
        double modifierOutpost = AuroraUniverse.getInstance().getConfig().getDouble("town-outpost-mod-price");
        double priceNewNation = AuroraUniverse.getInstance().getConfig().getDouble("price-new-nation");

        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.header")));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.new-town").replace("%s", String.valueOf(newTownPrice))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.chunk-price").replace("%s", String.valueOf(defChunkPrice))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.outpost-price").replace("%s", String.valueOf(defOutpostPrice))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.chunk-mod").replace("%s", String.valueOf(modifierChunk))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.outpost-mod").replace("%s", String.valueOf(modifierOutpost))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.nation-price").replace("%s", String.valueOf(priceNewNation))));
        p.sendMessage(ColorCodes.toColor(AuroraUniverse.getLanguage().getString("prices.new-day").replace("%s", String.valueOf(WorldTimer.getInstance().getRemainingHours()))));

    }


    //Simple send color message with prefix
    public static void sendPrefixedMessage(String message, CommandSender sender) {
        if (AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("use-prefix")) {
            if (sender instanceof Player) {
                message = PlaceholderFormatter.process(message, Bukkit.getPlayer(sender.getName()));
            }
            sender.sendMessage(ColorCodes.toColor(AuroraUniverse.getPrefix() + " " + message));
        } else {
            sender.sendMessage(ColorCodes.toColor("&b" + message));
        }

    }


    public static String getStringFromArgs(String[] args, int fromIndex) {
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
