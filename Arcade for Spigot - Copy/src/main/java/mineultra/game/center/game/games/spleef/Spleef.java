package mineultra.game.center.game.games.spleef;

import java.lang.reflect.Field;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.GameType;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.SoloGame;
import mineultra.game.center.game.games.spleef.*;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.perks.event.PerkLeapEvent;
import net.minecraft.server.v1_8_R2.EntityArrow;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class Spleef
  extends SoloGame
{
  public Spleef(centerManager manager)
  {
    super(manager, GameType.Spleef, new Kit[] {new KitLeaper(manager), new KitBrawler(manager), new KitArcher(manager) }, new String[] {"Punch blocks to break them!", "1 Hunger per block smashed!", "Last player alive wins!" });
    

    this.DamagePvP = false;
    this.WorldWaterDamage = 4;
    
    this.PrepareFreeze = false;
  }
  /*
  
  @EventHandler
  public void ArrowDamage(ProjectileHitEvent event)
  {
    final Arrow arrow = (Arrow)event.getEntity();
    final double velocity = arrow.getVelocity().length();
    if (!(arrow.getShooter() instanceof Player)) {
      return;
    }
    final Player player = (Player)arrow.getShooter();
    
    this.Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(this.Manager.GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          EntityArrow entityArrow = ((CraftArrow)arrow).getHandle();
          
          Field fieldX = EntityArrow.class.getDeclaredField("d");
          Field fieldY = EntityArrow.class.getDeclaredField("e");
          Field fieldZ = EntityArrow.class.getDeclaredField("f");
          
          fieldX.setAccessible(true);
          fieldY.setAccessible(true);
          fieldZ.setAccessible(true);
          
          int x = fieldX.getInt(entityArrow);
          int y = fieldY.getInt(entityArrow);
          int z = fieldZ.getInt(entityArrow);
          
          Block block = arrow.getWorld().getBlockAt(x, y, z);
          
          double radius = 0.5D + velocity / 1.6D;
          
          Spleef.this.BlockFade(block, player);
          for (Block other : UtilBlock.getInRadius(block.getLocation().add(0.5D, 0.5D, 0.5D), radius).keySet()) {
            Spleef.this.BlockFade(other, player);
          }
          arrow.remove();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }, 0L);
  }
  
  @EventHandler(priority=EventPriority.LOW)
  public void BlockDamage(BlockDamageEvent event)
  {
    if (!IsLive()) {
      return;
    }
    if (!IsAlive(event.getPlayer())) {
      return;
    }
    event.setCancelled(true);
    
    BlockFade(event.getBlock(), event.getPlayer());
  }
  
  @EventHandler
  public void LeapDamage(PerkLeapEvent event)
  {
    if (!UtilEnt.isGrounded(event.GetPlayer())) {
      return;
    }
    for (Block block : UtilBlock.getInRadius(event.GetPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D), 3.0D, 0.0D).keySet()) {
      BlockFade(block, event.GetPlayer());
    }
  }
  
  public void BlockFade(Block block, Player player)
  {
    UtilPlayer.hunger(player, 1);
    if ((block.getTypeId() == 35) || (block.getTypeId() == 159))
    {
      if ((block.getData() == 5) || (block.getData() == 13)) {
        block.setData((byte)4);
      } else if (block.getData() == 4) {
        block.setData((byte)14);
      } else {
        Break(block);
      }
    }
    else if (block.getTypeId() == 1) {
      block.setTypeId(4);
    } else if (block.getTypeId() == 98)
    {
      if ((block.getData() == 0) || (block.getData() == 1)) {
        block.setData((byte)2);
      } else {
        Break(block);
      }
    }
    else if (block.getTypeId() == 2) {
      block.setTypeId(3);
    } else if (block.getTypeId() == 5)
    {
      if (block.getData() == 1) {
        block.setData((byte)0);
      } else if (block.getData() == 0) {
        block.setData((byte)2);
      } else {
        Break(block);
      }
    }
    else if (block.getTypeId() != 7) {
      Break(block);
    }
  }
  
  public void Break(Block block)
  {
    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
    block.setTypeId(0);
  }
  
  @EventHandler
  public void Hunger(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    if (!IsLive()) {
      return;
    }
    for (Player player : GetPlayers(true))
    {
      if (player.getFoodLevel() <= 0)
      {
        this.Manager.GetDamage().NewDamageEvent(player, null, null, 
          EntityDamageEvent.DamageCause.STARVATION, 1.0D, false, true, false, 
          "Starvation", GetName());
        
        UtilPlayer.message(player, F.main("Game", "Rompe bloques para recobrar vida!"));
      }
      UtilPlayer.hunger(player, -1);
    }
  }
  */
}
