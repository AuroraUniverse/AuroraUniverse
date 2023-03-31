package ru.etysoft.aurorauniverse.events;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class SendTownInfoEvent extends Event {

    private CommandSender sender;

    private Town town;

    public SendTownInfoEvent(CommandSender sender, Town town) {
        this.sender = sender;
        this.town = town;
    }

    public CommandSender getSender() {
        return sender;
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
