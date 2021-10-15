package ru.etysoft.aurorauniverse.data;

import org.bukkit.entity.Player;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.world.Resident;

public class Residents {
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

    public static boolean createResident(String playerName)
    {
        if(AuroraUniverse.residentlist.containsKey(playerName))
        {
            return  false;
        }
        else
        {
            AuroraUniverse.residentlist.put(playerName, new Resident(playerName));
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
