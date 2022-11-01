package server.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import server.ServerPlugin;
import server.api.menu.Menu;
import server.api.menu.MenuGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noname on 28/4/2017.
 */
public class MenuAPI implements Listener {

    private static HashMap<String, Menu> playerMenuMap = new HashMap(1,0.25F);

    private static List<MenuGenerator> menuGeneratorList = new ArrayList(1);

    public MenuAPI() {
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    public static Menu getMenu(String str){
        return playerMenuMap.get(str);
    }

    public static void setMenu(String str, Menu menu){
        playerMenuMap.put(str, menu);
    }

    public static void removeMenu(String str){
        playerMenuMap.remove(str);
    }

    public static void removeMenu(String str, Menu menu){
        playerMenuMap.remove(str, menu);
    }

    public static void addGenerator(MenuGenerator menuGenerator){
        menuGeneratorList.add(menuGenerator);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getClickedInventory() != null && event.getClickedInventory().getName() != null) {
            Menu menu = MenuAPI.getMenu(event.getWhoClicked().getName());

            if (menu != null) {
                ItemStack itemStack = event.getCurrentItem();
                Player player = (Player)event.getWhoClicked();
                for(Menu.ItemSet itemSet : menu.getItemSetList()){
                    if(itemSet.getSlot() == event.getSlot()){
                        if(itemSet.getClickCallable() != null){
                            itemSet.getClickCallable().onClick(player, menu, itemStack, event);
                        }
                    }
                }

                if(event.getCursor() != null && event.getAction() != InventoryAction.NOTHING && event.getAction() != InventoryAction.UNKNOWN){
                    menu.addItemFor(player, event);
                    System.out.println("Adding for " + event.getAction().toString());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(event.getInventory() != null && event.getInventory().getName() != null && event.getPlayer() instanceof Player){
            Player player = (Player)event.getPlayer();

            Menu menu = MenuAPI.getMenu(player.getName());
            if(menu != null){
                menu.closeFor(player, event);
            }
        }
    }

    public static void handleCommands(PlayerCommandPreprocessEvent event, String[] args){

        for(MenuGenerator menuGenerator : menuGeneratorList){
            if(menuGenerator.onCommand(args)){
                Menu menu = menuGenerator.createMenu(event.getPlayer(), Menu.OpenReason.Command);
                menu.openFor(event.getPlayer(), Menu.OpenReason.Command);
            }
        }
    }

}
