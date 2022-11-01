package me.peacefulmen.turbolobby;

import de.inventivegames.util.title.TitleManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.peacefulmen.turbolobby.MySQL;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class TurboLobby
  extends JavaPlugin
  implements Listener

{

  JavaPlugin plugin = null;
  String hcmgame = null;
  
  int turbocost = -1;
  int framecost = -1;
  int enginecost = -1;
  int sellpercent = -1;
  int epicpartcost = -1;
  Language lg = null;
  @Override
  public void onEnable()
  {
    this.saveDefaultConfig();
      plugin = this;
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this, this);
turbocost = this.getConfig().getInt("TurboRacers.parts.levelcost.turbo");
enginecost = this.getConfig().getInt("TurboRacers.parts.levelcost.engine");
framecost = this.getConfig().getInt("TurboRacers.parts.levelcost.frame");
sellpercent = this.getConfig().getInt("TurboRacers.parts.sellpercent");
epicpartcost = this.getConfig().getInt("TurboRacers.parts.epicpartcost");
hcmgame = this.getConfig().getString("TurboRacers.HCPuntosName");
this.sslot = this.getConfig().getInt("TurboRacers.itemshopslot");
lg = new Language(this.getConfig());


this.init();
}
  


  @Override
  public void onDisable()
  {  
      
  }
  


    String host = "127.0.0.1";
    String port = "3306";
    String db = "stats";
    String user = "root";
    String pwd = "trabek123-";
    String table = "test";
    
    boolean enabled = true;
    java.sql.Connection data = null;
    
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
       }
        
    }
    
    
 
    
    HashMap<Player,Scoreboard> scoreboards = new HashMap();
    HashMap<Player,Objective> objs = new HashMap();
@EventHandler
public void onJoin(PlayerJoinEvent event) throws SQLException{
    
       if(enabled == false){
            return;
        }
        Player p = event.getPlayer();
String sqlinsert = "INSERT INTO `"+table+"` ("
        + "Name,"
        + "UUID,"
        + columnType.Engine.getName()+","
        + columnType.FirstPlace.getName()+","
        + columnType.Frame.getName()+","
        + columnType.Helmets.getName()+","
        + columnType.Horns.getName()+","
        + columnType.KartSkin.getName()+","
        + columnType.ParticleQuality.getName()+","
        + columnType.ParticleTrail.getName()+","
        + columnType.RacingSuits.getName()+","
        + columnType.SecondPlace.getName()+","
        + columnType.ThirdPlace.getName()+","
        + columnType.Played.getName()+","
        + columnType.Laps.getName()+","
        + columnType.Turbocharger.getName()        
        +") \n" +
"  SELECT '"+p.getName()+"','"+p.getUniqueId().toString()+"','0,0,0-0,0,0','0','0,0,0-0,0,0','0-0','0-0','0-0','2','0-0','0-0','0','0','0','0','0,0,0-0,0,0' FROM dual\n" +
"WHERE NOT EXISTS \n" +
"  (SELECT UUID FROM "+table+" WHERE UUID='"+p.getUniqueId().toString()+"');";
    
        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            System.out.println("Record not inserted");
        }
    

if(!scoreboards.containsKey(p)){
   scoreboards.put(p, Bukkit.getScoreboardManager().getNewScoreboard());
}
Scoreboard sco = scoreboards.get(p);

if(!objs.containsKey(p)){
    objs.put(p, sco.registerNewObjective("Obj "+r.nextInt(999999), "dummy"));
}

Objective obj = objs.get(p);
obj.setDisplaySlot(DisplaySlot.SIDEBAR);
obj.setDisplayName(Colorizer.Color(this.getConfig().getString("TurboRacers.scoreboard")));
int next = 16;
obj.getScore("  ").setScore(next--);
obj.getScore(Colorizer.Color(lg.firstplace+Integer.parseInt(this.getValue(p, columnType.FirstPlace)))).setScore(next--);
obj.getScore(Colorizer.Color(lg.secondplace+Integer.parseInt(this.getValue(p, columnType.SecondPlace)))).setScore(next--);
obj.getScore(Colorizer.Color(lg.thirdplace+Integer.parseInt(this.getValue(p, columnType.ThirdPlace)))).setScore(next--);
obj.getScore(Colorizer.Color(lg.laps+Integer.parseInt(this.getValue(p, columnType.Laps)))).setScore(next--);
obj.getScore(Colorizer.Color(lg.played+Integer.parseInt(this.getValue(p, columnType.Played)))).setScore(next--);
obj.getScore("   ").setScore(next--);
obj.getScore(Colorizer.Color(lg.coins+this.getMinimizedCoins(p))).setScore(next--);
obj.getScore("    ").setScore(next--);
obj.getScore(Colorizer.Color(this.getConfig().getString("TurboRacers.website"))).setScore(next--);

p.setScoreboard(sco);

}

public String getMinimizedCoins(Player pe){
int size = this.getviewPoints(pe);
String ret = "";

//100.000 100k,  999.999 = 999k
// 1.000.000 = 1m
if(size >= 1000000){
    ret = ""+(size/1000000)+"m";
}
else if(size >= 1000){
    ret = ""+(size / 1000)+"k";
}else{
    ret = ""+(size);
}

return ret;
}



@EventHandler
public void onQuit(PlayerQuitEvent event){
    if(scoreboards.containsKey(event.getPlayer())){
        scoreboards.get(event.getPlayer()).clearSlot(DisplaySlot.SIDEBAR);
        scoreboards.remove(event.getPlayer());
        objs.remove(event.getPlayer());
    }
}

    
public void buyItem(int type, Player p, columnType column,String name, int price) throws SQLException{
    if(this.ownsItem(p,type,column)){
       p.sendMessage(Colorizer.Color(lg.equipped+name));
        this.setValueSelected(p, column, ""+type);
        p.closeInventory();
    }
    else
  if(this.getPointsAPI(p).look(p.getUniqueId()) >  price){
     this.getPointsAPI(p).take(p.getUniqueId(), price);
      p.sendMessage(Colorizer.Color(lg.buyed+name));
      this.addValue(p, column, ","+type, false);
  p.closeInventory();
  }else{
      p.sendMessage(Colorizer.Color(lg.insufficientmoney));
  p.closeInventory();
  }
      
}



