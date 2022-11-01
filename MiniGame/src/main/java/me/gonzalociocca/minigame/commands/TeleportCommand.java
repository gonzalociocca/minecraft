package me.gonzalociocca.minigame.commands;

import me.gonzalociocca.minigame.Core;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 28/3/2017.
 */
public class TeleportCommand {
    public static boolean onCommand(Core plugin, boolean isPlayer, boolean isConsole, CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("teleport")) {//teleport wolrd x y z
            if(args.length == 4){
                String worldname = args[0];
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);
                Player player = (Player)sender;
                player.teleport(new Location(Bukkit.getWorld(worldname),x,y,z));
                return true;
            }
        }
        return false;
    }
}
