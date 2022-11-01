package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Suicide implements Listener {

   public void onDeath(PlayerDeathEvent playerdeathevent) {
      Player player = playerdeathevent.getEntity();
      String s = FileManager.getManager().getEnchantments().getString("suicide");
      s = ChatColor.translateAlternateColorCodes('&', s);
      int i = (int)(Math.random() * 100.0D);
      if(player.getInventory().getChestplate() != null && player.getInventory().getChestplate().hasItemMeta() && player.getInventory().getChestplate().getItemMeta().hasLore() && player.getInventory().getChestplate().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("suicide.level_I.chance")) {
         player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), (float)Main.getConfiguration().getInt("suicide.level_I.power"), false, false);
      }

   }
}
