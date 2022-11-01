package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.SuperPotions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand
  implements ICommand
{
  public List<String> getLabels()
  {
    List<String> labels = new ArrayList();
    
    labels.add("reload");
    
    return labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.help");
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    SuperPotions.instance.reloadConfig();
    return true;
  }
}
