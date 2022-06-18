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

import java.util.ArrayList;
import java.util.List;

public class Resident {

    private String nickname;
    private boolean lastwild = true;
    private String lasttownname = null;
    private String townname = null;
    private Bank bank;
    private String permissonGroup;
    private ChunkPair lastChunk;
    private ArrayList<Integer> ignoreChannels = new ArrayList<>();
    private int chatMode;
    private List<String> repPlus = new ArrayList<>();
    private List<String> repMinus = new ArrayList<>();
    private long millisPlayed = 0;


    public Resident(String name) {
        nickname = name;
        permissonGroup = "newbies";
        chatMode = AuroraChat.Channels.GLOBAL;
        bank = new Bank(nickname, AuroraUniverse.getPlugin(AuroraUniverse.class).getConfig().getDouble("start-balance"), nickname);
        AuroraUniverse.getInstance().getEconomy().addBank(bank);
    }


    public List<String> getRepMinus() {
        return repMinus;
    }

    public List<String> getRepPlus() {
        return repPlus;
    }

    public void addRepPlus(String nickname) {
        if(repPlus.contains(nickname)) return;

        if(repMinus.contains(nickname))
        {
            repPlus.remove(nickname);
        }

        repPlus.add(nickname);
    }

    public void addRepMinus(String nickname) {
        if(repPlus.contains(nickname)) return;

        if(repPlus.contains(nickname))
        {
            repMinus.remove(nickname);
        }

        repMinus.add(nickname);
    }

    public void setMillisPlayed(long millisPlayed) {
        this.millisPlayed = millisPlayed;
    }

    public long getMillisPlayed() {
        return millisPlayed;
    }

    public int getRep() {
        return repPlus.size() - repMinus.size();
    }

    public boolean toggleIgnore(int chatMode)
    {
        if(!ignoreChannels.contains(chatMode))
        {
            ignoreChannels.add(chatMode);
            return true;
        }
        else
        {
            ignoreChannels.remove(chatMode);
            return false;
        }
    }

    public ArrayList<Integer> getIgnoreChannels() {
        return ignoreChannels;
    }

    public ChunkPair getLastChunk() {
        return lastChunk;
    }

    public void setLastChunk(ChunkPair lastChunk) {
        this.lastChunk = lastChunk;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Types.NAME, nickname);
        jsonObject.put(Types.BALANCE, bank.getBalance());
        jsonObject.put(Types.PERM_GROUP, permissonGroup);
        jsonObject.put(Types.IS_IN_WILD, isLastWild());
        jsonObject.put(Types.REPUTATION_PLUS,repPlus);
        jsonObject.put(Types.REPUTATION_MINUS,repMinus);
        jsonObject.put(Types.TIME_PLAYED, getMillisPlayed());
        return jsonObject;
    }

    public int getHoursPlayed()
    {
        return Math.toIntExact(millisPlayed / 1000L / 60L / 60L);
    }

    public static Resident fromJSON(JSONObject residentJsonObj) {
        String name = (String) residentJsonObj.get(Types.NAME);
        if (Residents.createResident(name)) {
            Resident resident = Residents.getResident(name);
            if (resident != null) {
                resident.setBalance((double) residentJsonObj.get(Types.BALANCE));
                resident.setLastwild((boolean) residentJsonObj.get(Types.IS_IN_WILD));
                resident.setPermissionGroup((String) residentJsonObj.get(Types.PERM_GROUP));
                if(residentJsonObj.containsKey(Types.REPUTATION_PLUS))
                {
                   List<String> reputationPlus = (List<String>) residentJsonObj.get(Types.REPUTATION_PLUS);
                   resident.repPlus = reputationPlus;
                }

                if(residentJsonObj.containsKey(Types.REPUTATION_MINUS))
                {
                    List<String> reputationMinus = (List<String>) residentJsonObj.get(Types.REPUTATION_MINUS);
                    resident.repMinus = reputationMinus;
                }

                if(residentJsonObj.containsKey(Types.TIME_PLAYED))
                {
                    resident.setMillisPlayed((long) residentJsonObj.get(Types.TIME_PLAYED));
                }
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
        public static final String REPUTATION_PLUS = "REPUTATION_PLUS";
        public static final String REPUTATION_MINUS = "REPUTATION_MINUS";
        public static final String TIME_PLAYED = "TIME_PLAYED";
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
