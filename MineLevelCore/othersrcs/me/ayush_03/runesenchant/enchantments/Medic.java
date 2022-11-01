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
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class Medic implements Listener {

   private ArrayList special = new ArrayList();

   @EventHandler
   public void onDamage(ProjectileLaunchEvent projectilelaunchevent) {
      String s = FileManager.getManager().getEnchantments().getString("medic");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(projectilelaunchevent.getEntity() instanceof Arrow) {
         Arrow arrow = (Arrow)projectilelaunchevent.getEntity();
         if(arrow.getShooter() instanceof Player) {
            Player player = (Player)arrow.getShooter();

            try {
               if(player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
                  this.special.add(arrow);
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
      if(entitydamagebyentityevent.getDamager() instanceof Projectile) {
         Projectile projectile = (Projectile)entitydamagebyentityevent.getDamager();
         if(this.special.contains(projectile) && entitydamagebyentityevent.getEntity() instanceof Player) {
            Player player = (Player)entitydamagebyentityevent.getEntity();
            if(projectile.getShooter() instanceof Player) {
               Player player1 = (Player)projectile.getShooter();
               if(entitydamagebyentityevent.isCancelled()) {
                  return;
               }

               if(Main.getConfiguration().getBoolean("allow-worldguard")) {
                  ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
                  if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                     return;
                  }
               }

               if(player.getName().equalsIgnoreCase(player1.getName())) {
                  return;
               }

               player.setHealth(20.0D);
            }
         }
      }

   }
}
