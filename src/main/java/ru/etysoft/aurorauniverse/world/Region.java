package ru.etysoft.aurorauniverse.world;

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

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public boolean isTownOwned() {
        return townOwned;
    }

}
