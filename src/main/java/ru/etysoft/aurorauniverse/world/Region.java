package ru.etysoft.aurorauniverse.world;

import ru.etysoft.aurorauniverse.data.Towns;

public class Region {

    public String regonname = "";
    public boolean undertown = true;
    public String townname = "AuroraTown";

    public boolean canbuild = false;

    public Region(String nameownertown)
    {
        townname = nameownertown;
    }

    public Town getTown()
    {
        return Towns.getTown(townname);
    }

    public String getTownName()
    {
        return townname;
    }



}
