package mineultra.game.center.kit.perks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import java.util.Iterator;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import java.util.HashSet;
import org.bukkit.util.Vector;
import org.bukkit.entity.WitherSkull;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;
import org.bukkit.Material;

public class WITHERSKULL extends Perk
{
    private HashMap<WitherSkull, Vector> _active;
    private HashSet<Player> _ignoreControl;
    
    public WITHERSKULL() {
        super("Wither Skull", new String[] { String.valueOf(C.cYellow) + "Right-Click with spiderfood" + C.cGray + " to use " + C.cGreen + "Wither Skull" });
        this._active = new HashMap<WitherSkull, Vector>();
        this._ignoreControl = new HashSet<Player>();
    }
    
    @EventHandler
    public void Activate(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if (!event.getPlayer().getItemInHand().getType().equals(Material.FERMENTED_SPIDER_EYE)) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 1000L, true, true)) {
            return;
        }
        final WitherSkull skull = (WitherSkull)player.launchProjectile((Class)WitherSkull.class);
        skull.setDirection(player.getLocation().getDirection());
        this._active.put(skull, player.getLocation().getDirection().multiply(0.6));
        player.getWorld().playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.0f, 1.0f);
        UtilPlayer.message((Entity)player, F.main("Skill", "You launched " + F.skill(this.GetName()) + "."));
        this._ignoreControl.remove(player);
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<WitherSkull> skullIterator = this._active.keySet().iterator();
        while (skullIterator.hasNext()) {
            final WitherSkull skull = skullIterator.next();
            final Player player = (Player)skull.getShooter();
            if (!skull.isValid()) {
                skullIterator.remove();
                skull.remove();
            }
            else if (player.isBlocking() && !this._ignoreControl.contains(player)) {
                skull.setDirection(player.getLocation().getDirection());
                skull.setVelocity(player.getLocation().getDirection().multiply(0.6));
                this._active.put(skull, player.getLocation().getDirection().multiply(0.6));
            }
            else {
                this._ignoreControl.add(player);
                skull.setDirection((Vector)this._active.get(skull));
                skull.setVelocity((Vector)this._active.get(skull));
            }
        }
    }
    
    @EventHandler
    public void Explode(final EntityExplodeEvent event) {
        if (!this._active.containsKey(event.getEntity())) {
            return;
        }
        event.setCancelled(true);
        final WitherSkull skull = (WitherSkull)event.getEntity();
        this.Explode(skull, event.getLocation(), (LivingEntity) skull.getShooter());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void ExplodeDamage(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetProjectile() != null && event.GetProjectile() instanceof WitherSkull) {
            event.SetCancelled("Wither Skull Cancel");
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void DirectHitDamage(final CustomDamageEvent event) {
        if (event.IsCancelled()) {
            return;
        }
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (event.GetDamageInitial() != 7.0) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(false);
        if (damager == null) {
            return;
        }
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        if (!this.Manager.IsAlive(damager)) {
            return;
        }
        event.SetCancelled("Wither Skull Direct Hit");
    }
    
    private void Explode(final WitherSkull skull, final Location loc, final LivingEntity shooter) {
        final double scale = 0.4 + 0.6 * Math.min(1.0, skull.getTicksLived() / 20.0);
        for (final Entity ent : skull.getWorld().getEntities()) {
            if (!(ent instanceof LivingEntity)) {
                continue;
            }
            if (UtilMath.offset(loc, ent.getLocation()) > 2.0) {
                continue;
            }
            if (ent instanceof Player && !this.Manager.GetGame().IsAlive((Player)ent)) {
                continue;
            }
            final LivingEntity livingEnt = (LivingEntity)ent;
            this.Manager.GetDamage().NewDamageEvent(livingEnt, shooter, null, EntityDamageEvent.DamageCause.CUSTOM, 12.0 * scale, false, true, false, UtilEnt.getName((Entity)shooter), this.GetName());
            UtilAction.velocity((Entity)livingEnt, UtilAlg.getTrajectory2d(loc, livingEnt.getLocation()), 1.6 * scale, true, 0.8 * scale, 0.0, 10.0, true);
        }
        loc.getWorld().createExplosion(loc, 0.8f);
    }
}
