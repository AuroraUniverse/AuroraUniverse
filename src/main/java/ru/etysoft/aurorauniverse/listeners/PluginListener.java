package ru.etysoft.aurorauniverse.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.permissions.Group;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.ChunkPair;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class PluginListener implements Listener {

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        if (Permissions.isAdmin(event.getPlayer(), false)) {
            Messaging.sendPrefixedMessage("&f>> &bAuroraUniverse &a" + AuroraUniverse.getInstance().getDescription().getVersion() + " &f working!", event.getPlayer());
            if (!AuroraUniverse.getWarnings().equals("")) {
                Messaging.sendPrefixedMessage("&c>> There is some warnings: &e" + ChatColor.YELLOW + AuroraUniverse.getWarnings(), event.getPlayer());
                Messaging.sendPrefixedMessage("&7Please review the issues and make sure they are permanent. &fOnly administrators can see this message.", event.getPlayer());
            }
        }
        Residents.createResident(event.getPlayer());
        Group group = AuroraPermissions.getGroup(Residents.getResident(event.getPlayer()).getPermissionGroupName());
        AuroraPermissions.setPermissions(event.getPlayer().getName(), group);

    }

    @EventHandler
    public void Quit(PlayerQuitEvent event) {
        AuroraPermissions.removePermissions(event.getPlayer());
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Resident resident = Residents.getResident(event.getPlayer());

        try {
            event.setRespawnLocation(resident.getTown().townSpawnPoint);
        } catch (TownNotFoundedException ignored) {

        }


    }

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk isNewChunk = player.getLocation().getChunk();
        if (Residents.getResident(player) == null) {
            if (AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getBoolean("kick-noresidents")) {
                player.kickPlayer("Can't find Resident.");
                Logger.warning("Kicked player " + player.getName() + ": can't find Resident with that name.");
            }
        }
        if (player.getLocation().getChunk() != isNewChunk) {
            //   player.sendMessage("Change chunk!" + event.getTo().getChunk());
        }
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {


        } else {



            ChunkPair chunkPair = ChunkPair.fromChunk(event.getTo().getChunk());
            Town town = Towns.getTown(chunkPair);
            if(town != null)
            {
                if(!town.isPvp(chunkPair) && ProtectionListener.isInBattle(event.getPlayer()))
                {
                    event.setCancelled(true);
                }
            }

            Towns.handleChunkChange(player, ChunkPair.fromChunk(event.getTo().getChunk()));

        }
    }

}
