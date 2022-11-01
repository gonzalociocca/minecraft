package me.ayush_03.runesenchant.enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

public class Shooter implements Listener {

   private HashMap temp = new HashMap();
   private ArrayList cooldown = new ArrayList();

   @EventHandler
   public void onInteract(PlayerInteractEvent playerinteractevent) {
      if(playerinteractevent.getAction() == Action.RIGHT_CLICK_AIR || playerinteractevent.getAction() == Action.RIGHT_CLICK_BLOCK) {
         String s = FileManager.getManager().getEnchantments().getString("shooter");
         s = ChatColor.translateAlternateColorCodes('&', s);
         final Player player = playerinteractevent.getPlayer();
         if(!this.cooldown.contains(player.getUniqueId())) {
            if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore() && player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
               int i;
               if(Main.getConfiguration().getString("shooter.level_I.projectile").equalsIgnoreCase("snowball")) {
                  Snowball snowball = player.throwSnowball();
                  snowball.setVelocity(player.getEyeLocation().getDirection().multiply(4));
                  this.temp.put(snowball, player.getName());
                  if(Main.getConfiguration().getBoolean("shooter.level_I.enable-cooldown")) {
                     i = Main.getConfiguration().getInt("shooter.level_I.cooldown") * 20;
                     this.cooldown.add(player.getUniqueId());
                     (new BukkitRunnable() {
                        public void run() {
                           Shooter.this.cooldown.remove(player.getUniqueId());
                        }
                     }).runTaskLater(Main.getInstance(), (long)i);
                  }
               } else if(Main.getConfiguration().getString("shooter.level_I.projectile").equalsIgnoreCase("fireball")) {
                  Fireball fireball = (Fireball)player.launchProjectile(Fireball.class);
                  fireball.setIsIncendiary(false);
                  fireball.setYield(0.0F);
                  fireball.setVelocity(player.getEyeLocation().getDirection().multiply(4));
                  this.temp.put(fireball, player.getName());
                  if(Main.getConfiguration().getBoolean("shooter.level_I.enable-cooldown")) {
                     i = Main.getConfiguration().getInt("shooter.level_I.cooldown") * 20;
                     this.cooldown.add(player.getUniqueId());
                     (new BukkitRunnable() {
                        public void run() {
                           Shooter.this.cooldown.remove(player.getUniqueId());
                        }
                     }).runTaskLater(Main.getInstance(), (long)i);
                  }
               } else {
                  if(!Main.getConfiguration().getString("shooter.level_I.projectile").equalsIgnoreCase("egg")) {
                     return;
                  }

                  Egg egg = (Egg)player.launchProjectile(Egg.class);
                  egg.setVelocity(player.getEyeLocation().getDirection().multiply(4));
                  this.temp.put(egg, player.getName());
                  if(Main.getConfiguration().getBoolean("shooter.level_I.enable-cooldown")) {
                     i = Main.getConfiguration().getInt("shooter.level_I.cooldown") * 20;
                     this.cooldown.add(player.getUniqueId());
                     (new BukkitRunnable() {
                        public void run() {
                           Shooter.this.cooldown.remove(player.getUniqueId());
                        }
                     }).runTaskLater(Main.getInstance(), (long)i);
                  }
               }
            }

         }
      }
   }

   @EventHandler
   public void onHit(ProjectileHitEvent projectilehitevent) {
      Projectile projectile = projectilehitevent.getEntity();
      if(this.temp.containsKey(projectile)) {
         String s = (String)this.temp.get(projectile);
         Player player = Bukkit.getPlayer(s);
         if(player == null) {
            return;
         }

         World world = projectilehitevent.getEntity().getWorld();
         BlockIterator blockiterator = new BlockIterator(world, projectilehitevent.getEntity().getLocation().toVector(), projectilehitevent.getEntity().getVelocity().normalize(), 0.0D, 4);
         Block block = null;

         while(blockiterator.hasNext()) {
            block = blockiterator.next();
            if(block.getTypeId() != 0) {
               break;
            }
         }

         BlockBreakEvent blockbreakevent = new BlockBreakEvent(block, player);
         Bukkit.getPluginManager().callEvent(blockbreakevent);
         if(!blockbreakevent.isCancelled()) {
            block.breakNaturally();
            this.temp.remove(projectile);
         }
      }

   }
}
