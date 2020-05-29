package ru.etysoft.aurorauniverse;



import com.sun.deploy.security.SelectableSecurityManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.exceptions.TownException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Town {

    public String name;
    private Resident mayor;
    private Map<Chunk, Region> townchunks = new ConcurrentHashMap<>();
    private ArrayList<Resident> residents = new ArrayList<>();
    public Chunk homeblock;
    public Location townspawn;
    public float bank;
    // TODO: ranks permissons
    public boolean pvp = false;
    public boolean build = false;
    public boolean destroy = false;
    private Chunk mainchunk = null;
    private double townbank = 0;

    public Town()
    {

    }

    public double getBank()
    {
        return townbank;
    }

   public void depositBank(double d)
    {
        townbank = townbank + d;
    }

    public boolean withdrawBank(double d)
    {
        if(townbank - d >= 0)
        {
            townbank = townbank - d;
            return true;
        }
        else
        {
            return false;
        }

    }


    public Town(String name2, Resident mayor2, Chunk homeblock) throws TownException {
        if(!mayor2.hasTown()) {

              if(!AuroraUniverse.townlist.containsKey(name2))
            {
                if (!name2.equals("") && !name2.contains("\\")) {
                    if (!TownFun.hasTown(homeblock)) {
                        name = name2;
                        mayor = mayor2;
                        addResident(mayor);
                        mayor.setTown(name);

                        townchunks.put(homeblock, new Region(name));
                        mainchunk = homeblock;
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
       if(mayor.nickname.equals(r.nickname))
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

    public boolean isConnected(Chunk chunk)
    {

        AtomicBoolean connected = new AtomicBoolean(false);

        AuroraUniverse.alltownblocks.forEach((chunk1, region) -> {
            int m =  AuroraUniverse.minTownBlockDistanse;

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

                                        connected.set(true);


                                }


                            } else {


                                // TODO: too close to another town
                            }
                        } else {
                            if (!region.getTownName().equals(name)) {

                            }
                            // TODO: too far from town
                        }
                    }
                }



            // chunk 2 is either adjacent to or has the same coordinates as chunk1







        });

        return connected.get();
    }

    public boolean delete()
    {
        for (Resident r:
             residents) {
            r.setTown(null);
            r.lastwild = true;
        }
        AuroraUniverse.townlist.remove(name);
        townchunks.forEach((chunk, region) -> {
            AuroraUniverse.alltownblocks.remove(chunk);
        });

        return true;
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

    public void spawnplayer(Player pl)
    {

        pl.teleport(townspawn);
        TownFun.ChangeChunk(pl, pl.getLocation().getChunk());
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
            //TODO: checl if town is mine
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
