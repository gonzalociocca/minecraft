package me.peacefulmen.creativetrack;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemReader
{
  public static ItemStack read(String str)
  {
    String[] split = str.split(",");
    ArrayList<String> lores = new ArrayList();
    
    ItemStack i = new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
    for (int a = 1; a < split.length; a++)
    {
      if (split[a].startsWith("lore:"))
      {
        ItemMeta im = i.getItemMeta();
        
        String s1 = split[a].replace("lore:", "");
        if (s1.contains("%name%")) {
          s1 = s1.replaceAll("%name%", getFriendlyItemName(i.getType()));
        }
        String s2 = Colorizer.Color(s1);
        lores.add(s2);im.setLore(lores);i.setItemMeta(im);
      }
      for (Enchantment enc : Enchantment.values()) {
        if (split[a].toUpperCase().startsWith(enc.getName().toUpperCase()))
        {
          String s1 = split[a].replace(enc.getName().toUpperCase() + ":", "");
          i.addUnsafeEnchantment(enc, Integer.parseInt(s1));
        }
      }
      if (split[a].startsWith("name:"))
      {
        ItemMeta im = i.getItemMeta();
        String nam = split[a].replace("name:", "");
        if (nam.contains("%name%")) {
          nam = nam.replaceAll("%name%", getFriendlyItemName(i.getType()));
        }
        im.setDisplayName(Colorizer.Color(nam));
        i.setItemMeta(im);
      }
    }
    return i;
  }
  
  public static String getFriendlyItemName(Material m)
  {
    String str = m.toString();
    str = str.replace('_', ' ');
    str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    
    return str;
  }
}
