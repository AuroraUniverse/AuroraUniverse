package ru.etysoft.aurorauniverse;

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
        return TownFun.getTown(townname);
    }

    public String getTownName()
    {
        return townname;
    }



}
