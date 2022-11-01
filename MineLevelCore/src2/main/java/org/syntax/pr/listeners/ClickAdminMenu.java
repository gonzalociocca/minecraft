package org.syntax.pr.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.syntax.pr.reports.Report;
import org.syntax.pr.reports.ReportManager;

public class ClickAdminMenu
        implements Listener
{
    ReportManager rm = ReportManager.getInstance();

    @EventHandler
    public void invInteract(InventoryClickEvent e)
    {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getWhoClicked();

        Inventory inv = e.getInventory();
        String name = ChatColor.stripColor(inv.getName());
        if (!name.contains("[R] ")) {
            return;
        }
        if(name.endsWith(".")){
            return;
        }
        ItemStack i = e.getCurrentItem();
        if (i == null) {
            return;
        }
        if (!i.hasItemMeta()) {
            return;
        }
        ItemMeta meta = i.getItemMeta();

        e.setCancelled(true);
        if (i.getType() == Material.SIGN)
        {
            String[] list = ChatColor.stripColor(meta.getDisplayName()).split("'");
            String listname = list[0].split(" ")[2];
            if (!this.rm.contains(listname))
            {
                p.closeInventory();
                return;
            }
            Report report = this.rm.get(listname);
            p.closeInventory();
            this.rm.showReportMenu(p, report);
        }
        else
        {
            String[] args = ChatColor.stripColor(meta.getDisplayName()).split(" ");
            Player target = p.getServer().getPlayer(args[(args.length - 1)]);
            String targetname = args[(args.length - 1)];

            if (args[0].equalsIgnoreCase("Banear"))
            {
                p.closeInventory();
                p.sendMessage(ChatColor.DARK_GREEN+"Razones de Ban: Permanente, Hacks, Grifear, Bugear, Insultos");
                p.sendMessage(ChatColor.GREEN+"Usa /ban "+targetname+" <razon>");
            }
            else if (target == null)
            {
                p.sendMessage(ChatColor.DARK_RED + "- " + ChatColor.RED + ChatColor.ITALIC + "Jugador no encontrado.");
                return;
            }
            else if (args[0].equalsIgnoreCase("Kickear"))
            {
                target.kickPlayer(ChatColor.DARK_RED + "Has sido kickeado, despues de ser reportado.");
                p.closeInventory();
                p.sendMessage(ChatColor.DARK_GREEN + "Jugador kickeado.");
                return;
            }
            else if (args[0].equalsIgnoreCase("Teletransportarse"))
            {
                p.teleport(target.getLocation());
                p.closeInventory();
                p.sendMessage(ChatColor.DARK_GREEN + "Te teletransportaste a " + target.getName());
            }
            else if (args[0].equalsIgnoreCase("Enviar"))
            {
                p.closeInventory();
                this.rm.showPlayerMessageMenu(p, targetname);
                return;
            }

        }
    }
}