@EventHandler
public void onBuying(InventoryClickEvent event) throws SQLException{
    if(event.getCurrentItem() == null){
        return;
    }
    if(!event.getCurrentItem().hasItemMeta()){
        return;
    }
    
    String displayname = event.getCurrentItem().getItemMeta().getDisplayName();
    String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
    Player pe = (Player)event.getWhoClicked();
    int slot = event.getSlot();

    ItemStack it = event.getCurrentItem();
    if(name.contains(lg.goback)){
        pe.closeInventory();
        this.openShop(pe);
    }
    
    if(event.getInventory().getName().equalsIgnoreCase(lg.helmetselector)){
    event.setCancelled(true);
int helmtype = -1;

    for(int mat : helmname.keySet()){
if(!helmname.get(mat).equalsIgnoreCase(name)){
    continue;
}
helmtype = mat;}
if(helmtype != -1)
this.buyItem(helmtype, pe,columnType.Helmets,helmname.get(helmtype),helmprice.get(helmtype));
    }
    else if(event.getInventory().getName().equalsIgnoreCase(lg.particletrailselector)){
    event.setCancelled(true);
int helmtype = -1;

    for(int mat : trailname.keySet()){
if(!trailname.get(mat).equalsIgnoreCase(name)){
    continue;
}
helmtype = mat;}
if(helmtype != -1)
this.buyItem(helmtype, pe,columnType.ParticleTrail,trailname.get(helmtype),trailprice.get(helmtype));
    }
    else
    if(event.getInventory().getName().equalsIgnoreCase(lg.hornselector)){
    event.setCancelled(true);
int helmtype = -1;

    for(int mat : hornname.keySet()){
if(!hornname.get(mat).equalsIgnoreCase(name)){
    continue;
}
helmtype = mat;}
if(helmtype != -1)
this.buyItem(helmtype, pe,columnType.Horns,hornname.get(helmtype),hornprice.get(helmtype));
}
  else  if(event.getInventory().getName().equalsIgnoreCase(lg.carskinselector)){
    event.setCancelled(true);
int skintype = -1;

    for(int mat : skinname.keySet()){
if(!skinname.get(mat).equalsIgnoreCase(name)){
    continue;
}
skintype = mat;}
if(skintype != -1)
this.buyItem(skintype, pe,columnType.KartSkin,skinname.get(skintype),skinprice.get(skintype));
    }
    
    else if(event.getInventory().getName().equalsIgnoreCase(lg.racesuitselector)){
 for(int a = 0; a < 5;a++){
    if(this.getSuitName(a, 0).equalsIgnoreCase(name)
      || this.getSuitName(a, 1).equalsIgnoreCase(name)
      || this.getSuitName(a, 2).equalsIgnoreCase(name)){
this.buyItem(a, pe, columnType.RacingSuits,lg.racesuit, this.getConfig().getInt("TurboRacers.suits.price.L"+a));
return;        
}
}
    }
    else if(event.getInventory().getName().equalsIgnoreCase(lg.partcrafterselector)){
      event.setCancelled(true);

      if(slot == 5){
         int pcnow = this.PCType.get(pe);
         if(pcnow >= 2){
             PCType.put(pe, 0);
         }else{
             PCType.put(pe,pcnow+1);
         }
      }
      if(slot == 3){
          int pcnow = this.PCType.get(pe);
          if(pcnow <= 0){
              pcnow = 2;
          }else{
              pcnow--;
          }
          PCType.put(pe, pcnow);
      }
      
        if(this.PCType.get(pe) == 0){
        if(slot == 49){
int val1 = this.PCEngineRecovery.get(pe);
int val2 = this.PCEngineTopSpeed.get(pe);
int val3 = this.PCEngineAceleracion.get(pe);
int total = val1+val2+val3;
int cost = this.getPartCost(pe, val1+val2+val3, this.enginecost);
if(this.getPointsAPI(pe).look(pe.getUniqueId()) >= cost){
    this.getPointsAPI(pe).take(pe.getUniqueId(), cost);
    this.addValue(pe, columnType.Engine, ";"+val1+","+val2+","+val3, false);
    Bukkit.broadcastMessage(Colorizer.Color(pe.getDisplayName()+lg.obtained+this.PCNameLevel(total)+lg.engine));
    pe.closeInventory();
}else{
    pe.sendMessage(Colorizer.Color(lg.insufficientmoney));
    pe.closeInventory();
}
}
        if(slot == 19){
            int current = this.PCEngineRecovery.get(pe);
            if(current > 0){
                this.PCEngineRecovery.put(pe, current-1);
            } 
        }
        if(slot == 28){
            int current = this.PCEngineTopSpeed.get(pe);
            if(current > 0){
                this.PCEngineTopSpeed.put(pe, current-1);
            } 
        }
        if(slot == 37){
            int current = this.PCEngineAceleracion.get(pe);
            if(current > 0){
                this.PCEngineAceleracion.put(pe, current-1);
            }
        }
        if(slot == 25){
            int current = this.PCEngineRecovery.get(pe);
            if(current < 5){
                this.PCEngineRecovery.put(pe, current+1);
            } 
        }
        if(slot == 34){
            int current = this.PCEngineTopSpeed.get(pe);
            if(current < 5){
                this.PCEngineTopSpeed.put(pe, current+1);
            } 
        }
        if(slot == 43){
            int current = this.PCEngineAceleracion.get(pe);
            if(current < 5){
                this.PCEngineAceleracion.put(pe, current+1);
            }
        }
            
       
        }
        
        else  if(this.PCType.get(pe) == 1){
        if(slot == 49){
int val1 = this.PCTChargerBoosterSpeed.get(pe);
int val2 = this.PCTChargerDerrape.get(pe);
int val3 = this.PCTChargerStopQuicker.get(pe);
int total = val1+val2+val3;
int cost = this.getPartCost(pe, val1+val2+val3, this.turbocost);
if(this.getPointsAPI(pe).look(pe.getUniqueId()) >= cost){
    this.getPointsAPI(pe).take(pe.getUniqueId(), cost);
    this.addValue(pe, columnType.Turbocharger, ";"+val1+","+val2+","+val3, false);
    Bukkit.broadcastMessage(Colorizer.Color(pe.getDisplayName()+lg.obtained
            +this.PCNameLevel(total)+lg.turbo));
    pe.closeInventory();
}else{
    pe.sendMessage(Colorizer.Color(lg.insufficientmoney));
    pe.closeInventory();
}
}
            
        if(slot == 19){
            int current = this.PCTChargerBoosterSpeed.get(pe);
            if(current > 0){
                this.PCTChargerBoosterSpeed.put(pe, current-1);
            } 
        }
        if(slot == 28){
            int current = this.PCTChargerDerrape.get(pe);
            if(current > 0){
                this.PCTChargerDerrape.put(pe, current-1);
            }
        }
        if(slot == 37){
            int current = this.PCTChargerStopQuicker.get(pe);
            if(current > 0){
                this.PCTChargerStopQuicker.put(pe, current-1);
            } 
        }
        if(slot == 25){
            int current = this.PCTChargerBoosterSpeed.get(pe);
            if(current < 5){
                this.PCTChargerBoosterSpeed.put(pe, current+1);
            } 
        }
        if(slot == 34){
            int current = this.PCTChargerDerrape.get(pe);
            if(current < 5){
                this.PCTChargerDerrape.put(pe, current+1);
            }
        }
        if(slot == 43){
            int current = this.PCTChargerStopQuicker.get(pe);
            if(current < 5){
                this.PCTChargerStopQuicker.put(pe, current+1);
            }
        }
            
        }
        
        else if(this.PCType.get(pe)==2){
        if(slot == 49){
int val1 = this.PCFrameManejo.get(pe);
int val2 = this.PCFrameStartPosition.get(pe);
int val3 = this.PCFrameTraccion.get(pe);
int total = val1+val2+val3;
int cost = this.getPartCost(pe, val1+val2+val3, this.framecost);
if(this.getPointsAPI(pe).look(pe.getUniqueId()) >= cost){
    this.getPointsAPI(pe).take(pe.getUniqueId(), cost);
    this.addValue(pe, columnType.Frame, ";"+val1+","+val2+","+val3, false);
    Bukkit.broadcastMessage(Colorizer.Color(pe.getDisplayName()+lg.obtained
            +this.PCNameLevel(total)+lg.chassis));
    pe.closeInventory();
}else{
    pe.sendMessage(Colorizer.Color(lg.insufficientmoney));
    pe.closeInventory();
}
}
        if(slot == 37){
            int current = this.PCFrameTraccion.get(pe);
            if(current > 0){
                this.PCFrameTraccion.put(pe, current-1);
            } 
        }
        if(slot == 19){
            int current = this.PCFrameManejo.get(pe);
            if(current > 0){
                this.PCFrameManejo.put(pe, current-1);
            } 
        }
        if(slot == 28){
            int current = this.PCFrameStartPosition.get(pe);
            if(current > 0){
                this.PCFrameStartPosition.put(pe, current-1);
            }
        }
        if(slot == 43){
            int current = this.PCFrameTraccion.get(pe);
            if(current < 5){
                this.PCFrameTraccion.put(pe, current+1);
            } 
        }
        if(slot == 25){
            int current = this.PCFrameManejo.get(pe);
            if(current < 5){
                this.PCFrameManejo.put(pe, current+1);
            } 
        }
        if(slot == 34){
            int current = this.PCFrameStartPosition.get(pe);
            if(current < 5){
                this.PCFrameStartPosition.put(pe, current+1);
            }
        }
        
        }
         this.updatePartCrafter(pe, event.getInventory());
    }else if(event.getInventory().getName().equalsIgnoreCase(lg.carcustomizer)){
        event.setCancelled(true);
        for(String str : this.kengine.keySet()){
            if(displayname.equalsIgnoreCase(str)){
           this.setValueSelected(pe, columnType.Engine, this.kengine.get(str));
           pe.sendMessage(Colorizer.Color(lg.equipped+displayname));
           pe.closeInventory();
            }
        }
        for(String str : this.kturbocharger.keySet()){
            if(displayname.equalsIgnoreCase(str)){
           this.setValueSelected(pe, columnType.Turbocharger, this.kturbocharger.get(str));
           pe.sendMessage(Colorizer.Color(lg.equipped+displayname));
           pe.closeInventory();
            }
        }
        for(String str : this.kframe.keySet()){
            if(displayname.equalsIgnoreCase(str)){
           this.setValueSelected(pe, columnType.Frame, this.kframe.get(str));
           pe.sendMessage(Colorizer.Color(lg.equipped+displayname));
           pe.closeInventory();
            }
        }
    }
    else if(event.getInventory().getName().equalsIgnoreCase(lg.partsseller)){
        event.setCancelled(true);
        for(String str : this.kengine.keySet()){
            if(displayname.equalsIgnoreCase(str)){
     String[] lvls = kengine.get(str).split(",");
     
     int level = Integer.parseInt(lvls[0])+Integer.parseInt(lvls[1])+Integer.parseInt(lvls[2]);
      
     int cost = (this.getPartCost(pe, level, enginecost)*sellpercent) / 100;
     this.removeValue(pe, columnType.Engine, kengine.get(str),true);
     this.getPointsAPI(pe).give(pe.getUniqueId(), cost);

     event.getInventory().clear();
     pe.sendMessage(Colorizer.Color(lg.yousell+str+lg.sellfor+cost));
     pe.closeInventory();
     
            }
        }
        for(String str : this.kturbocharger.keySet()){
            if(displayname.equalsIgnoreCase(str)){
     String[] lvls = kturbocharger.get(str).split(",");

     int level = Integer.parseInt(lvls[0])+Integer.parseInt(lvls[1])+Integer.parseInt(lvls[2]);

     int cost = (this.getPartCost(pe, level, turbocost)*sellpercent) / 100;
this.removeValue(pe, columnType.Turbocharger, kturbocharger.get(str),true);
     this.getPointsAPI(pe).give(pe.getUniqueId(), cost);
     event.getInventory().clear();
     pe.sendMessage(Colorizer.Color(lg.yousell+str+lg.sellfor+cost));
     pe.closeInventory();
     
            }
        }
        for(String str : this.kframe.keySet()){
            if(displayname.equalsIgnoreCase(str)){
     String[] lvls = kframe.get(str).split(",");

     int level = Integer.parseInt(lvls[0])+Integer.parseInt(lvls[1])+Integer.parseInt(lvls[2]);

     int cost = (this.getPartCost(pe, level, framecost)*sellpercent) / 100;
this.removeValue(pe, columnType.Frame, kframe.get(str),true);
     this.getPointsAPI(pe).give(pe.getUniqueId(), cost);
             
     event.getInventory().clear();
     pe.sendMessage(Colorizer.Color(lg.yousell+str+lg.sellfor+cost));
     pe.closeInventory();
     
            }
        }
        
    }
  

    

    

    

}


