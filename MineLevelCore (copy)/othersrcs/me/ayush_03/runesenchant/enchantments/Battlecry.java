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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class Battlecry implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      if(entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player) {
         Player player = (Player)entitydamagebyentityevent.getDamager();
         String s = FileManager.getManager().getEnchantments().getString("battlecry");
         s = ChatColor.translateAlternateColorCodes('&', s);
         if(entitydamagebyentityevent.isCancelled()) {
            return;
         }

         if(Main.getConfiguration().getBoolean("allow-worldguard")) {
            ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(entitydamagebyentityevent.getEntity().getWorld()).getApplicableRegions(entitydamagebyentityevent.getEntity().getLocation());
            if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
               return;
            }
         }

         int i = (int)(Math.random() * 100.0D);

         try {
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && (i < Main.getConfiguration().getInt("battlecry.level_I.chance") && player.hasPotionEffect(PotionEffectType.POISON) || player.hasPotionEffect(PotionEffectType.CONFUSION) || player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.WEAKNESS) || player.hasPotionEffect(PotionEffectType.SLOW))) {
               try {
                  player.removePotionEffect(PotionEffectType.BLINDNESS);
                  player.removePotionEffect(PotionEffectType.POISON);
                  player.removePotionEffect(PotionEffectType.CONFUSION);
                  player.removePotionEffect(PotionEffectType.WEAKNESS);
                  player.removePotionEffect(PotionEffectType.SLOW);
               } catch (Exception exception) {
                  ;
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II") && (i < Main.getConfiguration().getInt("battlecry.level_II.chance") && player.hasPotionEffect(PotionEffectType.POISON) || player.hasPotionEffect(PotionEffectType.CONFUSION) || player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.WEAKNESS) || player.hasPotionEffect(PotionEffectType.SLOW))) {
               try {
                  player.removePotionEffect(PotionEffectType.BLINDNESS);
                  player.removePotionEffect(PotionEffectType.POISON);
                  player.removePotionEffect(PotionEffectType.CONFUSION);
                  player.removePotionEffect(PotionEffectType.WEAKNESS);
                  player.removePotionEffect(PotionEffectType.SLOW);
               } catch (Exception exception1) {
                  ;
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III") && (i < Main.getConfiguration().getInt("battlecry.level_III.chance") && player.hasPotionEffect(PotionEffectType.POISON) || player.hasPotionEffect(PotionEffectType.CONFUSION) || player.hasPotionEffect(PotionEffectType.WITHER) || player.hasPotionEffect(PotionEffectType.WEAKNESS) || player.hasPotionEffect(PotionEffectType.SLOW))) {
               try {
                  player.removePotionEffect(PotionEffectType.BLINDNESS);
                  player.removePotionEffect(PotionEffectType.POISON);
                  player.removePotionEffect(PotionEffectType.CONFUSION);
                  player.removePotionEffect(PotionEffectType.WEAKNESS);
                  player.removePotionEffect(PotionEffectType.SLOW);
               } catch (Exception exception2) {
                  ;
               }
            }
         } catch (Exception exception3) {
            ;
         }
      }

   }
}
