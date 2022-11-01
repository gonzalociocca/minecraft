package mineultra.game.center.kit.perks;

import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import mineultra.game.center.kit.perks.data.FireflyData;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilParticle;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;

public class FLAMEDASH extends Perk
{
    private final HashSet<FireflyData> _data;
    
    public FLAMEDASH() {
        super("Flame Dash", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with sword to use " + C.cGreen + "Flame Dash" });
        this._data = new HashSet<>();
    }
    
    @EventHandler
    public void Skill(final PlayerInteractEvent event) {
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
        if (!event.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("_sword")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.usable(player, this.GetName())) {
            boolean done = false;
            for (final FireflyData data : this._data) {
                if (data.Player.equals(player)) {
                    data.Time = 0L;
                    done = true;
                }
            }
            if (done) {
                UtilPlayer.message((Entity)player, F.main("Skill", "You ended " + F.skill(this.GetName()) + "."));
                this.UpdateMovement();
            }
            else {
                Recharge.Instance.use(player, this.GetName(), 8000L, true, true);
            }
            return;
        }
        Recharge.Instance.useForce(player, this.GetName(), 8000L);
        this._data.add(new FireflyData(player));
        this.Manager.GetBuffer().Factory().Invisible(this.GetName(), (LivingEntity)player, (LivingEntity)player, 2.5, 0, false, false, true);
        UtilPlayer.message((Entity)player, F.main("Skill", "You used " + F.skill(this.GetName()) + "."));
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        this.UpdateMovement();
    }
    
    public void UpdateMovement() {
        final Iterator<FireflyData> dataIterator = this._data.iterator();
        while (dataIterator.hasNext()) {
            final FireflyData data = dataIterator.next();
            if (!UtilTime.elapsed(data.Time, 800L)) {
                final Vector vel = data.Location.getDirection();
                vel.setY(0);
                vel.normalize();
                vel.setY(0.05);
                data.Player.setVelocity(vel);
                data.Player.getWorld().playSound(data.Player.getLocation(), Sound.FIZZ, 0.6f, 1.2f);
            }
            else {
                for (final Player other : UtilPlayer.getNearby(data.Player.getLocation(), 3.0)) {
                    if (other.equals(data.Player)) {
                        continue;
                    }
                    if (!this.Manager.GetGame().IsAlive(other)) {
                        continue;
                    }
                    final double dist = UtilMath.offset(data.Player.getLocation(), data.Location) / 2.0;
                    this.Manager.GetDamage().NewDamageEvent((LivingEntity)other, (LivingEntity)data.Player, null, EntityDamageEvent.DamageCause.CUSTOM, 2.0 + dist, true, true, false, data.Player.getName(), this.GetName());
                    UtilPlayer.message((Entity)other, F.main("Game", String.valueOf(F.elem(new StringBuilder().append(this.Manager.GetColor(data.Player)).append(data.Player.getName()).toString())) + " hit you with " + F.elem(this.GetName()) + "."));
                }
                this.Manager.GetBuffer().EndBuffer((LivingEntity)data.Player, null, this.GetName());
                data.Player.getWorld().playSound(data.Player.getLocation(), Sound.EXPLODE, 1.0f, 1.2f);
                dataIterator.remove();
            }
        }
    }
    
    @EventHandler
    public void Knockback(final CustomDamageEvent event) {
        if (event.GetReason() == null || !event.GetReason().contains(this.GetName())) {
            return;
        }
        event.AddKnockback(this.GetName(), 2.0);
    }
}
