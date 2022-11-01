package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Plunder implements Listener {

   @EventHandler
   public void onDeath(EntityDeathEvent entitydeathevent) {
      String s = FileManager.getManager().getEnchantments().getString("plunder");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydeathevent.getEntity().getKiller() instanceof Player && !(entitydeathevent.getEntity() instanceof Player)) {
         Player player = entitydeathevent.getEntity().getKiller();
         if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
            entitydeathevent.setDroppedExp(20);
         }
      }

   }
}
