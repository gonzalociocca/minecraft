package flaminyogurt.plugins.superpotions.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class RemoveCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("removeall");
    labels.add("remove");
    labels.add("r");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.removeall");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (args.length == 1)
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage("You must use that command in game!");
        return false;
      }
      for (PotionEffect effect : ((Player)sender).getActivePotionEffects()) {
        ((Player)sender).removePotionEffect(effect.getType());
      }
      if (config.getBoolean("messages")) {
        sender.sendMessage(ChatColor.AQUA + "Potion effects successfully removed!");
      }
    }
    else if (sender.hasPermission("superpotions.removeall.others"))
    {
      if ((args[1].equalsIgnoreCase("all")) && (sender.hasPermission("superpotions.all")))
      {
        for (Player target : Bukkit.getServer().getOnlinePlayers()) {
          if (target != null)
          {
            for (PotionEffect effect : target.getActivePotionEffects()) {
              target.removePotionEffect(effect.getType());
            }
            if (config.getBoolean("messages")) {
              target.sendMessage(ChatColor.AQUA + "Potion effects were removed from all players on the server!");
            }
          }
        }
        if (config.getBoolean("messages")) {
          sender.sendMessage(ChatColor.AQUA + "Potion effects successfully removed from all players!");
        }
      }
      else
      {
        Player target = Bukkit.getServer().getPlayer(args[1]);
        if (target != null)
        {
          for (PotionEffect effect : target.getActivePotionEffects()) {
            target.removePotionEffect(effect.getType());
          }
          if (config.getBoolean("messages"))
          {
            target.sendMessage(ChatColor.AQUA + "All your potion effects were removed!");
            sender.sendMessage(ChatColor.AQUA + "Potion effects successfully removed from " + ChatColor.GREEN + target.getDisplayName() + ChatColor.AQUA + "!");
          }
        }
        else
        {
          sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GREEN + args[1] + ChatColor.RED + " is not online!");
        }
      }
    }
    return true;
  }
}
