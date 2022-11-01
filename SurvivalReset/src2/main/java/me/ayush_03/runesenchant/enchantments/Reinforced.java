package me.ayush_03.runesenchant.enchantments;

import me.ayush_03.runesenchant.ArmorEquipEvent;
import me.ayush_03.runesenchant.FileManager;
import me.ayush_03.runesenchant.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Reinforced implements Listener {

   @EventHandler
   public void onEquip(ArmorEquipEvent armorequipevent) {
      Player player = armorequipevent.getPlayer();
      String s = FileManager.getManager().getEnchantments().getString("reinforced");
      s = ChatColor.translateAlternateColorCodes('&', s);

      try {
         if(Main.chestplates.contains(armorequipevent.getNewArmorPiece().getType())) {
            ItemStack itemstack = armorequipevent.getNewArmorPiece();
            if(itemstack.getItemMeta().hasLore()) {
               int i;
               int j;
               if(itemstack.getItemMeta().getLore().contains(s + " I") && player.getHealth() <= (double)Main.getConfiguration().getInt("reinforced.level_I.required-health")) {
                  i = Main.getConfiguration().getInt("reinforced.level_I.duration") * 20;
                  j = Main.getConfiguration().getInt("reinforced.level_I.potion_lvl") - 1;
                  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, i, j));
               }

               if(itemstack.getItemMeta().getLore().contains(s + " II") && player.getHealth() <= (double)Main.getConfiguration().getInt("reinforced.level_II.required-health")) {
                  i = Main.getConfiguration().getInt("reinforced.level_II.duration") * 20;
                  j = Main.getConfiguration().getInt("reinforced.level_II.potion_lvl") - 1;
                  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, i, j));
               }

               if(itemstack.getItemMeta().getLore().contains(s + " III") && player.getHealth() <= (double)Main.getConfiguration().getInt("reinforced.level_III.required-health")) {
                  i = Main.getConfiguration().getInt("reinforced.level_III.duration") * 20;
                  j = Main.getConfiguration().getInt("reinforced.level_III.potion_lvl") - 1;
                  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, i, j));
               }

               if(itemstack.getItemMeta().getLore().contains(s + " IV") && player.getHealth() <= (double)Main.getConfiguration().getInt("reinforced.level_IV.required-health")) {
                  i = Main.getConfiguration().getInt("reinforced.level_IV.duration") * 20;
                  j = Main.getConfiguration().getInt("reinforced.level_IV.potion_lvl") - 1;
                  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, i, j));
               }

               if(itemstack.getItemMeta().getLore().contains(s + " V") && player.getHealth() <= (double)Main.getConfiguration().getInt("reinforced.level_V.required-health")) {
                  i = Main.getConfiguration().getInt("reinforced.level_V.duration") * 20;
                  j = Main.getConfiguration().getInt("reinforced.level_V.potion_lvl") - 1;
                  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, i, j));
               }
            }
         }
      } catch (Exception exception) {

      }

   }
}
