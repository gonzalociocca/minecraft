package me.gonzalociocca.minigame.events.Update;

import org.bukkit.plugin.java.JavaPlugin;

public class Updater
  implements Runnable
{
  private JavaPlugin _plugin;

  public Updater(JavaPlugin plugin)
  {
    _plugin = plugin;
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, this, 20L, 1L);
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