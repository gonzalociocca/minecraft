package flaminyogurt.plugins.superpotions.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class BooleanConfigCommand
  extends ConfigCommand
{
  private final String messageCurrent;
  private final String messageSet;
  private final String messageTrue;
  private final String messageFalse;
  
  public BooleanConfigCommand(String configItem, String messageCurrent, String messageSet, String messageTrue, String messageFalse, String... labels)
  {
    super(configItem, labels);
    this.messageCurrent = messageCurrent;
    this.messageSet = messageSet;
    this.messageTrue = messageTrue;
    this.messageFalse = messageFalse;
  }
  
  public boolean execute(CommandSender sender, String[] args, FileConfiguration config)
  {
    if (args.length == 1)
    {
      sender.sendMessage(ChatColor.AQUA + this.messageCurrent + (config.getBoolean(this.configItem) ? this.messageTrue : this.messageFalse));
    }
    else
    {
      config.set(this.configItem, Boolean.valueOf(Boolean.parseBoolean(args[1])));
      sender.sendMessage(ChatColor.AQUA + this.messageSet + (config.getBoolean(this.configItem) ? this.messageTrue : this.messageFalse));
    }
    return true;
  }
}
