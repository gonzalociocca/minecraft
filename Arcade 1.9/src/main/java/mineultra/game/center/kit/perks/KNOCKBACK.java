package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.recharge.Recharge;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class KNOCKBACK extends Perk
{
    private double _power;
    
    public KNOCKBACK(final double power) {
        super("Knockback", new String[] { String.valueOf(C.cGray) + "Attacks gives knockback with " + power + " power." });
        this._power = power;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void Knockback(final CustomDamageEvent event) {
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        if (!this.Manager.IsAlive(damager)) {
            return;
        }
        event.SetKnockback(false);
        if (!Recharge.Instance.use(damager, "KB " + UtilEnt.getName((Entity)event.GetDamageeEntity()), 400L, false, false)) {
            return;
        }
        event.GetDamageeEntity().playEffect(EntityEffect.HURT);
        UtilAction.velocity((Entity)event.GetDamageeEntity(), UtilAlg.getTrajectory((Entity)damager, (Entity)event.GetDamageeEntity()), this._power, false, 0.0, 0.1, 10.0, true);
    }
}
