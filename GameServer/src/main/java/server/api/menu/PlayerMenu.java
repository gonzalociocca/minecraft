package server.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import server.api.MenuAPI;
import server.instance.GameServer;
import server.user.User;

/**
 * Created by noname on 26/5/2017.
 */
public class PlayerMenu extends Menu {
    public PlayerMenu(Player player, String inventoryName, Menu.OpenReason openReason){
        super(inventoryName);
        setInventoryCallable(new InventoryCallable() {
            @Override
            public boolean onOpen(Player player, Menu menu, OpenReason reason, GameServer game, User playerData) {
                return true;
            }

            @Override
            public boolean onClose(Player player, Menu menu, GameServer game, User playerData, InventoryCloseEvent event) {
                MenuAPI.removeMenu(player.getName(), menu);
                return true;
            }

            @Override
            public boolean onAdd(Player player, Menu menu, GameServer game, User playerData, InventoryClickEvent event) {
                return false;
            }
        });
    }
}