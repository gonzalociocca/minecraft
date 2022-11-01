package org.syntax.pr.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.syntax.pr.reports.Report;
import org.syntax.pr.reports.ReportManager;
import org.syntax.pr.reports.Status;

public class ClickOnReportMenu
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
        if (!name.contains("[R]")) {
            return;
        }
        ItemStack i = e.getCurrentItem();
        if (i == null) {
            return;
        }
        if (!i.hasItemMeta()) {
            return;
        }
        e.setCancelled(true);

        String[] a = ChatColor.stripColor(inv.getName()).split(" ");
        String b = a[1].substring(0, a[1].length() - 2);
        if (!this.rm.contains(b)) {
            return;
        }
        Report r = this.rm.get(b);

        if (i.getType() == Material.BARRIER)
        {
            p.closeInventory();
            this.rm.delete(r.getReporter());
            p.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + ChatColor.ITALIC + "Reporte eliminado.");
        }
        else if (i.getType() == Material.SIGN)
        {
            p.closeInventory();
            this.rm.showMenuOne(p);
        }
        else if (i.getType() == Material.SKULL_ITEM)
        {
            SkullMeta meta = (SkullMeta)i.getItemMeta();
            String[] z = meta.getDisplayName().split(" ");
            String zname = ChatColor.stripColor(z[1]);
            p.closeInventory();
            this.rm.showPlayerActionMenu(p, zname, r);
        }
    }
}
