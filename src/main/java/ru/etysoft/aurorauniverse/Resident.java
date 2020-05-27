package ru.etysoft.aurorauniverse;

public class Resident {

    public String nickname;
    public boolean lastwild = true;
    private String townname = null;
    public String lasttownname = null;

    public Resident(String name)
    {
        nickname = name;
    }

    public void setTown(String town)
    {
        townname = town;
    }

    public boolean hasTown()
    {
        if(townname != null)
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public String getName()
    {
        return nickname;
    }

    public Town getTown()
    {
      return TownFun.getTown(townname);
    }

    public String getLastTown()
    {
        return lasttownname;
    }

    public void setLastTown(String name)
    {
      lasttownname = name;
    }
}
