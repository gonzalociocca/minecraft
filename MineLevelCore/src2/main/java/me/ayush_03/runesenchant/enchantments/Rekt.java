package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Rekt implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      if(!(entitydamagebyentityevent.getEntity() instanceof Player)) {
         String s = FileManager.getManager().getEnchantments().getString("rekt");
         s = ChatColor.translateAlternateColorCodes('&', s);
         if(entitydamagebyentityevent.getDamager() instanceof Player) {
            try {
               Player player = (Player)entitydamagebyentityevent.getDamager();
               if(entitydamagebyentityevent.isCancelled()) {
                  return;
               }

               if(player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
                  entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
               }
            } catch (Exception exception) {
               ;
            }
         }

      }
   }
}
