package mineultra.game.center.kit.perks;

import org.bukkit.event.EventPriority;
import org.bukkit.Effect;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.server.v1_8_R2.Navigation;
import net.minecraft.server.v1_8_R2.EntityCreature;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftCreature;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import java.util.HashMap;
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
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;
import net.minecraft.server.v1_8_R2.NavigationAbstract;

public class WOLF extends Perk
{
    private HashMap<Wolf, Player> _owner;
    private HashMap<Wolf, LivingEntity> _tackle;
    private HashMap<Player, Long> _strike;
    private HashMap<Player, ArrayList<Long>> _repeat;
    private HashMap<LivingEntity, Long> _tackleStrike;
    
    public WOLF() {
        super("Wolf Skills", new String[] { String.valueOf(C.cGray) + "Attacks give +1 Damage for 3 seconds. Stacks.", String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Cub Tackle", String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Wolf Strike", String.valueOf(C.cGray) + "Wolf Strike deals 200% Knockback to tackled opponents." });
        this._owner = new HashMap<Wolf, Player>();
        this._tackle = new HashMap<Wolf, LivingEntity>();
        this._strike = new HashMap<Player, Long>();
        this._repeat = new HashMap<Player, ArrayList<Long>>();
        this._tackleStrike = new HashMap<LivingEntity, Long>();
    }
    
