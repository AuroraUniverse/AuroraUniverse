package ru.etysoft.aurorauniverse.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class InTownBlockBrakeEvent extends Event implements Cancellable {

    private Town town;

    private Block block;

    private boolean cancelled = false;

    public InTownBlockBrakeEvent(Town town, Block block) {
        this.town = town;
        this.block = block;
    }

    public Town getTown() {
        return town;
    }

    public Block getBlock() {
        return block;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
