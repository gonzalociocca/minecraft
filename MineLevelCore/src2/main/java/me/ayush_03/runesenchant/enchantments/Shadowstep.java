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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Shadowstep implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      if(entitydamagebyentityevent.getEntity() instanceof Player && entitydamagebyentityevent.getDamager() instanceof Player) {
         String s = FileManager.getManager().getEnchantments().getString("shadowstep");
         s = ChatColor.translateAlternateColorCodes('&', s);

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
            ItemStack[] aitemstack = player.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int j = aitemstack.length;

            for(int k = 0; k < j; ++k) {
               ItemStack itemstack = aitemstack1[k];
               if(itemstack != null) {
                  int l;
                  Vector vector;
                  Location location;
                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("shadowstep.level_I.chance")) {
                     l = Main.getConfiguration().getInt("shadowstep.level_I.distance");
                     vector = player1.getLocation().getDirection();
                     vector = vector.multiply((double)l * -1.0D);
                     location = player1.getLocation().add(vector);
                     player.teleport(location);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("shadowstep.level_II.chance")) {
                     l = Main.getConfiguration().getInt("shadowstep.level_II.distance");
                     vector = player1.getLocation().getDirection();
                     vector = vector.multiply((double)l * -1.0D);
                     location = player1.getLocation().add(vector);
                     player.teleport(location);
                  }

                  if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("shadowstep.level_III.chance")) {
                     l = Main.getConfiguration().getInt("shadowstep.level_III.distance");
                     vector = player1.getLocation().getDirection();
                     vector = vector.multiply((double)l * -1.0D);
                     location = player1.getLocation().add(vector);
                     player.teleport(location);
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
