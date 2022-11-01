package me.gonzalociocca.minelevel.mercado;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by ciocca on 25/10/16.
 */
public class StoreItem {
    boolean alreadyBuyed = false;
    ItemStack storeitem;
    int price;
    public StoreItem(String serializeditem){
        storeitem = ItemStack.deserialize(this.getMap(serializeditem));
    }
    public Map<String,Object> getMap(String item){
        return null;
    }
}
