package me.gonzalociocca.minelevel.core.misc;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.EventType;

/**
 * Created by noname on 7/3/2017.
 */

public class FactionsEvent{

    EventType currentEvent;
    EventType lastEvent;
    EventType nextGameEvent;
    Long starttime;
    Long timeLimit;
    Main plugin;
    public FactionsEvent(Main plug,EventType dcurrentEvent,EventType dlastEvent, EventType dnextGameEvent){
        plugin = plug;
        currentEvent = dcurrentEvent;
        lastEvent = dlastEvent;
        nextGameEvent = dnextGameEvent;

        starttime = System.currentTimeMillis();
        timeLimit = dcurrentEvent.getTime();
    }

    public EventType getEventType(){
        return currentEvent;
    }
    public EventType getLastEvent(){
        return lastEvent;
    }
    public EventType getNextGameEvent(){
        return nextGameEvent;
    }

    public boolean isFinished(){
        if(starttime + timeLimit < System.currentTimeMillis()){
            return true;
        }
        return false;
    }

    public String getTimeLeft(){
        Long left = (starttime + timeLimit) - System.currentTimeMillis();
        int min = 0;
        int sec = 0;
        if(left > 60000){
            min = (int)(left / 60000L);
        }
        if(left > 1000){
            sec = (int)((left % 60000L)/1000L);
        }
        return min+"m "+sec+"s";
    }

    public void skip(){
        timeLimit = 1250L;
    }

}