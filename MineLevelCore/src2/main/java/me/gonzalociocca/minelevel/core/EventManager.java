package me.gonzalociocca.minelevel.core;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventManager extends Event {


    eventType currentEvent;
    eventType lastEvent;
    eventType nextGameEvent;
    Long starttime;
    Long timeLimit;
    Main plugin;
    public EventManager(Main plug,eventType dcurrentEvent,eventType dlastEvent, eventType dnextGameEvent){
       plugin = plug;
        currentEvent = dcurrentEvent;
        lastEvent = dlastEvent;
        nextGameEvent = dnextGameEvent;

        starttime = System.currentTimeMillis();
        timeLimit = dcurrentEvent.getTime();
    }

    public eventType getEventType(){
        return currentEvent;
    }
    public eventType getLastEvent(){
        return lastEvent;
    }
    public eventType getNextGameEvent(){
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
        int oleada = 10;

        public boolean canOleada(){
            if(oleada==0){
                return false;
            }
            Long rest = (starttime+timeLimit) - System.currentTimeMillis();
            if(oleada==10 && rest/1000L < 1800){
                oleada-=1;
                return true;
            }
            if(oleada==9 && rest/1000 < 1620){
                oleada-=1;
                return true;
            }
            if(oleada==8 && rest/1000 < 1440){
                oleada-=1;
                return true;
            }
            if(oleada==7 && rest/1000 < 1260){
                oleada-=1;
                return true;
            }
            if(oleada==6 && rest/1000 < 1080){
                oleada-=1;
                return true;
            }
            if(oleada==5 && rest/1000 < 900){
                oleada-=1;
                return true;
            }
            if(oleada==4 && rest/1000 < 720){
                oleada-=1;
                return true;
            }
            if(oleada==3 && rest/1000 < 540){
                oleada-=1;
                return true;
            }
            if(oleada==2 && rest/1000 < 360){
                oleada-=1;
                return true;
            }
            if(oleada==1 && rest/1000 < 180){
                oleada-=1;
                return true;
            }
            return false;
        }

    public void skip(){
        timeLimit = 1250L;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
