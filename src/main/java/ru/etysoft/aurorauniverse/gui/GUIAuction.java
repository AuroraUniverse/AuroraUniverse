package ru.etysoft.aurorauniverse.gui;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.auction.AuctionItem;
import ru.etysoft.aurorauniverse.commands.town.TownClaimCommand;
import ru.etysoft.aurorauniverse.commands.town.TownUnclaimCommand;
import ru.etysoft.aurorauniverse.data.Auction;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.*;
import ru.etysoft.aurorauniverse.world.*;
import ru.etysoft.epcore.gui.GUITable;
import ru.etysoft.epcore.gui.Items;
import ru.etysoft.epcore.gui.Slot;
import ru.etysoft.epcore.gui.SlotRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class GUIAuction {
    private Town town;
    private Player player;

    public GUIAuction(Resident resident, Player pl, CommandSender sender, int page) {
        if(Permissions.canUseAuction(sender)) {
            try {
                player = pl;
                town = resident.getTown();


                HashMap<Integer, Slot> matrix = new HashMap<>();

                int startId = ((page - 1) * 45) + 1;
                int maxId = 45 * page;
                int slotNumber = 1;
                ArrayList<AuctionItem> auctionItems = Auction.getListings();
                for (int i = startId - 1; i < auctionItems.size(); i++) {
                    if (slotNumber <= 45) {
                        AuctionItem auctionItem = auctionItems.get(i);

                        String yourItem = "";
                        String townName = "";

                        if (auctionItem.getResident().hasTown()) {
                            townName = auctionItem.getResident().getTown().getName();
                        }

                        if (auctionItem.getResident() == resident) {
                            yourItem = AuroraLanguage.getColorString("auction-gui.item-your-lore");
                        }

                        ItemStack itemStack = Items.createNamedItem(auctionItem.getItemStack(), auctionItem.getItemStack().getItemMeta().getDisplayName(),
                                AuroraLanguage.getColorString("auction-gui.price").replace("%s", String.valueOf(Numbers.round(auctionItem.getPrice()))),
                                AuroraLanguage.getColorString("auction-gui.item").replace("%s", auctionItem.getResident().getName()).replace("%t", townName),
                                AuroraLanguage.getColorString("auction-gui.item-exp").replace("%s", String.valueOf(auctionItem.getExpTime() / 1000 / 60)), yourItem);

                        Slot.SlotListener slotListener = new Slot.SlotListener() {
                            @Override
                            public void onRightClicked(Player player, GUITable guiTable) {

                            }

                            @Override
                            public void onLeftClicked(Player player, GUITable guiTable) {
                                if (!player.getName().equals(auctionItem.getResident().getName())) {
                                    Resident client = Residents.getResident(player.getName());
                                    boolean hasAuction = false;
                                    try {
                                        hasAuction = resident.getTown().hasAuction();
                                    } catch (Exception ignored) {
                                    }

                                    if (!hasAuction) {
                                        int maxItemsAmount = AuroraUniverse.getInstance().getConfig().getInt("max-items-without-struct");
                                        if (auctionItem.getItemStack().getAmount() > maxItemsAmount) {
                                            player.sendMessage(AuroraLanguage.getColorString("auction-buy-need-struct"));
                                            return;
                                        }
                                    }
                                    if (client.getBank().withdraw(auctionItem.getPrice())) {
                                        if (Auction.removeListing(auctionItem)) {
                                            auctionItem.getResident().getBank().deposit(auctionItem.getPrice());
                                            player.sendMessage(AuroraLanguage.getColorString("auction-buy-success").replace("%s",
                                                    auctionItem.getItemStack().getType().toString()).replace("%p",
                                                    String.valueOf(Numbers.round(auctionItem.getPrice()))));
                                            player.getInventory().addItem(Items.createNamedItem(auctionItem.getItemStack(), auctionItem.getItemStack().getItemMeta().getDisplayName()));
                                            new GUIAuction(resident, pl, sender, page);
                                        }
                                    } else {
                                        player.sendMessage(AuroraLanguage.getColorString("auction-buy-no-money"));
                                    }
                                }
                            }

                            @Override
                            public void onShiftClicked(Player player, GUITable guiTable) {

                                boolean hasBypassPerm = false;

                                try {
                                    if (Permissions.canRemoveTownListings(player) && auctionItem.getResident().getTown() == Residents.getResident(player.getName()).getTown()) {
                                        hasBypassPerm = true;
                                    }
                                } catch (Exception ignored) {

                                }
                                if (player.getName().equals(auctionItem.getResident().getName()) | hasBypassPerm) {
                                    if (Auction.removeListing(auctionItem)) {
                                        player.getInventory().addItem(Items.createNamedItem(auctionItem.getItemStack(), auctionItem.getItemStack().getItemMeta().getDisplayName()));
                                        new GUIAuction(resident, pl, sender, page);
                                    }
                                }
                            }
                        };

                        Slot slot = new Slot(slotListener, itemStack);
                        matrix.put(slotNumber, slot);
                        slotNumber++;
                    }
                }

                if ((auctionItems.size() - startId) > 44) {
                    // Has next page

                    ItemStack paper = new ItemStack(Material.PAPER, 1);
                    ItemStack itemStack = Items.createNamedItem(paper, AuroraLanguage.getColorString("auction-gui.next-page"));

                    SlotRunnable slotRunnable = new SlotRunnable() {
                        @Override
                        public void run() {
                            super.run();
                            new GUIAuction(resident, pl, sender, page + 1);
                        }
                    };

                    Slot slot = new Slot(slotRunnable, itemStack);

                    matrix.put(54, slot);

                }

                if (page > 1) {
                    // Has prev page

                    ItemStack paper = new ItemStack(Material.MAP, 1);
                    ItemStack itemStack = Items.createNamedItem(paper, AuroraLanguage.getColorString("auction-gui.prev-page"));

                    SlotRunnable slotRunnable = new SlotRunnable() {
                        @Override
                        public void run() {
                            super.run();
                            new GUIAuction(resident, pl, sender, page - 1);
                        }
                    };

                    Slot slot = new Slot(slotRunnable, itemStack);

                    matrix.put(53, slot);
                }


                try {
                    GUITable guiTable = new GUITable(AuroraLanguage.getColorString("auction-gui.title-gui"), 6, matrix, AuroraUniverse.getInstance(), Material.BROWN_STAINED_GLASS_PANE, true);
                    guiTable.open(pl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (TownNotFoundedException ignored) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
            }
        }
        else
        {
            sender.sendMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED));
        }
    }
}
