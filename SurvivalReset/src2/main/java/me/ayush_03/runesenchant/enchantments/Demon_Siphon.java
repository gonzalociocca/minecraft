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

public class Demon_Siphon implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("demon_siphon");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player) {
         try {
            Player player = (Player)entitydamagebyentityevent.getDamager();
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(entitydamagebyentityevent.getEntity().getWorld()).getApplicableRegions(entitydamagebyentityevent.getEntity().getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            if(entitydamagebyentityevent.getEntity() instanceof Player) {
               return;
            }

            int i = (int)(Math.random() * 100.0D);
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("demon_siphon.level_I.chance")) {
               if(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_I.health") > 20.0D) {
                  player.setHealth(20.0D);
               } else {
                  player.setHealth(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_I.health"));
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("demon_siphon.level_II.chance")) {
               if(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_II.health") > 20.0D) {
                  player.setHealth(20.0D);
               } else {
                  player.setHealth(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_II.health"));
               }
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("demon_siphon.level_III.chance")) {
               if(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_III.health") > 20.0D) {
                  player.setHealth(20.0D);
               } else {
                  player.setHealth(player.getHealth() + (double)Main.getConfiguration().getInt("demon_siphon.level_III.health"));
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
