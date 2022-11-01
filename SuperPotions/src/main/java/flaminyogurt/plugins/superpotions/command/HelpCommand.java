package flaminyogurt.plugins.superpotions.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class HelpCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("help");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.help");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    sender.sendMessage(ChatColor.AQUA + "---------------" + ChatColor.GREEN + "SuperPotions" + ChatColor.AQUA + "---------------");
    sender.sendMessage(ChatColor.GREEN + "/potion" + ChatColor.AQUA + " Brings you to this help page");
    sender.sendMessage(ChatColor.GREEN + "/potion effect <effectType> <seconds> <level> [player]" + ChatColor.AQUA + " Applies a potion effect to a player");
    sender.sendMessage(ChatColor.GREEN + "/potion create <effectType> <seconds> <level> <splash> [player]" + ChatColor.AQUA + " Gives a custom potion to a player");
    sender.sendMessage(ChatColor.GREEN + "/potion all <effectType> <seconds> <level> (radius)" + ChatColor.AQUA + " Applies a potion effect to all server players or all players in a radius");
    
    sender.sendMessage(ChatColor.GREEN + "/potion removeall [player/all]" + ChatColor.AQUA + " Removes all potion effects from a player or the whole server");
    sender.sendMessage(ChatColor.GREEN + "/potion kit [kitname] [player]" + ChatColor.AQUA + " Displays available options or purchases a kit. Including a player gifts the kit.");
    
    sender.sendMessage(ChatColor.GREEN + "/potion list" + ChatColor.AQUA + " Lists all possible effect types");
    sender.sendMessage(ChatColor.GREEN + "/potion maxtime [seconds]" + ChatColor.AQUA + " Sets/Displays the max duration of potion effects");
    sender.sendMessage(ChatColor.GREEN + "/potion maxlevel [level]" + ChatColor.AQUA + " Sets/Displays the max level of potion effects");
    sender.sendMessage(ChatColor.GREEN + "/potion replaceeffects [true/false]" + ChatColor.AQUA + " Sets/Displays whether commands will overwrite current effects");
    sender.sendMessage(ChatColor.GREEN + "/potion ambient [true/false]" + ChatColor.AQUA + " Sets/Displays whether potion effect particles are transparent");
    sender.sendMessage(ChatColor.GREEN + "/potion messages [true/false]" + ChatColor.AQUA + " Sets/Displays whether to send messages to players");
    sender.sendMessage(ChatColor.GREEN + "/potion reload" + ChatColor.AQUA + " Reloads the config");
    return true;
  }
}
