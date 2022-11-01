package me.ayush_03.runesenchant.enchantments;

import java.util.ArrayList;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Tnt_Shooter implements Listener {

   private ArrayList entity = new ArrayList();

   @EventHandler
   public void onShoot(EntityShootBowEvent entityshootbowevent) {
      String s = FileManager.getManager().getEnchantments().getString("tnt_shooter");
      s = ChatColor.translateAlternateColorCodes('&', s);
      if(entityshootbowevent.getEntity() instanceof Player) {
         Player player = (Player)entityshootbowevent.getEntity();

         try {
            if(entityshootbowevent.getBow().getItemMeta().hasLore() && entityshootbowevent.getBow().getItemMeta().getLore().contains(s + " I")) {
               TNTPrimed tntprimed = (TNTPrimed)entityshootbowevent.getProjectile().getWorld().spawn(entityshootbowevent.getProjectile().getLocation(), TNTPrimed.class);
               tntprimed.setVelocity(player.getEyeLocation().getDirection().multiply(entityshootbowevent.getForce()));
               entityshootbowevent.setProjectile(tntprimed);
               if(!Main.getConfiguration().getBoolean("tnt_shooter.level_I.explosion")) {
                  this.entity.add(tntprimed);
               }
            }
         } catch (Exception exception) {
            ;
         }
      }

   }

   @EventHandler
   public void onExplode(EntityExplodeEvent entityexplodeevent) {
      if(entityexplodeevent.getEntity() instanceof TNTPrimed) {
         TNTPrimed tntprimed = (TNTPrimed)entityexplodeevent.getEntity();
         if(this.entity.contains(tntprimed)) {
            entityexplodeevent.blockList().clear();
            this.entity.remove(tntprimed);
         }
      }

   }
}
