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

public class Thief implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      /*
      String s = FileManager.getManager().getEnchantments().getString("thief");
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
            int j;
            double d0;
            double d1;
            if(player.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("thief.level_I.chance")) {
               j = Main.getConfiguration().getInt("thief.level_I.money-percent");
               d0 = (double)j / 100.0D;
               d1 = d0 * Main.econ.getBalance(player1);
               Main.econ.withdrawPlayer(player1, d1);
               Main.econ.depositPlayer(player, d1);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("thief.level_II.chance")) {
               j = Main.getConfiguration().getInt("thief.level_II.money-percent");
               d0 = (double)j / 100.0D;
               d1 = d0 * Main.econ.getBalance(player1);
               Main.econ.withdrawPlayer(player1, d1);
               Main.econ.depositPlayer(player, d1);
            }

            if(player.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("thief.level_III.chance")) {
               j = Main.getConfiguration().getInt("thief.level_III.money-percent");
               d0 = (double)j / 100.0D;
               d1 = d0 * Main.econ.getBalance(player1);
               Main.econ.withdrawPlayer(player1, d1);
               Main.econ.depositPlayer(player, d1);
            }
         } catch (Exception exception) {
            ;
         }
      }
*/
   }
}
