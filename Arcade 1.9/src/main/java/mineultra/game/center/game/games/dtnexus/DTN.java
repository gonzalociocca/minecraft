package mineultra.game.center.game.games.dtnexus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftTNTPrimed;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DTN extends TeamGame
{

  private ArrayList<Location> coreLocs;
  FileConfiguration config = Manager.GetPlugin().getConfig();
  private int redcore1Health = 75;
   private int redcore2Health = 50;
  private int bluecore1Health = 75;
  private int bluecore2Health = 50;
  private boolean twoCores = false;
  private static long startTime = 0;
  private int fase = 1;

  public DTN(centerManager manager)
  {
    super(manager, GameType.DTN, 
      new Kit[] { 
      new KitWarrior(manager),
      new KitArcher(manager),
      new KitMiner(manager),
      new KitMaster(manager),
      new KitVampire(manager),
      new KitGolem(manager),
      new KitInferno(manager)
              
      }, 
      new String[] {
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " To win, you need to destroy the enemy nexus.", 
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " destroy the nexus, and help our teammates"
 });

    this._help =  config.getStringList("tips").toArray(new String[0]);

      this.canJoinAfterStart=true;
    HungerSet = 20;
    DeathOut = false;
    this.BlockBreak = true;
    this.BlockPlace = true;
    this.DeathDropItems = true;
    this.ItemDrop = true;
    this.ItemPickup = true;
    fase = 1;
        this.WorldTimeSet = 10000;
    this.DeathSpectateSecs = config.getInt("GameConfig.DeathSpectateSecs");
    
  }


 @EventHandler
 public void DisplayFase(UpdateEvent event) throws IllegalArgumentException, IllegalAccessException{
     if(event.getType() != UpdateType.SEC2){
         return;
     }
    
     if(!this.IsLive()){
         return;
     }
int lastFase = fase;
    if((startTime + config.getLong("GameConfig.fase4time")*1000) < System.currentTimeMillis()){
        if(fase != 4){
         fase = 4;            
     for(Player pe : this.GetPlayers(false)){
         giveOP(pe);
         pe.getWorld().playSound(pe.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
         
       UtilDisplay.sendTitle(pe, config.getString("titles.fase4") ,null);
     }
    }
     }
    else if((startTime + config.getLong("GameConfig.fase3time")*1000) < System.currentTimeMillis()){
        if(fase != 3){
         fase = 3;            
     for(Player pe : this.GetPlayers(false)){
         pe.getWorld().playSound(pe.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
          UtilDisplay.sendTitle(pe, config.getString("titles.fase3"),null);
     }
     
        }
     }

    else if((startTime + config.getLong("GameConfig.fase2time")*1000) < System.currentTimeMillis() ){
        if(fase != 2){
         fase = 2;            
     for(Player pe : this.GetPlayers(false)){
         pe.getWorld().playSound(pe.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
       UtilDisplay.sendTitle(pe, config.getString("titles.fase2"),null);
     }
        }
     }
String text = config.getString("titles.fase1");
if(fase == 2){
    text = config.getString("titles.fase2");
}else if(fase == 3){
    text = config.getString("titles.fase3");
}else if(fase == 4){
    text = config.getString("titles.fase4");
}
     for(Player pe : this.GetPlayers(false)){
     UtilDisplay.displayTextBar(Manager.GetPlugin(), pe, 100, ChatColor.YELLOW+""+ChatColor.BOLD+config.getString("titles.barmain")+text);
     }
     }
 
 
 
PotionEffect hp = new PotionEffect(PotionEffectType.HEALTH_BOOST,6000,5);
PotionEffect spd = new PotionEffect(PotionEffectType.SPEED,6000,1);
PotionEffect abp = new PotionEffect(PotionEffectType.FIRE_RESISTANCE,6000,3);
PotionEffect more = new PotionEffect(PotionEffectType.JUMP,6000,0);
PotionEffect more2 = new PotionEffect(PotionEffectType.ABSORPTION,6000,3);

 public void giveOP(Player p){

p.addPotionEffect(hp,false);
p.addPotionEffect(spd,false);
p.addPotionEffect(abp,false);
p.addPotionEffect(more,false);
p.addPotionEffect(more2,false);
     
 }
  @EventHandler(priority = EventPriority.HIGHEST)
  public void UltraFase(CustomDamageEvent event)
  {
         event.SetIgnoreArmor(false);
         event.SetIgnoreRate(false); 
         event.SetDamageToLevel(false);
if(event.GetDamageePlayer() instanceof Player && event.GetDamagerPlayer(false) instanceof Player  && event.GetDamageePlayer().getHealth()-event.GetDamage() < 1){
      UtilDisplay.sendTitle(event.GetDamageePlayer(), config.getString("titles.death"), ""+event.GetDamagerPlayer(false).getDisplayName());
}
    if(this.fase != 4){
        return;
    }
     if(event.GetDamageeEntity() == null){
         return;
     }
     if(!(event.GetDamageeEntity() instanceof Player)){
         return;
     }
      final Player p1 = event.GetDamageePlayer();
     if(GetTeam(p1) == null){
         return;
     }

      if(p1.getHealth() - event.GetDamage() < 1)    {
    
       BukkitTask task = new BukkitRunnable()
      {
        @Override
        public void run()
        {
giveOP(p1);
      }}
        .runTaskLater(Manager.GetPlugin(), 20L);
    }
    
   
  }
  
  @Override
  public void ParseData()
  {
    startTime = System.currentTimeMillis();
    coreLocs = WorldData.GetDataLocs("YELLOW");

    this.GetTeamList().get(1).SetColor(ChatColor.AQUA);
    this.GetTeamList().get(0).SetColor(ChatColor.RED);

      coreLocs.get(0).getWorld().spawnEntity(coreLocs.get(0),EntityType.ENDER_CRYSTAL);
      coreLocs.get(1).getWorld().spawnEntity(coreLocs.get(1),EntityType.ENDER_CRYSTAL);
      //coreLocs.get(0).getWorld().getBlockAt(coreLocs.get(0)).setTypeIdAndData(Material.STAINED_GLASS_PANE.getId(), (byte)5, true);
      //coreLocs.get(1).getWorld().getBlockAt(coreLocs.get(1)).setTypeIdAndData(Material.STAINED_GLASS_PANE.getId(), (byte)5, true);
      this.twoCores = false;
    
    if(coreLocs.size() == 4){
        coreLocs.get(2).getWorld().spawnEntity(coreLocs.get(2),EntityType.ENDER_CRYSTAL);
        coreLocs.get(3).getWorld().spawnEntity(coreLocs.get(3),EntityType.ENDER_CRYSTAL);
    //coreLocs.get(2).getWorld().getBlockAt(coreLocs.get(2)).setTypeIdAndData(Material.STAINED_GLASS_PANE.getId(), (byte)5, true);
    //coreLocs.get(3).getWorld().getBlockAt(coreLocs.get(3)).setTypeIdAndData(Material.STAINED_GLASS_PANE.getId(), (byte)5, true);
    this.twoCores = true;
  }
       this.fase = 1;
  
  }
 private boolean breakRun = false;
  @EventHandler
  public void updateCores(UpdateEvent event)
  {
      if(event.getType() != UpdateType.FAST){
          return;
      }
      if(!this.IsLive()){
          return;
      }
if(breakRun == true){
    return;
}
if(this.twoCores){
    if(this.bluecore1Health <= 0){
        for(Entity et : coreLocs.get(0).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(0).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(0).getWorld().spawnEntity(coreLocs.get(0),EntityType.ENDER_CRYSTAL);
        }
    }
    if(this.bluecore2Health <= 0){
        for(Entity et : coreLocs.get(1).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(1).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(1).getWorld().spawnEntity(coreLocs.get(1),EntityType.ENDER_CRYSTAL);
        }
    }
    if(this.redcore1Health <= 0){
        for(Entity et : coreLocs.get(2).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(2).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(2).getWorld().spawnEntity(coreLocs.get(2),EntityType.ENDER_CRYSTAL);
        }
    }
    if(this.redcore2Health <= 0){
        for(Entity et : coreLocs.get(3).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(3).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(3).getWorld().spawnEntity(coreLocs.get(3),EntityType.ENDER_CRYSTAL);
        }
    }
}
      else {
    if(this.bluecore1Health <= 0){
        for(Entity et : coreLocs.get(0).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(0).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(0).getWorld().spawnEntity(coreLocs.get(0),EntityType.ENDER_CRYSTAL);
        }
    }
    if(this.redcore1Health <= 0){
        for(Entity et : coreLocs.get(1).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                et.remove();
                break;
            }
        }

    }else{
        boolean inzone = false;
        for(Entity et : coreLocs.get(1).getChunk().getEntities()){
            if(et.getType() == EntityType.ENDER_CRYSTAL){
                inzone = true;
                break;
            }
        }
        if(!inzone){
            coreLocs.get(1).getWorld().spawnEntity(coreLocs.get(1),EntityType.ENDER_CRYSTAL);
        }
    }
}

  }
  @EventHandler(priority = EventPriority.HIGHEST)
  public void noLava(PlayerInteractEvent event){
    if(event.getPlayer().getItemInHand() != null){
        if(event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET ){
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}
  @EventHandler(priority = EventPriority.HIGHEST)
  public void noLava(BlockPlaceEvent event){
    if(event.getPlayer().getItemInHand() != null){
        if(event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET || 
                event.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("piston")){
            event.getPlayer().setItemInHand(new ItemStack(Material.AIR));
            event.setCancelled(true);
        }
    }
}
  @EventHandler
  public void MoveKits(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Live) {
      return;
    }
    for (int i = 0; (i < WorldData.GetDataLocs("BLUEMOB").size()) && i < GetKits().length; i++)
    {

        CreatureAllowOverride = true;
        Entity ent = GetKits()[i].SpawnEntity((Location)WorldData.GetDataLocs("BLUEMOB").get(i));
        UtilEnt.Vegetate(ent);
        
        
        CreatureAllowOverride = false;

        Manager.GetLobby().AddKitLocation(ent, GetKits()[i], (Location)WorldData.GetDataLocs("BLUEMOB").get(i));
      }
    for (int i = 0; (i < WorldData.GetDataLocs("REDMOB").size()) && i < GetKits().length; i++)
    {

        CreatureAllowOverride = true;
        Entity ent = GetKits()[i].SpawnEntity((Location)WorldData.GetDataLocs("REDMOB").get(i));
        UtilEnt.Vegetate(ent);
        
        CreatureAllowOverride = false;

        Manager.GetLobby().AddKitLocation(ent, GetKits()[i], (Location)WorldData.GetDataLocs("REDMOB").get(i));
      }
    
  }
  
  public void runCore(final Entity et, final int core){
      this.breakRun = true;
       BukkitTask task = new BukkitRunnable()
      {
        @Override
        public void run()
        {
      breakRun = false;
if(DTN.this.twoCores == true){
 if(core == 0){
  if(DTN.this.bluecore1Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 }
 }else if (core == 1){
  if(DTN.this.bluecore2Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 }
 }else if(core == 2){
  if(DTN.this.redcore1Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 }
 }else if(core == 3){
  if(DTN.this.redcore2Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 } 
 }
}else{
 if(core == 0){
  if(DTN.this.bluecore1Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 }
 }else if (core == 1){
  if(DTN.this.redcore1Health <= 0){

final TNTPrimed tnt = (TNTPrimed)et.getWorld().spawn(et.getLocation(), (Class)TNTPrimed.class);
((CraftTNTPrimed)tnt).getHandle().isIncendiary = true;
((CraftTNTPrimed)tnt).getHandle().setInvisible(true);
return;
 }
 }
}


        }}
        .runTaskLater(Manager.GetPlugin(), 120L);
    
  }

    HashMap<Player,Long> lastCoreHit = new HashMap();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onnewCoreDMG(EntityDamageByEntityEvent event){
      if(event.getEntity()==null){
          return;
      }
      if(event.getEntityType() != EntityType.ENDER_CRYSTAL){
          return;
      }
      event.setCancelled(true);
      Player p = null;
      if((event.getDamager() instanceof Player)){
          p =  (Player)event.getDamager();
      }
      if(event.getDamager() instanceof Projectile){
          if(((Projectile) event.getDamager()).getShooter() instanceof Player);
          p = (Player)((Projectile) event.getDamager()).getShooter();
      }
      if(p==null){
          return;
      }

      if(!lastCoreHit.containsKey(p)){
          lastCoreHit.put(p,System.currentTimeMillis()+750L);
      }
      if(lastCoreHit.get(p) > System.currentTimeMillis()){
          return;
      }else{
          lastCoreHit.put(p,System.currentTimeMillis()+750L);
      }

      int dmg = 1;
      if(this.fase == 1){

          event.getDamager().sendMessage(ChatColor.RED+this.config.getString("messages.fase1destroy"));
          event.getDamager().sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+this.config.getString("messages.wait")+" "+(((startTime  + 300000) - System.currentTimeMillis()) / 1000)+"s");
         event.setCancelled(true);
return;
      }else if(this.fase >= 3){
          dmg = 2;
      }
      Location loc = event.getEntity().getLocation();
      
if(this.coreLocs.size() == 2){
    if(this.coreLocs.get(0).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.RED)){
 event.setCancelled(true);
 return;
}
 this.bluecore1Health -= dmg;
  this.runCore(event.getEntity(),0);

    }else if(this.coreLocs.get(1).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.AQUA)){
 event.setCancelled(true);
 return;
}
 
 this.redcore1Health -= dmg;
 this.runCore(event.getEntity(),1);

    }
}else if(this.coreLocs.size() == 4){
    if(this.coreLocs.get(0).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.RED)){
 event.setCancelled(true);
 return;
}
 this.bluecore1Health -= dmg;
  this.runCore(event.getEntity(),0);

 event.setCancelled(true);
    }else if(this.coreLocs.get(1).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.RED)){
 event.setCancelled(true);
 return;
}
 this.bluecore2Health -= dmg;
  this.runCore(event.getEntity(),1);


    }
    else if(this.coreLocs.get(2).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.AQUA)){
 event.setCancelled(true);
 return;
}
 
 this.redcore1Health -= dmg;
 this.runCore(event.getEntity(),2);



    } else if(this.coreLocs.get(3).distanceSquared(loc) <= 1) {
 if(this.GetTeam(p) != this.GetTeam(ChatColor.AQUA)){
 event.setCancelled(true);
 return;
}
 
 this.redcore2Health -= dmg;
 this.runCore(event.getEntity(),3);
 


    }
}

