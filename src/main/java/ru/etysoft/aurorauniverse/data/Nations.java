package ru.etysoft.aurorauniverse.data;

import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.world.Nation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Nations {

    public static Nation getNation(String name)
    {
        if(name == null) return null;
        return AuroraUniverse.nationList.getOrDefault(name, null);
    }

    public static Collection<Nation> getNations() {
        return AuroraUniverse.nationList.values();
    }


    public static ArrayList<Nation> getNationsFromBiggest() {
        ArrayList<Nation> sorted = new ArrayList<Nation>();
        Collection<Nation> nationList = getNations();
        Nation[] array = new Nation[nationList.size()];
        nationList.toArray(array);
        for (int i = array.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {

                if (array[j].getTowns().size() > array[j + 1].getTowns().size()) {
                    Nation tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }

        Logger.debug("Towns : " + array.length);

        for(Nation nation : array)
        {
            sorted.add(nation);
        }
        Collections.reverse(sorted);
        return sorted;
    }

    public static boolean isNameValid(String name)
    {
        if(name.length() < 3)
        {
            return false;
        }
        int maxLength = AuroraUniverse.getInstance().getConfig().getInt("max-nation-name");
        if(!AuroraUniverse.matchesNameRegex(name)) return false;
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
