package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;

/**
 * Created by noname on 9/2/2017.
 */
public class CommandListener implements Listener {
    Main plugin;
    public CommandListener(Main main){
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCMDLimit(PlayerCommandPreprocessEvent event) throws IOException {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (plugin.connect.servers != null) {
            for (String s : plugin.connect.servers) {
                if (event.getMessage().equalsIgnoreCase("/" + s)) {
                    event.setCancelled(true);
                    player.chat("/moveto " + s);
                    return;
                }
            }
        }
    }
}
