package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.libs.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AllCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("all");
    labels.add("a");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.all");
  }
  
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
          seconds = (args[1].equalsIgnoreCase("heal")) || (args[1].equalsIgnoreCase("harm")) ? 1 : Integer.parseInt(args[2]) * 20;
          level = Integer.parseInt(args[3]) - 1;
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
        if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.all." + args[1])))
        {
          sender.sendMessage(ChatColor.RED + "You don't have permission to give this potion effect to all players.");
          return true;
        }
        int radius;
        Location senderLoc;
        if (args.length == 4)
        {
          for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            if (target != null) {
              if ((!target.hasMetadata("SuperPotions_opt")) || (((MetadataValue)target.getMetadata("SuperPotions_opt").get(0)).asBoolean()))
              {
                target.addPotionEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), seconds, level, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
                if (config.getBoolean("messages")) {
                  target.sendMessage(ChatColor.AQUA + "You were given a potion effect: " + args[1] + "!");
                }
              }
            }
          }
        }
        else
        {
          try
          {
            radius = Integer.parseInt(args[4]);
          }
          catch (NumberFormatException e)
          {
            sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: INVALID ARGUMENT TYPE!");
            return false;
          }
    
          if ((sender instanceof Player))
          {
            senderLoc = ((Player)sender).getLocation();
          }
          else
          {
            if ((sender instanceof BlockCommandSender))
            {
              senderLoc = ((BlockCommandSender)sender).getBlock().getLocation();
            }
            else
            {
              sender.sendMessage(ChatColor.RED + "Cannot use this command from the console.");
              return false;
            }
          }
          for (Player target : Bukkit.getServer().getOnlinePlayers()) {
            if ((target != null) && (senderLoc.distanceSquared(target.getLocation()) < radius * radius)) {
              if ((!target.hasMetadata("SuperPotions_opt")) || (((MetadataValue)target.getMetadata("SuperPotions_opt").get(0)).asBoolean()))
              {
                target.addPotionEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), seconds, level, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
                if (config.getBoolean("messages")) {
                  target.sendMessage(ChatColor.AQUA + "You were given a potion effect: " + args[1] + "!");
                }
              }
            }
          }
        }
        if (config.getBoolean("messages")) {
          sender.sendMessage(ChatColor.AQUA + "Potion effects successfully given to all players!");
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Potion Effect: " + args[1] + " is not an acceptable type! Do " + ChatColor.GREEN + "/potion list" + ChatColor.RED + " for a list of acceptable types.");
      }
    }
    else {
      sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: " + ChatColor.GREEN + "/potion all <effectType> <seconds> <level> (radius)");
    }
    return true;
  }
}
