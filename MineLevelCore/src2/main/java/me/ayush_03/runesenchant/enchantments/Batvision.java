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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class Batvision implements Listener {
   HashSet<String> players = new HashSet();

   @EventHandler
   public void onBatVision(UpdateEvent event) {
      if(event.getType() ==UpdateType.SEC3){
         String s = FileManager.getManager().getEnchantments().getString("batvision");
         s = ChatColor.translateAlternateColorCodes('&', s);

         for(Player p : Bukkit.getOnlinePlayers()){
             if(p.getInventory().getHelmet() == null || !p.getInventory().getHelmet().hasItemMeta() || !p.getInventory().getHelmet().getItemMeta().hasLore() || !p.getInventory().getHelmet().getItemMeta().getLore().contains(s+" I")){
              if(players.contains(p.getName())){
                 players.remove(p.getName());
                 p.removePotionEffect(PotionEffectType.NIGHT_VISION);
              }else{
               continue;}
            }else{
                if(!players.contains(p.getName())){players.add(p.getName());}
               if(!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                  p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 9999, 0));
               }
            }
         }
      }
      }

   }

