package me.peacefulmen.creativetrack;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Core
        extends JavaPlugin
        implements Listener

{

    public Core instance = this;
    public FileConfiguration config = getConfig();

    Random r = new Random();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        blacklist.add("SantyagoLuxo");
        blacklist.add("Sleepy_Killer");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
        getServer().getLogger().log(Level.INFO, "CreativeTrack [v{0}] has been disabled!", getDescription().getVersion());
    }

    String title = "[CreativeTrack] ";

    @EventHandler
    public void onCreativePickup(InventoryCreativeEvent event) {
        if (event.getWhoClicked() == null) {
            return;
        } else {
            Player p = (Player) event.getWhoClicked();

            ItemStack itemlore = event.getCursor();

            ItemMeta im = itemlore.getItemMeta();
            im.setLore(Arrays.asList("Origen: Creativo", "Dueño: " + p.getName()));
            itemlore.setItemMeta(im);
            event.setCursor(itemlore);
            event.setCurrentItem(itemlore);
            p.updateInventory();
        }
    }

    List<String> blacklist = new ArrayList();

    @EventHandler
    public void addBlackList(InventoryClickEvent event) {

        if(event.getCurrentItem()==null){
            return;
        }
        if(event.getClick().isCreativeAction() && event.getWhoClicked() != null && event.getWhoClicked().getGameMode()==GameMode.CREATIVE){
            Player p = (Player) event.getWhoClicked();

            ItemStack itemlore = event.getCurrentItem().clone();

            ItemMeta im = itemlore.getItemMeta();
            im.setLore(Arrays.asList("Origen: Creativo", "Dueño: " + p.getName()));
            itemlore.setItemMeta(im);
            event.setCursor(itemlore);
            p.updateInventory();
            return;
        }
        if (blacklist.isEmpty()) {
            return;
        }
        if (event.getWhoClicked() == null) {
            return;
        }
        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }
        if (!event.getCurrentItem().getItemMeta().hasLore()) {
            return;
        }
        List<String> lore = event.getCurrentItem().getItemMeta().getLore();
        if(lore.size() < 2){
            return;
        }
        if (lore.get(1) == null) {
            return;
        }

            for (String str : blacklist) {
                if(lore.get(1).contains(str)){
                    event.getWhoClicked().sendMessage(Colorizer.Color("&cHemos borrado un objeto ilegal en tu inventario."));
                    event.getCursor().setAmount(0);
                    return;
            }
        }


    }

}
