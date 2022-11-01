package me.gonzalociocca.minelevel.core;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;
import org.bukkit.Material;

public enum eventType
{

    Mineria("Evento de Mineria",true,"Mineria",1000L * 1200,"&aMina Ores para acumular puntos!"),
    Coliseo("Evento de Coliseo",false,"Coliseo",1000L * 2700,"&aVe al coliseo la batalla esta por comenzar!"),
    Invasion("Evento Invasion de Bestias",false,"InvasionBestias",1000L * 900,"&aBusca a las bestias en los alrededores de survival,pvp,nether,end!"),
    Min30("5minutos para ",true,"Min15",1000L * 300,"");

    private String dtitle;
    private boolean dactive;
    private String dname;
    private long dtime;
    public String dhover;
    //time Long 1s = 1000l
    eventType(String title,boolean active, String name,Long time, String hover) {
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

    public static eventType getbyName(String str){
        for(eventType type : eventType.values()){
            if(type.getName().equalsIgnoreCase(str)){
                return type;
            }
        }
        return null;
    }
}