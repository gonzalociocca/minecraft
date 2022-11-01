package me.ayush_03.runesenchant.enchantments;

import java.util.Iterator;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Shearer implements Listener {

   @EventHandler
   public void onInteract(PlayerInteractEvent playerinteractevent) {
      Player player = playerinteractevent.getPlayer();
      String s = FileManager.getManager().getEnchantments().getString("shearer");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(playerinteractevent.getAction() == Action.LEFT_CLICK_AIR && player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
         int i = Main.getConfiguration().getInt("shearer.level_I.radius");
         Iterator iterator = player.getNearbyEntities((double)i, (double)i, (double)i).iterator();

         while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(entity instanceof Sheep) {
               Sheep sheep = (Sheep)entity;
               if(!sheep.isSheared()) {
                  sheep.setSheared(true);
                  sheep.getWorld().dropItem(sheep.getLocation(), new ItemStack(Material.WOOL, 1, sheep.getColor().getWoolData()));
               }
            }
         }
      }

   }
}
