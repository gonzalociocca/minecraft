package mineultra.minecraft.game.core.mechanics;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilGear;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;

public class Weapon extends MiniPlugin
{
    
    public Weapon(final JavaPlugin plugin) {
        super("Weapon", plugin);
    }
    
    @EventHandler
    public void ShootBow(final EntityShootBowEvent event) {
        if (event.getEntity().getLocation().getBlock().isLiquid()) {
            UtilPlayer.message((Entity)event.getEntity(), F.main("Skill", "You cannot use " + F.item("Bow") + " in water."));
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void ArrowFix(final CustomDamageEvent event) {
        final Projectile proj = event.GetProjectile();
        if (proj == null) {
            return;
        }
        if (!(proj instanceof Arrow)) {
            return;
        }
        event.AddMod("Del", "Arrow Fix", -event.GetDamageInitial(), false);
        event.AddMod("Add", "Arrow Fix", proj.getVelocity().length() * 3.0, false);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void ArrowDelete(final CustomDamageEvent event) {
        if (event.GetCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        final Projectile proj = event.GetProjectile();
        if (proj == null) {
            return;
        }
        proj.remove();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void AttackExhaust(final CustomDamageEvent event) {
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (damager.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
            return;
        }
        event.AddMod(damager.getName(), "Exhaustion", -event.GetDamageInitial() + 1.0, false);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void WeaponDurability(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (this.GoldPower(damager)) {
            return;
        }
        final ItemStack item = damager.getItemInHand();
        if (item == null) {
            return;
        }
        if (item.getType().getMaxDurability() == 0) {
            return;
        }
        item.setDurability((short)(item.getDurability() + 1));
        if (item.getDurability() >= item.getType().getMaxDurability()) {
            UtilPlayer.message((Entity)damager, F.main("Weapon", "Your " + F.item(item.getItemMeta().getDisplayName()) + " has broken."));
            damager.setItemInHand((ItemStack)null);
            UtilInv.Update((Entity)damager);
            damager.getWorld().playSound(damager.getLocation(), Sound.ANVIL_LAND, 1.0f, 0.8f);
        }
    }
    
    private boolean GoldPower(final Player damager) {
        try {
            if (!UtilGear.isGold(damager.getItemInHand())) {
                return false;
            }
            if (!damager.getInventory().contains(Material.GOLD_NUGGET)) {
                return false;
            }
            UtilInv.remove(damager, Material.GOLD_NUGGET,(byte) 0, 1);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
