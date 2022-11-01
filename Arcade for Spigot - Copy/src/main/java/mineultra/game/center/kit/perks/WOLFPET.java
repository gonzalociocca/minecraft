package mineultra.game.center.kit.perks;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import net.minecraft.server.v1_8_R2.Navigation;
import net.minecraft.server.v1_8_R2.EntityCreature;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftCreature;
import net.minecraft.server.v1_8_R2.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftWolf;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.EntityEffect;
import org.bukkit.DyeColor;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Wolf;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.util.HashMap;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.recharge.Recharge;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.kit.Perk;
import net.minecraft.server.v1_8_R2.NavigationAbstract;

public class WOLFPET extends Perk
{
    private HashMap<Player, ArrayList<Wolf>> _wolfMap;
    private HashMap<Wolf, Long> _tackle;
    private int _spawnRate;
    private int _max;
    private boolean _baby;
    private boolean _name;
    
    public WOLFPET(final int spawnRate, final int max, final boolean baby, final boolean name) {
        super("Wolf Master", new String[] { String.valueOf(C.cGray) + "Spawnea un lobo cada " + spawnRate + " seconds. Maximo " + max + ".", String.valueOf(C.cYellow) + "Right-Click" + C.cGray + " Con espada/hacha para usar " + C.cGreen + "Wolf Tackle" });
        this._wolfMap = new HashMap<Player, ArrayList<Wolf>>();
        this._tackle = new HashMap<Wolf, Long>();
        this._spawnRate = spawnRate;
        this._max = max;
        this._baby = baby;
        this._name = name;
    }
    
    @Override
    public void Apply(final Player player) {
        Recharge.Instance.use(player, this.GetName(), this._spawnRate * 1000, false, false);
    }
    
