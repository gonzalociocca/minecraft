package server.instance.core.creature;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;
import server.common.Code;
import server.instance.core.creature.event.CreatureSpawnCustomEvent;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameCreature {

    @Expose private boolean _spawnForce = false;
    @Expose private boolean _disableCustom = true;

    public Entity SpawnEntity(Location location, EntityType entityType) {
        _spawnForce = true;
        Entity entity = location.getWorld().spawnEntity(location, entityType);
        _spawnForce = false;

        return entity;
    }

    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!_disableCustom) {
            if ((event.getEntity() instanceof LivingEntity)) {
                event.getEntity().setCanPickupItems(false);
            }
            if (_spawnForce) {
                return;
            }

            if (event.getEntityType() == EntityType.SQUID) {
                event.setCancelled(true);
                return;
            }

            CreatureSpawnCustomEvent customEvent = new CreatureSpawnCustomEvent(event.getLocation());

            Bukkit.getServer().getPluginManager().callEvent(customEvent);

            if (customEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    public void onEntityDeath(EntityDeathEvent event) {
        if (!_disableCustom) {
            event.setDroppedExp(0);
            List drops = event.getDrops();

            if (event.getEntityType() == EntityType.PLAYER)
                drops.add(Code.makeItemStack(Material.BONE, 1));
            else {
                drops.clear();
            }

            if (event.getEntityType() == EntityType.CHICKEN) {
                drops.add(Code.makeItemStack(Material.RAW_CHICKEN, 1));
                drops.add(Code.makeItemStack(Material.FEATHER, 2 + UtilMath.r(5)));
                drops.add(Code.makeItemStack(Material.BONE, 1));
            } else if (event.getEntityType() == EntityType.COW) {
                drops.add(Code.makeItemStack(Material.RAW_BEEF, 1 + UtilMath.r(4)));
                drops.add(Code.makeItemStack(Material.LEATHER, 2 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.BONE, 3 + UtilMath.r(4)));
            }

            if (event.getEntityType() == EntityType.MUSHROOM_COW) {
                drops.add(Code.makeItemStack(Material.RAW_BEEF, 1 + UtilMath.r(4)));
                drops.add(Code.makeItemStack(Material.RED_MUSHROOM, 2 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.BONE, 3 + UtilMath.r(4)));
            } else if (event.getEntityType() == EntityType.OCELOT) {
                drops.add(Code.makeItemStack(Material.RAW_BEEF, 1 + UtilMath.r(2)));
                drops.add(Code.makeItemStack(Material.RAW_FISH, 2 + UtilMath.r(7)));
                drops.add(Code.makeItemStack(Material.BONE, 1 + UtilMath.r(2)));
            } else if (event.getEntityType() == EntityType.PIG) {
                drops.add(Code.makeItemStack(Material.PORK, 1 + UtilMath.r(2)));
                drops.add(Code.makeItemStack(Material.BONE, 2 + UtilMath.r(2)));
            } else if (event.getEntityType() == EntityType.SHEEP) {
                drops.add(Code.makeItemStack(Material.RAW_BEEF, 1 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.WOOL, 1 + UtilMath.r(4)));
                drops.add(Code.makeItemStack(Material.BONE, 2 + UtilMath.r(3)));
            } else if (event.getEntityType() == EntityType.VILLAGER) {
                drops.add(Code.makeItemStack(Material.BONE, 2 + UtilMath.r(3)));
            } else if (event.getEntityType() == EntityType.BLAZE) {
                drops.add(Code.makeItemStack(Material.BLAZE_ROD, 1));
                drops.add(Code.makeItemStack(Material.BONE, 6 + UtilMath.r(7)));
            } else if (event.getEntityType() == EntityType.CAVE_SPIDER) {
                drops.add(Code.makeItemStack(Material.WEB, 2 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.SPIDER_EYE, 1));
                drops.add(Code.makeItemStack(Material.BONE, 6 + UtilMath.r(7)));
            } else if (event.getEntityType() == EntityType.CREEPER) {
                drops.add(Code.makeItemStack(Material.COAL, 6 + UtilMath.r(13)));
                drops.add(Code.makeItemStack(Material.BONE, 12 + UtilMath.r(13)));
            } else if (event.getEntityType() == EntityType.ENDERMAN) {
                drops.add(Code.makeItemStack(Material.ENDER_PEARL, 1));
                drops.add(Code.makeItemStack(Material.BONE, 12 + UtilMath.r(13)));
            } else if (event.getEntityType() == EntityType.GHAST) {
                drops.add(Code.makeItemStack(Material.GHAST_TEAR, 1));
                drops.add(Code.makeItemStack(Material.BONE, 36 + UtilMath.r(37)));
                for (int i = 0; i < 5 + UtilMath.r(11); i++) {
                    drops.add(Code.makeItemStack(Material.EMERALD, 1));
                }
            } else if (event.getEntityType() == EntityType.IRON_GOLEM) {
                drops.add(Code.makeItemStack(Material.IRON_INGOT, 2 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.BONE, 12 + UtilMath.r(13)));
            } else if (event.getEntityType() == EntityType.MAGMA_CUBE) {
                drops.add(Code.makeItemStack(Material.MAGMA_CREAM, 1));
                drops.add(Code.makeItemStack(Material.BONE, 1 + UtilMath.r(2)));
            } else if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
                drops.add(Code.makeItemStack(Material.GRILLED_PORK, 1 + UtilMath.r(2)));
                drops.add(Code.makeItemStack(Material.ROTTEN_FLESH, 1 + UtilMath.r(2)));
                drops.add(Code.makeItemStack(Material.BONE, 2 + UtilMath.r(2)));
                drops.add(Code.makeItemStack(Material.ARROW, 1 + UtilMath.r(12)));
                if (UtilMath.r(100) > 90) drops.add(Code.makeItemStack(Material.GOLD_SWORD, 1));

            } else if (event.getEntityType() == EntityType.SILVERFISH) {
                drops.add(Code.makeItemStack(Material.BONE, 1 + UtilMath.r(2)));
            } else if (event.getEntityType() == EntityType.SKELETON) {
                if (((Skeleton) event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
                    drops.add(Code.makeItemStack(Material.ARROW, 4 + UtilMath.r(5)));
                    drops.add(Code.makeItemStack(Material.BONE, 12 + UtilMath.r(13)));
                } else {
                    drops.add(Code.makeItemStack(Material.ARROW, 4 + UtilMath.r(10)));
                    drops.add(Code.makeItemStack(Material.BONE, 12 + UtilMath.r(26)));
                }

            } else if (event.getEntityType() == EntityType.SLIME) {
                drops.add(Code.makeItemStack(Material.BONE, 1 + UtilMath.r(2)));
            } else if (event.getEntityType() == EntityType.SPIDER) {
                drops.add(Code.makeItemStack(Material.WEB, 2 + UtilMath.r(3)));
                drops.add(Code.makeItemStack(Material.SPIDER_EYE, 1));
                drops.add(Code.makeItemStack(Material.BONE, 6 + UtilMath.r(7)));
            } else if (event.getEntityType() == EntityType.ZOMBIE) {
                event.getDrops().add(Code.makeItemStack(Material.ROTTEN_FLESH, 1));
                drops.add(Code.makeItemStack(Material.BONE, 6 + UtilMath.r(7)));
            }
        }
    }

    public void onEntityExplode(EntityExplodeEvent event) {
        if (!_disableCustom) {
            if (event.getEntity() instanceof Creeper) {
                HashMap players = UtilPlayer.getInRadius(event.getEntity().getLocation(), 8.0D);
                for (Player cur : (List<Player>) players.keySet()) {
                    Vector vec = UtilAlg.getTrajectory(event.getEntity().getLocation(), cur.getLocation());
                    UtilAction.velocity(cur, vec, 1.0D + 2.0D * ((Double) players.get(cur)).doubleValue(), false, 0.0D, 0.5D + 1.0D * ((Double) players.get(cur)).doubleValue(), 2.0D, true);
                }
            }
        }
    }

    public void SetForce(boolean force) {
        _spawnForce = force;
    }

    public void AddEntityName(LivingEntity ent, String name) {
        if (ent == null) {
            return;
        }
        UtilEnt.GetEntityNames().put(ent, name);
    }

    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            HashSet remove = new HashSet();

            for (Entity ent : UtilEnt.GetEntityNames().keySet()) {
                if ((ent.isDead()) || (!ent.isValid()))
                    remove.add(ent);
            }
            for (Entity ent : (Set<Entity>) remove)
                UtilEnt.GetEntityNames().remove(ent);
        }
    }

    public void SetDisableCustomDrops(boolean var) {
        _disableCustom = var;
    }
}