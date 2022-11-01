package net.elseland.xikage.MythicMobs.Adapters.Bukkit.Events;

import io.lumine.xikage.MythicLib.Adapters.Bukkit.BukkitAdapter;
import java.util.UUID;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.IO.Load.Configuration;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class MobTargetListener implements Listener {
    /*
    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void MobTargetEventNoOwner(EntityTargetLivingEntityEvent e) {
        if(e.getTarget() != null) {
            if(ActiveMobHandler.isRegisteredMob(e.getEntity().getUniqueId())) {
                ActiveMob amE = ActiveMobHandler.getMythicMobInstance((Entity)((LivingEntity)e.getEntity()));
                if(amE.getOwner().isPresent() && ((UUID)amE.getOwner().get()).equals(e.getTarget().getUniqueId())) {
                    e.setCancelled(true);
                }

            }
        }
    }*/
/*
    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void MobTargetEventNoFaction(EntityTargetLivingEntityEvent e) {
        if(e.getTarget() != null) {
            if(ActiveMobHandler.isRegisteredMob(e.getEntity().getUniqueId())) {
                if(ActiveMobHandler.isRegisteredMob(e.getTarget().getUniqueId())) {
                    ActiveMob amE = ActiveMobHandler.getMythicMobInstance((Entity)((LivingEntity)e.getEntity()));
                    ActiveMob amT = ActiveMobHandler.getMythicMobInstance((Entity)e.getTarget());
                    if(amE.getFaction() != null && amE.getFaction().equals(amT.getFaction())) {
                        e.setCancelled(true);
                    }

                }
            }
        }
    }*/
/*
    @EventHandler(
            priority = EventPriority.LOW
    )
    public void MobTargetEvent(EntityTargetLivingEntityEvent e) {
        if(!e.isCancelled()) {
            if(Configuration.EnableThreatTables) {
                if(e.getEntity() instanceof LivingEntity) {
                    ActiveMob am = ActiveMobHandler.getMythicMobInstance((Entity)((LivingEntity)e.getEntity()));
                    if(am != null) {
                        if(!am.isDead() && am.getType().usesThreatTable()) {
                            MythicMobs.debug(3, "Target Change Event called, reviewing Threat Table");
                            if(!am.getThreatTable().targetEvent(BukkitAdapter.adapt(e.getTarget()))) {
                                e.setCancelled(true);
                                am.setTarget(am.getThreatTable().getTopThreatHolder());
                            }

                        }
                    }
                }
            }
        }
    }*/
/*
    @EventHandler(
            priority = EventPriority.LOW
    )

    public void MobTargetEvent(EntityTargetEvent e) {

        if(Configuration.EnableThreatTables) {
            if(e.getEntity() instanceof LivingEntity) {
                ActiveMob am = ActiveMobHandler.getMythicMobInstance((Entity)((LivingEntity)e.getEntity()));
                if(am != null) {
                    if(!am.isDead() && am.getType().usesThreatTable() && !am.getThreatTable().inCombat()) {
                        MythicMobs.debug(3, "Target Change Event called, reviewing Threat Table");
                        if(!am.getThreatTable().targetEvent(BukkitAdapter.adapt(e.getTarget()))) {
                            e.setCancelled(true);
                            am.setTarget(am.getThreatTable().getTopThreatHolder());
                        }

                    }
                }
            }
        }
    }*/
}
