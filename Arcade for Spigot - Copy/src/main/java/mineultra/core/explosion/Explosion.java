package mineultra.core.explosion;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mineultra.core.MiniPlugin;
import mineultra.core.common.BlockRestore;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilAlg;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilMath;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Explosion extends MiniPlugin
{
  private boolean _regenerateGround = false;
  private boolean _temporaryDebris = true;
  private boolean _enableDebris = false;
  private boolean _tntSpread = true;
  private HashSet<FallingBlock> _explosionBlocks = new HashSet();
  private BlockRestore _blockRestore;

  public Explosion(JavaPlugin plugin, BlockRestore blockRestore)
  {
    super("Block Restore", plugin);

    _blockRestore = blockRestore;
  }

  @EventHandler
  public void ExplosionPrime(ExplosionPrimeEvent event)
  {
    if (event.getRadius() >= 5.0F) {
      return;
    }
    for (Block block : UtilBlock.getInRadius(event.getEntity().getLocation(), event.getRadius()).keySet())
      if (block.isLiquid())
        block.setTypeId(0);
  }

  @EventHandler
  public void ExplosionEntity(EntityExplodeEvent event)
  {
    if (event.isCancelled()) {
      return;
    }
    try
    {
      if (event.getEntityType() == EntityType.CREEPER) {
        event.blockList().clear();
      }
      if (event.getEntityType() == EntityType.WITHER_SKULL) {
        event.blockList().clear();
      }
    }
    catch (Exception localException)
    {
    }

    if (event.blockList().isEmpty()) {
      return;
    }

    ExplosionEvent explodeEvent = new ExplosionEvent(event.blockList());
    _plugin.getServer().getPluginManager().callEvent(explodeEvent);

    event.setYield(0.0F);

    final HashMap blocks = new HashMap();

    for (Block cur : event.blockList())
    {
      if ((cur.getTypeId() != 0) && (!cur.isLiquid()))
      {
        if ((cur.getType() == Material.CHEST) || 
          (cur.getType() == Material.IRON_ORE) || 
          (cur.getType() == Material.COAL_ORE) || 
          (cur.getType() == Material.GOLD_ORE) || 
          (cur.getType() == Material.DIAMOND_ORE))
        {
          cur.breakNaturally();
        }
        else
        {
          blocks.put(cur, new AbstractMap.SimpleEntry(Integer.valueOf(cur.getTypeId()), Byte.valueOf(cur.getData())));

          if (!_regenerateGround)
          {
            if ((cur.getTypeId() != 98) || ((cur.getData() != 0) && (cur.getData() != 3))) {
              cur.setTypeId(0);
            }
          }
          else
          {
            int heightDiff = cur.getLocation().getBlockY() - event.getEntity().getLocation().getBlockY();
            _blockRestore.Add(cur, 0, (byte)0, (long)(20000 + heightDiff * 3000 + Math.random() * 2900.0D));
          }
        }
      }
    }
    event.blockList().clear();

    final Entity fEnt = event.getEntity();
    final Location fLoc = event.getLocation();
    _plugin.getServer().getScheduler().runTaskLater(_plugin, new Runnable()
    {
      @Override
      public void run()
      {
          for (Iterator it = blocks.keySet().iterator(); it.hasNext();) {
              Block cur = (Block) it.next();
              if ((((Integer)((Map.Entry)blocks.get(cur)).getKey()) != 98) || (
                      (((Byte)((Map.Entry)blocks.get(cur)).getValue()) != 0) && (((Byte)((Map.Entry)blocks.get(cur)).getValue()) != 3)))
              {
                  if ((_tntSpread) && (((Integer)((Map.Entry)blocks.get(cur)).getKey()) == 46))
                  {
                      TNTPrimed ent = (TNTPrimed)cur.getWorld().spawn(cur.getLocation().add(0.5D, 0.5D, 0.5D), TNTPrimed.class);
                      Vector vec = UtilAlg.getTrajectory(fEnt, ent);
                      if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);
                      
                      UtilAction.velocity(ent, vec, 1.0D, false, 0.0D, 0.6D, 10.0D, false);
                      
                      ent.setFuseTicks(10);
                  }
                  else
                  {
                      double chance = 0.85D + _explosionBlocks.size() / 500.0D;
                      if (Math.random() > Math.min(0.975D, chance))
                      {
                          FallingBlock fall = cur.getWorld().spawnFallingBlock(cur.getLocation().add(0.5D, 0.5D, 0.5D), ((Integer)((Map.Entry)blocks.get(cur)).getKey()).intValue(), ((Byte)((Map.Entry)blocks.get(cur)).getValue()).byteValue());
                          
                          Vector vec = UtilAlg.getTrajectory(fEnt, fall);
                          if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);
                          
                          UtilAction.velocity(fall, vec, 0.5D + 0.25D * Math.random(), false, 0.0D, 0.4D + 0.2D * Math.random(), 10.0D, false);
                          
                          _explosionBlocks.add(fall);
                      }
                      
                  }
                  
              }
          }

        for (Block cur : UtilBlock.getInRadius(fLoc, 4.0D).keySet())
          if ((cur.getTypeId() == 98) && (
            (cur.getData() == 0) || (cur.getData() == 3)))
            cur.setTypeIdAndData(98, (byte)2, true);
      }
    }
    , 1L);
  }

  @EventHandler
  public void ExplosionBlockUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }

    Iterator fallingIterator = _explosionBlocks.iterator();

    while (fallingIterator.hasNext())
    {
      FallingBlock cur = (FallingBlock)fallingIterator.next();

      if ((cur.isDead()) || (!cur.isValid()) || (cur.getTicksLived() > 400) || (!cur.getWorld().isChunkLoaded(cur.getLocation().getBlockX() >> 4, cur.getLocation().getBlockZ() >> 4)))
      {
        fallingIterator.remove();

        if ((cur.getTicksLived() > 400) || (!cur.getWorld().isChunkLoaded(cur.getLocation().getBlockX() >> 4, cur.getLocation().getBlockZ() >> 4)))
        {
          cur.remove();
          return;
        }

        Block block = cur.getLocation().getBlock();
        block.setTypeIdAndData(0, (byte)0, true);

        if (_enableDebris)
        {
          if (_temporaryDebris)
          {
            _blockRestore.Add(block, cur.getBlockId(), cur.getBlockData(), 10000L);
          }
          else
          {
            block.setTypeIdAndData(cur.getBlockId(), cur.getBlockData(), true);
          }
        }
        else
        {
          cur.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, cur.getBlockId());
        }

        cur.remove();
      }
    }
  }

  @EventHandler
  public void ExplosionItemSpawn(ItemSpawnEvent event)
  {
    for (FallingBlock block : _explosionBlocks)
      if (UtilMath.offset(event.getEntity().getLocation(), block.getLocation()) < 1.0D)
        event.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.LOW)
  public void ExplosionBlocks(EntityExplodeEvent event)
  {
    if (event.getEntity() == null)
      event.blockList().clear();
  }

  public void SetRegenerate(boolean regenerate)
  {
    _regenerateGround = regenerate;
  }

  public void SetDebris(boolean value)
  {
    _enableDebris = value;
  }

  public void SetTNTSpread(boolean value)
  {
    _tntSpread = value;
  }

  public void SetTemporaryDebris(boolean value)
  {
    _temporaryDebris = value;
  }

  public HashSet<FallingBlock> GetExplosionBlocks()
  {
    return _explosionBlocks;
  }

  public void BlockExplosion(Collection<Block> blockSet, Location mid, boolean onlyAbove)
  {
    if (blockSet.isEmpty()) {
      return;
    }

    final HashMap blocks = new HashMap();

    for (Block cur : blockSet)
    {
      if (cur.getTypeId() != 0)
      {
        if ((!onlyAbove) || (cur.getY() >= mid.getY()))
        {
          blocks.put(cur, new AbstractMap.SimpleEntry(cur.getTypeId(), Byte.valueOf(cur.getData())));

          cur.setType(Material.AIR);
        }
      }
    }
    final Location fLoc = mid;
    _plugin.getServer().getScheduler().runTaskLater(_plugin, new Runnable()
    {
      @Override
      public void run()
      {
        for (Block cur : (List<Block>)blocks.keySet())
        {
          if ((((Integer)((Map.Entry)blocks.get(cur)).getKey()) != 98) || (
            (((Byte)((Map.Entry)blocks.get(cur)).getValue()) != 0) && (((Byte)((Map.Entry)blocks.get(cur)).getValue()) != 3)))
          {
            double chance = 0.2D + _explosionBlocks.size() / 120.0D;
            if (Math.random() > Math.min(0.85D, chance))
            {
              FallingBlock fall = cur.getWorld().spawnFallingBlock(cur.getLocation().add(0.5D, 0.5D, 0.5D), ((Integer)((Map.Entry)blocks.get(cur)).getKey()).intValue(), ((Byte)((Map.Entry)blocks.get(cur)).getValue()).byteValue());

              Vector vec = UtilAlg.getTrajectory(fLoc, fall.getLocation());
              if (vec.getY() < 0.0D) vec.setY(vec.getY() * -1.0D);

              UtilAction.velocity(fall, vec, 0.5D + 0.25D * Math.random(), false, 0.0D, 0.4D + 0.2D * Math.random(), 10.0D, false);

              _explosionBlocks.add(fall);
            }
          }
        }
      }
    }
    , 1L);
  }
}