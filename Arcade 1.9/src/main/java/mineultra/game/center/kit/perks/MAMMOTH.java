/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import mineultra.core.common.util.C;
import mineultra.game.center.kit.Perk;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;

public class MAMMOTH extends Perk
{
    public MAMMOTH() {
        super("Mammoth", new String[] { String.valueOf(C.cGray) + "Take 50% knockback and deal 150% knockback" });
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void KnockbackIncrease(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        event.AddKnockback(this.GetName(), 1.5);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void KnockbackDecrease(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        final Player damagee = event.GetDamageePlayer();
        if (damagee == null) {
            return;
        }
        if (!this.Kit.HasKit(damagee)) {
            return;
        }
        event.AddKnockback(this.GetName(), 0.5);
    }
}
