package ru.etysoft.aurorauniverse.data;

import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Town;

import java.util.Collection;

public class Nations {

    public static Nation getNation(String name)
    {
        if(name == null) return null;
        return AuroraUniverse.nationList.getOrDefault(name, null);
    }

    public static Collection<Nation> getNations() {
        return AuroraUniverse.nationList.values();
    }


    public static boolean isNameValid(String name)
    {
        if(name.length() < 3)
        {
            return false;
        }
        int maxLength = AuroraUniverse.getInstance().getConfig().getInt("max-nation-name");
        if(!AuroraUniverse.matchesRegex(name)) return false;
        if(name.length() > maxLength)
        {
            return  false;
        }
        else
        {
            return true;
        }
    }

}
