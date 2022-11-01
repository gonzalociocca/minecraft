import java.util.ArrayList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// $FF: renamed from: bj
public class class_83 {

    public static void Ξ(Inventory inventory, String s, ArrayList<String> arraylist, ItemStack itemstack, int i) {
        ItemMeta itemmeta = itemstack.getItemMeta();

        itemmeta.setDisplayName(s);
        if (arraylist != null) {
            itemmeta.setLore(arraylist);
        }

        itemstack.setItemMeta(itemmeta);
        if (i != -1) {
            inventory.setItem(i, itemstack);
        } else {
            inventory.addItem(new ItemStack[] { itemstack});
        }

    }

    public static ItemStack Ξ(String s, ArrayList<String> arraylist, ItemStack itemstack) {
        ItemMeta itemmeta = itemstack.getItemMeta();

        itemmeta.setDisplayName(s);
        if (arraylist != null) {
            itemmeta.setLore(arraylist);
        }

        itemstack.setItemMeta(itemmeta);
        return itemstack;
    }
}
