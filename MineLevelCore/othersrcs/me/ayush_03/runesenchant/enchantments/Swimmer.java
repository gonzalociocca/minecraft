package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class Swimmer implements Listener {

   HashSet<String> players = new HashSet();

   @EventHandler
   public void onSwimming(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("swimmer");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getInventory().getHelmet();
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               if(players.contains(p.getName())){
                  players.remove(p.getName());
                  p.removePotionEffect(PotionEffectType.WATER_BREATHING);}
               continue;}

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 9999, Main.getConfiguration().getInt("swimmer.level_III.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 9999, Main.getConfiguration().getInt("swimmer.level_II.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.WATER_BREATHING))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 9999, Main.getConfiguration().getInt("swimmer.level_I.potion_lvl") - 1));
            }
         }
      }
   }
}
