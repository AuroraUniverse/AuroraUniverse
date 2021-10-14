package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
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
        regionObj.put()
    }

    private static class JsonKeys
    {
        public static final String OWNER = "TOWNNAME";
        public static final String MEMBERS = "TOWN_OWNED";
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
