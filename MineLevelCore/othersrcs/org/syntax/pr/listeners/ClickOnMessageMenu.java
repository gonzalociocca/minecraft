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

public class ClickOnMessageMenu
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
        if (!name.contains("MSG:")) {
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
        if (i.getType() == Material.PAPER)
        {
            String invName = inv.getName();
            String[] sep = invName.split(" ");
            String playerName = ChatColor.stripColor(sep[1]);
            Player target = p.getServer().getPlayer(playerName);

            p.sendMessage(ChatColor.GREEN +""+ ChatColor.ITALIC + "Enviar a Jugador:");
            for (String s : meta.getLore())
            {
                target.sendMessage(ChatColor.DARK_RED +""+ ChatColor.ITALIC + ChatColor.BOLD + "Respuesta de reporte desde Staff" + ChatColor.GOLD + " > " + s);
                p.sendMessage(ChatColor.DARK_RED +""+ ChatColor.ITALIC + ChatColor.BOLD + "Respuesta de reporte desde Staff" + ChatColor.GOLD + " > " + s);
            }
        }
        else
        {
            String[] list = ChatColor.stripColor(meta.getDisplayName()).split(" ")[2].split("'");
            String listname = list[0];
            if (!this.rm.contains(listname))
            {
                p.closeInventory();
                return;
            }
            Report report = this.rm.get(listname);
            p.closeInventory();
            this.rm.showPlayerActionMenu(p, listname, report);
        }
    }
}
