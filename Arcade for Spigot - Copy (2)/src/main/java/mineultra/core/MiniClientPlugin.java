package mineultra.core;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public abstract class MiniClientPlugin<DataType> extends MiniPlugin
{
  private HashMap<String, DataType> _clientData = new HashMap();

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