p.getWorld().playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);

fixHealth();
  }


public void fixHealth(){
    if(this.redcore1Health < 0){
        this.redcore1Health = 0;
    }
    if(this.redcore2Health < 0){
        this.redcore2Health = 0;
    }
    if(this.bluecore1Health < 0){
        this.bluecore1Health = 0;
    }
    if(this.bluecore2Health < 0){
        this.bluecore2Health = 0;
    }
    
}

public boolean distance(Location loca2, Location spawn2, int dist){
Location loca = new Location(loca2.getWorld(),loca2.getX(),30,loca2.getZ());

Location spawn = new Location(spawn2.getWorld(),spawn2.getX(),30,spawn2.getZ());
    
    return loca.distance(spawn) <= dist -2;
}
  
  @EventHandler(priority = EventPriority.NORMAL)
public void prevent(BlockPlaceEvent event){
        if(event.getPlayer().getWorld() != event.getBlock().getWorld()){
        event.setCancelled(true);
        return;
    }
if(this.distance(this.GetTeamList().get(0).GetSpawn(), event.getBlock().getLocation(), 12)
        || this.distance(this.GetTeamList().get(1).GetSpawn(), event.getBlock().getLocation(), 12)){
        event.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant place blocks in the spawn zone");
        event.setCancelled(true);

    }
}

  @EventHandler(priority = EventPriority.NORMAL)
