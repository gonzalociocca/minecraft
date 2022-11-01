package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by noname on 9/2/2017.
 */
public class LobbyListener implements Listener {
    Main plugin;

    public LobbyListener(Main main){
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().hasItemMeta() && event.getPlayer().getItemInHand().getItemMeta().hasDisplayName()){
                if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Variable.LobbyServerMenu)){
                    this.openServersMenu(event.getPlayer());
                }
            }
        }
            if (event.getAction() == Action.LEFT_CLICK_BLOCK && !plugin.getDB().getPlayerData(event.getPlayer().getName()).getRank().getType().isAtLeast(RankType.OWNER)) {
                event.setCancelled(true);
            }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrivatizar(BlockPlaceEvent event) {
        if (!plugin.getDB().getPlayerData(event.getPlayer().getName()).getRank().getType().isAtLeast(RankType.OWNER)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void queueUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Block block = p.getLocation().getBlock();
                if (block.getType().equals(Material.PORTAL)) {
                    if (Variable.Portal1Location == null) {
                        Variable.Portal1Location = new Location(Bukkit.getWorld("world"), -539, 5, -252);
                        Variable.Portal2Location = new Location(Bukkit.getWorld("world"), -576, 5, -215);
                    }
                    if (p.getLocation().distanceSquared(Variable.Portal1Location) < 10) {
                        p.chat("/moveto skywars");
                    } else if(p.getLocation().distanceSquared(Variable.Portal2Location)  < 10){
                        p.chat("/moveto rex");
                    } else {
                        p.chat("/moveto factions");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10000, 5));
        event.getPlayer().setWalkSpeed(Code.getRealMoveSpeed(2, false));
        event.getPlayer().setFlySpeed(Code.getRealMoveSpeed(2, true));
        (((CraftPlayer) event.getPlayer()).getHandle()).getAttributeInstance(GenericAttributes.c).setValue(0.0D);
        this.giveLobbyItems(event.getPlayer());
    }
    public void openServersMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null,9,Variable.LobbyServerMenu);
        inventory.setItem(3,Code.makeItemStack(Material.DIAMOND_SWORD,Variable.ServerFacciones,new String[]{},1,(byte)0));
        inventory.setItem(4,Code.makeItemStack(Material.ENDER_PEARL,Variable.ServerRex,new String[]{},1,(byte)0));
        inventory.setItem(5,Code.makeItemStack(Material.GRASS,Variable.ServerSkyWars,new String[]{},1,(byte)0));
        inventory.setItem(6,Code.makeItemStack(Material.DIRT, Variable.ServerSkyWarsBeta, new String[]{},1, (byte)0));
        player.openInventory(inventory);
        player.updateInventory();
    }
    public void giveLobbyItems(Player player){
        player.getInventory().clear();
        player.getInventory().setItem(0, Code.makeItemStack(Material.NETHER_STAR,Variable.LobbyServerMenu, new String[]{},1,(byte)0));
    }
    @EventHandler
    public void onLobbyMenu(InventoryClickEvent event){
        if(!event.isCancelled() && event.getWhoClicked()!=null && event.getWhoClicked() instanceof Player && event.getInventory().getName()!=null && event.getInventory().getName().equals(Variable.LobbyServerMenu)){
            Player player = (Player)event.getWhoClicked();
            if(event.getSlot()== 3){
                player.chat("/factions");
            }
            else if(event.getSlot()==4){
                player.chat("/rex");
            }
            else if(event.getSlot()==5){
                player.chat("/skywars");
            } else if(event.getSlot() == 6){
                player.chat("/skywarsbeta");
            }
            event.setCancelled(true);
        }
    }

}
