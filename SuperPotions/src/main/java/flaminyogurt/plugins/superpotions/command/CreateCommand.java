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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CreateCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("create");
    labels.add("c");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.create");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (args.length >= 5)
    {
      if (Constants.potions.get(args[1]) != null)
      {
        boolean splash = Boolean.parseBoolean(args[4]);
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
        Potion potion = new Potion(PotionType.getByEffect((PotionEffectType)Constants.potions.get(args[1])));
        potion.setSplash(splash);
        ItemStack item = potion.toItemStack(1);
        PotionMeta data = (PotionMeta)item.getItemMeta();
        data.addCustomEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), (args[1].equalsIgnoreCase("heal")) || (args[1].equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
        
        item.setItemMeta(data);
        if ((item.getDurability() == 0) || (item.getDurability() == -1)) {
          if (splash) {
            item.setDurability((short)16390);
          } else {
            item.setDurability((short)8262);
          }
        }
        if (args.length == 5)
        {
          if (!(sender instanceof Player))
          {
            sender.sendMessage("You must use that command in game!");
            return false;
          }
          if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.create." + args[1])))
          {
            sender.sendMessage(ChatColor.RED + "You don't have permission to create a potion with this effect.");
          }
          else
          {
            ((Player)sender).getInventory().addItem(new ItemStack[] { item });
            if (config.getBoolean("messages")) {
              sender.sendMessage(ChatColor.AQUA + "Here is your potion!");
            }
          }
        }
        else if (sender.hasPermission("superpotions.create.others"))
        {
          Player target = Bukkit.getServer().getPlayer(args[5]);
          if (target != null)
          {
            if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.create.others." + args[1])))
            {
              sender.sendMessage(ChatColor.RED + "You don't have permission to create a potion with this effect for others.");
            }
            else
            {
              target.getInventory().addItem(new ItemStack[] { item });
              if (config.getBoolean("messages"))
              {
                sender.sendMessage(ChatColor.AQUA + "Potion successfully given to " + ChatColor.GREEN + target.getDisplayName() + ChatColor.AQUA + "!");
                target.sendMessage(ChatColor.AQUA + "You were given a custom potion: " + args[1] + "!");
              }
            }
          }
          else {
            sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GREEN + args[5] + ChatColor.RED + " is not online!");
          }
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Potion Effect: " + args[1] + " is not an acceptable type! Do " + ChatColor.GREEN + "/potion list" + ChatColor.RED + " for a list of acceptable types.");
      }
    }
    else {
      sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: TOO FEW ARGUMENTS: " + ChatColor.GREEN + "/potion create <effectType> <seconds> <level> <splash> [player]");
    }
    return true;
  }
}
