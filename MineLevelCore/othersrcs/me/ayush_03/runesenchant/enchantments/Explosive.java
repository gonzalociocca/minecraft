package me.ayush_03.runesenchant.enchantments;

import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.ayush_03.runesenchant.ExplosiveEvent;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import me.ayush_03.runesenchant.VeinminerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Explosive implements Listener {

   @EventHandler
   public void onBreak(BlockBreakEvent blockbreakevent) {
      if(!blockbreakevent.isCancelled()) {
         if(!(blockbreakevent instanceof ExplosiveEvent) && !(blockbreakevent instanceof VeinminerEvent)) {
            //prevent in pvp
            if(blockbreakevent.getPlayer().getWorld().getPVP()){
               return;
            }
            String s = FileManager.getManager().getEnchantments().getString("explosive");
            s = ChatColor.translateAlternateColorCodes('&', s);
            Player player = blockbreakevent.getPlayer();
            if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
               ItemStack itemstack = player.getItemInHand();
               ItemMeta itemmeta = itemstack.getItemMeta();
               ArrayList arraylist;
               Location location;
               Iterator iterator;
               ExplosiveEvent explosiveevent;
               if(itemmeta.getLore().contains(s + " I")) {
                  blockbreakevent.setCancelled(true);
                  if(Main.getConfiguration().getBoolean("is-1.9")) {
                     player.playSound(player.getLocation(), Sound.valueOf("ENTITY_GENERIC_EXPLODE"), 1.0F, 1.0F);
                  } else {
                     player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                  }

                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while(iterator.hasNext()) {
                     location = (Location)iterator.next();
                     if(location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     explosiveevent = new ExplosiveEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(explosiveevent);
                     if(!explosiveevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if(itemmeta.getLore().contains(s + " II")) {
                  blockbreakevent.setCancelled(true);
                  if(Main.getConfiguration().getBoolean("is-1.9")) {
                     player.playSound(player.getLocation(), Sound.valueOf("ENTITY_GENERIC_EXPLODE"), 1.0F, 1.0F);
                  } else {
                     player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                  }

                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate2(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while(iterator.hasNext()) {
                     location = (Location)iterator.next();
                     if(location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     explosiveevent = new ExplosiveEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(explosiveevent);
                     if(!explosiveevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if(itemmeta.getLore().contains(s + " III")) {
                  blockbreakevent.setCancelled(true);
                  if(Main.getConfiguration().getBoolean("is-1.9")) {
                     player.playSound(player.getLocation(), Sound.valueOf("ENTITY_GENERIC_EXPLODE"), 1.0F, 1.0F);
                  } else {
                     player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                  }

                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate3(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while(iterator.hasNext()) {
                     location = (Location)iterator.next();
                     if(location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     explosiveevent = new ExplosiveEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(explosiveevent);
                     if(!explosiveevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if(itemmeta.getLore().contains(s + " IV")) {
                  blockbreakevent.setCancelled(true);
                  if(Main.getConfiguration().getBoolean("is-1.9")) {
                     player.playSound(player.getLocation(), Sound.valueOf("ENTITY_GENERIC_EXPLODE"), 1.0F, 1.0F);
                  } else {
                     player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                  }

                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate4(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while(iterator.hasNext()) {
                     location = (Location)iterator.next();
                     if(location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     explosiveevent = new ExplosiveEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(explosiveevent);
                     if(!explosiveevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if(itemmeta.getLore().contains(s + " V")) {
                  blockbreakevent.setCancelled(true);
                  if(Main.getConfiguration().getBoolean("is-1.9")) {
                     player.playSound(player.getLocation(), Sound.valueOf("ENTITY_GENERIC_EXPLODE"), 1.0F, 1.0F);
                  } else {
                     player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                  }

                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate5(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while(iterator.hasNext()) {
                     location = (Location)iterator.next();
                     if(location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     explosiveevent = new ExplosiveEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(explosiveevent);
                     if(!explosiveevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }
            }

         }
      }
   }

   public void calculate(Location location, Location location1, List list) {
      int i = Main.getConfiguration().getInt("explosive.level_I.power");
      int j = Main.getConfiguration().getInt("explosive.level_I.max-blocks");
      if(list.size() <= j) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for(int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);
            if(!list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double)(i * 2)) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate2(Location location, Location location1, List list) {
      int i = Main.getConfiguration().getInt("explosive.level_II.power");
      int j = Main.getConfiguration().getInt("explosive.level_II.max-blocks");
      if(list.size() <= j) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for(int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);
            if(!list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double)(i * 2)) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate3(Location location, Location location1, List list) {
      int i = Main.getConfiguration().getInt("explosive.level_III.max-blocks");
      int j = Main.getConfiguration().getInt("explosive.level_III.power");
      if(list.size() <= i) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for(int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);
            if(!list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double)(j * 2)) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate4(Location location, Location location1, List list) {
      int i = Main.getConfiguration().getInt("explosive.level_IV.power");
      int j = Main.getConfiguration().getInt("explosive.level_IV.max-blocks");
      if(list.size() <= j) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for(int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);
            if(!list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double)(i * 2)) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate5(Location location, Location location1, List list) {
      int i = Main.getConfiguration().getInt("explosive.level_V.power");
      int j = Main.getConfiguration().getInt("explosive.level_V.max-blocks");
      if(list.size() <= j) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for(int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);
            if(!list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double)(i * 2)) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
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

   public void exempt(final Player player) {
      if(Main.getConfiguration().getBoolean("using-NCP")) {
         NCPExemptionManager.exemptPermanently(player);
         Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
            public void run() {
               NCPExemptionManager.unexempt(player);
            }
         }, 100L);
      }
   }

   public void spartan(Player player) {
   }
}