public void prevent(BlockBreakEvent eve){
        if(eve.getPlayer().getWorld() != eve.getBlock().getWorld()){
        eve.setCancelled(true);
        return;
    }
if(this.distance(this.GetTeamList().get(0).GetSpawn(), eve.getBlock().getLocation(), 12)
        || this.distance(this.GetTeamList().get(1).GetSpawn(), eve.getBlock().getLocation(), 12)){
      eve.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant destroy in the spawn zone");
       
    eve.setCancelled(true);
    }
     
}
    /*
  @EventHandler(priority = EventPriority.NORMAL)
public void prevent2(BlockBreakEvent eve){
        if(eve.getPlayer().getWorld() != eve.getBlock().getWorld()){
        eve.setCancelled(true);
        return;
    }
    if(this.coreLocs.contains(eve.getBlock().getLocation())){
        return;
    }

List<Location> blueLocations = new ArrayList<>();
List<Location> redLocations = new ArrayList<>();
if(this.twoCores){
blueLocations.add(this.coreLocs.get(0));blueLocations.add(this.coreLocs.get(1));
redLocations.add(this.coreLocs.get(2));redLocations.add(this.coreLocs.get(3));
}else{
  blueLocations.add(this.coreLocs.get(0));
  redLocations.add(this.coreLocs.get(1));
}



for(Location blue : blueLocations){
    if(this.distance(eve.getBlock().getLocation(), blue, 1) ){
   eve.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant destroy in the nexus zone");
        eve.setCancelled(true);
        return;
    }
}
for(Location red2 : redLocations){
    if(this.distance(eve.getBlock().getLocation(), red2, 1) ){
        eve.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant destroy in the nexus zone");
        eve.setCancelled(true);
        return;
    }
}
    }
     */


  @EventHandler(priority = EventPriority.HIGH)
