package mineultra.core;

import mineultra.core.common.util.NautHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MiniClientPlugin<DataType> extends MiniPlugin
{
  private NautHashMap<String, DataType> _clientData = new NautHashMap();

  public MiniClientPlugin(String moduleName, JavaPlugin plugin)
  {
    super(moduleName, plugin);
  }

  public DataType Get(String name)
  {
    if (!_clientData.containsKey(name)) {
      _clientData.put(name, AddPlayer(name));
    }
    return _clientData.get(name);
  }

  public DataType Get(Player player)
  {
    return Get(player.getName());
  }

  protected void Set(Player player, DataType data)
  {
    Set(player.getName(), data);
  }

  protected void Set(String name, DataType data)
  {
    _clientData.put(name, data);
  }

  protected abstract DataType AddPlayer(String paramString);
}