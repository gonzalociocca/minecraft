package mineultra.core.recharge;

import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RechargeData
{
  public Recharge Host;
  public long Time;
  public long Recharge;
  public Player Player;
  public String Name;
  public ItemStack Item;

  public RechargeData(long time)
  {
    Time = time;
  }

  public RechargeData(Recharge host, Player player, String name, ItemStack stack, long rechargeTime)
  {
    Host = host;

    Player = player;
    Name = name;
    Item = player.getItemInHand();
    Time = System.currentTimeMillis();
    Recharge = rechargeTime;
  }

  public boolean Update()
  {
    if ((Item != null) && (Name != null) && (Player != null))
    {
      try
      {
        if (Player.getItemInHand().getType() == Item.getType())
        {
          if (!UtilTime.elapsed(Time, Recharge))
          {
            double percent = (System.currentTimeMillis() - Time) / Recharge;

            UtilDisplay.displayTextBar(Host.GetPlugin(), Player, percent, C.cRed + C.Bold + Name + ChatColor.RESET + " - " + 
              C.cYellow + C.Bold + UtilTime.MakeStr(Recharge - (System.currentTimeMillis() - Time)));
          }
          else
          {
            UtilDisplay.displayTextBar(Host.GetPlugin(), Player, 1.0D, C.cGreen + C.Bold + Name);

            if (Recharge > 4000L)
              Player.playSound(Player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.4F, 3.0F);
          }
        }
      }
      catch (Exception e)
      {
        System.out.println("Recharge Indicator Error!");
        e.printStackTrace();
      }

    }

    return UtilTime.elapsed(Time, Recharge);
  }

  public long GetRemaining()
  {
    return Recharge - (System.currentTimeMillis() - Time);
  }
}