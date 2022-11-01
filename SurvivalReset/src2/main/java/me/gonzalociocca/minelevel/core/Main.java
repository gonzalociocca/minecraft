/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.minelevel.core;

/**
 *
 * @author ciocca
 */

import me.gonzalociocca.minelevel.core.mysql.MySQL;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.updater.UpdateType;
import me.gonzalociocca.minelevel.core.updater.Updater;
import net.elseland.xikage.MythicMobs.API.Exceptions.InvalidMobTypeException;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import net.elseland.xikage.MythicMobs.Mobs.MobManager;
import net.elseland.xikage.MythicMobs.MythicMobs;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.FallingSand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.syntax.pr.cmds.OpenMenu;
import org.syntax.pr.cmds.ReportPlayer;
import org.syntax.pr.listeners.ClickAdminMenu;
import org.syntax.pr.listeners.ClickOnMainMenu;
import org.syntax.pr.listeners.ClickOnMessageMenu;
import org.syntax.pr.listeners.ClickOnReportMenu;
import org.syntax.pr.reports.ReportManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends JavaPlugin implements Listener
{
	Main plugin = null;
    HashMap<String,PermissionAttachment> attachments = new HashMap();

    Long starttime;

    File messageYML;
    FileConfiguration messageConfig;
    net.elseland.xikage.MythicMobs.MythicMobs mythic;
    Location loc1 = null;
    Location loc2 = null;
    Location loc3 = null;
    Updater upd;

	@Override
	public void onEnable()
	{

        starttime = System.currentTimeMillis();
        plugin = this;
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, this);
        upd = new Updater(this);


        try {
        this.messageYML = new File(getDataFolder(), "/messages.yml");
        if (!this.messageYML.exists()) {
            try {
                this.messageYML.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.messageConfig = YamlConfiguration.loadConfiguration(this.messageYML);

        getServer().getPluginManager().registerEvents(new ClickOnMainMenu(), this);
        getServer().getPluginManager().registerEvents(new ClickOnReportMenu(), this);
        getServer().getPluginManager().registerEvents(new ClickAdminMenu(), this);
        getServer().getPluginManager().registerEvents(new ClickOnMessageMenu(), this);

        getCommand("reports").setExecutor(new OpenMenu(this));
        getCommand("report").setExecutor(new ReportPlayer(this));

        ArrayList<String> lores = new ArrayList();
        lores.add("&4Acciones reportadas estan justificadas.");
        lores.add("&bJugador reportado ha sido castido.");
        lores.add("&fHas sido avisado, no hagas eso devuelta.");
        lores.add("&6Reporte ha sido observado por un Staff.");
        if (!getMessagesConfig().contains("Messages")) {
            getMessagesConfig().set("Messages", lores);
        }
        saveMessagesConfig();
        if (!getConfig().contains("Reports.DONOTDELETE")) {
            getConfig().set("Reports.DONOTDELETE", Boolean.valueOf(true));
        }
        if (!getConfig().contains("CustomMessage.ReportsHelp")) {
            getConfig().set("CustomMessage.ReportsHelp", "&bUso: /reports");
        }
        if (!getConfig().contains("CustomMessage.ReportHelp")) {
            getConfig().set("CustomMessage.ReportHelp", "&bUso: /report <nombre-del-jugador> <razon>");
        }
        saveConfig();

        ReportManager.getInstance().setup(this);
        ReportManager.getInstance().loadAll();

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "-> Reportes han sido habilitados!");
    }catch(Exception e){
        e.printStackTrace();
    }

        this.init();

        Iterator<Recipe> s = Bukkit.getServer().recipeIterator();


        while(s.hasNext()) {
            Recipe s2= s.next();
            if (s2 != null && s2.getResult().getType().equals(Material.GOLDEN_APPLE)) {
                s.remove();
            }
        }


        ItemStack goldenapple = new ItemStack(Material.GOLDEN_APPLE,1,(short)1);
        ShapedRecipe sp = new ShapedRecipe(goldenapple);
        sp.shape("AAA","ABA","AAA");
        sp.setIngredient('A',Material.GOLD_INGOT);
        sp.setIngredient('B',Material.APPLE);
        Bukkit.getServer().addRecipe(sp);

        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE,1);
        ShapedRecipe sp2 = new ShapedRecipe(gapple);
        sp2.shape("AAA","ABA","AAA");
        sp2.setIngredient('A',Material.GOLD_NUGGET);
        sp2.setIngredient('B',Material.APPLE);
        Bukkit.getServer().addRecipe(sp2);

        ItemStack goldenapples = new ItemStack(Material.GOLDEN_APPLE,9,(short)1);
        ShapedRecipe sp3 = new ShapedRecipe(goldenapples);
        sp3.shape("AAA","ABA","AAA");
        sp3.setIngredient('A',Material.GOLD_BLOCK);
        sp3.setIngredient('B',Material.APPLE);
        Bukkit.getServer().addRecipe(sp3);

        loc1 = new Location(Bukkit.getWorld("world_the_end"),-261,23,-70);
        loc2 = new Location(Bukkit.getWorld("world_the_end"),-180,60,-149);
        loc3 = new Location(Bukkit.getWorld("world_the_end"),-220,30,-111);
	}

	@Override
    public void onDisable()
    {
        ReportManager.getInstance().saveAll();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "-> Reportes guardados!");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHDApples(PlayerItemConsumeEvent event) {
        if(event.isCancelled()){
            return;
        }
        if(event.getItem() == null){
            return;
        }
        if(event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1){
            event.setCancelled(true);

            Player p = event.getPlayer();
            p.sendMessage(Colorizer.Color("&aComida!"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*60,3,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*120,3,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*300,3,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*300,3,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*45,3,true,true), true);
            ItemStack it1 = p.getInventory().getItemInHand();
            if(it1 != null) {
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
        }else if(event.getItem().getType() == Material.GOLDEN_APPLE){
            event.setCancelled(true);

            Player p = event.getPlayer();
            p.sendMessage(Colorizer.Color("&aComida!"));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*45,1,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*120,1,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20*300,0,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,20*300,0,true,true), true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,20*45,1,true,true), true);
            ItemStack it1 = p.getInventory().getItemInHand();
            if(it1 != null) {
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
    }

    public FileConfiguration getMessagesConfig()
    {
        return this.messageConfig;
    }

    public MythicMobs getMythic(){
        if(this.mythic == null){
            mythic = net.elseland.xikage.MythicMobs.MythicMobs.inst();
        }
        //this.getMythic().getAPI().getMobAPI().spawnMythicMob()
        return mythic;
    }

    public void saveMessagesConfig()
    {
        try
        {
            this.messageConfig.save(this.messageYML);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

	EventManager lastEvent;

    boolean coliseoStarted = false;
    boolean coliseoMSG = false;
    boolean coliseoMSG2 = false;
    Long coliseostart = 0L;
    ConcurrentHashMap<String,Integer> coliseostats = new ConcurrentHashMap();


	@EventHandler
    public void onEvent(UpdateEvent event) throws InvalidMobTypeException {
        if(event.getType().equals(UpdateType.MIN1y2)) {
            Bukkit.broadcastMessage(Colorizer.Color("&c[MineLevel]&a Eres nuevo? usa /kit guias para mas informacion."));
        }
        else if(event.getType().equals(UpdateType.MIN1)){
            Bukkit.broadcastMessage(Colorizer.Color("&c[MineLevel]&a Consigue rangos en http://www.minelevel.com/donaciones y apoya al server!"));
        }
        if(event.getType() != UpdateType.SEC){
            return;
        }
        if(lastEvent == null){
            this.getServer().getPluginManager().callEvent(new EventManager(this,eventType.Min30, eventType.Min30,eventType.Mineria));
        }

        if(lastEvent.isFinished()){
            this.handleRewards();
            this.rewardsSended=false;
            if(lastEvent.getEventType().equals(eventType.Min30)){
                this.getServer().getPluginManager().callEvent(new EventManager(this,lastEvent.getNextGameEvent(),eventType.Min30,lastEvent.getNextGameEvent()));
            }
            else if(lastEvent.getEventType().equals(eventType.Invasion)){
                this.getServer().getPluginManager().callEvent(new EventManager(this,eventType.Min30,eventType.Invasion,eventType.Coliseo));
            }
            else if(lastEvent.getEventType().equals(eventType.Coliseo)){
                coliseostart = 0L;
                coliseoStarted=false;
                coliseoMSG=false;
                coliseoMSG2=false;
                if(!coliseostats.isEmpty()){
                    coliseostats.clear();
                }
                this.getServer().getPluginManager().callEvent(new EventManager(this,eventType.Min30,eventType.Coliseo,eventType.Mineria));
                //Remove last event mobs!
                MobManager.removeAllMobs();
            }
            else if(lastEvent.getEventType().equals(eventType.Mineria)){
                this.getServer().getPluginManager().callEvent(new EventManager(this,eventType.Min30,eventType.Mineria,eventType.Invasion));
            }
        }
        else if(lastEvent.getEventType().equals(eventType.Invasion) && lastEvent.canOleada()){

            for(World world : Bukkit.getWorlds()){

                if(world.getEnvironment().equals(World.Environment.NORMAL)){
                    int rx = r2.nextInt(1500);
                    int rz = r2.nextInt(1500);
                    if(rx < 100){rx = -rx;}
                    if(rz < 100){rz = -rz;}

                    Location loc = world.getSpawnLocation().add(rx,0,rz);

                    loc.setY(world.getHighestBlockYAt(loc)+1);
                    try{
                        for(Player p : world.getPlayers()){
                        p.sendMessage(Colorizer.Color("&c[Evento] &aOla de Bosses en X: "+loc.getX()+" Y: "+loc.getY()+" Z: "+loc.getZ()));}

                        this.getMythic().getAPI().getMobAPI().spawnMythicMob("normal0",loc,1);
                        this.getMythic().getAPI().getMobAPI().spawnMythicMob("normal1",loc,1);
                        this.getMythic().getAPI().getMobAPI().spawnMythicMob("normal2",loc,1);
                        this.getMythic().getAPI().getMobAPI().spawnMythicMob("normal3",loc,1);
                        this.getMythic().getAPI().getMobAPI().spawnMythicMob("normal4",loc,1);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                }

        }else if(lastEvent.getEventType().equals(eventType.Coliseo)){
            if(coliseostart==0L){
                coliseostart = System.currentTimeMillis();
            }
            if(coliseoStarted){
                if(coliseostats.size() == 1){
                    for(String na : coliseostats.keySet()){
                    this.removeFromColiseo(na,"ser el ultimo hombre en pie.");
                }}else if(coliseostats.size()==0){
                    lastEvent.skip();
                }
             for(String s : this.coliseostats.keySet()){
                 try{
                 if(!isInside(Bukkit.getPlayer(s).getLocation(),loc1,loc2)){
                     this.removeFromColiseo(s,"salir del coliseo.");
                 }}catch(Exception e){coliseostats.remove(s);}

             }
            }
            else if(coliseostart+65000L < System.currentTimeMillis() && coliseoStarted == false){
                Bukkit.broadcastMessage(Colorizer.Color("&c[Evento] &aProteccion PvP Desactivada!, no se pierden items al morir"));

                for(Player p : Bukkit.getWorld("world_the_end").getPlayers()){
                    if(isInside(p.getLocation(),loc1,loc2)){
                        this.coliseostats.put(p.getName(),0);
                        p.sendMessage(Colorizer.Color("&c[Evento] &aEvento ha comenzado, lucha contra los demas y sobrevive!"));
                    }
                }
            coliseoStarted=true;
            }
            else if(coliseostart+35000L < System.currentTimeMillis() && coliseoMSG2 == false){
                this.coliseoMSG2 = true;
                Bukkit.broadcastMessage(Colorizer.Color("&c[Evento] &a&lColiseo empezando en 30s usa /evento para ir"));
            }
            else if(coliseostart+5000L < System.currentTimeMillis() && coliseoMSG == false){
                this.coliseoMSG = true;
                Bukkit.broadcastMessage(Colorizer.Color("&c[Evento] &a&lColiseo empezando en 60s usa /evento para ir"));
            }


        }
    }
public boolean removeFromColiseo(String name, String reason){
    if(!coliseostats.containsKey(name)){
        return false;
    }
    Player player = Bukkit.getPlayer(name);
    if(player==null){
        coliseostats.remove(name);
        return true;
    }
            //.setKeepInventory(true);
    try{player.sendMessage(Colorizer.Color("&c[Evento]&a Posicionaste #"+this.coliseostats.size()+""));
            if(coliseostats.size() > 10){
                player.sendMessage(Colorizer.Color("&c[Evento] Recibiste 64 Diamonds"));
                this.getPlayerData(player.getName()).addDiamonds(64);
            }else if(coliseostats.size() > 3){
                player.sendMessage(Colorizer.Color("&c[Evento] Recibiste 256 Diamonds"));
                this.getPlayerData(player.getName()).addDiamonds(256);
            }else if(coliseostats.size() > 1){
                player.sendMessage(Colorizer.Color("&c[Evento] Recibiste 512 Diamonds"));
                this.getPlayerData(player.getName()).addDiamonds(512);
            }else if(coliseostats.size()==1){
                Bukkit.broadcastMessage(Colorizer.Color("&c[Evento] &aFelicitaciones &d&l"+player.getName()+"&a por ganar el evento Coliseo!"));
                player.sendMessage(Colorizer.Color("&c[Evento] Recibiste 1024 Diamonds"));
                this.getPlayerData(player.getName()).addDiamonds(1024);
                this.lastEvent.skip();
            }

                for(String s : coliseostats.keySet()){
                    Bukkit.getPlayer(s).sendMessage(Colorizer.Color("&c[Evento]&b "+player.getName()+" ha salido por "+reason));
                }
            }catch(Exception e){e.printStackTrace();}

    this.coliseostats.remove(player.getName());

            return true;
}

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEventDeath(PlayerDeathEvent event){
        if(this.coliseostart <= 0){
            return;
        }
        if(removeFromColiseo(event.getEntity().getName(),"ser asesinado.")){
            event.setKeepInventory(true);
        }
    }
    @EventHandler
    public void onCMDEvento(PlayerCommandPreprocessEvent event){
        if(this.coliseostart <= 0){
            return;
        }
        if(!lastEvent.getEventType().equals(eventType.Coliseo)){
            return;
        }
        if(isInside(event.getPlayer().getLocation(),loc1,loc2)){
            if(event.getMessage().startsWith("/set") || event.getMessage().startsWith("/eset")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if(!(event.getEntity() instanceof Player)){
            event.setDroppedExp(event.getDroppedExp()*3);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPvP(EntityDamageByEntityEvent event){
        if(event.getEntity()==null){
            return;
        }
        if(event.getDamager()==null){
            return;
        }
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        ((EntityPlayer)((CraftEntity)event.getEntity()).getHandle()).getAttributeInstance(GenericAttributes.c).setValue(0.33D);

        if(!event.getEntity().getWorld().getName().equals("world_the_end")){
            return;
        }
        if(!lastEvent.getEventType().equals(eventType.Coliseo)){
            return;
        }
        if(coliseoStarted){
            Player p1 = (Player)event.getEntity();
            Player p2 = (Player)event.getDamager();
            boolean p1t=false;
            boolean p2t=false;
            for(String s : this.coliseostats.keySet()){
                if(s==p1.getName()){
                    p1t=true;
                }else if(s==p2.getName()){
                    p2t=true;
                }
            }
            if(!p1t && p2t){
                if(!this.isInside(p1.getLocation(),loc1,loc2)){
                p1.sendMessage(Colorizer.Color("&c[Evento]&a Jugadores ajenos al evento no participan."));
                p1.teleport(new Location(Bukkit.getWorld("world"),0,256,0));
                event.setCancelled(true);}
            }
            if(!p2t && p1t){
                if(!this.isInside(p2.getLocation(),loc1,loc2)){
                p2.sendMessage(Colorizer.Color("&c[Evento]&a Jugadores ajenos al evento no participan."));
                p2.teleport(new Location(Bukkit.getWorld("world"),0,256,0));
                event.setCancelled(true);            }
        }}else {
            if (isInside(event.getEntity().getLocation(), loc1, loc2)) {
                event.setCancelled(true);
            }

        }
    }

    public boolean isInside(Location loc, Location l1, Location l2) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());

        return loc.getX() >= x1 && loc.getX() <= x2 && loc.getZ() >= z1 && loc.getZ() <= z2;
    }

    @EventHandler
    public void onGameEvent(EventManager event){
        lastEvent = event;
        if(event.getEventType().equals(eventType.Min30)){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.spigot().sendMessage(this.hovermsg(Colorizer.Color("&c[Evento] "+"&d"+event.getEventType().getTitle()+ event.getNextGameEvent().getTitle()),
                        Colorizer.Color(event.getNextGameEvent().getHover())));
            }
        }else{
            for(Player p : Bukkit.getOnlinePlayers()){
                p.spigot().sendMessage(this.hovermsg(Colorizer.Color("&c[Evento] "+"&a"+event.getEventType().getTitle()+" ha comenzado!"),
                            Colorizer.Color(event.getEventType().getHover())));
        }
    }
    }

    LinkedHashMap<String,Integer> mineria = new LinkedHashMap<>();

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onMineriaEvento(BlockBreakEvent event){
        if(event.getPlayer()==null){
            return;
        }
        if(!this.lastEvent.getEventType().equals(eventType.Mineria)){
            return;
        }
        if(event.isCancelled()){
            return;
        }
                if(event.getBlock().getType() == Material.DIAMOND_ORE){
                    mineriaAddPoint(event.getPlayer(),5);
                }
                else if(event.getBlock().getType() == Material.GOLD_ORE){
                    mineriaAddPoint(event.getPlayer(),4);
                }
                else if(event.getBlock().getType() == Material.IRON_ORE){
                    mineriaAddPoint(event.getPlayer(),3);
                }
                else if(event.getBlock().getType() == Material.COAL_ORE){
                    mineriaAddPoint(event.getPlayer(),2);
                }
                else if(event.getBlock().getType() == Material.LAPIS_ORE){
                    mineriaAddPoint(event.getPlayer(),3);
                }
                else if(event.getBlock().getType() == Material.REDSTONE_ORE){
                    mineriaAddPoint(event.getPlayer(),1);
                }
                else if(event.getBlock().getType() == Material.EMERALD_ORE){
                    mineriaAddPoint(event.getPlayer(),8);
                }
                else if(event.getBlock().getType() == Material.QUARTZ_ORE){
                    mineriaAddPoint(event.getPlayer(),1);

                }}

    boolean rewardsSended=false;

    public TextComponent hovermsg(String str, String hovermsg){
        TextComponent tc = new TextComponent(str);
        tc.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovermsg).create() ) );

        return tc;
    }

    public void handleRewards(){
        if(rewardsSended==true){
            return;
        }
        if(lastEvent.getEventType().equals(eventType.Mineria)){
            this.mineria = (LinkedHashMap)this.sortByValue((Map)mineria);
            int pos = 0;
            for(String s : mineria.keySet()){
                if(Bukkit.getPlayer(s) != null){
                    pos++;
                    Player p = Bukkit.getPlayer(s);
                    p.sendMessage(Colorizer.Color("&c[Evento] &aHas acumulado "+mineria.get(s)+" puntos"));
                    if(mineria.get(s) > 10){
                        int diamondreward = (int)(mineria.get(s) / 10);
                        if(pos==1){
                            p.sendMessage(Colorizer.Color("&c[Evento]&aPosicionaste 1ro, recibes premios extra!"));
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE,200));
                            diamondreward+=200;
                        }
                        else if(pos==2){
                            p.sendMessage(Colorizer.Color("&c[Evento]&aPosicionaste 2do, recibes premios extra!"));
                            diamondreward+=150;
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE,150));
                        }
                        else if(pos==3){
                            p.sendMessage(Colorizer.Color("&c[Evento]&aPosicionaste 3ro, recibes premios extra!"));
                            diamondreward+=100;
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE,100));
                        }
                        else if(pos> 3 && pos < 50){
                            p.sendMessage(Colorizer.Color("&c[Evento]&aPosicionaste "+pos+"th, recibes premios extra!"));
                            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE,50));
                            diamondreward+=50;
                        }
                        this.sendBankDiamonds(Bukkit.getPlayer(s),diamondreward);

                        Bukkit.getPlayer(s).sendMessage(Colorizer.Color("&c[Evento]&aRecibiste "+diamondreward+" diamantes"));

                    }else{
                        Bukkit.getPlayer(s).sendMessage(Colorizer.Color("&c[Evento]&3Has minado muy pocos minerales, no calificas para recibir premios"));
                    }
                }
            }
                    }
        rewardsSended=true;
        System.out.println("Sending rewards of "+this.lastEvent.getEventType().getName()+" event");
    }

    public void sendBankDiamonds(Player player, int diamonds){
        this.getPlayerData(player.getName()).addDiamonds(diamonds);
    }

    public void mineriaAddPoint(Player p,Integer pts){
        if(!mineria.containsKey(p.getName())){
            mineria.put(p.getName(),0);
        }
        mineria.put(p.getName(),mineria.get(p.getName())+pts);
        p.sendMessage(Colorizer.Color("&c[Evento]&aSumaste "+pts+" Puntos en Mineria,&e Total: "+mineria.get(p.getName())));
    }

    private Object getField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);

            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }        return null;
    }
    private ItemStack addGlow(org.bukkit.inventory.ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = (net.minecraft.server.v1_8_R3.ItemStack) getField(CraftItemStack.asCraftCopy(stack), "handle");
        // Initialize the compound if we need to
        if (!nmsStack.hasTag()) {
            nmsStack.setTag(new NBTTagCompound());
        }
        NBTTagCompound compound = nmsStack.getTag();

        // Empty enchanting compound
        compound.set("ench", new NBTTagList());

        return CraftItemStack.asCraftMirror(nmsStack);
    }

