package server.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import server.ServerPlugin;
import server.common.Code;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;

public class UtilNetwork
{
  private final HashSet<String> _connectingPlayers = new HashSet();

  public UtilNetwork()
  {
    Bukkit.getMessenger().registerOutgoingPluginChannel(ServerPlugin.getInstance(), "BungeeCord");
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
    player.sendMessage(Code.Color("&aSending to: "+serverName));
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

    player.sendPluginMessage(ServerPlugin.getInstance(), "BungeeCord", b.toByteArray());
    _connectingPlayers.add(player.getName());

    Bukkit.getScheduler().scheduleSyncDelayedTask(ServerPlugin.getInstance(), new Runnable()
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