package me.gonzalociocca.minelevel.core.updater;

import me.gonzalociocca.minelevel.core.enums.UpdateType;
import org.bukkit.plugin.java.JavaPlugin;

public class Updater
  implements Runnable
{
  private JavaPlugin _plugin;

  public Updater(JavaPlugin plugin)
  {
    _plugin = plugin;
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, this, 20L, 9L);
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