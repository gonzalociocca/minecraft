package server.util;

import server.common.Code;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by noname on 25/4/2017.
 */
public class UtilMenu {

    public enum InventoryMode {
        Centered // Size = 9*,18*,27*,36*,45*,54*
        , NineBlock  // Size = 27(4), 45(8)
        , Raw
        ;

        InventoryMode(){

        }
    }

    public static ItemStack menuSpace = Code.makeItemStack(Material.STAINED_GLASS_PANE, Code.Color("&f     "), null, 1, (byte)5);

    public static void fillInventory(Inventory inventory, List<ItemStack> itemList, InventoryMode inventoryMode){
        ItemStack[] content = inventory.getContents();

        if(inventoryMode == InventoryMode.Centered){
            //---------
            //---xxx---
            //---------
            int startSize = (inventory.getSize()/2);
            startSize -= itemList.size() / 2;
            startSize = startSize < 0 ? 0 : startSize;
            for(ItemStack it : itemList){
                content[startSize++] = it;
            }
        } else if(inventoryMode == InventoryMode.NineBlock){
            //---------  #9
            //-10-12-14-16-  #18
            //---------  #27
            //-28-30-32-34-  #36
            //---------  #45
            int count = 0;

            for(ItemStack it : itemList){
                int index = count > 4 ? 26 + ((count-4) * 2): 8 + (count*2);
                content[index] = it;
                count++;
            }
        } else if(inventoryMode == InventoryMode.Raw){
            int count = 0;

            for(ItemStack it : itemList){
                content[count] = it;
                count++;
            }
        }

        for(int a = 0; a < content.length; a++){
            if(content[a] == null){
                content[a] = menuSpace;
            }
        }
        inventory.setContents(content);

    }

    public static Inventory cloneInventory(Inventory inv){
        Inventory newInv = Bukkit.createInventory(inv.getHolder(), inv.getSize(), inv.getName());
        newInv.setContents(inv.getContents());

        return newInv;
    }
}
