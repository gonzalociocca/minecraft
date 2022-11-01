package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import mineultra.core.common.util.C;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class FALLDAMAGE extends Perk
{
    private int _mod;
    
    public FALLDAMAGE(final int mod) {
        super("Feather Falling", new String[] { String.valueOf(C.cGray) + "You take " + mod + " damage from falling" });
        this._mod = mod;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void DamageDecrease(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        final Player damagee = event.GetDamageePlayer();
        if (damagee == null) {
            return;
        }
        if (!this.Kit.HasKit(damagee)) {
            return;
        }
        int decrease = 0;
        if (this._mod < 0) {
            decrease = (int)(-Math.min(Math.abs(this._mod), event.GetDamageInitial()));
        }
        event.AddMod(damagee.getName(), this.GetName(), decrease, false);
    }
}
