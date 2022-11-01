package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class Feather_Fall implements Listener {

   @EventHandler
   public void onDamage(EntityDamageEvent entitydamageevent) {
      String s = FileManager.getManager().getEnchantments().getString("feather_fall");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamageevent.getEntity() instanceof Player) {
         Player player = (Player)entitydamageevent.getEntity();
         ItemStack[] aitemstack;
         int i = (aitemstack = player.getInventory().getArmorContents()).length;

         for(int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack[j];
            if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && entitydamageevent.getCause() == DamageCause.FALL && itemstack.getItemMeta().getLore().contains(s + " I")) {
               entitydamageevent.setCancelled(true);
            }
         }
      }

   }
}
