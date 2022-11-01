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
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class Eyepatch implements Listener {


   HashSet<String> players = new HashSet();

   @EventHandler
   public void onEyepatch(UpdateEvent event) {
      if(event.getType() == UpdateType.FAST){
         String s = FileManager.getManager().getEnchantments().getString("eyepatch");
         s = ChatColor.translateAlternateColorCodes('&', s);
         for(String ps : this.players){
            if(Bukkit.getPlayer(ps) != null){
               Player p = Bukkit.getPlayer(ps);
               ItemStack[] aitemstack = p.getInventory().getArmorContents();
               int j = aitemstack.length;
               ItemStack itemstack;
               int k;
               for(k = 0; k < j; ++k) {
                  itemstack = aitemstack[k];
                  if(itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()){
                     continue;
                  }
                  if(itemstack.getItemMeta().getLore().contains(s+" I")) {
                     if(p.hasPotionEffect(PotionEffectType.BLINDNESS)){
                        p.removePotionEffect(PotionEffectType.BLINDNESS);}
                  }
               }}
            }
         }
      else if(event.getType() == UpdateType.SEC3){
         String s = FileManager.getManager().getEnchantments().getString("eyepatch");
         s = ChatColor.translateAlternateColorCodes('&', s);
         players.clear();

         for(Player p : Bukkit.getOnlinePlayers()){
            ItemStack[] aitemstack = p.getInventory().getArmorContents();
            int j = aitemstack.length;
            ItemStack itemstack;
            int k;
            for(k = 0; k < j; ++k) {
               itemstack = aitemstack[k];
               if(itemstack == null || !itemstack.hasItemMeta() || !itemstack.getItemMeta().hasLore()){
                  continue;
               }
               if(itemstack.getItemMeta().getLore().contains(s+" I")){
                  if(!players.contains(p.getName())){
                     players.add(p.getName());
                     k = j-1;
                  }}
            }
         }
      }
   }
}
