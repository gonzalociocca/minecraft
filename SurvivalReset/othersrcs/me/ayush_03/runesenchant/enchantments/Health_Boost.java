package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.updater.UpdateType;
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

public class Health_Boost implements Listener {

   HashSet<String> players = new HashSet();

   @EventHandler
   public void onHealthBoost(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("health_boost");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getInventory().getChestplate();
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               if(players.contains(p.getName())){
                  p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
                  players.remove(p.getName());
               }
               continue;
            }

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST))
               p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999, Main.getConfiguration().getInt("health_boost.level_III.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST))
               p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999, Main.getConfiguration().getInt("health_boost.level_II.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST))
               p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 9999, Main.getConfiguration().getInt("health_boost.level_I.potion_lvl") - 1));
            }
         }
      }
   }
   }
