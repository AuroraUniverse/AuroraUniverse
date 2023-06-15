package ru.etysoft.aurorauniverse.gulag;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class StalinNPC {

    private static NPC npc;
    private static Location spawnLocation;

    private static boolean isCreated = false;

    public static void create(Location location)
    {
        if(isCreated) return;
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        npc = registry.createNPC(EntityType.PLAYER, "Stalin");

        npc.spawn(location);
        isCreated = true;
        spawnLocation = location;
    }

    public static void remove()
    {
        if(npc != null)
        {
            npc.despawn();
            CitizensAPI.getNPCRegistry().deregister(npc);
            npc.destroy();
            CitizensAPI.getNPCRegistry().saveToStore();
            Logger.info("Deleting Stalin");
        }

    }

    public static void updateTarget()
    {
        if(isCreated)
        {
            try {
                Player target = null;
                double lastClose = -1;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    double currDistance = player.getLocation().distance(npc.getEntity().getLocation());
                    if (lastClose == -1) {
                        lastClose = currDistance;
                        target = player;
                    } else if (lastClose > currDistance) {
                        lastClose = currDistance;
                        target = player;
                    }
                }
                int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
                npc.getNavigator().setTarget(spawnLocation);
                if (target.getLocation().distance(spawnLocation) < 500) {
                    if (randomNum < 40) {

                        npc.getNavigator().setTarget(target, true);
                        npc.getNavigator().getLocalParameters().attackRange(5D).attackDelayTicks(2).updatePathRate(1).baseSpeed(1.5F).attackStrategy();

                    } else if (randomNum < 50) {
                        target.sendMessage("Сталин: только каторга поможет вам поверить в коммунистические идеалы, бездельники!");
                    } else if (randomNum < 80) {
                        target.getWorld().dropItem(npc.getEntity().getLocation(), new ItemStack(Material.ROTTEN_FLESH, 1));
                    } else {
                        npc.getEntity().getWorld().spawnEntity(npc.getEntity().getLocation(), EntityType.COD);
                    }
                }
            }
            catch (Exception e)
            {
                Logger.debug("Stalin stuck!");
                npc.getEntity().teleport(spawnLocation);
                if(AuroraUniverse.debugmode)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
