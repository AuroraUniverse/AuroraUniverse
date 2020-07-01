package ru.etysoft.aurorauniverse.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.ArrayList;
import java.util.Arrays;

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
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-pvp"), p);
                }
            }
            catch (Exception e){
                //can't pass event because null
            }
        }
    }

    @EventHandler
    public void BreakBlock(BlockBreakEvent event)
    {
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (Residents.getResident(event.getPlayer()).hasTown()) {
                if (Towns.hasMyTown(event.getBlock().getChunk(), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlock().getChunk());
                    if (town != null) {
                        if (!town.canDestroy(Residents.getResident(event.getPlayer()))) {
                            Logger.debug("Prevented from block break[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block break[2] " + event.getPlayer().getName());
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
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
        ;
        if (causes.contains(event.getCause())) {

            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                if (!town.isFire(event.getBlock().getChunk())) {
                    event.setCancelled(true);
                    Logger.debug("Prevented fire spread on X:" + event.getBlock().getLocation().getBlockX() + " Y:" + event.getBlock().getLocation().getBlockY() + " Z:" + event.getBlock().getLocation().getBlockZ() + ". From " + event.getCause() + " on " + town.getName());
                }
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
                        if (!town.canBuild(Residents.getResident(event.getPlayer()))) {
                            Logger.debug("Prevented from block place[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block place[2] " + event.getPlayer().getName());
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block place[3] " + event.getPlayer().getName());
                }
            }
        }
    }

}
