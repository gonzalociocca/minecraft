package flaminyogurt.plugins.superpotions.command;

import flaminyogurt.plugins.superpotions.SuperPotions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class OptCommand
  implements ICommand
{
  private final Boolean opt;
  private final List<String> labels = new ArrayList();
  
  public OptCommand(Boolean value, String... labels)
  {
    this.opt = value;
    this.labels.addAll(Arrays.asList(labels));
  }
  
  public List<String> getLabels()
  {
    return this.labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return (SuperPotions.instance.config.getBoolean("allow-opt")) && (sender.hasPermission("superpotions.opt"));
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if ((sender instanceof Player))
    {
      ((Player)sender).setMetadata("SuperPotions_opt", new FixedMetadataValue(SuperPotions.instance, this.opt));
      sender.sendMessage(ChatColor.AQUA + "You have opted " + (this.opt.booleanValue() ? "in for" : "out of") + " /potion all effects");
    }
    else
    {
      sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
    }
    return true;
  }
}
