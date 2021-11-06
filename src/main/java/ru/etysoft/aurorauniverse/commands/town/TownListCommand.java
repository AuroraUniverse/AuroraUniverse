package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;

public class TownListCommand {

    public TownListCommand(CommandSender sender, String[] args) {
        sender.sendMessage(ColorCodes.toColor(AuroraLanguage.getColorString("town-list")));
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
            }
        }

        int townOnPage = 8;
        int index = 0;
        double d = Double.parseDouble("" + AuroraUniverse.getTownList().size());
        double maxPage = Math.ceil((double) d / townOnPage);
        int fromIndex = (page - 1) * townOnPage;
        int toIndex = fromIndex + townOnPage;


        for (Town town : Towns.getTownsFromBiggest()) {
            if (index >= fromIndex && index < toIndex) {
                try {
                    sender.sendMessage(ChatColor.AQUA + town.getName() + ChatColor.GOLD + "(" + town.getMembersCount() + ", " + town.getMayor().getName() + ")");
                }
                catch (Exception e)
                {
                    sender.sendMessage(town.getName() + " (bad town)");
                }
            }
            index++;
        }

        sender.sendMessage(AuroraLanguage.getColorString("town-pages").replace("%s", String.valueOf(page)).replace("%y", ((int)maxPage) + ""));

    }

}
