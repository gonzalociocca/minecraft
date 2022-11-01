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

public class Blessed implements Listener {

   HashSet<String> players = new HashSet();

   @EventHandler
   public void onBlessed(UpdateEvent event) {
      if(event.getType() == UpdateType.FAST){
         int i = (int)(Math.random() * 100.0D);
         String s = FileManager.getManager().getEnchantments().getString("blessed");
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
               if(itemstack.getItemMeta().getLore().contains(s+" III") && i < (Main.getConfiguration().getInt("blessed.level_III.chance"))
                       || itemstack.getItemMeta().getLore().contains(s + " II") && i < (Main.getConfiguration().getInt("blessed.level_II.chance"))
                       || itemstack.getItemMeta().getLore().contains(s + " I") && i < (Main.getConfiguration().getInt("blessed.level_I.chance")) ) {
                     p.setHealth(p.getMaxHealth());
                  p.setFoodLevel(20);
               }}
            }
         }
      }
      else if(event.getType() == UpdateType.SEC3){
         String s = FileManager.getManager().getEnchantments().getString("blessed");
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
               }if(itemstack.getItemMeta().getLore().contains(s+" I") || itemstack.getItemMeta().getLore().contains(s+" II") || itemstack.getItemMeta().getLore().contains(s+" III")){
                  if(!players.contains(p.getName())){
                  players.add(p.getName());
                     k = j-1;
               }}
            }
         }
      }
   }
}
