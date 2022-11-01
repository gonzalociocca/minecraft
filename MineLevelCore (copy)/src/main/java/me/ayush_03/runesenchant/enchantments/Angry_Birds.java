package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Angry_Birds implements Listener {

   @EventHandler
   public void onEntityDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("angry_birds");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getEntity() instanceof Player && entitydamagebyentityevent.getDamager() instanceof Player) {
         Player player = (Player)entitydamagebyentityevent.getEntity();
         Player player1 = (Player)entitydamagebyentityevent.getDamager();
         int i = (int)(Math.random() * 100.0D);
         if(player1.getItemInHand() != null && player1.getItemInHand().hasItemMeta() && player1.getItemInHand().getItemMeta().hasLore()) {
            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("angry_birds.level_I.chance")) {
               player.setVelocity(new Vector(0, Main.getConfiguration().getInt("angry_birds.level_I.height"), 0));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("angry_birds.level_II.chance")) {
               player.setVelocity(new Vector(0, Main.getConfiguration().getInt("angry_birds.level_II.height"), 0));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("angry_birds.level_III.chance")) {
               player.setVelocity(new Vector(0, Main.getConfiguration().getInt("angry_birds.level_III.height"), 0));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("angry_birds.level_IV.chance")) {
               player.setVelocity(new Vector(0, Main.getConfiguration().getInt("angry_birds.level_IV.height"), 0));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("angry_birds.level_V.chance")) {
               player.setVelocity(new Vector(0, Main.getConfiguration().getInt("angry_birds.level_V.height"), 0));
            }
         }
      }

   }
}
