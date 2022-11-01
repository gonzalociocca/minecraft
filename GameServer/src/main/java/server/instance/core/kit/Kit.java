package server.instance.core.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import server.common.Code;
import server.util.UtilMsg;
import server.common.Rarity;
import server.util.UtilInv;
import server.util.UtilPlayer;

import java.util.List;

public class Kit {

    public String _display;
    double _cost;
    Rarity _rarity;
    String _permission;
    List<String> _descriptionList;
    List<ItemStack> _itemList;

    protected ItemStack _displayItem;
    protected ItemStack _storeSaleItem;
    protected ItemStack _storeSelectItem;

    public Kit(String Display, double Cost, Rarity KitRarity, String KitPermission, List<String> KitDescriptionList, List<ItemStack> KitItemList, ItemStack displayItem) {
        _display = Display;
        _cost = Cost;
        _rarity = KitRarity;
        _permission = KitPermission;
        _descriptionList = KitDescriptionList;
        _itemList = KitItemList;
        _displayItem = displayItem;
        _storeSaleItem = craftStoreSaleItem(_display, _rarity, _cost, _displayItem);
        _storeSelectItem = craftStoreSelectItem(_display, _rarity, _cost, _displayItem);
    }

    public String getDisplay() {
        return _display;
    }

    public double getCost(){
        return _cost;
    }

    public Rarity getRarity(){
        return _rarity;
    }

    public String getPermission(){
        return _permission;
    }

    public List<String> getDescriptionList() {
        return _descriptionList;
    }

    public List<ItemStack> getItemList(){
        return _itemList;
    }

    public ItemStack getDisplayItem(){
        return _displayItem;
    }

    public ItemStack getStoreSaleItem() {
        return _storeSaleItem;
    }

    public ItemStack getStoreSelectItem() {
        return _storeSelectItem;
    }

    public String getFormattedName() {
        return _rarity.getColor() + UtilMsg.Bold + this._display;
    }

    public void displayDescription(final Player player) {
        for (int i = 0; i < 3; ++i) {
            UtilPlayer.message(player, "");
        }
        UtilPlayer.message(player, UtilMsg.spacer);
        UtilPlayer.message(player, UtilMsg.KitName.replace("%s", getDisplay()));
        for (String line : getDescriptionList()) {
            UtilPlayer.message(player, UtilMsg.Gray + "  " + line);
        }
        UtilPlayer.message(player, UtilMsg.spacer);
    }

    public void giveItems(Player player){

    }

    public void applyKit(Player player) {
        UtilInv.Clear(player);
        giveItems(player);
        UtilInv.Update(player);
    }

    public Kit clone() {
        return new Kit(_display, _cost, _rarity, _permission, _descriptionList, _itemList, _displayItem);
    }

    public static ItemStack craftStoreSaleItem(String display, Rarity rarity, double cost, ItemStack displayItem) {
        String[] storeLore = new String[]{"", UtilMsg.Price + cost + " " + UtilMsg.Currency};
        return Code.makeItemStack(displayItem.getType(), rarity.getColor() + display, storeLore, displayItem.getAmount(), (byte) displayItem.getDurability());
    }

    public static ItemStack craftStoreSelectItem(String display, Rarity rarity, double cost, ItemStack displayItem) {
        String[] buyedLore = new String[]{"", UtilMsg.Green + "Haz click en este Item", UtilMsg.Yellow + "para seleccionarlo como tu Kit Preferido."};
        return Code.makeItemStack(displayItem.getType(), rarity.getColor() + display, buyedLore, displayItem.getAmount(), (byte) displayItem.getDurability());
    }

}
