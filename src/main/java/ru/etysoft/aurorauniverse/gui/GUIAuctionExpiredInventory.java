package ru.etysoft.aurorauniverse.gui;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.auction.AuctionExpiredItems;
import ru.etysoft.aurorauniverse.auction.AuctionItem;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.epcore.gui.GUITable;
import ru.etysoft.epcore.gui.Items;
import ru.etysoft.epcore.gui.Slot;
import ru.etysoft.epcore.gui.SlotRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class GUIAuctionExpiredInventory {

    public GUIAuctionExpiredInventory(String[] args, Resident residentSender, Player player, CommandSender sender)
    {
        if (residentSender == null) return;


        String receiverName;

        if (args.length > 1)
        {
            if (!player.hasPermission("town.auction.checkinventories"))
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("access-denied-message"), sender);
                return;
            }

            Resident receiver = Residents.getResident(args[1]);

            if (receiver == null)
            {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("not-registered-resident"), sender);
                return;
            }

            receiverName = args[1];
        }
        else
        {
            receiverName = residentSender.getName();
        }

        processInventory(receiverName, player, sender, 1);
    }

    public static void processInventory(String receiverName, Player player, CommandSender sender, int page)
    {
        HashMap<Integer, Slot> matrix = new HashMap<>();


        AuctionExpiredItems auctionPlayerItems = AuctionExpiredItems.getAuctionExpiredItems(receiverName);


        if (auctionPlayerItems == null)
        {
            auctionPlayerItems = new AuctionExpiredItems(receiverName);
        }

        ArrayList<ItemStack> stacks = auctionPlayerItems.getItemStacks();

        int startID = (page - 1) * 45 + 1;
        int maxID = page*45;
        int slotNumber = 1;

        for (int i = startID - 1; (i < maxID) && i < (stacks.size()); i++)
        {
            try
            {
                ItemStack stack = stacks.get(i);

                int finalI = i;
                Slot.SlotListener slotListener = new Slot.SlotListener() {
                    @Override
                    public void onRightClicked(Player player, GUITable guiTable) {

                    }

                    @Override
                    public void onLeftClicked(Player player, GUITable guiTable) {

                        if (player.getInventory().firstEmpty() != -1)
                        {
                            player.getInventory().addItem(stack);
                            AuctionExpiredItems.getAuctionExpiredItems(receiverName).getItemStacks().remove(finalI);
                            processInventory(receiverName, player, sender, page);
                        }
                        else
                        {
                            player.getWorld().dropItem(player.getLocation(), stack);
                            AuctionExpiredItems.getAuctionExpiredItems(receiverName).getItemStacks().remove(finalI);
                            processInventory(receiverName, player, sender, page);
                        }

                    }

                    @Override
                    public void onShiftClicked(Player player, GUITable guiTable) {

                    }
                };

                Slot slot = new Slot(slotListener, stack);

                matrix.put(slotNumber, slot);

                slotNumber++;

            }
            catch (Exception e)
            {
                Logger.error("Can't load ItemStack in AuctionExpiredInventory to GUI ");
                e.printStackTrace();
            }
        }

        ItemStack emerald = Items.createNamedItem(new ItemStack(Material.EMERALD, 1), AuroraLanguage.getColorString("auction-gui.open-auction"));

        SlotRunnable slotRunnableEmerald = new SlotRunnable() {
            @Override
            public void run() {
                super.run();
                new GUIAuction(Residents.getResident(player.getName()), player, sender, 1);
            }
        };

        Slot slotEmerald = new Slot(slotRunnableEmerald, emerald);

        matrix.put(46, slotEmerald);

        if (stacks.size() - startID > 44)
        {
            ItemStack paper = new ItemStack(Material.PAPER, 1);
            ItemStack itemStack = Items.createNamedItem(paper, AuroraLanguage.getColorString("auction-gui.next-page"));

            SlotRunnable slotRunnable = new SlotRunnable() {
                @Override
                public void run() {
                    super.run();
                    processInventory(receiverName, player, sender, page + 1);
                }
            };

            Slot slot = new Slot(slotRunnable, itemStack);

            matrix.put(54, slot);
        }

        if (page > 1)
        {
            ItemStack paper = new ItemStack(Material.MAP, 1);
            ItemStack itemStack = Items.createNamedItem(paper, AuroraLanguage.getColorString("auction-gui.prev-page"));

            SlotRunnable slotRunnable = new SlotRunnable() {
                @Override
                public void run() {
                    super.run();
                    processInventory(receiverName, player, sender, page - 1);
                }
            };

            Slot slot = new Slot(slotRunnable, itemStack);

            matrix.put(53, slot);
        }

        try
        {
            GUITable guiTable = new GUITable(AuroraLanguage.getColorString("auction-gui.expired-of-player").replace("%s", receiverName), 6,
                    matrix, AuroraUniverse.getInstance(), Material.BROWN_STAINED_GLASS_PANE, true);
            guiTable.open(player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
