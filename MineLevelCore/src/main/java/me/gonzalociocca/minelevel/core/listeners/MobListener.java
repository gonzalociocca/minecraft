package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.LuckItem;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 9/2/2017.
 */
public class MobListener implements Listener {
    Main plugin;

    public MobListener(Main main) {
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void CustomMobs(EntitySpawnEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof HumanEntity) && !(event.getEntity() instanceof ArmorStand) && event.getEntity().getCustomName() == null) {
            int level = Code.getRandom().nextInt(14) + 1;
            LivingEntity ent = (LivingEntity) event.getEntity();
            ent.setMaxHealth(level * 5);
            ent.setHealth(level * 5);
            //ent.setMaximumNoDamageTicks(level);
            ent.setCustomName(Code.Color("&2&l" + ent.getType().name() + "&f ( Nivel " + level + " )"));
            ent.setCustomNameVisible(true);
            try {
                AttributeInstance instance = ((CraftLivingEntity) ent).getHandle().getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
                if (instance != null) {
                    instance.setValue((level / 2) + 1);
                }
            } catch (Exception e) {
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() != null && !(event.getEntity() instanceof Player) && event.getEntity().getCustomName() != null) {
            boolean customLevel = event.getEntity().getCustomName().contains("Nivel");
            double level = -1;
            if (customLevel) {
                String[] args = event.getEntity().getCustomName().split(" ");
                boolean isnext = false;

                for (String s : args) {
                    if (isnext) {
                        level = Integer.parseInt(s);
                        break;
                    }
                    if (s.equals("Nivel")) {
                        isnext = true;
                    }
                }
            }
            RankType rankType = RankType.User;
            if (event.getEntity().getKiller() != null) {
                PlayerData pd = plugin.getDB().getPlayerData(event.getEntity().getKiller().getName());
                rankType = pd.getRank().getType();
            }
            checkRandomXPDrop(event, level, rankType);
            checkRandomItemDrop(event, level, rankType);
        }
    }

    public void checkRandomXPDrop(EntityDeathEvent event, double level, RankType rankType) {
        double base = 100 + (level * 6);
        double mult = 100;
        mult += rankType.getPosition() * 10;

        if (Variable.ActiveXpBoost > 0) {
            mult += Variable.ActiveXpBoost;
        }
        event.setDroppedExp((int) ((event.getDroppedExp() * (base / 100)) * (mult / 100)));
    }

    static int baseluck = 100;

    //todo: assign drop items with luck and groups.
    public void checkRandomItemDrop(EntityDeathEvent event, double level, RankType rankType) {
        double ran = Code.getRandom().nextInt(baseluck) + Code.getRandom().nextDouble();

        if (level > 0 && level < 16 && ran < 1) {
            Code.giveRandomMisteryBox(event.getEntity().getKiller(), 1, false, null);
        }
        for (LuckItem luckItem : getLuckItemList()) {
            if (luckItem.isFit(ran, level)) {
                event.getDrops().add(luckItem.getItemStack());
            }
        }
    }

    public void addLuckItem(LuckItem luckItem){
        luckItemList.add(luckItem);
    }

    List<LuckItem> luckItemList = null;

