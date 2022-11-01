package me.gonzalociocca.minelevel.core.events;

import me.gonzalociocca.minelevel.core.misc.FactionsEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventStartEvent extends Event {
    FactionsEvent var1;

    public EventStartEvent(FactionsEvent event){
        var1 = event;
    }

    public FactionsEvent getLastEvent(){
        return var1;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
