package mineultra.game.center.kit.perks;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Iterator;
import org.bukkit.util.Vector;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Projectile;
import java.util.HashSet;
import org.bukkit.entity.Player;
import java.util.WeakHashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class BARRAGE extends Perk
{
    private final WeakHashMap<Player, Integer> _charge;
    private final WeakHashMap<Player, Long> _chargeLast;
    private final HashSet<Player> _firing;
    private final HashSet<Projectile> _arrows;
    private final int _max;
    private final long _tick;
    private final boolean _remove;
    private final boolean _noDelay;
    
    public BARRAGE(final int max, final long tick, final boolean remove, final boolean noDelay) {
        super("Barrage", new String[] { String.valueOf(C.cYellow) + "Hold" + C.cGray + " your Bow to use " + C.cGreen + "Barrage" });
        this._charge = new WeakHashMap<>();
        this._chargeLast = new WeakHashMap<>();
        this._firing = new HashSet<>();
        this._arrows = new HashSet<>();
        this._max = max;
        this._tick = tick;
        this._remove = remove;
        this._noDelay = noDelay;
    }
    
    @EventHandler
    public void BarrageDrawBow(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.BOW) {
            return;
        }
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!player.getInventory().contains(Material.ARROW)) {
            return;
        }
        if (event.getClickedBlock() != null && UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        this._charge.put(player, 0);
        this._chargeLast.put(player, System.currentTimeMillis());
        this._firing.remove(player);
    }
    
    @EventHandler
    public void BarrageCharge(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (this._charge.containsKey(cur)) {
                if (!this._firing.contains(cur)) {
                    if (this._charge.get(cur) < this._max) {
                        if (this._charge.get(cur) == 0) {
                            if (!UtilTime.elapsed(this._chargeLast.get(cur), 1000L)) {
                                continue;
                            }
                        }
                        else if (!UtilTime.elapsed(this._chargeLast.get(cur), this._tick)) {
                            continue;
                        }
                        if (cur.getItemInHand() == null || cur.getItemInHand().getType() != Material.BOW) {
                            this._charge.remove(cur);
                            this._chargeLast.remove(cur);
                        }
                        else {
                            this._charge.put(cur, this._charge.get(cur) + 1);
                            this._chargeLast.put(cur, System.currentTimeMillis());
                            cur.playSound(cur.getLocation(), Sound.CLICK, 1.0f, 1.0f + 0.1f * this._charge.get(cur));
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void BarrageFireBow(final EntityShootBowEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!this.Manager.GetGame().IsLive()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getProjectile() instanceof Arrow)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (!this._charge.containsKey(player)) {
            return;
        }
        this._firing.add(player);
        this._chargeLast.put(player, System.currentTimeMillis());
    }
    
    @EventHandler
    public void BarrageArrows(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final HashSet<Player> remove = new HashSet<>();
        for (final Player cur : this._firing) {
            if (!this._charge.containsKey(cur) || !this._chargeLast.containsKey(cur)) {
                remove.add(cur);
            }
            else if (cur.getItemInHand() == null || cur.getItemInHand().getType() != Material.BOW) {
                remove.add(cur);
            }
            else {
                final int arrows = this._charge.get(cur);
                if (arrows <= 0) {
                    remove.add(cur);
                }
                else {
                    this._charge.put(cur, arrows - 1);
                    final Vector random = new Vector((Math.random() - 0.5) / 10.0, (Math.random() - 0.5) / 10.0, (Math.random() - 0.5) / 10.0);
                    final Projectile arrow = cur.launchProjectile((Class)Arrow.class);
                    arrow.setVelocity(cur.getLocation().getDirection().add(random).multiply(3));
                    this._arrows.add(arrow);
                    cur.getWorld().playSound(cur.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
                }
            }
        }
        for (final Player cur : remove) {
            this._charge.remove(cur);
            this._chargeLast.remove(cur);
            this._firing.remove(cur);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void BarrageDamageTime(final CustomDamageEvent event) {
        if (!this._noDelay) {
            return;
        }
        if (event.GetProjectile() == null) {
            return;
        }
        if (event.GetDamagerPlayer(true) == null) {
            return;
        }
        if (!(event.GetProjectile() instanceof Arrow)) {
            return;
        }
        final Player damager = event.GetDamagerPlayer(true);
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        event.SetCancelled("Barrage Cancel");
        event.GetProjectile().remove();
        this.Manager.GetDamage().NewDamageEvent(event.GetDamageeEntity(), (LivingEntity)damager, null, EntityDamageEvent.DamageCause.THORNS, event.GetDamage(), true, true, false, damager.getName(), this.GetName());
    }
    
    @EventHandler
    public void BarrageProjectileHit(final ProjectileHitEvent event) {
        if (this._remove && this._arrows.remove(event.getEntity())) {
            event.getEntity().remove();
        }
    }
    
    @EventHandler
    public void BarrageClean(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        final Iterator<Projectile> arrowIterator = this._arrows.iterator();
        while (arrowIterator.hasNext()) {
            final Projectile arrow = arrowIterator.next();
            if (arrow.isDead() || !arrow.isValid()) {
                arrowIterator.remove();
            }
        }
    }
    
    @EventHandler
    public void Quit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this._charge.remove(player);
        this._chargeLast.remove(player);
        this._firing.remove(player);
    }
}
