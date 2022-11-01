package net.elseland.xikage.MythicMobs.Adapters.Bukkit.Events;

import io.lumine.xikage.MythicLib.Adapters.AbstractEntity;
import io.lumine.xikage.MythicLib.Adapters.TaskManager;
import io.lumine.xikage.MythicLib.Adapters.Bukkit.BukkitAdapter;
import java.util.Iterator;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MobDamagedListener implements Listener {

    @EventHandler(
            priority = EventPriority.NORMAL
    )
    public void MobDamagedEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
                if(ActiveMobHandler.isRegisteredMob(e.getEntity().getUniqueId())) {
                    if(e.getEntity().getLocation().getY() < 1){
                        return;
                    }
                    //am = ActiveMobHandler.registerActiveMob(BukkitAdapter.adapt(damaged));
                }else{return;}

            final LivingEntity damaged = (LivingEntity)e.getEntity();
            final ActiveMob am = ActiveMobHandler.getMythicMobInstance(e.getEntity());
                if(am != null) {
                    //MythicMobs.debug(3, "-- MythicMob " + am.getType().getInternalName() + " took damage!");
/*                    if(am.hasImmunityTable() && !(e instanceof EntityDamageByEntityEvent)) {
                        if(am.getImmunityTable().onCooldown((AbstractEntity)null)) {
                            e.setCancelled(true);
                            //MythicMobs.debug(3, "---- MythicMob is currently immune to damage from non-player sources!");
                            return;
                        }

                        am.getImmunityTable().setCooldown((AbstractEntity)null);
                        //MythicMobs.debug(3, "---- Setting MythicMob immune to damage from non-player sources!");
                    }*/

                    if(am.getType().getIsInvincible()) {
                        //MythicMobs.debug(3, "---- MythicMob is optionInvincible, canceling damage.");
                        e.setCancelled(true);
                    } else {
                        double damage = e.getDamage();
                        if(am.getArmor() > 0.0D) {
                            //MythicMobs.debug(3, "---- Modifying damage based on armor: " + am.getArmor());
                            damage -= am.getArmor();
                            if(damage < 1.0D) {
                                damage = 1.0D;
                            }
                            e.setDamage(damage);
                        }

                        if(am.getType().getDamageModifiers() != null) {
                            Iterator Loc = am.getType().getDamageModifiers().iterator();

                            while(Loc.hasNext()) {
                                String dm = (String)Loc.next();
                                String[] split = dm.split(" ");

                                try {
                                    if(DamageCause.valueOf(split[0]) == e.getCause()) {
                                        damage *= Double.parseDouble(split[1]);
                                        if(damage > 0.0D) {
                                            e.setDamage(damage);
                                        } else if(damage == 0.0D) {
                                            e.setDamage(0.0D);
                                        } else if(damage < 0.0D) {
                                            e.setDamage(0.0D);
                                            e.setCancelled(true);
                                            if(damaged.getHealth() - damage > damaged.getMaxHealth()) {
                                                damaged.setHealth(damaged.getMaxHealth());
                                            } else {
                                                damaged.setHealth(damaged.getHealth() - damage);
                                            }
                                        }
                                    }
                                } catch (Exception exception) {

                                }
                            }
                        }

                        if(am.hasImmunityTable()) {
                            TaskManager.get().runLater(new Runnable() {
                                public void run() {
                                    damaged.setNoDamageTicks(0);
                                }
                            }, 1);
                        } else if(am.getNoDamageTicks() != 20) {
                            TaskManager.get().runLater(new Runnable() {
                                public void run() {
                                    damaged.setNoDamageTicks(am.getNoDamageTicks());
                                }
                            }, 1);
                        }

                        if(am.getType().digout && e.getCause() == DamageCause.SUFFOCATION) {
                            Location Loc1 = damaged.getLocation().add(0.0D, 2.0D, 0.0D);
                            damaged.teleport(Loc1);
                            e.setCancelled(true);
                        }
                    }
                }
        }
    }
}
