package mineultra.minecraft.game.core.Buffer;

import java.util.Iterator;
import org.bukkit.EntityEffect;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.Buffer.Buffer.BufferType;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;

public class BufferEffect implements Listener
{
    protected BufferManager Manager;
    
    public BufferEffect(final BufferManager manager) {
        super();
        this.Manager = manager;
        this.Manager.GetPlugin().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.Manager.GetPlugin());
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void Invulnerable(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        if (!this.Manager.IsInvulnerable(ent)) {
            return;
        }
        event.SetCancelled("Invulnerable");
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void Cloak(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        if (!this.Manager.IsCloaked(ent)) {
            return;
        }
        event.SetCancelled("Cloak");
    }
    
    @EventHandler
    public void Cloak(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        for (final LivingEntity ent : this.Manager.GetActiveBuffers().keySet()) {
            if (!(ent instanceof Player)) {
                continue;
            }
            final Player player = (Player)ent;
            if (this.Manager.IsCloaked(ent)) {

                      for (Iterator<? extends Player> it = Bukkit.getOnlinePlayers().iterator();it.hasNext();) {
                    final Player other = it.next();
                    ((CraftPlayer)other).hidePlayer(player);
                }
            }
            else {

        for (Iterator<? extends Player> it = Bukkit.getOnlinePlayers().iterator();it.hasNext();) {
                    final Player other = it.next();
                    other.showPlayer(player);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void Cloak(final EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) {
            return;
        }
        if (!this.Manager.HasBuffer((LivingEntity)event.getTarget(), Buffer.BufferType.CLOAK, null)) {
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void Protection(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damagee = event.GetDamageePlayer();
        if (damagee == null) {
            return;
        }
        if (!damagee.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            return;
        }
        final Buffer cond = this.Manager.GetActiveBuffer((LivingEntity)damagee, Buffer.BufferType.DAMAGE_RESISTANCE);
        if (cond == null) {
            return;
        }
        event.AddMod(UtilEnt.getName((Entity)cond.GetSource()), cond.GetReason(), -1 * (cond.GetMult() + 1), false);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void VulnerabilityDamagee(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damagee = event.GetDamageePlayer();
        if (damagee == null) {
            return;
        }
        if (!damagee.hasPotionEffect(PotionEffectType.WITHER)) {
            return;
        }
        final Buffer cond = this.Manager.GetActiveBuffer((LivingEntity)damagee, Buffer.BufferType.WITHER);
        if (cond == null) {
            return;
        }
        event.AddMod(UtilEnt.getName((Entity)cond.GetSource()), cond.GetReason(), cond.GetMult() + 1, false);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void VulnerabilityDamager(final CustomDamageEvent event) {
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
        if (!damager.hasPotionEffect(PotionEffectType.WITHER)) {
            return;
        }
        final Buffer cond = this.Manager.GetActiveBuffer((LivingEntity)damager, Buffer.BufferType.WITHER);
        if (cond == null) {
            return;
        }
        event.AddMod(UtilEnt.getName((Entity)cond.GetSource()), cond.GetReason(), -1 * (cond.GetMult() + 1), false);
    }
    
    @EventHandler
    public void VulnerabilityEffect(final UpdateEvent event) {
        if (event.getType() != UpdateType.FASTER) {
            return;
        }
        for (final LivingEntity ent : this.Manager.GetActiveBuffers().keySet()) {
            if (ent.isDead()) {
                continue;
            }
            if (!ent.hasPotionEffect(PotionEffectType.WITHER)) {
                continue;
            }
            if (this.Manager.HasBuffer(ent, Buffer.BufferType.CLOAK, null)) {
                continue;
            }
            ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 1);
            ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 3);
            ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 5);
            ent.getWorld().playEffect(ent.getLocation(), Effect.SMOKE, 7);
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void VulnerabilityWitherCancel(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() == EntityDamageEvent.DamageCause.WITHER) {
            event.SetCancelled("Vulnerability Wither");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void Strength(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(true);
        if (damager == null) {
            return;
        }
        if (!damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            return;
        }
        final Buffer cond = this.Manager.GetActiveBuffer((LivingEntity)damager, Buffer.BufferType.INCREASE_DAMAGE);
        if (cond == null) {
            return;
        }
        event.AddMod(damager.getName(), cond.GetReason(), cond.GetMult() + 1, true);
    }
    
    @EventHandler
    public void Shock(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        for (final LivingEntity ent : this.Manager.GetActiveBuffers().keySet()) {
            for (final BufferActive ind : this.Manager.GetActiveBuffers().get(ent)) {
                if (ind.GetBuffer().GetType() == Buffer.BufferType.SHOCK) {
                    ent.playEffect(EntityEffect.HURT);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void Lightning(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.LIGHTNING) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        final Buffer Buffer = this.Manager.GetActiveBuffer(ent,  BufferType.LIGHTNING);
        if (Buffer == null) {
            return;
        }
        event.SetDamager(Buffer.GetSource());
        event.AddMod(UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetReason(), 0.0, true);
        if (Buffer.GetMult() != 0) {
            event.AddMod("Lightning Modifier", UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetMult(), false);
        }
        event.SetKnockback(false);
    }
    
    @EventHandler
    public void Explosion(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.GetCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        final Buffer Buffer = this.Manager.GetActiveBuffer(ent, BufferType.EXPLOSION);
        if (Buffer == null) {
            return;
        }
        event.SetDamager(Buffer.GetSource());
        event.AddMod("Negate", Buffer.GetReason(), -event.GetDamageInitial(), false);
        event.AddMod(UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetReason(), Math.min(event.GetDamageInitial(), Buffer.GetMult()), true);
        event.SetKnockback(false);
    }
    
    @EventHandler
    public void Fire(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        if (ent.getFireTicks() > 160) {
            ent.setFireTicks(160);
        }
        final Buffer Buffer = this.Manager.GetActiveBuffer(ent, BufferType.BURNING);
        if (Buffer == null) {
            return;
        }
        event.SetDamager(Buffer.GetSource());
        event.AddMod(UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetReason(), 0.0, true);
        event.SetIgnoreArmor(true);
        event.SetKnockback(false);
    }
    
    @EventHandler
    public void FireDouse(final UpdateEvent event) {
        if (event.getType() != UpdateType.FASTER) {
            return;
        }
        for (final LivingEntity ent : this.Manager.GetActiveBuffers().keySet()) {
            if (ent.getFireTicks() <= 0) {
                this.Manager.EndBuffer(ent, Buffer.BufferType.BURNING, null);
            }
        }
    }
    
    @EventHandler
    public void Poison(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.POISON) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        final Buffer Buffer = this.Manager.GetActiveBuffer(ent, BufferType.POISON);
        if (Buffer == null) {
            return;
        }
        event.SetDamager(Buffer.GetSource());
        event.AddMod(UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetReason(), 0.0, true);
        event.SetIgnoreArmor(true);
        event.SetKnockback(false);
    }
    
    @EventHandler
    public void Fall(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        final LivingEntity ent = event.GetDamageeEntity();
        if (ent == null) {
            return;
        }
        final Buffer Buffer = this.Manager.GetActiveBuffer(ent, BufferType.FALLING);
        if (Buffer == null) {
            return;
        }
        event.SetDamager(Buffer.GetSource());
        event.AddMod(UtilEnt.getName((Entity)Buffer.GetSource()), Buffer.GetReason(), 0.0, true);
        event.SetIgnoreArmor(true);
        event.SetKnockback(false);
    }
    
    @EventHandler
    public void Fall(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        for (final LivingEntity ent : this.Manager.GetActiveBuffers().keySet()) {
            if (!UtilEnt.isGrounded((Entity)ent)) {
                continue;
            }
            final Buffer Buffer = this.Manager.GetActiveBuffer(ent, BufferType.FALLING);
            if (Buffer == null) {
                return;
            }
            if (!UtilTime.elapsed(Buffer.GetTime(), 250L)) {
                continue;
            }
            this.Manager.EndBuffer(ent, BufferType.FALLING, null);
        }
    }
}
