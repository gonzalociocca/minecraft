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

public class Platemail implements Listener {
   HashSet<String> players = new HashSet();

   @EventHandler
   public void onPlateMail(UpdateEvent event) {

      if (event.getType() == UpdateType.SEC3) {
         String s = FileManager.getManager().getEnchantments().getString("platemail");
         s = ChatColor.translateAlternateColorCodes('&', s);
         String s1 = FileManager.getManager().getEnchantments().getString("reinforced");
         s1 = ChatColor.translateAlternateColorCodes('&', s1);

         for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack itemstack = p.getInventory().getChestplate();
            if (itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()) {
               if(players.contains(p.getName())){
                  players.remove(p.getName());
                  p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);}
               continue;}

            if (itemstack.getItemMeta().getLore().contains(s + " III")) {
               if(!itemstack.getItemMeta().getLore().contains(s1 + " I") && !itemstack.getItemMeta().getLore().contains(s1 + " II") && !itemstack.getItemMeta().getLore().contains(s1 + " III") && !itemstack.getItemMeta().getLore().contains(s1 + " IV") && !itemstack.getItemMeta().getLore().contains(s1 + " V")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                  p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, 2));}}
            } else if (itemstack.getItemMeta().getLore().contains(s + " II")) {
               if(!itemstack.getItemMeta().getLore().contains(s1 + " I") && !itemstack.getItemMeta().getLore().contains(s1 + " II") && !itemstack.getItemMeta().getLore().contains(s1 + " III") && !itemstack.getItemMeta().getLore().contains(s1 + " IV") && !itemstack.getItemMeta().getLore().contains(s1 + " V")) {
                  if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                  p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999, 1));}}
            } else if (itemstack.getItemMeta().getLore().contains(s + " I")) {
                  if(!itemstack.getItemMeta().getLore().contains(s1 + " I") && !itemstack.getItemMeta().getLore().contains(s1 + " II") && !itemstack.getItemMeta().getLore().contains(s1 + " III") && !itemstack.getItemMeta().getLore().contains(s1 + " IV") && !itemstack.getItemMeta().getLore().contains(s1 + " V")) {
               if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)){
                  p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999,0));}}
            }
         }
      }
   }
}
