package flaminyogurt.plugins.superpotions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners
  implements Listener
{
  private final SuperPotions plugin;
  
  public Listeners(SuperPotions instance)
  {
    this.plugin = instance;
  }
}
