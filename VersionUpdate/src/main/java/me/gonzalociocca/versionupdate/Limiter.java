package me.gonzalociocca.versionupdate;

import org.bukkit.entity.Player;

/**
 * Created by ciocca on 22/03/2016.
 */
public class Limiter {
    Player p;
    Long lastInteract = 0L;
    Long lastItemHeld = 0L;
    Long lastInventoryClick = 0L;
    Long lastDropItem = 0L;
    Long lastUseCommand = 0L;
    Limiter(Player player){
        p = p;
    }
    public boolean canInteract(){
        if(lastInteract > System.currentTimeMillis()){
            return false;
        }else{
            lastInteract = System.currentTimeMillis()+150;
            return true;
        }
    }
    public boolean canItemHeld(){
        if(lastItemHeld > System.currentTimeMillis()){
            return false;
        }else{
            lastItemHeld = System.currentTimeMillis()+100;
            return true;
        }
    }
    public boolean canInventoryClick(){
        if(lastInventoryClick > System.currentTimeMillis()){
            return false;
        }else{
            lastInventoryClick = System.currentTimeMillis()+100;
            return true;
        }
    }
    public boolean canDropItem(){
        if(lastDropItem > System.currentTimeMillis()){
            return false;
        }else{
            lastDropItem = System.currentTimeMillis()+300;
            return true;
        }
    }
    public boolean canUseCommand(){
        if(lastUseCommand > System.currentTimeMillis()){
            return false;
        }else{
            lastUseCommand = System.currentTimeMillis()+1000;
            return true;
        }
    }

}
