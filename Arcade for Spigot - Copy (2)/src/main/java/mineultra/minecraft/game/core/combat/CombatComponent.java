package mineultra.minecraft.game.core.combat;

import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilTime;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import java.util.LinkedList;

public class CombatComponent
{
    private boolean _player;
    private LinkedList<CombatDamage> _damage;
    protected String EntityName;
    protected long LastDamage;
    
    public CombatComponent(final String name, final LivingEntity ent) {
        super();
        this._player = false;
        this.LastDamage = 0L;
        this.EntityName = name;
        if (ent != null && ent instanceof Player) {
            this._player = true;
        }
    }
    
    public void AddDamage(String source, final double dmg) {
        if (source == null) {
            source = "-";
        }
        this.GetDamage().addFirst(new CombatDamage(source, dmg));
        this.LastDamage = System.currentTimeMillis();
    }
    
    public String GetName() {
        if (this.EntityName.equals("Null")) {
            return "World";
        }
        return this.EntityName;
    }
    
    public LinkedList<CombatDamage> GetDamage() {
        if (this._damage == null) {
            this._damage = new LinkedList<CombatDamage>();
        }
        return this._damage;
    }
    
    public String GetReason() {
        if (this._damage.isEmpty()) {
            return null;
        }
        return this._damage.get(0).GetName();
    }
    
    public long GetLastDamage() {
        return this.LastDamage;
    }
    
    public int GetTotalDamage() {
        int total = 0;
        for (final CombatDamage cur : this.GetDamage()) {
            total += (int)cur.GetDamage();
        }
        return total;
    }
    
    public String GetBestWeapon() {
        final HashMap<String, Integer> cumulative = new HashMap<String, Integer>();
        String weapon = null;
        final int best = 0;
        for (final CombatDamage cur : this._damage) {
            int dmg = 0;
            if (cumulative.containsKey(cur.GetName())) {
                dmg = cumulative.get(cur.GetName());
            }
            cumulative.put(cur.GetName(), dmg);
            if (dmg >= best) {
                weapon = cur.GetName();
            }
        }
        return weapon;
    }
    
    public String Display(final long _deathTime) {
        String time = "";
        if (_deathTime == 0L) {
            time = String.valueOf(UtilTime.convertString(System.currentTimeMillis() - this.LastDamage, 1, UtilTime.TimeUnit.FIT)) + " Ago";
        }
        else {
            time = String.valueOf(UtilTime.convertString(_deathTime - this.LastDamage, 1, UtilTime.TimeUnit.FIT)) + " Prior";
        }
        return String.valueOf(F.name(this.EntityName)) + " [" + F.elem(String.valueOf(this.GetTotalDamage()) + "dmg") + "] [" + F.elem(this.GetBestWeapon()) + "]  [" + F.time(time) + "]";
    }
    
    public String Display(final long _deathTime, final CombatDamage damage) {
        String time = "";
        if (_deathTime == 0L) {
            time = String.valueOf(UtilTime.convertString(System.currentTimeMillis() - damage.GetTime(), 1, UtilTime.TimeUnit.FIT)) + " Ago";
        }
        else {
            time = String.valueOf(UtilTime.convertString(_deathTime - damage.GetTime(), 1, UtilTime.TimeUnit.FIT)) + " Prior";
        }
        return String.valueOf(F.name(this.EntityName)) + " [" + F.elem(String.valueOf(damage.GetDamage()) + " dmg") + "] [" + F.elem(damage.GetName()) + "]  [" + F.time(time) + "]";
    }
    
    public boolean IsPlayer() {
        return this._player;
    }
    
    public String GetLastDamageSource() {
        return this._damage.getFirst().GetName();
    }
}
