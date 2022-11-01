package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Riftslayer implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("riftslayer");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && !(entitydamagebyentityevent.getEntity() instanceof Player)) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(player.getItemInHand() != null && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 3.0D);
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
