package server.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;



public class UtilPlayer
{
  public static void message(org.bukkit.entity.Entity client, LinkedList<String> messageList)
  {
    for(String str : messageList){
      message(client, str);
    }
  }

  public static void message(org.bukkit.entity.Entity client, String message)
  {
    if(client != null && client instanceof Player){
     client.sendMessage(message);
    }
  }
  public static void sendPacket(Player player, Packet... packets)
  {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

    for (Packet packet : packets)
    {
      connection.sendPacket(packet);
    }
  }

  public static Player searchExact(String name)
  {
    for (Player cur : Bukkit.getOnlinePlayers()) {
      if (cur.getName().equalsIgnoreCase(name))
        return cur;
    }
    return null;
  }

  public static Player searchOnline(Player caller, String player, boolean inform)
  {
    LinkedList matchList = new LinkedList();

    for (Player cur : Bukkit.getOnlinePlayers())
    {
      if (cur.getName().equalsIgnoreCase(player)) {
        return cur;
      }
      if (cur.getName().toLowerCase().contains(player.toLowerCase())) {
        matchList.add(cur);
      }
    }

    if (matchList.size() != 1)
    {
      if (!inform) {
        return null;
      }

      message(caller, F.main("Online Player Search", 
        C.mCount + matchList.size() + 
        C.mBody + " matches for [" + 
        C.mElem + player + 
        C.mBody + "]."));

      if (matchList.size() > 0)
      {
        String matchString = "";
        for (Player cur : (List<Player>)matchList)
          matchString = matchString + F.elem(cur.getName()) + ", ";
        if (matchString.length() > 1) {
          matchString = matchString.substring(0, matchString.length() - 2);
        }
        message(caller, F.main("Online Player Search", 
          C.mBody + "Matches [" + 
          C.mElem + matchString + 
          C.mBody + "]."));
      }

      return null;
    }

    return (Player)matchList.get(0);
  }

  public static LinkedList<Player> matchOnline(Player caller, String players, boolean inform)
  {
    LinkedList matchList = new LinkedList();

    String failList = "";

    for (String cur : players.split(","))
    {
      Player match = searchOnline(caller, cur, inform);

      if (match != null) {
        matchList.add(match);
      }
      else {
        failList = failList + cur + " ";
      }
    }
    if ((inform) && (failList.length() > 0))
    {
      failList = failList.substring(0, failList.length() - 1);
      message(caller, F.main("Online Player(s) Search", 
        C.mBody + "Invalid [" + 
        C.mElem + failList + 
        C.mBody + "]."));
    }

    return matchList;
  }

          public static LinkedList<Player> getNearby(Location loc, double maxDist)
          {
            LinkedList nearbyMap = new LinkedList();

            for (Player cur : loc.getWorld().getPlayers())
            {
              if (cur.getGameMode() != GameMode.CREATIVE)
              {
                if (!cur.isDead())
                {
                  double dist = loc.toVector().subtract(cur.getLocation().toVector()).length();

          if (dist <= maxDist)
          {
            for (int i = 0; i < nearbyMap.size(); i++)
            {
              if (dist < loc.toVector().subtract(((Player)nearbyMap.get(i)).getLocation().toVector()).length())
              {
                nearbyMap.add(i, cur);
                break;
              }
            }

            if (!nearbyMap.contains(cur))
              nearbyMap.addLast(cur); 
          }
        }
      }
    }
    return nearbyMap;
  }

  public static Player getClosest(Location loc, Collection<Player> ignore)
  {
    Player best = null;
    double bestDist = 0.0D;

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE)
      {
        if (!cur.isDead())
        {
          if ((ignore == null) || (!ignore.contains(cur)))
          {
            double dist = UtilMath.offset(cur.getLocation(), loc);

            if ((best == null) || (dist < bestDist))
            {
              best = cur;
              bestDist = dist;
            }
          }
        }
      }
    }
    return best;
  }

  public static Player getClosest(Location loc, org.bukkit.entity.Entity ignore)
  {
    Player best = null;
    double bestDist = 0.0D;

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE)
      {
        if (!cur.isDead())
        {
          if ((ignore == null) || (!ignore.equals(cur)))
          {
            double dist = UtilMath.offset(cur.getLocation(), loc);

            if ((best == null) || (dist < bestDist))
            {
              best = cur;
              bestDist = dist;
            }
          }
        }
      }
    }
    return best;
  }

  public static HashMap<Player, Double> getInRadius(Location loc, double dR)
  {
    HashMap players = new HashMap();

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE)
      {
        double offset = UtilMath.offset(loc, cur.getLocation());

        if (offset < dR)
          players.put(cur, Double.valueOf(1.0D - offset / dR));
      }
    }
    return players;
  }

  public static boolean isChargingBow(Player player)
  {
    if (!UtilGear.isMat(player.getItemInHand(), Material.BOW)) {
      return false;
    }
    return (((CraftEntity)player).getHandle().getDataWatcher().getByte(0) & 0x10) != 0;
  }
}