package mineultra.game.center.game.games.smash;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import mineultra.core.common.Rank;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.centerManager;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.perks.SMASH;
import mineultra.game.center.kit.perks.TRYHARD;
import mineultra.game.center.kit.perks.WITHERSKULL;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class TryHardKit extends Kit
{
    public TryHardKit(final centerManager manager) {
        super(manager, "TryHard", KitAvailability.Green, new String[] { "TryHard in the game" }
                , new Perk[] {new TRYHARD(),new SMASH("Smash",1,2500L) }
                , EntityType.PLAYER, new ItemStack(Material.DIAMOND_AXE));
    }
    
    @Override
    public void GiveItems(final Player player) {
        
player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE));
ItemStack eye = new ItemStack(Material.NETHER_STAR);
ItemMeta ime = eye.getItemMeta();
ime.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+"Want to try?");
eye.setItemMeta(ime);
player.getInventory().addItem(eye);
    ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        
          LeatherArmorMeta im = (LeatherArmorMeta)helmet.getItemMeta();
          im.setColor(this.getColor(player));
          helmet.setItemMeta(im);

          LeatherArmorMeta im2 = (LeatherArmorMeta)armor.getItemMeta();
          im2.setColor(this.getColor(player));
          armor.setItemMeta(im2);
      

          LeatherArmorMeta im3 = (LeatherArmorMeta)leggs.getItemMeta();
          im3.setColor(this.getColor(player));
          leggs.setItemMeta(im3);
      

          LeatherArmorMeta im4 = (LeatherArmorMeta)boots.getItemMeta();
          im4.setColor(this.getColor(player));
          boots.setItemMeta(im4);
      
          helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
          armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
          leggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
          boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
          
          player.getInventory().setHelmet(helmet);
          player.getInventory().setChestplate(armor);
          player.getInventory().setLeggings(leggs);
          player.getInventory().setBoots(boots);
         
    }
    
    public Color getColor(Player player){
if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.GOLD){
return Color.ORANGE; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.GREEN){
return Color.GREEN; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.RED){
return Color.RED; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.BLUE){
return Color.BLUE; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.WHITE){
return Color.WHITE; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.YELLOW){
return Color.YELLOW; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.AQUA){
return Color.AQUA; 
}else if(this.Manager.GetGame().GetTeam(player).GetColor() == ChatColor.GRAY){
return Color.GRAY; 
}
return Color.BLACK;
    }
    

    @Override
    public int GetCost() {
        return this.Manager.getConfig().getInt("kitprices.tryhard");
    }
}
