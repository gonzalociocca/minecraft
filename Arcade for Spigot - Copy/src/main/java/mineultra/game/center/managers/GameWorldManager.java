package mineultra.game.center.managers;

import java.util.HashSet;
import java.util.Iterator;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.Game;
import mineultra.game.center.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameWorldManager
  implements Listener
{
  centerManager Manager = null;
  private final HashSet<WorldData> _worldLoader = new HashSet();

  public GameWorldManager(final centerManager manager)
  {
    Manager = manager;

 manager.GetPluginManager().registerEvents(GameWorldManager.this, manager.GetPlugin());
  
  }

  @EventHandler
  public void LoadWorldChunks(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
for(Iterator worldIterator = _worldLoader.iterator();worldIterator.hasNext();){

      long HardLoad = (long)30L * Bukkit.getOnlinePlayers().size();
      long timeLeft = System.currentTimeMillis() + HardLoad;
      if (timeLeft > 0L)
      {
        WorldData worldData = (WorldData)worldIterator.next();

        if (worldData.World == null)
        {
          worldIterator.remove();
        }
        else if (worldData.LoadChunks(timeLeft))
        {
          worldData.Host.SetState(Game.GameState.Recruit);
          worldIterator.remove();
        }
      }
}
    
  }



  public void RegisterWorld(WorldData worldData)
  {
    _worldLoader.add(worldData);
  }
}