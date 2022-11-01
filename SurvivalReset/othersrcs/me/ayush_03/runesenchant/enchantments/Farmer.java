package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Farmer implements Listener {

   @EventHandler
   public void onPlace(BlockPlaceEvent blockplaceevent) {
      String s = FileManager.getManager().getEnchantments().getString("farmer");
      s = ChatColor.translateAlternateColorCodes('&', s);
      Player player = blockplaceevent.getPlayer();

      try {
         ItemStack[] aitemstack;
         int i = (aitemstack = player.getInventory().getArmorContents()).length;

         for(int j = 0; j < i; ++j) {
            ItemStack itemstack = aitemstack[j];
            if(itemstack.hasItemMeta() && itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
               if(player.getItemInHand().getType() == Material.CARROT_ITEM) {
                  blockplaceevent.getBlockPlaced().setTypeIdAndData(Material.CARROT.getId(), (byte)7, true);
               }

               if(player.getItemInHand().getType() == Material.MELON_SEEDS) {
                  blockplaceevent.getBlockPlaced().setType(Material.MELON);
               }

               if(player.getItemInHand().getType() == Material.POTATO_ITEM) {
                  blockplaceevent.getBlockPlaced().setTypeIdAndData(Material.POTATO.getId(), (byte)7, true);
               }

               if(player.getItemInHand().getType() == Material.SEEDS) {
                  blockplaceevent.getBlockPlaced().setTypeIdAndData(Material.CROPS.getId(), (byte)7, true);
               }

               if(player.getItemInHand().getType() == Material.PUMPKIN_SEEDS) {
                  blockplaceevent.getBlockPlaced().setType(Material.PUMPKIN);
               }
            }
         }
      } catch (Exception exception) {
         ;
      }

   }
}
