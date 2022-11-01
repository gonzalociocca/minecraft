/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineultra.core.common.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author cuack
 */
public class MatColorUtil {
    
    
public static ItemStack byDyeColor(DyeColor dye){
    if(dye==DyeColor.BLACK){
    return new ItemStack(Material.INK_SACK,1);
    }else if(dye==DyeColor.RED){
    return new ItemStack(Material.INK_SACK,1,(short)1);
    }else if(dye==DyeColor.GREEN){
    return new ItemStack(Material.INK_SACK,1,(short)2);
    }else if(dye==DyeColor.BROWN){
    return new ItemStack(Material.INK_SACK,1,(short)3);
    }else if(dye==DyeColor.BLUE){
    return new ItemStack(Material.INK_SACK,1,(short)4);
    }else if(dye==DyeColor.PURPLE){
    return new ItemStack(Material.INK_SACK,1,(short)5);
    }else if(dye==DyeColor.CYAN){
    return new ItemStack(Material.INK_SACK,1,(short)6);
    }else if(dye==DyeColor.SILVER){
    return new ItemStack(Material.INK_SACK,1,(short)7);
    }else if(dye==DyeColor.GRAY){
    return new ItemStack(Material.INK_SACK,1,(short)8);
    }else if(dye==DyeColor.PINK){
    return new ItemStack(Material.INK_SACK,1,(short)9);
    }else if(dye==DyeColor.LIME){
    return new ItemStack(Material.INK_SACK,1,(short)10);
    }else if(dye==DyeColor.YELLOW){
    return new ItemStack(Material.INK_SACK,1,(short)11);
    }else if(dye==DyeColor.LIGHT_BLUE){
    return new ItemStack(Material.INK_SACK,1,(short)12);
    }else if(dye==DyeColor.MAGENTA){
    return new ItemStack(Material.INK_SACK,1,(short)13);
    }else if(dye==DyeColor.ORANGE){
    return new ItemStack(Material.INK_SACK,1,(short)14);
    }else if(dye==DyeColor.WHITE){
    return new ItemStack(Material.INK_SACK,1,(short)15);
    }
 return new ItemStack(Material.INK_SACK,1,(short)15);   
}

public static DyeColor byDurability(short dura){
    if(dura==0){
        return DyeColor.BLACK;
    }if(dura==1){
        return DyeColor.RED;
    }
    if(dura==2){
        return DyeColor.GREEN;
    }
    if(dura==3){
        return DyeColor.BROWN;
    }
    if(dura==4){
        return DyeColor.BLUE;
    }if(dura==5){
        return DyeColor.PURPLE;
    }if(dura==6){
        return DyeColor.CYAN;
    }if(dura==7){
        return DyeColor.SILVER;
    }
    if(dura==8){
        return DyeColor.GRAY;
    }
    if(dura==9){
        return DyeColor.PINK;
    }
    if(dura==10){
        return DyeColor.LIME;
    }
    if(dura==11){
        return DyeColor.YELLOW;
    }
    if(dura==12){
        return DyeColor.LIGHT_BLUE;
    }
    if(dura==13){
        return DyeColor.MAGENTA;
    }
    if(dura==14){
        return DyeColor.ORANGE;
    }
    if(dura==15){
        return DyeColor.WHITE;
    }
   return DyeColor.WHITE;
}


}
