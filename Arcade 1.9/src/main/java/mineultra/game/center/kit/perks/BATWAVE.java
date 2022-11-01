package mineultra.game.center.kit.perks;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import java.util.HashSet;
import org.bukkit.entity.Bat;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilParticle;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.kit.Perk;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.Material;

public class BATWAVE extends Perk
{
    private HashMap<Player, Long> _active;
    private HashMap<Player, Location> _direction;
    private HashMap<Player, ArrayList<Bat>> _bats;
    private HashSet<Player> _pulling;
    private HashSet<Player> _allowLeash;
    
    public BATWAVE() {
        super("Bat Wave", new String[] { String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " to use " + C.cGreen + "Bat Wave", String.valueOf(C.cYellow) + "Double Right-Click" + C.cGray + " to use " + C.cGreen + "Bat Leash" });
        this._active = new HashMap<Player, Long>();
        this._direction = new HashMap<Player, Location>();
        this._bats = new HashMap<Player, ArrayList<Bat>>();
        this._pulling = new HashSet<Player>();
        this._allowLeash = new HashSet<Player>();
    }
    
    @EventHandler
    public void Deactivate(final CustomDamageEvent event) {
        final Player player = event.GetDamageePlayer();
        if (player == null) {
            return;
        }
        if (this._pulling.remove(player)) {
            for (final Bat bat : this._bats.get(player)) {
                bat.setLeashHolder((Entity)null);
            }
        }
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
        if (!(event.getPlayer().getItemInHand().getType() == Material.COAL)) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, this.GetName(), 8000L, false, true)) {
            if (this._active.containsKey(player)) {
                if (!Recharge.Instance.use(player, "Leash Bats", 500L, false, false)) {
                    return;
                }
                if (!this._pulling.remove(player)) {
                    if (this._allowLeash.remove(player)) {
                        this._pulling.add(player);
                        for (final Bat bat : this._bats.get(player)) {
                            bat.setLeashHolder((Entity)player);
                        }
                    }
                }
                else {
                    for (final Bat bat : this._bats.get(player)) {
                        bat.setLeashHolder((Entity)null);
                    }
                }
            }
            else {
                Recharge.Instance.use(player, this.GetName(), 8000L, true, true);
            }
        }
        else {
            this._direction.put(player, player.getEyeLocation());
            this._active.put(player, System.currentTimeMillis());
            this._allowLeash.add(player);
            this._bats.put(player, new ArrayList<Bat>());
            for (int i = 0; i < 32; ++i) {
                this.Manager.GetGame().CreatureAllowOverride = true;
                final Bat bat2 = (Bat)player.getWorld().spawn(player.getEyeLocation(), (Class)Bat.class);
                this._bats.get(player).add(bat2);
                this.Manager.GetGame().CreatureAllowOverride = false;
            }
            UtilPlayer.message((Entity)player, F.main("Skill", "You used " + F.skill(this.GetName()) + "."));
        }
    }
    
    @EventHandler
    public void Update(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (this._active.containsKey(cur)) {
                if (UtilTime.elapsed(this._active.get(cur), 3000L)) {
                    this.Clear(cur);
                }
                else {
                    final Location loc = this._direction.get(cur);
                    final Vector batVec = new Vector(0, 0, 0);
                    double batCount = 0.0;
                    for (final Bat bat : this._bats.get(cur)) {
                        if (!bat.isValid()) {
                            continue;
                        }
                        batVec.add(bat.getLocation().toVector());
                        ++batCount;
                        final Vector rand = new Vector((Math.random() - 0.5) / 2.0, (Math.random() - 0.5) / 2.0, (Math.random() - 0.5) / 2.0);
                        bat.setVelocity(loc.getDirection().clone().multiply(0.5).add(rand));
                        for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
                            if (other.equals(cur)) {
                                continue;
                            }
                            if (!Recharge.Instance.usable(other, "Hit by Bat")) {
                                continue;
                            }
                            if (!UtilEnt.hitBox(bat.getLocation(), (LivingEntity)other, 2.0, null)) {
                                continue;
                            }
                            this.Manager.GetDamage().NewDamageEvent((LivingEntity)other, (LivingEntity)cur, null, EntityDamageEvent.DamageCause.CUSTOM, 2.5, true, true, false, cur.getName(), this.GetName());
                            bat.getWorld().playSound(bat.getLocation(), Sound.ENTITY_BAT_HURT, 1.0f, 1.0f);
                            bat.remove();
                            Recharge.Instance.useForce(other, "Hit by Bat", 200L);
                        }
                    }
                    if (this._pulling.contains(cur)) {
                        batVec.multiply(1.0 / batCount);
                        final Location batLoc = batVec.toLocation(cur.getWorld());
                        UtilAction.velocity((Entity)cur, UtilAlg.getTrajectory(cur.getLocation(), batLoc), 0.5, false, 0.0, 0.0, 10.0, false);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void PlayerQuit(final PlayerQuitEvent event) {
        this.Clear(event.getPlayer());
    }
    
    @EventHandler
    public void PlayerDeath(final PlayerDeathEvent event) {
        this.Clear(event.getEntity());
    }
    
    public void Clear(final Player player) {
        this._active.remove(player);
        this._direction.remove(player);
        this._pulling.remove(player);
        if (this._bats.containsKey(player)) {
            for (final Bat bat : this._bats.get(player)) {
                bat.remove();
            }
            this._bats.remove(player);
        }
    }
    
    @EventHandler
    public void Knockback(final CustomDamageEvent event) {
        if (event.GetReason() == null || !event.GetReason().contains(this.GetName())) {
            return;
        }
        event.AddKnockback(this.GetName(), 2.25);
    }
}
