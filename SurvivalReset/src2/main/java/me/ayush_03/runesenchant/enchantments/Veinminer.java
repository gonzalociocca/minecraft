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
import me.gonzalociocca.minelevel.core.Colorizer;
import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class Veinminer implements Listener {

   @EventHandler
   public void onBreak(BlockBreakEvent blockbreakevent) {
      if(blockbreakevent.isCancelled()){
         return;
      }
      if(blockbreakevent.getPlayer().getWorld().getPVP()){
         return;
      }
      Player player = blockbreakevent.getPlayer();

      if (!(blockbreakevent instanceof VeinminerEvent) && !(blockbreakevent instanceof ExplosiveEvent)) {

         String s = FileManager.getManager().getEnchantments().getString("veinminer");

         s = Colorizer.Color(s);
         if (blockbreakevent.getBlock().getType() != Material.SKULL) {
            if (player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
               ItemStack itemstack = player.getItemInHand();
               ItemMeta itemmeta = itemstack.getItemMeta();
               ArrayList arraylist;
               Location location;
               Iterator iterator;
               VeinminerEvent veinminerevent;

               if (itemmeta.getLore().contains(s + " I")) {
                  blockbreakevent.setCancelled(true);
                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while (iterator.hasNext()) {
                     location = (Location) iterator.next();
                     if (location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     veinminerevent = new VeinminerEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(veinminerevent);
                     if (!veinminerevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if (itemmeta.getLore().contains(s + " II")) {
                  blockbreakevent.setCancelled(true);
                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate2(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while (iterator.hasNext()) {
                     location = (Location) iterator.next();
                     if (location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     veinminerevent = new VeinminerEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(veinminerevent);
                     if (!veinminerevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }

               if (itemmeta.getLore().contains(s + " III")) {
                  blockbreakevent.setCancelled(true);
                  arraylist = new ArrayList();
                  arraylist.add(blockbreakevent.getBlock().getLocation());
                  this.calculate3(blockbreakevent.getBlock().getLocation(), blockbreakevent.getBlock().getLocation(), arraylist);
                  iterator = arraylist.iterator();

                  while (iterator.hasNext()) {
                     location = (Location) iterator.next();
                     if (location.getBlock().getType() == Material.BEDROCK) {
                        return;
                     }

                     this.exempt(player);
                     this.spartan(player);
                     veinminerevent = new VeinminerEvent(location.getBlock(), player);
                     Bukkit.getServer().getPluginManager().callEvent(veinminerevent);
                     if (!veinminerevent.isCancelled()) {
                        location.getBlock().breakNaturally();
                     }
                  }
               }
            }

         }
      }
   }

   public void calculate(Location location, Location location1, List<Location> list) {
      int i = Main.getConfiguration().getInt("veinminer.level_I.max-blocks");
      int j = Main.getConfiguration().getInt("veinminer.level_I.radius");

      if (list.size() <= i) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for (int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);

            if (block.getType() == location1.getBlock().getType() && !list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double) j) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate2(Location location, Location location1, List<Location> list) {
      int i = Main.getConfiguration().getInt("veinminer.level_II.max-blocks");
      int j = Main.getConfiguration().getInt("veinminer.level_II.radius");

      if (list.size() <= i) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for (int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);

            if (block.getType() == location1.getBlock().getType() && !list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double) j) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public void calculate3(Location location, Location location1, List<Location> list) {
      int i = Main.getConfiguration().getInt("veinminer.level_III.max-blocks");
      int j = Main.getConfiguration().getInt("veinminer.level_III.radius");

      if (list.size() <= i) {
         BlockFace[] ablockface;
         int k = (ablockface = BlockFace.values()).length;

         for (int l = 0; l < k; ++l) {
            BlockFace blockface = ablockface[l];
            Block block = location1.getBlock().getRelative(blockface);

            if (block.getType() == location1.getBlock().getType() && !list.contains(block.getLocation()) && location.distance(block.getLocation()) <= (double) j) {
               list.add(block.getLocation());
               this.calculate(location, block.getLocation(), list);
            }
         }

      }
   }

   public int calculateFortune(Player player, Material material) {
      int i = 1;

      if (player.getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
         i = (new Random()).nextInt(player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
         if (i <= 0) {
            i = 1;
         }

         i = (material == Material.LAPIS_ORE ? 4 + (new Random()).nextInt(5) : 1) * (i + 1);
      }

      return i;
   }
   public void exempt(final Player paramPlayer)
   {
      if (!Main.getConfiguration().getBoolean("using-NCP")) {
         return;
      }

      NCPExemptionManager.exemptPermanently(paramPlayer);
      Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new BukkitRunnable()
      {
         public void run()
         {
            NCPExemptionManager.unexempt(paramPlayer);
         }
      }, 100L);
   }

   public void spartan(Player paramPlayer)
   {
      if (!Main.getConfiguration().getBoolean("using-Spartan")) {
         return;
      }

      API.cancelCheck(paramPlayer, Enums.HackType.GhostHand, 60);
      API.cancelCheck(paramPlayer, Enums.HackType.NoSwing, 60);
      API.cancelCheck(paramPlayer, Enums.HackType.BlockReach, 60);
      API.cancelCheck(paramPlayer, Enums.HackType.Liquids, 60);
      API.cancelCheck(paramPlayer, Enums.HackType.Speed, 60);
      API.cancelCheck(paramPlayer, Enums.HackType.Nuker, 60);

   }
}
