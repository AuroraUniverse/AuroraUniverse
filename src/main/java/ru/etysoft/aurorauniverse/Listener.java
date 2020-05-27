package ru.etysoft.aurorauniverse;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void joininfo(PlayerJoinEvent event)
    {
        if(event.getPlayer().hasPermission("aun.info"))
        {
              event.getPlayer().sendMessage(ChatColor.AQUA + ">> Welcome to the server! AuroraUniverse " + AuroraUniverse.getPlugin(AuroraUniverse.class).getDescription().getVersion() + " working!");
              if(!AuroraUniverse.warnings.equals(""))
              {
                  event.getPlayer().sendMessage(ChatColor.RED + ">> There is some warnings: " + ChatColor.YELLOW + AuroraUniverse.warnings);
                  event.getPlayer().sendMessage(ChatColor.GRAY + "Please review the issues and make sure they are permanent. Only administrators can see this message.");

              }
        }
    }

    @EventHandler
    public void pvp(EntityDamageByEntityEvent event)
    {


        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            Resident resident = TownFun.getResident(p);
            try {
                if (!TownFun.getTown(p.getLocation().getChunk()).getPvP(resident, p.getLocation().getChunk())) {

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
    public void breakb(BlockBreakEvent event)
    {
        //TODO: check town toggles
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (TownFun.getResident(event.getPlayer()).hasTown()) {
                if (TownFun.hasMyTown(event.getBlock().getChunk(), TownFun.getResident(event.getPlayer()).getTown())) {

                } else {
                    if (TownFun.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            } else {
                if (TownFun.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void placeb(BlockPlaceEvent event)
    {
        //TODO: check town toggles
        if(!event.getPlayer().hasPermission("aun.edittowns")) {
            if (TownFun.getResident(event.getPlayer()).hasTown()) {
                if (TownFun.hasMyTown(event.getBlock().getChunk(), TownFun.getResident(event.getPlayer()).getTown())) {

                } else {
                    if (TownFun.hasTown(event.getBlock().getChunk())) {
                        Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                        event.setCancelled(true);
                    }
                }
            } else {
                if (TownFun.hasTown(event.getBlock().getChunk())) {
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }



    @EventHandler
    public void login(PlayerJoinEvent e)
    {
        TownFun.createResident(e.getPlayer());
    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Chunk isNewChunk = player.getLocation().getChunk();

        if(player.getLocation().getChunk()!=isNewChunk){
         //   player.sendMessage("Change chunk!" + event.getTo().getChunk());
        }
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {


        } else {


              TownFun.ChangeChunk(player, event.getTo().getChunk());

        }
    }

}
