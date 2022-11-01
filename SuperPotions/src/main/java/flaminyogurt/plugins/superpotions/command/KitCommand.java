package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.Kits;
import flaminyogurt.plugins.superpotions.SuperPotions;
import flaminyogurt.plugins.superpotions.libs.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class KitCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("kit");
    labels.add("kits");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.kits");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    Kits kits = SuperPotions.instance.kits;
    Economy econ = SuperPotions.instance.economy;
    if (args.length == 1)
    {
      String kitsString = "Available kits are: ";
      for (String kit : kits.getKitConfig().getKeys(false)) {
        if ((!config.getBoolean("kit-permissions")) || (sender.hasPermission("superpotions.kits." + kit)))
        {
          if (config.getBoolean("economy")) {
            kit = kit + "(" + kits.getKitConfig().getConfigurationSection(kit).getDouble("cost") + ")";
          }
          kitsString = kitsString + kit + ", ";
        }
      }
      sender.sendMessage(ChatColor.AQUA + kitsString.substring(0, kitsString.length() - 2));
    }
    else if (args.length == 2)
    {
      if (!(sender instanceof Player))
      {
        sender.sendMessage("You must use that command in game!");
        return true;
      }
      if ((config.getBoolean("kit-permissions")) && (!sender.hasPermission("superpotions.kits." + args[1])))
      {
        sender.sendMessage(ChatColor.RED + "You don't have permission for this kit.");
        return false;
      }
      ConfigurationSection kitConfig = kits.getKitConfig().getConfigurationSection(args[1]);
      if (kitConfig != null)
      {
        double cost = kitConfig.getDouble("cost", 0.0D);
        if (config.getBoolean("economy"))
        {
          OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(((Player)sender).getUniqueId());
          EconomyResponse r = econ.withdrawPlayer(offlinePlayer, cost);
          if (r.transactionSuccess())
          {
            sender.sendMessage(String.format("You spent %s and now have %s", new Object[] { econ.format(r.amount), econ.format(r.balance) }));
          }
          else
          {
            sender.sendMessage(String.format("An error occurred: %s", new Object[] { r.errorMessage }));
            return false;
          }
        }
        for (String key : kitConfig.getConfigurationSection("potions").getKeys(false))
        {
          ConfigurationSection info = kitConfig.getConfigurationSection("potions." + key);
          String name = info.getString("name");
          int quantity = info.getInt("quantity", 1);
          boolean splash = info.getBoolean("splash", false);
          
          ConfigurationSection effects = info.getConfigurationSection("effects");
          ItemStack item = null;
          PotionMeta data = null;
          for (String effect : effects.getKeys(false))
          {
            if (item == null)
            {
              Potion potion = new Potion(PotionType.getByEffect((PotionEffectType)Constants.potions.get(effect)));
              potion.setSplash(splash);
              item = potion.toItemStack(quantity);
              data = (PotionMeta)item.getItemMeta();
              if (name != null) {
                data.setDisplayName(name);
              }
            }
            int level = effects.getConfigurationSection(effect).getInt("level");
            int seconds = effects.getConfigurationSection(effect).getInt("seconds");
            data.addCustomEffect(new PotionEffect((PotionEffectType)Constants.potions.get(effect), (effect.equalsIgnoreCase("heal")) || (effect.equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), false);
          }
          if (item != null)
          {
            item.setItemMeta(data);
            if ((item.getDurability() == 0) || (item.getDurability() == -1)) {
              if (splash) {
                item.setDurability((short)16390);
              } else {
                item.setDurability((short)8262);
              }
            }
            ((Player)sender).getInventory().addItem(new ItemStack[] { item });
          }
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Kit does not exist!");
      }
    }
    else if ((args.length == 3) && (sender.hasPermission("superpotions.kits.others")))
    {
      if ((config.getBoolean("kit-permissions")) && (!sender.hasPermission("superpotions.kits.others." + args[1])))
      {
        sender.sendMessage(ChatColor.RED + "You don't have permission to give this kit to others.");
        return false;
      }
      ConfigurationSection kitConfig = kits.getKitConfig().getConfigurationSection(args[1]);
      Player target;
      if (kitConfig != null)
      {
        target = Bukkit.getPlayer(args[2]);
        if (target == null)
        {
          sender.sendMessage(ChatColor.RED + "Player: " + ChatColor.GREEN + args[4] + ChatColor.RED + " is not online!");
          return false;
        }
        double cost = kitConfig.getDouble("cost", 0.0D);
        if ((config.getBoolean("economy")) && ((sender instanceof Player)))
        {
          OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(((Player)sender).getUniqueId());
          EconomyResponse r = econ.withdrawPlayer(offlinePlayer, cost);
          if (r.transactionSuccess())
          {
            sender.sendMessage(String.format("You spent %s and now have %s", new Object[] { econ.format(r.amount), econ.format(r.balance) }));
          }
          else
          {
            sender.sendMessage(String.format("An error occurred: %s", new Object[] { r.errorMessage }));
            return false;
          }
        }
        for (String key : kitConfig.getConfigurationSection("potions").getKeys(false))
        {
          ConfigurationSection info = kitConfig.getConfigurationSection("potions." + key);
          String name = info.getString("name");
          int quantity = info.getInt("quantity", 1);
          boolean splash = info.getBoolean("splash", false);
          
          ConfigurationSection effects = info.getConfigurationSection("effects");
          ItemStack item = null;
          PotionMeta data = null;
          for (String effect : effects.getKeys(false))
          {
            if (item == null)
            {
              Potion potion = new Potion(PotionType.getByEffect((PotionEffectType)Constants.potions.get(effect)));
              potion.setSplash(splash);
              item = potion.toItemStack(quantity);
              data = (PotionMeta)item.getItemMeta();
              if (name != null) {
                data.setDisplayName(name);
              }
            }
            int level = effects.getConfigurationSection(effect).getInt("level");
            int seconds = effects.getConfigurationSection(effect).getInt("seconds");
            data.addCustomEffect(new PotionEffect((PotionEffectType)Constants.potions.get(effect), (effect.equalsIgnoreCase("heal")) || (effect.equalsIgnoreCase("harm")) ? 1 : seconds * 20, level - 1, config.getBoolean("ambient")), false);
          }
          if (item != null)
          {
            item.setItemMeta(data);
            if ((item.getDurability() == 0) || (item.getDurability() == -1)) {
              if (splash) {
                item.setDurability((short)16390);
              } else {
                item.setDurability((short)8262);
              }
            }
            target.getInventory().addItem(new ItemStack[] { item });
          }
        }
      }
      else
      {
        sender.sendMessage(ChatColor.RED + "Kit does not exist!");
      }
    }
    else
    {
      sender.sendMessage(ChatColor.RED + "SYNTAX ERROR: WRONG ARGUMENTS: " + ChatColor.GREEN + "/potion kit [kitname] [player]");
    }
    return true;
  }
}
