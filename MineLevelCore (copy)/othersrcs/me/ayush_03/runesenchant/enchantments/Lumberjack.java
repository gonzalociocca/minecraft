package me.ayush_03.runesenchant.enchantments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Lumberjack implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBreak(BlockBreakEvent blockbreakevent) {
      String s = FileManager.getManager().getEnchantments().getString("lumberjack");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(!blockbreakevent.isCancelled()) {
         Player player = blockbreakevent.getPlayer();
         if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I") && blockbreakevent.getBlock().getType() == Material.LOG) {
            ArrayList arraylist = new ArrayList();
            arraylist.add(Material.LOG);
            arraylist.add(Material.LEAVES);
            Set set = this.getTree(blockbreakevent.getBlock(), arraylist);
            Iterator iterator = set.iterator();

            while(iterator.hasNext()) {
               Block block = (Block)iterator.next();
               block.breakNaturally();
            }
         }

      }
   }

   private Set getNearbyBlocks(Block block, List list, HashSet hashset) {
      for(int i = -1; i < 2; ++i) {
         for(int j = -1; j < 2; ++j) {
            for(int k = -1; k < 2; ++k) {
               Block block1 = block.getLocation().clone().add((double)i, (double)j, (double)k).getBlock();
               if(block1 != null && !hashset.contains(block1) && list.contains(block1.getType())) {
                  hashset.add(block1);
                  hashset.addAll(this.getNearbyBlocks(block1, list, hashset));
               }
            }
         }
      }

      return hashset;
   }

   public Set getTree(Block block, List list) {
      return this.getNearbyBlocks(block, list, new HashSet());
   }
}
