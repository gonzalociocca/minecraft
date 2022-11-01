package mineultra.minecraft.game.core.fire;

import org.bukkit.Effect;
import org.bukkit.Location;
import java.util.HashSet;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.EntityEffect;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Item;
import java.util.HashMap;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilMath;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.Buffer.Buffer;
import mineultra.minecraft.game.core.Buffer.BufferManager;
import mineultra.minecraft.game.core.damage.DamageManager;

public class Fire extends MiniPlugin
{
    private final BufferManager _BufferManager;
    private final DamageManager _damageManager;
    private final HashMap<Item, FireData> _fire;
    
    public Fire(final JavaPlugin plugin, final BufferManager BufferManager, final DamageManager damageManager) {
        super("Fire", plugin);
        this._fire = new HashMap<>();
        this._BufferManager = BufferManager;
        this._damageManager = damageManager;
    }
    
    public void Add(final Item item, final LivingEntity owner, final double expireTime, final double delayTime, final double burnTime, final int damage, final String skillName) {
        this._fire.put(item, new FireData(owner, expireTime, delayTime, burnTime, damage, skillName));
        item.setPickupDelay(0);
    }
    
    @EventHandler
    public void IgniteCollide(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final HashMap<Item, LivingEntity> collided = new HashMap<Item, LivingEntity>();
        for (final Item fire : this._fire.keySet()) {
            if (!this._fire.get(fire).IsPrimed()) {
                continue;
            }
            if (fire.getLocation().getBlock().isLiquid()) {
                collided.put(fire, null);
            }
            else {
                for (final Object object : fire.getWorld().getEntitiesByClass((Class)LivingEntity.class)) {
                    LivingEntity ent = (LivingEntity)object;
                    if (ent instanceof Player && ((CraftPlayer)ent).getHandle().isInvisible()) {
                            continue;
                    }
                    if (ent.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                        continue;
                    }
                    if (ent.getLocation().getBlock().getTypeId() == 8) {
                        continue;
                    }
                    if (ent.getLocation().getBlock().getTypeId() == 9) {
                        continue;
                    }
                    if (ent.equals(this._fire.get(fire).GetOwner())) {
                        continue;
                    }
                    if (this._BufferManager.HasBuffer(ent, Buffer.BufferType.FIRE_ITEM_IMMUNITY, null)) {
                        continue;
                    }
                    if (!UtilEnt.hitBox(fire.getLocation(), ent, 1.5, null)) {
                        continue;
                    }
                    collided.put(fire, ent);
                }
            }
        }
        for (final Item fire : collided.keySet()) {
            final FireData fireData = this._fire.remove(fire);
            fire.remove();
            this.Ignite(collided.get(fire), fireData);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void IgnitePickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        final Item fire = event.getItem();
        if (!this._fire.containsKey(fire)) {
            return;
        }
        event.setCancelled(true);
        if (((CraftPlayer)player).getHandle().isInvisible()) {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            return;
        }
        if (player.getLocation().getBlock().getTypeId() == 8 || player.getLocation().getBlock().getTypeId() == 9) {
            return;
        }
        if (!this._fire.get(fire).IsPrimed()) {
            return;
        }
        if (this._BufferManager.HasBuffer((LivingEntity)player, Buffer.BufferType.FIRE_ITEM_IMMUNITY, null)) {
            return;
        }
        if (!UtilEnt.hitBox(fire.getLocation(), (LivingEntity)player, 1.5, null)) {
            return;
        }
        final FireData fireData = this._fire.remove(fire);
        fire.remove();
        this.Ignite((LivingEntity)player, fireData);
    }
    
    @EventHandler
    public void HopperPickup(final InventoryPickupItemEvent event) {
        if (this._fire.containsKey(event.getItem())) {
            event.setCancelled(true);
        }
    }
    
    public void Ignite(final LivingEntity ent, final FireData fireData) {
        if (ent == null) {
            return;
        }
        this._BufferManager.Factory().Ignite(fireData.GetName(), ent, fireData.GetOwner(), fireData.GetBurnTime(), true, true);
        if (fireData.GetDamage() > 0) {
            if (fireData.GetDamage() == 1 && ent instanceof Player && !Recharge.Instance.use((Player)ent, "Fire Damage", 150L, false, false)) {
                ent.playEffect(EntityEffect.HURT);
                return;
            }
            this._damageManager.NewDamageEvent(ent, fireData.GetOwner(), null, EntityDamageEvent.DamageCause.CUSTOM, fireData.GetDamage(), false, true, false, UtilEnt.getName((Entity)fireData.GetOwner()), fireData.GetName());
        }
    }
    
    @EventHandler
    public void Expire(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final HashSet<Item> expire = new HashSet<Item>();
        for (final Item cur : this._fire.keySet()) {
            if (!cur.isValid() || this._fire.get(cur).Expired()) {
                expire.add(cur);
            }
        }
        for (final Item cur : expire) {
            this._fire.remove(cur);
            cur.remove();
        }
    }
    
    public void Remove(final LivingEntity owner, final String cause) {
        final HashSet<Item> remove = new HashSet<Item>();
        for (final Item cur : this._fire.keySet()) {
            if ((owner == null || this._fire.get(cur).GetOwner().equals(owner)) && (cause == null || this._fire.get(cur).GetName().equals(cause))) {
                remove.add(cur);
            }
        }
        for (final Item cur : remove) {
            this._fire.remove(cur);
            cur.remove();
        }
    }
    
    public void RemoveNear(final Location loc, final double range) {
        final HashSet<Item> remove = new HashSet<>();
        for (final Item cur : this._fire.keySet()) {
            if (UtilMath.offset(loc, cur.getLocation()) < range) {
                remove.add(cur);
            }
        }
        for (final Item cur : remove) {
            this._fire.remove(cur);
            cur.getWorld().playEffect(cur.getLocation(), Effect.EXTINGUISH, 0);
            cur.remove();
        }
    }
}