private PlayerPoints playerPoints;


private boolean hookPlayerPoints() {
    final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
    playerPoints = PlayerPoints.class.cast(plugin);
    return playerPoints != null; 
}


public PlayerPoints getPlayerPoints() {
    if(hookPlayerPoints()){
    return playerPoints;}
    else {
        return null;
    }
}

public PlayerPointsAPI getPointsAPI(Player pe){
    if(this.points.containsKey(pe.getName())){
        points.remove(pe.getName());
    }
    return getPlayerPoints().getAPI();
}

public void openSuits(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.racesuitselector);
    Material.getMaterial(1);
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+" &6"+this.getviewPoints(p), null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
   
    iv.setItem(11, this.itemShop(p, 0, this.getSuitCost("suit1-1", 0), Material.GOLD_CHESTPLATE, 1, (short)0, this.getSuitName(0, 0), columnType.RacingSuits, lg.racesuit));
    iv.setItem(12, this.itemShop(p, 1, this.getSuitCost("suit2-1", 0), Material.CHAINMAIL_CHESTPLATE, 1, (short)0, this.getSuitName(1, 0), columnType.RacingSuits, lg.racesuit));
    iv.setItem(13, this.itemShop(p, 2, this.getSuitCost("suit3-1", 0), Material.IRON_CHESTPLATE, 1, (short)0, this.getSuitName(2, 0), columnType.RacingSuits, lg.racesuit));
    iv.setItem(14, this.itemShop(p, 3, this.getSuitCost("suit4-1", 0), Material.DIAMOND_CHESTPLATE, 1, (short)0, this.getSuitName(3, 0), columnType.RacingSuits, lg.racesuit));
    iv.setItem(15, this.itemShop(p, 4, this.getSuitCost("suit5-1", 0), Material.LEATHER_CHESTPLATE, 1, (short)0, this.getSuitName(4, 0), columnType.RacingSuits, lg.racesuit));

    iv.setItem(20, this.itemShop(p, 0, this.getSuitCost("suit1-2", 0), Material.GOLD_LEGGINGS, 1, (short)0, this.getSuitName(0, 1), columnType.RacingSuits, lg.racesuit));
    iv.setItem(21, this.itemShop(p, 1, this.getSuitCost("suit2-2", 0), Material.CHAINMAIL_LEGGINGS, 1, (short)0, this.getSuitName(1, 1), columnType.RacingSuits, lg.racesuit));
    iv.setItem(22, this.itemShop(p, 2, this.getSuitCost("suit3-2", 0), Material.IRON_LEGGINGS, 1, (short)0, this.getSuitName(2, 1), columnType.RacingSuits, lg.racesuit));
    iv.setItem(23, this.itemShop(p, 3, this.getSuitCost("suit4-2", 0), Material.DIAMOND_LEGGINGS, 1, (short)0, this.getSuitName(3, 1), columnType.RacingSuits, lg.racesuit));
    iv.setItem(24, this.itemShop(p, 4, this.getSuitCost("suit5-2", 0), Material.LEATHER_LEGGINGS, 1, (short)0, this.getSuitName(4, 1), columnType.RacingSuits, lg.racesuit));
    
    iv.setItem(29, this.itemShop(p, 0, this.getSuitCost("suit1-3", 0), Material.GOLD_BOOTS, 1, (short)0, this.getSuitName(0, 2), columnType.RacingSuits, lg.racesuit));
    iv.setItem(30, this.itemShop(p, 1, this.getSuitCost("suit2-3", 0), Material.CHAINMAIL_BOOTS, 1, (short)0, this.getSuitName(1, 2), columnType.RacingSuits, lg.racesuit));
    iv.setItem(31, this.itemShop(p, 2, this.getSuitCost("suit3-3", 0), Material.IRON_BOOTS, 1, (short)0, this.getSuitName(2, 2), columnType.RacingSuits, lg.racesuit));
    iv.setItem(32, this.itemShop(p, 3, this.getSuitCost("suit4-3", 0), Material.DIAMOND_BOOTS, 1, (short)0, this.getSuitName(3, 2), columnType.RacingSuits, lg.racesuit));
    iv.setItem(33, this.itemShop(p, 4, this.getSuitCost("suit5-3", 0), Material.LEATHER_BOOTS, 1, (short)0, this.getSuitName(4, 2), columnType.RacingSuits, lg.racesuit));
    p.openInventory(iv);
    p.updateInventory();
    
}

HashMap<String,Integer> gsc = new HashMap();
public int getSuitCost(String str, int type){
    if(gsc.containsKey(str)){
        return gsc.get(str);
    }
      int irn = this.getConfig().getInt("TurboRacers.suits.price.L"+type);
      gsc.put(str, irn);
return irn;
}

public void openSkin(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.carskinselector);
    Material.getMaterial(1);
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+" &6"+this.getviewPoints(p), null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    int start = 9;
    for(int a : skinprice.keySet()){
                start++;
        if((start+1)%9==0){
            start+=2;
        }
    iv.setItem(start,this.itemShop(p,a, skinprice.get(a), Material.getMaterial(skinmat.get(a)), 1, skindura.get(a), skinname.get(a),columnType.KartSkin,lg.carskin));
    }
   
    p.openInventory(iv);
    p.updateInventory();
    
}

HashMap<Player,Integer> PCFrameTraccion = new HashMap();
HashMap<Player,Integer> PCFrameManejo = new HashMap();
HashMap<Player,Integer> PCFrameStartPosition = new HashMap();

HashMap<Player,Integer> PCEngineRecovery = new HashMap();
HashMap<Player,Integer> PCEngineTopSpeed = new HashMap();
HashMap<Player,Integer> PCEngineAceleracion = new HashMap();

HashMap<Player,Integer> PCTChargerBoosterSpeed = new HashMap();
HashMap<Player,Integer> PCTChargerStopQuicker = new HashMap();
HashMap<Player,Integer> PCTChargerDerrape = new HashMap();

HashMap<Player,Integer> PCType = new HashMap();

public void insertPCT(Player pe){
    if(!PCFrameTraccion.containsKey(pe)){
        PCFrameTraccion.put(pe, 0);
    }
    if(!PCFrameManejo.containsKey(pe)){
        PCFrameManejo.put(pe, 0);
    }
    if(!PCFrameStartPosition.containsKey(pe)){
        PCFrameStartPosition.put(pe, 0);
    }
    
    if(!PCEngineRecovery.containsKey(pe)){
        PCEngineRecovery.put(pe, 0);
    }
    if(!PCEngineTopSpeed.containsKey(pe)){
        PCEngineTopSpeed.put(pe, 0);
    }
    if(!PCEngineAceleracion.containsKey(pe)){
        PCEngineAceleracion.put(pe, 0);
    }
    
    if(!PCTChargerBoosterSpeed.containsKey(pe)){
        PCTChargerBoosterSpeed.put(pe, 0);
    }
    if(!PCTChargerStopQuicker.containsKey(pe)){
        PCTChargerStopQuicker.put(pe, 0);
    }
    if(!PCTChargerDerrape.containsKey(pe)){
        PCTChargerDerrape.put(pe, 0);
    }
    if(!PCType.containsKey(pe)){
        PCType.put(pe, 0);
    }
        
}

