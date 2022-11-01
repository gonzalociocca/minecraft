package net.elseland.xikage.MythicMobs.Adapters.Bukkit.Events;

import io.lumine.xikage.MythicLib.Adapters.AbstractEntity;
import io.lumine.xikage.MythicLib.Adapters.Bukkit.BukkitAdapter;
import java.util.UUID;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import net.elseland.xikage.MythicMobs.Mobs.MobManager;
import net.elseland.xikage.MythicMobs.Skills.QueuedSkill;
import net.elseland.xikage.MythicMobs.Skills.SkillTrigger;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.projectiles.ProjectileSource;

public class SkillTriggerAD implements Listener {

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void MobSkillEvent(EntityDamageByEntityEvent e) {

        if (!e.isCancelled()) {
            MythicMobs.debug(3, "EntityDamageByEntityEvent fired for " + e.getFinalDamage());
            if (e.getEntity() instanceof LivingEntity) {
                AbstractEntity damager;

                if (e.getDamager() instanceof LivingEntity) {
                    damager = BukkitAdapter.adapt(e.getDamager());
                } else if (e.getDamager() instanceof Projectile) {
                    ProjectileSource am = ((Projectile) e.getDamager()).getShooter();

                    if (am instanceof LivingEntity) {
                        damager = BukkitAdapter.adapt((LivingEntity) am);
                    } else {
                        damager = null;
                    }
                } else {
                    damager = null;
                }

                AbstractEntity damaged;

                if (e.getEntity() instanceof LivingEntity) {
                    damaged = BukkitAdapter.adapt(e.getEntity());
                } else {
                    damaged = null;
                }

                ActiveMob am1;

                if (damaged != null && MythicMobs.inst().activeMobs.containsKey(damaged.getUniqueId())) {
                    am1 = ActiveMobHandler.getMythicMobInstance(damaged);
                    if (damager != null && ActiveMobHandler.isRegisteredMob(damager.getUniqueId())) {
                        ActiveMob am2 = ActiveMobHandler.getMythicMobInstance(damager);

                        if (am1.getFaction() != null && am2.getFaction() != null && am1.getFaction().equals(am2.getFaction())) {
                            e.setCancelled(true);
                            return;
                        }
                    }

                    if (am1.getType().maxAttackableRange > 0 && damager != null) {
                        if (damaged.getLocation().distanceSquared(damager.getLocation()) > Math.pow((double) am1.getType().maxAttackableRange, 2.0D)) {
                            MythicMobs.debug(3, "---- Damager is out of MaxCombatRange, cancelling damage.");
                            e.setCancelled(true);
                            return;
                        }
                    } else if (am1.getType().maxAttackableRange == 0) {
                        MythicMobs.debug(3, "---- MythicMob is not attackable, cancelling damage.");
                        e.setCancelled(true);
                        return;
                    }

                    if (am1.getType().getShowHealthInChat()) {
                        MobManager.showHealth(am1);
                    }

                    new QueuedSkill(SkillTrigger.DAMAGED, am1, damager);

                    /* no thread tables
                    if (am1.getType().usesThreatTable() && damager != null && !am1.getEntity().getUniqueId().equals(damager.getUniqueId())) {
                        am1.getThreatTable().threatGain(damager, e.getDamage());
                    }*/
                }

                if (damager != null && ActiveMobHandler.isRegisteredMob(damager.getUniqueId())) {
                    MythicMobs.debug(3, "A MythicMob attacked something for " + e.getOriginalDamage(DamageModifier.BASE));
                    am1 = ActiveMobHandler.getMythicMobInstance(damager);
                    MythicMobs.debug(3, "-- Attacking mob was " + am1.getType().getInternalName() + "!");
                    if (am1.getOwner().isPresent() && ((UUID) am1.getOwner().get()).equals(damaged.getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }

                    if (am1.isUsingDamageSkill()) {
                        e.setDamage(DamageModifier.BASE, am1.getLastDamageSkillAmount());
                    } else if (e.getDamager()!=null && e.getDamager() instanceof Creeper) {
                        if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
                            MythicMobs.debug(3, "** Setting Creeper Custom Damage");
                            e.setDamage(am1.getDamage());
                        }
                    } else if (e.getCause() == DamageCause.ENTITY_ATTACK) {
                        MythicMobs.debug(3, "** Setting Mob Custom Damage to " + am1.getDamage());
                        if (am1.getDamage() > 0.0D && MythicMobs.inst().getMinecraftVersion() < 8) {
                            e.setDamage(am1.getDamage());
                        }
                    }

                    new QueuedSkill(SkillTrigger.ATTACK, am1, damaged);
                }

            }
        }
    }
}
