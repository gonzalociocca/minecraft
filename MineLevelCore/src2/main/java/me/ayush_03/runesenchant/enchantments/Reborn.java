package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Reborn implements Listener {

   @EventHandler
   public void onDeath(PlayerDeathEvent playerdeathevent) {
      String s = FileManager.getManager().getEnchantments().getString("reborn");
      s = ChatColor.translateAlternateColorCodes('&', s);
      Player player = playerdeathevent.getEntity();
      if(player.getKiller() instanceof Player) {
         Player player1 = player.getKiller();

         try {
            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
               player1.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Main.getConfiguration().getInt("reborn.level_I.absorption.duration") * 20, Main.getConfiguration().getInt("reborn.level_I.absorption.potion_lvl") - 1));
               player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Main.getConfiguration().getInt("reborn.level_I.regeneration.duration") * 20, Main.getConfiguration().getInt("reborn.level_I.regeneration.potion_lvl") - 1));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " II")) {
               player1.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Main.getConfiguration().getInt("reborn.level_II.absorption.duration") * 20, Main.getConfiguration().getInt("reborn.level_II.absorption.potion_lvl") - 1));
               player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Main.getConfiguration().getInt("reborn.level_II.regeneration.duration") * 20, Main.getConfiguration().getInt("reborn.level_II.regeneration.potion_lvl") - 1));
            }

            if(player1.getItemInHand().getItemMeta().getLore().contains(s + " III")) {
               player1.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Main.getConfiguration().getInt("reborn.level_III.absorption.duration") * 20, Main.getConfiguration().getInt("reborn.level_III.absorption.potion_lvl") - 1));
               player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Main.getConfiguration().getInt("reborn.level_III.regeneration.duration") * 20, Main.getConfiguration().getInt("reborn.level_III.regeneration.potion_lvl") - 1));
            }
         } catch (Exception exception) {
            ;
         }
      }

   }
}
