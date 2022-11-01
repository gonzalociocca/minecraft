package net.elseland.xikage.MythicMobs.Adapters.Bukkit.Events;

import java.util.HashSet;
import java.util.Iterator;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMobHandler;
import net.elseland.xikage.MythicMobs.Spawners.MythicSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener implements Listener {
/*
    @EventHandler(
            priority = EventPriority.NORMAL
    )
    public void ChunkUnloadedEvent(ChunkUnloadEvent e) {

        String cS = e.getChunk().getWorld().getName() + "-" + e.getChunk().getX() + "-" + e.getChunk().getZ();
        if(MythicMobs.inst().mmChunkSpawnerLookup.contains(cS)) {
            Iterator iterator = ((HashSet)MythicMobs.plugin.mmChunkSpawnerLookup.get(cS)).iterator();

            while(iterator.hasNext()) {
                MythicSpawner ms = (MythicSpawner)iterator.next();
                ms.unloadSpawner();
            }
        }

        Entity[] aentity = e.getChunk().getEntities();
        int i = aentity.length;

        for(int j = 0; j < i; ++j) {
            Entity ee = aentity[j];
            if(ee instanceof LivingEntity) {
                ActiveMob am = ActiveMobHandler.getMythicMobInstance((Entity)((LivingEntity)ee));
                if(am != null && am.getType().getDespawns()) {
                    ActiveMobHandler.unregisterActiveMob(am);
                    ee.remove();
                }
            }
        }
    }*/
}
