package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Animal_Tamer implements Listener {

   @EventHandler
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("animal_tamer");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && !(entitydamagebyentityevent.getEntity() instanceof Player)) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            ItemStack[] aitemstack = player.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for(int j = 0; j < i; ++j) {
               ItemStack itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && entitydamagebyentityevent.getEntity() instanceof Wolf) {
                  entitydamagebyentityevent.setCancelled(true);
                  entitydamagebyentityevent.setDamage(0.0D);
                  Wolf wolf = (Wolf)entitydamagebyentityevent.getEntity();
                  if(wolf.isTamed()) {
                     return;
                  }

                  wolf.setTamed(true);
                  wolf.setOwner(player);
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
