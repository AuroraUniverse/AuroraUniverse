package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;

public class PlayerEnterTownEvent extends Event {

    private Town town;
    private Resident resident;

    public PlayerEnterTownEvent (Town enteredTown, Resident resident)
    {
        town = enteredTown;
        this.resident = resident;
    }

    public Resident getResident() {
        return resident;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
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

