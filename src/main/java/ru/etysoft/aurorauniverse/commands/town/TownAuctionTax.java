package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.command.CommandSender;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownAuctionTax {

    public TownAuctionTax(CommandSender sender, Resident resident, String[] args)
    {
        double tax;

        if (args.length == 3)
        {
            try {
                tax = Double.parseDouble(args[2]);
            } catch (Exception e)
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
                return;
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("no-arguments"), sender);
            return;
        }

        double maxTax = AuroraUniverse.getInstance().getConfig().getDouble("town-auction-tax-max");

        if (tax < maxTax && tax >= 0)
        {
            if (resident != null)
            {
                try {
                    Town town = resident.getTown();
                    if (sender.hasPermission(Permissions.TOWN_AUCTION_EDITTAX))
                    {
                        town.setAuctionTax(tax);
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-tax-change").replace("%s", tax + ""), sender);
                    }
                    else
                    {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                    }
                } catch (TownNotFoundedException e)
                {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
                }
            }
            else
            {
                Messaging.sendPrefixedMessage(Messages.cantConsole(), sender);
            }
        }
        else
        {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-tax-incorrect-value").replace("%s", maxTax + ""), sender);
        }

    }
}
