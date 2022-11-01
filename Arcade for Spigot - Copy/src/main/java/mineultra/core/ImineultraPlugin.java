package mineultra.core;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract interface ImineultraPlugin
{
  public abstract JavaPlugin GetPlugin();

  public abstract Server GetRealServer();

  public abstract PluginManager GetPluginManager();
}