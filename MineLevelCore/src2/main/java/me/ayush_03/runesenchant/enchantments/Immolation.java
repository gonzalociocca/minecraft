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

public class Immolation implements Listener {

   @EventHandler
   public void onSneak(PlayerToggleSneakEvent playertogglesneakevent) {
      String s = FileManager.getManager().getEnchantments().getString("immolation");
      s = ChatColor.translateAlternateColorCodes('&', s);
      Player player = playertogglesneakevent.getPlayer();
      int i = (int)(Math.random() * 100.0D);
      if(Main.getConfiguration().getBoolean("allow-worldguard")) {
         ApplicableRegionSet applicableregionset = WGBukkit.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
         if(applicableregionset.queryState((RegionAssociable)null, new StateFlag[]{DefaultFlag.PVP}) == State.DENY) {
            return;
         }
      }

      try {
         Iterator iterator;
         Entity entity;
         if(player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("immolation.level_I.chance")) {
            iterator = player.getNearbyEntities(Main.getConfiguration().getDouble("immolation.level_I.radius"), Main.getConfiguration().getDouble("immolation.level_I.radius"), Main.getConfiguration().getDouble("immolation.level_I.radius")).iterator();

            while(iterator.hasNext()) {
               entity = (Entity)iterator.next();
               if(entity instanceof Player) {
                  Player player1 = (Player)entity;
                  player1.setFireTicks(Main.getConfiguration().getInt("immolation.level_I.duration") * 20);
               }
            }
         }

         if(player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("immolation.level_II.chance")) {
            iterator = player.getNearbyEntities(Main.getConfiguration().getDouble("immolation.level_II.radius"), Main.getConfiguration().getDouble("immolation.level_II.radius"), Main.getConfiguration().getDouble("immolation.level_II.radius")).iterator();

            while(iterator.hasNext()) {
               entity = (Entity)iterator.next();
               if(entity instanceof Player) {
                  ((Player)entity).setFireTicks(Main.getConfiguration().getInt("immolation.level_II.duration") * 20);
               }
            }
         }

         if(player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("immolation.level_III.chance")) {
            iterator = player.getNearbyEntities(Main.getConfiguration().getDouble("immolation.level_III.radius"), Main.getConfiguration().getDouble("immolation.level_III.radius"), Main.getConfiguration().getDouble("immolation.level_III.radius")).iterator();

            while(iterator.hasNext()) {
               entity = (Entity)iterator.next();
               if(entity instanceof Player) {
                  ((Player)entity).setFireTicks(Main.getConfiguration().getInt("immolation.level_III.duration") * 20);
               }
            }
         }

         if(player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " IV") && i < Main.getConfiguration().getInt("immolation.level_IV.chance")) {
            iterator = player.getNearbyEntities(Main.getConfiguration().getDouble("immolation.level_IV.radius"), Main.getConfiguration().getDouble("immolation.level_IV.radius"), Main.getConfiguration().getDouble("immolation.level_IV.radius")).iterator();

            while(iterator.hasNext()) {
               entity = (Entity)iterator.next();
               if(entity instanceof Player) {
                  ((Player)entity).setFireTicks(Main.getConfiguration().getInt("immolation.level_IV.duration") * 20);
               }
            }
         }

         if(player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " V") && i < Main.getConfiguration().getInt("immolation.level_V.chance")) {
            iterator = player.getNearbyEntities(Main.getConfiguration().getDouble("immolation.level_V.radius"), Main.getConfiguration().getDouble("immolation.level_V.radius"), Main.getConfiguration().getDouble("immolation.level_V.radius")).iterator();

            while(iterator.hasNext()) {
               entity = (Entity)iterator.next();
               if(entity instanceof Player) {
                  ((Player)entity).setFireTicks(Main.getConfiguration().getInt("immolation.level_V.duration") * 20);
               }
            }
         }
      } catch (Exception exception) {
         ;
      }

   }
}
