package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * Created by noname on 9/2/2017.
 */
public class FactionsListener implements Listener {
    Main plugin;

    public FactionsListener(Main main) {
        plugin = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() < 1) {
                    event.setDamage(event.getDamage() / 4);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerGodMode(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.getEntity().getWorld().getPVP()) {
                if (Variable.GodList.contains(event.getEntity().getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    int WorldChanged = 0;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFlyPvP(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getPVP()) {
            if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.getPlayer().setAllowFlight(true);
            } else {
                event.getPlayer().setAllowFlight(false);
            }
        }
        WorldChanged++;
        if (WorldChanged % 500 == 0) {
            Code.giveRandomMisteryBox(event.getPlayer(), Code.getRandom().nextInt(3) + 1, true, "Teletransportarse N°" + WorldChanged + " del Día.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHopperInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.HOPPER_MINECART)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.isCancelled() && e.getWhoClicked() instanceof Player) {
            Inventory inv = e.getInventory();

            if (inv instanceof AnvilInventory) {
                InventoryView view = e.getView();
                int rawSlot = e.getRawSlot();

                if (rawSlot == view.convertSlot(rawSlot)) {

                    if (rawSlot == 2) {
                        ItemStack item = e.getCurrentItem();

                        if (item != null) {
                            if (item.getType().equals(Material.MOB_SPAWNER)) {
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    int BlocksPlaced = 0;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHopperInteract(BlockPlaceEvent event) {
        if(event.isCancelled()){
            return;
        }
        if (event.getBlock() != null && event.getBlock().getType().equals(Material.HOPPER_MINECART)) {
            event.setCancelled(true);
        }
        if (event.getBlock() != null && event.getBlock().getType().equals(Material.MOB_SPAWNER) && event.getItemInHand() != null && event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().hasDisplayName()) {
            CreatureSpawner state = ((CreatureSpawner) event.getBlock().getState());
            state.setSpawnedType(EntityType.fromName(event.getItemInHand().getItemMeta().getDisplayName().replace(" ", "").replace("_", "").toUpperCase()));
        }
        BlocksPlaced++;
        if (BlocksPlaced % 1000 == 0) {
            Code.giveRandomMisteryBox(event.getPlayer(), Code.getRandom().nextInt(3) + 1, true, "Colocar Bloque N°" + BlocksPlaced + " del Día.");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                    if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
                        if (!Variable.CompassLimit.containsKey(event.getPlayer().getName())) {
                            Variable.CompassLimit.put(event.getPlayer().getName(), 0L);
                        }
                        if (Variable.CompassLimit.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
                            event.getPlayer().sendMessage(Code.Color("&aSolo puedes moverte cada 5segundos"));
                            return;
                        }
                        int x = Code.getRandom().nextInt(30000);
                        int z = Code.getRandom().nextInt(30000);
                        int y = event.getPlayer().getWorld().getHighestBlockYAt(x, z) + 1;
                        event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), x, y, z));
                        event.getPlayer().sendMessage(Code.Color("&aMovido a X: " + x + " Y: " + y + " Z: " + z));
                        Variable.CompassLimit.put(event.getPlayer().getName(), System.currentTimeMillis() + 5000);
                    }
                }
            }
        }
    }

    int ItemsConsumed = 0;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHDApples(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
            event.setCancelled(true);

            Player p = event.getPlayer();
            p.sendMessage(Code.Color("&aComida!"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 60, 3, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 90, 3, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 90, 2, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 90, 2, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 15, 2, true, true), true);
            ItemStack it1 = p.getInventory().getItemInHand();
            if (it1 != null) {
                if (it1.getType() == Material.GOLDEN_APPLE && it1.getDurability() == 1) {
                    if (it1.getAmount() > 1) {
                        it1.setAmount(it1.getAmount() - 1);
                    } else {
                        it1 = new ItemStack(Material.AIR);
                    }
                    p.getInventory().setItemInHand(it1);
                    p.updateInventory();
                    return;
                }
            }
        } else if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            event.setCancelled(true);

            Player p = event.getPlayer();
            p.sendMessage(Code.Color("&aComida!"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 45, 1, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 120, 1, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 300, 0, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 300, 0, true, true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 45, 1, true, true), true);
            ItemStack it1 = p.getInventory().getItemInHand();
            if (it1 != null) {
                if (it1.getType() == Material.GOLDEN_APPLE) {
                    if (it1.getAmount() > 1) {
                        it1.setAmount(it1.getAmount() - 1);
                    } else {
                        it1 = new ItemStack(Material.AIR);
                    }
                    p.getInventory().setItemInHand(it1);
                    p.updateInventory();
                    return;
                }
            }
        }
        ItemsConsumed++;
        if (ItemsConsumed % 500 == 0) {
            Code.giveRandomMisteryBox(event.getPlayer(), Code.getRandom().nextInt(3) + 1, true, "Consumidor N°" + ItemsConsumed + " del Día.");
        }
    }

    static int JoinedToday = 0;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void LoadPlayer(PlayerJoinEvent event) {
        if (Variable.ShuttingDown) {
            event.getPlayer().kickPlayer(Code.Color("&f&lServer reiniciandose..."));
            return;
        }
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.getPlayer().setWalkSpeed(Code.getRealMoveSpeed(1, false));
        event.getPlayer().setFlySpeed(Code.getRealMoveSpeed(1, true));
        (((CraftPlayer) event.getPlayer()).getHandle()).getAttributeInstance(GenericAttributes.c).setValue(0.0D);
        JoinedToday++;
        if (JoinedToday % 100 == 0) {
            Code.giveRandomMisteryBox(event.getPlayer(), Code.getRandom().nextInt(3) + 1, true, "Usuario N°" + JoinedToday + " del Día.");
        }

        if(event.getPlayer().getWorld().getPVP()){
            Variable.Attachments.get(event.getPlayer().getName()).unsetPermission("MyPet.extended.ride");
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void SwordBreaker(EntityDamageByEntityEvent event) {
        if(event.isCancelled()){
            return;
        }
        if (event.getDamager() != null && event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();

            if (p.getEquipment() != null) {
                ItemStack item5 = p.getEquipment().getItemInHand();
                boolean changed = false;
                if (item5 != null && item5.getType().equals(Material.DIAMOND_AXE) && item5.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 6) {
                    int level = item5.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                    item5.removeEnchantment(Enchantment.DAMAGE_ALL);
                    changed = true;
                    PlayerData pd = plugin.getDB().getPlayerData(p.getName());
                    pd.addDiamonds(50000);
                    plugin.getLogger().log(Level.WARNING, "User: "+p.getName()+" Sharpness: "+level+" Prize:$50000");
                    try {
                        item5.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                    } catch (Exception e) {
                    }
                    for(int a = 0; a < 3; a++) {
                        Code.giveRandomMisteryBox(p, Code.getRandom().nextInt(16)+1, false, "");
                    }
                }
                if (changed) {
                    event.setCancelled(true);
                    p.updateInventory();
                }

            }
        }
    }


    GameMode latestGameMode = null;
    String latestPlayerName = null;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldChange(PlayerChangedWorldEvent event){
        latestGameMode = event.getPlayer().getGameMode();
        latestPlayerName = event.getPlayer().getName();

        if(event.getPlayer().getWorld().getPVP()){
            Variable.Attachments.get(event.getPlayer().getName()).unsetPermission("MyPet.extended.ride");
        } else {
            Variable.Attachments.get(event.getPlayer().getName()).setPermission("MyPet.extended.ride", true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public synchronized void onWorldChangeFinal(PlayerChangedWorldEvent event){
        if(latestGameMode != null && latestPlayerName != null &&
                (latestGameMode == GameMode.SPECTATOR || latestGameMode == GameMode.CREATIVE) && event.getPlayer().getName().equals(latestPlayerName)){
            final GameMode newGameMode = latestGameMode;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().setGameMode(newGameMode);
                    System.out.println(event.getPlayer().getName()+": teleported to "+event.getPlayer().getLocation().toString()+" with GameMode "+newGameMode.toString());
                }
            },20);
            latestGameMode = null;
            latestPlayerName = null;
        }
    }
}
