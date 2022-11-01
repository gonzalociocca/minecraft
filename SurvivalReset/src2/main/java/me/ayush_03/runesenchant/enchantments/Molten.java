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

public class Molten implements Listener {

   HashSet<String> players = new HashSet();

   @EventHandler
   public void onMolten(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("molten");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getInventory().getChestplate();
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               if(players.contains(p.getName())){
                  players.remove(p.getName());
                  p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);}
               continue;}

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999, Main.getConfiguration().getInt("molten.level_III.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999, Main.getConfiguration().getInt("molten.level_II.potion_lvl") - 1));
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
                  p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999, Main.getConfiguration().getInt("molten.level_I.potion_lvl") - 1));
            }
         }
      }
   }
}
