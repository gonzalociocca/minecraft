package me.ayush_03.runesenchant.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Plow implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onInteract(PlayerInteractEvent playerinteractevent) {
      if(!playerinteractevent.isCancelled()) {
         String s = FileManager.getManager().getEnchantments().getString("plow");
         s = ChatColor.translateAlternateColorCodes('&', s);
         Player player = playerinteractevent.getPlayer();
         if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
            Block block = playerinteractevent.getClickedBlock();
            if(block.getType() != Material.DIRT && block.getType() != Material.GRASS) {
               return;
            }

            double d0;
            Location location;
            double d1;
            double d2;
            Block block1;
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
               d0 = 0.5D;
               location = block.getLocation();

               for(d1 = (double)location.getBlockX() - d0; d1 <= (double)location.getBlockX() + d0; ++d1) {
                  for(d2 = (double)location.getBlockZ() - d0; d2 <= (double)location.getBlockZ() + d0; ++d2) {
                     block1 = location.getWorld().getBlockAt(new Location(block.getWorld(), d1, (double)block.getY(), d2));
                     if(block1.getType() != Material.GRASS && block1.getType() != Material.DIRT) {
                        return;
                     }

                     if(Main.getConfiguration().getBoolean("allow-worldguard") && !WGBukkit.getPlugin().canBuild(player, block1)) {
                        return;
                     }

                     block1.setType(Material.SOIL);
                  }
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II")) {
               d0 = 1.0D;
               location = block.getLocation();

               for(d1 = (double)location.getBlockX() - d0; d1 <= (double)location.getBlockX() + d0; ++d1) {
                  for(d2 = (double)location.getBlockZ() - d0; d2 <= (double)location.getBlockZ() + d0; ++d2) {
                     block1 = location.getWorld().getBlockAt(new Location(block.getWorld(), d1, (double)block.getY(), d2));
                     if(block1.getType() != Material.GRASS && block1.getType() != Material.DIRT) {
                        return;
                     }

                     if(Main.getConfiguration().getBoolean("allow-worldguard") && !WGBukkit.getPlugin().canBuild(player, block1)) {
                        return;
                     }

                     block1.setType(Material.SOIL);
                  }
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III")) {
               d0 = 1.5D;
               location = block.getLocation();

               for(d1 = (double)location.getBlockX() - d0; d1 <= (double)location.getBlockX() + d0; ++d1) {
                  for(d2 = (double)location.getBlockZ() - d0; d2 <= (double)location.getBlockZ() + d0; ++d2) {
                     block1 = location.getWorld().getBlockAt(new Location(block.getWorld(), d1, (double)block.getY(), d2));
                     if(block1.getType() != Material.GRASS && block1.getType() != Material.DIRT) {
                        return;
                     }

                     if(Main.getConfiguration().getBoolean("allow-worldguard") && !WGBukkit.getPlugin().canBuild(player, block1)) {
                        return;
                     }

                     block1.setType(Material.SOIL);
                  }
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " IV")) {
               d0 = 2.0D;
               location = block.getLocation();

               for(d1 = (double)location.getBlockX() - d0; d1 <= (double)location.getBlockX() + d0; ++d1) {
                  for(d2 = (double)location.getBlockZ() - d0; d2 <= (double)location.getBlockZ() + d0; ++d2) {
                     block1 = location.getWorld().getBlockAt(new Location(block.getWorld(), d1, (double)block.getY(), d2));
                     if(block1.getType() != Material.GRASS && block1.getType() != Material.DIRT) {
                        return;
                     }

                     if(Main.getConfiguration().getBoolean("allow-worldguard") && !WGBukkit.getPlugin().canBuild(player, block1)) {
                        return;
                     }

                     block1.setType(Material.SOIL);
                  }
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " V")) {
               d0 = 2.5D;
               location = block.getLocation();

               for(d1 = (double)location.getBlockX() - d0; d1 <= (double)location.getBlockX() + d0; ++d1) {
                  for(d2 = (double)location.getBlockZ() - d0; d2 <= (double)location.getBlockZ() + d0; ++d2) {
                     block1 = location.getWorld().getBlockAt(new Location(block.getWorld(), d1, (double)block.getY(), d2));
                     if(block1.getType() != Material.GRASS && block1.getType() != Material.DIRT) {
                        return;
                     }

                     if(Main.getConfiguration().getBoolean("allow-worldguard") && !WGBukkit.getPlugin().canBuild(player, block1)) {
                        return;
                     }

                     block1.setType(Material.SOIL);
                  }
               }
            }
         }

      }
   }
}
