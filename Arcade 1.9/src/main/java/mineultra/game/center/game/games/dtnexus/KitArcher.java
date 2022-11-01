package mineultra.game.center.game.games.dtnexus;

import org.bukkit.entity.LivingEntity;
import mineultra.core.itemstack.ItemStackFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import mineultra.game.center.kit.perks.*;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.centerManager;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.perks.BARRAGE;
import mineultra.game.center.kit.perks.ROPEDARROW;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class KitArcher extends Kit
{
    public KitArcher(final centerManager manager) {
        super(manager, "Archer", KitAvailability.Green, new String[] { "Un as del arco" }, new Perk[] { new ROPEDARROW("Roped Arrow", 1.0, 6000L), new BARRAGE(3, 250L, true, false), new FLETCHER(2, 2, true) }, EntityType.PLAYER, new ItemStack(Material.BOW));
    }
    
    @Override
    public void GiveItems(final Player player) {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        player.getInventory().addItem(bow);
        player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.ARROW,5) });
        player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP) });
        player.getInventory().addItem(new ItemStack[] { ItemStackFactory.Instance.CreateStack(Material.MUSHROOM_SOUP) });
        player.getInventory().addItem(new ItemStack(Material.WOOD,64));
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
      
          helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          leggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          
          player.getInventory().setHelmet(helmet);
          player.getInventory().setChestplate(armor);
          player.getInventory().setLeggings(leggs);
          player.getInventory().setBoots(boots);
          
    }
    
    public Color getColor(Player player){
        if(this.Manager.GetGame().GetTeam(player).equals(this.Manager.GetGame().GetTeamList().get(1))){
            return Color.fromRGB(1,230,233);
        }else{
            return Color.fromRGB(255,30,93);
        }
    }
    
    @Override
    public void SpawnCustom(final LivingEntity ent) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        
          LeatherArmorMeta im = (LeatherArmorMeta)helmet.getItemMeta();
          im.setColor(Color.fromRGB(255,30,93));
          helmet.setItemMeta(im);

          LeatherArmorMeta im2 = (LeatherArmorMeta)armor.getItemMeta();
          im2.setColor(Color.fromRGB(255,30,93));
          armor.setItemMeta(im2);
      

          LeatherArmorMeta im3 = (LeatherArmorMeta)leggs.getItemMeta();
          im3.setColor(Color.fromRGB(255,30,93));
          leggs.setItemMeta(im3);
      

          LeatherArmorMeta im4 = (LeatherArmorMeta)boots.getItemMeta();
          im4.setColor(Color.fromRGB(255,30,93));
          boots.setItemMeta(im4);
      
          helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          leggs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
          
          
          
        ent.getEquipment().setHelmet(helmet);
        ent.getEquipment().setChestplate(armor);
        ent.getEquipment().setLeggings(leggs);
        ent.getEquipment().setBoots(boots);
    }
   @Override
    public int GetCost() {
        return this.Manager.getConfig().getInt("kitprices.archer");
    }
}
