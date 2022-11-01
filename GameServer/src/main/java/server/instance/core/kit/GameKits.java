package server.instance.core.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import server.ServerPlugin;
import server.common.Code;
import server.instance.GameServer;
import server.instance.misc.GameTeam;
import server.user.User;
import server.util.UtilMenu;
import server.util.UtilMsg;
import server.util.UtilPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameKits {

    private Kit[] _kits = new Kit[0];
    String[] storeLore = {"", Code.Color("&fHaz click teniendo este item en la mano"), Code.Color("&ePara abrir el menu de kit.")};
    private ItemStack _storeItem = Code.makeItemStack(Material.BOW, getStoreName(), storeLore, 1, (byte) 0);

    String _storeName = Code.Color("&a%g &f- &eKits");

    public ItemStack getStoreItem() {
        return _storeItem;
    }

    public String getStoreName() {
        return _storeName;
    }

    public void validateKit(GameServer game, Player player, GameTeam team) {
        Kit kit = getKit(player);
        if (kit != null && !team.isKitAllowed(kit)) {
            for (Kit otherKit : getKitArray()) {
                if (team.isKitAllowed(otherKit)) {
                    setKit(game, player, kit, false);
                }
            }
        }
    }

    public Kit getFirstKit() {
        for (Kit kit : getKitArray()) {
                return kit;
        }
        return null;
    }

    public void setKit(GameServer game, Player player, Kit kit, boolean announce) {
        GameTeam team = game.getLogin().getTeam(player);
        if (team != null) {
            if (!team.isKitAllowed(kit)) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 2.0F, 0.5F);
                UtilPlayer.message(player, UtilMsg.CantEquip.replace("%s", team.getFormattedName()).replace("%d", kit.getFormattedName()));
                return;
            }
        }

        for (Kit currentKit : getKitArray()) {
            /*if (currentKit.hasPlayer(player)) {
                currentKit.removePlayer(player);
            }*/
        }

        if (announce) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 1.0F);
            UtilPlayer.message(player, UtilMsg.KitEquip.replace("%s", kit.getFormattedName()));
        }
    }

    public Kit getKit(Player player) {
        for (Kit kit : getKitArray()) {
            /*if (kit.hasPlayer(player)) {
                return kit;
            }*/
        }
        return null;
    }

    public Inventory getInventoryStore(User pd) {
        Inventory storeInventory = Bukkit.createInventory(null, 27, getStoreName());

        List<ItemStack> itemList = new ArrayList();
        /*
        Kits kits = pd.getDataManager().getKits();

        Kit[] kitArray = getKitArray();

        if (kitArray.length > 0) {
            for (Kit kit : getKitArray()) {
                if (kits.hasKit(getGame().getType(), kit)) {
                    itemList.add(kit.getStoreSelectItem());
                } else {
                    itemList.add(kit.getStoreSaleItem());
                }
            }
        }
        */
        UtilMenu.fillInventory(storeInventory, itemList, UtilMenu.InventoryMode.Centered);

        return storeInventory;
    }

    public Kit[] getKitArray() {
        return _kits;
    }

    public void setKitArray(Kit[] newKits) {
        _kits = newKits;
    }

    public void openKitStoreMenu(Player player) {
        player.openInventory(getInventoryStore(ServerPlugin.getPlayerData(player.getName())));
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().equals(_storeName)) {
            openKitStoreMenu(event.getPlayer());
        }
    }

    public void purchaseKit(Player player, final Kit kit) {
        /*
        User pd = Manager.getPlayerData(player.getId());
        Kits kit = pd.getDataManager().getKits();

        InstanceData classeditor = kit.getGame().getType();
        Money money = pd.getDataManager().getMoney();

        if (kit.hasKit(classeditor, kit)) {
            selectKit(player, kit, true);
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1F, 1F);
        } else if (money.getValue(classeditor) >= kit.getCost()) {
            money.substractValue(classeditor, kit.getCost());
            kit.addKit(classeditor, kit);
            UtilPlayer.message(player, Code.Color(UtilMsg.Buyed.replace("%kit", kit.getFormattedName()).replace("%s", "" + kit.getCost())));
            selectKit(player, kit, true);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.2F, 0.9F);
        } else {
            String m1 = Code.Color(UtilMsg.InsufficientBuy.replace("%s", "" + kit.getCost()));
            String m2 = Code.Color(UtilMsg.InsufficientBuy2);
            UtilPlayer.message(player, F.main("Kit", m1));
            UtilPlayer.message(player, F.main("Kit", m2));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.5F, 0.7F);
        }
        player.closeInventory();*/
    }


    public void selectKit(GameServer game, Player player, String kitPermission, boolean announce) {
        for (Kit kit : getKitArray()) {
            if (kit.getPermission().equals(kitPermission)) {
                selectKit(game, player, kit, true);
                break;
            }
        }
    }

    public void selectKit(GameServer game, Player player, Kit kit, boolean announce) {
        if (kit != null) {
            User pd = ServerPlugin.getPlayerData(player.getName());
            //pd.getDataManager().getKits().setDefaultKit(getGame().getType(), kit);
            setKit(game, player, kit, true);
            player.closeInventory();
        }
    }

    public void onInventoryClick(GameServer game, InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory.getName() != null && clickedInventory.getName().equals(getStoreName())) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.getItemMeta().hasLore()) {
                for (Kit kit : getKitArray()) {
                    if (kit.getStoreSaleItem().equals(currentItem)) {
                        purchaseKit((Player) event.getWhoClicked(), kit);
                        break;
                    } else if (kit.getStoreSelectItem().equals(currentItem)) {
                        selectKit(game, (Player) event.getWhoClicked(), kit, true);
                        break;
                    }
                }
            }
            event.setCancelled(true);
        }
    }

}
