package ru.etysoft.aurorauniverse.gui;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.town.TownClaimCommand;
import ru.etysoft.aurorauniverse.commands.town.TownUnclaimCommand;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.events.GUITownOpenEvent;
import ru.etysoft.aurorauniverse.exceptions.AuctionPlaceException;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.exceptions.WorldNotFoundedException;
import ru.etysoft.aurorauniverse.structures.StructureBuildException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.ColorCodes;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.*;
import ru.etysoft.epcore.gui.GUITable;
import ru.etysoft.epcore.gui.Items;
import ru.etysoft.epcore.gui.Slot;
import ru.etysoft.epcore.gui.SlotRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class GUITown {

    private Town town;
    private Player player;

    public GUITown(Resident resident, Player pl, CommandSender sender) {
        try {
            player = pl;
            town = resident.getTown();

            Chunk chunk = pl.getLocation().getChunk();

            HashMap<Integer, Slot> matrix = new HashMap<>();


            GUITownOpenEvent guiTownOpenEvent = new GUITownOpenEvent(town, matrix, resident, pl, sender);
            Bukkit.getPluginManager().callEvent(guiTownOpenEvent);


            String embargos = "";

            for (Town townEmbargo : town.getEmbargoList()) {
                embargos += townEmbargo.getName() + "; ";
            }
            Slot townSlot = new Slot(new SlotRunnable() {
                @Override
                public void run() {

                }
            }, Items.createNamedItem(new ItemStack(Material.COBBLESTONE_WALL, 1), resident.getTown().getName(),
                    ColorCodes.toColor(AuroraUniverse.getLanguage().getString("gui.mayor").replace("%s", town.getMayor().getName())),
                    ColorCodes.toColor(AuroraUniverse.getLanguage().getString("gui.bank").replace("%s", String.valueOf(town.getBank().getBalance()))),
                    ColorCodes.toColor(AuroraUniverse.getLanguage().getString("gui.embargo").replace("%s", embargos)),
                    ColorCodes.toColor(AuroraUniverse.getLanguage().getString("gui.auction-tax").replace("%s", String.valueOf(town.getAuctionTax()))),
                    ColorCodes.toColor(AuroraUniverse.getLanguage().getString("gui.chunks").replace("%s", String.valueOf(town.getChunksCount()))
                                    .replace("%m", String.valueOf(town.getMaxChunks())).replace("%b", String.valueOf(town.getBonusChunks()))).replace("%o", String.valueOf(town.getOutPosts().size()))
                            .replace("%p", String.valueOf(AuroraUniverse.getMaxOutposts()))
            ));


            if (town.getTownChunks().keySet().contains(ChunkPair.fromChunk(chunk))) {
                if (Permissions.canUnClaim(sender)) {
                    Slot unclaimSlot = new Slot(new SlotRunnable() {
                        @Override
                        public void run() {
                            new TownUnclaimCommand(resident, sender, pl);
                            pl.closeInventory();

                        }
                    }, Items.createNamedItem(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1), AuroraLanguage.getColorString("gui.unclaim")
                    ));

                    matrix.put(46, unclaimSlot);
                }
            } else {

                if (Permissions.canClaim(sender)) {
                    double price = town.getNewChunkPrice();
                    if (!town.isConnected(player.getLocation().getChunk(), null)) {
                        price = town.getNewOutpostPrice();
                    }
                    Slot claimSlot = new Slot(new SlotRunnable() {
                        @Override
                        public void run() {
                            new TownClaimCommand(resident, sender, pl);
                            pl.closeInventory();
                        }
                    }, Items.createNamedItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1), AuroraLanguage.getColorString("gui.claim")
                            .replace("%s", String.valueOf(price))
                    ));
                    matrix.put(46, claimSlot);
                }

            }


            Slot standingSlot;

            if (Permissions.canEditTowns(sender) | Permissions.canGetRegionInfo(sender)) {


                Town standingTown = Towns.getTown(((Player) sender).getLocation().getChunk());
                if (standingTown != null) {
                    if (standingTown == resident.getTown() | Permissions.canEditTowns(sender)) {
                        Region region = standingTown.getRegion(((Player) sender).getLocation());

                        if (resident.getTown() == standingTown | Permissions.canEditTowns(sender)) {
                            if (region != null) {
                                if (region instanceof ResidentRegion) {
                                    ResidentRegion residentRegion = (ResidentRegion) region;

                                    String membersString = "";

                                    for (String nickname : residentRegion.getMembers()) {
                                        membersString += nickname + "; ";
                                    }
                                    standingSlot = new Slot(new SlotRunnable() {
                                        @Override
                                        public void run() {

                                        }
                                    }, Items.createNamedItem(new ItemStack(Material.PODZOL, 1), AuroraLanguage.getColorString("gui.standing"),
                                            AuroraLanguage.getColorString("gui.has-owner")
                                                    .replace("%s1", String.valueOf(residentRegion.getMembers().size()))
                                                    .replace("%s", residentRegion.getOwnerName())
                                    ));
                                    matrix.put(54, standingSlot);
                                } else {
                                    String townBlock = AuroraLanguage.getColorString("gui.standing-town");
                                    if (standingTown.getMainChunk().equals(ChunkPair.fromChunk(((Player) sender).getLocation().getChunk()))) {
                                        townBlock = AuroraLanguage.getColorString("gui.standing-main-chunk");
                                    }
                                    standingSlot = new Slot(new SlotRunnable() {
                                        @Override
                                        public void run() {

                                        }
                                    }, Items.createNamedItem(new ItemStack(Material.GRASS_BLOCK, 1), AuroraLanguage.getColorString("gui.standing"),
                                            townBlock
                                    ));

                                    matrix.put(54, standingSlot);


                                }
                            }
                        }
                    }
                }


            }
            processMenu(matrix);
