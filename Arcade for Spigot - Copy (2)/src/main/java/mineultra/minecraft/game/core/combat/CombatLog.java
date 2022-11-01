package mineultra.minecraft.game.core.combat;

import java.util.HashMap;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilTime;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.LinkedList;

public class CombatLog
{
    private LinkedList<CombatComponent> _damager;
    private CombatComponent _player;
    private long _expireTime;
    private long _deathTime;
    private CombatComponent _killer;
    private int _assistants;
    private String _killedColor;
    private String _killerColor;
    protected CombatComponent LastDamager;
    protected long _lastDamaged;
    protected long _lastCombat;
    
    public CombatLog(final Player player, final long expireTime) {
        super();
        this._damager = new LinkedList<>();
        this._deathTime = 0L;
        this._killedColor = new StringBuilder().append(ChatColor.YELLOW).toString();
        this._killerColor = new StringBuilder().append(ChatColor.YELLOW).toString();
        this._expireTime = expireTime;
        this._player = new CombatComponent(player.getName(), (LivingEntity)player);
    }
    
    public LinkedList<CombatComponent> GetAttackers() {
        return this._damager;
    }
    
    public CombatComponent GetPlayer() {
        return this._player;
    }
    
    public void Attacked(final String damagerName, final double damage, final LivingEntity damagerEnt, final String attackName) {
        final CombatComponent comp = this.GetEnemy(damagerName, damagerEnt);
        comp.AddDamage(attackName, damage);
        this.LastDamager = comp;
        this._lastDamaged = System.currentTimeMillis();
        this._lastCombat = System.currentTimeMillis();
    }
    
    public CombatComponent GetEnemy(final String name, final LivingEntity ent) {
        this.ExpireOld();
        CombatComponent component = null;
        for (final CombatComponent cur : this._damager) {
            if (cur.GetName().equals(name)) {
                component = cur;
            }
        }
        if (component != null) {
            this._damager.remove(component);
            this._damager.addFirst(component);
            return this._damager.getFirst();
        }
        this._damager.addFirst(new CombatComponent(name, ent));
        return this._damager.getFirst();
    }
    
    public void ExpireOld() {
        int expireFrom = -1;
        for (int i = 0; i < this._damager.size(); ++i) {
            if (UtilTime.elapsed(this._damager.get(i).GetLastDamage(), this._expireTime)) {
                expireFrom = i;
                break;
            }
        }
        if (expireFrom != -1) {
            while (this._damager.size() > expireFrom) {
                this._damager.remove(expireFrom);
            }
        }
    }
    
    public LinkedList<String> Display() {
        final LinkedList<String> out = new LinkedList<>();
        for (int i = 0; i < 8; ++i) {
            if (i < this._damager.size()) {
                out.add(F.desc("#" + i, this._damager.get(i).Display(this._deathTime)));
            }
        }
        return out;
    }
    
    public LinkedList<String> DisplayAbsolute() {
        final HashMap<Long, String> components = new HashMap<>();
        for (final CombatComponent cur : this._damager) {
            for (final CombatDamage dmg : cur.GetDamage()) {
                components.put(dmg.GetTime(), cur.Display(this._deathTime, dmg));
            }
        }
        int id = components.size();
        final LinkedList<String> out = new LinkedList<>();
        while (!components.isEmpty()) {
            long bestTime = 0L;
            String bestString = null;
            for (final long time : components.keySet()) {
                if (time > bestTime || bestString == null) {
                    bestTime = time;
                    bestString = components.get(time);
                }
            }
            components.remove(bestTime);
            out.addFirst(F.desc("#" + id, bestString));
            --id;
        }
        return out;
    }
    
    public CombatComponent GetKiller() {
        return this._killer;
    }
    
    public void SetKiller(final CombatComponent killer) {
        this._killer = killer;
    }
    
    public int GetAssists() {
        return this._assistants;
    }
    
    public void SetAssists(final int assistants) {
        this._assistants = assistants;
    }
    
    public CombatComponent GetLastDamager() {
        return this.LastDamager;
    }
    
    public long GetLastDamaged() {
        return this._lastDamaged;
    }
    
    public long GetLastCombat() {
        return this._lastCombat;
    }
    
    public void SetLastCombat(final long time) {
        this._lastCombat = time;
    }
    
    public long GetDeathTime() {
        return this._deathTime;
    }
    
    public void SetDeathTime(final long deathTime) {
        this._deathTime = deathTime;
    }
    
    public String GetKilledColor() {
        return this._killedColor;
    }
    
    public void SetKilledColor(final String color) {
        this._killedColor = color;
    }
    
    public String GetKillerColor() {
        return this._killerColor;
    }
    
    public void SetKillerColor(final String color) {
        this._killerColor = color;
    }
}
