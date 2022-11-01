package mineultra.core.portal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.Colorizer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Portal extends MiniPlugin
{
  private final HashSet<String> _connectingPlayers = new HashSet();

  public Portal(JavaPlugin plugin)
  {
    super("Portal", plugin);

    Bukkit.getMessenger().registerOutgoingPluginChannel(GetPlugin(), "BungeeCord");
  }

  public void SendAllPlayers(String serverName)
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      SendPlayerToServer(player, serverName);
    }
  }

  public void SendPlayerToServer(final Player player, String serverName)
  {
    if (_connectingPlayers.contains(player.getName())) {
      return;
    }
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(b);
    try
    {
      out.writeUTF("Connect");
      out.writeUTF(serverName);
    player.sendMessage(Colorizer.Color("&aSending to: "+serverName));
    }
    catch (IOException localIOException1)
    {
      try
      {
        out.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        out.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    player.sendPluginMessage(GetPlugin(), "BungeeCord", b.toByteArray());
    _connectingPlayers.add(player.getName());

    GetScheduler().scheduleSyncDelayedTask(GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
        _connectingPlayers.remove(player.getName());
      }
    }
    , 20L);
  }
}