package me.gonzalociocca.minelevel.core.events;

import me.gonzalociocca.minelevel.core.misc.FactionsEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by noname on 7/3/2017.
 */
public class EventStopEvent extends Event {

    FactionsEvent var1;

    public EventStopEvent(FactionsEvent event){
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
