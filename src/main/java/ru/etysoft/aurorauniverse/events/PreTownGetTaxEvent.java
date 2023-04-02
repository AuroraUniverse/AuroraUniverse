package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class PreTownGetTaxEvent extends Event {
    private final Town town;

    public PreTownGetTaxEvent(Town town)
    {
        this.town = town;
    }

    public Town getTown() {
        return town;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
