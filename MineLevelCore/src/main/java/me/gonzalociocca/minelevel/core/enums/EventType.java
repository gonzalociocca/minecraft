package me.gonzalociocca.minelevel.core.enums;

import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;

public enum EventType
{

    Mineria("Evento de Mineria",true,"Mineria",1000L * 1800,"&aMina Ores para acumular puntos!"),
    Coliseo("Evento de Coliseo",true,"Coliseo",1000L * 2700,"&aVe al coliseo la batalla esta por comenzar!"),
    MasXP("Evento de XP",true,"MasXP",1000L * 1800, "&aMas xp al asesinar monstruos!"),
    Subastas("Evento Subastas", true, "Subastas",1000L * 1200,"&eSubastas Especiales!"),
    Min5("5 minutos para ",true,"Min5",1000L * 300,"");

    private String dtitle;
    private boolean dactive;
    private String dname;
    private long dtime;
    public String dhover;
    //time Long 1s = 1000l
    EventType(String title,boolean active, String name,Long time, String hover) {
        dtitle = title;
        dactive = active;
        dname = name;
        dtime = time;
        dhover = hover;
    }

    public String getTitle(){
        return dtitle;
    }
    public String getHover() { return dhover;}
    public boolean isActive(){
        return dactive;
    }
    public String getName(){
        return dname;
    }
    public Long getTime(){
        return dtime;
    }

    public static EventType getbyName(String str){
        for(EventType type : EventType.values()){
            if(type.getName().equalsIgnoreCase(str)){
                return type;
            }
        }
        return null;
    }
    public static EventType getRandomEvent(){
        int ran = Code.getRandom().nextInt(4);
        EventType var1 = getEvent(ran);

        if(var1.equals(Variable.currentEvent.getEventType())){
            var1 = getEvent(ran+1);
        }
        return var1;
    }
    public static EventType getEvent(int var){
        if(var==0){
            return EventType.Mineria;
        } else if(var==1){
            return EventType.Coliseo;
        } else if(var==2){
            return EventType.MasXP;
        } else if(var==3){
            return EventType.Subastas;
        } else {
            return EventType.Mineria;
        }
    }
}