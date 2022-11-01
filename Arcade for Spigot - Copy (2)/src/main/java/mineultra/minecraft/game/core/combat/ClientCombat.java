package mineultra.minecraft.game.core.combat;

import mineultra.core.common.util.UtilTime;
import org.bukkit.entity.LivingEntity;
import java.util.WeakHashMap;
import java.util.LinkedList;

public class ClientCombat
{
    private LinkedList<CombatLog> _kills;
    private LinkedList<CombatLog> _assists;
    private LinkedList<CombatLog> _deaths;
    private WeakHashMap<LivingEntity, Long> _lastHurt;
    private WeakHashMap<LivingEntity, Long> _lastHurtBy;
    private long _lastHurtByWorld;
    
    public ClientCombat() {
        super();
        this._kills = new LinkedList<CombatLog>();
        this._assists = new LinkedList<CombatLog>();
        this._deaths = new LinkedList<CombatLog>();
        this._lastHurt = new WeakHashMap<LivingEntity, Long>();
        this._lastHurtBy = new WeakHashMap<LivingEntity, Long>();
        this._lastHurtByWorld = 0L;
    }
    
    public LinkedList<CombatLog> GetKills() {
        return this._kills;
    }
    
    public LinkedList<CombatLog> GetAssists() {
        return this._assists;
    }
    
    public LinkedList<CombatLog> GetDeaths() {
        return this._deaths;
    }
    
    public boolean CanBeHurtBy(final LivingEntity damager) {
        if (damager == null) {
            if (UtilTime.elapsed(this._lastHurtByWorld, 250L)) {
                this._lastHurtByWorld = System.currentTimeMillis();
                return true;
            }
            return false;
        }
        else {
            if (!this._lastHurtBy.containsKey(damager)) {
                this._lastHurtBy.put(damager, System.currentTimeMillis());
                return true;
            }
            if (System.currentTimeMillis() - this._lastHurtBy.get(damager) > 400L) {
                this._lastHurtBy.put(damager, System.currentTimeMillis());
                return true;
            }
            return false;
        }
    }
    
    public boolean CanHurt(final LivingEntity damagee) {
        if (damagee == null) {
            return true;
        }
        if (!this._lastHurt.containsKey(damagee)) {
            this._lastHurt.put(damagee, System.currentTimeMillis());
            return true;
        }
        if (System.currentTimeMillis() - this._lastHurt.get(damagee) > 400L) {
            this._lastHurt.put(damagee, System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
