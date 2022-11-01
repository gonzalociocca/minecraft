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

public class Demonic_Aura implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("demonic_aura");
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

            ItemStack[] aitemstack = player1.getInventory().getArmorContents();
            ItemStack[] aitemstack1 = aitemstack;
            int i = aitemstack.length;

            ItemStack itemstack;
            int j;
            int k;
            for(j = 0; j < i; ++j) {
               itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  k = (int)(Math.random() * 100.0D);
                  if(k < Main.getConfiguration().getInt("demonic_aura.level_I.chance")) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Main.getConfiguration().getInt("demonic_aura.level_I.duration") * 20, 0));
                  }
               }
            }

            aitemstack1 = aitemstack;
            i = aitemstack.length;

            for(j = 0; j < i; ++j) {
               itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II")) {
                  k = (int)(Math.random() * 100.0D);
                  if(k < Main.getConfiguration().getInt("demonic_aura.level_II.chance")) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Main.getConfiguration().getInt("demonic_aura.level_II.duration") * 20, 0));
                  }
               }
            }

            aitemstack1 = aitemstack;
            i = aitemstack.length;

            for(j = 0; j < i; ++j) {
               itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III")) {
                  k = (int)(Math.random() * 100.0D);
                  if(k < Main.getConfiguration().getInt("demonic_aura.level_III.chance")) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Main.getConfiguration().getInt("demonic_aura.level_III.duration") * 20, 0));
                  }
               }
            }

            aitemstack1 = aitemstack;
            i = aitemstack.length;

            for(j = 0; j < i; ++j) {
               itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " IV")) {
                  k = (int)(Math.random() * 100.0D);
                  if(k < Main.getConfiguration().getInt("demonic_aura.level_IV.chance")) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Main.getConfiguration().getInt("demonic_aura.level_IV.duration") * 20, 0));
                  }
               }
            }

            aitemstack1 = aitemstack;
            i = aitemstack.length;

            for(j = 0; j < i; ++j) {
               itemstack = aitemstack1[j];
               if(itemstack != null && itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " V")) {
                  k = (int)(Math.random() * 100.0D);
                  if(k < Main.getConfiguration().getInt("demonic_aura.level_V.chance")) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Main.getConfiguration().getInt("demonic_aura.level_V.duration") * 20, 0));
                  }
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
