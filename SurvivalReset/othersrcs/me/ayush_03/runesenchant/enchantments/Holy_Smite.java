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

public class Holy_Smite implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("holy_smite");
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
            PotionEffect potioneffect;
            Iterator iterator;
            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("holy_smite.level_I.chance")) {
               player.getWorld().strikeLightningEffect(player.getLocation());
               iterator = player.getActivePotionEffects().iterator();

               while(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player.removePotionEffect(potioneffect.getType());
               }
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("holy_smite.level_II.chance")) {
               player.getWorld().strikeLightningEffect(player.getLocation());
               iterator = player.getActivePotionEffects().iterator();

               while(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player.removePotionEffect(potioneffect.getType());
               }
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("holy_smite.level_III.chance")) {
               player.getWorld().strikeLightningEffect(player.getLocation());
               iterator = player.getActivePotionEffects().iterator();

               while(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player.removePotionEffect(potioneffect.getType());
               }
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("holy_smite.level_IV.chance")) {
               player.getWorld().strikeLightningEffect(player.getLocation());
               iterator = player.getActivePotionEffects().iterator();

               while(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player.removePotionEffect(potioneffect.getType());
               }
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("holy_smite.level_V.chance")) {
               player.getWorld().strikeLightningEffect(player.getLocation());
               iterator = player.getActivePotionEffects().iterator();

               while(iterator.hasNext()) {
                  potioneffect = (PotionEffect)iterator.next();
                  player.removePotionEffect(potioneffect.getType());
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
