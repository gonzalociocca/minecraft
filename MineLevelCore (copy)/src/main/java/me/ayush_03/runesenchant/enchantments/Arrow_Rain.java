package me.ayush_03.runesenchant.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import java.util.ArrayList;
import java.util.Iterator;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Arrow_Rain implements Listener {

   private ArrayList special = new ArrayList();
   private ArrayList special2 = new ArrayList();
   private ArrayList special3 = new ArrayList();
   private ArrayList special4 = new ArrayList();
   private ArrayList special5 = new ArrayList();

   @EventHandler
   public void onDamage(ProjectileLaunchEvent projectilelaunchevent) {
      String s = FileManager.getManager().getEnchantments().getString("arrow_rain");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(projectilelaunchevent.getEntity() instanceof Arrow) {
         Arrow arrow = (Arrow)projectilelaunchevent.getEntity();
         if(arrow.getShooter() instanceof Player) {
            Player player = (Player)arrow.getShooter();

            try {
               if(player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
                  this.special.add(arrow);
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " II")) {
                  this.special2.add(arrow);
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " III")) {
                  this.special3.add(arrow);
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " IV")) {
                  this.special4.add(arrow);
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " V")) {
                  this.special5.add(arrow);
               }
            } catch (Exception exception) {
               ;
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      int i = (int)(Math.random() * 100.0D);
      if(!entitydamagebyentityevent.isCancelled()) {
         if(Main.getConfiguration().getBoolean("allow-worldguard")) {
            ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(entitydamagebyentityevent.getEntity().getWorld()).getApplicableRegions(entitydamagebyentityevent.getEntity().getLocation());
            if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
               return;
            }
         }

         if(entitydamagebyentityevent.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile)entitydamagebyentityevent.getDamager();
            Location location;
            ArrayList arraylist;
            int j;
            int k;
            Entity entity;
            Location location1;
            Iterator iterator;
            if(this.special.contains(projectile) && i < Main.getConfiguration().getInt("arrow_rain.level_I.chance")) {
               location = entitydamagebyentityevent.getEntity().getLocation().add(0.0D, 20.0D, 0.0D);
               arraylist = new ArrayList();

               for(j = -2; j <= 2; ++j) {
                  for(k = -2; k <= 2; ++k) {
                     arraylist.add(location.clone().add((double)j, 0.0D, (double)k));
                  }
               }

               for(iterator = arraylist.iterator(); iterator.hasNext(); entity = location1.getWorld().spawnEntity(location1, EntityType.ARROW)) {
                  location1 = (Location)iterator.next();
               }
            }

            if(this.special2.contains(projectile) && i < Main.getConfiguration().getInt("arrow_rain.level_II.chance")) {
               location = entitydamagebyentityevent.getEntity().getLocation().add(0.0D, 20.0D, 0.0D);
               arraylist = new ArrayList();

               for(j = -3; j <= 2; ++j) {
                  for(k = -3; k <= 2; ++k) {
                     arraylist.add(location.clone().add((double)j, 0.0D, (double)k));
                  }
               }

               for(iterator = arraylist.iterator(); iterator.hasNext(); entity = location1.getWorld().spawnEntity(location1, EntityType.ARROW)) {
                  location1 = (Location)iterator.next();
               }
            }

            if(this.special3.contains(projectile) && i < Main.getConfiguration().getInt("arrow_rain.level_III.chance")) {
               location = entitydamagebyentityevent.getEntity().getLocation().add(0.0D, 20.0D, 0.0D);
               arraylist = new ArrayList();

               for(j = -4; j <= 2; ++j) {
                  for(k = -4; k <= 2; ++k) {
                     arraylist.add(location.clone().add((double)j, 0.0D, (double)k));
                  }
               }

               for(iterator = arraylist.iterator(); iterator.hasNext(); entity = location1.getWorld().spawnEntity(location1, EntityType.ARROW)) {
                  location1 = (Location)iterator.next();
               }
            }

            if(this.special4.contains(projectile) && i < Main.getConfiguration().getInt("arrow_rain.level_IV.chance")) {
               location = entitydamagebyentityevent.getEntity().getLocation().add(0.0D, 20.0D, 0.0D);
               arraylist = new ArrayList();

               for(j = -5; j <= 2; ++j) {
                  for(k = -5; k <= 2; ++k) {
                     arraylist.add(location.clone().add((double)j, 0.0D, (double)k));
                  }
               }

               for(iterator = arraylist.iterator(); iterator.hasNext(); entity = location1.getWorld().spawnEntity(location1, EntityType.ARROW)) {
                  location1 = (Location)iterator.next();
               }
            }

            if(this.special5.contains(projectile) && i < Main.getConfiguration().getInt("arrow_rain.level_V.chance")) {
               location = entitydamagebyentityevent.getEntity().getLocation().add(0.0D, 20.0D, 0.0D);
               arraylist = new ArrayList();

               for(j = -6; j <= 2; ++j) {
                  for(k = -6; k <= 2; ++k) {
                     arraylist.add(location.clone().add((double)j, 0.0D, (double)k));
                  }
               }

               for(iterator = arraylist.iterator(); iterator.hasNext(); entity = location1.getWorld().spawnEntity(location1, EntityType.ARROW)) {
                  location1 = (Location)iterator.next();
               }
            }
         }

      }
   }
}
