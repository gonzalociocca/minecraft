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
            playerinteractevent.getItem().setDurability((short)0);
         }
      } catch (Exception exception) {

      }

   }

   @EventHandler
   public void noWeaponBreakDamage(EntityDamageByEntityEvent entitydamagebyentityevent) {
      String s = FileManager.getManager().getEnchantments().getString("animal_tamer");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         Player player;
         if(entitydamagebyentityevent.getDamager() instanceof Player) {
            player = (Player)entitydamagebyentityevent.getDamager();
            if(player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
               ((Player)entitydamagebyentityevent.getDamager()).getItemInHand().setDurability((short)0);
            }
         }

         if(entitydamagebyentityevent.getEntity() instanceof Player) {
            player = (Player)entitydamagebyentityevent.getEntity();
            ItemStack itemstack;
            if(player.getInventory().getHelmet() != null) {
               itemstack = player.getInventory().getHelmet();
               if(itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  itemstack.setDurability((short)0);
                  player.getInventory().setHelmet(itemstack);
               }
            }

            if(player.getInventory().getChestplate() != null) {
               itemstack = player.getInventory().getChestplate();
               if(itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  itemstack.setDurability((short)0);
                  player.getInventory().setChestplate(itemstack);
               }
            }

            if(player.getInventory().getLeggings() != null) {
               itemstack = player.getInventory().getLeggings();
               if(itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  itemstack.setDurability((short)0);
                  player.getInventory().setLeggings(itemstack);
               }
            }

            if(player.getInventory().getBoots() != null) {
               itemstack = player.getInventory().getBoots();
               if(itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
                  itemstack.setDurability((short)0);
                  player.getInventory().setBoots(itemstack);
               }
            }
         }
      } catch (Exception exception) {

      }

   }


   @EventHandler
   public void noWeaponBreakDamage(EntityShootBowEvent entityshootbowevent) {
      String s = FileManager.getManager().getEnchantments().getString("animal_tamer");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         if(entityshootbowevent.getEntity() instanceof Player && entityshootbowevent.getBow().getItemMeta().hasLore() && entityshootbowevent.getBow().getItemMeta().getLore().contains(s + " I")) {
            entityshootbowevent.getBow().setDurability((short)0);
         }
      } catch (Exception exception) {

      }

   }

   @EventHandler
   public void noWeaponBreakDamage(PlayerItemBreakEvent playeritembreakevent) {
      String s = FileManager.getManager().getEnchantments().getString("animal_tamer");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         ItemStack itemstack = playeritembreakevent.getBrokenItem().clone();
         if(itemstack.getItemMeta().hasLore() && itemstack.getItemMeta().getLore().contains(s + " I")) {
            itemstack.setDurability((short)0);
         }

         playeritembreakevent.getPlayer().getInventory().addItem(new ItemStack[]{itemstack});
      } catch (Exception exception) {

      }
   }
}
