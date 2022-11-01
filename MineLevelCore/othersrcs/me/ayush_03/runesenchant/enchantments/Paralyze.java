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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Paralyze implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("paralyze");
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
            ItemStack[] aitemstack = player.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int j = aitemstack.length;

            for(int k = 0; k < j; ++k) {
               ItemStack itemstack = aitemstack1[k];
               if(itemstack != null) {
                  if(itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("paralyze.level_I.chance")) {
                     player1.addPotionEffect(new PotionEffect(PotionEffectType.getById(4), Main.getConfiguration().getInt("paralyze.level_I.duration") * 20, Main.getConfiguration().getInt("paralyze.level_I.potion_lvl") - 1));
                  }

                  if(itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("paralyze.level_II.chance")) {
                     player1.addPotionEffect(new PotionEffect(PotionEffectType.getById(4), Main.getConfiguration().getInt("paralyze.level_II.duration") * 20, Main.getConfiguration().getInt("paralyze.level_II.potion_lvl") - 1));
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
