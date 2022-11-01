package me.gonzalociocca.minelevel.core.commands;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by noname on 9/2/2017.
 */
public class ServerCommand {
    public static boolean run(Main plugin, CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("mine") && sender.hasPermission("minelevel.admin")
                || cmd.getName().equalsIgnoreCase("mine") && sender.isOp()) {
            if (args.length <= 0) {
                sender.sendMessage(Code.Color("&b&m-----&cComandos&b&m-----"));
                sender.sendMessage(Code.Color("&a/mine addrank <rank> <player> <days>"));
                sender.sendMessage(Code.Color("&a/mine adddiamonds <amount> <player>"));
                return true;
            }
            if (args[0].equalsIgnoreCase("addrank") && sender.hasPermission("minelevel.addrank") || args[0].equalsIgnoreCase("addrank") && sender instanceof Player && plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.OWNER)) {
                // /mine addrank <rank> <player> <days>
                if (args.length == 4) {
                    String rank = args[1];
                    String name = args[2];
                    plugin.getDB().insert(name, "");
                    Integer days = Integer.parseInt(args[3]);
                    PlayerData pd = plugin.getDB().getPlayerData(name);
                    pd.addRank(rank, days);
                    sender.sendMessage(Code.Color("&eRango &7" + rank + " &eañadido a &7" + name + " &epor &7" + days + "dias"));
                    return true;
                } else {
                    sender.sendMessage(Code.Color("&aUso: /mine addrank <rank> <player> <days>"));
                }
            } else if (args[0].equalsIgnoreCase("adddiamonds") && sender.hasPermission("minelevel.adddiamonds") || args[0].equalsIgnoreCase("adddiamonds") && sender instanceof Player && plugin.getDB().getPlayerData(sender.getName()).getRank().getType().isAtLeast(RankType.OWNER)) {
                // /mine addcoins <amount> <player>
                if (args.length == 3) {
                    int amount = Integer.parseInt(args[1]);
                    String name = args[2];
                    plugin.getDB().insert(name, "");
                    PlayerData pd = plugin.getDB().getPlayerData(name);
                    pd.addDiamonds(amount);
                    sender.sendMessage(Code.Color("&e" + amount + " &ediamantes añadidos a &7" + name));
                    return true;
                } else {
                    sender.sendMessage(Code.Color("&aUso: /mine adddiamonds <amount> <player>"));
                }
            }


            sender.sendMessage(Code.Color("&b&m-----&cComandos&b&m-----"));
            sender.sendMessage(Code.Color("&a/mine addrank <rank> <player> <days>"));
            sender.sendMessage(Code.Color("&a/mine adddiamonds <amount> <player>"));

            return true;
        }
        return false;
    }
}
