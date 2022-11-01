package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Aegis implements Listener {

   @EventHandler
   public void block(PlayerInteractEvent playerinteractevent) {
      String s = FileManager.getManager().getEnchantments().getString("aegis");
      s = ChatColor.translateAlternateColorCodes('&', s);
      Player player = playerinteractevent.getPlayer();
      int i = (int)(Math.random() * 100.0D);
      ItemStack[] aitemstack = player.getInventory().getArmorContents();
      if(playerinteractevent.getAction() == Action.RIGHT_CLICK_AIR) {
         try {
            if(player.getItemInHand().getType() == Material.DIAMOND_SWORD || player.getItemInHand().getType() == Material.IRON_SWORD || player.getItemInHand().getType() == Material.GOLD_SWORD || player.getItemInHand().getType() == Material.STONE_SWORD || player.getItemInHand().getType() == Material.WOOD_SWORD) {
               ItemStack[] aitemstack1 = aitemstack;
               int j = aitemstack.length;

               for(int k = 0; k < j; ++k) {
                  ItemStack itemstack = aitemstack1[k];
                  if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("aegis.level_I.chance")) {
                     player.setHealth(player.getHealth() + 1.0D);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("aegis.level_II.chance")) {
                     player.setHealth(player.getHealth() + 1.0D);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("aegis.level_III.chance")) {
                     player.setHealth(player.getHealth() + 2.0D);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("aegis.level_IV.chance")) {
                     player.setHealth(player.getHealth() + 2.0D);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains("§7Aegis V") && i < Main.getConfiguration().getInt("aegis.level_V.chance")) {
                     player.setHealth(player.getHealth() + 3.0D);
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }

      }
   }
}
