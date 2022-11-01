package mineultra.game.center.game.games.zombiesurvival;


import org.bukkit.Location;

public final class ZombieData
{
  public Location Target;
  public long Time;
  
  public ZombieData(Location target)
  {
    SetTarget(target);
  }
  
  public void SetTarget(Location target)
  {
    this.Target = target;
    this.Time = System.currentTimeMillis();
  }
}
