package server.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import server.ServerPlugin;
import server.api.GameAPI;
import server.api.MenuAPI;
import server.common.Code;
import server.instance.GameServer;
import server.user.User;
import server.util.UtilMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Menu {

    public class ItemSet {
        ItemStack _itemStack;
        ClickCallable _clickCallable;
        int _slot;
        public ItemSet(){
        }

        public int getSlot(){
            return _slot;
        }

        public ItemSet setSlot(int slot){
            _slot = slot; return this;
        }

        public ClickCallable getClickCallable(){
            return _clickCallable;
        }

        public ItemSet setClickCallable(ClickCallable clickCallable){
            _clickCallable = clickCallable; return this;
        }

        public ItemStack getItemStack(){
            return _itemStack;
        }

        public ItemSet setItemStack(ItemStack itemStack){
            _itemStack = itemStack; return this;
        }

    }

    private List<ItemSet> _itemSetList;
    private String _inventoryName;
    private Integer _inventorySize;
    private UtilMenu.InventoryMode _inventoryMode;
    private InventoryCallable _inventoryCallable;
    private boolean _shouldGetGame;
    private boolean _shouldGetPlayerData;

    public abstract static class InventoryCallable {
        public abstract boolean onOpen(Player player, Menu menu, OpenReason reason, GameServer game, User playerData);

        public abstract boolean onClose(Player player, Menu menu, GameServer game, User playerData, InventoryCloseEvent event);

        public abstract boolean onAdd(Player player, Menu menu, GameServer game, User playerData, InventoryClickEvent event);
    }

    public static enum OpenReason {
        Entity, Command, Sign, Menu;
    }

    public static class ClickCallable {
        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
            event.setCancelled(true);
        }
    }

    public static class ClickCallableMenuOpen extends Menu.ClickCallable{
        final MenuGenerator _toOpen;
        public ClickCallableMenuOpen(MenuGenerator toOpen){
            _toOpen = toOpen;
        }

        @Override
        public void onClick(Player player, Menu menu, ItemStack itemStack, InventoryClickEvent event) {
            super.onClick(player, menu, itemStack, event);
            Menu newMenu = _toOpen.createMenu(player, OpenReason.Menu);
            newMenu.openFor(player, OpenReason.Menu);
        }
    }

    static int inventoryID = 0;
    public Menu(String inventoryName) {
        _inventoryCallable = null;
        _itemSetList = new ArrayList<ItemSet>();
        _inventoryName = Code.Color(inventoryName);
        _inventorySize = -1;
        _inventoryMode = UtilMenu.InventoryMode.Raw;
        _shouldGetGame = false;
        _shouldGetPlayerData = false;
    }

    public boolean shouldGetGame(){
        return _shouldGetGame;
    }

    public boolean shouldGetPlayerData(){
        return _shouldGetPlayerData;
    }

    public String getInventoryName() {
        return _inventoryName;
    }

    public Integer getInventorySize() {
        if (_inventorySize == -1) {
            return Math.min(((_itemSetList.size() / 9) * 9) + 9, 54);
        } else {
            return _inventorySize;
        }
    }

    public UtilMenu.InventoryMode getInventoryMode() {
        return _inventoryMode;
    }

    public InventoryCallable getInventoryCallable() {
        if(_inventoryCallable == null){
            _inventoryCallable = new InventoryCallable() {
                @Override
                public boolean onOpen(Player player, Menu menu, OpenReason reason, GameServer game, User playerData) {
                    return true;
                }

                @Override
                public boolean onClose(Player player, Menu menu, GameServer game, User playerData, InventoryCloseEvent event) {
                    return true;
                }

                @Override
                public boolean onAdd(Player player, Menu menu, GameServer game, User playerData, InventoryClickEvent event) {
                    return false;
                }
            };
        }
        return _inventoryCallable;
    }

    public List<ItemSet> getItemSetList() {
        return _itemSetList;
    }

    public Menu setInventoryName(String str) {
        _inventoryName = str;
        return this;
    }

    public Menu setInventorySize(Integer newSize) {
        _inventorySize = newSize;
        return this;
    }

    public Menu setInventoryMode(UtilMenu.InventoryMode newMode) {
        _inventoryMode = newMode;
        return this;
    }

    public Menu setInventoryCallable(InventoryCallable newCallable) {
        _inventoryCallable = newCallable;
        return this;
    }

    public Menu setShouldGetGame(boolean var){
        _shouldGetGame = var;
        return this;
    }

    public Menu setShouldGetPlayerData(boolean var){
        _shouldGetPlayerData = var;
        return this;
    }

    public Menu add(ItemStack itemStack, ClickCallable callback) {
        _itemSetList.add(new ItemSet().setClickCallable(callback).setItemStack(itemStack).setSlot(-1));
        return this;
    }

    public void remove(int slot){
        for(Iterator<ItemSet> it = getItemSetList().iterator();it.hasNext();){
            ItemSet itemSet = it.next();
            if(itemSet.getSlot() == slot){
                it.remove();
                break;
            }
        }
    }

    public void removeFirst(ItemStack itemStack) {
        List<ItemSet> itemSetList = getItemSetList();
        for(int a = 0; a < getItemSetList().size(); a++){
            ItemSet itemSet = itemSetList.get(a);
            if(itemStack.equals(itemSet.getItemStack())){
                itemSetList.remove(a);
                break;
            }
        }
    }

    public void removeLast(ItemStack itemStack) {
        List<ItemSet> itemSetList = getItemSetList();
        for(int a = getItemSetList().size(); a > 0; a--){
            ItemSet itemSet = itemSetList.get(a);
            if(itemStack.equals(itemSet.getItemStack())){
                itemSetList.remove(a);
                break;
            }
        }
    }

    public void removeAll(ItemStack itemStack) {
        List<ItemSet> itemSetList = getItemSetList();
        for(Iterator<ItemSet> it = itemSetList.iterator(); it.hasNext();){
            ItemSet itemSet = it.next();
            if(itemStack.equals(itemSet.getItemStack())){
                it.remove();
            }
        }
    }

    public boolean addItemFor(Player player, InventoryClickEvent event){
        boolean shouldAdd = (_inventoryCallable != null && _inventoryCallable.onAdd(player, this, shouldGetGame() ? GameAPI.getGameOf(player) : null , shouldGetPlayerData() ? ServerPlugin.getPlayerData(player.getName()) : null, event));
        event.setCancelled(!shouldAdd);
        return shouldAdd;
    }

    public void openFor(Player player, OpenReason openReason) {
        if(getInventoryCallable().onOpen(player, this,openReason,shouldGetGame() ? GameAPI.getGameOf(player) : null , shouldGetPlayerData() ? ServerPlugin.getPlayerData(player.getName()) : null)) {
            player.openInventory(getCraftedInventory());
            MenuAPI.setMenu(player.getName(), this);
        }
    }

    public void closeFor(Player player, InventoryCloseEvent event) {
        if(getInventoryCallable().onClose(player, this,shouldGetGame() ? GameAPI.getGameOf(player) : null , shouldGetPlayerData() ? ServerPlugin.getPlayerData(player.getName()) : null, event)) {
            if(getInventoryCallable() != null){
                getInventoryCallable().onClose(player, this, shouldGetGame() ? GameAPI.getGameOf(player) : null , shouldGetPlayerData() ? ServerPlugin.getPlayerData(player.getName()) : null, event);
            }
        }
    }

    public Inventory getCraftedInventory() {
        int newSlot = 0;
        Inventory iv = Bukkit.createInventory(null, getInventorySize(), getInventoryName());
        for(ItemSet itemSet : getItemSetList()){
           itemSet.setSlot(newSlot);
           iv.setItem(newSlot, itemSet.getItemStack());
           newSlot++;
        }
        return iv;
    }

}
