package ru.etysoft.aurorauniverse.world;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.Bank;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;

import java.util.ArrayList;

public class Nation {
    private String name;
    private double tax;
    private Bank bank;
    private Town capital;
    private ArrayList<Town> towns = new ArrayList<>();
    private ArrayList<Town> invitedTowns = new ArrayList<>();


    public Nation(String name, Town owner)
    {
        this.name = name;
        this.capital = owner;
        this.bank = new Bank("aun.nation." + name, 0, owner.getMayor().getName());
        owner.setNationName(name);
        AuroraUniverse.nationList.put(name, this);
    }

    public ArrayList<Town> getTowns() {
        return towns;
    }

    public ArrayList<Town> getInvitedTowns() {
        return invitedTowns;
    }

    public String getName() {
        return name;
    }

    public double getTax() {
        return tax;
    }

    public ArrayList<String> getTownNames()
    {
        ArrayList stringList = new ArrayList();

        for(Town t : towns)
        {
            stringList.add(t.getName());
        }
        return stringList;
    }

    public Bank getBank() {
        return bank;
    }

    public void setCapital(Town capital) {
        this.capital = capital;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTax(double tax) {
        if(tax < 0) return;
        this.tax = tax;
    }

    public void setTowns(ArrayList<Town> towns) {
        this.towns = towns;
    }

    public boolean hasTown(Town t)
    {
        return towns.contains(t);
    }

    public void removeTown(Town t)
    {
        if(towns.contains(t))
        {
            towns.remove(t);
            t.setNationName(null);
        }
    }

    public void addTown(Town t)
    {
        if(!towns.contains(t))
        {
            towns.add(t);
            t.setNationName(name);
        }
    }

    public Town getCapital() {
        return capital;
    }

    public void delete()
    {
        for(Town town : towns)
        {
            town.setNationName(null);
        }
        capital.setNationName(null);
        AuroraUniverse.nationList.remove(name);
    }

    public static void loadFromJson(JSONObject jsonObject)
    {
        String name = (String) jsonObject.get(Keys.NAME);
        String nameCapital = (String) jsonObject.get(Keys.CAPITAL);

        Nation nation = null;
        try {
            nation = new Nation(name, Towns.getTown(nameCapital));
        } catch (TownNotFoundedException e) {
            Logger.error("Capital of nation " + name + " (" + nameCapital + ") not founded!");
            e.printStackTrace();
            return;
        }

        JSONArray townList = (JSONArray) jsonObject.get(Keys.TOWNS);

        if(townList != null) {
            for (int i = 0; i < townList.size(); i++) {
                String membersName = (String) townList.get(i);
                Town town = null;
                try {
                    town = Towns.getTown(membersName);
                    nation.addTown(town);
                } catch (TownNotFoundedException e) {
                    Logger.error("Town of nation " + name + " (" + membersName + ") not founded!");
                    e.printStackTrace();
                }

            }
        }

        if(jsonObject.containsKey(Keys.BALANCE))
        {
            nation.bank.setBalance((double) jsonObject.get(Keys.BALANCE));
        }
    }

    public JSONObject toJSON()
    {
        JSONObject nationObj = new JSONObject();
        nationObj.put(Keys.NAME, name);
        nationObj.put(Keys.CAPITAL, capital.getName());

        JSONArray townsList = new JSONArray();

        for(Town t : towns)
        {
            townsList.add(t.getName());
        }

        nationObj.put(Keys.TOWNS, townsList);
        nationObj.put(Keys.TAX, tax);
        nationObj.put(Keys.BALANCE, bank.getBalance());

        return nationObj;

    }

    public static class Keys
    {
        private final static String NAME = "NAME";
        private final static String CAPITAL = "CAPITAL";
        private final static String TOWNS = "TOWNS";
        private final static String TAX = "TAX";
        private final static String BALANCE = "BALANCE";
    }

    public void rename(String newName)
    {
        if(!AuroraUniverse.getNationList().containsKey(name)) return;

        AuroraUniverse.nationList.remove(name);
        AuroraUniverse.nationList.put(newName, this);
        name = newName;

        capital.setNationName(newName);

        for (Town town: getTowns())
        {
            town.setNationName(newName);
        }

    }

    public void sendMessage(String message)
    {

        this.getCapital().sendMessage(message);

        for (Town town: this.getTowns())
        {
            town.sendMessage(message);
        }
    }
    public int getOnline() {
        int online = 0;
        for (Town town : getTowns()) {
            for (Resident resident : town.getResidents()) {
                if (resident.getPlayer() != null) {
                    online = online + 1;
                }
            }
        }
        return online;
    }

    public ArrayList getResidentsOnline() {
        ArrayList<String> online = new ArrayList<>();
        for (Town town : getTowns()) {
            for (Resident resident : town.getResidents()) {
                if (resident.getPlayer() != null) {
                    online.add(resident.getName());
                }
            }
        }
        return online;
    }
}
