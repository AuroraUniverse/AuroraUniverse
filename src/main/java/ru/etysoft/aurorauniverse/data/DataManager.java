package ru.etysoft.aurorauniverse.data;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.exceptions.TownException;
import ru.etysoft.aurorauniverse.world.Nation;
import ru.etysoft.aurorauniverse.world.Resident;
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

        JSONArray newbies = new JSONArray();
        for (Resident newbie : Residents.getList()) {
            if(!newbie.hasTown())
            {
                newbies.add(newbie.toJson());
            }

        }

        jsonObject.put("towns", towns);
        jsonObject.put("nations", nations);
        jsonObject.put("newbies", newbies);


        saveStringToFile(jsonObject.toJSONString());
    }


    private static String readFile() throws IOException {
        File file = new File("plugins/AuroraUniverse/data.json");
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static boolean loadData() {
        JSONArray towns = null;
        JSONArray nations = null;
        JSONArray newbies = null;
        try {
            Logger.log("Loading data from JSON...");
            if((new File("plugins/AuroraUniverse/data.json").exists())) {
                JSONParser jsonParser = new JSONParser();
                JSONObject mainJson = (JSONObject) jsonParser.parse(readFile());
                towns = (JSONArray) mainJson.get("towns");
                nations = (JSONArray) mainJson.get("nations");
                newbies = (JSONArray) mainJson.get("newbies");

                for (int i = 0; i < newbies.size(); i++) {
                    JSONObject jsonObject = (JSONObject) newbies.get(i);
                    Resident resident = Resident.fromJSON(jsonObject);
                    if(resident == null)
                    {
                        Logger.error("Cannot load resident (null, " +i + ")");
                    }

                }

                for (int i = 0; i < towns.size(); i++) {
                    JSONObject jsonObject = (JSONObject) towns.get(i);
                    try {

                        Town.loadTownFromJSON(jsonObject);
                    }
                    catch (Exception e) {
                        Logger.error("Error loading town!");
                        if (e instanceof TownException)
                        {
                            Logger.error(((TownException) e).getErrorMessage());
                        }
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < nations.size(); i++) {
                    JSONObject jsonObject = (JSONObject) nations.get(i);
                    try {
                        Nation.loadFromJson(jsonObject);
                    }
                    catch (Exception e)
                    {
                        Logger.error("Cannot load nation " + jsonObject);
                    }
                }

                Logger.log("Loading finished");
            }
            else
            {
                Logger.log("Storage not found. Loading finished");
            }

                return true;


        } catch (IOException | ParseException e) {
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
