package ru.etysoft.aurorauniverse.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Town;

public class PreTownGetTaxEvent extends Event {

    private double tax;

    private final Town town;

    public PreTownGetTaxEvent(double tax, Town town)
    {
        this.town = town;
        this.tax = tax;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public Town getTown() {
        return town;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
