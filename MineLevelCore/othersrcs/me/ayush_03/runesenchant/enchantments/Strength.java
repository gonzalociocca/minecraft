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

public class Strength implements Listener {


   HashSet<String> players = new HashSet();

   @EventHandler
   public void onStrengthBoost(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("strength");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getInventory().getChestplate();
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               if(players.contains(p.getName())){
                  players.remove(p.getName());
                  p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);}
               continue;}

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, Main.getConfiguration().getInt("strength.level_III.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, Main.getConfiguration().getInt("strength.level_II.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, Main.getConfiguration().getInt("strength.level_I.potion_lvl") - 1));
            }
         }
      }
   }
}
