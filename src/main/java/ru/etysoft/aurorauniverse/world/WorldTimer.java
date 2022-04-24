package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.DataManager;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.gulag.StalinNPC;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Numbers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;


public class WorldTimer {

    private long lastTimeMillis;
    private long lastTimeSave;

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

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    private void start()
    {
        Bukkit.getServer().getScheduler().runTaskTimer(AuroraUniverse.getInstance(), new Runnable() {
                @Override
                public void run() {


                    StalinNPC.updateTarget();

                    long backupDelay = AuroraUniverse.getInstance().getConfig().getInt("backup-delay") * 60 * 1000;

                    for (World world : (Bukkit.getServer().getWorlds())) {
                        for (Entity e : world.getEntities()) {
                            if (e instanceof Monster) {

                               Region region = AuroraUniverse.getTownBlock(ChunkPair.fromChunk(e.getLocation().getChunk()));
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

                    if(System.currentTimeMillis() - lastTimeSave >= backupDelay)
                    {
                        File file = new File("plugins/AuroraUniverse/backups");
                        File data = new File("plugins/AuroraUniverse/data.json");
                        File dataCopy = new File("plugins/AuroraUniverse/backups/data_" + System.currentTimeMillis() / 1000 + ".json");
                        if(!file.exists()) file.mkdirs();

                        try {
                            Logger.info("Auto-saving and backuping...");
                            copyFile(data, dataCopy);
                            DataManager.saveData();
                            Logger.info("Save and backup successful!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        lastTimeSave = System.currentTimeMillis();


                    }

                    if(System.currentTimeMillis() - lastTimeMillis >= delay)
                    {
                       initNewDay();

                    }
                }
            }, 20L, 100L);
    }

    public void initNewDay()
    {
        Logger.log("New day begins!");
        AuroraChat.sendGlobalMessage(AuroraLanguage.getColorString("world-timer"));
        lastTimeMillis = System.currentTimeMillis();


        try {

            for (String townName : new ArrayList<>(AuroraUniverse.getTownList().keySet())) {
                double townChunkTax = AuroraUniverse.getInstance().getConfig().getDouble("town-chunk-tax");


                Town town = AuroraUniverse.getTownList().get(townName);

                for (Resident resident : town.getResidents()) {
                    if (resident.getBalance() >= town.getResTax()) {
                        resident.setBalance(resident.getBalance() - town.getResTax());
                        town.getBank().deposit(town.getResTax());
                    } else {
                        town.getBank().deposit(resident.getBalance());
                        resident.setBalance(0);
                    }
                }

                double sum = townChunkTax * town.getChunksCount();

                if (town.getBank().getBalance() < sum) {
                    AuroraChat.sendGlobalMessage(AuroraLanguage.getColorString("town-out-money")
                            .replace("%s", town.getName()));
                    town.delete();
                    Logger.log("Town " + townName + " has been deleted due taxes");
                } else {
                    town.getBank().withdraw(sum);
                    Logger.log("Town " + townName + " paid " + sum + " for taxes");
                }


            }
        }
        catch (Exception e)
        {
            Logger.log("Can't pay taxes!");
            e.printStackTrace();
        }

        try {
            for (String nationName : new ArrayList<>(AuroraUniverse.nationList.keySet())) {
                Nation nation = Nations.getNation(nationName);
                if (nation != null) {
                    for (String townName : nation.getTownNames()) {
                        Town town = Towns.getTown(townName);
                        double tax = nation.getTax();
                        if(town.getBank().withdraw(tax))
                        {
                            nation.getBank().deposit(tax);
                        }

                    }

                    if(!nation.getBank().withdraw(nation.getTowns().size() * AuroraUniverse.getInstance().getConfig().getDouble("nation-town-tax")))
                    {
                        nation.delete();
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
