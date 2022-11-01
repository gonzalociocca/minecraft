package org.syntax.pr.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.syntax.pr.reports.Report;
import org.syntax.pr.reports.ReportManager;

public class ClickOnMainMenu
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
        if (!name.equalsIgnoreCase("Menu de Reportes")) {
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

        String[] a = meta.getDisplayName().split("'");

        if (!this.rm.contains(ChatColor.stripColor(a[0]))) {
            return;
        }

        Report r = this.rm.get(ChatColor.stripColor(a[0]),ChatColor.stripColor(meta.getLore().get(2)).substring(9));

        p.closeInventory();
        this.rm.showReportMenu(p, r);
    }
}
