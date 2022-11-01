package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import mineultra.core.common.util.C;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class IRONDEF extends Perk
{
    private double _reduction;
    
    public IRONDEF(final double d) {
        super("Iron Skin", new String[] { String.valueOf(C.cGray) + "Receive " + d + " less damage from attacks" });
        this._reduction = d;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void DamageDecrease(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            return;
        }
        if (event.GetDamage() <= 1.0) {
            return;
        }
        final Player damagee = event.GetDamageePlayer();
        if (damagee == null) {
            return;
        }
        if (!this.Kit.HasKit(damagee)) {
          
event.AddMod(damagee.getName(), this.GetName(), -2, false);
return;
        }
       
        
        event.AddMod(damagee.getName(), this.GetName(), -this._reduction, false);
    }
}
