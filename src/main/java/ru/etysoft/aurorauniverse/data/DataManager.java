package ru.etysoft.aurorauniverse.data;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;

import java.io.File;

public class DataManager {

    public static void SaveData() {

    }

    public static void saveResidents(String path) {

    }

    public static void saveTowns(String filename) {
        Bukkit.getConsoleSender().sendMessage("Saving data file...");
        File file2 = new File(AuroraUniverse.getInstance().getDataFolder(), filename);

        JSONArray arr = new JSONArray();
        AuroraUniverse.getTownlist().forEach((townname, town) -> {
            JSONObject pl = new JSONObject();

            // Added in 0.1.1.0
            pl.put("town", town);


            arr.add(pl);

        });

        // Bukkit.getConsoleSender().sendMessage("Added " + mbp.name + " to array...");


    }

    private static void saveRegions(String path) {

    }
}
