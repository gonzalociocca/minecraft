package me.ayush_03.runesenchant.enchantments;

import java.util.Random;
import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class Smelt implements Listener {

   @EventHandler
   public void onBreak(BlockBreakEvent blockbreakevent) {
      if(!blockbreakevent.isCancelled()) {
         Player player = blockbreakevent.getPlayer();
         String s = FileManager.getManager().getEnchantments().getString("smelt");
         s = ChatColor.translateAlternateColorCodes('&', s);
         if(player.getItemInHand() != null && player.getInventory().getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
            ItemStack itemstack = player.getItemInHand();
            if(itemstack.getItemMeta().getLore().contains(s + " I")) {
               Block block = blockbreakevent.getBlock();
               int i = this.calculateFortune(player, block.getType());
               if(block.getType() != Material.IRON_ORE && block.getType() != Material.GOLD_ORE && block.getType() != Material.DIAMOND_ORE && block.getType() != Material.LAPIS_ORE) {
                  block.getType();
                  Material material = Material.EMERALD_ORE;
               }

               if(block.getType() == Material.IRON_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.GOLD_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.COAL_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.COAL, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.IRON_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.DIAMOND_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.DIAMOND, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.EMERALD_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.EMERALD, i));
                  block.setType(Material.AIR);
               }

               if(block.getType() == Material.LAPIS_ORE) {
                  blockbreakevent.getBlock().getWorld().dropItemNaturally(blockbreakevent.getBlock().getLocation(), new ItemStack(Material.INK_SACK, i, (short)4));
                  block.setType(Material.AIR);
               }
            }
         }

      }
   }

   public int calculateFortune(Player player, Material material) {
      int i = 1;
      if(player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
         i = (new Random()).nextInt(player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
         if(i <= 0) {
            i = 1;
         }

         i = (material == Material.LAPIS_ORE?4 + (new Random()).nextInt(5):1) * (i + 1);
      }

      return i;
   }
}
