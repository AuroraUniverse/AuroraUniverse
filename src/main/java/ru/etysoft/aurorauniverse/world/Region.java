package ru.etysoft.aurorauniverse.world;

import org.bukkit.Chunk;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;

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

    public void setTown(Town town) {
        this.town = town;
    }

    public static Region fromJSON(JSONObject jsonObject) throws TownNotFoundedException {
        return new Region(Towns.getTown((String) jsonObject.get(JsonKeys.TOWN_NAME)));
    }

    public static void transfer(Region region, Town toTown) throws TownNotFoundedException {
        String fromTownName = region.getTown().getName();
        Town fromTown = Towns.getTown(fromTownName);

        region.setTown(toTown);
        ChunkPair keyChunk = null;
        for(ChunkPair chunk : fromTown.getTownChunks().keySet())
        {
           Region regionToCompare = fromTown.getTownChunks().get(chunk);
           if(region == regionToCompare)
           {
               Logger.debug("Transferred " + chunk);
               keyChunk = chunk;
           }
        }
        AuroraUniverse.removeTownBlock(keyChunk);
        AuroraUniverse.addTownBlock(keyChunk, region);
        fromTown.getTownChunks().remove(keyChunk);

        toTown.getTownChunks().put(keyChunk, region);

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

    public static class JsonKeys
    {
        public static final String TOWN_NAME = "TOWNNAME";
        public static final String TOWN_OWNED = "TOWN_OWNED";
        public static final String REGION_NAME = "REGION_NAME";
    }

}