public void prevent2(BlockPlaceEvent eve){
    if(eve.getPlayer().getWorld() != eve.getBlock().getWorld()){
        eve.setCancelled(true);
        return;
    }
    if(this.coreLocs.contains(eve.getBlock().getLocation())){
        return;
    }
    
List<Location> blueLocations = new ArrayList<>();
List<Location> redLocations = new ArrayList<>();
if(this.twoCores){
blueLocations.add(this.coreLocs.get(0));blueLocations.add(this.coreLocs.get(1));
redLocations.add(this.coreLocs.get(2));redLocations.add(this.coreLocs.get(3));    
}else{
  blueLocations.add(this.coreLocs.get(0));
  redLocations.add(this.coreLocs.get(1));
}

for(Location blue : blueLocations){
    if((this.distance(eve.getBlock().getLocation(), blue, 1) )){
   eve.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant place blocks here");
        eve.setCancelled(true);
        return;
    }
}
for(Location red2 : redLocations){
    if((this.distance(eve.getBlock().getLocation(), red2, 1) )){
        eve.getPlayer().sendMessage(ChatColor.RED+""+ChatColor.BOLD+"You cant place blocks here");
        eve.setCancelled(true);
        return;
    }
}
}

  @EventHandler(priority = EventPriority.HIGHEST)
