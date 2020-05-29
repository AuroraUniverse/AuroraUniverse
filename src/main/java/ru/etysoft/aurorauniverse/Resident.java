package ru.etysoft.aurorauniverse;

public class Resident {

    public String nickname;
    public boolean lastwild = true;
    private String townname = null;
    public String lasttownname = null;
    private double balance = AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getDouble("start-balance");

    public Resident(String name)
    {
        nickname = name;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double d)
    {
        balance = d;
    }

    public void giveBalance(double d)
    {
        balance = balance + d;
    }

    public boolean takeBalance(double d)
    {
        if(balance - d >= 0)
        {
            balance = balance - d;
            return  true;
        }
       else
        {
            return false;
        }
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
