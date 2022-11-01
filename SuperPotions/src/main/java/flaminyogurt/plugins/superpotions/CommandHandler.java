package flaminyogurt.plugins.superpotions;

import flaminyogurt.plugins.superpotions.command.AddCommand;
import flaminyogurt.plugins.superpotions.command.AllCommand;
import flaminyogurt.plugins.superpotions.command.BooleanConfigCommand;
import flaminyogurt.plugins.superpotions.command.CreateCommand;
import flaminyogurt.plugins.superpotions.command.EffectCommand;
import flaminyogurt.plugins.superpotions.command.HelpCommand;
import flaminyogurt.plugins.superpotions.command.ICommand;
import flaminyogurt.plugins.superpotions.command.IntegerConfigCommand;
import flaminyogurt.plugins.superpotions.command.KitCommand;
import flaminyogurt.plugins.superpotions.command.ListCommand;
import flaminyogurt.plugins.superpotions.command.OptCommand;
import flaminyogurt.plugins.superpotions.command.ReloadCommand;
import flaminyogurt.plugins.superpotions.command.RemoveCommand;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler
  implements CommandExecutor
{
  private final Map<String, ICommand> commands = new HashMap();
  
  public CommandHandler()
  {
    registerCommand(new HelpCommand());
    registerCommand(new EffectCommand());
    registerCommand(new AllCommand());
    registerCommand(new ListCommand());
    registerCommand(new RemoveCommand());
    registerCommand(new CreateCommand());
    registerCommand(new AddCommand());
    registerCommand(new KitCommand());
    registerCommand(new ReloadCommand());
    registerCommand(new OptCommand(Boolean.valueOf(true), new String[] { "optin" }));
    registerCommand(new OptCommand(Boolean.valueOf(false), new String[] { "optout" }));
    registerCommand(new IntegerConfigCommand("maxlevel", "The current max level is ", "Max level set to ", new String[] { "maxlevel" }));
    registerCommand(new IntegerConfigCommand("maxduration", "The current max duration is ", "Max duration set to ", new String[] { "maxtime" }));
    registerCommand(new BooleanConfigCommand("replaceeffects", "You will ", "You will now ", "overwrite existing effects.", "keep existing effects", new String[] { "replaceeffects" }));
    registerCommand(new BooleanConfigCommand("ambient", "Particles will be ", "Particles will now be ", "transparent.", "opaque.", new String[] { "ambient" }));
    registerCommand(new BooleanConfigCommand("messages", "Messages ", "Now messages ", "will be sent to players.", "will be sent to players.", new String[] { "messages" }));
    registerCommand(new BooleanConfigCommand("allow-opt", "Players currently ", "Players now ", "can opt out of /potion all.", "can't opt out of /potion all.", new String[] { "optconfig" }));
  }
  
  private void registerCommand(ICommand command)
  {
    for (String alias : command.getLabels()) {
      this.commands.put(alias, command);
    }
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((label.equalsIgnoreCase("potion")) || (label.equalsIgnoreCase("sp")) || (label.equalsIgnoreCase("pe")))
    {
      ICommand command = args.length > 0 ? (ICommand)this.commands.get(args[0].toLowerCase()) : (ICommand)this.commands.get("help");
      if ((command != null) && (command.senderCanExecute(sender))) {
        return command.execute(sender, args, SuperPotions.instance.config);
      }
    }
    return true;
  }
}
