package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by noname on 9/2/2017.
 */
public class MoveToCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("moveto") && sender instanceof Player) {
            Player player = (Player) sender;
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("Connect");
                out.writeUTF(args[0]);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            return true;
        }
        return false;
    }
}
