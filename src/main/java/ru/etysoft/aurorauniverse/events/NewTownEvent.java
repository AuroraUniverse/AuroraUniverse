package ru.etysoft.aurorauniverse.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Region;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;


public class NewTownEvent extends Event {

    private String townname;
    private Town town;

    public NewTownEvent(Town town)
    {
        this.town = town;
    }




    public Town getTown()
    {
        return town;
    }




    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
