package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;

public class TownListCommand {

    public TownListCommand(CommandSender sender, String[] args) {
        sender.sendMessage(ColorCodes.toColor(AuroraConfiguration.getColorString("town-list")));
        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
            } catch (Exception e) {
                Messaging.mess(AuroraConfiguration.getColorString("no-arguments"), sender);
            }
        }
        final int[] i = {1};
        int finalPage = page;
        double d = Double.parseDouble("" + AuroraUniverse.getTownlist().size());
        double maxPage = Math.ceil((double) d / 10f);

        AuroraUniverse.getTownlist().forEach((name, town) -> {

            if (i[0] != (10 * finalPage) + 1) {
                if (i[0] > maxPage - 1) {
                    try {
                        sender.sendMessage(ChatColor.AQUA + name + ChatColor.GOLD + "(" + town.getMembersCount() + ", " + town.getMayor().getName() + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage(name);
                    }


                }
            }
            i[0]++;
        });

        Messaging.mess(AuroraConfiguration.getColorString("town-pages").replace("%s", String.valueOf(page)).replace("%y", AuroraUniverse.getTownlist().size() + ""), sender);

    }

}
