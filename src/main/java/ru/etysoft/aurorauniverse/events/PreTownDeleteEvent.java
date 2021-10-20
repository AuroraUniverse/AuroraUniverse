package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class PreTownDeleteEvent extends Event implements Cancellable {

    private boolean cancelled = false;
    private Town town;

    public PreTownDeleteEvent(Town town)
    {
        this.town = town;
    }

    public Town getTown()
    {
        return town;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
    cancelled = cancel;
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
