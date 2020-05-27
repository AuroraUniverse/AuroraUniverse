package ru.etysoft.aurorauniverse;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.exceptions.TownException;

public class TownFun {

    public  static Town getTown(String name)
    {
        if(AuroraUniverse.townlist != null) {
            if (AuroraUniverse.townlist.containsKey(name)) {
                return AuroraUniverse.townlist.get(name);
            } else {
                return null;
            }
        }
        else
        {
            Logger.warning("Townlist is null!");
            return  null;
        }
    }





    public static boolean hasMyTown(Chunk chunk, Town town)
    {
        if(AuroraUniverse.alltownblocks.containsKey(chunk))
        {
            if(AuroraUniverse.alltownblocks.get(chunk).getTown() == town)
            {
                return  true;
            }
           else
            {
                return  false;
            }
        }
        else
        {
            return  false;
        }
    }

    public static boolean hasTown(Chunk chunk)
    {
        if(AuroraUniverse.alltownblocks.containsKey(chunk))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public static Town getTown(Chunk chunk)
    {
        if(AuroraUniverse.alltownblocks.containsKey(chunk))
        {
            return AuroraUniverse.alltownblocks.get(chunk).getTown();
        }
        else
        {
            return  null;
        }
    }

    public static boolean hasTown(Location lc)
    {
        if(AuroraUniverse.alltownblocks.containsKey(lc.getChunk()))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public static boolean createResident(Player pl)
    {
        if(AuroraUniverse.residentlist.containsKey(pl.getName()))
        {
            return  false;
        }
        else
        {
            AuroraUniverse.residentlist.put(pl.getName(), new Resident(pl.getName()));
            return  true;
        }
    }

    public static Resident getResident(String s)
    {
        if(AuroraUniverse.residentlist.containsKey(s))
        {
            return  AuroraUniverse.residentlist.get(s);
        }
        else
        {

            return  null;
        }
    }

    public static void ChangeChunk(Player player, Chunk ch)
    {
        if(AuroraUniverse.alltownblocks.containsKey((ch)))
        {
            Region rg = AuroraUniverse.alltownblocks.get((ch));

            Resident resident = TownFun.getResident(player);
            if(resident != null)
            {
                if(resident.lastwild)
                {
                    resident.lastwild = false;
                    resident.setLastTown(rg.townname);
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTownName()), player);
                }
                else if (!rg.getTownName().equals(resident.getLastTown()))
                {
                    resident.setLastTown(rg.townname);
                    Messaging.mess(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTownName()), player);
                }
            }
            else
            {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        }
        else
        {
            Resident resident = TownFun.getResident(player);
            if(resident != null)
            {
                if(!resident.lastwild)
                {
                    resident.lastwild = true;
                    Messaging.mess(AuroraUniverse.getLanguage().getString("world"), player);
                }
            }
            else
            {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        }
    }

    public static Resident getResident(Player pl)
    {
        try {
            if(AuroraUniverse.residentlist.containsKey(pl.getName()))
            {
                return  AuroraUniverse.residentlist.get(pl.getName());
            }
            else
            {

                return  null;
            }
        }
       catch (Exception e) {
           return null;
       }

    }
}