public ItemStack PCUpDown(int type,int exact, boolean Incrementard){
   if(type == 0){
       if(exact == 0){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseBoostRecover, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseBoostRecover, null);
           }
           }
       if(exact == 1){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseMaxVelocity, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseMaxVelocity, null);
           }
           }
       if(exact == 2){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseAcceleration, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseAcceleration, null);
           }
           }
   }
   if(type == 1){
       if(exact == 0){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseBoostVelocity, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseBoostVelocity, null);
           }
           }

       if(exact == 1){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseDrifting, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseDrifting, null);
           }
           }
       if(exact == 2){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseStopFast, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseStopFast, null);
           }
           }
   }
   
   if(type == 2){
       if(exact == 0){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseHandling, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseHandling, null);
           }
           }
       if(exact == 1){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseStartPosition, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.decreaseStartPosition, null);
           }
     }
       if(exact == 2){
           if(Incrementard){
return this.makeItem(Material.COOKED_BEEF, 1,(short)0, lg.increaseMoveBack, null);    
           }
           else{
return this.makeItem(Material.COOKED_CHICKEN, 1,(short)0, lg.increaseMoveBack, null);
           }
           }
   }
    return null;
}

public void nivelate(Inventory iv, int type,int line,int start, Player pe){

    int level = -1;

    
    if(type == 0){
     if(line == 0){
     level = this.PCEngineRecovery.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
     }
     else if(line == 1){

     level = this.PCEngineTopSpeed.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}

     
    }
     else if(line == 2){

     level = this.PCEngineAceleracion.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
    }
    }
    
    else     if(type == 1){
     if(line == 0){
     level = this.PCTChargerBoosterSpeed.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
     }
     else if(line == 1){

     level = this.PCTChargerDerrape.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}

     
    }
     else if(line == 2){

     level = this.PCTChargerStopQuicker.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
    }
    }
    
    
    else if(type == 2){
     if(line == 0){
     level = this.PCFrameManejo.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
     }
     else if(line == 1){

     level = this.PCFrameStartPosition.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}

     
    }
     else if(line == 2){

     level = this.PCFrameTraccion.get(pe);
     for(int a = start; a < start+5;a++){
short sh = -1;
if(level < 2){
    sh = 14;
}
else if(level <= 3){
    sh = 4;
}else{
    sh = 5;
}
         if(a >= start+level){
iv.setItem(a, this.makeItem(Material.STAINED_GLASS, 1, sh,lg.level+level, null));
         }else{
iv.setItem(a, this.makeItem(Material.STAINED_CLAY, 1, sh,lg.level+level, null));
     }}
    }
    }


}

public int getPartCost(Player pe,int levels, int multiplier){
    int total = 0;

    

        total+= (levels*levels)*multiplier;
    

return total;
}

public void updatePartCrafter(Player pe, Inventory iv) throws SQLException{

if(PCType.get(pe) == 0){
iv.setItem(49, this.makeItem(Material.STAINED_CLAY, 1, (short)14, lg.confirmpurchase, new String[]{
 lg.price+this.getPartCost(pe, this.PCEngineRecovery.get(pe)+this.PCEngineTopSpeed.get(pe)+this.PCEngineAceleracion.get(pe), this.enginecost)
}));
       this.nivelate(iv, 0, 0, 20, pe);
       this.nivelate(iv, 0, 1, 20+9, pe);
       this.nivelate(iv, 0, 2, 20+18, pe);
      // engine
iv.setItem(3, this.makeItem(Material.COOKED_BEEF, 1, (short)0, "&a"+lg.chassis, new String[]{lg.optimizechassis}));
iv.setItem(5, this.makeItem(Material.COOKED_CHICKEN, 1, (short)0, "&a"+lg.turbo, new String[]{lg.optimizeturbo}));
iv.setItem(4, this.PCToLevel(pe, 0, this.PCEngineRecovery.get(pe) +this.PCEngineTopSpeed.get(pe)+ this.PCEngineAceleracion.get(pe)));
iv.setItem(19, this.PCUpDown(0, 0, true));
iv.setItem(25, this.PCUpDown(0, 0, false));

iv.setItem(28, this.PCUpDown(0, 1, true)); 
iv.setItem(34, this.PCUpDown(0, 1, false));

iv.setItem(37, this.PCUpDown(0, 2, true)); 
iv.setItem(43, this.PCUpDown(0, 2, false));
   }
      if(PCType.get(pe) == 1){
iv.setItem(49, this.makeItem(Material.STAINED_CLAY, 1, (short)14, lg.confirmpurchase, new String[]{
 lg.price+this.getPartCost(pe, this.PCTChargerBoosterSpeed.get(pe)+this.PCTChargerDerrape.get(pe)+this.PCTChargerStopQuicker.get(pe), this.turbocost)
}));
       this.nivelate(iv, 1, 0, 20, pe);
       this.nivelate(iv, 1, 1, 20+9, pe);
       this.nivelate(iv, 1, 2, 20+18, pe);
          // turbocharger
iv.setItem(3, this.makeItem(Material.COOKED_BEEF, 1, (short)0, "&a"+lg.engine, new String[]{lg.optimizeengine}));
iv.setItem(5, this.makeItem(Material.COOKED_CHICKEN, 1, (short)0, "&a"+lg.chassis, new String[]{lg.optimizechassis}));
iv.setItem(4, this.PCToLevel(pe, 1,this.PCTChargerBoosterSpeed.get(pe)+this.PCTChargerDerrape.get(pe)+this.PCTChargerStopQuicker.get(pe)));

iv.setItem(19, this.PCUpDown(1, 0, true));
iv.setItem(25, this.PCUpDown(1, 0, false));

iv.setItem(28, this.PCUpDown(1, 1, true)); 
iv.setItem(34, this.PCUpDown(1, 1, false));

iv.setItem(37, this.PCUpDown(1, 2, true)); 
iv.setItem(43, this.PCUpDown(1, 2, false));
      }
         if(PCType.get(pe) == 2){
iv.setItem(49, this.makeItem(Material.STAINED_CLAY, 1, (short)14, lg.confirmpurchase, new String[]{
 "&7Precio: &6"+this.getPartCost(pe, this.PCFrameManejo.get(pe)+this.PCFrameStartPosition.get(pe)+this.PCFrameTraccion.get(pe), this.framecost)
}));
       this.nivelate(iv, 2, 0, 20, pe);
       this.nivelate(iv, 2, 1, 20+9, pe);
       this.nivelate(iv, 2, 2, 20+18, pe);
             // frame
iv.setItem(3, this.makeItem(Material.COOKED_BEEF, 1, (short)0, "&a"+lg.turbo, new String[]{lg.optimizeturbo}));
iv.setItem(5, this.makeItem(Material.COOKED_CHICKEN, 1, (short)0, "&a"+lg.engine, new String[]{lg.optimizeengine}));
iv.setItem(4, this.PCToLevel(pe, 2,this.PCFrameManejo.get(pe)+this.PCFrameStartPosition.get(pe)+this.PCFrameTraccion.get(pe)));

iv.setItem(19, this.PCUpDown(2, 0, true));
iv.setItem(25, this.PCUpDown(2, 0, false));

iv.setItem(28, this.PCUpDown(2, 1, true)); 
iv.setItem(34, this.PCUpDown(2, 1, false));

iv.setItem(37, this.PCUpDown(2, 2, true)); 
iv.setItem(43, this.PCUpDown(2, 2, false));
         }

    

}

public String PCNameLevel(int level){
    if(level == 0){
        return lg.level0;
    }
    else if(level == 1){
        return lg.level1;
    }
    else if(level == 2){
        return lg.level2;
    }
    else if(level == 3){
        return lg.level3;
    }
    else if(level == 4){
        return lg.level4;
    }else if(level == 5){
        return lg.level5;
    }
    else if(level == 6){
        return lg.level6;
    }
    else if(level == 7){
        return lg.level7;
    }
    else if(level == 8){
        return lg.level8;
    }
    else  if(level == 9){
        return lg.level9;
    }
    else if(level == 10){
        return lg.level10;
    }else if(level == 11){
        return lg.level11;
    }else if(level == 12){
        return lg.level12;
    }else if(level == 13) {
        return lg.level13;
    }else if(level == 14){
        return lg.level14;
    }else if(level == 15){
        return lg.level15;
    }
       
           return "";
}


