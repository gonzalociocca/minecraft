package mineultra.game.center.kit.perks;

import java.util.Iterator;
import org.bukkit.util.Vector;
import org.bukkit.entity.Projectile;
import org.bukkit.Sound;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Entity;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;

public class ROPEDARROW extends Perk
{
    private HashSet<Entity> _arrows;
    private String _name;
    private double _power;
    private long _recharge;
    
    public ROPEDARROW(final String name, final double power, final long recharge) {
        super(name, new String[] { String.valueOf(C.cYellow) + "Left-Click" + C.cGray + " with Bow to " + C.cGreen + name });
        this._arrows = new HashSet<Entity>();
        this._name = name;
        this._power = power;
        this._recharge = recharge;
    }
    
    @EventHandler
    public void Fire(final PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType() != Material.BOW) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this._name, this._recharge, true, true)) {
            return;
        }
        final Arrow arrow = (Arrow)player.launchProjectile((Class)Arrow.class);
        arrow.setVelocity(player.getLocation().getDirection().multiply(2.4 * this._power));
        this._arrows.add((Entity)arrow);
        UtilPlayer.message((Entity)player, F.main("Game", "You fired " + F.skill(this._name) + "."));
    }
    
    @EventHandler
    public void Hit(final ProjectileHitEvent event) {
        if (!this._arrows.remove(event.getEntity())) {
            return;
        }
        final Projectile proj = event.getEntity();
        if (proj.getShooter() == null) {
            return;
        }
        if (!(proj.getShooter() instanceof Player)) {
            return;
        }
        final Vector vec = UtilAlg.getTrajectory((Entity)proj.getShooter(), (Entity)proj);
        final double mult = proj.getVelocity().length() / 3.0;
        UtilAction.velocity((Entity)proj.getShooter(), vec, 0.4 + mult * this._power, false, 0.0, 0.6 * mult * this._power, 1.2 * mult * this._power, true);
        proj.getWorld().playSound(proj.getLocation(), Sound.ARROW_HIT, 2.5f, 0.5f);
    }
    
    @EventHandler
    public void Clean(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        final Iterator<Entity> arrowIterator = this._arrows.iterator();
        while (arrowIterator.hasNext()) {
            final Entity arrow = arrowIterator.next();
            if (!arrow.isValid()) {
                arrowIterator.remove();
            }
        }
    }
}