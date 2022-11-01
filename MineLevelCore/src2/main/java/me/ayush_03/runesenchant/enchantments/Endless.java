package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Endless implements Listener {

   @EventHandler(
      ignoreCancelled = true
   )
   public void noWeaponBreakDamage(PlayerInteractEvent playerinteractevent) {
      String s = FileManager.getManager().getEnchantments().getString("endless");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         if(playerinteractevent.getItem().getItemMeta().hasLore() && playerinteractevent.getItem().getItemMeta().getLore().contains(s + " I")) {
            playerinteractevent.getItem().setDurability((short)1);
         }
      } catch (Exception exception) {

      }

   }


}
