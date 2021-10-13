package ru.etysoft.aurorauniverse.listeners;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.PluginCommands;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.ResidentRegion;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProtectionListener implements Listener {

    @EventHandler
    public void PvP(EntityDamageByEntityEvent event)
    {


        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            Resident resident = Residents.getResident(p);
            try {
                if (!Towns.getTown(p.getLocation().getChunk()).getPvP(resident, p.getLocation().getChunk())) {

                    event.setCancelled(true);
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-pvp"), p);
                }
            }
            catch (Exception e){
                //can't pass event because null
            }
        }
    }


    @EventHandler
    public void UseEvent(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("aun.edittowns")) {

            if (event.hasBlock()) {
                Chunk chunk = event.getClickedBlock().getChunk();
                Block block = event.getClickedBlock();
                List<Material> materials = new ArrayList<Material>();
                materials.add(Material.CHEST);
                materials.add(Material.CHEST_MINECART);
                materials.add(Material.SHULKER_BOX);
                materials.add(Material.FURNACE);
                materials.add(Material.BREWING_STAND);
                materials.add(Material.ITEM_FRAME);

                // 1.14+
                try {
                    materials.add(Material.BARREL);
                } catch (Exception e) {
                    Logger.warning("Can't find Barrel material! Is the server outdated?");
                }

                if (materials.contains(block.getType())) {
                    if (Residents.getResident(event.getPlayer()).hasTown()) {
                        if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
                            Town town = Towns.getTown(chunk);
                            if (town != null) {
                                if (!town.canUse(Residents.getResident(event.getPlayer()), chunk)) {
                                    Logger.debug("Prevented from use[1] " + event.getPlayer().getName());
                                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                                    event.setCancelled(true);
                                } else {

                                }
                            }
                        } else {
                            if (Towns.hasTown(chunk)) {
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                                event.setCancelled(true);
                                Logger.debug("Prevented from use[2] " + event.getPlayer().getName());
                            }
                        }
                    } else {
                        if (Towns.hasTown(chunk)) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                            event.setCancelled(true);
                            Logger.debug("Prevented from use[3] " + event.getPlayer().getName());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void SwitchEvent(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            if (event.hasBlock()) {
                Chunk chunk = event.getClickedBlock().getChunk();
                Block block = event.getClickedBlock();
                List<Material> materials = new ArrayList<Material>();

                materials.add(Material.DARK_OAK_DOOR);
                materials.add(Material.ACACIA_DOOR);
                materials.add(Material.BIRCH_DOOR);
                materials.add(Material.IRON_DOOR);
                materials.add(Material.JUNGLE_DOOR);
                materials.add(Material.OAK_DOOR);
                materials.add(Material.SPRUCE_DOOR);

                materials.add(Material.BIRCH_BUTTON);
                materials.add(Material.ACACIA_BUTTON);
                materials.add(Material.DARK_OAK_BUTTON);
                materials.add(Material.JUNGLE_BUTTON);
                materials.add(Material.OAK_BUTTON);
                materials.add(Material.SPRUCE_BUTTON);
                materials.add(Material.STONE_BUTTON);

                materials.add(Material.LEVER);

                materials.add(Material.ACACIA_PRESSURE_PLATE);
                materials.add(Material.BIRCH_PRESSURE_PLATE);
                materials.add(Material.JUNGLE_PRESSURE_PLATE);
                materials.add(Material.OAK_PRESSURE_PLATE);
                materials.add(Material.SPRUCE_PRESSURE_PLATE);
                materials.add(Material.STONE_PRESSURE_PLATE);
                materials.add(Material.DARK_OAK_PRESSURE_PLATE);
                materials.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
                materials.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);

                if (materials.contains(block.getType())) {
                    if (Residents.getResident(event.getPlayer()).hasTown()) {
                        if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
                            Town town = Towns.getTown(chunk);
                            if (town != null) {
                                if (!town.canSwitch(Residents.getResident(event.getPlayer()), chunk)) {
                                    Logger.debug("Prevented from switch[1] " + event.getPlayer().getName());
                                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                                    event.setCancelled(true);
                                } else {

                                }
                            }
                        } else {
                            if (Towns.hasTown(chunk)) {
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                                event.setCancelled(true);
                                Logger.debug("Prevented from switch[2] " + event.getPlayer().getName());
                            }
                        }
                    } else {
                        if (Towns.hasTown(chunk)) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                            event.setCancelled(true);
                            Logger.debug("Prevented from switch[3] " + event.getPlayer().getName());
                        }
                    }
                }
            }
        }
    }



    @EventHandler
    public void BreakBlock(BlockBreakEvent event)
    {
        if(PluginCommands.debugHand.contains(event.getPlayer().getName()))
        {
            String infoMessage = "";
            Town town = Towns.getTown(event.getBlock().getChunk());
            if(town != null)
            {
                Region region = town.getTownChunks().get(event.getBlock().getChunk());
                infoMessage += "Name: " + region.getRegionName() + "\n" +
                        "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ")";

                if(region instanceof ResidentRegion)
                {
                    infoMessage = "[!] RESIDENT REGION \nName: " + region.getRegionName() + "\n" +
                            "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ") + \n" + ((ResidentRegion) region).getOwner().getName() ;
                }
            }
            else
            {
                infoMessage = "Unowned";
            }
            event.getPlayer().sendMessage(infoMessage);
            event.setCancelled(true);
        }
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (Residents.getResident(event.getPlayer()).hasTown()) {
                if (Towns.hasMyTown(event.getBlock().getChunk(), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlock().getChunk());
                    if (town != null) {
                        if (!town.canDestroy(Residents.getResident(event.getPlayer()), event.getBlock().getChunk())) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                            Logger.debug("Prevented from block break[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block break[2] " + event.getPlayer().getName());
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block break[3] " + event.getPlayer().getName());
                }
            }
        }
    }


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        ArrayList<BlockIgniteEvent.IgniteCause> causes = new ArrayList<>(Arrays.asList(BlockIgniteEvent.IgniteCause.values()));
        if (causes.contains(event.getCause())) {

            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                if (!town.isFireAllowed(event.getBlock().getChunk())) {
                    event.setCancelled(true);
                    Logger.debug("Prevented fire spread on X:" + event.getBlock().getLocation().getBlockX() + " Y:" + event.getBlock().getLocation().getBlockY() + " Z:" + event.getBlock().getLocation().getBlockZ() + ". From " + event.getCause() + " on " + town.getName());
                }
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Material id = event.getBlock().getType();
        Town town = Towns.getTown(event.getBlock().getChunk());
        Town toTown = Towns.getTown(event.getToBlock().getChunk());
        if (town == null | toTown != town) {

        if((id == Material.LAVA | id == Material.WATER) &&  (toTown != null)) {
            event.setCancelled(true);
        }

        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event)
    {
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (Residents.getResident(event.getPlayer()).hasTown()) {
                if (Towns.hasMyTown(event.getBlock().getChunk(), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlock().getChunk());
                    if (town != null) {
                        if (!town.canBuild(Residents.getResident(event.getPlayer()), event.getBlock().getChunk())) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                            Logger.debug("Prevented from block place[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block place[2] " + event.getPlayer().getName());
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block place[3] " + event.getPlayer().getName());
                }
            }
        }
    }

}