@EventHandler
public void onStatus(ServerListPingEvent event){
    Long day = 86400000L;
    Long hour = 3600000L;
    Long min = 60000L;
    Long end = 1476154799000L - System.currentTimeMillis();

    int days = (int)(end / day);
    end = end%day;

    int hours = (int) (end / hour);
    end = end%hour;
    int mins = (int) (end / min);
    end = end%min;

    int secs = 0;
    if(end > 1000L){
    secs = (int) (end / 1000L);
    end = end%secs;}

    event.setMotd(Colorizer.Color("&f»&2&m----&a&m----&f&m----&f» &2&lMine&a&lLevel &f«&m----&a&m----&2&m----&f« \n&a&lSurvival+Factions! &f&l+&a&l Nuevo McMMO!"));
}


HashMap<String,Long> compasslimit = new HashMap();

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(event.getPlayer().getItemInHand() != null){
                if(event.getPlayer().getItemInHand().getType().equals(Material.COMPASS)){
                    if(event.getPlayer().getWorld().getName().equalsIgnoreCase("world")){
                        if(!compasslimit.containsKey(event.getPlayer().getName())){
                            compasslimit.put(event.getPlayer().getName(),0L);
                        }
                        if(compasslimit.get(event.getPlayer().getName())> System.currentTimeMillis()){
                            event.getPlayer().sendMessage(Colorizer.Color("&aSolo puedes moverte cada 5segundos"));
                            return;
                        }
                        int x = r2.nextInt(30000);
                        int z = r2.nextInt(30000);
                        int y = event.getPlayer().getWorld().getHighestBlockYAt(x,z)+1;
                        event.getPlayer().teleport(new Location(event.getPlayer().getWorld(),x,y,z));
                        event.getPlayer().sendMessage(Colorizer.Color("&aMovido a X: "+x+" Y: "+y+" Z: "+z));
                            compasslimit.put(event.getPlayer().getName(),System.currentTimeMillis()+5000);
                    }
                }
            }
        }
        /*
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getInventory().getItemInHand().getType().equals(Material.CHEST) || event.getPlayer().getInventory().getItemInHand().getType().equals(Material.TRAPPED_CHEST)){
                BlockPlaceEvent evt = new BlockPlaceEvent(event.getClickedBlock().getRelative(event.getBlockFace()),event.getClickedBlock().getState(),event.getClickedBlock(),event.getPlayer().getInventory().getItemInHand(),event.getPlayer(),true);
                Bukkit.getServer().getPluginManager().callEvent(evt);
                if(evt.isCancelled()){
                    event.setCancelled(true);
                }else{

                    if(event.getPlayer().getInventory().getItemInHand().getAmount() > 1){
                        event.getPlayer().getInventory().getItemInHand().setAmount(event.getPlayer().getInventory().getItemInHand().getAmount()-1);
                    }else{
                        event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                    }
                }
            }
        }*/
    }
    /*
    @EventHandler(priority = EventPriority.NORMAL)
    public void EntityChangeBlockEvent (EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof FallingBlock || event.getEntity() instanceof FallingSand){
            if(event.getBlock().getType().name().toLowerCase().contains("chest") || event.getBlock().getType().name().toLowerCase().contains("sign")){
                event.setCancelled(true);
            }
        }
    }*/

