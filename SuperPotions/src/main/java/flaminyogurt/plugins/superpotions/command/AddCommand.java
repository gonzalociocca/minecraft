package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.libs.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AddCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("add");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.create");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (!(sender instanceof Player))
    {
      sender.sendMessage("You must use that command in game!");
      return true;
    }
    Player player = (Player)sender;
    if (args.length == 4)
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
        ItemStack item = player.getItemInHand();
        if (item.getType() != Material.POTION)
        {
          player.sendMessage(ChatColor.RED + "Potion must be in your hand!");
          return true;
        }
        if ((config.getBoolean("effect-permissions")) && (!sender.hasPermission("superpotions.create." + args[1])))
        {
          sender.sendMessage(ChatColor.RED + "You don't have permission to create a potion with this effect.");
          return true;
        }
        PotionMeta data = (PotionMeta)item.getItemMeta();
        data.addCustomEffect(new PotionEffect((PotionEffectType)Constants.potions.get(args[1]), (args[1].equalsIgnoreCase("heal")) || (args[1].equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), config.getBoolean("replaceeffects"));
        
        item.setItemMeta(data);
        player.sendMessage(ChatColor.AQUA + "Potion effect added!");
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Potion Effect: " + args[1] + " is not an acceptable type! Do " + ChatColor.GREEN + "/potion list" + ChatColor.RED + " for a list of acceptable types.");
      }
    }
    else {
      sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: TOO FEW ARGUMENTS: " + ChatColor.GREEN + "/potion add <effectType> <seconds> <level>");
    }
    return true;
  }
}
