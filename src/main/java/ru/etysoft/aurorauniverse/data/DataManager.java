package ru.etysoft.aurorauniverse.data;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Town;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataManager {

    public static void saveData() {
        Logger.info("Saving data...");

        JSONObject jsonObject = new JSONObject();

        JSONArray towns = new JSONArray();
        for (String townName : AuroraUniverse.getTownList().keySet()) {
            Town town = AuroraUniverse.getTownList().get(townName);
            towns.add(town.toJSON());
        }

        JSONArray nations = new JSONArray();
        for (String nationName : AuroraUniverse.nationList.keySet()) {
            Nation nation = AuroraUniverse.nationList.get(nationName);
            nations.add(nation.toJSON());
        }

        jsonObject.put("towns", towns);
        jsonObject.put("nations", nations);


        saveStringToFile(jsonObject.toJSONString());
    }


    private static String readFile() throws IOException {
        File file = new File("plugins/AuroraUniverse/data.json");
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static boolean loadData() {
        JSONArray towns = null;
        JSONArray nations = null;
        try {
            if((new File("plugins/AuroraUniverse/data.json").exists())) {
                JSONParser jsonParser = new JSONParser();
                JSONObject mainJson = (JSONObject) jsonParser.parse(readFile());
                towns = (JSONArray) mainJson.get("towns");
                nations = (JSONArray) mainJson.get("nations");

                for (int i = 0; i < towns.size(); i++) {
                    JSONObject jsonObject = (JSONObject) towns.get(i);
                    Town.loadTownFromJSON(jsonObject);
                }

                for (int i = 0; i < nations.size(); i++) {
                    JSONObject jsonObject = (JSONObject) towns.get(i);
                    Nation.loadFromJson(jsonObject);
                }
            }
                return true;


        } catch (IOException | ParseException | TownException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static void saveStringToFile(String string) {
        try (PrintWriter out = new PrintWriter("plugins/AuroraUniverse/data.json")) {
            out.println(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
