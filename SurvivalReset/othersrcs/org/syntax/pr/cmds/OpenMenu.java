package org.syntax.pr.cmds;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.syntax.pr.reports.ReportManager;

public class OpenMenu
        implements CommandExecutor
{
    ReportManager rm = ReportManager.getInstance();
    Main plugin;
    FileConfiguration config;

    public OpenMenu(Main plugin)
    {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("No puedes usar este comando si no eres un jugador.");
            return false;
        }
        Player p = (Player)sender;
        if (!plugin.getPlayerData(p.getName()).getRank().isAtLeast(Rank.Helper))
        {
            p.sendMessage(ChatColor.DARK_RED + "- " + ChatColor.RED + ChatColor.ITALIC + "No tienes permisos para usar este comando.");
            return false;
        }
        if (args.length > 0)
        {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.config.getString("CustomMessage.ReportsHelp")));
            return false;
        }
        this.rm.showMenuOne(p);
        return false;
    }
}
