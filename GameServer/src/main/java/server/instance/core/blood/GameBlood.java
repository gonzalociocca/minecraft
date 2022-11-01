package server.instance.core.blood;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;
import server.common.Code;
import server.common.UpdateType;
import server.common.event.UpdateEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GameBlood {
    private HashMap<Item, Integer> _blood = new HashMap();

    public void onPlayerDeath(PlayerDeathEvent event) {
        Effects(event.getEntity().getEyeLocation(), 10, 0.5D, Sound.HURT_FLESH, 1.0F, 1.0F, Material.INK_SACK, (byte) 1, true);
    }

    public void Effects(Location loc, int particles, double velMult, Sound sound, float soundVol, float soundPitch, Material type, byte data, boolean bloodStep) {
        Effects(loc, particles, velMult, sound, soundVol, soundPitch, type, data, 10, bloodStep);
    }

    public void Effects(Location loc, int particles, double velMult, Sound sound, float soundVol, float soundPitch, Material type, byte data, int ticks, boolean bloodStep) {
        for (int i = 0; i < particles; i++) {
            Item item = loc.getWorld().dropItem(loc,
                    Code.makeItemStack(type, data));

            item.setVelocity(new Vector((Math.random() - 0.5D) * velMult, Math.random() * velMult, (Math.random() - 0.5D) * velMult));

            _blood.put(item, Integer.valueOf(ticks));
        }

        if (bloodStep) {
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 55);
        }
        loc.getWorld().playSound(loc, sound, soundVol, soundPitch);
    }

    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        HashSet expire = new HashSet();

        for (Item cur : _blood.keySet()) {
            if ((cur.getTicksLived() > _blood.get(cur).intValue()) || (!cur.isValid()))
                expire.add(cur);
        }
        for (Item cur : (Set<Item>) expire) {
            cur.remove();
            _blood.remove(cur);
        }
    }

    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (_blood.containsKey(event.getItem()))
            event.setCancelled(true);
    }

    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (_blood.containsKey(event.getItem()))
            event.setCancelled(true);
    }
}