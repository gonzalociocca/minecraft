package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Kill_Confirm implements Listener {

   @EventHandler
   public void onDeath(PlayerDeathEvent playerdeathevent) {
      Player player = playerdeathevent.getEntity();
      String s = FileManager.getManager().getEnchantments().getString("kill_confirm");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         DamageCause damagecause = playerdeathevent.getEntity().getLastDamageCause().getCause();
         if(damagecause.equals(DamageCause.ENTITY_ATTACK) && playerdeathevent.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entitydamagebyentityevent = (EntityDamageByEntityEvent)playerdeathevent.getEntity().getLastDamageCause();
            Entity entity = entitydamagebyentityevent.getDamager();
            if(entity instanceof Player) {
               Player player1 = (Player)entity;
               int i = (int)(Math.random() * 100.0D);
               ItemStack itemstack;
               SkullMeta skullmeta;
               if(player1.getItemInHand().getItemMeta().getLore().contains(s + " I") && i < Main.getConfiguration().getInt("kill_confirm.level_I.chance")) {
                  itemstack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
                  skullmeta = (SkullMeta)itemstack.getItemMeta();
                  skullmeta.setOwner(player.getName());
                  skullmeta.setDisplayName("§aSkull of " + player.getName());
                  itemstack.setItemMeta(skullmeta);
                  player1.getWorld().dropItem(player.getLocation(), itemstack);
               }

               if(player1.getItemInHand().getItemMeta().getLore().contains(s + " II") && i < Main.getConfiguration().getInt("kill_confirm.level_II.chance")) {
                  itemstack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
                  skullmeta = (SkullMeta)itemstack.getItemMeta();
                  skullmeta.setOwner(player.getName());
                  skullmeta.setDisplayName("§aSkull of " + player.getName());
                  itemstack.setItemMeta(skullmeta);
                  player1.getWorld().dropItem(player.getLocation(), itemstack);
               }

               if(player1.getItemInHand().getItemMeta().getLore().contains(s + " III") && i < Main.getConfiguration().getInt("kill_confirm.level_III.chance")) {
                  itemstack = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
                  skullmeta = (SkullMeta)itemstack.getItemMeta();
                  skullmeta.setOwner(player.getName());
                  skullmeta.setDisplayName("§aSkull of " + player.getName());
                  itemstack.setItemMeta(skullmeta);
                  player1.getWorld().dropItem(player.getLocation(), itemstack);
               }
            }
         }
      } catch (Exception exception) {
         ;
      }

   }
}
