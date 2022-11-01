package org.syntax.pr.cmds;

import me.gonzalociocca.minelevel.core.Rank;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import me.gonzalociocca.minelevel.core.Main;
import org.syntax.pr.reports.ReportManager;

public class ReportPlayer
        implements CommandExecutor
{
    ReportManager rm = ReportManager.getInstance();
    Main plugin;
    FileConfiguration config;

    public ReportPlayer(Main plugin)
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
        /*
        free permission.
        if (!p.hasPermission("ProReports.report"))
        {
            p.sendMessage(ChatColor.DARK_RED + "- " + ChatColor.RED + ChatColor.ITALIC + "You do not have permission to use this command.");
            return false;
        }*/
        if(rm.contains(p.getName())){
            p.sendMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"Espera a que tus antiguos reportes hayan sido revisados!");
        return false;
        }
        if (args.length < 2)
        {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.config.getString("CustomMessage.ReportHelp")));
            return false;
        }
        String target = args[0];

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i] + " ");
        }
        String a = sb.toString().trim();
        String reason = WordUtils.capitalize(a.toLowerCase());
        if ((!reason.endsWith(".")) && (!reason.endsWith("!")) && (!reason.endsWith("?"))) {
            reason = reason + ".";
        }
        for (Player admin : p.getServer().getOnlinePlayers()) {
            if (plugin.getPlayerData(admin.getName()).getRank().isAtLeast(Rank.Helper))
            {
                admin.sendMessage(ChatColor.GREEN +""+ ChatColor.BOLD + ChatColor.ITALIC + " Nuevo reporte!");
                admin.playSound(admin.getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
            }
        }
        p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + ChatColor.ITALIC + "Jugador reportado! Un staff observara el caso pronto.");

        if(Bukkit.getPlayer(target) != null) {
            Bukkit.getPlayer(target).sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ChatColor.ITALIC + "Has sido reportado! " + ChatColor.GRAY + ChatColor.ITALIC + "Un admin observara el caso.");
            Bukkit.getPlayer(target).playSound(Bukkit.getPlayer(target).getLocation(), Sound.ORB_PICKUP, 10.0F, 10.0F);
        }
        this.rm.add(p.getName(), target, reason);
        return false;
    }
}
