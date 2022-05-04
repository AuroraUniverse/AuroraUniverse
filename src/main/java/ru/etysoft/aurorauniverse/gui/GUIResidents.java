package ru.etysoft.aurorauniverse.gui;

import javafx.scene.transform.MatrixType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Numbers;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;
import ru.etysoft.epcore.gui.*;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIResidents {


    private Player sender;
    private Town town;
    private GUIPageable guiPageable;
    private GUITable guiTable;

    public GUIResidents(Player sender, Town town, boolean onlyOnline) {
        this.sender = sender;
        this.town = town;



        try {

            guiPageable = new GUIPageable();
            HashMap<Integer, Slot> matrix = new HashMap<>();
            ItemStack itemStack =  Items.createNamedItem(new ItemStack(Material.CLOCK, 1), AuroraLanguage.getColorString("residents-gui.loading-title"),
                    AuroraLanguage.getColorString("residents-gui.loading-lore"));
            matrix.put(1, new Slot(new SlotRunnable(), itemStack));

            guiTable = new GUITable(AuroraLanguage.getColorString("residents-gui.title"), 6, matrix,
                    AuroraUniverse.getInstance(), Material.GRAY_STAINED_GLASS_PANE, true);

            guiTable.open(sender);

            Bukkit.getScheduler().runTaskAsynchronously(AuroraUniverse.getInstance(), new Runnable() {

                @Override
                public void run() {
                    List<Slot> slots = new ArrayList<>();
                    List<Slot> bottomSlots = new ArrayList<>();

                    for (Resident resident : town.getResidents()) {



                        SkullMeta skull = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                        if(Bukkit.getPlayer(resident.getName()) != null)
                        {
                            assert skull != null;
                            skull.setOwningPlayer(Bukkit.getPlayer(resident.getName()));
                        }

                        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);



//                        if(Bukkit.getPlayer(resident.getName()) == null)
//                        {
//                            // TODO: offline players
//                            for(OfflinePlayer player : Bukkit.getOfflinePlayers())
//                            {
//                                if(player.getName() != null)
//                                {
//
//                                    if (player.getName().equals(resident.getName())) {
//                                       // head = Items.getHead(player);
//                                    }
//                                }
//
//
//                            }
//                        }
//                        else
//                        {
//                            head = Items.getHead(Bukkit.getPlayer(resident.getName()));
//                        }




                        head.setItemMeta(skull);

                        ItemStack namedItem = Items.createNamedItem(head,
                                AuroraLanguage.getColorString("residents-gui.resident-name").replace("%s", resident.getName()),
                                AuroraLanguage.getColorString("residents-gui.resident-perm").replace("%s", resident.getPermissionGroupName()),
                                AuroraLanguage.getColorString("residents-gui.resident-bal").replace("%s",
                                        String.valueOf(Numbers.round(resident.getBalance()))));

                        Slot slot = new Slot(new SlotRunnable(), namedItem);
                        if(onlyOnline)
                        {
                            if(Bukkit.getPlayer(resident.getName()) != null)
                            {
                                slots.add(slot);
                            }
                        }
                        else
                        {

                            slots.add(slot);
                        }
                    }

                    Slot next = new Slot(new Slot.SlotListener() {
                        @Override
                        public void onRightClicked(Player player, GUITable guiTable) {

                        }

                        @Override
                        public void onLeftClicked(Player player, GUITable guiTable) {
                            guiPageable.nextPage();
                        }

                        @Override
                        public void onShiftClicked(Player player, GUITable guiTable) {

                        }
                    }, Items.createNamedItem(new ItemStack(Material.MAP, 1), AuroraLanguage.getColorString("residents-gui.next-page")));
                    bottomSlots.add(next);


                    Slot prev = new Slot(new Slot.SlotListener() {
                        @Override
                        public void onRightClicked(Player player, GUITable guiTable) {

                        }

                        @Override
                        public void onLeftClicked(Player player, GUITable guiTable) {
                            guiPageable.prevPage();
                        }

                        @Override
                        public void onShiftClicked(Player player, GUITable guiTable) {

                        }
                    }, Items.createNamedItem(new ItemStack(Material.PAPER, 1), AuroraLanguage.getColorString("residents-gui.prev-page")));
                    bottomSlots.add(prev);


                    String toggleOnline = AuroraLanguage.getColorString("residents-gui.toggle-all");

                    Material toggleOnlineMaterial = Material.YELLOW_CONCRETE;

                    if(!onlyOnline)
                    {
                        toggleOnline = AuroraLanguage.getColorString("residents-gui.toggle-online");
                        toggleOnlineMaterial = Material.GREEN_CONCRETE;
                    }

                    Slot toggleOnlineSlot = new Slot(new Slot.SlotListener() {
                        @Override
                        public void onRightClicked(Player player, GUITable guiTable) {

                        }

                        @Override
                        public void onLeftClicked(Player player, GUITable guiTable) {
                           player.closeInventory();
                           new GUIResidents(sender, town, !onlyOnline);
                        }

                        @Override
                        public void onShiftClicked(Player player, GUITable guiTable) {

                        }
                    }, Items.createNamedItem(new ItemStack(toggleOnlineMaterial, 1), toggleOnline));
                    bottomSlots.add(toggleOnlineSlot);


                    Bukkit.getScheduler().runTask(AuroraUniverse.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            try {
                                guiPageable.initialize(slots, bottomSlots, guiTable, sender);
                                guiPageable.nextPage();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
