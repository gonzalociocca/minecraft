package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class Pyromaniac implements Listener {

   @EventHandler
   public void onDamage(EntityDamageEvent entitydamageevent) {
      if(entitydamageevent.getEntity() instanceof Player) {
         String s = FileManager.getManager().getEnchantments().getString("pyromaniac");
         s = ChatColor.translateAlternateColorCodes('&', s);
         Player player = (Player)entitydamageevent.getEntity();
         if(entitydamageevent.getCause() == DamageCause.FIRE || entitydamageevent.getCause() == DamageCause.FIRE_TICK) {
            int i = (int)(Math.random() * 100.0D);

            try {
               ItemStack[] aitemstack = player.getInventory().getArmorContents();
               ItemStack[] aitemstack1 = aitemstack;
               int j = aitemstack.length;

               for(int k = 0; k < j; ++k) {
                  ItemStack itemstack = aitemstack1[k];
                  if(itemstack != null) {
                     if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("pyromaniac.level_I.chance")) {
                        player.setHealth(20.0D);
                        player.setFoodLevel(20);
                     }

                     if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("pyromaniac.level_II.chance")) {
                        player.setHealth(20.0D);
                        player.setFoodLevel(20);
                     }

                     if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("pyromaniac.level_III.chance")) {
                        player.setHealth(20.0D);
                        player.setFoodLevel(20);
                     }

                     if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("pyromaniac.level_IV.chance")) {
                        player.setHealth(20.0D);
                        player.setFoodLevel(20);
                     }

                     if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("pyromaniac.level_V.chance")) {
                        player.setHealth(20.0D);
                        player.setFoodLevel(20);
                     }
                  }
               }
            } catch (Exception exception) {
               ;
            }
         }
      }

   }
}
