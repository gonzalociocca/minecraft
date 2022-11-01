package server.common;

import org.bukkit.inventory.ItemStack;

/**
 * Created by noname on 23/3/2017.
 */
public class InventoryItem {
    ItemStack itemstack;
    int slot;

    public InventoryItem(ItemStack it, int aslot) {
        itemstack = it;
        slot = aslot;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemstack;
    }
}
