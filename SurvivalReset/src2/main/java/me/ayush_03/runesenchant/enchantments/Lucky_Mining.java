package me.ayush_03.runesenchant.enchantments;

import java.util.List;
import java.util.Random;
import me.ayush_03.runesenchant.ExplosiveEvent;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import me.ayush_03.runesenchant.VeinminerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Lucky_Mining implements Listener {

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBreak(BlockBreakEvent blockbreakevent) {
      if(!blockbreakevent.isCancelled()) {
         if(!(blockbreakevent instanceof VeinminerEvent) && !(blockbreakevent instanceof ExplosiveEvent)) {
            Player player = blockbreakevent.getPlayer();
            int i = (int)(Math.random() * 100.0D);
            String s = FileManager.getManager().getEnchantments().getString("lucky_mining");
            s = ChatColor.translateAlternateColorCodes('&', s);
            if(player.getInventory().getItemInHand() != null && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
               String s1;
               if(player.getItemInHand().getItemMeta().getLore().contains(s + " I")) {
                  double d0 = Main.getConfiguration().getDouble("lucky_mining.level_I.chance");
                  if((double)i < d0) {
                     List list = Main.getConfiguration().getStringList("lucky_mining.level_I.commands");
                     String s2 = (String)list.get((new Random()).nextInt(list.size()));
                     String[] astring = s2.split(" : ");
                     s1 = astring[0];
                     s1 = s1.replace("%player%", player.getName());
                     String s3 = ChatColor.translateAlternateColorCodes('&', astring[1]);
                     s3 = s3.replace("%player%", player.getName());
                     Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s1);
                     player.sendMessage(s3);
                  }
               }

               List list1;
               int j;
               String s4;
               String[] astring1;
               String s5;
               if(player.getItemInHand().getItemMeta().getLore().contains(s + " II")) {
                  j = Main.getConfiguration().getInt("lucky_mining.level_II.chance");
                  if(i < j) {
                     list1 = Main.getConfiguration().getStringList("lucky_mining.level_II.commands");
                     s4 = (String)list1.get((new Random()).nextInt(list1.size()));
                     astring1 = s4.split(" : ");
                     s5 = astring1[0];
                     s5 = s5.replace("%player%", player.getName());
                     s1 = ChatColor.translateAlternateColorCodes('&', astring1[1]);
                     s1 = s1.replace("%player%", player.getName());
                     Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s5);
                     player.sendMessage(s1);
                  }
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " III")) {
                  j = Main.getConfiguration().getInt("lucky_mining.level_III.chance");
                  if(i < j) {
                     list1 = Main.getConfiguration().getStringList("lucky_mining.level_III.commands");
                     s4 = (String)list1.get((new Random()).nextInt(list1.size()));
                     astring1 = s4.split(" : ");
                     s5 = astring1[0];
                     s5 = s5.replace("%player%", player.getName());
                     s1 = ChatColor.translateAlternateColorCodes('&', astring1[1]);
                     s1 = s1.replace("%player%", player.getName());
                     Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s5);
                     player.sendMessage(s1);
                  }
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " IV")) {
                  j = Main.getConfiguration().getInt("lucky_mining.level_IV.chance");
                  if(i < j) {
                     list1 = Main.getConfiguration().getStringList("lucky_mining.level_IV.commands");
                     s4 = (String)list1.get((new Random()).nextInt(list1.size()));
                     astring1 = s4.split(" : ");
                     s5 = astring1[0];
                     s5 = s5.replace("%player%", player.getName());
                     s1 = ChatColor.translateAlternateColorCodes('&', astring1[1]);
                     s1 = s1.replace("%player%", player.getName());
                     Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s5);
                     player.sendMessage(s1);
                  }
               }

               if(player.getItemInHand().getItemMeta().getLore().contains(s + " V")) {
                  j = Main.getConfiguration().getInt("lucky_mining.level_V.chance");
                  if(i < j) {
                     list1 = Main.getConfiguration().getStringList("lucky_mining.level_V.commands");
                     s4 = (String)list1.get((new Random()).nextInt(list1.size()));
                     astring1 = s4.split(" : ");
                     s5 = astring1[0];
                     s5 = s5.replace("%player%", player.getName());
                     s1 = ChatColor.translateAlternateColorCodes('&', astring1[1]);
                     s1 = s1.replace("%player%", player.getName());
                     Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s5);
                     player.sendMessage(s1);
                  }
               }
            }

         }
      }
   }
}
