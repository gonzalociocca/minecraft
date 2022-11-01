package server.util;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Created by noname on 12/5/2017.
 */
public class UtilName {


    public static String getFriendlyName(ItemStack obj){
        return getFriendlyName(obj.getType().name());
    }

    public static String getFriendlyName(Block obj){
        return getFriendlyName(obj.getType().name());
    }

    public static String getFriendlyName(String str){
        return str.replace("_"," ");
    }
}