public ItemStack PCToLevel(Player pe, int type, int level){
    ItemStack it = null;
    int total = 0;
    if(type == 0){
   //engine
  total+=level;
if(total == 0){
it = this.makeItem(Material.DIAMOND_HOE, 1, (short)0,this.PCNameLevel(total)+lg.engine, null);
}else if(total < 6){
it = this.makeItem(Material.WOOD_HOE, 1, (short)0,this.PCNameLevel(total)+lg.engine, null);
}else if(total < 11){
it = this.makeItem(Material.WOOD_PICKAXE, 1, (short)0,this.PCNameLevel(total)+lg.engine, null);
}else{
it = this.makeItem(Material.WOOD_SPADE, 1, (short)0,this.PCNameLevel(total)+lg.engine, null);    
}
    }
    else if(type == 1){
   // turbocharger
  total+=level;
if(total == 0){
it = this.makeItem(Material.DIAMOND_HOE, 1, (short)0,this.PCNameLevel(total)+lg.turbo, null);
}else if(total < 6){
it = this.makeItem(Material.DIAMOND_PICKAXE, 1, (short)0,this.PCNameLevel(total)+lg.turbo, null);
}else if(total < 11){
it = this.makeItem(Material.DIAMOND_SPADE, 1, (short)0,this.PCNameLevel(total)+lg.turbo, null);
}else{
it = this.makeItem(Material.GOLD_AXE, 1, (short)0,this.PCNameLevel(total)+lg.turbo, null);    
}
    }
    else if(type == 2){
  // frame
  total+=level;
if(total == 0){
it = this.makeItem(Material.DIAMOND_HOE, 1, (short)0,this.PCNameLevel(total)+lg.chassis, null);
}else if(total < 6){
it = this.makeItem(Material.GOLD_HOE, 1, (short)0,this.PCNameLevel(total)+lg.chassis, null);
}else if(total < 11){
it = this.makeItem(Material.GOLD_PICKAXE, 1, (short)0,this.PCNameLevel(total)+lg.chassis, null);
}else{
it = this.makeItem(Material.WOOD_AXE, 1, (short)0,this.PCNameLevel(total)+lg.chassis, null);    
}
    }
    
    return it;
    
}

HashMap<String,String> kengine = new HashMap();
HashMap<String,String> kturbocharger = new HashMap();
HashMap<String,String> kframe = new HashMap();


public void openKartCustomizer(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null,54,lg.carcustomizer);
    
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, lg.coins+this.getviewPoints(p), null);

    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    
    
    String engines = this.getValue(p, columnType.Engine).split("-")[0];
    String turbos = this.getValue(p, columnType.Turbocharger).split("-")[0];
    String frames = this.getValue(p, columnType.Frame).split("-")[0];
    
    List<String> dengines = new ArrayList();
    List<String> dturbos = new ArrayList();
    List<String> dframes = new ArrayList();
    
    if(engines.contains(";")){
    for(String str : engines.split(";")){
        dengines.add(str);
    }
    }else{
        dengines.add(engines);
    }
    
    if(turbos.contains(";")){
    for(String str : turbos.split(";")){
        dturbos.add(str);
    }
    }else{
        dturbos.add(turbos);
    }
    
    if(frames.contains(";")){
    for(String str : frames.split(";")){
        dframes.add(str);
    }
    }else{
        dframes.add(frames);
    }
    
    int added = 9;
    for(String str : dengines){
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]);
        ItemStack tomenu = this.PCToLevel(p, 0, level);
        kengine.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    
    for(String str : dturbos){
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]); 
        ItemStack tomenu = this.PCToLevel(p, 1, level);
        kturbocharger.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    
    for(String str : dframes){
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]); 
        ItemStack tomenu = this.PCToLevel(p, 2, level);
        kframe.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    

    
    p.openInventory(iv);
    p.updateInventory();

    
}

public void openPartCrafter(Player p) throws SQLException{

    Inventory iv = Bukkit.createInventory(null, 54,lg.partcrafterselector);
    this.insertPCT(p);

    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+" &6"+this.getviewPoints(p), null);
  
    ItemStack change1 = this.makeItem(Material.COOKED_BEEF, 1, (short)0, "Back", null);
    ItemStack change2 = this.makeItem(Material.COOKED_CHICKEN, 1, (short)0, "Next", null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,change1,null,change2,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,change1,null,null,null,null,null,change2,null,
        null,change1,null,null,null,null,null,change2,null,
        null,change1,null,null,null,null,null,change2,null,
        null,null,null,back,null,total,null,null,null
    });
    this.updatePartCrafter(p, iv);
   
    
    p.openInventory(iv);
    p.updateInventory();
    
}

public void openHelmet(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.helmetselector);
    Material.getMaterial(1);
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.helmetselector,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+" &6"+this.getviewPoints(p), null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    int start = 9;
    for(int a : helmprice.keySet()){
                start++;
        if((start+1)%9==0){
            start+=2;
        }
    iv.setItem(start,this.itemShop(p,a, helmprice.get(a), Material.getMaterial(helmmat.get(a)), 1, helmdura.get(a), helmname.get(a),columnType.Helmets,lg.helmet));
    }
   
    p.openInventory(iv);
    p.updateInventory();
    
}

public void openHorn(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.hornselector);
    Material.getMaterial(1);
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+" &6"+this.getviewPoints(p), null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    
    int start = 9;
    for(int a : hornprice.keySet()){
        start++;
        if((start+1)%9==0){
            start+=2;
        }
    iv.setItem(start,this.itemShop(p,a, hornprice.get(a), Material.getMaterial(hornmat.get(a)), 1, horndura.get(a), hornname.get(a),columnType.Horns,lg.horn));
    }
   
    p.openInventory(iv);
    p.updateInventory();
    
}

public void openTrailSelector(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.particletrailselector);
    Material.getMaterial(1);
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.goback,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+"&6"+this.getviewPoints(p), null);
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    
    int start = 9;
    for(int a : trailprice.keySet()){
        start++;
        if((start+1)%9==0){
            start+=2;
        }
    iv.setItem(start,this.itemShop(p,a, trailprice.get(a), Material.getMaterial(trailmat.get(a)), 1, traildura.get(a), trailname.get(a),columnType.ParticleTrail,lg.particletrail));
    }
   
    p.openInventory(iv);
    p.updateInventory();
    
}

public HashMap<Integer,Integer> helmprice = new HashMap();
public HashMap<Integer,Integer> helmmat = new HashMap();
public HashMap<Integer,Short> helmdura = new HashMap();
public HashMap<Integer,String> helmname = new HashMap();

public HashMap<Integer,Integer> trailprice = new HashMap();
public HashMap<Integer,Integer> trailmat = new HashMap();
public HashMap<Integer,Short> traildura = new HashMap();
public HashMap<Integer,String> trailname = new HashMap();

public HashMap<Integer,Integer> skinprice = new HashMap();
public HashMap<Integer,Integer> skinmat = new HashMap();
public HashMap<Integer,Short> skindura = new HashMap();
public HashMap<Integer,String> skinname = new HashMap();

public HashMap<Integer,Integer> hornprice = new HashMap();
public HashMap<Integer,Integer> hornmat = new HashMap();
public HashMap<Integer,Short> horndura = new HashMap();
public HashMap<Integer,String> hornname = new HashMap();
public HashMap<Integer,String> hornsound = new HashMap();

public boolean ownsItem(Player pe, int type, columnType column) throws SQLException{

    String[] items = this.getValue(pe, column).split("-");
    
    if(items[0].contains(",")){
        items = items[0].split(",");
    }else{
        items[1] = "";
    }
    
    for(String str : items){

        if(str.equalsIgnoreCase(""+type)){
            return true;
        }
    }
    return false;
}



public ItemStack itemShop(Player pe,int type,int cost, Material mat,int quantity, short dura,String name,columnType column, String display) throws SQLException{
    ItemStack it = new ItemStack(mat,quantity,dura);
    
    ItemMeta im = it.getItemMeta();
    boolean owns = this.ownsItem(pe,type,column);
    List<String> lore = new ArrayList();
    
    lore.add(Colorizer.Color("&7Use &f"+name));
    lore.add(Colorizer.Color("&7as your: "+display+"."));
    
    if(owns){
    im.setDisplayName(Colorizer.Color("&a"+name));

    lore.add("     ");
    lore.add(Colorizer.Color("&aClick to equip."));
    }else{
    im.setDisplayName(Colorizer.Color("&c"+name));
    lore.add("     ");
    lore.add(Colorizer.Color(lg.price+cost));
    lore.add(Colorizer.Color("&eClick to Purchase!"));
    }
    im.setLore(lore);
    
    it.setItemMeta(im);
    
    return it;
}


