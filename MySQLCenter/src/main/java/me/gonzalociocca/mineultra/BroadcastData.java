package me.gonzalociocca.mineultra;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastData
{
    String amsg;
    Long astart;
    Long afinish;
    Long adelay;

    BroadcastData(String msg, Long start, Long finish, Long delay) {
        amsg = Colorizer.Color(msg);
        astart = start;
        afinish = finish;
        adelay = delay;
    }

    public String getMessage(){
        return amsg;
    }
    public boolean isEnabled(){
        if(afinish > System.currentTimeMillis()){
            return true;
        }else{
            return false;
        }
    }

    Long nextmessage = 0L;
    public boolean canSend(){
        if(nextmessage < System.currentTimeMillis()){
            nextmessage = System.currentTimeMillis()+adelay;
            return true;
        }else{
            return false;
        }
    }

    public void sendToAll(MineUltra pl){
        if(isEnabled() && canSend()){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!pl.getPlayerData(p.getName()).getRank().isAtLeast(Rank.VIP)){
            p.sendMessage(amsg);}
        }}
    }

}