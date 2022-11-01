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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Stealth implements Listener {

   @EventHandler
   public void onSneak(PlayerToggleSneakEvent playertogglesneakevent) {
      Player player = playertogglesneakevent.getPlayer();
      String s = FileManager.getManager().getEnchantments().getString("stealth");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(Main.getConfiguration().getBoolean("allow-worldguard")) {
         ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
         if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
            return;
         }
      }

      int i = (int)(Math.random() * 100.0D);
      ItemStack[] aitemstack = player.getInventory().getArmorContents();

      try {
         ItemStack[] aitemstack1 = aitemstack;
         int j = aitemstack.length;

         for(int k = 0; k < j; ++k) {
            ItemStack itemstack = aitemstack1[k];
            if(itemstack != null) {
               int l;
               Entity entity;
               Iterator iterator;
               Player player1;
               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("stealth.level_I.chance")) {
                  l = Main.getConfiguration().getInt("stealth.level_I.radius");
                  iterator = player.getNearbyEntities((double)l, (double)l, (double)l).iterator();

                  while(iterator.hasNext()) {
                     entity = (Entity)iterator.next();
                     if(entity instanceof Player) {
                        player1 = (Player)entity;
                        player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getConfiguration().getInt("stealth.level_I.duration") * 20, 0));
                     }
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("stealth.level_II.chance")) {
                  l = Main.getConfiguration().getInt("stealth.level_II.radius");
                  iterator = player.getNearbyEntities((double)l, (double)l, (double)l).iterator();

                  while(iterator.hasNext()) {
                     entity = (Entity)iterator.next();
                     if(entity instanceof Player) {
                        player1 = (Player)entity;
                        player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getConfiguration().getInt("stealth.level_II.duration") * 20, 0));
                     }
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("stealth.level_III.chance")) {
                  l = Main.getConfiguration().getInt("stealth.level_III.radius");
                  iterator = player.getNearbyEntities((double)l, (double)l, (double)l).iterator();

                  while(iterator.hasNext()) {
                     entity = (Entity)iterator.next();
                     if(entity instanceof Player) {
                        player1 = (Player)entity;
                        player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getConfiguration().getInt("stealth.level_III.duration") * 20, 0));
                     }
                  }
               }

               if(itemstack.hasItemMeta() && itemstack.getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("stealth.level_IV.chance")) {
                  l = Main.getConfiguration().getInt("stealth.level_IV.radius");
                  iterator = player.getNearbyEntities((double)l, (double)l, (double)l).iterator();

                  while(iterator.hasNext()) {
                     entity = (Entity)iterator.next();
                     if(entity instanceof Player) {
                        player1 = (Player)entity;
                        player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getConfiguration().getInt("stealth.level_IV.duration") * 20, 0));
                     }
                  }
               }
            }
         }
      } catch (Exception exception) {
         ;
      }

   }
}