public void loadHorns(){
    if(!hornprice.isEmpty()){
        hornprice.clear();
    }
   
    if(!hornmat.isEmpty()){
        hornmat.clear();
    }
    
    if(!horndura.isEmpty()){
        horndura.clear();
    }
    
    if(!hornname.isEmpty()){
        hornname.clear();
    }
    if(!hornsound.isEmpty()){
        hornsound.clear();
    }

     for(String str : this.getConfig().getStringList("TurboRacers.horns")){

      String[] div = str.split(",");
      int type = Integer.parseInt(div[0]);
      int price = Integer.parseInt(div[1]);
      int mat = Integer.parseInt(div[2]);
      int dura = Integer.parseInt(div[3]);
      String name = div[4];
      String sound = div[5];
      
      hornprice.put(type, price);
      hornmat.put(type, mat);
      horndura.put(type, (short)dura);
      hornname.put(type, name);
      hornsound.put(type, sound);
             
     }
}


public void loadSkins(){
    if(!skinprice.isEmpty()){
        skinprice.clear();
    }
    
    if(!skinmat.isEmpty()){
        skinmat.clear();
    }
    
    if(!skindura.isEmpty()){
        skindura.clear();
    }
    
    if(!skinname.isEmpty()){
        skinname.clear();
    }

     for(String str : this.getConfig().getStringList("TurboRacers.skins")){

      String[] div = str.split(",");
      int type = Integer.parseInt(div[0]);
      int price = Integer.parseInt(div[1]);
      int mat = Integer.parseInt(div[2]);
      int dura = Integer.parseInt(div[3]);
      String name = div[4];
      
      skinprice.put(type, price);
      skinmat.put(type, mat);
      skindura.put(type, (short)dura);
      skinname.put(type, name);
             
     }
}

public void loadTrails(){
    if(!trailprice.isEmpty()){
        trailprice.clear();
    }
    
    if(!trailmat.isEmpty()){
        trailmat.clear();
    }
    
    if(!traildura.isEmpty()){
        traildura.clear();
    }
    
    if(!trailname.isEmpty()){
        trailname.clear();
    }

     for(String str : this.getConfig().getStringList("TurboRacers.particletrail")){

      String[] div = str.split(",");
      int type = Integer.parseInt(div[0]);
      int price = Integer.parseInt(div[1]);
      int mat = Integer.parseInt(div[2]);
      int dura = Integer.parseInt(div[3]);
      String name = div[4];
      
      trailprice.put(type, price);
      trailmat.put(type, mat);
      traildura.put(type, (short)dura);
      trailname.put(type, name);
             
     }
}

public void loadHelmets(){
    if(!helmprice.isEmpty()){
        helmprice.clear();
    }
    
    if(!helmmat.isEmpty()){
        helmmat.clear();
    }
    
    if(!helmdura.isEmpty()){
        helmdura.clear();
    }
    
    if(!helmname.isEmpty()){
        helmname.clear();
    }

     for(String str : this.getConfig().getStringList("TurboRacers.helmets")){

      String[] div = str.split(",");
      int type = Integer.parseInt(div[0]);
      int price = Integer.parseInt(div[1]);
      int mat = Integer.parseInt(div[2]);
      int dura = Integer.parseInt(div[3]);
      String name = div[4];
      
      helmprice.put(type, price);
      helmmat.put(type, mat);
      helmdura.put(type, (short)dura);
      helmname.put(type, name);
             
     }
}


ItemStack shopitem = this.makeItem(Material.EMERALD, 1, (short)0, "&aShop &7(Right-Click)",null);
int sslot = 0;

@EventHandler
public void updatingShop(PlayerJoinEvent event){
    
Player p = event.getPlayer();
        p.getInventory().setItem(sslot, shopitem);
    
}
  
    public ItemStack makeItem(Material mat, int quantity, short sh, String name, String[] lore){
    ItemStack it = new ItemStack(mat,quantity,sh);
    ItemMeta imt = it.getItemMeta();
    
    if(name != null){
    imt.setDisplayName(Colorizer.Color(name));
    }
    
    if(lore != null){
    List<String> newlore = new ArrayList();
    for(String str : lore){
     newlore.add(Colorizer.Color(str));
    }
    imt.setLore(newlore);
    }
    
    it.setItemMeta(imt);
    
    return it;
        
    }
    
    
    
    
    
    
public void openJunkyard(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null,54,lg.partsseller);
    
    ItemStack back = this.makeItem(Material.COOKED_BEEF, 1,(short) 0, "&a"+lg.partsseller,null);
    
    ItemStack total = this.makeItem(Material.EMERALD, 1, (short)0, "&7"+lg.coins+"&6"+this.getviewPoints(p), null);

    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    
    
    String engines = this.getValue(p, columnType.Engine).split("-")[0];
    String turbos = this.getValue(p, columnType.Turbocharger).split("-")[0];
    String frames = this.getValue(p, columnType.Frame).split("-")[0];
    
    List<String> dengines = new ArrayList();
    List<String> dturbos = new ArrayList();
    List<String> dframes = new ArrayList();
    
    if(engines.contains(";")){
    for(String str : engines.split(";")){
        dengines.add(str);
    }
    }else{
        dengines.add(engines);
    }
    
    if(turbos.contains(";")){
    for(String str : turbos.split(";")){
        dturbos.add(str);
    }
    }else{
        dturbos.add(turbos);
    }
    
    if(frames.contains(";")){
    for(String str : frames.split(";")){
        dframes.add(str);
    }
    }else{
        dframes.add(frames);
    }
    
    int added = 9;
    for(String str : dengines){
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]);
        if(level == 0){
            continue;
        }
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        

        ItemStack tomenu = this.PCToLevel(p, 0, level);
        ItemMeta im = tomenu.getItemMeta();
        List<String> lore = new ArrayList();
        int cost = this.getPartCost(p, level, enginecost);
        cost = (cost * sellpercent) /100;
        lore.add(Colorizer.Color(lg.yousell+lg.sellfor+cost));
        im.setLore(lore);
        tomenu.setItemMeta(im);
        
        kengine.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    
    for(String str : dturbos){
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]); 
        if(level == 0){
            continue;
        }
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        

        ItemStack tomenu = this.PCToLevel(p, 1, level);
        ItemMeta im = tomenu.getItemMeta();
        List<String> lore = new ArrayList();
        int cost = this.getPartCost(p, level, turbocost);
        cost = (cost * sellpercent) /100;
        lore.add(Colorizer.Color(lg.yousell+lg.sellfor+cost));
        im.setLore(lore);
        tomenu.setItemMeta(im);
        kturbocharger.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    
    for(String str : dframes){
        String[] strplit = str.split(",");
        int level = Integer.parseInt(strplit[0])+Integer.parseInt(strplit[1])+Integer.parseInt(strplit[2]); 
        if(level == 0){
            continue;
        }
        added++;
        if(added == 17 || added == 17+9 || added == 17+18){
           added+=2;
        }
        if(added > 44){
            continue;
        }
        

        ItemStack tomenu = this.PCToLevel(p, 2, level);
        ItemMeta im = tomenu.getItemMeta();
        List<String> lore = new ArrayList();
        int cost = this.getPartCost(p, level, framecost);
        cost = (cost * sellpercent) /100;
        lore.add(Colorizer.Color(lg.yousell+lg.sellfor+cost));
        im.setLore(lore);
        tomenu.setItemMeta(im);
        kframe.put(tomenu.getItemMeta().getDisplayName(), str);
    iv.setItem(added, tomenu);
    }
    

    
    p.openInventory(iv);
    p.updateInventory();

 
}
    
    
    