public void noBreak(BlockBreakEvent eve){
    if(eve.getPlayer().getGameMode() != GameMode.CREATIVE)
    if(eve.getBlock().getWorld().getName().equalsIgnoreCase("world")){
        eve.setCancelled(true);
    }
}

  
  @EventHandler(priority = EventPriority.HIGHEST)
public void noBreak(BlockPlaceEvent eve){
        if(eve.getPlayer().getGameMode() != GameMode.CREATIVE)
    if(eve.getBlock().getWorld().getName().equalsIgnoreCase("world")){
        eve.setCancelled(true);
    }
} 

  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
  String border1 = null;
  String border2 = null;
  String border3 = null;
  String map = null;
          
  @EventHandler
  @Override
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC2) {
      return;
    }
if(border1 == null){
    border1 =  "      ";
}
if(border2 == null){
    border2 =  "    ";
}
if(border3 == null){
    border3 =  "     ";
}
if(map == null){
 map =  Color("&6Mapa");
}

    
    if (!(this.bluecore1Health < 0))
    {
      GetObjectiveSide().getScore(Color(""+ChatColor.BLUE+"Nexo1")).setScore(this.bluecore1Health);
    }
    
    if (!(this.redcore1Health < 0))
    {
      GetObjectiveSide().getScore(Color(""+ChatColor.RED+"Nexo1")).setScore(this.redcore1Health);
    }
    
    if(this.twoCores){
        if (!(this.redcore2Health < 0))
    {
      GetObjectiveSide().getScore(Color(""+ChatColor.RED+"Nexo2")).setScore(this.redcore2Health);
    }
        if (!(this.bluecore2Health < 0))
    {
      GetObjectiveSide().getScore(Color(""+ChatColor.BLUE+"Nexo2")).setScore(this.bluecore2Health);
    }
    }
    String worldname = WorldData.MapName;
    if(worldname.length() > 14){
        worldname = worldname.substring(0, 14);
    }
    
    GetObjectiveSide().getScore(Color(border1)).setScore(1);
      GetObjectiveSide().getScore("www.mineultra.com").setScore(0);
    GetObjectiveSide().getScore(Color(border2)).setScore(101);
    GetObjectiveSide().getScore(Color(border3)).setScore(104);
    GetObjectiveSide().getScore(Color(map)).setScore(103);
    if(worldname != null){
        if(!("null".equals(worldname.toLowerCase()))){
    GetObjectiveSide().getScore(Color(worldname)).setScore(102);
            
        }
    }
    
    HashMap _scoreGroup = new HashMap();
        
    
    for (String group : (Set<String>)_scoreGroup.keySet())
    {
      GetObjectiveSide().getScore(group).setScore(((Integer)_scoreGroup.get(group)));
    }
  for(GameTeam team : this.GetTeamList()){
    for(Player player : team.GetPlayers(false)){
    this.SetPlayerScoreboardTeam(player, team.GetName());
  } 
  }
  }
  
  
  
 
  
  @Override
  public void EndCheck()
  {
    if (!IsLive()){
        return;
    }
if(this.bluecore1Health < 0){
    this.bluecore1Health = 0;
}
if(this.bluecore2Health < 0){
    this.bluecore2Health = 0;
}
if(this.redcore1Health < 0){
    this.redcore1Health = 0;
}
if(this.redcore2Health < 0){
    this.redcore2Health = 0;
}

if(this.twoCores){
    if (this.bluecore1Health <= 0 && this.bluecore2Health <= 0 
            || this.redcore1Health <= 0 &&  this.redcore2Health <= 0
        || this.GetTeamList().get(0).GetSize() <= 0
            || this.GetTeamList().get(1).GetSize() <= 0)
    {
        endTwo();
    }    

}else if(this.bluecore1Health <= 0 
        || this.redcore1Health <= 0
        || this.GetTeamList().get(0).GetSize() <= 0 
        || this.GetTeamList().get(1).GetSize() <= 0){
    endSingle();
}

  }

