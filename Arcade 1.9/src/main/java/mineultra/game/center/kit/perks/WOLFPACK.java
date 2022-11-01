package mineultra.game.center.kit.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
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
import net.minecraft.server.v1_9_R1.EntityCreature;
import net.minecraft.server.v1_9_R1.Navigation;
import net.minecraft.server.v1_9_R1.NavigationAbstract;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftWolf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public class WOLFPACK
  extends Perk
{
  private final HashMap<Player, ArrayList<Wolf>> _wolfMap = new HashMap();
  private final HashMap<Wolf, Long> _strike = new HashMap();
  private final HashMap<Player, Long> _tackle = new HashMap();
  private final HashMap<Wolf, Long> _useDelay = new HashMap();
  private final int _spawnRate;
  private final int _max;
  private final boolean _baby;
  private final boolean _name;
  
  public WOLFPACK(int spawnRate, int max, boolean baby, boolean name)
  {
    super("Wolf Master", new String[] {C.cYellow + "Tap Jump Twice" + C.cGray + " to " + C.cGreen + "Double Jump", C.cGray + "Spawn 1 Wolf Cub every " + spawnRate + " seconds. Maximum of " + max + ".", C.cYellow + "Right-Click" + C.cGray + " with Axe to use " + C.cGreen + "Cub Strike", C.cYellow + "Right-Click" + C.cGray + " with Spade to use " + C.cGreen + "Pack Leap", C.cYellow + "Crouch" + C.cGray + " to use " + C.cGreen + "Cub Return" });
    

    this._spawnRate = spawnRate;
    this._max = max;
    this._baby = baby;
    this._name = name;
  }
  
  @Override
  public void Apply(Player player)
  {
    Recharge.Instance.use(player, GetName(), this._spawnRate * 1000, false, false);
    if (this._wolfMap.containsKey(player))
    {
      for (Wolf wolf : this._wolfMap.get(player)) {
        wolf.remove();
      }
      ((ArrayList)this._wolfMap.get(player)).clear();
      
      this._wolfMap.remove(player);
    }
  }
  
  @EventHandler
  public void DoubleJump(PlayerToggleFlightEvent event)
  {
    final Player player = event.getPlayer();
    if (!this.Kit.HasKit(player)) {
      return;
    }
    if (player.getGameMode() == GameMode.CREATIVE) {
      return;
    }
    event.setCancelled(true);
    player.setFlying(false);
    

    player.setAllowFlight(false);
    

    UtilAction.velocity(player, player.getLocation().getDirection(), 1.0D, true, 1.0D, 0.0D, 1.0D, true);
    if (this._wolfMap.containsKey(player)) {
      for (final Wolf wolf : this._wolfMap.get(player)) {
        this.Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
        {
          @Override
          public void run()
          {
            Vector velocity = UtilAlg.getTrajectory(player.getLocation(), 
              player.getLocation().add(player.getLocation().getDirection().setY(0).multiply(6).add(new Vector(0, 6, 0))));
            

            double power = 1.2D;
            if (player.isSprinting()) {
              power = 1.6D;
            }
            UtilAction.velocity(wolf, velocity, power, true, 1.0D, 0.0D, 1.0D, true);
            

            wolf.getWorld().playEffect(wolf.getLocation(), Effect.BLAZE_SHOOT, 0);
          }
        }, UtilMath.r(10));
      }
    }
    player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
  }
  
  @EventHandler
  public void DoubleJumpUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player player = arrayOfPlayer[i];
      if (player.getGameMode() != GameMode.CREATIVE) {
        if (this.Kit.HasKit(player)) {
          if ((UtilEnt.isGrounded(player)) || (UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN)))) {
            player.setAllowFlight(true);
          }
        }
      }
    }
  }
  
  @EventHandler
  public void MinionSpawn(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player cur = arrayOfPlayer[i];
      if (this.Kit.HasKit(cur)) {
        if (this.Manager.GetGame().IsAlive(cur)) {
          if (Recharge.Instance.use(cur, GetName(), this._spawnRate * 1000, false, false)) {
            if (!this._wolfMap.containsKey(cur))
            {
              this._wolfMap.put(cur, new ArrayList());
              while (((ArrayList)this._wolfMap.get(cur)).size() < this._max) {
                MinionSpawn(cur);
              }
            }
            else if (((ArrayList)this._wolfMap.get(cur)).size() < this._max)
            {
              MinionSpawn(cur);
            }
          }
        }
      }
    }
  }
  
  public void MinionSpawn(Player cur)
  {
    this.Manager.GetGame().CreatureAllowOverride = true;
    Wolf wolf = (Wolf)cur.getWorld().spawn(cur.getLocation(), Wolf.class);
    this.Manager.GetGame().CreatureAllowOverride = false;
    


    wolf.playEffect(EntityEffect.WOLF_HEARTS);
    
    wolf.setMaxHealth(30.0D);
    wolf.setHealth(wolf.getMaxHealth());
    if (this._baby) {
      wolf.setBaby();
    }
    if (this._name)
    {
      wolf.setCustomName(cur.getName() + "'s Wolf");
      wolf.setCustomNameVisible(true);
    }
    ((ArrayList)this._wolfMap.get(cur)).add(wolf);
  }
  
  @EventHandler
  public void MinionTargetCancel(EntityTargetEvent event)
  {
    if (!this._wolfMap.containsKey(event.getTarget())) {
      return;
    }
    if (((ArrayList)this._wolfMap.get(event.getTarget())).contains(event.getEntity())) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void MinionUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    Iterator<Wolf> wolfIterator;
    for (Iterator localIterator = this._wolfMap.keySet().iterator(); localIterator.hasNext(); wolfIterator.hasNext())
    {
      Player player = (Player)localIterator.next();
      
      wolfIterator = ((ArrayList)this._wolfMap.get(player)).iterator();
      while (wolfIterator.hasNext())
      {
        Wolf wolf = (Wolf)wolfIterator.next();
        if (!this.Manager.GetGame().IsAlive(player))
        {
          wolf.remove();
          wolfIterator.remove();
        }
        else if (!wolf.isValid())
        {
          wolf.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_DEATH, 1.0F, 1.0F);
          Recharge.Instance.useForce(player, GetName(), this._spawnRate * 1000);
          wolfIterator.remove();
        }
        else
        {
          if (player.isSneaking())
          {
            ((CraftWolf)wolf).getHandle().setGoalTarget(null);
            wolf.setAngry(false);
          }
          double range = 0.5D;
          if (wolf.getTarget() != null) {
            range = 12.0D;
          }
          Location target = player.getLocation().add(player.getLocation().getDirection().multiply(3));
          target.setY(player.getLocation().getY());
          if (UtilMath.offset(wolf.getLocation(), target) > range)
          {
            float speed = 1.0F;
            if (player.isSprinting()) {
              speed = 1.4F;
            }
            if ((UtilEnt.isGrounded(wolf)) && (UtilMath.offset(target, wolf.getLocation()) > 6.0D) && (!this._useDelay.containsKey(wolf)))
            {
              Vector vel = UtilAlg.getTrajectory(wolf, player);
              if (vel.getY() < 0.2D) {
                vel.setY(0.2D);
              }
              UtilAction.velocity(wolf, vel, 1.2D, false, 1.0D, 0.2D, 1.0D, true);
              this._useDelay.put(wolf, (long)(System.currentTimeMillis() + 500L + 500.0D * Math.random()));
            }
            if (UtilMath.offset(target, wolf.getLocation()) > 16.0D) {
              target = wolf.getLocation().add(UtilAlg.getTrajectory(wolf.getLocation(), target));
            }
            EntityCreature ec = ((CraftCreature)wolf).getHandle();
            NavigationAbstract nav = ec.getNavigation();
            nav.a(target.getX(), target.getY(), target.getZ(), speed);
            
            wolf.setTarget(null);
          }
        }
      }
      wolfIterator = this._useDelay.keySet().iterator();
      
      
      
      Wolf wolf = (Wolf)wolfIterator.next();
      if (System.currentTimeMillis() > ((Long)this._useDelay.get(wolf)).longValue()) {
        wolfIterator.remove();
      }
    }
  }
  
  @EventHandler
  public void CubStrikeTrigger(PlayerInteractEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
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
    Player player = event.getPlayer();
    if (!this.Kit.HasKit(player)) {
      return;
    }
    Wolf wolf = null;
    double best = 999.0D;
    for (Wolf other : this._wolfMap.get(player)) {
      if (!this._useDelay.containsKey(other))
      {
        double dist = UtilMath.offset(other.getLocation(), player.getEyeLocation().add(player.getLocation().getDirection().setY(0).multiply(1)));
        if (dist <= 4.0D) {
          if ((wolf == null) || (dist < best))
          {
            wolf = other;
            best = dist;
          }
        }
      }
    }
    if (wolf == null)
    {
      UtilPlayer.message(player, F.main("Game", "You have no nearby Wolf Cubs."));
      return;
    }
    UtilAction.velocity(wolf, player.getLocation().getDirection(), 1.6D, false, 0.0D, 0.2D, 1.2D, true);
    
    wolf.playEffect(EntityEffect.WOLF_SMOKE);
    
    player.getWorld().playSound(wolf.getLocation(), Sound.ENTITY_WOLF_SHAKE, 1.0F, 1.8F);
    

    this._strike.put(wolf, Long.valueOf(System.currentTimeMillis()));
    this._useDelay.put(wolf, Long.valueOf(System.currentTimeMillis() + 1000L));
    

    UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Cub Strike") + "."));
  }
  
  @EventHandler
  public void CubStrikeEnd(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Iterator<Wolf> wolfIterator = this._strike.keySet().iterator();
    while (wolfIterator.hasNext())
    {
      Wolf wolf = (Wolf)wolfIterator.next();
      for (Player other : this.Manager.GetGame().GetPlayers(true)) {
        if ((other.getGameMode() == GameMode.SURVIVAL) && 
          (UtilEnt.hitBox(wolf.getLocation(), other, 2.0D, null))) {
          if (!other.equals(GetOwner(wolf)))
          {
            CubStrikeHit(GetOwner(wolf), other, wolf);
            wolfIterator.remove();
            return;
          }
        }
      }
      if (UtilEnt.isGrounded(wolf)) {
        if (UtilTime.elapsed(((Long)this._strike.get(wolf)).longValue(), 1000L)) {
          wolfIterator.remove();
        }
      }
    }
  }
  
  public void CubStrikeHit(Player damager, LivingEntity damagee, Wolf wolf)
  {
    if (damager == null) {
      return;
    }
    this.Manager.GetDamage().NewDamageEvent(damagee, damager, null, 
      EntityDamageEvent.DamageCause.CUSTOM, 5.0D, true, true, false, 
      damager.getName(), "Cub Strike");
    

    ((CraftWolf)wolf).getHandle().setGoalTarget(((CraftLivingEntity)damagee).getHandle());
    

    damagee.getWorld().playSound(damagee.getLocation(), Sound.ENTITY_WOLF_SHAKE, 1.5F, 2.0F);
    

    UtilPlayer.message(damager, F.main("Game", "You hit " + F.name(UtilEnt.getName(damagee)) + " with " + F.skill("Cub Strike") + "."));
    UtilPlayer.message(damagee, F.main("Game", F.name(damager.getName()) + " hit you with " + F.skill("Cub Strike") + "."));
  }
  
  @EventHandler
  public void CubHeal(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    Iterator localIterator2;
    for (Iterator localIterator1 = this._wolfMap.values().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
    {
      ArrayList<Wolf> wolves = (ArrayList)localIterator1.next();
      
      localIterator2 = wolves.iterator();
      Wolf wolf = (Wolf)localIterator2.next();
      if (wolf.getHealth() > 0.0D) {
        wolf.setHealth(Math.min(wolf.getMaxHealth(), wolf.getHealth() + 1.0D));
      }
    }
  }
  
  @EventHandler
  public void TackleTrigger(PlayerInteractEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
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
    Player player = event.getPlayer();
    if (!this.Kit.HasKit(player)) {
      return;
    }
    if (!Recharge.Instance.use(player, "Pack Leap", 4000L, true, true)) {
      return;
    }
    UtilAction.velocity(player, player.getLocation().getDirection(), 1.6D, false, 1.0D, 0.2D, 1.4D, true);
    if (this._wolfMap.containsKey(player)) {
      for (Wolf wolf : this._wolfMap.get(player)) {
        UtilAction.velocity(wolf, player.getLocation().getDirection(), 1.6D, false, 1.0D, 0.2D, 1.4D, true);
      }
    }
    this._tackle.put(player, Long.valueOf(System.currentTimeMillis()));
    
    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_SHAKE, 1.0F, 1.2F);
    

    UtilPlayer.message(player, F.main("Game", "You used " + F.skill("Tackle Leap") + "."));
  }
  
  @EventHandler
  public void TackleEnd(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Iterator<Player> playerIterator = this._tackle.keySet().iterator();
    while (playerIterator.hasNext())
    {
      Player player = (Player)playerIterator.next();
      for (Player other : this.Manager.GetGame().GetPlayers(true)) {
        if ((!player.equals(other)) && 
          (other.getGameMode() == GameMode.SURVIVAL) && 
          (UtilEnt.hitBox(player.getLocation(), other, 2.0D, null)))
        {
          TackleHit(player, other);
          playerIterator.remove();
          return;
        }
      }
      if (UtilEnt.isGrounded(player)) {
        if (UtilTime.elapsed(((Long)this._tackle.get(player)).longValue(), 1000L)) {
          playerIterator.remove();
        }
      }
    }
  }
  
  public void TackleHit(Player damager, LivingEntity damagee)
  {
    damager.setVelocity(new Vector(0, 0, 0));
    
    this.Manager.GetDamage().NewDamageEvent(damagee, damager, null, 
      EntityDamageEvent.DamageCause.CUSTOM, 7.0D, false, true, false, 
      damager.getName(), "Tackle Leap");
    if (this._wolfMap.containsKey(damager)) {
      for (Wolf wolf : this._wolfMap.get(damager)) {
        ((CraftWolf)wolf).getHandle().setGoalTarget(((CraftLivingEntity)damagee).getHandle());
      }
    }
    damagee.getWorld().playSound(damagee.getLocation(), Sound.ENTITY_WOLF_SHAKE, 1.5F, 1.5F);
    

    this.Manager.GetBuffer().Factory().Slow(GetName(), damagee, damager, 6.0D, 2, false, false, true, false);
    

    UtilPlayer.message(damager, F.main("Game", "You hit " + F.name(UtilEnt.getName(damagee)) + " with " + F.skill("Tackle Leap") + "."));
    UtilPlayer.message(damagee, F.main("Game", F.name(damager.getName()) + " hit you with " + F.skill("Tackle Leap") + "."));
  }
  
  @EventHandler
  public void Knockback(CustomDamageEvent event)
  {
    if ((event.GetReason() != null) && (event.GetReason().contains("Cub Strike"))) {
      event.AddKnockback(GetName(), 3.0D);
    }
    if ((event.GetDamagerEntity(false) != null) && ((event.GetDamagerEntity(false) instanceof Wolf))) {
      event.AddKnockback(GetName(), 3.0D);
    }
  }
  
  public Player GetOwner(Wolf wolf)
  {
    for (Player player : this._wolfMap.keySet()) {
      if (((ArrayList)this._wolfMap.get(player)).contains(wolf)) {
        return player;
      }
    }
    return null;
  }
}