@EventHandler
public void onShop(InventoryClickEvent event) throws SQLException{
    if(event.getCurrentItem() == null){
        return;
    }
    if(!event.getCurrentItem().hasItemMeta()){
        return;
    }
    if(!event.getInventory().getName().equalsIgnoreCase(shopname)){
        return;
    }
    event.setCancelled(true);
    Player pe = (Player)event.getWhoClicked();
    String itemname = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
    if(itemname.equalsIgnoreCase(lg.particlequalityselector)){
        this.openParticleQuality(pe);
    }else if(itemname.equalsIgnoreCase(lg.helmetselector)){
        this.openHelmet(pe);
    }else if(itemname.equalsIgnoreCase(lg.hornselector)){
        this.openHorn(pe);
    }
    else if(itemname.equalsIgnoreCase(lg.carskinselector)){
        this.openSkin(pe);
    }else if(itemname.equalsIgnoreCase(lg.racesuitselector)){
        this.openSuits(pe);
    }else if(itemname.equalsIgnoreCase(lg.partcrafterselector)){
        this.openPartCrafter(pe);
    }else if(itemname.equalsIgnoreCase(lg.carcustomizer)){
        this.openKartCustomizer(pe);
    }else if(itemname.equalsIgnoreCase(lg.partsseller)){
        this.openJunkyard(pe);
    }else if(itemname.equalsIgnoreCase(lg.partroller)){
        this.usePartRoller(pe);
    }else if(itemname.equalsIgnoreCase(lg.particletrailselector)){
        this.openTrailSelector(pe);
    }
    
        
}


Random r = new Random();
HashMap<Integer,Integer> tasks = new HashMap();

public void usePartRoller(final Player pe){
    pe.closeInventory();

if(this.getPointsAPI(pe).look(pe.getUniqueId()) >= this.epicpartcost){
    this.getPointsAPI(pe).take(pe.getUniqueId(), epicpartcost);

final int ren = r.nextInt(999999);

int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
   Long timer = System.currentTimeMillis()+3000L+r.nextInt(5000);
   int type = 1;

   int btype = 0;
    @Override
    public void run(){
      
if(timer < System.currentTimeMillis()){
try{
int val1 = 0;
int val2 = 0;
int val3 = 0;
columnType ctype = null;

if(btype == 0){
    ctype = columnType.Engine;
}
else if(btype == 1){
    ctype = columnType.Turbocharger;
}
else if(btype == 2){
    ctype = columnType.Frame;
}

    for(int a = 0; a < type;a++){
       int nx = r.nextInt(2);
       if(nx == 0){
           val1+=1;
       }
       else if(nx == 1){
           val2+=1;
       }
       else if(nx == 3){
           val3+=1;
       }
    }
Bukkit.broadcastMessage(Colorizer.Color(pe.getDisplayName()+lg.obtained+TurboLobby.this.PCNameLevel((int)type)+pcTypeName(btype)));
addValue(pe, ctype, ";"+val1+","+val2+","+val3, false);
 
}catch(Exception e){
    
}
    try {
    
        Bukkit.getServer().getScheduler().cancelTask(tasks.get(ren));

    } catch (Throwable ex) {
        Logger.getLogger(TurboLobby.class.getName()).log(Level.SEVERE, null, ex);
    }
    return;
}
       type+=r.nextInt(20)+1;
       btype++;
       if(btype >= 3){
           btype = 0;
       }
       if(type >= 16 || type <= 0){
           type = r.nextInt(2)+1;
       }
       if(type > 4){
           type = r.nextInt(type)+1;
       }
       if(type > 8){
           type = r.nextInt(type)+1;
       }
       if(type > 12){
           type = r.nextInt(type)+1;
       }
       
       sendTitle(pe, "", Colorizer.Color(PCNameLevel(type)+pcTypeName(btype)));
   
    }
    }, 2L,2L);
tasks.put(ren, task);

       

   
}else{
    pe.sendMessage(Colorizer.Color(lg.insufficientmoney+" &cCost: "+epicpartcost));
    pe.closeInventory();
    
}

}
HashMap<String,Integer> points = new HashMap();
public int getviewPoints(Player pe){
    if(!points.containsKey(pe.getName())){
        points.put(pe.getName(), (int)this.getPointsAPI(pe).look(pe.getUniqueId()));
    }
    return points.get(pe.getName());
}


public String pcTypeName(int type){
    if(type == 0){
        return lg.engine;
    }
    else if(type  == 1){
        return lg.turbo;
    }
    else if(type == 2){
        return lg.chassis;
    }
    return "";
}
       

public void sendTitle(Player player, String text, String sub){

TitleManager.sendTimings(player, 0, 50, 0);
TitleManager.sendTitle(player, "{\"text\":\"\",\"extra\":[{\"text\":\""+ text +"\",\"color\":\"yellow\"}]}");


if(sub != null){
TitleManager.sendSubTitle(player, "{\"text\":\"\",\"extra\":[{\"text\":\""+ sub +"\",\"color\":\"gold\"}]}");
}
  
  }
@EventHandler
public void onPreShop(PlayerInteractEvent event){
    if(event.getPlayer().getItemInHand() == null){
        return;
    }
    if(event.getPlayer().getItemInHand().getType() != Material.EMERALD){
        return;
    }
    if(!event.getPlayer().getItemInHand().hasItemMeta()){
        return;
    }
    
    this.openShop(event.getPlayer());
}

    String shopname = "Shop";
    
public void openShop(Player pe){
    Inventory iv = Bukkit.createInventory(null, 45, shopname);
    
   ItemStack prqu = this.makeItem(Material.NETHER_STAR, 1, (short)0, "&a"+lg.particlequalityselector, new String[]{
        "&7Enables you to increase"
        ,"&7or decrease the amount"
        ,"&7of particles in the game!"
   });
   
  
   
   ItemStack helm = this.makeItem(Material.DIAMOND_HELMET, 1, (short)0, "&a"+lg.helmetselector, new String[]{
      "&7Select from a Variety of Race Helmets!" 
   });
   
   ItemStack raci = this.makeItem(Material.GOLD_CHESTPLATE, 1, (short)0, "&a"+lg.racesuitselector, new String[]{
      "&7Select from a variety of clothing!" 
   });
   
   ItemStack kart = this.makeItem(Material.DISPENSER, 1, (short)0, "&a"+lg.carcustomizer, new String[]{
      "&7Customize your car!" 
   });
   
   ItemStack junk = this.makeItem(Material.DIAMOND_HOE, 1, (short)0, "&a"+lg.partsseller, new String[]{
      "&7Sell old parts for coins!",
      "&cGo race to obtain more parts!"
   });
   
   ItemStack roll = this.makeItem(Material.GOLD_INGOT, 1, (short)0, "&c"+lg.partroller, new String[]{
      "&7Use coins for a change to get"
      ,"&7an Epic Part!"
      ,"     "
      ,"&7Price: &6"+epicpartcost
   });
      
   ItemStack part = this.makeItem(Material.RAW_FISH, 1, (short)1, "&c"+lg.partcrafterselector, new String[]{
      "&7Craft parts with coins!"
   });
   
   ItemStack horn = this.makeItem(Material.APPLE, 1, (short)0, "&a"+lg.hornselector, new String[]{
      "&7Change the sound of the Horn"
   });
   
   ItemStack skin = this.makeItem(Material.ANVIL, 1, (short)2, "&a"+lg.carskinselector, new String[]{
      "&7Change the skin of your Car"
   });
   
   ItemStack trai = this.makeItem(Material.BAKED_POTATO, 1, (short)0, "&a"+lg.particletrailselector, new String[]{
      "&7Left your competitors in smoke... or lava!"
   });
   
   ItemStack musi = this.makeItem(Material.IRON_HOE, 1, (short)0, "&a"+lg.musicselector, new String[]{
      "&7Turn different sounds ON or OFF!"
   });
     
   
    iv.setContents(new ItemStack[]{
        null,null,helm,null,raci,null,kart,null,null,
        null,null,null,null,null,null,null,null,null,
        prqu,null,junk,null,roll,null,part,null,musi,
        null,null,null,null,null,null,null,null,null,
        null,null,horn,null,skin,null,trai,null,null
    });
    
    pe.openInventory(iv);
    pe.updateInventory();
}




HashMap<String,String> suitname = new HashMap();

public String getSuitName(int id, int line){
  if(suitname.containsKey(""+id+"-"+line)){
      return suitname.get(""+id+"-"+line);
  }
    String ret = Colorizer.Color(this.getConfig().getStringList("TurboRacers.suits.L"+id).get(line));
suitname.put(""+id+"-"+line, ret);
return ret;
}