public void endSingle(){

      ChatColor winnerTeam = ChatColor.RED;
      String name = "";
      if(this.bluecore1Health > 0){
          winnerTeam = ChatColor.AQUA;
          name = "Blue";
      }else{
          winnerTeam = ChatColor.RED;
          name = "Red";
      }
      
      this.WinnerTeam = this.GetTeam(winnerTeam);

        for (GameTeam team : GetTeamList()) {
            if ((WinnerTeam != null) && (team.equals(WinnerTeam)))
            {
                for (Player player : team.GetPlayers(false))
                {
                    db.getPlayerData(player).addWinned(1);
                    AddGems(player, 25.0D, "Winner Team", false);
                }
            }
          else
          for (Player player : team.GetPlayers(false)) {
              if (player.isOnline()) {
                  db.getPlayerData(player).addLoss(1);
                  AddGems(player, 10.0D, "Participation", false);
              }
          }
}

      SetCustomWinLine(C.cDGreen+"The team "+winnerTeam+name+ C.cDGreen + " has destroyed the enemy Nexus!");
      AnnounceEnd(GetTeam(winnerTeam));
      SetState(Game.GameState.End);
}


public void endTwo(){

      ChatColor winnerTeam = ChatColor.RED;

      String name = "";
      if(this.bluecore1Health + this.bluecore2Health > 0){
          winnerTeam = ChatColor.AQUA;
          name = "Blue";
      }else{
          winnerTeam = ChatColor.RED;
          name = "Red";
      }
      this.WinnerTeam = this.GetTeam(winnerTeam);

        for (GameTeam team : GetTeamList()) {
            if ((WinnerTeam != null) && (team.equals(WinnerTeam)))
            {
                for (Player player : team.GetPlayers(false))
                {
                    AddGems(player, 25.0D, "Winner Team", false);
                }
            }
            
            else
          for (Player player : team.GetPlayers(false)) {
              if (player.isOnline()) {
                  AddGems(player, 10.0D, "Participation", false);
              }
          }
}
      
      SetCustomWinLine(C.cDGreen+"The team "+winnerTeam+name+ C.cDGreen + " has destroy the enemy Nexus!");      
      AnnounceEnd(GetTeam(winnerTeam));
SetState(Game.GameState.End);

}

}