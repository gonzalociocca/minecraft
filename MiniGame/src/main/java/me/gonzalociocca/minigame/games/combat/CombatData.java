package me.gonzalociocca.minigame.games.combat;

import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noname on 4/4/2017.
 */
public class CombatData {

    HashMap<PlayerData, CombatInfo> Received = new HashMap();
    HashMap<PlayerData, CombatInfo> Granted = new HashMap();
    ArrayList<CombatInfo> kills = new ArrayList();
    ArrayList<CombatInfo> deaths = new ArrayList();
    public CombatData(){
    }

    public ArrayList<CombatInfo> getKills(){
        return kills;
    }
    public ArrayList<CombatInfo> getDeaths(){
        return deaths;
    }
    public HashMap<PlayerData, CombatInfo> getDamageReceivedMap(){
        return Received;
    }
    public HashMap<PlayerData, CombatInfo> getDamageGrantedMap(){
        return Granted;
    }

    public void addKill(PlayerData killed){
        CombatInfo info = Granted.get(killed);
        if(info != null) {
            kills.add(info);
        }
    }
    public void addDeath(PlayerData killer){
        if(killer!=null) {
            CombatInfo info = Received.get(killer);
            if (info != null) {
                deaths.add(info);
            }
        }
    }

    public void addDamageDone(PlayerData pd, double damage, EntityDamageEvent.DamageCause cause){
        CombatInfo info = Granted.get(pd);
        if(info==null){
            info = new CombatInfo();
            Granted.put(pd, info);
        }
        info.setPlayer(pd);
        info.addDamage(damage);
        info.addDamageCause(cause);
    }
    public void addDamageReceived(PlayerData pd, double damage, EntityDamageEvent.DamageCause cause){
        CombatInfo info = Received.get(pd);
        if(info==null){
            info = new CombatInfo();
            Received.put(pd, info);
        }
        info.setPlayer(pd);
        info.addDamage(damage);
        info.addDamageCause(cause);
    }
}
