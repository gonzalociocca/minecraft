/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import org.bukkit.entity.Arrow;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.entity.Entity;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.data.ReboundData;

public class ARROWREBOUND extends Perk
{
    private HashMap<Entity, ReboundData> _arrows;
    private int _max;
    private float _maxPower;
    
    public ARROWREBOUND(final int max, final float maxPower) {
        super("Chain Arrows", new String[] { String.valueOf(C.cGray) + "On hit, arrows bounce to nearby enemies.", String.valueOf(C.cGray) + "Arrows bounce up to " + max + " times." });
        this._arrows = new HashMap<Entity, ReboundData>();
        this._max = 0;
        this._maxPower = 1.0f;
        this._max = max;
        this._maxPower = maxPower;
    }
    
    @EventHandler
    public void ShootBow(final EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        this._arrows.put(event.getProjectile(), new ReboundData(player, this._max, null));
    }
    
    @EventHandler
    public void Rebound(final ProjectileHitEvent event) {
        final ReboundData data = this._arrows.remove(event.getEntity());
        if (data == null) {
            return;
        }
        if (data.Bounces <= 0) {
            return;
        }
        final Location arrowLoc = event.getEntity().getLocation().add(event.getEntity().getVelocity());
        final Player hit = UtilPlayer.getClosest(arrowLoc, data.Ignore);
        if (hit == null) {
            return;
        }
        if (UtilMath.offset(hit.getLocation(), arrowLoc) > 1.0 && UtilMath.offset(hit.getEyeLocation(), arrowLoc) > 1.0) {
            return;
        }
        data.Ignore.add(hit);
        final Player target = UtilPlayer.getClosest(event.getEntity().getLocation().add(event.getEntity().getVelocity()), data.Ignore);
        if (target == null) {
            return;
        }
        final Vector trajectory = UtilAlg.getTrajectory((Entity)hit, (Entity)target);
        trajectory.add(new Vector(0.0, UtilMath.offset((Entity)hit, (Entity)target) / 100.0, 0.0));
        float power = (float)(0.8 + UtilMath.offset((Entity)hit, (Entity)target) / 30.0);
        if (this._maxPower > 0.0f && power > this._maxPower) {
            power = this._maxPower;
        }
        final Arrow ent = hit.getWorld().spawnArrow(hit.getEyeLocation().add(UtilAlg.getTrajectory((Entity)hit, (Entity)target)), trajectory, power, 0.0f);
        ent.setShooter((LivingEntity)data.Shooter);
        this._arrows.put((Entity)ent, new ReboundData(data.Shooter, data.Bounces - 1, data.Ignore));
    }
}
