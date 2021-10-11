package ru.etysoft.aurorauniverse.world;

import org.bukkit.Bukkit;
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
