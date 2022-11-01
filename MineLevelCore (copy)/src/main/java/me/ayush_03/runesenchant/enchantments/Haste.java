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

public class Haste implements Listener {

   HashSet<String> players = new HashSet();

   @EventHandler
   public void onHaste(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("haste");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getItemInHand();
            if (players.contains(p.getName())) {
               if (p.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                  p.removePotionEffect(PotionEffectType.FAST_DIGGING);
               }
               players.remove(p.getName());
            }
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               continue;
            }

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               players.add(p.getName());
               p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 9999, 2));
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               players.add(p.getName());
               p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 9999, 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
               players.add(p.getName());
               p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 9999, 0));
            }
         }
      }
   }
}
