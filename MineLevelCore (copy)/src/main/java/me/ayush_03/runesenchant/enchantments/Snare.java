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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Snare implements Listener {

   private ArrayList special = new ArrayList();
   private ArrayList special2 = new ArrayList();
   private ArrayList special3 = new ArrayList();
   private ArrayList special4 = new ArrayList();

   @EventHandler
   public void onProjectileLaunch(ProjectileLaunchEvent projectilelaunchevent) {
      String s = FileManager.getManager().getEnchantments().getString("snare");
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
      if(entitydamagebyentityevent.getEntity() instanceof LivingEntity) {
         LivingEntity livingentity = (LivingEntity)entitydamagebyentityevent.getEntity();
         if(entitydamagebyentityevent.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile)entitydamagebyentityevent.getDamager();
            if(projectile.getShooter() instanceof Player) {
               Player player = (Player)projectile.getShooter();
               if(entitydamagebyentityevent.isCancelled()) {
                  return;
               }

               if(Main.getConfiguration().getBoolean("allow-worldguard")) {
                  ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(livingentity.getWorld()).getApplicableRegions(livingentity.getLocation());
                  if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                     return;
                  }
               }

               if(this.special.contains(projectile)) {
                  livingentity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getConfiguration().getInt("snare.level_I.duration") * 20, Main.getConfiguration().getInt("snare.level_I.potion_lvl") - 1));
               }

               if(this.special2.contains(projectile)) {
                  livingentity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getConfiguration().getInt("snare.level_II.duration") * 20, Main.getConfiguration().getInt("snare.level_II.potion_lvl") - 1));
               }

               if(this.special3.contains(projectile)) {
                  livingentity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getConfiguration().getInt("snare.level_III.duration") * 20, Main.getConfiguration().getInt("snare.level_III.potion_lvl") - 1));
               }

               if(this.special4.contains(projectile)) {
                  livingentity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getConfiguration().getInt("snare.level_IV.duration") * 20, Main.getConfiguration().getInt("snare.level_IV.potion_lvl") - 1));
               }
            }
         }
      }

   }
}
