/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.game.center.kit.perks;

import java.util.Iterator;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Chicken;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.perks.data.ChickenMissileData;

public class CHICKENROCKET extends Perk
{
    private final HashSet<ChickenMissileData> _data;
    
    public CHICKENROCKET() {
        super("Misil de patos", new String[] { String.valueOf(C.cYellow) + "Click derecho" + C.cGray + " con Hacha para usar" + C.cGreen + "Lanza Patos", String.valueOf(C.cGreen) + "LanzaPatos"  });
        this._data = new HashSet<>();
    }
    
    @EventHandler
    public void Missile(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (UtilBlock.usable(event.getClickedBlock())) {
            return;
        }
        if (event.getPlayer().getItemInHand() == null) {
            return;
        }
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 20000L, true, true)) {
            return;
        }
        this.Manager.GetGame().CreatureAllowOverride = true;
        final Chicken ent = (Chicken)player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), (Class)Chicken.class);
        ent.getLocation().setPitch(0.0f);
        ent.getLocation().setYaw(player.getLocation().getYaw());
        ent.setBaby();
        ent.setAgeLock(true);
        UtilEnt.Vegetate((Entity)ent);
        this.Manager.GetGame().CreatureAllowOverride = false;
        this._data.add(new ChickenMissileData(player, (Entity)ent));
        UtilPlayer.message((Entity)player, F.main("Game", "Usaste " + F.skill(this.GetName()) + "."));
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<ChickenMissileData> dataIterator = this._data.iterator();
        while (dataIterator.hasNext()) {
            final ChickenMissileData data = dataIterator.next();
            data.Chicken.setVelocity(data.Direction);
            data.Chicken.getWorld().playSound(data.Chicken.getLocation(), Sound.ENTITY_CHICKEN_HURT, 0.3f, 1.5f);
            if (!UtilTime.elapsed(data.Time, 200L)) {
                continue;
            }
            boolean detonate = false;
            if (UtilTime.elapsed(data.Time, 4000L)) {
                detonate = true;
            }
            else {
                for (final Entity ent : data.Player.getWorld().getEntities()) {
                    if (ent instanceof Arrow && ((Arrow)ent).isOnGround()) {
                        continue;
                    }
                    if (ent.equals(data.Player)) {
                        continue;
                    }
                    if (ent.equals(data.Chicken)) {
                        continue;
                    }
                    if (UtilMath.offset(data.Chicken.getLocation(), ent.getLocation().add(0.0, 0.5, 0.0)) > 2.0) {
                        continue;
                    }
                    if (ent instanceof Player && !this.Manager.GetGame().IsAlive((Player)ent)) {
                        continue;
                    }

                    detonate = true;
                    break;
                }
                if (!detonate && data.HasHitBlock()) {
                    detonate = true;
                }
            }
            if (!detonate) {
                continue;
            }
            for (final Entity ent : data.Player.getWorld().getEntities()) {
                if (!(ent instanceof LivingEntity)) {
                    continue;
                }
                if (ent.equals(data.Player)) {
                    continue;
                }
                if (UtilMath.offset(data.Chicken.getLocation(), ent.getLocation().add(0.0, 0.5, 0.0)) > 3.0) {
                    continue;
                }
                if (ent instanceof Player && !this.Manager.GetGame().IsAlive((Player)ent)) {
                    continue;
                }
                final LivingEntity livingEnt = (LivingEntity)ent;
                this.Manager.GetDamage().NewDamageEvent(livingEnt, (LivingEntity)data.Player, null, EntityDamageEvent.DamageCause.PROJECTILE, 8.0, false, true, false, data.Player.getName(), this.GetName());
                UtilAction.velocity((Entity)livingEnt, UtilAlg.getTrajectory2d(data.Chicken, (Entity)livingEnt), 1.6, true, 0.8, 0.0, 10.0, true);
            }
            data.Chicken.getWorld().createExplosion(data.Chicken.getLocation().getX(),data.Chicken.getLocation().getY(),data.Chicken.getLocation().getZ(), 1.8f,false,false);

            data.Chicken.remove();
            dataIterator.remove();
        }
    }
}
