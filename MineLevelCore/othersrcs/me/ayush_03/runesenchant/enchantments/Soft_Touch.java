package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Soft_Touch implements Listener {

   @EventHandler
   public void onBlockBreak(BlockBreakEvent blockbreakevent) {
      Player player = blockbreakevent.getPlayer();
      Block block = blockbreakevent.getBlock();
      String s = FileManager.getManager().getEnchantments().getString("soft_touch");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && block.getType() == Material.MOB_SPAWNER) {
            CreatureSpawner creaturespawner = (CreatureSpawner)block.getState();
            ItemStack itemstack = new ItemStack(creaturespawner.getType(), 1, block.getData());
            ItemMeta itemmeta = itemstack.getItemMeta();
            itemmeta.setDisplayName(ChatColor.AQUA + creaturespawner.getCreatureTypeName());
            itemstack.setItemMeta(itemmeta);
            if(!blockbreakevent.isCancelled()) {
               block.getLocation().getWorld().dropItem(block.getLocation(), itemstack);
            }
         }
      } catch (Exception exception) {
         ;
      }

   }

   @EventHandler
   public void onPlace(BlockPlaceEvent blockplaceevent) {
      Block block = blockplaceevent.getBlock();

      try {
         if(block.getType() == Material.MOB_SPAWNER) {
            String s = ChatColor.stripColor(blockplaceevent.getItemInHand().getItemMeta().getDisplayName());
            CreatureSpawner creaturespawner = (CreatureSpawner)block.getState();
            creaturespawner.setSpawnedType(EntityType.fromName(s));
         }
      } catch (Exception exception) {
         ;
      }

   }
}
