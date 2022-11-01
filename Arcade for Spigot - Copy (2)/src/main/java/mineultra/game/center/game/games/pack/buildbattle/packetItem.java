package mineultra.game.center.game.games.pack.buildbattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author cuack
 */
public class packetItem {
    ItemStack toSerialize = null;
    
    String Serialized = null;
    public packetItem(ItemStack it){
     toSerialize = it;

    }
    public static String serialize(ItemStack ite){
net.minecraft.server.v1_8_R3.ItemStack it = CraftItemStack.asNMSCopy(ite);;
        String str = "";

        str = str+"type>"+ite.getType().getId()+"#";
        str = str+"data>"+ite.getDurability()+"#";
        str = str+"ammo>"+ite.getAmount()+"#";
        if(!ite.getEnchantments().isEmpty()){
        for(Enchantment ec : ite.getEnchantments().keySet()){
         str = str+"ench>"+ec.getName()+":"+ite.getEnchantments().get(ec)+"#";
        }
       }
       if(it.hasTag()){
        for(String tag : it.getTag().c()){
        NBTBase tagval = it.getTag().get(tag);
        if(tagval != null){
        str = str+"tags>"+tag+":"+tagval+"#";
        }
       }
      } 
       if(ite.hasItemMeta()){
       if(ite.getItemMeta().hasDisplayName()){
        str = str+"name>"+ite.getItemMeta().getDisplayName()+"#";
       }
       if(ite.getItemMeta().hasLore()){
        if(!ite.getItemMeta().getLore().isEmpty()){
        List<String> lore = ite.getItemMeta().getLore();
        for(String ind : lore){
        str = str+"lore>"+ind+"#";
         }
        }
       }
       }
return str;
    }
    
    public String getSerialized(){
        return this.Serialized;
    }
    public ItemStack getItemStack(){
        return this.toSerialize;
    }
    
    public static ItemStack deserialize(String str){
        if(str.length() < 5){
            return null;
        }
        ItemStack ite = null;
        int type = 0;
        int data = 0;
        int amount = 1;
        String name = null;
        HashMap<Enchantment,Integer> enchs = new HashMap();
        String tags = "";
        List<String> lore = new ArrayList();
        
        for(String st : str.split("#")){

            if(st.startsWith("type>")){
            type = Integer.parseInt(st.substring(5));

            }
            if(st.startsWith("data>")){
            data = Integer.parseInt(st.substring(5));

            }
            if(st.startsWith("ammo>")){
            amount = Integer.parseInt(st.substring(5));

            }
            if(st.startsWith("name>")){
            name = st.substring(5);

            }
            if(st.startsWith("lore>")){
            lore.add(st.substring(5));

            }
            if(st.startsWith("ench>")){
            String[] ec = st.substring(5).split(":");
            String ecname = ec[0];
            int eclevel = Integer.parseInt(ec[1]);
            Enchantment ce = Enchantment.getByName(ecname);
            enchs.put(ce, eclevel);

            }
            if(st.startsWith("tags>")){
            String tag = st.substring(5);
            if(tags.length() > 3){
                tags = tags+",";
            }
            tags = tags+tag;

            }   
        }
        
        ite = new ItemStack(type,amount,(short)data);
        ItemMeta im = ite.getItemMeta();
        if(name !=null){
        im.setDisplayName(name);
        }
        if(!lore.isEmpty()){
        im.setLore(lore);
        }
        ite.setItemMeta(im);
        if(!enchs.isEmpty()){
         ite.addUnsafeEnchantments(enchs);
        }
        
        if(!tags.isEmpty()){
net.minecraft.server.v1_8_R3.ItemStack it = CraftItemStack.asNMSCopy(ite);


    try {
        
        it.setTag(MojangsonParser.parse("{"+tags+"}"));
    } catch (MojangsonParseException ex) {
        Logger.getLogger(packetItem.class.getName()).log(Level.SEVERE, null, ex);
    }


ite = CraftItemStack.asBukkitCopy(it);
        }
        
        return ite;
    }
    
    
}
