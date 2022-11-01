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
import org.bukkit.inventory.ItemStack;

public class Spiked implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("spiked");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entitydamagebyentityevent.getDamager() instanceof Player && entitydamagebyentityevent.getEntity() instanceof Player) {
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

            Player player1 = (Player)entitydamagebyentityevent.getEntity();
            ItemStack[] aitemstack = player1.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            for(int j = 0; j < i; ++j) {
               ItemStack itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  player.damage(2.0D);
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II")) {
                  player.damage(4.0D);
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
