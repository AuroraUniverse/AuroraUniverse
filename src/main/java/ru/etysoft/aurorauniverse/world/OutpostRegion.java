package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;



public class OutpostRegion extends Region {

    private Location spawnLocation;

    public OutpostRegion(Town town, Location spawn) {
        super(town);
        this.spawnLocation = spawn;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    @Override
    public JSONObject toJson() {
        JSONObject regionObj = super.toJson();
        regionObj.put(Town.JsonKeys.SPAWN_DIR_X, spawnLocation.getDirection().getX());
        regionObj.put(Town.JsonKeys.SPAWN_YAW, spawnLocation.getYaw());
        regionObj.put(Town.JsonKeys.SPAWN_PITCH, spawnLocation.getPitch());
        regionObj.put(Town.JsonKeys.SPAWN_DIR_Y, spawnLocation.getDirection().getY());
        regionObj.put(Town.JsonKeys.SPAWN_DIR_Z, spawnLocation.getDirection().getZ());
        regionObj.put(Town.JsonKeys.SPAWN_WORLD, spawnLocation.getWorld().getName());

        regionObj.put(Town.JsonKeys.SPAWN_X, spawnLocation.getX());
        regionObj.put(Town.JsonKeys.SPAWN_Y, spawnLocation.getY());
        regionObj.put(Town.JsonKeys.SPAWN_Z, spawnLocation.getZ());
        return regionObj;
    }

    public static OutpostRegion fromJSON(JSONObject jsonObject) throws TownNotFoundedException {
        String spawnWorld = (String) jsonObject.get(Town.JsonKeys.SPAWN_WORLD);
        double spawnX = (double) jsonObject.get(Town.JsonKeys.SPAWN_X);
        double spawnY = (double) jsonObject.get(Town.JsonKeys.SPAWN_Y);
        double spawnZ = (double) jsonObject.get(Town.JsonKeys.SPAWN_Z);
        double spawnYaw = (double) jsonObject.get(Town.JsonKeys.SPAWN_YAW);
        double spawnPitch = (double) jsonObject.get(Town.JsonKeys.SPAWN_PITCH);

        Location spawnLocation = new Location(Bukkit.getWorld(spawnWorld), spawnX, spawnY, spawnZ);
        spawnLocation.setYaw(-Float.parseFloat(String.valueOf(spawnYaw)));
        spawnLocation.setPitch(Float.parseFloat(String.valueOf(spawnPitch)));
        return new OutpostRegion(Towns.getTown((String) jsonObject.get(JsonKeys.TOWN_NAME)), spawnLocation);
    }

}
