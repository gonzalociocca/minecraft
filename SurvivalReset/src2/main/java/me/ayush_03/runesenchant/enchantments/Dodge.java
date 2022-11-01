package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class Dodge implements Listener {

   @EventHandler
   public void onDamage(EntityDamageEvent entitydamageevent) {
      String s = FileManager.getManager().getEnchantments().getString("dodge");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamageevent.getEntity() instanceof Player) {
         Player player = (Player)entitydamageevent.getEntity();
         int i = (int)(Math.random() * 100.0D);
         ItemStack[] aitemstack = player.getInventory().getArmorContents();

         try {
            ItemStack[] aitemstack1 = aitemstack;
            int j = aitemstack.length;

            ItemStack itemstack;
            int k;
            for(k = 0; k < j; ++k) {
               itemstack = aitemstack1[k];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("dodge.level_I.chance")) {
                  entitydamageevent.setDamage(0.0D);
               }
            }

            aitemstack1 = aitemstack;
            j = aitemstack.length;

            for(k = 0; k < j; ++k) {
               itemstack = aitemstack1[k];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("dodge.level_II.chance")) {
                  entitydamageevent.setDamage(0.0D);
               }
            }

            aitemstack1 = aitemstack;
            j = aitemstack.length;

            for(k = 0; k < j; ++k) {
               itemstack = aitemstack1[k];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("dodge.level_III.chance")) {
                  entitydamageevent.setDamage(0.0D);
               }
            }

            aitemstack1 = aitemstack;
            j = aitemstack.length;

            for(k = 0; k < j; ++k) {
               itemstack = aitemstack1[k];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("dodge.level_IV.chance")) {
                  entitydamageevent.setDamage(0.0D);
               }
            }

            aitemstack1 = aitemstack;
            j = aitemstack.length;

            for(k = 0; k < j; ++k) {
               itemstack = aitemstack1[k];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("dodge.level_V.chance")) {
                  entitydamageevent.setDamage(0.0D);
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
