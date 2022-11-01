package org.syntax.pr.reports;

import java.util.ArrayList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import me.gonzalociocca.minelevel.core.Main;

public class ReportManager
{
    Main plugin;
    FileConfiguration config;
    FileConfiguration mconfig;

    public void setup(Main plugin)
    {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.mconfig = plugin.getMessagesConfig();
    }

    private static ReportManager instance = new ReportManager();

    public static ReportManager getInstance()
    {
        return instance;
    }

    ArrayList<Report> reports = new ArrayList();

    public void add(String reporter, String reported, String message)
    {
        this.reports.add(new Report(reporter, reported, message));
        save(get(reporter));
    }

    public void add(String reporter, String reported, String message, Status status)
    {
        this.reports.add(new Report(reporter, reported, message, status));
        save(get(reporter));
    }

    public void remove(String reporter)
    {
        Report r = get(reporter);
        if (r == null) {
            return;
        }
        save(get(reporter));
        this.reports.remove(r);
    }

    public void delete(String reporter)
    {
        remove(reporter);
        this.config.set("Reports." + reporter, null);
        this.plugin.saveConfig();
    }

    public boolean contains(String reporter, String msg)
    {
        Report r = get(reporter,msg);
        if (r == null) {
            return false;
        }
        return true;
    }
    public boolean contains(String reporter)
    {
        Report r = get(reporter);
        if (r == null) {
            return false;
        }
        return true;
    }

    public Report get(String reporter)
    {
        for (Report r : this.reports) {
            if (r.getReporter().equalsIgnoreCase(reporter)) {
                    return r;
            }
        }
        return null;
    }

    public Report get(String reporter,String msg)
    {
        for (Report r : this.reports) {
            if (r.getReporter().equalsIgnoreCase(reporter)) {
                if(r.getMessage().equalsIgnoreCase(ChatColor.stripColor(msg))){
                    return r;
                }
            }
        }
        return null;
    }

    public void save(Report report)
    {
        this.config.set("Reports." + report.getReporter() + ".Reporter", report.getReporter());
        this.config.set("Reports." + report.getReporter() + ".Reported", report.getReported());
        this.config.set("Reports." + report.getReporter() + ".Message", report.getMessage());
        this.config.set("Reports." + report.getReporter() + ".Status", report.getStatus().toString());
        this.plugin.saveConfig();
    }

    public void saveAll()
    {
        for (Report r : this.reports) {
            if (r != null) {
                save(r);
            }
        }
    }

    public void load(String reporter)
    {
        if (!this.config.contains("Reports." + reporter + ".Reporter")) {
            return;
        }
        if (contains(reporter)) {
            return;
        }
        String reported = this.config.getString("Reports." + reporter + ".Reported");
        String msg = this.config.getString("Reports." + reporter + ".Message");
        Status status;
        try
        {
            status = Status.valueOf(this.config.getString("Reports." + reporter + ".Status").toUpperCase());
        }
        catch (Exception e)
        {
            e.printStackTrace(); return;
        }
        add(reporter, reported, msg, status);
    }

    public void loadAll()
    {
        if (!this.config.contains("Reports.DONOTDELETE")) {
            this.config.set("Reports.DONOTDELETE", Boolean.valueOf(true));
        }
        this.plugin.saveConfig();
        for (String s : this.config.getConfigurationSection("Reports").getKeys(false)) {
            if ((!s.equalsIgnoreCase("DONOTDELETE")) &&
                    (this.config.contains("Reports." + s + ".Reported"))) {
                load(s);
            }
        }
    }

