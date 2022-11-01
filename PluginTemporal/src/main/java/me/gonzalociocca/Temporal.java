/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca;

/**
 * @author ciocca
 */

import com.google.common.collect.Sets;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Temporal extends JavaPlugin implements Listener {

    public FileConfiguration fc;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);
        fc = this.getConfig();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "-> Plugin Temporal Desactivado!");
    }


    Random r = new Random();

    @EventHandler
    public void onPlaceSkull(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getBlock().getType() == Material.SKULL) {
            event.getBlockPlaced().getState().update(true);
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Location loc = event.getEntity().getLocation();
        for (Player p : event.getEntity().getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(loc) < 1024) {
                p.sendMessage(event.getDeathMessage());
            }
        }
        event.setDeathMessage("");
        if (event.getEntity() != null && event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause() != null && event.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            if (r.nextInt(10) == 0) {
                ItemStack it = new ItemStack(Material.SKULL_ITEM);
                it.setDurability((short) 3);
                SkullMeta im = (SkullMeta) it.getItemMeta();

                im.setOwner(event.getEntity().getName());
                im.setDisplayName(Code.Color("&a" + event.getEntity().getName()));
                it.setItemMeta(im);
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), it);
                event.setDeathMessage(Code.Color("&c" + event.getEntity().getName() + " ha sido decapitado."));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInCombat(event.getPlayer())) {
                for (Player play : event.getPlayer().getWorld().getPlayers()) {
                    play.sendMessage(Code.Color(pvpPrefix + event.getPlayer().getName() + " se ha disconectado en combate."));
                }
                event.getPlayer().setHealth(0.0D);
                combatLog.remove(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (combatLog.containsKey(event.getEntity().getName())) {
            combatLog.remove(event.getEntity().getName());
            event.getEntity().sendMessage(pvpPrefix + "Ya no estas en combate!");
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().equals("Estas logeado desde otra localizacion.")) {
            event.setCancelled(true);
        }
        combatLog.remove(event.getPlayer().getName());
    }

    String pvpPrefix = Code.Color("&b&l[PvP] &e");
    HashMap<String, Long> combatLog = new HashMap();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCom(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/feed") || event.getMessage().startsWith("/msg") || event.getMessage().startsWith("/helpop")) {
            return;
        }
        if (isInCombat(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCombatTeleport(PlayerTeleportEvent event){
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && isInCombat(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    public boolean isInCombat(Player player){
        Long time = combatLog.get(player.getName());
        if(time != null && time > System.currentTimeMillis()){
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() != null && event.getEntity() instanceof Player && event.getDamager() != null && event.getDamager() instanceof Player) {
            Player p1 = (Player) event.getEntity();
            Player p2 = (Player) event.getDamager();
            if(p1.getGameMode().equals(GameMode.CREATIVE) || p2.getGameMode().equals(GameMode.CREATIVE) || p1.getGameMode().equals(GameMode.SPECTATOR) || p2.getGameMode().equals(GameMode.SPECTATOR)){
                event.setCancelled(true);
                return;
            }
            if (p1.getWorld().getName().equalsIgnoreCase("world") || p1.getWorld().getName().equalsIgnoreCase("world_nether") || p1.getWorld().getName().equalsIgnoreCase("plots")) {
                return;
            }
            Long clp1 = combatLog.get(p1.getName());
            Long clp2 = combatLog.get(p2.getName());
            Long now = System.currentTimeMillis();

            if (clp1 == null || clp1 < now) {
                p1.sendMessage(pvpPrefix + "Has entrado en combate, no te disconectes, 10s restantes.");
            }
            if (clp2 == null || clp2 < now) {
                p2.sendMessage(pvpPrefix + "Has entrado en combate, no te disconectes, 10s restantes");
            }
            combatLog.put(p1.getName(), now + 10000L);
            combatLog.put(p2.getName(), now + 10000L);

            if (p1.getAllowFlight()) {
                p1.setAllowFlight(false);
            }
            if (p2.getAllowFlight()) {
                p2.setAllowFlight(false);
            }
            if (p1.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                p1.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            if (p2.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                p2.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
        }
    }
    @EventHandler
    public void onPortal(PortalCreateEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType().equals(UpdateType.SEC)) {
            long currentMillis = System.currentTimeMillis();
            Iterator<Map.Entry<String, Long>> it = combatLog.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, Long> entry = it.next();
                if (entry.getValue() < currentMillis) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    it.remove();
                    if (player != null && player.isOnline()) {
                        player.sendMessage(pvpPrefix + "Ya no estas en combate!");
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayerExact(event.getName());
        if (player != null) {
            if (!player.getAddress().getAddress().getCanonicalHostName().equals(event.getAddress().getCanonicalHostName())) {
                event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("Un jugador con tu nombre ya esta conectado!");
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            } else {
                event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Code.Color("&aIntenta conectarte nuevamente, aparentemente estabas conectado!"));
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                player.kickPlayer(Code.Color("&aEstas logeado desde otro launcher."));
            }
        }
    }

    private Object getField(Object obj, String name) {
        try {
            Field field = obj.getClass().getField(name);
            field.setAccessible(true);

            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void NoGuardian(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Guardian) {
            event.setCancelled(true);
        }
    }

    HashMap<String, Long> cooldowns = new HashMap();

    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(cbc, (byte) 2));
    }

    @EventHandler
    public void onPearlCooldown(PlayerInteractEvent e) {
        if (e.getPlayer() == null) {
            return;
        }

        if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                (e.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))) {
            Player p = e.getPlayer();
            if (cooldowns.size() > 256) {
                cooldowns.clear();
            }
            if (!this.cooldowns.containsKey(p.getName())) {
                cooldowns.put(p.getName(), System.currentTimeMillis() + 16000L);
            } else if (cooldowns.get(p.getName()) > System.currentTimeMillis()) {
                this.sendActionBar(p, Code.Color("&fPodras lanzar otra perla en &c" + (cooldowns.get(p.getName()) - System.currentTimeMillis()) + "ms"));
                e.setCancelled(true);
            } else {
                cooldowns.put(p.getName(), System.currentTimeMillis() + 16000L);
            }
        }
    }

    HashMap<String, LinkedList<ItemStack>> map = new HashMap();

    @EventHandler
    public void recibeItems(UpdateEvent event) {
        if (event.getType() != UpdateType.SEC) {
            return;
        }
        if (map.isEmpty()) {
            return;
        }
        for (String pe : map.keySet()) {
            if (map.get(pe).isEmpty()) {
                map.remove(pe);
                break;
            }
            Player player = Bukkit.getPlayer(pe);
            if (player == null) {
                continue;
            }
            int space = 0;
            for (ItemStack it : player.getInventory().getContents()) {
                if (it == null) {
                    space++;
                }
            }
            while (space > 2) {
                ItemStack it = map.get(pe).poll();
                space--;
                if (it != null) {
                    player.getInventory().addItem(it);
                }

            }
        }
    }

    public List<String> watchKit(int kitlevel) {
        List<String> list = new ArrayList();
        list.add(Code.Color("&e&lKit Aprobado:"));
        list.add(Code.Color("&f" + 1 * kitlevel + "x Armadura Prote 5 Dura 4"));
        list.add(Code.Color("&f" + 1 * kitlevel + "x Hacha Filo 6"));
        list.add(Code.Color("&f" + 1 * kitlevel + "x Espada Filo 6 Fuego 2 Dura 3"));
        list.add(Code.Color("&f" + 32 * kitlevel + "x Manzanas IV"));
        list.add(Code.Color("&f" + 16 * kitlevel + "x Autografos de Rudolph"));
        list.add(Code.Color("&f" + 8 * kitlevel + "x Autografos de Santa"));
        if (kitlevel < 5) {
            list.add(Code.Color("&aUsa /bugcofre recibir &cpara recibir los items (SOLO SE DAN ITEMS 1 VEZ!)"));
            list.add(Code.Color("&aSi has perdido mas items, usa /bugcofre aumentar Y espera 24 horas"));
            list.add(Code.Color("&fpara que te den un kit mejor y verifiquen que perdiste."));
        } else {
            list.add(Code.Color("&aUsa /bugcofre recibir &fpara recibir tus items(Kit Maximo)"));
        }
        return list;
    }

    public void giveKit(Player pe, int kitlevel) {
        LinkedList items = new LinkedList();
        for (int a = 0; a < kitlevel; a++) {
            items.add(makeArmor(Material.DIAMOND_HELMET));
            items.add(makeArmor(Material.DIAMOND_CHESTPLATE));
            items.add(makeArmor(Material.DIAMOND_LEGGINGS));
            items.add(makeArmor(Material.DIAMOND_BOOTS));
            items.add(makeAxe(Material.DIAMOND_AXE));
            items.add(makeSword(Material.DIAMOND_SWORD));
            items.add(new ItemStack(Material.GOLDEN_APPLE, 32, (short) 1));
            items.add(makeAutografo(16, 0));
            items.add(makeAutografo(8, 1));
        }
        map.put(pe.getName(), items);
    }

    public ItemStack makeAutografo(int size, int aut) {
        ItemStack it = new ItemStack(Material.PAPER, size);
        ItemMeta im = it.getItemMeta();
        if (aut == 0) {
            im.setDisplayName(Code.Color("&eVale 1x Autografo de Rudolph"));
            im.setLore(Arrays.asList(Code.Color("&cCanjealo con santa!")));
        }
        if (aut == 1) {
            im.setDisplayName(Code.Color("&aVale 1x Autografo de Santa"));
            im.setLore(Arrays.asList(Code.Color("&cCanjealo con santa!")));
        }
        it.setItemMeta(im);
        return it;
    }

    public ItemStack makeArmor(Material mt) {
        org.bukkit.inventory.ItemStack it = new ItemStack(mt, 1);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 4);
        return it;
    }

    public ItemStack makeAxe(Material mt) {
        org.bukkit.inventory.ItemStack it = new ItemStack(mt, 1);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 6);
        return it;
    }

    public ItemStack makeSword(Material mt) {
        org.bukkit.inventory.ItemStack it = new ItemStack(mt, 1);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 6);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.FIRE_ASPECT, 2);
        it.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 3);
        return it;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equals("analyzer")) {
            if (sender.isOp()) {
                if (args.length == 0) {
                    sender.sendMessage(Code.Color("Comando: /killmobs watch"));
                    sender.sendMessage(Code.Color("Comando: /killmobs clear"));
                    sender.sendMessage(Code.Color("Comando: /killmobs removeall <entity>"));
                }
                if (args.length >= 1) {
                    if (args[0].equalsIgnoreCase("worldtest")) {
                        sender.sendMessage("getWorldServer(0): " + MinecraftServer.getServer().getWorldServer(0).getWorld().getName());
                        sender.sendMessage("worlds(0): " + MinecraftServer.getServer().worlds.get(0).getWorld().getName());
                    }
                    if (args[0].equalsIgnoreCase("uuidclean")) {
                        int deadcount = 0;
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            try {
                                if (w != null)
                                    for (Iterator<Entity> it = ((WorldServer) w).entitiesByUUID.values().iterator(); it.hasNext(); ) {
                                        if (deadcount > 1000) {
                                            break;
                                        }
                                        Entity ent = (Entity) it.next();
                                        if (ent.dead) {
                                            ((WorldServer) w).entitiesById.d(ent.getId());
                                            it.remove();
                                            deadcount++;
                                        }
                                    }

                            } catch (Exception e) {

                            }
                        }
                        sender.sendMessage("DeadCount: " + deadcount);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("uuid")) {
                        int deadcount = 0;
                        int byuuidcount = 0;
                        HashMap<String, Integer> mapc = new HashMap();
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            try {
                                Map map = ((WorldServer) w).entitiesByUUID;
                                byuuidcount += map.size();
                                for (Object obj : map.values()) {
                                    if (obj instanceof net.minecraft.server.v1_8_R3.Entity) {
                                        Entity ent = (Entity) obj;
                                        if (ent.dead) {
                                            if (!mapc.containsKey(ent.getClass().getName())) {
                                                mapc.put(ent.getClass().getName(), 1);
                                            } else {
                                                mapc.put(ent.getClass().getName(), mapc.get(ent.getClass().getName()) + 1);
                                            }
                                            deadcount++;
                                        }
                                    }
                                }

                            } catch (Exception e) {

                            }
                        }
                        for (String s : mapc.keySet()) {
                            sender.sendMessage(s + ": " + mapc.get(s));
                        }
                        sender.sendMessage("DeadCount: " + deadcount + "  AllCount: " + byuuidcount);
                    }
                    if (args[0].equalsIgnoreCase("watch")) {
                        HashMap<String, Integer> values2 = new HashMap();
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            for (net.minecraft.server.v1_8_R3.Entity et : w.entityList) {
                                if (!values2.containsKey(et.getClass().getName())) {
                                    values2.put(et.getClass().getName(), 1);
                                } else {
                                    values2.put(et.getClass().getName(), values2.get(et.getClass().getName()) + 1);
                                }
                            }
                        }
                        for (String s : values2.keySet()) {
                            sender.sendMessage(s + "==(" + values2.get(s) + ")");

                        }
                    }
                    if (args[0].equalsIgnoreCase("chunks")) {
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            sender.sendMessage(w.getWorld().getName() + " Chunks: " + ((WorldServer) w).chunkProviderServer.chunks.size());
                        }
                    }
                    if (args[0].equalsIgnoreCase("players")) {
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            sender.sendMessage(w.getWorld().getName() + " Players: " + ((WorldServer) w).players.size());
                        }
                    }
                    if (args[0].equalsIgnoreCase("unloadqueue")) {
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            try {
                                sender.sendMessage("" + w.getWorld().getName() + ": " + ((WorldServer) w).chunkProviderServer.unloadQueue.size() + "");
                            } catch (Exception e) {
                            }
                        }

                    }
                    if (args[0].equalsIgnoreCase("redstone")) {
                        //HashMap<String,Integer> names = new HashMap();
                        int truecount = 0;
                        int falsecount = 0;
                        long max = 0;
                        long maxw = 0;
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            ConcurrentLinkedQueue<NextTickListEntry> tree = (ConcurrentLinkedQueue<NextTickListEntry>) this.getField((WorldServer) w, "M");
                            if (tree != null) {
                                sender.sendMessage(w.getWorld().getName() + " size: " + tree.size());
                                Iterator<NextTickListEntry> it = tree.iterator();
                                while (it.hasNext()) {
                                    NextTickListEntry nx = it.next();
                                    if (nx == null) {
                                        continue;
                                    }

                                    if (nx.a().getName().equalsIgnoreCase("Gravel")) {
                                        if (nx.b > w.getTime()) {
                                            if (nx.b > max) {
                                                max = nx.b;
                                            }
                                            truecount++;
                                        } else {
                                            falsecount++;
                                        }
                                    }
                                /*    String pos = "X:"+nx.a.getX()+" Y:"+nx.a.getY()+" Z:"+nx.a.getZ();
                                    if(!names.containsKey(pos)){
                                        names.put(pos,0);
                                    }
                                    names.put(pos,names.get(pos)+1);
                                }*/
                                }
                            }
                            if (w.getWorldData().getTime() > maxw) {
                                maxw = w.getWorldData().getTime();
                            }
                        }
                        /*for(String name : names.keySet()){
                          sender.sendMessage(name+": "+names.get(name));
                        }*/
                        sender.sendMessage("true: " + truecount + " false: " + falsecount + " max: " + max + " worldtime: " + maxw);

                    }
                    if (args[0].equalsIgnoreCase("redstoneclear")) {
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            ConcurrentLinkedQueue<NextTickListEntry> tree = (ConcurrentLinkedQueue<NextTickListEntry>) this.getField((WorldServer) w, "M");
                            if (tree != null) {
                                synchronized (tree) {
                                    tree.clear();
                                }
                            }
                        }
                    }
                    if (args[0].equalsIgnoreCase("add500")) {
                        Set<net.minecraft.server.v1_8_R3.Entity> list = Sets.newConcurrentHashSet();
                        ConcurrentLinkedQueue<net.minecraft.server.v1_8_R3.Entity> test = new ConcurrentLinkedQueue();
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            for (net.minecraft.server.v1_8_R3.Entity et : w.entityList) {
                                list.add(et);
                                list.add(et);
                                test.add(et);
                                test.add(et);
                            }
                            for (net.minecraft.server.v1_8_R3.Entity et : list) {
                                w.entityList.add(et);
                            }
                            System.out.println("list: " + list.size() + " test: " + test.size());
                            list.clear();
                            test.clear();
                        }
                    }
                    if (args[0].equalsIgnoreCase("searchGuardian")) {
                        HashMap<String, Integer> guardianamount = new HashMap();
                        HashMap<String, String> coords = new HashMap();
                        for (net.minecraft.server.v1_8_R3.World w : ((CraftServer) Bukkit.getServer()).getHandle().getServer().worlds) {
                            for (net.minecraft.server.v1_8_R3.Entity et : w.entityList) {
                                if (et instanceof net.minecraft.server.v1_8_R3.EntityGuardian) {
                                    Location loc = et.getBukkitEntity().getLocation();
                                    String key = loc.getWorld().getName() + " X: " + loc.getChunk().getX() + " Z: " + loc.getChunk().getZ();
                                    if (!guardianamount.containsKey(key)) {
                                        guardianamount.put(key, 0);
                                    }
                                    guardianamount.put(key, guardianamount.get(key) + 1);
                                    coords.put(key, "X: " + loc.getX() + " / Y:" + loc.getY() + " / Z:" + loc.getZ());
                                }
                            }
                        }
                        for (String s : guardianamount.keySet()) {
                            sender.sendMessage(s + ": " + guardianamount.get(s) + " Last: " + coords.get(s));
                        }
                    }


                }
                return true;
            }
            return true;
        }
        return false;

    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event){
        event.getPlayer().teleport(Bukkit.getWorld("world_the_end").getSpawnLocation());
        event.setCancelled(true);
    }

    @EventHandler
    public void onJ(PlayerJoinEvent event) {
        if (event.getPlayer().getActivePotionEffects() != null && !event.getPlayer().getActivePotionEffects().isEmpty()) {
            for (PotionEffect pot : event.getPlayer().getActivePotionEffects()) {
                event.getPlayer().removePotionEffect(pot.getType());
            }
        }
    }

}
