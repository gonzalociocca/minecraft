package mineultra.core.common.util;

import org.bukkit.World;

public class WorldLoadInfo
{
  private final World _world;
  private final int _minChunkX;
  private final int _minChunkZ;
  private final int _maxChunkX;
  private final int _maxChunkZ;
  public int CurrentChunkX;
  public int CurrentChunkZ;

  public WorldLoadInfo(World world, int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ)
  {
    _world = world;
    _minChunkX = minChunkX;
    _minChunkZ = minChunkZ;
    _maxChunkX = maxChunkX;
    _maxChunkZ = maxChunkZ;

    CurrentChunkX = minChunkX;
    CurrentChunkZ = minChunkZ;
  }

  public World GetWorld()
  {
    return _world;
  }

  public int GetMinChunkX()
  {
    return _minChunkX;
  }

  public int GetMinChunkZ()
  {
    return _minChunkZ;
  }

  public int GetMaxChunkX()
  {
    return _maxChunkX;
  }

  public int GetMaxChunkZ()
  {
    return _maxChunkZ;
  }
}