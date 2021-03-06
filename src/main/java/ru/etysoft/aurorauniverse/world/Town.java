package ru.etysoft.aurorauniverse.world;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.Bank;
import ru.etysoft.aurorauniverse.events.PreTownDeleteEvent;
import ru.etysoft.aurorauniverse.events.TownDeleteEvent;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.permissions.Group;
import ru.etysoft.aurorauniverse.utils.Messaging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Town {

    public String name;
    public Chunk homeblock;
    public Location townspawn;
    public float bank;
    public boolean pvp = false;

    private boolean fire = false;
    private Resident mayor;
    private Map<Chunk, Region> townchunks = new ConcurrentHashMap<>();
    private ArrayList<Resident> residents = new ArrayList<>();
    private Chunk mainchunk = null;
    private Bank townbank;

    // Permission type -> list of groups with permission
    private Set<String> buildGroups = new HashSet<String>();
    private Set<String> breakGroups = new HashSet<String>();
    private Set<String> useGroups = new HashSet<String>();
    private Set<String> switchGroups = new HashSet<String>();

    @Warning
    public Town(){}

    public void isFire(Boolean enableFireSpreading) {
        fire = enableFireSpreading;
    }

    public boolean hasPlayerRegion(Chunk chunk) {
        return false;
    }

    public boolean isFire(Chunk chunk) {
        return fire;
    }

    public Bank getBank()
    {
        return townbank;
    }

    public void depositBank(double d)
    {
        townbank.deposit(d);
    }

    public boolean withdrawBank(double d)
    {
       return townbank.withdraw(d);
    }


    public Town(String name2, Resident _mayor, Chunk homeblock) throws TownException {
        if(!_mayor.hasTown()) {

              if(!AuroraUniverse.townlist.containsKey(name2))
            {
                if (!name2.equals("") && !name2.contains("\\")) {
                    if (!Towns.hasTown(homeblock)) {
                        name = name2;
                        mayor = _mayor;
                        addResident(mayor);
                        mayor.setTown(name);
                        Group mayorperms = AuroraPermissions.getGroup("mayor");
                        toggleBuild(mayorperms);
                        toggleDestroy(mayorperms);
                        toggleUse(mayorperms);
                        townchunks.put(homeblock, new Region(name));
                        mainchunk = homeblock;
                        townbank = new Bank("aun.town." + name, 0, _mayor.getName());
                        AuroraUniverse.getInstance().getEconomy().addBank(townbank);
                    } else {
                        throw new TownException(AuroraUniverse.getLanguage().getString("e1"));
                    }

                } else {
                    throw new TownException(AuroraUniverse.getLanguage().getString("e2"));
                }
            }
            else
            {
                throw new TownException(AuroraUniverse.getLanguage().getString("e3").replace("%s", name2));
            }
        }
        else
        {
            throw new TownException(AuroraUniverse.getLanguage().getString("e4"));
        }
    }

    public int getChunksCount()
    {
        return townchunks.size();
    }

    public String getMembersList()
    {
        String res = "";
        for (Resident resident : residents)
        {
           if(res.equals(""))
           {
               res = resident.getName();
           }
           else
           {
               res = res + ", " + resident.getName();
           }
        }
        return  res;
    }

    public int getMembersCount()
    {
        return residents.size();
    }

    public Chunk getMainChunk()
    {
        return  mainchunk;
    }

   public boolean getPvP(Resident resident, Chunk ch)
   {
       return pvp;
   }

   public boolean isMayor(Resident r)
   {
       if(mayor.getName().equals(r.getName()))
       {
           return  true;
       }else
       {
           return  false;
       }
   }

    public void setPvP(boolean pvp2)
    {
        pvp = pvp2;
    }

    public void addResident(Resident resident)
    {
        if(!resident.hasTown())
        {
            residents.add(resident);
            resident.setTown(name);
        }

    }

    public String getName()
    {
        return name;
    }


    public void removeResident(Resident resident)
    {
        if(residents.contains(resident))
        {
            if(!isMayor(resident))
            {
                residents.remove(resident);
                resident.setTown(null);
            }

        }

    }

    public boolean toggleBuild(Group group) {
        if (buildGroups.contains(group.getName())) {
            buildGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't build in " + getName());
            return false;
        } else {
            buildGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can build in " + getName());
            return true;
        }
    }

    public boolean toggleDestroy(Group group) {
        if (breakGroups.contains(group.getName())) {
            breakGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't destroy in " + getName());
            return false;
        } else {
            breakGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can destroy in " + getName());
            return true;
        }
    }

    public boolean toggleUse(Group group) {
        if (useGroups.contains(group.getName())) {
            useGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't use in " + getName());
            return false;
        } else {
            useGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can use in " + getName());
            return true;
        }
    }

    public boolean toggleSwitch(Group group) {
        if (switchGroups.contains(group.getName())) {
            switchGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't switch in " + getName());
            return false;
        } else {
            switchGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can switch in " + getName());
            return true;
        }
    }

    public boolean canSwitch(Resident resident, Chunk chunk) {
        return switchGroups.contains(resident.getPermissonGroupName());
    }

    public boolean canUse(Resident resident, Chunk chunk) {
        return useGroups.contains(resident.getPermissonGroupName());
    }

    public boolean canDestroy(Resident resident, Chunk chunk) {
        return breakGroups.contains(resident.getPermissonGroupName());
    }

    public boolean canBuild(Resident resident, Chunk chunk) {
        return buildGroups.contains(resident.getPermissonGroupName());
    }

    public boolean isConnected(Chunk chunk, Player player) {
        AtomicBoolean connected = new AtomicBoolean(false);
        AuroraUniverse.alltownblocks.forEach((chunk1, region) -> {
            int m =  AuroraUniverse.minTownBlockDistanse;
            for (int x = -1; x<2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (chunk1.getX() + x == chunk.getX() && chunk1.getZ() + z == chunk.getZ()) {
                        if (region.getTownName().equals(name)) //если это наш чанк(приват рядом со своим городом
                        {
                            //Проверяем есть ли диагональ
                            if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() + m) //++
                            {
                            } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() + m) // -+
                            {
                            } else if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() - m) //+-
                            {
                            } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() - m) //--
                            {
                            } else {
                                connected.set(true);
                            }
                        } else {
                            Messaging.mess(Messages.claimTooClose(), player);
                        }
                    } else {
                        if (!region.getTownName().equals(name)) {
                            Messaging.mess(Messages.claimTooFar(), player);
                        }

                    }
                }
            }


            // chunk 2 is either adjacent to or has the same coordinates as chunk1


        });

        return connected.get();
    }

    public boolean delete()
    {
        PreTownDeleteEvent preTownDeleteEvent = new PreTownDeleteEvent(this);
        AuroraUniverse.callEvent(preTownDeleteEvent);

        if(!preTownDeleteEvent.isCancelled())
        {
            for (Resident r:
                    residents) {
                r.setTown(null);
                r.setLastwild(true);
            }
            AuroraUniverse.townlist.remove(name);
            townchunks.forEach((chunk, region) -> {
                AuroraUniverse.alltownblocks.remove(chunk);
            });
            TownDeleteEvent townDeleteEvent = new TownDeleteEvent(this);
            AuroraUniverse.callEvent(townDeleteEvent);
            return true;
        }
        else
        {
            return false;
        }



    }


    public boolean claimChunk(Chunk chunk) throws TownException
    {
        AtomicBoolean claimed = new AtomicBoolean(false);
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean far = new AtomicBoolean(false);
       AuroraUniverse.alltownblocks.forEach((chunk1, region) -> {
           int m =  AuroraUniverse.minTownBlockDistanse;
           if(!hasChunk(chunk)) // есть ли чанк, который мы хотим заприватить в городе
           {
               //такого чанка нет


                   //другой чанк города лежит не более чем в 1 блоке рядом
               for (int x = -1; x<2; x++) {
                   for (int z = -1; z < 2; z++) {
                       if (chunk1.getX() + x == chunk.getX() && chunk1.getZ() + z == chunk.getZ()) {
                           if (region.getTownName().equals(name)) //если это наш чанк(приват рядом со своим городом
                           {
                               //Проверяем есть ли диагональ
                               if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() + m) //++
                               {

                                   //Diagonal
                               } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() + m) // -+
                               {

                                   //Diagonal
                               } else if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() - m) //+-
                               {

                                   //Diagonal
                               } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() - m) //--
                               {

                                   //Diagonal
                               } else {
                                   if (!hasChunk(chunk)) {
                                       success.set(true);

                                   } else {

                                   }
                               }


                           } else {

                               claimed.set(true);
                               // TODO: too close to another town
                           }
                       } else {
                           if (!region.getTownName().equals(name)) {
                              // far.set(true);
                           }
                           // TODO: too far from town
                       }
                   }
               }
               }


               // chunk 2 is either adjacent to or has the same coordinates as chunk1








       });
       if(success.get() && !claimed.get() && !far.get())
       {
           townchunks.put(chunk, new Region(name));
           Logger.info("Claimed chunk: " + chunk + " for town " + name);
           AuroraUniverse.alltownblocks.put(chunk, new Region(name));
       }
       else
       {
           success.set(false);
       }
        return success.get();
    }

    public boolean unclaimChunk(Chunk chunk)
    {
        if(townchunks.containsKey(chunk) && mainchunk != chunk)
        {
            townchunks.remove(chunk);
            AuroraUniverse.alltownblocks.remove(chunk);
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public void teleportToTownSpawn(Player pl)
    {
        pl.teleport(townspawn);
        Towns.ChangeChunk(pl, pl.getLocation().getChunk());
        //TODO: teleport message
    }

    public boolean hasChunk(Location lc)
    {
        if(townchunks.containsKey(lc.getChunk()))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public boolean hasChunk(Chunk chunk)
    {
        if(townchunks.containsKey(chunk))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public void setSpawn(Location lc) throws TownException
    {
        if(hasChunk(lc))
        {
            //TODO: check if I have permission
            townspawn = lc;
        }
        else
        {
            throw new TownException(AuroraUniverse.getLanguage().getString("e5"));
        }


    }

    public Map<Chunk, Region> getTownChunks()
    {
        return townchunks;
    }

    public Resident getMayor()
    {
        return  mayor;
    }
}
