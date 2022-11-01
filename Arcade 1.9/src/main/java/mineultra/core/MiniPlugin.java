package mineultra.core;

import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilTime;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class MiniPlugin
  implements Listener
{
  protected String _moduleName = "Default";
  protected JavaPlugin _plugin;

  public MiniPlugin(String moduleName, JavaPlugin plugin)
  {
    _moduleName = moduleName;
    _plugin = plugin;

    onEnable();
    RegisterEvents(this);
  }

  public PluginManager GetPluginManager()
  {
    return _plugin.getServer().getPluginManager();
  }

  public BukkitScheduler GetScheduler()
  {
    return _plugin.getServer().getScheduler();
  }

  public JavaPlugin GetPlugin()
  {
    return _plugin;
  }

  public void RegisterEvents(Listener listener)
  {
    _plugin.getServer().getPluginManager().registerEvents(listener, _plugin);
  }

  public final void onEnable()
  {
    long epoch = System.currentTimeMillis();
    Log("Initializing...");
    Enable();
    AddCommands();
    Log("Enabled in " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, UtilTime.TimeUnit.FIT) + ".");
  }

  public final void onDisable()
  {
    Disable();

    Log("Disabled.");
  }
  public void Enable() {
  }
  public void Disable() {
  }

  public void AddCommands() {
  }

  public final String GetName() {
    return _moduleName;
  }

  protected void Log(String message)
  {
    System.out.println(F.main(_moduleName, message));
  }
}