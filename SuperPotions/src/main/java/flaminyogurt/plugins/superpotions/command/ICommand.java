package flaminyogurt.plugins.superpotions.command;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public abstract interface ICommand
{
  public abstract List<String> getLabels();
  
  public abstract boolean senderCanExecute(CommandSender paramCommandSender);
  
  public abstract boolean execute(CommandSender paramCommandSender, String[] paramArrayOfString, FileConfiguration paramFileConfiguration);
}
