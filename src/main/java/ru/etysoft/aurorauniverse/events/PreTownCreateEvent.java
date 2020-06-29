package ru.etysoft.aurorauniverse.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ru.etysoft.aurorauniverse.world.Resident;



public class PreTownCreateEvent extends Event implements Cancellable {

    private String townname;
    private Resident mayor;
    private Location homeblock;
    private boolean cancelled = false;

   public PreTownCreateEvent(String name, Resident mayor, Location homeblock)
   {
       townname = name;
       this.mayor = mayor;
       this.homeblock = homeblock;
   }




   public boolean isCancelled()
   {
       return cancelled;
   }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public Location getHomeBlockLocation()
    {
        return homeblock;
    }

   public Resident getMayor()
   {
       return mayor;
   }

    public String getName()
    {
        return townname;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
