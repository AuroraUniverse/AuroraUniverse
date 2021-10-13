package ru.etysoft.aurorauniverse.data;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.events.NewTownEvent;
import ru.etysoft.aurorauniverse.events.PreTownCreateEvent;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class Towns {

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


     public static boolean isTownExists(String s)
     {
         if(AuroraUniverse.townlist.containsKey(s))
         {
             return  true;
         }
         else
         {
             return false;
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





    public static void ChangeChunk(Player player, Chunk ch)
    {
        if(AuroraUniverse.alltownblocks.containsKey((ch)))
        {
            Region rg = AuroraUniverse.alltownblocks.get((ch));

            Resident resident = Residents.getResident(player);
            if(resident != null)
            {
                if(resident.isLastWild())
                {
                    resident.setLastwild(false);
                    resident.setLastTown(rg.getTown().getName());
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTown().getName()), player);
                }
                else if (!rg.getTown().getName().equals(resident.getLastTown()))
                {
                    resident.setLastTown(rg.getTown().getName());
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-welcome").replace("%s", rg.getTown().getName()), player);
                }
            }
            else
            {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        }
        else
        {
            Resident resident = Residents.getResident(player);
            if(resident != null)
            {
                if(!resident.isLastWild())
                {
                    resident.setLastwild(true);
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("world"), player);
                }
            }
            else
            {
                Logger.warning("Can't find Resident with nickname " + player.getName());
            }
        }
    }

    public static boolean createTown(String name, Player mayor) throws TownException
    {
        PreTownCreateEvent preTownCreateEvent = new PreTownCreateEvent(name.toString(), Residents.getResident(mayor), mayor.getLocation());
        Bukkit.getPluginManager().callEvent(preTownCreateEvent);

        if(!preTownCreateEvent.isCancelled()) {
            Town newtown = new Town(name, Residents.getResident(mayor), mayor.getLocation().getChunk());
            newtown.setSpawn(mayor.getLocation());
            Residents.getResident(mayor).setPermissonGroup("mayor");

            AuroraPermissions.setPermissions(mayor, AuroraPermissions.getGroup("mayor"));
            AuroraUniverse.getTownlist().put(newtown.getName(), newtown);
            AuroraUniverse.getTownBlocks().putAll(newtown.getTownChunks());

            NewTownEvent newTownEvent = new NewTownEvent(newtown);
            Bukkit.getPluginManager().callEvent(newTownEvent);
            return true;
        }
        return false;
    }


}
