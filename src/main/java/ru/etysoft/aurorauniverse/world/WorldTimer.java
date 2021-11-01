package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.gulag.StalinNPC;
import ru.etysoft.aurorauniverse.utils.AuroraConfiguration;
import ru.etysoft.aurorauniverse.utils.Numbers;

import java.util.ArrayList;


public class WorldTimer {

    private long lastTimeMillis;

    // 24 hours 86400000
    private long delay = 86400000;

    private static WorldTimer instance;

    private WorldTimer(){
        lastTimeMillis = System.currentTimeMillis();
        start();
    }

    public double getRemainingHours()
    {
        long millis = System.currentTimeMillis() - lastTimeMillis;
        return Numbers.round((delay - millis) / 3600000D);
    }

    private void start()
    {
        Bukkit.getServer().getScheduler().runTaskTimer(AuroraUniverse.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Logger.debug("WorldTimer check...");

                    StalinNPC.updateTarget();



                    for (World world : (Bukkit.getServer().getWorlds())) {
                        for (Entity e : world.getEntities()) {
                            if (e instanceof Monster) {

                               Region region = AuroraUniverse.alltownblocks.get(e.getLocation().getChunk());
                               if(region != null)
                               {
                                   if(!region.getTown().isMobs())
                                   {
                                       Logger.debug("Removing monster at " + e.getLocation());
                                       e.remove();
                                   }

                               }
                            }
                        }
                    }

                    if(System.currentTimeMillis() - lastTimeMillis >= delay)
                    {
                        AuroraChat.sendGlobalMessage(AuroraConfiguration.getColorString("world-timer"));
                        lastTimeMillis = System.currentTimeMillis();



                        for(String townName : AuroraUniverse.getTownList().keySet())
                        {
                            long townChunkTax = AuroraUniverse.getInstance().getConfig().getLong("town-chunk-tax");

                            Town town = AuroraUniverse.getTownList().get(townName);

                            for(Resident resident : town.getResidents())
                            {
                                if(resident.getBalance() >= town.getResTax())
                                {
                                    resident.setBalance(resident.getBalance() - town.getResTax());
                                    town.getBank().deposit(town.getResTax());
                                }
                                else
                                {
                                    town.getBank().deposit(resident.getBalance());
                                    resident.setBalance(0);
                                }
                            }

                            long sum = townChunkTax * town.getChunksCount();

                            if(town.getBank().getBalance() < sum)
                            {
                                AuroraChat.sendGlobalMessage(AuroraConfiguration.getColorString("town-out-money")
                                .replace("%s", town.getName()));
                                town.delete();
                            }
                            else
                            {
                                town.getBank().withdraw(sum);
                            }


                        }

                        try {
                            for (String nationName : AuroraUniverse.nationList.keySet()) {
                                Nation nation = Nations.getNation(nationName);
                                if (nation != null) {
                                    for (String townName : nation.getTownNames()) {
                                        Town town = Towns.getTown(townName);
                                        town.getBank().withdraw(nation.getTax());
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Logger.debug("Error nation taxes!");
                            e.printStackTrace();
                        }

                    }
                }
            }, 20L, 100L);
    }

    public void resetTimer()
    {
        lastTimeMillis = System.currentTimeMillis();
    }

    public static WorldTimer getInstance() {
        if(instance == null)
        {
            instance = new WorldTimer();
        }
        return instance;
    }
}
