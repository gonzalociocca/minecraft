package mineultra.core.common.util;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitScheduler;

public class WorldChunkLoader
  implements Runnable
{
  private static WorldChunkLoader _worldChunkLoader = null;

  private HashMap<WorldLoadInfo, Runnable> _worldRunnableMap = new HashMap();
  private long _loadPassStart;
  private long _maxPassTime = 25L;

  private WorldChunkLoader()
  {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugins()[0], this, 0L, 1L);
  }

  public static void AddWorld(WorldLoadInfo worldInfo, Runnable runnable)
  {
    if (_worldChunkLoader == null)
    {
      _worldChunkLoader = new WorldChunkLoader();
    }

    _worldChunkLoader._worldRunnableMap.put(worldInfo, runnable);
  }

  @Override
  public void run()
  {
    _loadPassStart = System.currentTimeMillis();

    Iterator worldInfoIterator = _worldRunnableMap.keySet().iterator();

    while (worldInfoIterator.hasNext())
    {
      WorldLoadInfo worldInfo = (WorldLoadInfo)worldInfoIterator.next();
      System.out.println("Loading chunks for " + worldInfo.GetWorld().getName());

      while (worldInfo.CurrentChunkX <= worldInfo.GetMaxChunkX())
      {
        while (worldInfo.CurrentChunkZ <= worldInfo.GetMaxChunkZ())
        {
          if (System.currentTimeMillis() - _loadPassStart >= _maxPassTime) {
            return;
          }
          worldInfo.GetWorld().loadChunk(worldInfo.CurrentChunkX, worldInfo.CurrentChunkZ);
          worldInfo.CurrentChunkZ += 1;
        }

        worldInfo.CurrentChunkZ = worldInfo.GetMinChunkZ();
        worldInfo.CurrentChunkX += 1;
      }

      ((Runnable)_worldRunnableMap.get(worldInfo)).run();
      worldInfoIterator.remove();
    }
  }
}