    @EventHandler
    public void TackleTrigger(final PlayerInteractEvent event) {
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
        if (!Recharge.Instance.use(player, "Cub Tackle", 8000L, true, true)) {
            return;
        }
        this.Manager.GetGame().CreatureAllowOverride = true;
        final Wolf wolf = (Wolf)player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), (Class)Wolf.class);
        this.Manager.GetGame().CreatureAllowOverride = false;
        wolf.setBaby();
        wolf.setAngry(true);
        UtilEnt.Vegetate((Entity)wolf);
        wolf.setMaxHealth(30.0);
        wolf.setHealth(wolf.getMaxHealth());
        UtilAction.velocity((Entity)wolf, player.getLocation().getDirection(), 1.8, false, 0.0, 0.2, 1.2, true);
        player.getWorld().playSound(wolf.getLocation(), Sound.WOLF_BARK, 1.0f, 1.8f);
        this._owner.put(wolf, player);
        UtilPlayer.message((Entity)player, F.main("Game", "You used " + F.skill("Cub Tackle") + "."));
    }
    
    @EventHandler
    public void TackleCollide(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<Wolf> wolfIterator = this._owner.keySet().iterator();
        while (wolfIterator.hasNext()) {
            final Wolf wolf = wolfIterator.next();
            for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
                if (other.getGameMode() == GameMode.SURVIVAL && UtilEnt.hitBox(wolf.getLocation(), (LivingEntity)other, 2.0, null)) {
                    if (other.equals(this.TackleGetOwner(wolf))) {
                        continue;
                    }
                    this.TackleCollideAction(this.TackleGetOwner(wolf), (LivingEntity)other, wolf);
                    wolfIterator.remove();
                    return;
                }
            }
            if (!wolf.isValid() || (UtilEnt.isGrounded((Entity)wolf) && wolf.getTicksLived() > 20)) {
                wolf.remove();
                wolfIterator.remove();
            }
        }
    }
    
    public void TackleCollideAction(final Player damager, final LivingEntity damagee, final Wolf wolf) {
        if (damager == null) {
            return;
        }
        this._tackle.put(wolf, damagee);
        wolf.setVelocity(new Vector(0.0, -0.6, 0.0));
        damagee.setVelocity(new Vector(0, 0, 0));
        this.Manager.GetDamage().NewDamageEvent(damagee, (LivingEntity)damager, null, EntityDamageEvent.DamageCause.CUSTOM, 5.0, false, true, false, damager.getName(), "Cub Tackle");
        damagee.getWorld().playSound(damagee.getLocation(), Sound.WOLF_GROWL, 1.5f, 1.5f);
        UtilPlayer.message((Entity)damager, F.main("Game", "You hit " + F.name(UtilEnt.getName((Entity)damagee)) + " with " + F.skill("Cub Tackle") + "."));
        UtilPlayer.message((Entity)damagee, F.main("Game", String.valueOf(F.name(damager.getName())) + " hit you with " + F.skill("Cub Tackle") + "."));
    }
    
    @EventHandler
    public void TackleUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<Wolf> wolfIterator = this._tackle.keySet().iterator();
        while (wolfIterator.hasNext()) {
            final Wolf wolf = wolfIterator.next();
            final LivingEntity ent = this._tackle.get(wolf);
            if (!wolf.isValid() || !ent.isValid() || wolf.getTicksLived() > 100) {
                wolf.remove();
                wolfIterator.remove();
            }
            else {
                if (UtilMath.offset((Entity)wolf, (Entity)ent) < 2.5) {
                    this.Manager.GetBuffer().Factory().Slow("Cub Table", ent, (LivingEntity)wolf, 0.9, 1, false, false, false, false);
                    ent.setVelocity(new Vector(0.0, -0.3, 0.0));
                }
                final Location loc = ent.getLocation();
                loc.add(UtilAlg.getTrajectory2d((Entity)ent, (Entity)wolf).multiply(1));
                final EntityCreature ec = ((CraftCreature)wolf).getHandle();
                final NavigationAbstract nav = ec.getNavigation();
                nav.a(loc.getX(), loc.getY(), loc.getZ(), 1.0);
            }
        }
    }
    
    public Player TackleGetOwner(final Wolf wolf) {
        if (this._owner.containsKey(wolf)) {
            return this._owner.get(wolf);
        }
        return null;
    }
    
    @EventHandler
    public void TackleTargetCancel(final EntityTargetEvent event) {
        if (this._owner.containsKey(event.getEntity()) && this._owner.get(event.getEntity()).equals(event.getTarget())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void TackleDamage(final CustomDamageEvent event) {
        if (event.GetCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final LivingEntity damager = event.GetDamagerEntity(false);
        if (damager == null) {
            return;
        }
        if (damager instanceof Wolf) {
            event.SetCancelled("Wolf Cub");
        }
    }
    
    @EventHandler
    public void StrikeTrigger(final PlayerInteractEvent event) {
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
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_SPADE")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!Recharge.Instance.use(player, "Wolf Strike", 8000L, true, true)) {
            return;
        }
        UtilAction.velocity((Entity)player, player.getLocation().getDirection(), 1.6, false, 1.0, 0.2, 1.2, true);
        this._strike.put(player, System.currentTimeMillis());
        player.getWorld().playSound(player.getLocation(), Sound.WOLF_BARK, 1.0f, 1.2f);
        UtilPlayer.message((Entity)player, F.main("Game", "You used " + F.skill("Wolf Strike") + "."));
    }
    
    @EventHandler
    public void StrikeEnd(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<Player> playerIterator = this._strike.keySet().iterator();
        while (playerIterator.hasNext()) {
            final Player player = playerIterator.next();
            for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
                if (!player.equals(other) && other.getGameMode() == GameMode.SURVIVAL && UtilEnt.hitBox(player.getLocation().add(0.0, 1.0, 0.0), (LivingEntity)other, 2.0, null)) {
                    this.StrikeHit(player, (LivingEntity)other);
                    playerIterator.remove();
                    return;
                }
            }
            if (!UtilEnt.isGrounded((Entity)player)) {
                continue;
            }
            if (!UtilTime.elapsed(this._strike.get(player), 1500L)) {
                continue;
            }
            playerIterator.remove();
        }
    }
    
    public void StrikeHit(final Player damager, final LivingEntity damagee) {
        damager.setVelocity(new Vector(0, 0, 0));
        final Iterator<Wolf> wolfIterator = this._tackle.keySet().iterator();
        while (wolfIterator.hasNext()) {
            final Wolf wolf = wolfIterator.next();
            if (this._tackle.get(wolf).equals(damagee)) {
                wolf.remove();
                wolfIterator.remove();
                this._tackleStrike.put(damagee, System.currentTimeMillis());
            }
        }
        this.Manager.GetDamage().NewDamageEvent(damagee, (LivingEntity)damager, null, EntityDamageEvent.DamageCause.CUSTOM, 7.0, true, true, false, damager.getName(), "Wolf Strike");
        damagee.getWorld().playSound(damagee.getLocation(), Sound.WOLF_BARK, 1.5f, 1.0f);
        UtilPlayer.message((Entity)damager, F.main("Game", "You hit " + F.name(UtilEnt.getName((Entity)damagee)) + " with " + F.skill("Wolf Strike") + "."));
        UtilPlayer.message((Entity)damagee, F.main("Game", String.valueOf(F.name(damager.getName())) + " hit you with " + F.skill("Wolf Strike") + "."));
    }
    
    @EventHandler
    public void StrikeKnockback(final CustomDamageEvent event) {
        if (event.GetReason() != null && event.GetReason().contains("Wolf Strike")) {
            if (this._tackleStrike.containsKey(event.GetDamageeEntity()) && !UtilTime.elapsed(this._tackleStrike.get(event.GetDamageeEntity()), 100L)) {
                event.AddKnockback(this.GetName(), 3.0);
                event.GetDamageeEntity().getWorld().playEffect(event.GetDamageeEntity().getLocation(), Effect.STEP_SOUND, 55);
                event.GetDamageeEntity().getWorld().playSound(event.GetDamageeEntity().getLocation(), Sound.WOLF_BARK, 2.0f, 1.5f);
            }
            else {
                event.AddKnockback(this.GetName(), 1.5);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void RepeatDamage(final CustomDamageEvent event) {
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
        if (!this.Kit.HasKit(damager)) {
            return;
        }
        if (!this._repeat.containsKey(damager)) {
            this._repeat.put(damager, new ArrayList<Long>());
            this._repeat.get(damager).add(System.currentTimeMillis());
            damager.setExp(Math.min(0.9999f, this._repeat.get(damager).size() / 9.0f));
            return;
        }
        final int count = this._repeat.get(damager).size();
        if (count > 0) {
            event.AddMod(damager.getName(), "Ravage", count, false);
            damager.getWorld().playSound(damager.getLocation(), Sound.WOLF_BARK, (float)(0.5 + count * 0.25), (float)(1.0 + count * 0.25));
        }
        this._repeat.get(damager).add(System.currentTimeMillis());
        damager.setExp(Math.min(0.9999f, this._repeat.get(damager).size() / 9.0f));
    }
    
    @EventHandler
    public void RepeatExpire(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        for (final Player player : this._repeat.keySet()) {
            final Iterator<Long> timeIterator = this._repeat.get(player).iterator();
            while (timeIterator.hasNext()) {
                final long time = timeIterator.next();
                if (UtilTime.elapsed(time, 3000L)) {
                    timeIterator.remove();
                }
            }
            player.setExp(Math.min(0.9999f, this._repeat.get(player).size() / 9.0f));
        }
    }
}
