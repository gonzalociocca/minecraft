/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import mineultra.core.common.util.C;
import mineultra.game.center.kit.Perk;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;

public class SHOCKINGSTRIKE extends Perk
{
    public SHOCKINGSTRIKE() {
        super("Shocking Strikes", new String[] { String.valueOf(C.cGray) + "Your attacks Shock/Blind/Slow opponents." });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void Effect(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
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
        this.Manager.GetBuffer().Factory().Slow(this.GetName(), event.GetDamageeEntity(), (LivingEntity)damager, 2.0, 1, false, false, false, false);
        this.Manager.GetBuffer().Factory().Blind(this.GetName(), event.GetDamageeEntity(), (LivingEntity)damager, 1.0, 0, false, false, false);
        this.Manager.GetBuffer().Factory().Shock(this.GetName(), event.GetDamageeEntity(), (LivingEntity)damager, 1.0, false, false);
    }
}
