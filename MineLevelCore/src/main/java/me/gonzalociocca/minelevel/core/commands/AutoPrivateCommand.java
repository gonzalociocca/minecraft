package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by noname on 9/2/2017.
 */
public class AutoPrivateCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (Variable.ServerType.equals(SvType.Factions) && cmd.getName().equalsIgnoreCase("auto")) {
            if (Variable.AutoPrivates.get(sender.getName())) {
                Variable.AutoPrivates.put(sender.getName(), false);
                sender.sendMessage(Code.Color("&c[MineLevel] &2Has desactivado AutoPrivatizacion, usa &a/auto&2 nuevamente para activarlo"));
            } else {
                Variable.AutoPrivates.put(sender.getName(), true);
                sender.sendMessage(Code.Color("&c[MineLevel] &2Has activado AutoPrivatizacion, usa &a/auto&2 nuevamente para desactivarlo"));
            }
            return true;
        }
        return false;
    }
}
