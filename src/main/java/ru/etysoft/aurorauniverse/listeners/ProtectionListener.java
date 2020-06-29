package ru.etysoft.aurorauniverse.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Resident;

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
        //TODO: check town toggles
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (Residents.getResident(event.getPlayer()).hasTown()) {
                if (Towns.hasMyTown(event.getBlock().getChunk(), Residents.getResident(event.getPlayer()).getTown())) {

                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event)
    {
        //TODO: check town toggles
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (Residents.getResident(event.getPlayer()).hasTown()) {
                if (Towns.hasMyTown(event.getBlock().getChunk(), Residents.getResident(event.getPlayer()).getTown())) {

                } else {
                    if (Towns.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            } else {
                if (Towns.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

}
