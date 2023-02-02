package ru.etysoft.aurorauniverse.gui;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.*;
import ru.etysoft.epcore.gui.GUITable;
import ru.etysoft.epcore.gui.Items;
import ru.etysoft.epcore.gui.Slot;
import ru.etysoft.epcore.gui.SlotRunnable;

import java.util.HashMap;

public class GUITownToggles {

    private Town town;
    private Player player;
    private Resident resident;
    private CommandSender sender;


    public GUITownToggles(Resident resident, Player player, CommandSender sender)
    {
        try {
            this.player = player;
            this.resident = resident;
            this.sender = sender;

            town = resident.getTown();


            HashMap<Integer, Slot> matrix = new HashMap<>();



            processToggles(matrix);
//
            try {
                GUITable guiTable = new GUITable(AuroraLanguage.getColorString("gui.toggles"), 1, matrix, AuroraUniverse.getInstance(), Material.GRAY_STAINED_GLASS_PANE, true);

              /*  guiTable.setOnClosed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new GUITown(resident, player, sender);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });*/
                guiTable.open(player);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (TownNotFoundedException ignored){
            Messaging.sendPrefixedMessage(AuroraLanguage.getColorString("town-dont-belong"), sender);
        }
    }

    private void processToggles(HashMap<Integer, Slot> matrix)
    {
        String fireToggle = AuroraLanguage.getColorString("gui.state-off");

        if(town.isTownFire())
        {
            fireToggle = AuroraLanguage.getColorString("gui.state-on");
        }

        Slot fireToggleSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                String suffix = "on";
                if(town.isTownFire())
                {
                    suffix = "off";
                }
                player.performCommand("auntown toggle fire " + suffix);

                new GUITownToggles(resident, player, sender);
            }
        }, Items.createNamedItem(new ItemStack(Material.CAMPFIRE, 1),  AuroraLanguage.getColorString("gui.toggle-fire").replace("%s", fireToggle)
        ));

        matrix.put(1, fireToggleSlot);

        String mobsToggle = AuroraLanguage.getColorString("gui.state-off");

        if(town.isMobs())
        {
            mobsToggle = AuroraLanguage.getColorString("gui.state-on");
        }

        Slot mobsToggleSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                String suffix = "on";
                if(town.isMobs())
                {
                    suffix = "off";
                }
                player.performCommand("auntown toggle mobs " + suffix);

                new GUITownToggles(resident, player, sender);
            }
        }, Items.createNamedItem(new ItemStack(Material.ZOMBIE_HEAD, 1),  AuroraLanguage.getColorString("gui.toggle-mobs").replace("%s", mobsToggle)
        ));

        matrix.put(2, mobsToggleSlot);


        String pvpToggle = AuroraLanguage.getColorString("gui.state-off");

        if(town.isTownPvp())
        {
            pvpToggle = AuroraLanguage.getColorString("gui.state-on");
        }

        Slot pvpToggleSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                String suffix = "on";
                if(town.isTownPvp())
                {
                    suffix = "off";
                }
                player.performCommand("auntown toggle pvp " + suffix);

                new GUITownToggles(resident, player, sender);
            }
        }, Items.createNamedItem(new ItemStack(Material.DIAMOND_SWORD, 1),  AuroraLanguage.getColorString("gui.toggle-pvp").replace("%s", pvpToggle)
        ));

        matrix.put(3, pvpToggleSlot);

        String expToggle = AuroraLanguage.getColorString("gui.state-off");

        if(town.isExplosionEnabled())
        {
            expToggle = AuroraLanguage.getColorString("gui.state-on");
        }

        Slot expToggleSlot = new Slot(new SlotRunnable() {
            @Override
            public void run() {
                String suffix = "on";
                if(town.isExplosionEnabled())
                {
                    suffix = "off";
                }
                player.performCommand("auntown toggle explosions " + suffix);

                new GUITownToggles(resident, player, sender);
            }
        }, Items.createNamedItem(new ItemStack(Material.TNT, 1),  AuroraLanguage.getColorString("gui.toggle-exp").replace("%s", expToggle)
        ));

        matrix.put(4, expToggleSlot);
    }


}
