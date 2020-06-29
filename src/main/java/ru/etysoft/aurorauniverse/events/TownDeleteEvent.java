package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class TownDeleteEvent extends Event {

    private Town town;

    public TownDeleteEvent (Town deletedTown)
    {
        town = deletedTown;
    }

    public Town getDeletedTown()
    {
        return town;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
