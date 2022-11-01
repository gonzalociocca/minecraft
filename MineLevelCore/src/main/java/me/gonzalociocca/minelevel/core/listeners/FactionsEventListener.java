package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.EventType;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.events.EventStartEvent;
import me.gonzalociocca.minelevel.core.events.EventStopEvent;
import me.gonzalociocca.minelevel.core.misc.*;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Created by noname on 8/2/2017.
 */
public class FactionsEventListener implements Listener {

    Main plugin;

    Long coliseostart = 0L;
    HashMap<String, Integer> coliseostats = new HashMap();

    public FactionsEventListener(Main main) {
        plugin = main;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                Variable.Location1 = new Location(Bukkit.getWorld("world_the_end"), -299, 22, 16);
                Variable.Location2 = new Location(Bukkit.getWorld("world_the_end"), -149, 86, -233);
            }
        }, 200L);
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public static FactionsEvent getLastEvent() {
        return Variable.currentEvent;
    }

    @EventHandler
    public void onEventStart(EventStartEvent event) {
        if (event.getLastEvent().getEventType().equals(EventType.MasXP)) {
            Variable.ActiveXpBoost = 25 + Code.getRandom().nextInt(175);
            Bukkit.broadcastMessage(Code.Color("&a> &nEvento MasXP&a, Experiencia Aumentada en un " + Variable.ActiveXpBoost + "%"));
        } else if (event.getLastEvent().getEventType().equals(EventType.Subastas)) {
            Variable.SubastasInventory = Bukkit.createInventory(null, 54, Code.Color("&d&n&lSubastas!"));
            Collections.sort(Variable.SubastaItems, new Comparator<SubastaItem>() {
                @Override
                public int compare(SubastaItem bid1, SubastaItem bid2) {
                    return Code.getRandom().nextInt();
                }
            });
            int ranPos = Code.getRandom().nextInt(Variable.SubastaItems.size());
            for (int a = 0; a < 54; a++) {
                if (a >= 0 && a < 9 || a >= 18 && a < 27 || a >= 36 && a < 45) {
                    ranPos++;
                    if (ranPos >= Variable.SubastaItems.size()) {
                        ranPos = 0;
                    }
                    SubastaItem item = Variable.SubastaItems.get(ranPos);
                    item.setGlassSlot(a + 9);
                    Variable.SubastasInventory.setItem(a, item.getItemStack());
                    Variable.SubastasInventory.setItem(a + 9, Code.makeItemStack("160:4 1 name:&aClick_para_ofertar_por_$" + item.getInitialPrice()));
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onSubastaInventory(InventoryClickEvent event) {
        if(Variable.SubastasInventory == null){
            return;
        }
        boolean isSubastas;

        isSubastas = event.getInventory() != null && event.getInventory().getTitle() != null && event.getInventory().getTitle().equals(Variable.SubastasInventory.getTitle());

        if(!isSubastas){
            isSubastas = event.getClickedInventory() != null && event.getInventory().getTitle() != null && event.getInventory().getTitle().equals(Variable.SubastasInventory.getTitle());
        }


        if(!isSubastas){
            return;
        }
        event.setCancelled(true);

        if (event.getCurrentItem() != null && event.getWhoClicked() != null) {
            int a = event.getSlot();
            if (!(a >= 0 && a < 9 || a >= 18 && a < 27 || a >= 36 && a < 45)) {
                int xItem = a - 9;
                for (SubastaItem item : Variable.SubastaItems) {
                    if (item.getItemStack().equals(event.getInventory().getItem(xItem))) {
                        item.addBidder(plugin, (Player) event.getWhoClicked());
                        return;
                    }
                }
                event.getWhoClicked().sendMessage("Hubo un error en la subasta, reportalo en el foro con imagenes de este mensaje.");

            }

        }
    }

    @EventHandler
    public void onEventStop(EventStopEvent event) {
        this.handleRewards();
        Variable.RewardsSended = false;
        if (event.getLastEvent().getEventType().equals(EventType.Coliseo)) {
            coliseostart = 0L;
            Variable.ColiseoStarted = false;
            Variable.ColiseoPhase1 = false;
            Variable.ColiseoPhase2 = false;
            coliseostats.clear();
            Variable.ColiseoPreviousLocations.clear();
            Variable.ColiseoAntiAfk.clear();

        } else if (event.getLastEvent().getEventType().equals(EventType.MasXP)) {
            Variable.ActiveXpBoost = 0;
        } else if (event.getLastEvent().getEventType().equals(EventType.Subastas)) {
            for (SubastaItem si : Variable.SubastaItems) {
                for (SubastaBid sb : si.getSortedBids()) {
                    Player player = Bukkit.getPlayer(sb.getBidderName());
                    if (player != null && player.isOnline()) {
                        int firstEmpty = player.getInventory().firstEmpty();
                        if (firstEmpty >= 0) {
                            PlayerData pd = plugin.getDB().getPlayerData(player.getName());
                            if (pd.getDiamonds() >= sb.getMaxBid()) {
                                pd.removeDiamonds(sb.getMaxBid());
                                player.getInventory().addItem(si.getItemStack());
                                player.sendMessage(Code.Color("&a&n&lSubastas&e: Has ganado la subasta por $" + sb.getMaxBid()));
                                break;
                            }
                        }
                    }
                }
                si.resetAll();
            }
            HumanEntity[] users = Variable.SubastasInventory.getViewers().toArray(new HumanEntity[Variable.SubastasInventory.getViewers().size()]);
            for (HumanEntity entity : users) {
                if (entity != null) {
                    entity.closeInventory();
                }
            }
            Variable.SubastasInventory.clear();
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {

        if (event.getType() != UpdateType.SEC) {
            return;
        }
        if (Variable.currentEvent == null) {
            Variable.currentEvent = new FactionsEvent(plugin, EventType.Min5, EventType.Min5, EventType.Mineria);
            Bukkit.getServer().getPluginManager().callEvent(new EventStartEvent(Variable.currentEvent));
        }
        if (Variable.currentEvent.isFinished()) {
            Bukkit.getServer().getPluginManager().callEvent(new EventStopEvent(Variable.currentEvent));
            if (Variable.currentEvent.getEventType().equals(EventType.Min5)) {
                Variable.currentEvent = new FactionsEvent(plugin, Variable.currentEvent.getNextGameEvent(), EventType.Min5, EventType.getRandomEvent());
                Bukkit.getServer().getPluginManager().callEvent(new EventStartEvent(Variable.currentEvent));
            } else {
                Variable.currentEvent = new FactionsEvent(plugin, EventType.Min5, Variable.currentEvent.getEventType(), EventType.getRandomEvent());
                Bukkit.getServer().getPluginManager().callEvent(new EventStartEvent(Variable.currentEvent));
            }
        }


        if (Variable.currentEvent.getEventType().equals(EventType.Coliseo)) {
            if (coliseostart == 0L) {
                coliseostart = System.currentTimeMillis();
            }
            if (Variable.ColiseoStarted) {
                if (coliseostats.size() == 1) {
                    for (String na : coliseostats.keySet()) {
                        this.removeFromColiseo(na, "ser el ultimo hombre en pie.");
                    }
                } else if (coliseostats.size() == 0) {
                    Variable.currentEvent.skip();
                }

                for (String s : coliseostats.keySet().toArray(new String[coliseostats.size()])) {
                    if (s != null) {
                        try {
                            if (!isInside(Bukkit.getPlayer(s).getLocation(), Variable.Location1, Variable.Location2)) {
                                this.removeFromColiseo(s, "salir del coliseo.");
                            } else if (Variable.ColiseoAntiAfk.containsKey(s)) {
                                long time = Variable.ColiseoAntiAfk.get(s);
                                long timeLeft = time - System.currentTimeMillis();
                                if (timeLeft < 0) {
                                    this.removeFromColiseo(s, "Estar AFK");
                                } else if (timeLeft > 30000 && timeLeft < 31000) {
                                    Bukkit.getPlayer(s).sendMessage(Code.Color("&eSeras kickeado del coliseo en 30s por estar AFK."));
                                } else if (timeLeft > 15000 && timeLeft < 16000) {
                                    Bukkit.getPlayer(s).sendMessage(Code.Color("&eSeras kickeado del coliseo en 15s por estar AFK."));
                                } else if (timeLeft > 5000 && timeLeft < 6000) {
                                    Bukkit.getPlayer(s).sendMessage(Code.Color("&eSeras kickeado del coliseo en 5s por estar AFK."));
                                }
                            }
                        } catch (Exception e) {
                            coliseostats.remove(s);
                        }
                    }
                }
            } else if (coliseostart + 65000L < System.currentTimeMillis() && Variable.ColiseoStarted == false) {
                Bukkit.broadcastMessage(Code.Color("&c[Evento] &aProteccion PvP Desactivada!, no se pierden items al morir"));

                for (Player p : Bukkit.getWorld("world_the_end").getPlayers()) {
                    if (isInside(p.getLocation(), Variable.Location1, Variable.Location2)) {
                        this.coliseostats.put(p.getName(), 0);
                        Variable.ColiseoAntiAfk.put(p.getName(),System.currentTimeMillis()+120000L);
                        p.sendMessage(Code.Color("&c[Evento] &aEvento ha comenzado, lucha contra los demas y sobrevive!"));
                    }
                }
                Variable.ColiseoStarted = true;
            } else if (coliseostart + 35000L < System.currentTimeMillis() && Variable.ColiseoPhase2 == false) {
                Variable.ColiseoPhase2 = true;
                Bukkit.broadcastMessage(Code.Color("&c[Evento] &a&lColiseo empezando en 30s usa /evento para ir"));
            } else if (coliseostart + 5000L < System.currentTimeMillis() && Variable.ColiseoPhase1 == false) {
                Variable.ColiseoPhase1 = true;
                Bukkit.broadcastMessage(Code.Color("&c[Evento] &a&lColiseo empezando en 60s usa /evento para ir"));
            }


        }
    }

    public boolean isInColiseo(String name){
        return coliseostats.containsKey(name);
    }

    public boolean removeFromColiseo(String name, String reason) {
        if (!coliseostats.containsKey(name)) {
            return false;
        }
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            coliseostats.remove(name);
            return true;
        }
        try {
            player.sendMessage(Code.Color("&c[Evento]&a Posicionaste #" + this.coliseostats.size() + ""));
            if (coliseostats.size() > 10) {
                player.sendMessage(Code.Color("&c[Evento] Recibiste 640 Diamonds"));
                plugin.getDB().getPlayerData(player.getName()).addDiamonds(640);
            } else if (coliseostats.size() > 3) {
                player.sendMessage(Code.Color("&c[Evento] Recibiste 2560 Diamonds"));
                plugin.getDB().getPlayerData(player.getName()).addDiamonds(2560);
            } else if (coliseostats.size() > 1) {
                player.sendMessage(Code.Color("&c[Evento] Recibiste 5120 Diamonds"));
                plugin.getDB().getPlayerData(player.getName()).addDiamonds(5120);
            } else if (coliseostats.size() == 1) {
                Bukkit.broadcastMessage(Code.Color("&c[Evento] &aFelicitaciones &d&l" + player.getName() + "&a por ganar el evento Coliseo!"));
                player.sendMessage(Code.Color("&c[Evento] Recibiste 10240 Diamonds"));
                plugin.getDB().getPlayerData(player.getName()).addDiamonds(10240);
                Variable.currentEvent.skip();
            }

            for (String s : coliseostats.keySet()) {
                if (Bukkit.getPlayer(s) != null) {
                    Bukkit.getPlayer(s).sendMessage(Code.Color("&c[Evento]&b " + player.getName() + " ha salido por " + reason));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.coliseostats.remove(player.getName());
        Location loc = Variable.ColiseoPreviousLocations.get(player.getName());
        if (loc != null) {
            player.teleport(loc);
        }
        Variable.ColiseoPreviousLocations.remove(player.getName());
        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEventDeath(PlayerDeathEvent event) {
        if (getLastEvent() != null && getLastEvent().getEventType() == EventType.Coliseo && removeFromColiseo(event.getEntity().getName(), "ser asesinado.")) {
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onCMDEvento(PlayerCommandPreprocessEvent event) {
        if (this.coliseostart < 1) {
            return;
        }
        if (!Variable.currentEvent.getEventType().equals(EventType.Coliseo)) {
            return;
        }
        if (isInside(event.getPlayer().getLocation(), Variable.Location1, Variable.Location2)) {
            PlayerData pd = plugin.getDB().getPlayerData(event.getPlayer().getName());

            if (pd.getRank().getType().isAtLeast(RankType.Builder) || event.getMessage().startsWith("/f") || event.getMessage().startsWith("/warp") || event.getMessage().startsWith("/home") || event.getMessage().startsWith("/spawn") || event.getMessage().startsWith("/tell") || event.getMessage().startsWith("/msg")) {
                return;
            } else {
                event.getPlayer().sendMessage(Code.Color("&cNo puedes usar comandos en el coliseo, usa /spawn para salir!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onColiseoTeleport(PlayerTeleportEvent event) {
        if (coliseostart < 1 || !Variable.ColiseoStarted) {
            return;
        }
        if (isInside(event.getTo(), Variable.Location1, Variable.Location2)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onColiseoDrop(PlayerDropItemEvent event) {
        if (coliseostart < 1) {
            return;
        }
        if (isInside(event.getPlayer().getLocation(), Variable.Location1, Variable.Location2)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageByEntityEvent event) {
        if (event.getEntity() == null) {
            return;
        }
        if (event.getDamager() == null) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Player && !(event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() != null && ((Projectile) event.getDamager()).getShooter() instanceof Player))) {
            return;
        }

        if (!event.getEntity().getWorld().getName().equals("world_the_end")) {
            return;
        }
        if (!Variable.currentEvent.getEventType().equals(EventType.Coliseo)) {
            return;
        }
        if (Variable.ColiseoStarted) {
            Player p1 = (Player) event.getEntity();
            Player p2 = null;
            if(event.getDamager() instanceof Player){
                p2 = (Player)event.getDamager();
            } else if(event.getDamager() instanceof  Projectile){
                p2 = (Player)((Projectile) event.getDamager()).getShooter();
            }
            if(p2==null){
                return;
            }
            boolean p1t = coliseostats.containsKey(p1.getName());
            boolean p2t = coliseostats.containsKey(p2.getName());

            if (p2t) {
                Variable.ColiseoAntiAfk.put(p2.getName(), System.currentTimeMillis() + 60000L);
            }

            if (!p1t && p2t) {
                if (!this.isInside(p1.getLocation(), Variable.Location1, Variable.Location2)) {
                    p1.sendMessage(Code.Color("&c[Evento]&a Jugadores ajenos al evento no participan."));
                    Location loc = Variable.ColiseoPreviousLocations.get(p1.getName());
                    if (loc != null) {
                        p1.teleport(loc);
                    } else {
                        p1.teleport(new Location(Bukkit.getWorld("world"), 0, 256, 0));
                    }
                    event.setCancelled(true);
                }
            }
            if (!p2t && p1t) {
                if (!this.isInside(p2.getLocation(), Variable.Location1, Variable.Location2)) {
                    p2.sendMessage(Code.Color("&c[Evento]&a Jugadores ajenos al evento no participan."));
                    Location loc = Variable.ColiseoPreviousLocations.get(p2.getName());
                    if (loc != null) {
                        p2.teleport(loc);
                    } else {
                        p2.teleport(new Location(Bukkit.getWorld("world"), 0, 256, 0));
                    }
                    event.setCancelled(true);
                }
            }
        } else {
            if (isInside(event.getEntity().getLocation(), Variable.Location1, Variable.Location2)) {
                event.setCancelled(true);
            }

        }
    }

    public boolean isInside(Location loc, Location l1, Location l2) {
        if(!loc.getWorld().getName().equals(l1.getWorld().getName())){
            return false;
        }
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());

        return loc.getX() >= x1 && loc.getX() <= x2 && loc.getZ() >= z1 && loc.getZ() <= z2;
    }

    @EventHandler
    public void onGameEvent(EventStartEvent event) {
        Variable.currentEvent = event.getLastEvent();
        if (Variable.currentEvent.getEventType().equals(EventType.Min5)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.spigot().sendMessage(Code.toHoverMessage(
                        Code.Color("&c[Evento] " + "&d" + Variable.currentEvent.getEventType().getTitle() + Variable.currentEvent.getNextGameEvent().getTitle()),
                        Code.Color(Variable.currentEvent.getNextGameEvent().getHover())));
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.spigot().sendMessage(Code.toHoverMessage(Code.Color("&c[Evento] " + "&a" + Variable.currentEvent.getEventType().getTitle() + " ha comenzado!"),
                        Code.Color(Variable.currentEvent.getEventType().getHover())));
            }
        }
    }

    LinkedHashMap<String, Integer> mineria = new LinkedHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMineriaEvento(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }
        if (Variable.currentEvent.getEventType() != (EventType.Mineria)) {
            return;
        }

        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            mineriaAddPoint(event.getPlayer(), 5);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.DIAMOND,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.GOLD_ORE) {
            mineriaAddPoint(event.getPlayer(), 4);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.GOLD_INGOT,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.IRON_ORE) {
            mineriaAddPoint(event.getPlayer(), 3);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.IRON_INGOT,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.COAL_ORE) {
            mineriaAddPoint(event.getPlayer(), 2);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.COAL,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.LAPIS_ORE) {
            mineriaAddPoint(event.getPlayer(), 3);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.INK_SACK,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)4));
        } else if (event.getBlock().getType() == Material.REDSTONE_ORE) {
            mineriaAddPoint(event.getPlayer(), 1);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.REDSTONE,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.EMERALD_ORE) {
            mineriaAddPoint(event.getPlayer(), 8);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.EMERALD,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else if (event.getBlock().getType() == Material.QUARTZ_ORE) {
            mineriaAddPoint(event.getPlayer(), 1);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), Code.makeItemStack(Material.QUARTZ,null,new String[]{},Code.getRandom().nextInt(5)+1,(byte)0));
        } else{
            return;
        }
        event.getBlock().setType(Material.AIR, true);
        event.setCancelled(true);

    }

    public void handleRewards() {
        if (Variable.RewardsSended == true) {
            return;
        }
        if (Variable.currentEvent.getEventType().equals(EventType.Mineria)) {
            this.mineria = (LinkedHashMap) Code.sortByValue((Map) mineria);
            int pos = 0;
            for (String s : mineria.keySet()) {
                if (Bukkit.getPlayer(s) != null) {
                    pos++;
                    Player p = Bukkit.getPlayer(s);
                    p.sendMessage(Code.Color("&c[Evento] &aHas acumulado " + mineria.get(s) + " puntos"));
                    if (mineria.get(s) > 10) {
                        int diamondreward = (int) (mineria.get(s) / 10);
                        if (pos == 1) {
                            p.sendMessage(Code.Color("&c[Evento]&aPosicionaste 1ro, recibes premios extra!"));
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 200));
                            diamondreward += 2000;
                        } else if (pos == 2) {
                            p.sendMessage(Code.Color("&c[Evento]&aPosicionaste 2do, recibes premios extra!"));
                            diamondreward += 1500;
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 150));
                        } else if (pos == 3) {
                            p.sendMessage(Code.Color("&c[Evento]&aPosicionaste 3ro, recibes premios extra!"));
                            diamondreward += 1000;
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 100));
                        } else if (pos > 3 && pos < 50) {
                            p.sendMessage(Code.Color("&c[Evento]&aPosicionaste " + pos + "th, recibes premios extra!"));
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 50));
                            diamondreward += 500;
                        }
                        this.sendBankDiamonds(Bukkit.getPlayer(s), diamondreward);

                        Bukkit.getPlayer(s).sendMessage(Code.Color("&c[Evento]&aRecibiste " + diamondreward + " diamantes"));

                    } else {
                        Bukkit.getPlayer(s).sendMessage(Code.Color("&c[Evento]&3Has minado muy pocos minerales, no calificas para recibir premios"));
                    }
                }
            }
        }
        Variable.RewardsSended = true;
        System.out.println("Sending rewards of " + Variable.currentEvent.getEventType().getName() + " event");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (this.coliseostats.containsKey(event.getPlayer().getName())) {
            this.removeFromColiseo(event.getPlayer().getName(), "desconectarse del server.");
        }
    }

    public void sendBankDiamonds(Player player, int diamonds) {
        plugin.getDB().getPlayerData(player.getName()).addDiamonds(diamonds);
    }

    public void mineriaAddPoint(Player p, Integer pts) {
        if (!mineria.containsKey(p.getName())) {
            mineria.put(p.getName(), 0);
        }
        int limit = mineria.get(p.getName());
        if (limit > 5000) {
            p.sendMessage("Llegaste al limite de 5000 puntos en el evento.");
            return;
        }
        mineria.put(p.getName(), limit + pts);
        p.sendMessage(Code.Color("&c[Evento]&aSumaste " + pts + " Puntos en Mineria,&e Total: " + mineria.get(p.getName())));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(getLastEvent() != null && getLastEvent().getEventType() == EventType.Coliseo && !hasColiseoStarted() && isInColiseo(event.getEntity().getName())){
            event.setCancelled(true);
        }
    }

    public static boolean hasColiseoStarted() {
        return Variable.ColiseoStarted;
    }

}
