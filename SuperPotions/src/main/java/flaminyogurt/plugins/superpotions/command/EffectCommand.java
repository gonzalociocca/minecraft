package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.libs.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectCommand
  implements ICommand
{
  @Override
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("effect");
    labels.add("e");
    
    return labels;
  }
  
  @Override
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.effect");
  }
  
  @Override
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (args.length >= 4)
    {
      if (Constants.potions.get(args[1]) != null)
      {
        int seconds;
        int level;
        try
        {
          seconds = Integer.parseInt(args[2]);
          level = Integer.parseInt(args[3]);
          if (config.getInt("maxduration") > 0) {
            seconds = Math.min(seconds, config.getInt("maxduration"));
          }
          if (config.getInt("maxlevel") > 0) {
            level = Math.min(level, config.getInt("maxlevel"));
          }
        }
        catch (NumberFormatException e)
        {
          sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: INVALID ARGUMENT TYPE!");
          return false;
        }
        if (args.length == 4)
        {
          if (!(sender instanceof Player))
          {
            sender.sendMessage("You must use that command in game!");
            return false;
          }
          if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.effect." + args[1])))
          {
            sender.sendMessage(ChatColor.RED + "You don't have permission for this potion effect.");
          }
          else
          {
            ((Player)sender).addPotionEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), (args[1].equalsIgnoreCase("heal")) || (args[1].equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
            if (config.getBoolean("messages")) {
              sender.sendMessage(ChatColor.AQUA + "Potion effects successfully given!");
            }
          }
        }
        else if (sender.hasPermission("superpotions.effect.others"))
        {
          Player target = Bukkit.getServer().getPlayer(args[4]);
          if (target != null)
          {
            if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.effect.others." + args[1])))
            {
              sender.sendMessage(ChatColor.RED + "You don't have permission to give this potion effect to others.");
            }
            else
            {
              target.addPotionEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), (args[1].equalsIgnoreCase("heal")) || (args[1].equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
              if (config.getBoolean("messages"))
              {
                sender.sendMessage(ChatColor.AQUA + "Potion effects successfully given to " + ChatColor.GREEN + target.getDisplayName() + ChatColor.AQUA + "!");
                target.sendMessage(ChatColor.AQUA + "You were given a potion effect: " + args[1] + "!");
              }
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GREEN + args[4] + ChatColor.RED + " is not online!");
          }
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Potion Effect: " + args[1] + " is not an acceptable type! Do " + ChatColor.GREEN + "/potion list" + ChatColor.RED + " for a list of acceptable types.");
      }
    }
    else {
      sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: TOO FEW ARGUMENTS: " + ChatColor.GREEN + "/potion effect <effectType> <seconds> <level> [player]");
    }
    return true;
  }
}
