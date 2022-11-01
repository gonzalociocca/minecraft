package mineultra.game.center.managers;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import mineultra.game.center.centerManager;
import java.util.List;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.GameType;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MiscManager implements Listener
{
    private List<String> _dontGiveClockList = null;
    private centerManager Manager = null;
    
    public MiscManager(final centerManager manager) {
        super();
        this._dontGiveClockList = new ArrayList<>();
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents(this, manager.GetPlugin());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void InteractActive(final PlayerInteractEvent event) {
        event.setCancelled(false);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void InteractClickCancel(final PlayerInteractEvent event) {
        if (Manager.GetGame() == null) {
            return;
        }
        final Player player = event.getPlayer();
        if (!Manager.GetGame().IsAlive(player)) {
            event.setCancelled(true);
        }
        else if (event.getPlayer().getItemInHand().getType() == Material.INK_SACK && event.getPlayer().getItemInHand().getData().getData() == 15 && event.getAction() == Action.RIGHT_CLICK_BLOCK ) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void InventoryClickCancel(final InventoryClickEvent event) {
        if (Manager.GetGame() == null) {
            return;
        }
        final Player player = UtilPlayer.searchExact(event.getWhoClicked().getName());
        if (Manager.GetGame().IsLive() && !Manager.GetGame().IsAlive(player)) {
            event.setCancelled(true);
            player.closeInventory();
        }
    }
    
    @EventHandler
    public void addClockPrevent(final InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            this._dontGiveClockList.add(event.getPlayer().getName());
        }
    }
    
    @EventHandler
    public void removeClockPrevent(final InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            this._dontGiveClockList.remove(event.getPlayer().getName());
        }
    }
    
    @EventHandler
    public void HubClockUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.SLOW) {
            return;
        }
        if (Manager.GetGame() == null) {
            return;
        }

        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (!Manager.GetGame().IsAlive(player) && !this._dontGiveClockList.contains(player.getName()) && !player.getInventory().contains(Material.WATCH)) {
                Manager.HubClock(player);
            }
        }
    }

    
    @EventHandler
    public void openPlayerFinder(PlayerInteractEvent event){

            if(event.getPlayer().getItemInHand() == null){
                return;
            }
            
            if(event.getPlayer().getItemInHand().getType() != Material.COMPASS){
                return;
            }
            if(this.Manager.GetGame() == null){
                return;
            }
            if(!this.Manager.GetGame().IsLive()){
                return;
            }
            Inventory iv = Bukkit.createInventory(null, 54, Colorizer.Color("&f&lPlayerFinder"));
            Player p = event.getPlayer();
            

            

            for(Player te : this.Manager.GetGame().GetPlayers(true)){
                ItemStack tepe = new ItemStack(Material.SKULL_ITEM);
                ItemMeta im = tepe.getItemMeta();
                im.setDisplayName(Colorizer.Color("&a&l"+te.getName()));
                List<String> lore = new ArrayList();
                lore.add(Colorizer.Color("&eClick to &fTeleport"));
                im.setLore(lore);
                tepe.setItemMeta(im);
                iv.addItem(tepe);
                
            }
        
            
            event.getPlayer().openInventory(iv);
            event.getPlayer().updateInventory();
            event.setCancelled(true);
      
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void HubClockInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getItemInHand() == null) {
            return;
        }
        if (player.getItemInHand().getType() != Material.WATCH) {
            return;
        }
        
        if(!player.getItemInHand().hasItemMeta()){
            return;
        }
        if(!player.getItemInHand().getItemMeta().hasDisplayName()){
            return;
        }
        if(!player.getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("return")){
        return;
        }

if(Manager.GetGame().GetType() == GameType.BuildBattle){
Manager.GetPortal().SendPlayerToServer(player, Manager.getConfig().getString("BuildBattle.lobby"));

}else
Manager.GetPortal().SendPlayerToServer(player, Manager.getConfig().getString("GameConfig.bungeelobby"));
            
    }
    
    @EventHandler
    public void HubCommand(final PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/hub")
                || event.getMessage().startsWith("/lobby")
                || event.getMessage().startsWith("/leave")
                || event.getMessage().startsWith("/"+Manager.getConfig().getString("GameConfig.bungeelobby")))
        {
Manager.GetPortal().SendPlayerToServer(event.getPlayer(), Manager.getConfig().getString("GameConfig.bungeelobby"));
event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDie(CombatDeathEvent event){
     if(event.GetEvent().getEntity() != null){
         if(event.GetLog().GetKiller() != null){
             if(event.GetLog().GetKiller().GetName() != null){

                 UtilDisplay.sendTitle((Player)event.GetEvent().getEntity(), Colorizer.Color("&a&lKilled by"), Colorizer.Color("&c&l"+event.GetLog().GetKiller().GetName()));
             }
         }
     }   
   }
    
    



}
