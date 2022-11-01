package mineultra.game.center.game.games.smash;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.LivingEntity;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.perks.FLAMEDASH;
import mineultra.game.center.kit.perks.SMASH;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class SmashKit extends Kit
{
    public SmashKit(final centerManager manager) {
        super(manager, "Smash", KitAvailability.Green, new String[] { "A little smash swagger" }
                , new Perk[] {new SMASH("Smash",1,3000L) }
                , EntityType.PLAYER, new ItemStack(Material.DIAMOND_AXE));
    }
    
    @Override
    public void GiveItems(final Player player) {
    player.getInventory().addItem(new ItemStack(Material.DIAMOND_AXE));
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
        return 0;
    }
}
