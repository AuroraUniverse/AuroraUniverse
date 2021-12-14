package ru.etysoft.aurorauniverse.commands.town;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.auction.AuctionItem;
import ru.etysoft.aurorauniverse.data.Auction;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Numbers;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class TownSellCommand extends CommandPattern {
    public TownSellCommand(CommandSender sender, Resident resident, String[] args) {
        super(sender, resident, args);
        if (resident.hasTown()) {
            try {
                Town t = resident.getTown();
                if (t.hasAuction()) {
                    int maxListPerTown = AuroraUniverse.getInstance().getConfig().getInt("max-listings-per-town");
                    if(Auction.getListingsCountByTown(t) < maxListPerTown) {
                        if (args.length > 1) {
                            try {
                                double price = Double.parseDouble(args[1]);
                                if (price > 0) {
                                    Player player = (Player) sender;
                                    ItemStack toSell = player.getInventory().getItemInMainHand();
                                    if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                        Auction.addListing(new AuctionItem(toSell, resident.getName(), price));
                                        sender.sendMessage(AuroraLanguage.getColorString("auction-sell-success")
                                                .replace("%s", toSell.getType().toString()).replace("%p", String.valueOf(Numbers.round(price))));
                                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));
                                    } else {
                                        sender.sendMessage(AuroraLanguage.getColorString("auction-sell-nothing"));
                                    }
                                } else {
                                    sender.sendMessage(AuroraLanguage.getColorString("auction-sell-double"));
                                }
                            } catch (Exception e) {
                                sender.sendMessage(AuroraLanguage.getColorString("auction-sell-double"));
                            }

                        }
                    }
                    else
                    {
                        sender.sendMessage(AuroraLanguage.getColorString("auction-sell-town-max")
                        .replace("%s", String.valueOf(maxListPerTown)));
                    }
                } else {
                    sender.sendMessage(AuroraLanguage.getColorString("auction-sell-need-struct"));
                }
            } catch (TownNotFoundedException e) {
                e.printStackTrace();
            }

        } else {
            sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.TOWN_DONT_BELONG));

        }
    }
}