    public void showMenuOne(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, 45, ChatColor.DARK_RED + "Menu de Reportes");
        for (Report r : this.reports)
        {
            ChatColor color = r.getStatus() == Status.EN_ESPERA ? ChatColor.AQUA : ChatColor.GREEN;
            ItemStack i = new ItemStack(Material.WOOL, 1, (short)(r.getStatus() == Status.EN_ESPERA ? 3 : 5));
            ItemMeta meta = i.getItemMeta();
            meta.setDisplayName(color + r.getReporter() + "'s reporte.");
            ArrayList<String> lores = new ArrayList();
            lores.add(ChatColor.DARK_GREEN + "Victima: " + ChatColor.GREEN + ChatColor.ITALIC + r.getReporter());
            lores.add(ChatColor.DARK_RED + "Acusado: " + ChatColor.RED + ChatColor.ITALIC + r.getReported());
            lores.add(ChatColor.DARK_GRAY + "Mensaje: " + ChatColor.GRAY + ChatColor.ITALIC + r.getMessage());
            lores.add((color == ChatColor.AQUA ? ChatColor.DARK_AQUA : ChatColor.DARK_GREEN) + "Estado: " + color + ChatColor.ITALIC + WordUtils.capitalizeFully(r.getStatus().toString().toLowerCase().replace('_', ' ')));
            meta.setLore(lores);
            i.setItemMeta(meta);
            inv.addItem(new ItemStack[] { i });
        }
        p.openInventory(inv);
    }

    public void showReportMenu(Player p, Report r)
    {
        String reporter = r.getReporter();
        String reported = r.getReported();
        String message = r.getMessage();
        Status status = r.getStatus();
        Player p_reporter = p.getServer().getPlayer(reporter);
        Player p_reported = p.getServer().getPlayer(reported);

        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "[R] " + ChatColor.RED + reporter + "'s reporte.");

        ItemStack i = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta smeta = (SkullMeta)i.getItemMeta();
        smeta.setDisplayName(ChatColor.DARK_GREEN + "Victima: " + ChatColor.GREEN + reporter);
        smeta.setOwner(reporter);
        ArrayList<String> lores = new ArrayList();

        if(Bukkit.getPlayer(reporter) == null){
            lores.add(ChatColor.DARK_RED + "Jugador se ha desconectado.");
        }else{
            lores.add(ChatColor.DARK_GREEN + "Jugador esta conectado.");
        }

        smeta.setLore(lores);
        i.setItemMeta(smeta);
        inv.setItem(0, i);


        i = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        smeta = (SkullMeta)i.getItemMeta();
        smeta.setDisplayName(ChatColor.DARK_RED + "Acusado: " + ChatColor.RED + reported);
        //bug viaversion i think, smeta.setOwner(reported);
        lores = new ArrayList();
        if(Bukkit.getPlayer(reported) == null){
            lores.add(ChatColor.DARK_RED + "Jugador se ha desconectado.");
        }else{
            lores.add(ChatColor.DARK_GREEN + "Jugador esta conectado.");
        }
        smeta.setLore(lores);
        i.setItemMeta(smeta);
        inv.setItem(1, i);

        i = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Mensaje:");
        lores = new ArrayList();
        lores.add(ChatColor.GRAY + message);
        meta.setLore(lores);
        i.setItemMeta(meta);
        inv.setItem(4, i);

        i = new ItemStack(Material.SIGN, 1);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Ir atras");
        lores = new ArrayList();
        meta.setLore(lores);
        i.setItemMeta(meta);
        inv.setItem(7, i);

        i = new ItemStack(Material.BARRIER, 1);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Eliminar Reporte");
        lores = new ArrayList();
        meta.setLore(lores);
        i.setItemMeta(meta);
        inv.setItem(8, i);

        p.openInventory(inv);
    }

    public void showPlayerActionMenu(Player p, String player, Report r)
    {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "[R] " + ChatColor.RED + player);

        ItemStack i = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "Banear " + player);
        i.setItemMeta(meta);
        inv.setItem(0, i);

        i = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta smeta = (SkullMeta)i.getItemMeta();
        smeta.setDisplayName(ChatColor.RED + "Kickear " + player);
        i.setItemMeta(smeta);
        inv.setItem(1, i);

        i = new ItemStack(Material.BOOK_AND_QUILL, 1);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Enviar mensaje de plantilla a " + player);
        ArrayList<String> lores = new ArrayList();
        lores.add(ChatColor.GOLD +""+ ChatColor.ITALIC + ChatColor.BOLD + "Ejemplos:");
        for (String s : this.mconfig.getStringList("Messages")) {
            lores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(lores);
        i.setItemMeta(meta);
        inv.setItem(2, i);

        i = new ItemStack(Material.ENDER_PEARL, 1);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Teletransportarse a " + player);
        i.setItemMeta(meta);
        inv.setItem(3, i);

        i = new ItemStack(Material.SIGN, 1);
        meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Volver a " + r.getReporter() + "'s reporte");
        i.setItemMeta(meta);
        inv.setItem(8, i);

        p.openInventory(inv);
    }

    public void showPlayerMessageMenu(Player p, String player)
    {
        Inventory inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY + "MSG: " + ChatColor.LIGHT_PURPLE + player);

        int x = 0;
        for (String s : this.mconfig.getStringList("Messages")) {
            if (x < 17)
            {
                ItemStack i = new ItemStack(Material.PAPER, 1);
                ItemMeta meta = i.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Enviar mensaje:");
                ArrayList<String> lores = new ArrayList();
                lores.add(ChatColor.translateAlternateColorCodes('&', s));
                meta.setLore(lores);
                i.setItemMeta(meta);
                inv.addItem(new ItemStack[] { i });
                x++;
            }
        }
        ItemStack i = new ItemStack(Material.SIGN, 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Volver a " + player + "'s menu");
        i.setItemMeta(meta);
        inv.setItem(17, i);

        p.openInventory(inv);
    }
}
