package net.elseland.xikage.MythicMobs.Adapters.Bukkit.Events;

import io.lumine.xikage.MythicLib.Adapters.AbstractEntity;
import io.lumine.xikage.MythicLib.Adapters.Bukkit.BukkitAdapter;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import net.elseland.xikage.MythicMobs.Skills.QueuedSkill;
import net.elseland.xikage.MythicMobs.Skills.SkillTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {
/*
    @EventHandler(
        priority = EventPriority.HIGH
    )
    public void ChunkLoadEvent(ChunkLoadEvent e) {
        if (e.getChunk() != null) {
            ChunkLoadListener.ChunkLoader cl = new ChunkLoadListener.ChunkLoader(e.getChunk());

            Bukkit.getScheduler().scheduleSyncDelayedTask(MythicMobs.plugin, cl, 40L);
        }
    }

    private class ChunkLoader implements Runnable {

        private Chunk c;

        public ChunkLoader(Chunk c) {
            this.c = c;
        }

        public void run() {
            if (this.c != null) {
                Entity[] el = this.c.getEntities();

                if (el.length != 0) {
                    Entity[] aentity = el;
                    int i = el.length;

                    for (int j = 0; j < i; ++j) {
                        Entity ee = aentity[j];

                        if (ee instanceof LivingEntity) {
                            LivingEntity l = (LivingEntity) ee;
                            ActiveMob am;

                            if (ActiveMobHandler.isRegisteredMob(ee.getUniqueId())) {
                                if (l.getRemoveWhenFarAway()) {
                                    ee.remove();
                                    return;
                                }

                                am = ActiveMobHandler.getMythicMobInstance((Entity) ((LivingEntity) ee));
                                if (am.getType().getDespawns()) {
                                    ActiveMobHandler.unregisterActiveMob(am);
                                    ee.remove();
                                } else {
                                    ee = am.getType().applyMobVolatileOptions(am);
                                }
                            } else {
                                am = ActiveMobHandler.registerActiveMob(BukkitAdapter.adapt(l));
                                if (am != null) {
                                    new QueuedSkill(SkillTrigger.SPAWN, am, (AbstractEntity) null);
                                }
                            }
                        }
                    }

                }
            }
        }
    }*/
}
