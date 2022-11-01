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

public class Crushing implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("crushing");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            Player player1 = (Player)entitydamagebyentityevent.getEntity();
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player1.getWorld()).getApplicableRegions(player1.getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            int i = (int)(Math.random() * 100.0D);
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("crushing.level_I.chance")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("crushing.level_II.chance")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("crushing.level_III.chance")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("crushing.level_IV.chance")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("crushing.level_V.chance")) {
               entitydamagebyentityevent.setDamage(entitydamagebyentityevent.getDamage() * 2.0D);
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
