package ru.etysoft.aurorauniverse.events;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Resident;
import ru.etysoft.aurorauniverse.world.Town;
import ru.etysoft.epcore.gui.Slot;

import java.util.HashMap;

public class GUITownOpenEvent extends Event {

    private final Town town;

    private HashMap<Integer, Slot> matrix;

    private Resident resident;

    private Player player;

    private CommandSender sender;

    public GUITownOpenEvent(Town town, HashMap<Integer, Slot> matrix, Resident resident, Player player, CommandSender sender)
    {
        this.town = town;
        this.resident = resident;
        this.matrix = matrix;
        this.player = player;
        this.sender = sender;
    }

    public Town getTown() {
        return town;
    }

    public HashMap<Integer, Slot> getMatrix() {
        return matrix;
    }

    public CommandSender getSender() {
        return sender;
    }

    public Player getPlayer() {
        return player;
    }

    public Resident getResident() {
        return resident;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
