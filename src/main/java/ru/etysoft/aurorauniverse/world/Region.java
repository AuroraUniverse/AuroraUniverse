package ru.etysoft.aurorauniverse.world;

import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.data.Towns;

import java.io.Serializable;

public class Region {

    private String regionName = "";
    private boolean townOwned = true;
    private Town town;


    public Region(Town town)
    {
        this.town = town;
    }

    public Town getTown()
    {
        return town;
    }

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonKeys.TOWN_NAME, town.getName());
        jsonObject.put(JsonKeys.TOWN_OWNED, townOwned);
        jsonObject.put(JsonKeys.REGION_NAME, regionName);
        return jsonObject;
    }

    public static Region fromJSON(JSONObject jsonObject)
    {
        return new Region(Towns.getTown((String) jsonObject.get(JsonKeys.TOWN_NAME)));
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public boolean isTownOwned() {
        return townOwned;
    }

    private static class JsonKeys
    {
        public static final String TOWN_NAME = "TOWNNAME";
        public static final String TOWN_OWNED = "TOWN_OWNED";
        public static final String REGION_NAME = "REGION_NAME";
    }

}
