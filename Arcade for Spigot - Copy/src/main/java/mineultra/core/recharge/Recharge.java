package mineultra.core.recharge;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import mineultra.core.MiniPlugin;
import mineultra.core.common.util.F;
import mineultra.core.common.util.NautHashMap;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Recharge extends MiniPlugin
{
  public static Recharge Instance;
  public HashSet<String> informSet = new HashSet();
  public NautHashMap<String, NautHashMap<String, RechargeData>> _recharge = new NautHashMap();

  protected Recharge(JavaPlugin plugin)
  {
    super("Recharge", plugin);
  }

  public static void Initialize(JavaPlugin plugin)
  {
    Instance = new Recharge(plugin);
  }

  @EventHandler
  public void PlayerDeath(PlayerDeathEvent event)
  {
    Get(event.getEntity().getName()).clear();
  }

  public NautHashMap<String, RechargeData> Get(String name)
  {
    if (!_recharge.containsKey(name)) {
      _recharge.put(name, new NautHashMap());
    }
    return (NautHashMap)_recharge.get(name);
  }

  public NautHashMap<String, RechargeData> Get(Player player)
  {
    return Get(player.getName());
  }

  @EventHandler
  public void update(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    recharge();
  }

  public void recharge()
  {
    for (Player cur : UtilServer.getPlayers())
    {
      LinkedList rechargeList = new LinkedList();

      for (String ability : Get(cur).keySet())
      {
        if (((RechargeData)Get(cur).get(ability)).Update()) {
          rechargeList.add(ability);
        }
      }

      for (String ability : (List<String>)rechargeList)
      {
        Get(cur).remove(ability);

        RechargedEvent rechargedEvent = new RechargedEvent(cur, ability);
        UtilServer.getServer().getPluginManager().callEvent(rechargedEvent);

        if (informSet.contains(ability))
          UtilPlayer.message(cur, F.main("Recharge", "You can use " + F.skill(ability) + "."));
      }
    }
  }

  public boolean use(Player player, String ability, long recharge, boolean inform, boolean attachItem)
  {
    return use(player, ability, ability, recharge, inform, attachItem);
  }

  public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform, boolean attachItem)
  {
    if (recharge == 0L) {
      return true;
    }

    recharge();

    if (inform) {
      informSet.add(ability);
    }

    if (Get(player).containsKey(ability))
    {
      if (inform)
      {
        UtilPlayer.message(player, F.main("Recharge", "You cannot use " + F.skill(abilityFull) + " for " + 
          F.time(UtilTime.convertString(((RechargeData)Get(player).get(ability)).GetRemaining(), 1, UtilTime.TimeUnit.FIT)) + "."));
      }

      return false;
    }

    UseRecharge(player, ability, recharge, attachItem);

    return true;
  }

  public void useForce(Player player, String ability, long recharge)
  {
    UseRecharge(player, ability, recharge, false);
  }

  public boolean usable(Player player, String ability)
  {
    if (!Get(player).containsKey(ability)) {
      return true;
    }
    return System.currentTimeMillis() > ((RechargeData)Get(player).get(ability)).Time;
  }

  public void UseRecharge(Player player, String ability, long recharge, boolean attachItem)
  {
    RechargeEvent rechargeEvent = new RechargeEvent(player, ability, recharge);
    UtilServer.getServer().getPluginManager().callEvent(rechargeEvent);

    if (attachItem)
      Get(player).put(ability, new RechargeData(this, player, ability, player.getItemInHand(), rechargeEvent.GetRecharge()));
    else
      Get(player).put(ability, new RechargeData(System.currentTimeMillis() + rechargeEvent.GetRecharge()));
  }

  public void recharge(Player player, String ability)
  {
    Get(player).remove(ability);
  }

  @EventHandler
  public void clearPlayer(PlayerQuitEvent event)
  {
    _recharge.remove(event.getPlayer().getName());
  }

  public void Reset(Player player)
  {
    _recharge.put(player.getName(), new NautHashMap());
  }
}