package me.iiSnipez.CombatLog.Listeners;

import me.iiSnipez.CombatLog.CombatLog;
import me.iiSnipez.CombatLog.Events.PlayerTagEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    CombatLog plugin;

    public EntityDamageByEntityListener(CombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            Entity damagee = event.getEntity();
            Entity damager = event.getDamager();
            PlayerTagEvent event1;

            if (damagee instanceof Player && damager instanceof Player) {
                if (((Player) damager).hasPermission("combatlog.bypass") || ((Player) damagee).hasPermission("combatlog.bypass")) {
                    return;
                }

                event1 = new PlayerTagEvent((Player) damager, (Player) damagee, this.plugin.tagDuration);
                this.plugin.getServer().getPluginManager().callEvent(event1);
            }

            if (damagee instanceof Player && damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
                if (((Player) ((Projectile)damager).getShooter()).hasPermission("combatlog.bypass") || damagee.hasPermission("combatlog.bypass")) {
                    return;
                }

                if (damager instanceof Arrow) {
                    event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(), (Player) damagee, this.plugin.tagDuration);
                    this.plugin.getServer().getPluginManager().callEvent(event1);
                } else if (damager instanceof Snowball) {
                    event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(), (Player) damagee, this.plugin.tagDuration);
                    this.plugin.getServer().getPluginManager().callEvent(event1);
                } else if (damager instanceof Egg) {
                    event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(), (Player) damagee, this.plugin.tagDuration);
                    this.plugin.getServer().getPluginManager().callEvent(event1);
                } else if (damager instanceof ThrownPotion) {
                    event1 = new PlayerTagEvent((Player) ((Projectile) damager).getShooter(), (Player) damagee, this.plugin.tagDuration);
                    this.plugin.getServer().getPluginManager().callEvent(event1);
                }
            }
        }

    }
}