@EventHandler(priority=EventPriority.MONITOR)
public void onPrivatizar(BlockPlaceEvent event){
    if(event.isCancelled()){
        return;
    }
    if(event.getItemInHand() == null){
        return;
    }
    if(!event.getItemInHand().getType().equals(Material.CHEST)){
        return;
    }
    if(!event.canBuild()){
        event.setCancelled(true);
        return;
    }

    Player pe = event.getPlayer();
    if(this.autoprivados.get(pe.getName())) {
        BlockFace signface = BlockFace.EAST;
        Block bsign = event.getBlockPlaced().getRelative(BlockFace.EAST);
        if (!bsign.isEmpty()) {
            signface = BlockFace.WEST;
            bsign = event.getBlockPlaced().getRelative(BlockFace.WEST);
        }
        if (!bsign.isEmpty()) {
            signface = BlockFace.NORTH;
            bsign = event.getBlockPlaced().getRelative(BlockFace.NORTH);
        }
        if (!bsign.isEmpty()) {
            signface = BlockFace.SOUTH;
            bsign = event.getBlockPlaced().getRelative(BlockFace.SOUTH);
        }
        if(bsign.isEmpty()){


        int a = -1;
        if(signface.equals(BlockFace.EAST)){
            a = 5;
        }
        if(signface.equals(BlockFace.WEST)){
            a = 4;
        }
        if(signface.equals(BlockFace.SOUTH)){
            a = 3;
        }
        if(signface.equals(BlockFace.NORTH)){
            a = 2;
        }
            try{
                if(!event.getBlockPlaced().getType().equals(Material.CHEST)){
                    return;
                }
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
        bsign.setTypeIdAndData(Material.WALL_SIGN.getId(),(byte)a,true);

        Sign se = (Sign) bsign.getState();

        se.setLine(0, "[Private]");
        se.setLine(1, pe.getName());
        se.update();
            pe.sendMessage(Colorizer.Color("&c[MineLevel]&2 Cofre privatizado automaticamente, usa /auto para modo manual"));
        }
    }
}


    Random r2 = new Random();

 @EventHandler(ignoreCancelled = true)
 public void onMove(final InventoryMoveItemEvent event){
     if(event.getSource() != null){
if(event.getDestination()==null){
    return;
}
event.setCancelled(true);
getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
@Override
public void run() {

             Bukkit.getPluginManager().callEvent(new CustomHopperEvent(event.getSource(),event.getDestination()));

}
}, 1L);
     }

 }


    String host = "127.0.0.1";
    String port = "3306";
    String db = "minelevel";
    String user = "root";
    String pwd = "notevinomas123-";
    String table = "data";

    boolean enabled = true;
    java.sql.Connection data = null;


    @EventHandler(priority= EventPriority.HIGHEST)
    public void beforeJoin(PlayerLoginEvent event){
        this.insert(event.getPlayer().getName(),event.getPlayer().getUniqueId().toString());
    }


    //CMD  0      1     2      3
    //mine addrank vip HappyBear 3

    public void openBanco(Player p){
        Inventory iv = Bukkit.createInventory(null,9,Colorizer.Color("&cOpciones del Banco."));
        ItemStack extraer = new ItemStack(Material.SULPHUR);
        ItemMeta im = extraer.getItemMeta();
        im.setDisplayName(Colorizer.Color("&cExtraer Diamantes"));
        im.setLore(Arrays.asList("",Colorizer.Color("&aClickealo para abrir el menu de extracciones")));
        extraer.setItemMeta(im);

        ItemStack depositar = new ItemStack(Material.DIAMOND);
        ItemMeta im2 = depositar.getItemMeta();
        im2.setDisplayName(Colorizer.Color("&cDepositar Diamantes"));
        im2.setLore(Arrays.asList("",Colorizer.Color("&aClickealo para abrir el menu de depositos")));
        depositar.setItemMeta(im2);

        ItemStack canjear = new ItemStack(Material.NETHER_STAR);
        ItemMeta im3 = canjear.getItemMeta();
        im3.setDisplayName(Colorizer.Color("&cCanjear Diamantes"));
        im3.setLore(Arrays.asList("",Colorizer.Color("&aClickealo para abrir el menu de canjes")));
        canjear.setItemMeta(im3);

        iv.setItem(3,extraer);
        iv.setItem(4,depositar);
        iv.setItem(5,canjear);

        p.openInventory(iv);

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
            event.setDamage(0);
        }
    }

    HashMap<String,Long> limitbank = new HashMap();

    @EventHandler
    public void onBanco(InventoryClickEvent event){
        if(event.getClickedInventory()==null){
            return;
        }
        if(event.getClickedInventory().getName().equals(null)){
            return;
        }
        if(event.getCurrentItem()==null){
            return;
        }
        if(!event.getCurrentItem().hasItemMeta()){
            return;
        }

        if(event.getClickedInventory().getName().equals(Colorizer.Color("&cOpciones del Banco."))){
            if(!limitbank.containsKey(event.getWhoClicked().getName())){
                limitbank.put(event.getWhoClicked().getName(),0L);
            }
            if(limitbank.get(event.getWhoClicked().getName()) > System.currentTimeMillis()){
                event.getWhoClicked().sendMessage(Colorizer.Color("&cEspera 1 segundo antes de clickear"));
                event.setCancelled(true);
                return;
            }else{
                limitbank.put(event.getWhoClicked().getName(),System.currentTimeMillis()+750L);
            }
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().hasLore()){
                if(event.getCurrentItem().getItemMeta().getLore().contains(Colorizer.Color("&aPrecio: &b&l"+100+"&b Diamantes"))){
                    int a = 100;
                    if(this.getPlayerData(event.getWhoClicked().getName()).getDiamonds() < a){
                        event.getWhoClicked().sendMessage(Colorizer.Color("&cDiamantes en el banco insuficientes."));
                        return;
                    }
                    int d = 1;
                    int en = 0;
                    for(ItemStack it : event.getWhoClicked().getInventory().getContents()){
                        if(it == null){
                            en++;
                        }
                    }
                    if(a > 64){
                        d = d+ a/64;
                    }
                    if(en < d){
                        event.getWhoClicked().sendMessage(Colorizer.Color("&cEspacio insuficiente para guardar las runas"));
                        return;
                    }

                    if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Proteccion")){
                        event.getWhoClicked().getInventory().addItem(this.getRuna(0));
                        event.getWhoClicked().sendMessage(Colorizer.Color("&aRecibiste &f&lRuna de Proteccion"));
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Magica")){
                        event.getWhoClicked().getInventory().addItem(this.getRuna(1));
                        event.getWhoClicked().sendMessage(Colorizer.Color("&aRecibiste &d&lRuna Magica"));
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Maldita")){
                        event.getWhoClicked().getInventory().addItem(this.getRuna(2));
                        event.getWhoClicked().sendMessage(Colorizer.Color("&aRecibiste &c&lRuna Maldita"));
                    }else{
                        return;
                    }
                    this.getPlayerData(event.getWhoClicked().getName()).removeDiamonds(a);
                    event.getWhoClicked().sendMessage(Colorizer.Color("&c"+a+" extraidos del banco."));
                }
              else if(event.getSlot()==3){
                  for(int as = 3;as<6;as++){
                      ItemStack it = new ItemStack(Material.SULPHUR);
                      ItemMeta im = it.getItemMeta();
                      im.setDisplayName(Colorizer.Color("&aExtraer "+(32*as)+" Diamantes"));
                      it.setItemMeta(im);
                      event.getClickedInventory().setItem(as,it);
                  }
            }else if(event.getSlot()==4){
                  for(int as = 3;as<6;as++){
                      ItemStack it = new ItemStack(Material.DIAMOND);
                      ItemMeta im = it.getItemMeta();
                      im.setDisplayName(Colorizer.Color("&aDepositar "+(32*as)+" Diamantes"));
                      it.setItemMeta(im);
                      event.getClickedInventory().setItem(as,it);
                  }
            }else if(event.getSlot()==5){
                  ItemStack it1 = this.getRuna(0);
                  ItemMeta im = it1.getItemMeta();
                  im.setLore(Arrays.asList("",Colorizer.Color("&aPrecio: &b&l"+100+"&b Diamantes")));
                  it1.setItemMeta(im);
                  event.getClickedInventory().setItem(3,it1.clone());

                  it1 = this.getRuna(1);
                  im = it1.getItemMeta();
                  im.setLore(Arrays.asList("",Colorizer.Color("&aPrecio: &b&l"+100+"&b Diamantes")));
                  it1.setItemMeta(im);
                  event.getClickedInventory().setItem(4,it1.clone());

                  it1 = this.getRuna(2);
                  im = it1.getItemMeta();
                  im.setLore(Arrays.asList("",Colorizer.Color("&aPrecio: &b&l"+100+"&b Diamantes")));
                  it1.setItemMeta(im);
                  event.getClickedInventory().setItem(5,it1.clone());

                  event.setCancelled(true);

            }}else{
                if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Depositar")){
                    int a = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
                    int d = 0;
                    if(event.getWhoClicked().getInventory().contains(Material.DIAMOND)){
                        HashMap<Integer,? extends ItemStack> its = event.getWhoClicked().getInventory().all(Material.DIAMOND);
                        for(Integer in : its.keySet()){
                            if(a <= 0){
                                break;
                            }
                            ItemStack it = event.getWhoClicked().getInventory().getItem(in);
                            if(it.getAmount() > a){
                            }else{
                                a -= it.getAmount();
                                d += it.getAmount();
                                event.getWhoClicked().getInventory().setItem(in, new ItemStack(Material.AIR));
                            }
                        }

                        event.getWhoClicked().sendMessage(Colorizer.Color("&c"+d+" diamantes guardados en el banco."));
                        this.getPlayerData(event.getWhoClicked().getName()).addDiamonds(d);

                    }
                }
                if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Extraer")){
                    int a = Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[1]);
                    if(this.getPlayerData(event.getWhoClicked().getName()).getDiamonds() < a){
                        event.getWhoClicked().sendMessage(Colorizer.Color("&cDiamantes en el banco insuficientes."));
                        return;
                    }
                    int d = 1;
                        int en = 0;
                    for(ItemStack it : event.getWhoClicked().getInventory().getContents()){
                        if(it == null){
                            en++;
                        }
                    }
                    if(a > 64){
                        d = d+ a/64;
                    }
                    if(en < d){
                        event.getWhoClicked().sendMessage(Colorizer.Color("&cEspacio insuficiente para guardar los diamantes"));
                        return;
                    }
                    this.getPlayerData(event.getWhoClicked().getName()).removeDiamonds(a);
                    event.getWhoClicked().getInventory().addItem(new ItemStack(Material.DIAMOND,a));
                        event.getWhoClicked().sendMessage(Colorizer.Color("&c"+a+" extraidos del banco."));
                }
            }
        }
    }
    public ItemStack getRuna(int id){
        if(id==0){
        ItemStack it = new ItemStack(Material.PAPER);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName(Colorizer.Color("&f&lRuna de Proteccion"));
        List<String> lore = new ArrayList();
        lore.add(Colorizer.Color("&lRuna de Proteccion"));
        lore.add(Colorizer.Color("&f&lProteje el item de"));
        lore.add(Colorizer.Color("&f&ldestruccion al poner"));
        lore.add(Colorizer.Color("&f&lencantamientos de runas."));
        im.setLore(lore);
        it.setItemMeta(im);
            return it;}
        else if(id==1){
            ItemStack it = new ItemStack(Material.NETHER_STAR);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName(Colorizer.Color("&6&lRuna Magica"));
            List<String> lore = new ArrayList();
            lore.add(Colorizer.Color("&6Runa Magica"));
            lore.add(Colorizer.Color("&7Da una Ranura extra a un item"));
            lore.add(Colorizer.Color("&7en el que esta runa es colocado."));
            im.setLore(lore);
            it.setItemMeta(im);
            return it;
        }else if(id==2){
            ItemStack it = new ItemStack(Material.SKULL_ITEM,1,(short)1);
            ItemMeta im = it.getItemMeta();
            im.setDisplayName(Colorizer.Color("&4&lPiedra Maldita"));
            List<String> lore = new ArrayList();
            lore.add(Colorizer.Color("&4&lLa Piedra Maldita"));
            lore.add(Colorizer.Color("&5Saca un encantamiento al azar de un item"));
            lore.add(Colorizer.Color("&5y te da la runa a ti."));
            im.setLore(lore);
            it.setItemMeta(im);
            return it;
        }
        return null;
    }

    @EventHandler
    public void onFlyPvP(PlayerChangedWorldEvent event){
        if(event.getPlayer().getWorld().getPVP()){
            if(event.getPlayer().getGameMode().equals(GameMode.SPECTATOR) || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                event.getPlayer().setAllowFlight(true);
            }else{
            event.getPlayer().setAllowFlight(false);}
        }
    }
    HashSet<String> godlist = new HashSet();

    @EventHandler
    public void onPlayerGodMode(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            if(!event.getEntity().getWorld().getPVP()){
                if(godlist.contains(event.getEntity().getName())){
                    event.setCancelled(true);
                }
            }
        }
    }

    HashMap<String,Long> dioswait = new HashMap();

    public boolean isEquipment(ItemStack it){
        String itname = it.getType().name().toLowerCase();
        if(itname.contains("helmet")){
            return true;
        }else if(itname.contains("chestplate")){
            return true;
        }else if(itname.contains("leggings")){
            return true;
        }else if(itname.contains("boots")){
            return true;
        }else if(itname.contains("sword")){
            return true;
        }else if(itname.contains("pickaxe")){
            return true;
        }else if(itname.contains("shovel")){
            return true;
        }else if(itname.contains("axe")){
            return true;
        }else if(itname.contains("hoe")){
            return true;
        }else if(itname.contains("bow")){
            return true;
        }
        return false;

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("rename")){
            if(args.length < 1){
                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &cPara cambiar el nombre de el item en tu mano"));
                sender.sendMessage(Colorizer.Color("&ausa /rename <nombre>"));
                return true;
            }else if(getPlayerData(sender.getName()).getRank().isAtLeast(Rank.VIP)){
                Player p = (Player)sender;
                if(!this.isEquipment(p.getItemInHand())){
                    sender.sendMessage(Colorizer.Color("&c&lSolo se puede renombrar equipos y armas"));
                    return true;
                }
                String name = "";

                for(String s : args){
                    if(s != null){
                    name = name+" "+s;}
                }
                name = name.substring(1);
                name = Colorizer.Color(name);
                if(name.length()>=48){
                    name = name.substring(0,47);
                    sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &c Nombre del item limitado a 48 caracteres."));
                }

                ItemMeta im = p.getItemInHand().getItemMeta();
                im.setDisplayName(name);
                p.getItemInHand().setItemMeta(im);
                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aNuevo nombre: "+name));
                return true;
            }else{
                sender.sendMessage(Colorizer.Color("&cComando /rename solo esta disponible para vips!"));
            }
            return true;
        }
        else if(cmd.getName().equals("fly")){
            if(args.length<=0){
                if(this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.ELITE)){
                    Player p = Bukkit.getPlayer(sender.getName());
                    if(p.getWorld().getPVP() && !this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.Builder)){
                        p.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &cNo se puede activar el vuelo en mundos PvP"));
                        return true;
                    }else if(!p.getAllowFlight()){
                        p.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aVuelo activado, usa /fly para desactivarlo."));
                        p.setAllowFlight(true);
                        return true;
                    }else{
                        p.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aVuelo desactivado, usa /fly para activarlo."));
                        p.setAllowFlight(false);
                        return true;
                    }
                }else{
                    sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Rango Minimo: Elite."));
                    return true;
                }
            }else if(args.length==1){
                    if(this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.DIOS)){
                        if(dioswait.containsKey(sender.getName()) && dioswait.get(sender.getName()) > System.currentTimeMillis()){
                            sender.sendMessage(Colorizer.Color("&c[&aMineUltra&c] &aEspera unos segundos antes de otorgar privilegios!"));
                            return true;
                        }
                        if(Bukkit.getPlayer(args[0]) != null){
                            Player p = Bukkit.getPlayer(args[0]);
                            if(this.getPlayerData(p.getName()).getRank().isAtLeast(Rank.ELITE)){
                                sender.sendMessage(Colorizer.Color("&aNo puedes controlar el vuelo de los jugadores de ELITE en adelante."));
                                return true;
                            }
                            else if(p.getWorld().getPVP()){
                                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a No se puede activar el vuelo en mundos PvP"));
                                return true;
                            }else if(!p.getAllowFlight()){
                                p.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aVuelo activado, por &b&k|||&b&lDIOS&b&k|||&3&l "+sender.getName()+"."));
                                p.setAllowFlight(true);
                                this.dioswait.put(sender.getName(),System.currentTimeMillis()+30000L);
                                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Vuelo activado para "+p.getName()));
                                return true;
                            }else{
                                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aEl jugador ya tiene el fly activado!"));
                                return true;
                            }
                        }else{
                            sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aJugador no esta conectado."));
                            return true;
                        }
                    }else{
                        sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Rango Minimo: Dios."));
                        return true;
                    }


            }else{
                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Uso: /fly o /fly <nombre>"));
                return true;
            }
        }else if(cmd.getName().equals("god")){
            if(args.length <= 0){
            if(this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.ELITE)){
                if(this.godlist.contains(sender.getName())){
                    sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Modo Invencible Desactivado."));
                    this.godlist.remove(sender.getName());
                    return true;
                }else{
                    sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Modo Invencible Activado."));
                    this.godlist.add(sender.getName());
                    return true;
                }
            }else{
                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Rango Minimo: Elite."));
                return true;
            }
            }else if(args.length==1){
                    if(this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.DIOS)){
                        if(dioswait.containsKey(sender.getName()) && dioswait.get(sender.getName()) > System.currentTimeMillis()){
                            sender.sendMessage(Colorizer.Color("&c[&aMineUltra&c] &aEspera unos segundos antes de otorgar privilegios!"));
                            return true;
                        }
                        if(Bukkit.getPlayer(args[0]) != null){
                            Player p = Bukkit.getPlayer(args[0]);
                            if(this.getPlayerData(p.getName()).getRank().isAtLeast(Rank.ELITE)){
                                sender.sendMessage(Colorizer.Color("&aNo puedes controlar el modo invencible de los jugadores de ELITE en adelante."));
                                return true;
                            }
                            else if(!this.godlist.contains(p.getName())){
                                p.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aModo invencible activado por &b&k|||&b&lDIOS&b&k|||&3&l "+sender.getName()+"."));
                                this.godlist.add(p.getName());
                                this.dioswait.put(sender.getName(),System.currentTimeMillis()+30000L);
                                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Modo Invencible activado para "+p.getName()));
                                return true;
                            }else{
                                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aEl jugador ya tiene el modo invencible activado!"));
                                return true;
                            }
                        }else{
                            sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c] &aJugador no esta conectado."));
                            return true;
                        }
                    }else{
                        sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Rango Minimo: Dios."));
                        return true;
                    }


            }else{
                sender.sendMessage(Colorizer.Color("&c[&aMineLevel&c]&a Uso: /god o /god <nombre>"));
                return true;
            }
        }
        else if(cmd.getName().equalsIgnoreCase("banco")){
            if(sender instanceof Player){
            this.openBanco((Player)sender);
            sender.sendMessage(Colorizer.Color("&aAbriendo menu del banco..."));
            return true;}else{
                sender.sendMessage(Colorizer.Color("Solo jugadores pueden abrir el banco"));
                return true;
            }
        }
        else if(cmd.getName().equalsIgnoreCase("runas")){
            if(sender instanceof Player){
            ((Player)sender).performCommand("runes tinkerer");
            return true;}

        }
        else if(cmd.getName().equalsIgnoreCase("evento")){
            if(args.length==1 && args[0].equals("skip") && sender.hasPermission("minelevel.mod")){
                lastEvent.skip();
            }
            eventType event = lastEvent.getEventType();
            if(event.equals(eventType.Invasion)){
                sender.sendMessage(Colorizer.Color("&c[Evento]&a Las bestias estan invadiendo las 4 ciudades cada 3 minutos"));
                sender.sendMessage(Colorizer.Color("&aPvP, Survival, Nether y End"));
                sender.sendMessage(Colorizer.Color("&aPatrulla sus alrededores y derrotalas"));
                sender.sendMessage(Colorizer.Color("&aRecibiras grandes recompensas!"));
                sender.sendMessage(Colorizer.Color("&cTiempo Restante: "+lastEvent.getTimeLeft()));
            }else if(event.equals(eventType.Mineria)){
                sender.sendMessage(Colorizer.Color("&c[Evento]&a Mina minerales de todo tipo"));
                sender.sendMessage(Colorizer.Color("&amientras mas minerales mines, mayor sera la recompensa"));
                sender.sendMessage(Colorizer.Color("&ay los pocos expertos recibiran premios extra"));
                sender.sendMessage(Colorizer.Color("&cTiempo Restante: "+lastEvent.getTimeLeft()));
            }
            else if(event.equals(eventType.Coliseo)){
                if(sender instanceof Player && !coliseoStarted){
                    Bukkit.getPlayer(sender.getName()).teleport(loc3);
                }else{
                    sender.sendMessage(Colorizer.Color("&c[Evento]&a El evento ya ha comenzado usa /evento para ingresar a la proxima!"));
                }
                sender.sendMessage(Colorizer.Color("&c[Evento] &aNo se pierden items al morir"));
                sender.sendMessage(Colorizer.Color("&cEl evento pvp empezara pronto, espera aqui!"));
            }else if(event.equals(eventType.Min30)){
                sender.sendMessage(Colorizer.Color("&c[Evento]&a Hay 30 minutos de pausa entre cada evento"));
                sender.sendMessage(Colorizer.Color("&aSiguiente evento: "+lastEvent.getNextGameEvent().getTitle()));
                sender.sendMessage(Colorizer.Color("&aen "+lastEvent.getTimeLeft()));
            }
            return true;
        }
        else if(cmd.getName().equalsIgnoreCase("auto")){
            if(this.autoprivados.get(sender.getName())){
                this.autoprivados.put(sender.getName(),false);
                sender.sendMessage(Colorizer.Color("&c[MineLevel] &2Has desactivado AutoPrivatizacion, usa &a/auto&2 nuevamente para activarlo"));
            }else{
                this.autoprivados.put(sender.getName(),true);
                sender.sendMessage(Colorizer.Color("&c[MineLevel] &2Has activado AutoPrivatizacion, usa &a/auto&2 nuevamente para desactivarlo"));
            }
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("mine") && sender.hasPermission("minelevel.admin")
                || cmd.getName().equalsIgnoreCase("mine") && sender.isOp()){
            if(args.length <= 0){
                sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
                sender.sendMessage(Colorizer.Color("&a/mine addrank <rank> <player> <days>"));
                sender.sendMessage(Colorizer.Color("&a/mine adddiamonds <amount> <player>"));
                return true;
            }
            if(args[0].equalsIgnoreCase("addrank") && sender.hasPermission("minelevel.addrank") || args[0].equalsIgnoreCase("addrank") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.OWNER)) {
                // /mine addrank <rank> <player> <days>
                if (args.length == 4) {
                    String rank = args[1];
                    String name = args[2];
                    this.insert(name, "");
                    Integer days = Integer.parseInt(args[3]);
                    PlayerData pd = this.getPlayerData(name);
                    pd.addRank(rank, days);
                    sender.sendMessage(Colorizer.Color("&eRango &7" + rank + " &eañadido a &7" + name + " &epor &7" + days + "dias"));
                return true;
                } else {
                    sender.sendMessage(Colorizer.Color("&aUso: /mine addrank <rank> <player> <days>"));
                }
            }else if(args[0].equalsIgnoreCase("adddiamonds") && sender.hasPermission("minelevel.adddiamonds")|| args[0].equalsIgnoreCase("adddiamonds") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.OWNER)) {
                // /mine addcoins <amount> <player>
                if (args.length == 3) {
                    int amount = Integer.parseInt(args[1]);
                    String name = args[2];
                    this.insert(name, "");
                    PlayerData pd = this.getPlayerData(name);
                    pd.addDiamonds(amount);
                    sender.sendMessage(Colorizer.Color("&e"+amount + " &ediamantes añadidos a &7" + name));
                    return true;
                } else {
                    sender.sendMessage(Colorizer.Color("&aUso: /mine adddiamonds <amount> <player>"));
                }
            }


            sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
            sender.sendMessage(Colorizer.Color("&a/mine addrank <rank> <player> <days>"));
            sender.sendMessage(Colorizer.Color("&a/mine adddiamonds <amount> <player>"));

            return true;
        }


        if (cmd.getName().equalsIgnoreCase("mod")){
            if(args.length <= 0 && !label.equalsIgnoreCase("vanish")){
                sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
                sender.sendMessage(Colorizer.Color("&a/ban <jugador> <razon>    &e(Ayudante)"));
                sender.sendMessage(Colorizer.Color("&a/unban <jugador>          &d(GM)"));
                sender.sendMessage(Colorizer.Color("&a/kick <jugador> <mensaje> &e(Ayudante)"));
                sender.sendMessage(Colorizer.Color("&a/vanish                   &d(GM)"));
                sender.sendMessage(Colorizer.Color("&a/tp <jugador>             &d(GM)"));
                return true;
            }
            /* if(args[0].equalsIgnoreCase("topwins")){
                try {
                    int pos = 1;
                    sender.sendMessage(Colorizer.Color("&b&m[---------&b&nTop Victorias&b&m---------]"));
                    for(String s : this.getTopWins().keySet()){
                        sender.sendMessage(Colorizer.Color("&b"+pos+". &a"+s+": &c"+this.getTopWins().get(s)+"victorias"));
                        pos++;
                    }
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/


            else if(label.equalsIgnoreCase("ban") && sender.hasPermission("minelevel.ban")|| label.equalsIgnoreCase("ban") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.Helper)){
                // /ban <name> <reason>
                if(args.length == 2) {
                    String name = args[0];
                    String reason = args[1];
                    banType type = banType.getByName(reason);
                    if(sender instanceof Player){
                        if(type == banType.Hacks || type==banType.Permanente){
                            PlayerData pd = this.getPlayerData(sender.getName());
                            if(!pd.getRank().isAtLeast(Rank.GM)){
                                sender.sendMessage(Colorizer.Color("&cSolo pueden banear permanente y por hacks &dLos que son [GM] en adelante"));
                                return true;}
                        }
                    }
                    PlayerData pd = this.getPlayerData(name);
                    if (pd==null){
                        sender.sendMessage(Colorizer.Color("&cEse jugador no existe."));
                        return true;
                    }
                    if(pd.getRank().isAtLeast(Rank.Helper)){
                        sender.sendMessage(Colorizer.Color("&aNo puedes banear a otros staffs!"));
                        sender.sendMessage(Colorizer.Color("&cCuida tu comportamiento o seras baneado."));
                        return true;
                    }
                    pd.ban(type, sender.getName());
                    sender.sendMessage(Colorizer.Color("&c" + name + " &ebaneado " + type.getDays() + "dias por &7" + type.getReason() + " &eOrigen: " + sender.getName()));
                    if(Bukkit.getPlayer(name) != null){
                        Bukkit.getPlayer(name).kickPlayer(Colorizer.Color("&cHas sido baneado"));
                    }
                }else{
                    sender.sendMessage(Colorizer.Color("&aUso: /ban <jugador> <razon>"));
                    sender.sendMessage(Colorizer.Color("&aRazones: "+banType.getAllNames()));
                }
            }
            else if(label.equalsIgnoreCase("unban") && sender.hasPermission("minelevel.unban")|| label.equalsIgnoreCase("unban") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)){
                // /unban <name>
                if(args.length==1) {
                    String name = args[0];

                    PlayerData pd = this.getPlayerData(name);
                    if (pd==null){
                        sender.sendMessage(Colorizer.Color("&cEse jugador no existe."));
                        return true;
                    }
                    pd.unban();
                    sender.sendMessage(Colorizer.Color("&c" + name + " &edesbaneado por &7" + sender.getName()));
                }else{
                    sender.sendMessage(Colorizer.Color("&aUso: /unban <jugador>"));
                }
            }
            else if(label.equalsIgnoreCase("kick") && sender.hasPermission("minelevel.kick")|| label.equalsIgnoreCase("kick") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.Helper)){
                // /kick <name> <mensaje>
                if(args.length==2) {
                    String name = args[0];
                    String mensaje = args[1];

                    if(Bukkit.getPlayer(name) != null){
                        Bukkit.getPlayer(name).kickPlayer(Colorizer.Color(mensaje+" &cKickeado por "+sender.getName()));
                        Bukkit.broadcastMessage(Colorizer.Color("&a"+name+" &cKickeado por "+sender.getName()));}
                    else{
                        sender.sendMessage(Colorizer.Color("&cJugador no encontrado."));
                    }
                }else{
                    sender.sendMessage(Colorizer.Color("&aUso: /kick <jugador> <mensaje>"));}
            }
            else if(label.equalsIgnoreCase("vanish") && sender.hasPermission("minelevel.vanish") || label.equalsIgnoreCase("vanish") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)) {
                // /vanish
                if(sender instanceof Player){
                    Player p = (Player)sender;
                    if(p.getGameMode()== GameMode.SPECTATOR){
                        p.setGameMode(GameMode.SURVIVAL);
                        p.sendMessage(Colorizer.Color("&aVanish Desactivado"));
                    }else{
                        p.setAllowFlight(true);
                        p.setGameMode(GameMode.SPECTATOR);
                    p.sendMessage(Colorizer.Color("&aVanish Activado"));}
                }else{
                    sender.sendMessage(Colorizer.Color("&cEste comando solo se puede usar siendo un jugador"));
                }
            }
            else if(label.equalsIgnoreCase("tp") && sender.hasPermission("minelevel.tp")|| label.equalsIgnoreCase("tp") && sender instanceof Player && this.getPlayerData(sender.getName()).getRank().isAtLeast(Rank.GM)) {
                // /tp <jugador>
                if(sender instanceof Player && args.length == 1){
                    String to = args[0];
                    if(Bukkit.getPlayer(to) == null){
                        sender.sendMessage(Colorizer.Color("&cJugador no encontrado"));
                    }else{
                        Player p = (Player)sender;
                        p.sendMessage(Colorizer.Color("&aTeletransportando"));
                        p.teleport(Bukkit.getPlayer(to));
                    }
                }else if(args.length==2){
                    sender.sendMessage(Colorizer.Color("&cEste comando solo se puede usar siendo un jugador"));
                }else{
                    sender.sendMessage(Colorizer.Color("&aUso: /tp <jugador>"));
                }
            }
            else{
                sender.sendMessage(Colorizer.Color("&b&m-----&cComandos&b&m-----"));
                sender.sendMessage(Colorizer.Color("&a/ban <jugador> <razon>    &e(Ayudante)"));
                sender.sendMessage(Colorizer.Color("&a/unban <jugador>          &d(GM)"));
                sender.sendMessage(Colorizer.Color("&a/kick <jugador> <mensaje> &e(Ayudante)"));
                sender.sendMessage(Colorizer.Color("&a/vanish                   &d(GM)"));
                sender.sendMessage(Colorizer.Color("&a/tp <jugador>             &d(GM)"));

                return true;}
            return true;
        }
        return false;
    }


    public void checkData(){
        if(enabled == false){
            return;
        }
        if(data == null){
            openData();
        }else try {
            if(data.isClosed()){
                openData();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void insert(String name, String uuid){
        String sqlinsert = "INSERT INTO `"+table+"` ("
                + columnType.Name.getName()+","
                + columnType.UUID.getName()+","
                + columnType.Kills.getName()+","
                + columnType.Deaths.getName()+","
                + columnType.MineriaPts.getName()+","
                + columnType.ColiseoPts.getName()+","
                + columnType.LastMessages.getName()+","
                + columnType.LastReports.getName()+","
                + columnType.LastTransactions.getName()+","
                + columnType.Pets.getName()+","
                + columnType.Ranks.getName()+","
                + columnType.Diamonds.getName()+","
                + columnType.Friends.getName()+","
                + columnType.Bans.getName()
                +") " +
                "  SELECT '"+name.toLowerCase()+"',"
                + "'"+uuid+"',"
                + "'"+columnType.Kills.getDefault()+"',"
                + "'"+columnType.Deaths.getDefault()+"',"
                + "'"+columnType.MineriaPts.getDefault()+"',"
                + "'"+columnType.ColiseoPts.getDefault()+"',"
                + "'"+columnType.LastMessages.getDefault()+"',"
                + "'"+columnType.LastReports.getDefault()+"',"
                + "'"+columnType.LastTransactions.getDefault()+"',"
                + "'"+columnType.Pets.getDefault()+"',"
                + "'"+columnType.Ranks.getDefault()+"',"
                + "'"+columnType.Diamonds.getDefault()+"',"
                + "'"+columnType.Friends.getDefault()+"',"
                + "'"+columnType.Bans.getDefault()+"'"
                + " FROM dual " +
                "WHERE NOT EXISTS " +
                "( SELECT Name FROM "+table+" WHERE Name='"+name.toLowerCase()+"' );";

        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Record not inserted");
        }
    }

    public void removePlayerData(String name){
        if(this.playerdata.containsKey(name.toLowerCase())){
            this.playerdata.remove(name.toLowerCase());}
    }

    HashMap<String,PlayerData> playerdata = new HashMap();

    public PlayerData getPlayerData(String name){
        if(playerdata.containsKey(name.toLowerCase())){
            return playerdata.get(name.toLowerCase());
        }else{
            PlayerData pd = new PlayerData(name.toLowerCase(),this);
            if(pd != null){
                playerdata.put(name.toLowerCase(), pd);
                return pd;
            }
        }
        return null;
    }

    private void createTable() throws SQLException {
        if(enabled == false){
            return;
        }

        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS `"+table+"` ("
                        + "`ID` int(11) NOT NULL auto_increment"
                        +",`"+columnType.Name.getName()+"` varchar(255) NOT NULL"
                        +",`"+columnType.UUID.getName()+"` varchar(255) NOT NULL"
                        +",`"+columnType.Kills.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Deaths.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.MineriaPts.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.ColiseoPts.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.LastMessages.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.LastReports.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.LastTransactions.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Pets.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Ranks.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Diamonds.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Friends.getName()+"` text(4999) NOT NULL"
                        +",`"+columnType.Bans.getName()+"` text(4999) NOT NULL"
                        +",PRIMARY KEY  (`ID`)"
                        +
                        ")";


        this.getStatement().execute(sqlCreate);
    }

    java.sql.Statement Statement = null;

    public java.sql.Statement getStatement(){
        if(enabled == false){
            return null;
        }
        try {
            if(Statement == null){
                Statement = data.createStatement();
            }else if(Statement.isClosed()){
                Statement = data.createStatement();
            }


            return Statement;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Statement;
    }


    public final void openData(){
        if(enabled == false){
            return;
        }
        MySQL mysql = new MySQL(this, host, port, db, user, pwd);
        try {
            data = mysql.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void init(){
        System.out.println("[MineLevel] Initializing");
        try{
            this.openData();
        }catch(Exception e){
            e.printStackTrace();
            enabled = false;
            return;
        }

        try {
            this.createTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("MineLevel Initialized.");
        enabled = true;
    }

    @EventHandler
    public void onServerStartJoin(PlayerLoginEvent event){
        if(starttime + 5000L > System.currentTimeMillis()){
            event.setKickMessage(Colorizer.Color("&aEl server se esta iniciando, intenta devuelta en 5segundos"));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInteractH(BlockPlaceEvent event){
        if(event.getBlock() != null && event.getBlock().getType().equals(Material.HOPPER_MINECART)){
            event.setCancelled(true);
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInteractH(PlayerInteractEvent event){
        if(event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.HOPPER_MINECART)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        event.getPlayer().setMaximumNoDamageTicks(5);
    }
    HashMap<String,Boolean> autoprivados = new HashMap();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void LoadPlayer(PlayerJoinEvent event){
        event.getPlayer().setMaximumNoDamageTicks(6);

        this.autoprivados.put(event.getPlayer().getName(),true);
        //pre load player data
        if(getPlayerData(event.getPlayer().getName()).isBanned()){
            event.getPlayer().kickPlayer(Colorizer.Color(getPlayerData(event.getPlayer().getName()).getBan().getKickMessage()));
        }
        event.setJoinMessage(null);

        if(getPlayerData(event.getPlayer().getName()).getRank().isAtLeast(Rank.VIP)){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(Colorizer.Color("&a[+] &a"+getPlayerData(event.getPlayer().getName()).getRank().getChatPrefix()+event.getPlayer().getName()));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP,1F,1F);
            }

            for(Rank rk : getPlayerData(event.getPlayer().getName()).getRanks()){
                event.getPlayer().sendMessage(Colorizer.Color("&7[&cMineLevel&7] &eLe quedan "+rk.getDaysLeft()+" a tu rango "+rk.getChatPrefix()));
            }

        }


        SetPlayerScoreboardTeam(event.getPlayer());
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p == event.getPlayer()){
                continue;
            }
                if (GetScoreboard(p).getTeam(event.getPlayer().getName())==null)  {
                    GetScoreboard(p).registerNewTeam(event.getPlayer().getName()).setPrefix(getPlayerData(event.getPlayer().getName()).getRank().getScoreboardPrefix());
                }
                GetScoreboard(p).getTeam(event.getPlayer().getName()).addPlayer(event.getPlayer());
            if(GetScoreboard(event.getPlayer()).getEntryTeam(p.getName())==null) {
                GetScoreboard(event.getPlayer()).registerNewTeam(p.getName()).setPrefix(getPlayerData(p.getName()).getRank().getScoreboardPrefix());
            }else{
                GetScoreboard(event.getPlayer()).getTeam(p.getName()).setPrefix(getPlayerData(p.getName()).getRank().getScoreboardPrefix());
            }
            GetScoreboard(event.getPlayer()).getTeam(p.getName()).addPlayer(p);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void UnloadPlayer(PlayerQuitEvent event){
        if(this.coliseostats.containsKey(event.getPlayer().getName())){
            this.removeFromColiseo(event.getPlayer().getName(),"desconectarse de server.");
        }

        if(this.autoprivados.containsKey(event.getPlayer().getName())){
        this.autoprivados.remove(event.getPlayer().getName());}

        if(getPlayerData(event.getPlayer().getName()).getRank().isAtLeast(Rank.VIP)){
            Bukkit.broadcastMessage(Colorizer.Color("&c[-] &a"+getPlayerData(event.getPlayer().getName()).getRank().getChatPrefix()+event.getPlayer().getName()));
        }
        event.setQuitMessage(null);
        if(this.toReset1.containsKey(event.getPlayer().getName())){
            _sideObjective.get(event.getPlayer().getName()).getScoreboard().resetScores(toReset1.get(event.getPlayer().getName()));
            _sideObjective.get(event.getPlayer().getName()).getScoreboard().resetScores(toReset2.get(event.getPlayer().getName()));
            _sideObjective.get(event.getPlayer().getName()).getScoreboard().resetScores(toReset3.get(event.getPlayer().getName()));
            this.toReset1.remove(event.getPlayer().getName());
            this.toReset2.remove(event.getPlayer().getName());
            this.toReset3.remove(event.getPlayer().getName());
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            if(event.getPlayer()==p){
                continue;
            }
            try{
                if(GetScoreboard(p).getTeam(event.getPlayer().getName()) != null){
            GetScoreboard(p).getTeam(event.getPlayer().getName()).unregister();}
            }catch(Exception e){e.printStackTrace();}
        }
        _scoreboard.remove(event.getPlayer().getName());
        _sideObjective.remove(event.getPlayer().getName());
        //unload


        this.removePlayerData(event.getPlayer().getName());
    }


    LinkedHashMap<String,Integer> topcoliseo = null;

    public LinkedHashMap<String,Integer> getTopColiseo() throws SQLException {
        if(topcoliseo != null){
            return topcoliseo;
        }
        else{
            ResultSet res = null;
            res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" ORDER BY ColiseoPts DESC LIMIT 10");
            topcoliseo = new LinkedHashMap();

            while(res.next()){
                topcoliseo.put(res.getString("Name"),res.getInt("ColiseoPts"));}
        }
        topcoliseo = (LinkedHashMap)this.sortByValue((Map)topcoliseo);
        return topcoliseo;
    }

    LinkedHashMap<String,Integer> topmineria = null;

    public LinkedHashMap<String,Integer> getTopMineria() throws SQLException {
        if(topmineria != null){
            return topmineria;
        }
        else{
            ResultSet res = null;
            res = plugin.getStatement().executeQuery("SELECT * FROM "+plugin.table+" ORDER BY MineriaPts DESC LIMIT 10");
            topmineria = new LinkedHashMap();

            while(res.next()){
                topmineria.put(res.getString("Name"),res.getInt("MineriaPts"));}
        }
        topmineria = (LinkedHashMap)this.sortByValue((Map)topmineria);
        return topmineria;
    }


    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    HashMap<String,Boolean> hasGod = new HashMap();
    /*
@EventHandler
public void onUpdateEvent(UpdateEvent event){
    if(event.getType() != UpdateType.SLOW) {
        return;
    }
    for(String player : hasGod.keySet()){
        if(Bukkit.getPlayer(player) == null){
            hasGod.remove(player);
            continue;
        }
        Player p = Bukkit.getPlayer(player);
        p.set

    }

}

*/

    public String getWorldTag(String world){
        if(world.equalsIgnoreCase("world")){
            return "&7[&aS&7]&r";
        }else if(world.equalsIgnoreCase("world_nether")){
            return "&7[&4N&7]&r";
        }else if(world.equalsIgnoreCase("world_the_end")){
            return "&7[&dE&7]&r";
        }else if(world.equalsIgnoreCase("pvp")){
            return "&7[&cP&7]&r";
        }
        return "&7[&fM&7]&r";
    }
        @EventHandler(priority=EventPriority.HIGHEST)
        public void onChat(PlayerChatEvent event){
            Rank rk = this.getPlayerData(event.getPlayer().getName()).getRank();

            String msgtype = Colorizer.Color(this.getWorldTag(event.getPlayer().getWorld().getName()));

            if(!event.getMessage().startsWith("!")){
                List<Player> toremove = new ArrayList();
            for(Player p : event.getRecipients()){
             if(p.getWorld() != event.getPlayer().getWorld()){
                 toremove.add(p);
             }
            }
            for(Player p : toremove){
                event.getRecipients().remove(p);
            }

            }else if(this.getPlayerData(event.getPlayer().getName()).getRank().isAtLeast(Rank.VIP)){
                msgtype = Colorizer.Color("&7(&b&l!&7)&r");
            }
            else if(this.getPlayerData(event.getPlayer().getName()).getDiamonds() > 4){
                this.getPlayerData(event.getPlayer().getName()).removeDiamonds(5);
                msgtype = Colorizer.Color("&7(&b&l!&7)&r");
                event.getPlayer().sendMessage(Colorizer.Color("&7[&cMineLevel&7]&a Usaste 5 diamantes para enviar el mensaje"));
            }else{
                event.getPlayer().sendMessage(Colorizer.Color("&c7[&cMineLevel&7]&a Diamantes insuficientes para hablar por global, necesitas 5"));
                event.setCancelled(true);
                return;
            }

            if(rk.isAtLeast(Rank.VIP)){
                event.setFormat(msgtype+""+rk.getChatPrefix()+" "+event.getPlayer().getName()+Colorizer.Color("&6: "+event.getMessage()));
            }
            else{try{
            event.setFormat(msgtype+rk.getChatPrefix()+event.getPlayer().getName()+Colorizer.Color(": &7")+event.getMessage());}catch(Exception e){
                e.printStackTrace();
            }
        }}




        public ItemStack makeItem(Material mat, int amount, short dura, String name,List<String> lore){
            ItemStack it = new ItemStack(mat,amount,dura);
            ItemMeta meta = it.getItemMeta();
            if(name != null) {
                meta.setDisplayName(Colorizer.Color(name));
            }
            if(lore != null){
                meta.setLore(lore);}

            it.setItemMeta(meta);
            if(!it.getType().name().toLowerCase().contains("glass")){
                it = addGlow(it);
            }
            return it;
        }

        public HashMap<String,Scoreboard> _scoreboard = new HashMap();
        private HashMap<String,Objective> _sideObjective = new HashMap();
    private HashMap<String,String> toReset1 = new HashMap();
    private HashMap<String,String> toReset2 = new HashMap();
    private HashMap<String,String> toReset3 = new HashMap();



        public void SetPlayerScoreboardTeam(Player player)
        {
            for (Team team : GetScoreboard(player).getTeams()) {
                if(team.hasPlayer(player)){
                    team.removePlayer(player);
                }
            }
            String next = getPlayerData(player.getName()).getRank().getScoreboardPrefix();
            if(next.length() > 16){
                next = next.substring(0, 16);
            }
            if(GetScoreboard(player).getTeam(next) == null){
                Team t = _scoreboard.get(player.getName()).registerNewTeam(next);
                t.setPrefix(next+" ");
                t.setAllowFriendlyFire(true);
                t.setNameTagVisibility(NameTagVisibility.ALWAYS);
            }


            int nx = 10;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&a &b")).setScore(nx);
            nx--;

            String timeleft = "";
            String currentgame = "";
            if(lastEvent != null && lastEvent.getEventType().equals(eventType.Min30)){
                timeleft = Colorizer.Color("&aEmpieza en ")+lastEvent.getTimeLeft();
                currentgame = Colorizer.Color("&cEvento:&f")+lastEvent.getNextGameEvent().getName();
            }else if(lastEvent != null){
                timeleft = Colorizer.Color("&cTermina en ")+lastEvent.getTimeLeft();
                currentgame = Colorizer.Color("&cEvento:&f")+lastEvent.getEventType().getName();
            }
            if(lastEvent != null) {
                _sideObjective.get(player.getName()).getScore(currentgame).setScore(nx);
                toReset1.put(player.getName(), currentgame);
            }
            nx--;
            if(lastEvent != null) {
            _sideObjective.get(player.getName()).getScore(timeleft).setScore(nx);
            toReset2.put(player.getName(),timeleft);}

            nx--;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&b &c")).setScore(nx);
            nx--;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&3Rango:")).setScore(nx);
            nx--;
            _sideObjective.get(player.getName()).getScore("➡ "+getPlayerData(player.getName()).getRank().getScoreboardPrefix()).setScore(nx);
            nx--;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&c &d")).setScore(nx);
            nx--;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&3Diamonds:")).setScore(nx);
            nx--;

            String diamonds = Colorizer.Color("➡ &b"+((int)getPlayerData(player.getName()).getDiamonds()));
            _sideObjective.get(player.getName()).getScore(diamonds).setScore(nx);
            toReset3.put(player.getName(),diamonds);

            nx--;
            _sideObjective.get(player.getName()).getScore(Colorizer.Color("&d &f")).setScore(nx);
            nx--;
            _sideObjective.get(player.getName()).getScore("www.minelevel.com").setScore(nx);

            GetScoreboard(player).getTeam(next).addPlayer(player);
            player.setScoreboard(_scoreboard.get(player.getName()));
        }
        public Scoreboard GetScoreboard(Player p)
        {
            if(!_scoreboard.containsKey(p.getName())){
                Scoreboard sco = Bukkit.getScoreboardManager().getNewScoreboard();
                _sideObjective.put(p.getName(), sco.registerNewObjective("Obj" + r2.nextInt(999999999), "dummy"));
                _sideObjective.get(p.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);
                _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+"  "+"MineLevel"+"  "));

                _scoreboard.put(p.getName(), sco);
            }

            return _scoreboard.get(p.getName());
        }

        String g = ChatColor.GOLD+""+ChatColor.BOLD;
        String y = ChatColor.YELLOW+""+ChatColor.BOLD;
        String f = ChatColor.WHITE+""+ChatColor.BOLD;



        int nex = 0;

        @EventHandler
        public void updateScoreboard(UpdateEvent event){
            if(event.getType() == UpdateType.SEC || event.getType()==UpdateType.MIN1){
                if(starttime+15000L > System.currentTimeMillis()){
                return;}
                if(event.getType()==UpdateType.MIN1){
                    for(Player p : Bukkit.getOnlinePlayers()){
                        p.setScoreboard(_scoreboard.get(p.getName()));
                    }
                }
                String timeleft;
                String currentgame;
                if(lastEvent==null){
                    return;
                }
                if(lastEvent.getEventType().equals(eventType.Min30)){
                    timeleft = Colorizer.Color("&aEmpieza en ")+lastEvent.getTimeLeft();
                    currentgame = Colorizer.Color("&cEvento:&f ")+lastEvent.getNextGameEvent().getName();
                }else{
                    timeleft = Colorizer.Color("&cTermina en ")+lastEvent.getTimeLeft();
                    currentgame = Colorizer.Color("&cEvento:&f ")+lastEvent.getEventType().getName();
                }
try{
                for(String s : toReset1.keySet()){
                    if(!_sideObjective.containsKey(s)){
                        continue;
                    }
                    String s2 = toReset1.get(s);
                    _sideObjective.get(s).getScoreboard().resetScores(s2);
                    _sideObjective.get(s).getScore(currentgame).setScore(9);
                    toReset1.put(s,currentgame);
                }
                for(String s : toReset2.keySet()){
                    if(!_sideObjective.containsKey(s)){
                        continue;
                    }
                    String s2 = toReset2.get(s);
                    _sideObjective.get(s).getScoreboard().resetScores(s2);
                    _sideObjective.get(s).getScore(timeleft).setScore(8);
                    toReset2.put(s,timeleft);
                }
                for(String s : toReset3.keySet()){
                    if(!_sideObjective.containsKey(s)){
                        continue;
                    }
                    String s2 = toReset3.get(s);
                    String diamonds = Colorizer.Color("➡ &b"+((int)getPlayerData(s).getDiamonds()));
                    _sideObjective.get(s).getScoreboard().resetScores(s2);
                    _sideObjective.get(s).getScore(diamonds).setScore(2);
                    toReset3.put(s,diamonds);
                }}catch(Exception e){
    e.printStackTrace();
                }
            }
            if(event.getType() != UpdateType.FAST){
                return;
            }
            this.setNextPrefix();
        }

        public void loadP(Player p){
            p.setMaximumNoDamageTicks(5);

            this.autoprivados.put(p.getName(),true);
            //pre load player data
            if(getPlayerData(p.getName()).isBanned()){
                p.kickPlayer(Colorizer.Color(getPlayerData(p.getName()).getBan().getKickMessage()));
            }

            if(getPlayerData(p.getName()).getRank().isAtLeast(Rank.VIP)){
                for(Player pe : Bukkit.getOnlinePlayers()){
                    pe.sendMessage(Colorizer.Color("&a[+] &a"+getPlayerData(p.getName()).getRank().getChatPrefix()+p.getName()));
                    pe.playSound(p.getLocation(), Sound.ORB_PICKUP,1F,1F);
                }

                for(Rank rk : getPlayerData(p.getName()).getRanks()){
                    p.sendMessage(Colorizer.Color("&7[&cMineLevel&7] &eLe quedan "+rk.getDaysLeft()+" a tu rango "+rk.getChatPrefix()));
                }

            }


            SetPlayerScoreboardTeam(p);
            for(Player pe : Bukkit.getOnlinePlayers()){
                if(pe == p){
                    continue;
                }
                if (GetScoreboard(pe).getTeam(p.getName())==null)  {
                    GetScoreboard(pe).registerNewTeam(p.getName()).setPrefix(getPlayerData(p.getName()).getRank().getScoreboardPrefix());
                }
                GetScoreboard(pe).getTeam(p.getName()).addPlayer(p);
                if(GetScoreboard(p).getEntryTeam(pe.getName())==null) {
                    GetScoreboard(p).registerNewTeam(pe.getName()).setPrefix(getPlayerData(pe.getName()).getRank().getScoreboardPrefix());
                }else{
                    GetScoreboard(p).getTeam(pe.getName()).setPrefix(getPlayerData(pe.getName()).getRank().getScoreboardPrefix());
                }
                GetScoreboard(p).getTeam(pe.getName()).addPlayer(pe);
            }
        }

        public void setNextPrefix(){
            nex++;
            if(nex > 9){
                nex = 0;
            }
            for(Player p : Bukkit.getOnlinePlayers()){
               if(!_sideObjective.containsKey(p.getName())){
                   this.loadP(p);
                   System.out.println("re-loaded: "+p.getName());
               }
                    if (nex == 0) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(y + " " + "M" + f + "ineLevel") + "  ");
                    } else if (nex == 1) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g + " " + "M" + y + "i" + f + "neLevel") + "  ");
                    } else if (nex == 2) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g + " " + "Mi" + y + "n" + f + "eLevel") + "  ");
                    } else if (nex == 3) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g + " " + "Min" + y + "e" + f + "Level") + "  ");
                    } else if (nex == 4) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g + " " + "Mine" + y + "L" + f + "evel") + "  ");
                    } else if (nex == 5) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(y + " " + "M" + g + "ineL" + y + "e" + f + "vel") + "  ");
                    } else if (nex == 6) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f + " " + "M" + y + "i" + g + "neLe" + y + "v" + f + "el") + "  ");
                    } else if (nex == 7) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f + " " + "Mi" + y + "n" + g + "eLev" + y + "e" + f + "l") + "  ");
                    } else if (nex == 8) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f + " " + "Min" + y + "e" + g + "Leve" + y + "l") + "  ");
                    } else if (nex == 9) {
                        _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f + " " + "Mine" + y + "L" + g + "evel") + "  ");
                    } /*else if(nex == 11){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" M"+y+"i"+f+"neU"+y+"l"+g+"tra")+"  ");
  } else if(nex == 12){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" Mi"+y+"n"+f+"eUl"+y+"t"+g+"ra")+"  ");
  } else if(nex == 13){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(g+" Min"+y+"e"+f+"Ult"+y+"r"+g+"a")+"  ");
  } else if(nex == 14){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  } else if(nex == 15){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  } else if(nex == 16){
      _sideObjective.get(p.getName()).setDisplayName(Colorizer.Color(f+" "+"Min"+y+"e"+g+"Ultr"+y+"a")+"  ");
  }*/
            }
        }

    }
