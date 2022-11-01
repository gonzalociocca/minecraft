package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.listeners.BankListener;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 9/2/2017.
 */
public class BankCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equalsIgnoreCase("banco")) {
            if (sender instanceof Player) {
                BankListener.openBanco((Player) sender);
                sender.sendMessage(Code.Color("&aAbriendo menu del banco..."));
                return true;
            } else {
                sender.sendMessage(Code.Color("Solo jugadores pueden abrir el banco"));
                return true;
            }
        }
        return false;
    }
}
