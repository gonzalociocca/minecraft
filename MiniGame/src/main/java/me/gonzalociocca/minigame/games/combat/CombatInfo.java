package me.gonzalociocca.minigame.games.combat;

import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 4/4/2017.
 */
public class CombatInfo {
    public PlayerData player;
    public double damage = 0;
    public ArrayList<EntityDamageEvent.DamageCause> causes = new ArrayList();
    public long time = -1;
    public long lastHit = -1;

    public CombatInfo() {

    }

    public void setPlayer(PlayerData str) {
        player = str;
    }

    public PlayerData getPlayer() {
        return player;
    }

    public void addDamage(double amount) {
        damage += amount;
        updateCombatTime();
    }

    public double getDamage() {
        return damage;
    }

    public void addDamageCause(EntityDamageEvent.DamageCause newCause) {
        causes.add(newCause);
    }

    public List<EntityDamageEvent.DamageCause> getDamageCauses() {
        return causes;
    }

    public void updateCombatTime() {
        if (lastHit == -1) {
            time = 0;
            lastHit = System.currentTimeMillis();
        } else {
            time = (System.currentTimeMillis() - lastHit) / 1000;
        }
    }

    public CombatInfo clone() {
        CombatInfo newInfo = new CombatInfo();
        newInfo.player = player;
        newInfo.damage = damage;
        newInfo.causes = new ArrayList(causes);
        newInfo.time = time;
        newInfo.lastHit = lastHit;
        return newInfo;
    }
}