@EventHandler
public void onParticleQuality(InventoryClickEvent event){
    if(event.getCurrentItem() == null){
        return;
    }
    if(!event.getInventory().getName().equalsIgnoreCase(lg.particlequalityselector)){
        return;
    }
    
    if(!event.getCurrentItem().hasItemMeta()){
        return;
    }
    event.setCancelled(true);
    
    Player pe = (Player)event.getWhoClicked();
    String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
    short dura = event.getCurrentItem().getDurability();
    
    if(name.contains(lg.goback)){
        pe.closeInventory();
        this.openShop(pe);
    }
    
    if(dura == 1){
        this.setValueInteger(pe, columnType.ParticleQuality, 0);
        pe.sendMessage(Colorizer.Color("&6Low Quality &aSELECTED"));
        pe.closeInventory();
    }
    else if(dura == 2){
        this.setValueInteger(pe, columnType.ParticleQuality, 1);
        pe.sendMessage(Colorizer.Color("&eMedium Quality &aSELECTED"));
        pe.closeInventory();
    }
    else if(dura == 3){
        this.setValueInteger(pe, columnType.ParticleQuality, 2);
        pe.sendMessage(Colorizer.Color("&aHigh Quality &aSELECTED"));
        pe.closeInventory();
    }
    
}

public void openParticleQuality(Player p) throws SQLException{
    Inventory iv = Bukkit.createInventory(null, 54, lg.particlequalityselector);
    
    
    ItemStack back = new ItemStack(Material.COOKED_BEEF,1);
    ItemMeta ime = back.getItemMeta();
    ime.setDisplayName(Colorizer.Color("&a"+lg.goback));
    back.setItemMeta(ime);
    
    
    ItemStack total = new ItemStack(Material.EMERALD);
    ItemMeta tm = total.getItemMeta();
    tm.setDisplayName(Colorizer.Color("&7"+lg.coins+"&6"+this.getviewPoints(p)));
    total.setItemMeta(tm);
    int particle = Integer.parseInt(this.getValue(p, columnType.ParticleQuality));
    
    ItemStack low = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)1);
    {
    ItemMeta im = low.getItemMeta();
    im.setDisplayName(Colorizer.Color("&6Low Quality"));
    List<String> lore = new ArrayList();
    lore.add(Colorizer.Color("&7Decrease the amount of"));
    lore.add(Colorizer.Color("&7particles that you can see"));
    lore.add("        ");
    if(particle == 0){
    lore.add(Colorizer.Color("&aSELECTED"));
    }else{
    lore.add(Colorizer.Color("&7Click to Select"));
    }
    im.setLore(lore);
    low.setItemMeta(im);
    }
    
    ItemStack medium = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)2);
    {
    ItemMeta im = medium.getItemMeta();
    im.setDisplayName(Colorizer.Color("&eMedium Quality"));
    List<String> lore = new ArrayList();
    lore.add(Colorizer.Color("&7Decrease the amount of"));
    lore.add(Colorizer.Color("&7particles that you can see."));
    lore.add("        ");
    if(particle == 1){
    lore.add(Colorizer.Color("&aSELECTED"));
    }else{
    lore.add(Colorizer.Color("&7Click to Select"));
    }
    im.setLore(lore);
    medium.setItemMeta(im);
    }
    
    ItemStack high = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)3);
    {
    ItemMeta im = high.getItemMeta();
    im.setDisplayName(Colorizer.Color("&aMax Quality"));
    List<String> lore = new ArrayList();
    lore.add(Colorizer.Color("&7Show all the particles"));
    lore.add(Colorizer.Color("&7for better gaming"));
    lore.add("        ");
    if(particle == 2){
    lore.add(Colorizer.Color("&aSELECTED"));
    }else{
    lore.add(Colorizer.Color("&7Click to Select"));
    }
    im.setLore(lore);
    high.setItemMeta(im);
    }
    
    
    iv.setContents(new ItemStack[]{
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,low,medium,high,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,
        null,null,null,back,total,null,null,null,null
    });
    p.openInventory(iv);
    p.updateInventory();
    
}

    

    
private void createTable() throws SQLException {
            if(enabled == false){
            return;
        }
          
    String sqlCreate =
"CREATE TABLE IF NOT EXISTS `"+table+"` ("
            + "`ID` int(11) NOT NULL auto_increment"
            +",`Name` varchar(255) NOT NULL"
            +",`UUID` varchar(255) NOT NULL"
            +",`"+columnType.Engine.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.FirstPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Frame.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Helmets.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Horns.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.KartSkin.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.ParticleQuality.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.ParticleTrail.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.RacingSuits.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.SecondPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.ThirdPlace.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Played.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Laps.getName()+"` text(4999) NOT NULL"
            +",`"+columnType.Turbocharger.getName()+"` text(4999) NOT NULL"
            +",PRIMARY KEY  (`ID`)"
            +
            ")";


    this.getStatement().execute(sqlCreate);
    
}

public String getValue(Player p, columnType type) throws SQLException{
            if(enabled == false){return null;}
        this.checkData();
        if(p == null){return null;}
        if(p.getName() == null){return null;}
ResultSet res = null;
res = getStatement().executeQuery("SELECT * FROM "+table+" WHERE UUID = '"+p.getUniqueId().toString()+"'");
String value = null;

try{res.next(); 
value = res.getString(type.getName());
res.close();} catch(Exception e){
  e.printStackTrace();}

   return value;
    }

    public void addValue(Player p, columnType type, String value, boolean isInteger){
        if(enabled == false){
            return;
        }
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{
    String val = this.getValue(p, type);

    if(isInteger){
        
    int cint = Integer.parseInt(val);
    cint+= Integer.parseInt(value);
    val = ""+cint;
    }else{
        String[] vals = val.split("-");
        for(String str : vals){

        }
        val = vals[0]+value+"-"+vals[1];
    }
if(val.contains(";;")){
    val = val.replace(";;", ";");
}
    
String pay = "UPDATE "+table+" SET "+type.getName()+" ='"+val+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);  
}catch(Exception e){
    
}}
    
@EventHandler
public void onDrop(PlayerDropItemEvent event){
    if(event.getItemDrop().getItemStack().getType().equals(Material.EMERALD)){
        event.setCancelled(true);
    }
}
    
    public void removeValue(Player p, columnType type, String value,boolean fix){
        if(enabled == false){
            return;
        }
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{
        String val = this.getValue(p, type);

        String[] vals = val.split("-");
        
        val = vals[0].replaceFirst(value, "")+"-"+vals[1];
    if(fix){
   if(val.contains(";;")){
       val = val.replace(";;", ";");
   }
}

String rem = "UPDATE "+table+" SET "+type.getName()+" ='"+val+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(rem);  
}catch(Exception e){
    
}}
    
    public void setValueInteger(Player p, columnType type, int value){
        if(enabled == false){
            return;
        }
        
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{
             String pay = "UPDATE "+table+" SET "+type.getName()+" ='"+value+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);  
}catch(Exception e){
    
}}
    
    public void setValueSelected(Player p, columnType type, String value){
        if(enabled == false){
            return;
        }
        /*
        Example:
        0,0,1;0,0,2-0,0,1
        after:
        0,0,1;0,0,2-0,0,2*/
        
        if(p == null){
            System.out.println("Error trying to record player kill, player is null");
            return;
        }
        if(p.getName() == null){
            System.out.println("Error trying to record player kill, name is null");
        return;
        }
try{
    String val = this.getValue(p, type);

        String[] vals = val.split("-");
        val = vals[0]+"-"+value;
    
             String pay = "UPDATE "+table+" SET "+type.getName()+" ='"+val+"' WHERE UUID = '"+p.getUniqueId()+"'";

     getStatement().execute(pay);  
}catch(Exception e){
    
}}
    
    
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
         }
        return Statement;
}

public final void openData(){
            if(enabled == false){
            return;
        }
    try {
    MySQL MySQL = new MySQL(this, host, port, db, user, pwd);
    
        data = MySQL.openConnection();
    } catch (SQLException | ClassNotFoundException ex) {
  ex.printStackTrace();
    }

    
}

     public void init(){
        System.out.println("Starting Turbo...");

        host = plugin.getConfig().getString("TurboRacers.mysql.host");
        port = plugin.getConfig().getString("TurboRacers.mysql.port");
        db = plugin.getConfig().getString("TurboRacers.mysql.db");
        user = plugin.getConfig().getString("TurboRacers.mysql.user");
        pwd = plugin.getConfig().getString("TurboRacers.mysql.pwd");
        table = plugin.getConfig().getString("TurboRacers.mysql.table");
this.loadHelmets();
this.loadHorns();
this.loadSkins();
this.loadTrails();
        try{
        this.openData();    
        }catch(Exception e){
            System.out.println("[Turbo] Disabled");
            enabled = false;
            return;
        }
        
        try {
            this.createTable();
        } catch (SQLException ex) {
ex.printStackTrace();
System.out.println("[Turbo] Cannot insert the Table");

enabled = false;
            return;

            
        }
        System.out.println("Turbo started.");
        enabled = true;
    }
  


}
