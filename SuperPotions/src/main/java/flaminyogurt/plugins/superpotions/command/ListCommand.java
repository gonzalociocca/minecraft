package flaminyogurt.plugins.superpotions.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ListCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("list");
    labels.add("l");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return (sender.hasPermission("superpotions.effect")) || (sender.hasPermission("superpotions.create"));
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    sender.sendMessage(ChatColor.AQUA + "Available potion effect types are: blindness, confusion, resistance, haste, fireresistance, hunger, strength, jump, poison, regen, slow, fatigue, speed, breathing, weakness, heal, harm, wither, invisibility, nightvision, absorption, healthboost, saturation.");
    
    return true;
  }
}
