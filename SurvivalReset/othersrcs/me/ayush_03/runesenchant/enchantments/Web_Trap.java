package me.ayush_03.runesenchant.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import java.util.ArrayList;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Web_Trap implements Listener {

   private ArrayList temp = new ArrayList();

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("web_trap");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getEntity() instanceof Player && entitydamagebyentityevent.getDamager() instanceof Player) {
         try {
            Player player = (Player)entitydamagebyentityevent.getEntity();
            Player player1 = (Player)entitydamagebyentityevent.getDamager();
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            int i = (int)(Math.random() * 100.0D);
            ApplicableRegionSet applicableregionset1;
            final Block block;
            final Location location;
            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("web_trap.level_I.chance")) {
               if(Main.getConfiguration().getBoolean("web_trap.check-wg-flag")) {
                  applicableregionset1 = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
                  if(applicableregionset1.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.BUILD}) == State.DENY) {
                     return;
                  }
               }

               location = player.getLocation();
               block = location.getBlock();
               this.temp.add(block);
               location.getBlock().setType(Material.WEB);
               Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                  public void run() {
                     block.setType(Material.AIR);
                     Web_Trap.this.temp.clear();
                  }
               }, (long)(Main.getConfiguration().getInt("web_trap.level_I.duration") * 20));
            }

           else if(player1.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("web_trap.level_II.chance")) {
               if(Main.getConfiguration().getBoolean("web_trap.check-wg-flag")) {
                  applicableregionset1 = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
                  if(applicableregionset1.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.BUILD}) == State.DENY) {
                     return;
                  }
               }

               location = player.getLocation();
               block = location.getBlock();
               this.temp.add(block);
               location.getBlock().setType(Material.WEB);
               Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                  public void run() {
                     location.getBlock().setType(Material.AIR);
                     Web_Trap.this.temp.clear();
                  }
               }, (long)(Main.getConfiguration().getInt("web_trap.level_II.duration") * 20));
            }

           else if(player1.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("web_trap.level_III.chance")) {
               if(Main.getConfiguration().getBoolean("web_trap.check-wg-flag")) {
                  applicableregionset1 = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
                  if(applicableregionset1.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.BUILD}) == State.DENY) {
                     return;
                  }
               }

               location = player.getLocation();
               block = location.getBlock();
               this.temp.add(block);
               location.getBlock().setType(Material.WEB);
               Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new BukkitRunnable() {
                  public void run() {
                     location.getBlock().setType(Material.AIR);
                     Web_Trap.this.temp.clear();
                  }
               }, (long)(Main.getConfiguration().getInt("web_trap.level_III.duration") * 20));
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
