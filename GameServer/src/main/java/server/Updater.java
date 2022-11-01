package server;

import server.common.UpdateType;
import server.common.event.UpdateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater
  implements Runnable
{
  private final JavaPlugin _plugin;

  public Updater(JavaPlugin plugin)
  {
    _plugin = plugin;
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, this, 0L, 1L);
  }

  @Override
  public void run()
  {
    for (UpdateType updateType : UpdateType.values())
    {
      if (updateType.Elapsed())
      {
        _plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
      }
    }
  }
}