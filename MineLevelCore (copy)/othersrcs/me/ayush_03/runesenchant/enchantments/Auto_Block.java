package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Auto_Block implements Listener {

   @EventHandler
   public void onBreak(BlockBreakEvent blockbreakevent) {
      Player player = blockbreakevent.getPlayer();
      String s = FileManager.getManager().getEnchantments().getString("auto_block");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(player.getInventory().getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
         this.autoBlock(player);
      }

   }

   public void autoBlock(Player player) {
      try {
         ItemStack[] aitemstack;
         int i = (aitemstack = player.getInventory().getContents()).length;

         for(int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack[j];
            int k = itemstack.getAmount() / 9;
            int l = itemstack.getAmount() / 4;
            if(k > 0) {
               if(itemstack.getType() == Material.EMERALD) {
                  removeItems(player.getInventory(), Material.EMERALD, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.EMERALD_BLOCK, k)});
               }

               if(itemstack.getType() == Material.DIAMOND) {
                  removeItems(player.getInventory(), Material.DIAMOND, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_BLOCK, k)});
               }

               if(itemstack.getType() == Material.IRON_INGOT) {
                  removeItems(player.getInventory(), Material.IRON_INGOT, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_BLOCK, k)});
               }

               if(itemstack.getType() == Material.GOLD_INGOT) {
                  removeItems(player.getInventory(), Material.GOLD_INGOT, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.GOLD_BLOCK, k)});
               }

               if(itemstack.getType() == Material.INK_SACK && itemstack.getData().getData() == 4) {
                  removeItems(player.getInventory(), Material.INK_SACK, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.LAPIS_BLOCK, k)});
               }

               if(itemstack.getType() == Material.COAL) {
                  removeItems(player.getInventory(), Material.COAL, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.COAL_BLOCK, k)});
               }

               if(itemstack.getType() == Material.REDSTONE) {
                  removeItems(player.getInventory(), Material.REDSTONE, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.REDSTONE_BLOCK, k)});
               }
            }

            if(l > 0) {
               if(itemstack.getType() == Material.CLAY_BALL) {
                  removeItems(player.getInventory(), Material.CLAY_BALL, k * 4);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.CLAY, k)});
               }

               if(itemstack.getType() == Material.SNOW_BALL) {
                  removeItems(player.getInventory(), Material.SNOW_BALL, k * 9);
                  player.getInventory().addItem(new ItemStack[]{new ItemStack(Material.SNOW, k)});
               }
            }
         }
      } catch (Exception exception) {
         ;
      }

   }

   public static void removeItems(Inventory inventory, Material material, int i) {
      if(i > 0) {
         int j = inventory.getSize();

         for(int k = 0; k < j; ++k) {
            ItemStack itemstack = inventory.getItem(k);
            if(itemstack != null && material == itemstack.getType()) {
               int l = itemstack.getAmount() - i;
               if(l > 0) {
                  itemstack.setAmount(l);
                  break;
               }

               inventory.clear(k);
               i = -l;
               if(i == 0) {
                  break;
               }
            }
         }

      }
   }
}