//            processAuction(matrix);
//
//            processToggles(matrix);

            matrix.put(23, townSlot);

            try {
                GUITable guiTable = new GUITable(resident.getTown().getName(), 6, matrix, AuroraUniverse.getInstance(), Material.GRAY_STAINED_GLASS_PANE, true);
                guiTable.open(pl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (TownNotFoundedException ignored) {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

    private void processMenu(HashMap<Integer, Slot> matrix) {
        String transHistory = AuroraLanguage.getColorString("gui.trans-history");
        String transLore = AuroraLanguage.getColorString("gui.trans-history-lore");


        ArrayList<String> lore = new ArrayList<>();
        lore.add(transLore);
        lore.addAll(town.getTransactionHistory());


        Slot transHistorySlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {

            }
        }, Items.createNamedItem(new ItemStack(Material.MAP, 1), transHistory, lore.toArray(new String[lore.size()])
        ));

        matrix.put(41, transHistorySlot);

        String toggles = AuroraLanguage.getColorString("gui.toggles-button");


        Slot togglesSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                new GUITownToggles(Residents.getResident(player.getName()), player, player);
            }
        }, Items.createNamedItem(new ItemStack(Material.ZOMBIE_HEAD, 1), toggles, AuroraLanguage.getColorString("gui.toggles-lore")
        ));

        matrix.put(32, togglesSlot);

        Slot residentsSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                try {
                    new GUIResidents(player, Residents.getResident(player.getName()).getTown(), false);
                } catch (TownNotFoundedException e) {
                    e.printStackTrace();
                }
            }
        }, Items.createNamedItem(new ItemStack(Material.LEATHER_HELMET, 1), AuroraLanguage.getColorString("residents-gui.info"),
                AuroraLanguage.getColorString("residents-gui.lore")
        ));

        matrix.put(31, residentsSlot);

        processAuction(matrix);


    }

    private void processAuction(HashMap<Integer, Slot> matrix) {

        String title = AuroraLanguage.getColorString("auction-gui.title");

        if (town.hasAuction()) {
            title = AuroraLanguage.getColorString("auction-gui.title-bought");
        }


        Slot.SlotListener slotListener = new Slot.SlotListener() {
            @Override
            public void onRightClicked(Player player, GUITable guiTable) {
                if (Permissions.canCreateAuction(player)) {
                    if (town.getAuctionStructure() != null) {
                        try {
                            town.getAuctionStructure().destroy(new Runnable() {
                                @Override
                                public void run() {
                                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-structure-destroy-finished"), player);
                                    town.setAuctionStructure(null);
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {
                                    Logger.error("Error destroying structure!");
                                }
                            }, true);
                        } catch (WorldNotFoundedException | StructureBuildException e) {
                            e.printStackTrace();
                        }

                        town.setAuctionStructure(null);
                        player.closeInventory();
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED), player);
                }
            }

            @Override
            public void onLeftClicked(Player player, GUITable guiTable) {
                if (Permissions.canCreateAuction(player)) {
                    double price = AuroraUniverse.getInstance().getConfig().getDouble("auction-price");

                    if (!town.hasAuction()) {

                        if (!town.getBank().hasAmount(price)) {
                            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("economy.pay.no-money")
                                    .replace("%s", String.valueOf(price)), player);
                            return;
                        }
                    }
                    guiAuctionSelect(price);

                } else {
                    Messaging.sendPrefixedMessage(AuroraLanguage.getColorString(Messages.Keys.ACCESS_DENIED), player);
                }
            }

            @Override
            public void onShiftClicked(Player player, GUITable guiTable) {

            }
        };

        try {
            Slot fireToggleSlot = new Slot(slotListener, Items.createNamedItem(new ItemStack(Material.EMERALD, 1), title,
                    AuroraLanguage.getColorString("auction-gui.lore"), AuroraLanguage.getColorString("auction-gui.lore-2"),
                    AuroraLanguage.getColorString("auction-gui.price").replace("%s",
                            String.valueOf(AuroraUniverse.getInstance().getConfig().getDouble("auction-price")))));

            matrix.put(33, fireToggleSlot);
        } catch (Exception e) {

        }


    }

    private void guiAuctionSelect(double price) {

        HashMap<Integer, Slot> matrix = new HashMap<>();

        for (int i = 1; i < 10; i++) {
            String newPath = "auction-selector.select." + i;

            if (!AuroraLanguage.hasString(newPath)) {
                break;
            }

            ItemStack item = Items.createNamedItem(new ItemStack(
                    Material.getMaterial(AuroraUniverse.getInstance().getConfig().getString("auction-gui-materials." + i)), 1),
                    AuroraLanguage.getColorString(newPath));

            int finalI = i;

            Slot slot = new Slot(new SlotRunnable() {
                @Override
                public void run() {
                    createAuction(price, finalI);
                    player.closeInventory();

                }
            }, item);

            matrix.put(i, slot);
        }

        try {
            GUITable guiTable = new GUITable(AuroraLanguage.getColorString("auction-selector.selector-title"), 1, matrix,
                    AuroraUniverse.getInstance(), Material.AIR, true);
            guiTable.open(player);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(player.getName() + " can't open GUIAuctionSelect");
        }

    }

    private void createAuction(double price, int numberOfSelected) {

        if (town.getAuctionStructure() == null) {
            if (!town.getBank().hasAmount(price)) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-buy-no-money"), player);
                town.setAuctionStructure(null);
                return;
            }

            createAuctionWithWithdrawing(town, price, numberOfSelected, player);
        } else {
            try {
                town.createAuction(player.getLocation(), numberOfSelected, new Runnable() {
                    @Override
                    public void run() {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-replaced"), player);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        town.setAuctionStructure(null);

                        createAuctionWithWithdrawing(town, price, numberOfSelected, player);
                    }
                });
            } catch (WorldNotFoundedException | StructureBuildException e) {
                Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-error-place"), player);
            } catch (AuctionPlaceException e) {
                Messaging.sendPrefixedMessage(e.getMessage(), player);
            }
        }

    }

    public static void createAuctionWithWithdrawing(Town town, double price, int structureId, Player player) {
        try {
            town.createAuction(player.getLocation(), structureId, new Runnable() {
                @Override
                public void run() {
                    if (town.withdrawBank(price)) {
                        Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-created"), player);
                    } else {
                        try {
                            town.getAuctionStructure().destroy(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {
                                    Logger.error("Error destroying structure!");
                                }
                            }, true);
                        } catch (WorldNotFoundedException | StructureBuildException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Runnable() {
                @Override
                public void run() {
                    Logger.error("Error destroying wrong structure!");
                }
            });
        } catch (WorldNotFoundedException | StructureBuildException e) {
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("auction-error-place"), player);
        } catch (AuctionPlaceException e) {
            Messaging.sendPrefixedMessage(e.getMessage(), player);
        }
    }

}
