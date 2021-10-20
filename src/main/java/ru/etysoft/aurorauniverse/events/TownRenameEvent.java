package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class TownRenameEvent extends Event {

    private String newName, oldName;

    public TownRenameEvent(String newName, String oldName)
    {
        this.newName = newName;
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public String getOldName() {
        return oldName;
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

