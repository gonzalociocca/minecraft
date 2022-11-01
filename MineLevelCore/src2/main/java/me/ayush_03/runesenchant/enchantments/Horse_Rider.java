package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Horse_Rider implements Listener {

   @EventHandler
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("horse_rider");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && !(entitydamagebyentityevent.getEntity() instanceof Player)) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            ItemStack[] aitemstack = player.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for(int j = 0; j < i; ++j) {
               ItemStack itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && entitydamagebyentityevent.getEntity() instanceof Horse) {
                  entitydamagebyentityevent.setCancelled(true);
                  entitydamagebyentityevent.setDamage(0.0D);
                  Horse horse = (Horse)entitydamagebyentityevent.getEntity();
                  if(horse.isTamed()) {
                     return;
                  }

                  horse.setTamed(true);
                  horse.setOwner(player);
                  horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
