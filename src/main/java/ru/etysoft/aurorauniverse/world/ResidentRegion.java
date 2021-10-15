package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.utils.Permissions;

import java.util.ArrayList;

public class ResidentRegion extends Region {

    private Resident owner;
    private ArrayList<String> members = new ArrayList<>();

    public ResidentRegion(Town town, Resident owner) {
        super(town);
        this.owner = owner;
        members.add(owner.getName());
    }

    public boolean addMember(Resident resident)
    {
        String nickname = resident.getName();
        if(members.contains(nickname))
        {
            return  false;
        }
        else
        {
            members.add(nickname);
            return true;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject regionObj = super.toJson();
        regionObj.put(JsonKeys.OWNER, owner.getName());
        JSONArray jsonArray = new JSONArray();
        for(String member : members)
        {
            jsonArray.add(member);
        }
        regionObj.put(JsonKeys.MEMBERS, jsonArray);

        return regionObj;
    }

    public static ResidentRegion fromJSON(JSONObject regionObj)
    {
        String ownerName = (String) regionObj.get(JsonKeys.OWNER);
        Resident ownerResident = Residents.getResident(ownerName);

        if(ownerResident != null) {
            ResidentRegion residentRegion = new ResidentRegion(Towns.getTown((String) regionObj.get(Region.JsonKeys.TOWN_NAME)), ownerResident);

            JSONArray membersJsonArray = (JSONArray) regionObj.get(JsonKeys.MEMBERS);

            for (int i = 0; i < membersJsonArray.size(); i++) {
                String memberName = (String) membersJsonArray.get(i);
                Resident member = Residents.getResident(memberName);

                if(member != null)
                {
                    residentRegion.addMember(member);
                }
                else
                {
                    Logger.debug("Member with name " + memberName + " not found!");
                }

            }
            return residentRegion;

        }
        else
        {
            Logger.debug("Owner with name " + ownerName + " not found");
            return null;
        }

    }

    private static class JsonKeys
    {
        public static final String OWNER = "OWNER";
        public static final String MEMBERS = "MEMBERS";

    }

    public boolean removeMember(Resident resident)
    {
        String nickname = resident.getName();
        if(members.contains(nickname))
        {
            members.remove(nickname);
            return true;
        }
        else
        {
            return false;
        }
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public boolean canEdit(Resident resident)
    {
        if(getTown().isResident(resident))
        {
            try {
                if (Permissions.canBypassRegion(Bukkit.getPlayer(resident.getName()))) {
                    return true;
                }
            }
            catch (Exception e)
            {
                return false;
            }
           return members.contains(resident.getName());
        }
        else
        {
            members.remove(resident.getName());
            return false;
        }
    }

    public Resident getOwner() {
        return owner;
    }
}
