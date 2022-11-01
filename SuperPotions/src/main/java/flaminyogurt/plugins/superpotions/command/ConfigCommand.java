package flaminyogurt.plugins.superpotions.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class ConfigCommand
  implements ICommand
{
  private final List<String> labels = new ArrayList();
  final String configItem;
  
  ConfigCommand(String configItem, String... labels)
  {
    this.configItem = configItem;
    this.labels.addAll(Arrays.asList(labels));
  }
  
  public List<String> getLabels()
  {
    return this.labels;
  }
  
  public boolean senderCanExecute(CommandSender sender)
  {
    return sender.hasPermission("superpotions.config");
  }
}
