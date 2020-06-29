package ru.etysoft.aurorauniverse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.Resident;

public class PluginListener implements org.bukkit.event.Listener {

    @EventHandler
    public void Join(PlayerJoinEvent event)
    {
        if(Permissions.isAdmin(event.getPlayer(), false))
        {
              Messaging.mess("&f>> &bAuroraUniverse &a" + AuroraUniverse.getInstance().getDescription().getVersion() + " &f working!", event.getPlayer());
              if(!AuroraUniverse.getWarnings().equals(""))
              {
                  Messaging.mess("&c>> There is some warnings: &e" + ChatColor.YELLOW + AuroraUniverse.getWarnings(), event.getPlayer());
                  Messaging.mess("&7Please review the issues and make sure they are permanent. &fOnly administrators can see this message.", event.getPlayer());
              }
        }
    }




    @EventHandler
    public void Login(PlayerJoinEvent e)
    {
        Residents.createResident(e.getPlayer());
    }


    @EventHandler
    public void PlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Chunk isNewChunk = player.getLocation().getChunk();
        if(Residents.getResident(player) == null)
        {
            if(AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("kick-noresidents")) {
                player.kickPlayer("Can't find Resident.");
                Logger.warning("Kicked player " + player.getName() + ": can't find Resident with that name.");
            }
        }
        if(player.getLocation().getChunk()!=isNewChunk){
         //   player.sendMessage("Change chunk!" + event.getTo().getChunk());
        }
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {


        } else {


              Towns.ChangeChunk(player, event.getTo().getChunk());

        }
    }

}
