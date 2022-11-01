package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by noname on 18/2/2017.
 */
public class RexListener implements Listener {
    Main plugin;

    public RexListener(Main main) {
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    HashMap<String, Integer> killStreak = new HashMap();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            killStreak.put(event.getEntity().getName(), 0);
            if (event.getEntity().getKiller() != null) {
                Player killer = event.getEntity().getKiller();
                int ran = Code.getRandom().nextInt(3);
                if (!killStreak.containsKey(killer.getName())) {
                    killStreak.put(killer.getName(), 0);
                }
                int count = killStreak.get(killer.getName());
                ran += count / 3;
                ran = ran > 10 ? 10 : ran;
                killStreak.put(killer.getName(), count + 1);
                this.dropStarsAt(killer, ran);
                if (count > 0 && count % 5 == 0) {
                    Bukkit.broadcastMessage(Code.Color("&c&n&l" + killer.getName() + " &cesta en una racha de " + count + " asesinatos"));
                }
            }
        }
    }

    public void dropStarsAt(Player player, int amount) {
        if (player.hasPermission("rex.elite")) {
            amount *= 3;
        } else if (player.hasPermission("rex.vip")) {
            amount *= 2;
        }
        for (int a = 0; a < amount; a++) {
            player.getWorld().dropItemNaturally(player.getLocation(), Variable.RexStar);
        }
    }

    @EventHandler
    public void onExpPlayer(PlayerExpChangeEvent event) {
        if (event.getPlayer() != null) {
            event.setAmount(1);
            int level = event.getPlayer().getLevel();
            boolean isVip = event.getPlayer().hasPermission("rex.vip");
            boolean isElite = event.getPlayer().hasPermission("rex.elite");
            if (level < 101
                    || isVip && level < 501
                    || isElite && level < 2501) {
                if (isElite) {
                    event.getPlayer().setLevel(event.getPlayer().getLevel() + 5);
                } else if (isVip) {
                    event.getPlayer().setLevel(event.getPlayer().getLevel() + 2);
                } else {
                    event.getPlayer().setLevel(event.getPlayer().getLevel() + 1);
                }
            }
        }
    }

    @EventHandler
    public void SwordBreaker(EntityDamageByEntityEvent event) {
        if (event.getDamager() != null && event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();

            if (p.getEquipment() != null) {
                ItemStack item1 = p.getEquipment().getHelmet();
                ItemStack item2 = p.getEquipment().getChestplate();
                ItemStack item3 = p.getEquipment().getLeggings();
                ItemStack item4 = p.getEquipment().getBoots();
                ItemStack item5 = p.getEquipment().getItemInHand();
                boolean changed = false;
                for (Enchantment enc : Enchantment.values()) {
                    if (item1 != null && item1.getEnchantmentLevel(enc) > enc.getMaxLevel()) {
                        item1.removeEnchantment(enc);
                        changed = true;
                        try {
                            item1.addEnchantment(enc, enc.getMaxLevel());
                        }catch(Exception e){}
                    }
                    if (item2 != null && item2.getEnchantmentLevel(enc) > enc.getMaxLevel()) {
                        item2.removeEnchantment(enc);
                        changed = true;
                        try {
                            item2.addEnchantment(enc, enc.getMaxLevel());
                        }catch(Exception e){}
                    }
                    if (item3 != null && item3.getEnchantmentLevel(enc) > enc.getMaxLevel()) {
                        item3.removeEnchantment(enc);
                        changed = true;
                        try {
                            item3.addEnchantment(enc, enc.getMaxLevel());
                        }catch(Exception e){}
                    }
                    if (item4 != null && item4.getEnchantmentLevel(enc) > enc.getMaxLevel()) {
                        item4.removeEnchantment(enc);
                        changed = true;
                        try {
                            item4.addEnchantment(enc, enc.getMaxLevel());
                        }catch(Exception e){}
                    }
                    if (item5 != null && item5.getEnchantmentLevel(enc) > enc.getMaxLevel()) {
                        item5.removeEnchantment(enc);
                        changed = true;
                        try {
                            item5.addEnchantment(enc, enc.getMaxLevel());
                        }catch(Exception e){}
                    }
                }
                if(changed){
                    event.setCancelled(true);
                    p.updateInventory();
                }

            }
        }
    }
}