    @EventHandler
    public void CubSpawn(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player cur = players[i];
            if (this.Kit.HasKit(cur)) {
                if (this.Manager.GetGame().IsAlive(cur)) {
                    if (Recharge.Instance.use(cur, this.GetName(), this._spawnRate * 1000, false, false)) {
                        if (!this._wolfMap.containsKey(cur)) {
                            this._wolfMap.put(cur, new ArrayList<Wolf>());
                        }
                        if (this._wolfMap.get(cur).size() < this._max) {
                            this.Manager.GetGame().CreatureAllowOverride = true;
                            final Wolf wolf = (Wolf)cur.getWorld().spawn(cur.getLocation(), (Class)Wolf.class);
                            this.Manager.GetGame().CreatureAllowOverride = false;
                            wolf.setOwner((AnimalTamer)cur);
                            wolf.setCollarColor(DyeColor.GREEN);
                            wolf.playEffect(EntityEffect.WOLF_HEARTS);
                            wolf.setMaxHealth(18.0);
                            wolf.setHealth(wolf.getMaxHealth());
                            if (this._baby) {
                                wolf.setBaby();
                            }
                            if (this._name) {
                                wolf.setCustomName(String.valueOf(cur.getName()) + "'s Wolf");
                                wolf.setCustomNameVisible(true);
                            }
                            this._wolfMap.get(cur).add(wolf);
                            cur.playSound(cur.getLocation(), Sound.WOLF_HOWL, 1.0f, 1.0f);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void CubTargetCancel(final EntityTargetEvent event) {
        if (!this._wolfMap.containsKey(event.getTarget())) {
            return;
        }
        if (this._wolfMap.get(event.getTarget()).contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void CubUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        for (final Player player : this._wolfMap.keySet()) {
            final Iterator<Wolf> wolfIterator = this._wolfMap.get(player).iterator();
            while (wolfIterator.hasNext()) {
                final Wolf wolf = wolfIterator.next();
                if (!wolf.isValid()) {
                    wolf.getWorld().playSound(wolf.getLocation(), Sound.WOLF_DEATH, 1.0f, 1.0f);
                    Recharge.Instance.useForce(player, this.GetName(), this._spawnRate * 1000);
                    wolfIterator.remove();
                }
                else {
                    if (player.isSneaking()) {
                        ((CraftWolf)wolf).getHandle().setGoalTarget((EntityLiving)null);
                    }
                    double range = 0.5;
                    if (wolf.getTarget() != null) {
                        range = 12.0;
                    }
                    final Location target = player.getLocation().add(player.getLocation().getDirection().multiply(3));
                    target.setY(player.getLocation().getY());
                    if (UtilMath.offset(wolf.getLocation(), target) <= range) {
                        continue;
                    }
                    float speed = 1.0f;
                    if (player.isSprinting()) {
                        speed = 1.4f;
                    }
                    final EntityCreature ec = ((CraftCreature)wolf).getHandle();
                    final NavigationAbstract nav = ec.getNavigation();
                    nav.a(target.getX(), target.getY(), target.getZ(), (double)speed);
                    wolf.setTarget((LivingEntity)null);
                }
            }
        }
    }
    
    @EventHandler
    public void CubStrikeTrigger(final PlayerInteractEvent event) {
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
        if (!event.getPlayer().getItemInHand().getType().toString().contains("_AXE") && !event.getPlayer().getItemInHand().getType().toString().contains("_SWORD")) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.Kit.HasKit(player)) {
            return;
        }
        if (!this._wolfMap.containsKey(player) || this._wolfMap.get(player).isEmpty()) {
            UtilPlayer.message((Entity)player, F.main("Game", "You have no Wolf Cubs."));
            return;
        }
        if (!Recharge.Instance.use(player, "Cub Strike", 4000L, true, true)) {
            return;
        }
        final Wolf wolf = this._wolfMap.get(player).get(UtilMath.r(this._wolfMap.get(player).size()));
        UtilAction.velocity((Entity)wolf, player.getLocation().getDirection(), 1.4, false, 0.0, 0.2, 1.2, true);
        wolf.playEffect(EntityEffect.WOLF_SMOKE);
        player.getWorld().playSound(wolf.getLocation(), Sound.WOLF_BARK, 1.0f, 1.2f);
        this._tackle.put(wolf, System.currentTimeMillis());
        UtilPlayer.message((Entity)player, F.main("Game", "You used " + F.skill("Cub Strike") + "."));
    }
    
    @EventHandler
    public void CubStrikeEnd(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        final Iterator<Wolf> wolfIterator = this._tackle.keySet().iterator();
        while (wolfIterator.hasNext()) {
            final Wolf wolf = wolfIterator.next();
            for (final Player other : this.Manager.GetGame().GetPlayers(true)) {
                if (other.getGameMode() == GameMode.SURVIVAL && UtilEnt.hitBox(wolf.getLocation(), (LivingEntity)other, 2.0, null)) {
                    if (other.equals(wolf.getOwner())) {
                        continue;
                    }
                    this.CubStrikeHit((Player)wolf.getOwner(), (LivingEntity)other, wolf);
                    wolfIterator.remove();
                    return;
                }
            }
            if (!UtilEnt.isGrounded((Entity)wolf)) {
                continue;
            }
            if (!UtilTime.elapsed(this._tackle.get(wolf), 1000L)) {
                continue;
            }
            wolfIterator.remove();
        }
    }
    
    public void CubStrikeHit(final Player damager, final LivingEntity damagee, final Wolf wolf) {
        ((CraftWolf)wolf).getHandle().setGoalTarget(((CraftLivingEntity)damagee).getHandle());
        damagee.getWorld().playSound(damagee.getLocation(), Sound.WOLF_BARK, 1.5f, 1.5f);
        this.Manager.GetBuffer().Factory().Slow(this.GetName(), damagee, (LivingEntity)damager, 4.0, 1, false, false, true, false);
        UtilPlayer.message((Entity)damager, F.main("Game", "You hit " + F.name(UtilEnt.getName((Entity)damagee)) + " with " + F.skill("Wolf Tackle") + "."));
        UtilPlayer.message((Entity)damagee, F.main("Game", String.valueOf(F.name(damager.getName())) + " hit you with " + F.skill("Wolf Tackle") + "."));
    }
    
    @EventHandler
    public void CubHeal(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        for (final ArrayList<Wolf> wolves : this._wolfMap.values()) {
            for (final Wolf wolf : wolves) {
                if (wolf.getHealth() > 0.0) {
                    wolf.setHealth(Math.min(wolf.getMaxHealth(), wolf.getHealth() + 1.0));
                }
            }
        }
    }
    
    @EventHandler
    public void PlayerDeath(final PlayerDeathEvent event) {
        final ArrayList<Wolf> wolves = this._wolfMap.remove(event.getEntity());
        if (wolves == null) {
            return;
        }
        for (final Wolf wolf : wolves) {
            wolf.remove();
        }
        wolves.clear();
    }
    
    public boolean IsMinion(final Entity ent) {
        for (final ArrayList<Wolf> minions : this._wolfMap.values()) {
            for (final Wolf minion : minions) {
                if (ent.equals(minion)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @EventHandler
    public void Damage(final CustomDamageEvent event) {
        if (event.GetDamagerEntity(true) == null) {
            return;
        }
        if (!this.IsMinion((Entity)event.GetDamagerEntity(true))) {
            return;
        }
        final double damage = 4.0;
        event.AddMod("Wolf Minion", "Negate", -event.GetDamageInitial(), false);
        event.AddMod("Wolf Minion", "Damage", damage, false);
    }
}
