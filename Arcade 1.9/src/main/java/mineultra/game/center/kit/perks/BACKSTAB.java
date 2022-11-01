package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityDamageEvent;
import mineultra.core.common.util.C;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class BACKSTAB extends Perk
{
    public BACKSTAB() {
        super("Backstab", new String[] { String.valueOf(C.cGray) + "Deal +2 damage from behind opponents." });
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void Damage(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (event.GetDamageInitial() <= 1.0) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        final LivingEntity damagee = event.GetDamageeEntity();
        if (damagee == null) {
            return;
        }
        final Vector look = damagee.getLocation().getDirection();
        look.setY(0);
        look.normalize();
        final Vector from = damager.getLocation().toVector().subtract(damagee.getLocation().toVector());
        from.setY(0);
        from.normalize();
        final Vector check = new Vector(look.getX() * -1.0, 0.0, look.getZ() * -1.0);
        if (check.subtract(from).length() < 0.8) {
            event.AddMod(damager.getName(), this.GetName(), 2.0, true);
            damagee.getWorld().playSound(damagee.getLocation(), Sound.ENTITY_SHULKER_HURT_CLOSED, 1.0f, 2.0f);
        }
    }
}
