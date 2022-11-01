package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class STRENGTH extends Perk
{
    private int _power;
    
    public STRENGTH(final int power) {
        super("Strength", new String[] { String.valueOf(C.cGray) + "You deal " + power + " more damage" });
        this._power = power;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void DamageDecrease(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(true);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        event.AddMod(damager.getName(), this.GetName(), this._power, false);
    }
}
