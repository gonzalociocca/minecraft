package mineultra.core.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import mineultra.core.MiniPlugin;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Blood extends MiniPlugin
{
  private HashMap<Item, Integer> _blood = new HashMap();

  public Blood(JavaPlugin plugin)
  {
    super("Blood", plugin);
  }

  @EventHandler
  public void Death(PlayerDeathEvent event)
  {
    Effects(event.getEntity().getEyeLocation(), 10, 0.5D, Sound.HURT_FLESH, 1.0F, 1.0F, Material.INK_SACK, (byte)1, true);
  }

  public void Effects(Location loc, int particles, double velMult, Sound sound, float soundVol, float soundPitch, Material type, byte data, boolean bloodStep)
  {
    Effects(loc, particles, velMult, sound, soundVol, soundPitch, type, data, 10, bloodStep);
  }

  public void Effects(Location loc, int particles, double velMult, Sound sound, float soundVol, float soundPitch, Material type, byte data, int ticks, boolean bloodStep)
  {
    for (int i = 0; i < particles; i++)
    {
      Item item = loc.getWorld().dropItem(loc, 
        ItemStackFactory.Instance.CreateStack(type, data));

      item.setVelocity(new Vector((Math.random() - 0.5D) * velMult, Math.random() * velMult, (Math.random() - 0.5D) * velMult));

      _blood.put(item, Integer.valueOf(ticks));
    }

    if (bloodStep) {
      loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 55);
    }
    loc.getWorld().playSound(loc, sound, soundVol, soundPitch);
  }

  @EventHandler
  public void Update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    HashSet expire = new HashSet();

    for (Item cur : _blood.keySet()) {
      if ((cur.getTicksLived() > ((Integer)_blood.get(cur)).intValue()) || (!cur.isValid()))
        expire.add(cur);
    }
    for (Item cur : (Set<Item>)expire)
    {
      cur.remove();
      _blood.remove(cur);
    }
  }

  @EventHandler
  public void Pickup(PlayerPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (_blood.containsKey(event.getItem()))
      event.setCancelled(true);
  }

  @EventHandler
  public void HopperPickup(InventoryPickupItemEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if (_blood.containsKey(event.getItem()))
      event.setCancelled(true);
  }
}