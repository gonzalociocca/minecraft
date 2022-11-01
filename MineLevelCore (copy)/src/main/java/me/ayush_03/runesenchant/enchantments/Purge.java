package me.ayush_03.runesenchant.enchantments;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import java.util.Iterator;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;

public class Purge implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("purge");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            Player player1 = (Player)entitydamagebyentityevent.getEntity();
            int i = (int)(Math.random() * 100.0D);
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player1.getWorld()).getApplicableRegions(player1.getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            Iterator iterator;
            PotionEffect potioneffect;
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("purge.level_I.chance")) {
               player1.getWorld().strikeLightningEffect(player1.getLocation());
               iterator = player1.getActivePotionEffects().iterator();

               if(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player1.removePotionEffect(potioneffect.getType());
                  player1.damage(2.0D);
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("purge.level_II.chance")) {
               player1.getWorld().strikeLightningEffect(player1.getLocation());
               iterator = player1.getActivePotionEffects().iterator();

               if(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player1.removePotionEffect(potioneffect.getType());
                  player1.damage(2.0D);
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("purge.level_III.chance")) {
               player1.getWorld().strikeLightningEffect(player1.getLocation());
               iterator = player1.getActivePotionEffects().iterator();

               if(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player1.removePotionEffect(potioneffect.getType());
                  player1.damage(3.0D);
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("purge.level_IV.chance")) {
               player1.getWorld().strikeLightningEffect(player1.getLocation());
               iterator = player1.getActivePotionEffects().iterator();

               if(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player1.removePotionEffect(potioneffect.getType());
                  player1.damage(4.0D);
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
