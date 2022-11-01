package mineultra.core.projectile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.UtilParticle;
import mineultra.core.common.util.UtilParticle.PCType;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ProjectileManager extends MiniPlugin
{
  private WeakHashMap<Entity, ProjectileUser> _thrown = new WeakHashMap();

  public ProjectileManager(JavaPlugin plugin)
  {
    super("Throw", plugin);
  }

  public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, double hitboxMult)
  {
    _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
      expireTime, hitPlayer, hitBlock, idle, false, null, 1.0F, 1.0F, null, 0, null, null, hitboxMult));
  }

  public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, boolean pickup, double hitboxMult)
  {
    _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
      expireTime, hitPlayer, hitBlock, idle, pickup, 
      null, 1.0F, 1.0F, null, 0, null, null, hitboxMult));
  }

  public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, Sound sound, float soundVolume, float soundPitch, Effect effect, int effectData, UpdateType effectRate, double hitboxMult)
  {
    _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
      expireTime, hitPlayer, hitBlock, idle, false, 
      sound, soundVolume, soundPitch, effect, effectData, effectRate, null, hitboxMult));
  }

  public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, Sound sound, float soundVolume, float soundPitch, PCType particle, Effect effect, int effectData, UpdateType effectRate, double hitboxMult)
  {
    _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
      expireTime, hitPlayer, hitBlock, idle, false, 
      sound, soundVolume, soundPitch, effect, effectData, effectRate, particle, hitboxMult));
  }

  public void AddThrow(Entity thrown, LivingEntity thrower, IThrown callback, long expireTime, boolean hitPlayer, boolean hitBlock, boolean idle, Sound sound, float soundVolume, float soundPitch, PCType particle, UpdateType effectRate, double hitboxMult)
  {
    _thrown.put(thrown, new ProjectileUser(this, thrown, thrower, callback, 
      expireTime, hitPlayer, hitBlock, idle, false, 
      sound, soundVolume, soundPitch, null, 0, effectRate, particle, hitboxMult));
  }

  @EventHandler
  public void Update(UpdateEvent event)
  {

    if (event.getType() == UpdateType.TICK)
    {

        HashSet remove = new HashSet();
Entity cur;
        for (Iterator<Entity> it = _thrown.keySet().iterator(); it.hasNext();) {
            cur = it.next();
            if (((ProjectileUser)_thrown.get(cur)).Collision())
                remove.add(cur);
            else if ((cur.isDead()) || (!cur.isValid()))
                remove.add(cur);
        }
      for (Iterator it = remove.iterator(); it.hasNext(); ) { 
          cur = (Entity)it.next();
        _thrown.remove(cur);
      }
    }

    for (ProjectileUser cur2 : (Collection<ProjectileUser>)_thrown.values())
      cur2.Effect(event);
  }

  @EventHandler(priority=EventPriority.LOW)
  public void Pickup(PlayerPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if ((_thrown.containsKey(event.getItem())) && 
      (!((ProjectileUser)_thrown.get(event.getItem())).CanPickup(event.getPlayer())))
      event.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.LOW)
  public void HopperPickup(InventoryPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (_thrown.containsKey(event.getItem()))
      event.setCancelled(true);
  }
}