package flaminyogurt.plugins.superpotions.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class IntegerConfigCommand
  extends ConfigCommand
{
  private final String messageCurrent;
  private final String messageSet;
  
  public IntegerConfigCommand(String configItem, String messageCurrent, String messageSet, String... labels)
  {
    super(configItem, labels);
    this.messageCurrent = messageCurrent;
    this.messageSet = messageSet;
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (args.length == 1) {
      sender.sendMessage(ChatColor.AQUA + this.messageCurrent + ChatColor.GREEN + config.getInt(this.configItem));
    } else {
      try
      {
        config.set(this.configItem, Integer.valueOf(Integer.parseInt(args[1])));
        sender.sendMessage(ChatColor.AQUA + this.messageSet + ChatColor.GREEN + args[1]);
      }
      catch (NumberFormatException e)
      {
        sender.sendMessage(ChatColor.RED + "NOT A NUMBER!");
      }
    }
    return true;
  }
}
