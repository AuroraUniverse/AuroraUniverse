package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.chat.AuroraChat;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.Bank;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;

public class Resident {

    private String nickname;
    private boolean lastwild = true;
    private String lasttownname = null;
    private String townname = null;
    private Bank bank;
    private String permissonGroup;
    private Chunk lastChunk;
    private int chatMode;


    public Resident(String name) {
        nickname = name;
        permissonGroup = "newbies";
        chatMode = AuroraChat.Channels.GLOBAL;
        bank = new Bank(nickname, AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getDouble("start-balance"), nickname);
        AuroraUniverse.getInstance().getEconomy().addBank(bank);
    }

    public Chunk getLastChunk() {
        return lastChunk;
    }

    public void setLastChunk(Chunk lastChunk) {
        this.lastChunk = lastChunk;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Types.NAME, nickname);
        jsonObject.put(Types.BALANCE, bank.getBalance());
        jsonObject.put(Types.PERM_GROUP, permissonGroup);
        jsonObject.put(Types.IS_IN_WILD, isLastWild());
        return jsonObject;
    }

    public static Resident fromJSON(JSONObject residentJsonObj) {
        String name = (String) residentJsonObj.get(Types.NAME);
        if (Residents.createResident(name)) {
            Resident resident = Residents.getResident(name);
            if (resident != null) {
                resident.setBalance((double) residentJsonObj.get(Types.BALANCE));
                resident.setLastwild((boolean) residentJsonObj.get(Types.IS_IN_WILD));
                resident.setPermissionGroup((String) residentJsonObj.get(Types.PERM_GROUP));
                return resident;
            }
            Logger.error("Resident is null!");
            return null;
        }
        if(Residents.getResident(name) != null)
        {
            return Residents.getResident(name);
        }
        Logger.debug("Resident " + name + " cannot be created");
        return null;
    }

    public static class Types {
        public static final String NAME = "NAME";
        public static final String BALANCE = "BALANCE";
        public static final String PERM_GROUP = "PERM_GROUP";
        public static final String IS_IN_WILD = "IS_WILD";
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(nickname);
    }

    public void setPermissionGroup(String auroraPermissionGroup) {
        this.permissonGroup = auroraPermissionGroup;
    }

    public String getPermissionGroupName() {
        return permissonGroup;
    }

    public double getBalance() {
        return bank.getBalance();
    }

    public void setBalance(double d) {
        bank.setBalance(d);
    }

    public Bank getBank() {
        return bank;
    }

    public void giveBalance(double d) {
        bank.deposit(d);
    }

    public int getChatMode() {
        return chatMode;
    }

    public void setChatMode(int chatMode) {
        this.chatMode = chatMode;
    }

    public boolean takeBalance(double d) {
        return bank.withdraw(d);
    }

    public void setTown(String town) {
        townname = town;
    }

    public boolean hasTown() {
        if (townname != null) {
            return true;
        } else {
            return false;
        }
    }


    public String getName() {
        return nickname;
    }

    public boolean isLastWild() {
        return lastwild;
    }

    public String getTownName() {
        return townname;
    }

    public void setLastwild(boolean lastwild) {
        this.lastwild = lastwild;
    }

    public Town getTown() throws TownNotFoundedException {
        return Towns.getTown(townname);
    }

    public String getLastTown() {
        return lasttownname;
    }

    public void setLastTown(String name) {
        lasttownname = name;
    }
}
