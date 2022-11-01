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

public class Zeus implements Listener {

   private ArrayList special = new ArrayList();
   private ArrayList special2 = new ArrayList();
   private ArrayList special3 = new ArrayList();

   @EventHandler
   public void onDamage(ProjectileLaunchEvent projectilelaunchevent) {
      String s = FileManager.getManager().getEnchantments().getString("zeus");
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
      if(entitydamagebyentityevent.getDamager() instanceof Projectile) {
         Projectile projectile = (Projectile)entitydamagebyentityevent.getDamager();
         if(entitydamagebyentityevent.isCancelled()) {
            return;
         }

         if(Main.getConfiguration().getBoolean("allow-worldguard")) {
            ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(entitydamagebyentityevent.getEntity().getWorld()).getApplicableRegions(entitydamagebyentityevent.getEntity().getLocation());
            if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
               return;
            }
         }

         if(this.special.contains(projectile) && i < Main.getConfiguration().getInt("zeus.level_I.chance")) {
            entitydamagebyentityevent.getEntity().getWorld().strikeLightningEffect(entitydamagebyentityevent.getEntity().getLocation());
         }

         if(this.special2.contains(projectile) && i < Main.getConfiguration().getInt("zeus.level_II.chance")) {
            entitydamagebyentityevent.getEntity().getWorld().strikeLightningEffect(entitydamagebyentityevent.getEntity().getLocation());
         }

         if(this.special2.contains(projectile) && i < Main.getConfiguration().getInt("zeus.level_III.chance")) {
            entitydamagebyentityevent.getEntity().getWorld().strikeLightningEffect(entitydamagebyentityevent.getEntity().getLocation());
         }
      }

   }
}