    public List<LuckItem> getLuckItemList() {
        if (luckItemList == null) {
            luckItemList = new ArrayList<LuckItem>();
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.GOLDEN_APPLE, 16, (byte) 1)));
            addLuckItem(new LuckItem().setLuck(0.5D).setItem(Code.makeItemStack(Material.GOLDEN_APPLE, 1, (byte) 1)));
            addLuckItem(new LuckItem().setLuck(0.3D).setItem(Code.makeItemStack(Material.GOLDEN_APPLE, 3, (byte) 1)).setMinMobLevel(7));
            addLuckItem(new LuckItem().setLuck(0.1D).setItem(Code.makeItemStack(Material.SADDLE, 1, (byte) 0)).setMaxMobLevel(7));
            addLuckItem(new LuckItem().setLuck(0.2D).setItem(Code.makeItemStack(Material.ENDER_PEARL, 1, (byte) 0)).setMinMobLevel(5));
            addLuckItem(new LuckItem().setLuck(0.1D).setItem(Code.makeItemStack(Material.ENDER_PEARL, 3, (byte) 0)).setMinMobLevel(5));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENDER_PEARL, 16, (byte) 0)).setMinMobLevel(5));
            addLuckItem(new LuckItem().setLuck(0.3D).setItem(Code.makeItemStack(Material.NAME_TAG, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.2D).setItem(Code.makeItemStack(Material.DIAMOND_BLOCK, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.1D).setItem(Code.makeItemStack(Material.DIAMOND_BLOCK, 3, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.DIAMOND_BLOCK, 16, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.2D).setItem(Code.makeItemStack(Material.GOLD_BLOCK, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.1D).setItem(Code.makeItemStack(Material.GOLD_BLOCK, 3, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.GOLD_BLOCK, 16, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.BEACON, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(1D).setItem(Code.makeItemStack(Material.GOLDEN_APPLE, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.5D).setItem(Code.makeItemStack(Material.GOLDEN_APPLE, 3, (byte) 0)));

            addLuckItem(new LuckItem().setLuck(2D).setItem(Code.makeItemStack(Material.DIAMOND, 3, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(3D).setItem(Code.makeItemStack(Material.GOLD_INGOT, 1, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(3D).setItem(Code.makeItemStack(Material.DIAMOND, 1, (byte) 0)));

            addLuckItem(new LuckItem().setLuck(0.5D).setItem(Code.makeItemStack(Material.DIAMOND, 9, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.7D).setItem(Code.makeItemStack(Material.GOLD_INGOT, 9, (byte) 0)));
            addLuckItem(new LuckItem().setLuck(0.7D).setItem(Code.makeItemStack(Material.DIAMOND, 6, (byte) 0)));

            addLuckItem(new LuckItem().setLuck(3D).setItem(Code.makeItemStack(Material.ROTTEN_FLESH, 1, (byte) 0)).setMaxMobLevel(10));


            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 sharpness:"+ Enchantment.DAMAGE_ALL.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 durability:"+ Enchantment.DURABILITY.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 fire_aspect:"+ Enchantment.FIRE_ASPECT.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_fire:"+ Enchantment.ARROW_FIRE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_damage:"+ Enchantment.ARROW_DAMAGE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_infinite:"+ Enchantment.ARROW_INFINITE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_knockback:"+ Enchantment.ARROW_KNOCKBACK.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 damage_arthropods:"+ Enchantment.DAMAGE_ARTHROPODS.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 damage_undead:"+ Enchantment.DAMAGE_UNDEAD.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 depth_strider:"+ Enchantment.DEPTH_STRIDER.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 dig_speed:"+ Enchantment.DIG_SPEED.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 water_worker:"+ Enchantment.WATER_WORKER.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 thorns:"+ Enchantment.THORNS.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 silk_touch:"+ Enchantment.SILK_TOUCH.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_projectile:"+ Enchantment.PROTECTION_PROJECTILE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_fire:"+ Enchantment.PROTECTION_FIRE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_environmental:"+ Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_explosions:"+ Enchantment.PROTECTION_EXPLOSIONS.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_fall:"+ Enchantment.PROTECTION_FALL.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 knockback:"+ Enchantment.KNOCKBACK.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 loot_bonus_blocks:"+ Enchantment.LOOT_BONUS_BLOCKS.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 loot_bonus_mobs:"+ Enchantment.LOOT_BONUS_MOBS.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 luck:"+ Enchantment.LUCK.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 lure:"+ Enchantment.LURE.getMaxLevel())));
            addLuckItem(new LuckItem().setLuck(0.05D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 oxygen:"+ Enchantment.OXYGEN.getMaxLevel())));

            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 sharpness:"+ (Enchantment.DAMAGE_ALL.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 durability:"+ (Enchantment.DURABILITY.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 fire_aspect:"+ (Enchantment.FIRE_ASPECT.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_fire:"+ (Enchantment.ARROW_FIRE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_damage:"+ (Enchantment.ARROW_DAMAGE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_infinite:"+ (Enchantment.ARROW_INFINITE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 arrow_knockback:"+ (Enchantment.ARROW_KNOCKBACK.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 damage_arthropods:"+ (Enchantment.DAMAGE_ARTHROPODS.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 damage_undead:"+ (Enchantment.DAMAGE_UNDEAD.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 depth_strider:"+ (Enchantment.DEPTH_STRIDER.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 dig_speed:"+ (Enchantment.DIG_SPEED.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 water_worker:"+ (Enchantment.WATER_WORKER.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 thorns:"+ (Enchantment.THORNS.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 silk_touch:"+ (Enchantment.SILK_TOUCH.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_projectile:"+ (Enchantment.PROTECTION_PROJECTILE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_fire:"+ (Enchantment.PROTECTION_FIRE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_environmental:"+ (Enchantment.PROTECTION_ENVIRONMENTAL.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_explosions:"+ (Enchantment.PROTECTION_EXPLOSIONS.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 protection_fall:"+ (Enchantment.PROTECTION_FALL.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 knockback:"+ (Enchantment.KNOCKBACK.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 loot_bonus_blocks:"+ (Enchantment.LOOT_BONUS_BLOCKS.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 loot_bonus_mobs:"+ (Enchantment.LOOT_BONUS_MOBS.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 luck:"+ (Enchantment.LUCK.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 lure:"+ (Enchantment.LURE.getMaxLevel()+1))));
            addLuckItem(new LuckItem().setLuck(0.025D).setItem(Code.makeItemStack(Material.ENCHANTED_BOOK.getId()+":0 1 oxygen:"+ (Enchantment.OXYGEN.getMaxLevel()+1))));
        }
        return luckItemList;
    }
}
