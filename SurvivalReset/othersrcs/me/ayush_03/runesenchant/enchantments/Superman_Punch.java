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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Superman_Punch implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamaged(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("superman_punch");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getEntity() instanceof Player) {
         try {
            Entity entity = entitydamagebyentityevent.getDamager();
            Player player = (Player)entitydamagebyentityevent.getEntity();
            if(entitydamagebyentityevent.isCancelled()) {
               return;
            }

            if(Main.getConfiguration().getBoolean("allow-worldguard")) {
               ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
               if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
                  return;
               }
            }

            ItemStack[] aitemstack = player.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for(int j = 0; j < i; ++j) {
               ItemStack itemstack = aitemstack1[j];
               if(itemstack != null) {
                  int k;
                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                     k = (int)(Math.random() * 100.0D);
                     if(k < Main.getConfiguration().getInt("superman_punch.level_I.chance")) {
                        entity.setVelocity(player.getLocation().getDirection().multiply(Main.getConfiguration().getInt("superman_punch.level_I.power")));
                     }
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II")) {
                     k = (int)(Math.random() * 100.0D);
                     if(k < Main.getConfiguration().getInt("superman_punch.level_II.chance")) {
                        entity.setVelocity(player.getLocation().getDirection().multiply(Main.getConfiguration().getInt("superman_punch.level_II.power")));
                     }
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III")) {
                     k = (int)(Math.random() * 100.0D);
                     if(k < Main.getConfiguration().getInt("superman_punch.level_III.chance")) {
                        entity.setVelocity(player.getLocation().getDirection().multiply(Main.getConfiguration().getInt("superman_punch.level_III.power")));
                     }
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
