package me.ayush_03.runesenchant.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Wolves implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("wolves");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player) {
         try {
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            Player player = (Player)entitydamagebyentityevent.getDamager();
            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(entitydamagebyentityevent.getEntity().getWorld()).getApplicableRegions(entitydamagebyentityevent.getEntity().getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            Player player1 = (Player)entitydamagebyentityevent.getEntity();
            ItemStack[] aitemstack = player1.getInventory().getArmorContents();
            int i = (int)(Math.random() * 100.0D);
            ItemStack[] aitemstack1 = aitemstack;
            int j = aitemstack.length;

            for(int k = 0; k < j; ++k) {
               ItemStack itemstack = aitemstack1[k];
               int l;
               Wolf wolf;
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("wolves.level_I.chance")) {
                  for(l = 0; l < Main.getConfiguration().getInt("wolves.level_I.wolves"); ++l) {
                     wolf = (Wolf)player.getWorld().spawn(player.getLocation(), Wolf.class);
                     wolf.setTamed(true);
                     wolf.setOwner(player1);
                     wolf.setAdult();
                     wolf.setAngry(true);
                     wolf.setTarget(player);
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("wolves.level_II.chance")) {
                  for(l = 0; l < Main.getConfiguration().getInt("wolves.level_II.wolves"); ++l) {
                     wolf = (Wolf)player.getWorld().spawn(player.getLocation(), Wolf.class);
                     wolf.setTamed(true);
                     wolf.setOwner(player1);
                     wolf.setAdult();
                     wolf.setAngry(true);
                     wolf.setTarget(player);
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains("§7Wolves III") && i < Main.getConfiguration().getInt("wolves.level_III.chance")) {
                  for(l = 0; l < Main.getConfiguration().getInt("wolves.level_III.wolves"); ++l) {
                     wolf = (Wolf)player.getWorld().spawn(player.getLocation(), Wolf.class);
                     wolf.setTamed(true);
                     wolf.setOwner(player1);
                     wolf.setAdult();
                     wolf.setAngry(true);
                     wolf.setTarget(player);
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains("§7Wolves IV") && i < Main.getConfiguration().getInt("wolves.level_IV.chance")) {
                  for(l = 0; l < Main.getConfiguration().getInt("wolves.level_IV.wolves"); ++l) {
                     wolf = (Wolf)player.getWorld().spawn(player.getLocation(), Wolf.class);
                     wolf.setTamed(true);
                     wolf.setOwner(player1);
                     wolf.setAdult();
                     wolf.setAngry(true);
                     wolf.setTarget(player);
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains("§7Wolves V") && i < Main.getConfiguration().getInt("wolves.level_V.chance")) {
                  for(l = 0; l < Main.getConfiguration().getInt("wolves.level_V.wolves"); ++l) {
                     wolf = (Wolf)player.getWorld().spawn(player.getLocation(), Wolf.class);
                     wolf.setTamed(true);
                     wolf.setOwner(player1);
                     wolf.setAdult();
                     wolf.setAngry(true);
                     wolf.setTarget(player);
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
