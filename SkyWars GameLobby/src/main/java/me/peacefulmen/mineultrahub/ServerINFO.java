package me.peacefulmen.mineultrahub;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import me.gonzalociocca.mineultra.serverType;
import org.bukkit.event.Listener;

public class ServerINFO {
    int online = 0;
    int maxonline = 0;
    serverType type;
    int svsfree = 0;

    ServerINFO(int aonline, int amaxonline, serverType atype) {
        this.online = aonline;
        this.maxonline = amaxonline;
        this.type = atype;
    }

    public void addOnline(int aon){
        online+=aon;
    }
    public void addMaxOnline(int aon){
        maxonline+=aon;
    }

    public String getStatus(){
        if(!type.isSingle()){
        if(svsfree > 0){
            return Colorizer.Color("&aDisponible");
        }else{
         return Colorizer.Color("&cServers en progreso");
        }}else{
            if(online >= maxonline){
                return Colorizer.Color("&cServer Lleno");
            }else{
                return Colorizer.Color("&aDisponible");
            }
        }

    }
    public boolean isFull(){
        return online >= maxonline;
    }
    public void addSVFree(){
        svsfree+=1;
    }

    public int getOnline() {
        return this.online;
    }

    public int getMaxOnline() {
        return this.maxonline;
    }

    public serverType getType() {
        return this.type;
    }
}
