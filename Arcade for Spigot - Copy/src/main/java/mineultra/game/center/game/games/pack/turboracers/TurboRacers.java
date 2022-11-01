package mineultra.game.center.game.games.pack.turboracers;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineultra.core.common.CachedPerm;
import mineultra.core.common.MySQL;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.ValueComparator;
import mineultra.core.itemstack.ItemStackFactory;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.kit.Kit;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;

public class TurboRacers extends TeamGame
{
  private Language lg = null;
  private static long startTime = 0;
  public TurboRacers(centerManager manager)
  {
    super(manager, GameType.TurboRacers, 
      new Kit[] {}, 
      new String[] {
     });

    this._help =  this.Manager.getConfig().getStringList("tips").toArray(new String[0]);
lg = new Language(this.Manager.getConfig());
    HungerSet = 20;
    DeathOut = false;
    this.BlockBreak = false;
    this.BlockPlace = false;
    this.DeathDropItems = false;
    this.ItemDrop = false;
    this.ItemPickup = true;
    this.WorldTimeSet = 10000;
    this.DamagePvP = true;
    this.QuitOut = true;
    this.PrepareFreeze = false;
    this.DamageEvP = false;
    this.DamagePvE = false;
    this.CreatureAllowOverride=true;
    this.CreatureAllow=true;
  

    startTime = System.currentTimeMillis();
    Manager.GetExplosion().SetRegenerate(true);
   
  }
 
  
@Override
  public void ParseData()
  {
     
this.WorldData.World.setSpawnFlags(true, true);
      Manager.GetExplosion().SetRegenerate(true);
     
      this.loadMap();
this.loadAddons();
      this.init();
  }
  
  public void loadAddons(){
      try{
      this.loadCoins();
      this.loadBoxs();
      
      this.loadHelmets();
      this.loadHorns();
      this.loadSkins();
      }catch(Exception e){
          
      }
  }
  
Random r = new Random();
HashMap<Integer,Integer> tasks = new HashMap();

public void sendRandomPart(final Player pe, final int pos){
    pe.closeInventory();


final int ren = r.nextInt(999999);

int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Manager.GetPlugin(), new Runnable(){
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
if(type > 10){
    type-=pos;
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
   
Bukkit.broadcastMessage(Colorizer.Color(pe.getDisplayName()+" &ahas earned "+PCNameLevel((int)type)+pcTypeName(btype)));
addValue(pe, ctype, ";"+val1+","+val2+","+val3, false);
 
}catch(Exception e){
    
}
    try {
    
        Bukkit.getServer().getScheduler().cancelTask(tasks.get(ren));

    } catch (Throwable ex) {
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
       
       UtilDisplay.sendTitle(pe, "", Colorizer.Color(PCNameLevel(type)+pcTypeName(btype)));
   
    }
    }, 2L,2L);

tasks.put(ren, task);


}

public String pcTypeName(int type){
    if(type == 0){
        return " Engine";
    }
    else if(type  == 1){
        return " Turbo";
    }
    else if(type == 2){
        return " Chassis";
    }
    return "";
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

  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
  
 
  
  public static ItemStack read(String str)
  {
    String[] split = str.split(",");
    ArrayList<String> lores = new ArrayList();
    
    ItemStack i = new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Short.parseShort(split[2]));
    for (int a = 1; a < split.length; a++)
    {
      if (split[a].startsWith("lore:"))
      {
        ItemMeta im = i.getItemMeta();
        
        String s1 = split[a].replace("lore:", "");

        String s2 = Color(s1);
        lores.add(s2);im.setLore(lores);i.setItemMeta(im);
      }
      for (Enchantment enc : Enchantment.values()) {
        if (split[a].toUpperCase().startsWith(enc.getName().toUpperCase()))
        {
          String s1 = split[a].replace(enc.getName().toUpperCase() + ":", "");
          i.addUnsafeEnchantment(enc, Integer.parseInt(s1));
        }
      }
      if (split[a].startsWith("name:"))
      {
        ItemMeta im = i.getItemMeta();
        String nam = split[a].replace("name:", "").replace("%name", getFriendlyItemName(i.getType()));
        im.setDisplayName(Color(nam));
        i.setItemMeta(im);
      }
    }
    return i;
  }

HashMap<String,String> suitname = new HashMap();

public String getSuitName(int id, int line){
  if(suitname.containsKey(""+id+"-"+line)){
      return suitname.get(""+id+"-"+line);
  }
    String ret = Colorizer.Color(this.Manager.getConfig().getStringList("TurboRacers.suits.L"+id).get(line));
suitname.put(""+id+"-"+line, ret);
return ret;
}

  
  @EventHandler
  public void onJa(PlayerJoinEvent event){
   
Player pe = event.getPlayer();

pe.playSound(pe.getLocation(), "announcer.welcome", 1F, 1F);
UtilDisplay.sendTitle(pe, Colorizer.Color("&eTurbo Kart Racers!"), Colorizer.Color("&efrom &cSpigotMC"));
  }
  
  public void setCar(Player pe){
    
      if(this.hasTimed()){
          return;
      }
      if(this.isEditing(pe)){
          return;
      }
                        if(this.flaps.containsKey(pe)){
                      if(this.flaps.get(pe) != true){
                          return;
                      }}
                        
if(pe.getVehicle() != null){
    pe.getVehicle().remove();
}

this.spawnCustomVehicle(pe, CustomEntity.Vehicle, "ArmorStand", pe.getLocation(), ((CraftWorld)pe.getWorld()).getHandle().b()); 

CustomVehicleCar car = customentitys.get(pe);

car.manager = this;
car.getHandle().getHandle().setInvisible(true);
car.getHandle().setCustomNameVisible(false);
car.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.DISPENSER)));
car.getHandle().setPassenger(pe);
car.setSound("a");

// sound
try {
          String hsound = this.getValue(pe, columnType.Horns).split("-")[1];
          car.setSound(this.hornsound.get(Integer.parseInt(hsound)));
      } catch (SQLException ex) {
          Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
      }
// helmet
    try{
int helmet = Integer.parseInt(this.getValue(pe, columnType.Helmets).split("-")[1]);
pe.getInventory().setHelmet(this.makeItem(Material.getMaterial(this.helmmat.get(helmet)), 1, this.helmdura.get(helmet), this.helmname.get(helmet),null));
    }catch(SQLException | NumberFormatException ex){
      Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
    }
    
// trail
    try{
int ptrail = Integer.parseInt(this.getValue(pe, columnType.ParticleTrail).split("-")[1]);
car.setTrail(ptrail);
    }catch(SQLException | NumberFormatException ex){
      Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
    }
    
// skin
     try{
int skin = Integer.parseInt(this.getValue(pe, columnType.KartSkin).split("-")[1]);
car.setEquipment(4, CraftItemStack.asNMSCopy(new ItemStack(this.skinmat.get(skin),1,this.skindura.get(skin))));
     }catch(SQLException | NumberFormatException ex){
      Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
    }
     
// suit
     try{ 
int suit = Integer.parseInt(this.getValue(pe, columnType.RacingSuits).split("-")[1]);
if(suit == 0){
    pe.getInventory().setChestplate(this.makeItem(Material.GOLD_CHESTPLATE, 1, (short)0,this.getSuitName(0,0), null));
    pe.getInventory().setLeggings(this.makeItem(Material.GOLD_LEGGINGS, 1, (short)0,this.getSuitName(0,1), null));
    pe.getInventory().setBoots(this.makeItem(Material.GOLD_BOOTS, 1, (short)0,this.getSuitName(0,2), null));
}
if(suit == 1){
    pe.getInventory().setChestplate(this.makeItem(Material.CHAINMAIL_CHESTPLATE, 1, (short)0,this.getSuitName(1,0), null));
    pe.getInventory().setLeggings(this.makeItem(Material.CHAINMAIL_LEGGINGS, 1, (short)0,this.getSuitName(1,1), null));
    pe.getInventory().setBoots(this.makeItem(Material.CHAINMAIL_BOOTS, 1, (short)0,this.getSuitName(1,2), null));
}
else if(suit == 2){
    pe.getInventory().setChestplate(this.makeItem(Material.IRON_CHESTPLATE, 1, (short)0,this.getSuitName(2,0), null));
    pe.getInventory().setLeggings(this.makeItem(Material.IRON_LEGGINGS, 1, (short)0,this.getSuitName(2,1), null));
    pe.getInventory().setBoots(this.makeItem(Material.IRON_BOOTS, 1, (short)0,this.getSuitName(2,2), null));
}
else if(suit == 3){
    pe.getInventory().setChestplate(this.makeItem(Material.DIAMOND_CHESTPLATE, 1, (short)0,this.getSuitName(3,0), null));
    pe.getInventory().setLeggings(this.makeItem(Material.DIAMOND_LEGGINGS, 1, (short)0,this.getSuitName(3,1), null));
    pe.getInventory().setBoots(this.makeItem(Material.DIAMOND_BOOTS, 1, (short)0,this.getSuitName(3,2), null));  
}
if(suit == 4){
    pe.getInventory().setChestplate(this.makeItem(Material.LEATHER_CHESTPLATE, 1, (short)0,this.getSuitName(4,0), null));
    pe.getInventory().setLeggings(this.makeItem(Material.LEATHER_LEGGINGS, 1, (short)0,this.getSuitName(4,1), null));
    pe.getInventory().setBoots(this.makeItem(Material.LEATHER_BOOTS, 1, (short)0,this.getSuitName(4,2), null));
}
     }catch(SQLException | NumberFormatException ex){
      Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
    }
     
// particle quality
     try{
int quality = Integer.parseInt(this.getValue(pe, columnType.ParticleQuality));
car.setParticleQuality(quality);
     }catch(SQLException | NumberFormatException ex){
      Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
    }
     
// engine
try{
 String[] vals = this.getValue(pe, columnType.Engine).split("-")[1].split(",");
 
 //boost recovery
 int val1 = Integer.parseInt(vals[0]);
 car.increaseBoostDuration(val1);

 //top speed
 int val2 = Integer.parseInt(vals[1]);
  car.increaseMaxSpeed(val2);
  
 // acceleration
 int val3 = Integer.parseInt(vals[2]);
  car.increaseAcceleration(val3);
} catch(SQLException | NumberFormatException e){
    
}

// turbocharger
try{
 String[] vals = this.getValue(pe, columnType.Turbocharger).split("-")[1].split(",");
 
 //booster speed
 int val1 = Integer.parseInt(vals[0]);
 car.increaseBoostersSpeed(val1);

 //drifting
 int val2 = Integer.parseInt(vals[1]);
 car.increaseDrifting(val2);
  
 // stop quicker
 int val3 = Integer.parseInt(vals[2]);
 //not finished

} catch(Exception e){
    e.printStackTrace();
}

// frame
try{
 String[] vals = this.getValue(pe, columnType.Turbocharger).split("-")[1].split(",");
 
 // handling
 int val1 = Integer.parseInt(vals[0]);
car.reduceRotationSpeed(val1);

 // start position
 int val2 = Integer.parseInt(vals[1]);
  // not finished
 
 // traction
 int val3 = Integer.parseInt(vals[2]);
 car.increaseTraction(val3);
} catch(Exception e){
    e.printStackTrace();
}

    
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

  public List<ItemStack> items  = new ArrayList();
  // rojo,bolafuego,coheteazul,snowball,coheteverde


public HashMap<Player,Long> inbox = new HashMap();

  public void sendBox(Player p){
      if(!this.IsLive()){
          return;
      }
      if(items.isEmpty()){
          return;
      }

      p.getInventory().clear();
      
      inbox.put(p, System.currentTimeMillis()+4000+r.nextInt(3000));
      
      p.updateInventory();
  }
  
public void playNote(Player pe,int val0, int val1, int val2, int val3){

    if(val0 == 0){
pe.playNote(pe.getLocation(), Instrument.values()[val1], Note.flat(val2, Note.Tone.values()[val3]));    
}
if(val0 == 1){
pe.playNote(pe.getLocation(), Instrument.values()[val1], Note.natural(val2, Note.Tone.values()[val3])); 
}
if(val0 == 2){
pe.playNote(pe.getLocation(), Instrument.values()[val1], Note.sharp(val2, Note.Tone.values()[val3]));
}
}
  int tick = 0;
  @EventHandler
  public void onRandomBox(UpdateEvent event){
      if(event.getType() != UpdateType.TICK){
          return;
      }
      if(!this.IsLive()){
          return;
      }
      if(inbox.isEmpty()){
          return;
      }
      if(items.isEmpty()){
          return;
      }
      for(Player p : inbox.keySet()){
          if(!p.isOnline()){
              inbox.remove(p);
          continue;
          }
          if(!this.GetPlayers(true).contains(p)){
              p.getInventory().clear();
              inbox.remove(p);
              continue;
          }
          if(inbox.get(p) < System.currentTimeMillis()){
              for(int a = 0; a < p.getInventory().getSize();a++){
             
             if(a != p.getInventory().getHeldItemSlot() && p.getInventory().getItem(a) != null){
                 p.getInventory().clear(a);
             }
          
              }
    
              p.updateInventory();
              inbox.remove(p);
              continue;
          }
          if(tick >= 3){
              tick = 0;
          p.playSound(p.getLocation(), "random.orb", 0.20F, 1F);   
          }else{
              tick++;
          }
           
          for(int a = 0; a < 9;a++){
              p.getInventory().setItem(a,items.get(r.nextInt(items.size()-1)));
          }
      }
  }
  
  
  @EventHandler
  public void onBoxCoin(GameStateChangeEvent event){
if(event.GetState() == GameState.Live){

if(!items.isEmpty()){
    items.clear();
}

items.add(ItemStackFactory.Instance.CreateStack(Material.RED_ROSE, (byte)0, 1, (short)1, String.valueOf(C.cDGreen) + "Self Rocket", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw to the nearest player" }));
items.add(ItemStackFactory.Instance.CreateStack(Material.RED_ROSE, (byte)0, 1, (short)2, String.valueOf(C.cDGreen) + "Fireball", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw it out in front" }));
items.add(ItemStackFactory.Instance.CreateStack(Material.RED_ROSE, (byte)0, 1, String.valueOf(C.cDGreen) + "Blue Rocket", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw it out in front" }));
items.add(ItemStackFactory.Instance.CreateStack(Material.SNOW_BALL, (byte)0, 1, String.valueOf(C.cDGreen) + "Snowball", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw it out in front" }));
items.add(ItemStackFactory.Instance.CreateStack(Material.DEAD_BUSH, (byte)0, 1, String.valueOf(C.cDGreen) + "Green Rocket", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw it out in front" }));
items.add(ItemStackFactory.Instance.CreateStack(Material.TNT, (byte)0, 1, String.valueOf(C.cDGreen) + "Bomb", new String[] { "", ChatColor.RESET + "Right-Click", ChatColor.RESET + "to throw it out in back" }));
    
    try{
    for(Iterator<Location> it = this.coinsplaced.iterator();it.hasNext();){
    this.spawnCustomItemDrop(CustomEntity.ItemDrop, "Coin",it.next(),((CraftWorld)WorldData.World).getHandle().b(),"Coin");
}}catch(Exception e){
    
}
    try{
    for(Iterator<Location> it = this.boxsplaced.iterator();it.hasNext();){
    this.spawnCustomItemDrop(CustomEntity.ItemDrop, "Box",it.next(),((CraftWorld)WorldData.World).getHandle().b(),"Box");
}}catch(Exception e){
    
}
}
}


// #38:1 Red Missil: 0
// #32 Green Missil: 1
//#38 Blue Missil: 2
// #38:2 Fireball: 3
// SnowBall: 4



  @EventHandler
  public void onMissil(PlayerInteractEvent event){
      if(event.getPlayer().getItemInHand() == null){
          return;
      }
      if(!this.IsLive()){
          return;
      }
      if(event.getAction() != Action.RIGHT_CLICK_AIR 
              && event.getAction()  != Action.RIGHT_CLICK_BLOCK
              && event.getAction() != Action.PHYSICAL){
          return;
      }
      if(inbox.containsKey(event.getPlayer())){
          if(inbox.get(event.getPlayer()) > System.currentTimeMillis()){
              event.setCancelled(true);
              return;
          }else{
              inbox.remove(event.getPlayer());
          }
      }
      
      
      Material mate = event.getPlayer().getItemInHand().getType();
      short sh = event.getPlayer().getItemInHand().getDurability();
      if(mate.equals(Material.DEAD_BUSH)){
          this.spawnCustomMissile(CustomEntity.Missile, event.getPlayer(), event.getPlayer().getLocation().add(0, 1, 0), ((CraftWorld)WorldData.World).getHandle().b(), 1);
                  if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
      if(mate.equals(Material.RED_ROSE) && sh == 1){
          this.spawnCustomMissile(CustomEntity.Missile, event.getPlayer(), event.getPlayer().getLocation().add(0, 1, 0), ((CraftWorld)WorldData.World).getHandle().b(), 0);
                  if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
      else if(mate.equals(Material.RED_ROSE) && sh == 2){
this.spawnCustomMissile(CustomEntity.Missile, event.getPlayer(), event.getPlayer().getLocation().add(0, 1, 0), ((CraftWorld)WorldData.World).getHandle().b(), 3);
                  if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
      else if(mate.equals(Material.RED_ROSE)){
this.spawnCustomMissile(CustomEntity.Missile, event.getPlayer(), event.getPlayer().getLocation().add(0, 1, 0), ((CraftWorld)WorldData.World).getHandle().b(), 2);
            if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
     else if(mate.equals(Material.SNOW_BALL)){
this.spawnCustomMissile(CustomEntity.Missile, event.getPlayer(), event.getPlayer().getLocation().add(0, 1, 0), ((CraftWorld)WorldData.World).getHandle().b(), 4);
            if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
      else if(mate.equals(Material.TNT)){
event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.PRIMED_TNT);

            if(event.getPlayer().getItemInHand().getAmount() > 1){
          event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
      }else{
          event.getPlayer().getInventory().clear();
      }
      }
      
      

  }

  
  @EventHandler
  public void onCar(UpdateEvent event){
      if(event.getType() != UpdateType.SEC){
          return;
      }
      if(this.hasTimed()){
          return;
      }
      if(this.started){
          for(Player p : this.GetPlayers(true)){
              if(p.getVehicle() == null){
                  if(!laps.containsKey(p)){
                      laps.put(p, 0.0);
                  }

if(p.getWorld().getName().equalsIgnoreCase("world")){
    Integer lap = (int)(this.laps.get(p)*1);
    p.teleport(this.gamemap.get(lap).get(0));
}

this.setCar(p);
                                            
                      
                  

              }
          }
      }else if(this.GetState() == GameState.Recruit){
          for(Player p : Bukkit.getOnlinePlayers()){
              if(p.getVehicle() == null){
                  this.setCar(p);                          
                      
                  
              }
          }
      }
  }
  
  
  //0: Bump Jump
  //1: Spin Out
  //2: Slow
  
  public HashMap<Player,HashMap<Integer,Long>> bimap = new HashMap();
  
  public void rotateBump(Player pe, int type){
bimap.put(pe, new HashMap<Integer,Long>());
if(type == 0){
  bimap.get(pe).put(type, System.currentTimeMillis()+5000);
}
if(type == 1){
 bimap.get(pe).put(type, System.currentTimeMillis()+5000);
}
if(type == 2){
 bimap.get(pe).put(type, System.currentTimeMillis()+5000);
}
    
  }
  

  CachedPerm perm = new CachedPerm();
  
  HashMap<Integer,List<Location>> mapping = new HashMap();
  HashMap<Integer,List<Location>> coinsmap = new HashMap();
  HashMap<Integer,List<Location>> boxsmap = new HashMap();
  int mapid = -1;
  int coinid = -1;
  int boxid = -1;
  
  
 public boolean isEditing(Player pe){
     if(mapwand.containsKey(pe)){
         return true;
     }
     if(coinswand.containsKey(pe)){
         return true;
     }
     if(magicboxwand.containsKey(pe)){
         return true;
     }
     return false;
 }
  
  HashMap<Player,Boolean> mapwand = new HashMap();
  HashMap<Player,Boolean> coinswand = new HashMap();
  HashMap<Player,Boolean> magicboxwand = new HashMap();
  
  
  
  // mapwand:
  // Left: next
  // Right: add
  
  // CoinWand:
  // Left: More
  // Right: More
  
  // BoxWand:
  // Left: More
  // Right: More
  
  
  @EventHandler
  public void onEditorInteract(PlayerInteractEvent event){
      if(event.getPlayer().getItemInHand() == null){
          return;
      }
      if(!event.getPlayer().getItemInHand().hasItemMeta()){
          return;
      }
      if(!this.isEditing(event.getPlayer())){
          return;
      }
      
      Material slot = event.getPlayer().getItemInHand().getType();
      if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
         if(slot.equals(Material.BLAZE_ROD)){
        mapid++;
          mapping.put(mapid, new ArrayList());
          mapping.get(mapid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&cLocID %&a"+mapid));
          
         }else if(slot.equals(Material.GOLD_INGOT)){
        coinid++;
          coinsmap.put(coinid, new ArrayList());
          coinsmap.get(coinid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&eCoins : &a"+coinid));
          
         }else if(slot.equals(Material.BONE)){
          boxid++;
          boxsmap.put(boxid, new ArrayList());
          boxsmap.get(boxid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&7Box: &a"+boxid));
         }
      }else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() ==  Action.RIGHT_CLICK_BLOCK){
          
          if(slot.equals(Material.BLAZE_ROD)){
          if(mapid == -1){
              mapid = 0;
              mapping.put(mapid, new ArrayList());
          }
          mapping.get(mapid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&c%&a"+mapid+" &e- Loc"+mapping.get(mapid).size()));
      }
          else if(slot.equals(Material.GOLD_INGOT)){
        coinid++;
          coinsmap.put(coinid, new ArrayList());
          coinsmap.get(coinid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&eCoin: &a"+coinid));
          }
          else if(slot.equals(Material.BONE)){
          boxid++;
          boxsmap.put(boxid, new ArrayList());
          boxsmap.get(boxid).add(event.getPlayer().getLocation());
          event.getPlayer().sendMessage(Colorizer.Color("&7Box: &a"+boxid));
         }
      }
  }
 
  // text[0] = /turbo
  // text[1] = map - coins - boxs
  
  // mapconfig:
  // -> Set
  // -> Add
  // -> Wand
  // -> Save
  
  // boxs:
  // -> Add
  // -> Wand
  // -> Save
  
  // coins:
  // -> Add
  // -> Wand
  // -> Save
  
  public boolean mapConfig(Player p, String[] text){
      if(text.length == 2){
          p.sendMessage(Colorizer.Color("&c/&ehrc map wand"));
          p.sendMessage(Colorizer.Color("&c/&ehrc map save"));
          return true;
      }
      String cmd = text[2];
 if(cmd.equals("wand")){
       p.sendMessage(Colorizer.Color("&aLeft-Click: &eNext %"));
       p.sendMessage(Colorizer.Color("&aRight-Click: &eAdd locacion"));
          ItemStack vara = new ItemStack(Material.BLAZE_ROD);
          ItemMeta vm = vara.getItemMeta();
          vm.setDisplayName(Colorizer.Color("&cLeft: &aNext, &eRight: &aAdd"));
          vara.setItemMeta(vm);
       p.getInventory().addItem(vara);
          mapwand.put(p, true);
 return true;
 
 }else if(cmd.equals("save")){
          
          FileConfiguration cfg = this.Manager.getConfig();
        if(mapping.size() < 100){

            p.sendMessage(Colorizer.Color("&aYou must select 100 points"));
            for(int a = 0; a < 100;a++){
              if(!mapping.containsKey(a)){
                 p.sendMessage(Colorizer.Color("&c"+a));
              }
            }
        }else{
            for(int a = 0; a < 100;a++){
                String locs = "";
                
                for(Location loc : mapping.get(a)){
                    if(locs.length() <= 1){
                        locs = locs+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }else{
                        locs = locs+":"+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }
                }
                
                cfg.set(this.WorldData.MapName+".map.L"+a, locs);
            }
           this.Manager.getConfig().saveToString();
           this.Manager.GetPlugin().saveConfig();
           
            p.sendMessage(Colorizer.Color("&aCheck the config"));
        }
     return true;
      }
      return true;
  }
  
  public boolean Boxs(Player p, String[] text){
      if(text.length == 2){
                    p.sendMessage(Colorizer.Color("&c/&ehrc boxs wand"));
          p.sendMessage(Colorizer.Color("&c/&ehrc boxs save"));
          return true;
      }
      String cmd = text[2];
 if(cmd.equals("wand")){
       p.sendMessage(Colorizer.Color("&aLeft-Click: &eAdd MagicBox"));
       p.sendMessage(Colorizer.Color("&aRight-Click: &eAdd MagicBox"));
          ItemStack vara = new ItemStack(Material.BONE);
          ItemMeta vm = vara.getItemMeta();
          vm.setDisplayName(Colorizer.Color("&cLeft: &aAdd Box, &eRight: &aAdd Box"));
          vara.setItemMeta(vm);
       p.getInventory().addItem(vara);
          magicboxwand.put(p, true);
 return true;
 
 }else if(cmd.equals("save")){
          
          FileConfiguration cfg = this.Manager.getConfig();

            for(int a = 0; a < boxsmap.size();a++){
                String locs = "";
                
                for(Location loc : boxsmap.get(a)){
                    if(locs.length() <= 1){
                        locs = locs+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }else{
                        locs = locs+":"+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }
                }
                
                cfg.set(this.WorldData.MapName+".boxs.L"+a, locs);
            }
           this.Manager.getConfig().saveToString();
           this.Manager.GetPlugin().saveConfig();
           
            p.sendMessage(Colorizer.Color("&aCheck the Config"));
        
     return true;
      }
      return true;
  }
  
  public boolean Coins(Player p, String[] text){
      if(text.length == 2){
                    p.sendMessage(Colorizer.Color("&c/&ehrc coins wand"));
          p.sendMessage(Colorizer.Color("&c/&ehrc coins save"));
          return true;
      }
      String cmd = text[2];
 if(cmd.equals("wand")){
       p.sendMessage(Colorizer.Color("&aLeft-Click: &eAdd +Coin"));
       p.sendMessage(Colorizer.Color("&aRight-Click: &eAdd +Coin"));
          ItemStack vara = new ItemStack(Material.GOLD_INGOT);
          ItemMeta vm = vara.getItemMeta();
          vm.setDisplayName(Colorizer.Color("&cLeft: &aAdd Coin, &eRight: &aAdd Coin"));
          vara.setItemMeta(vm);
       p.getInventory().addItem(vara);
          coinswand.put(p, true);
 return true;
 
 }else if(cmd.equals("save")){
          
          FileConfiguration cfg = this.Manager.getConfig();

            for(int a = 0; a < coinsmap.size();a++){
                String locs = "";
                
                for(Location loc : coinsmap.get(a)){
                    if(locs.length() <= 1){
                        locs = locs+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }else{
                        locs = locs+":"+loc.getX()+","+loc.getY()+","+loc.getZ();
                    }
                }
                
                cfg.set(this.WorldData.MapName+".coins.L"+a, locs);
            }
           this.Manager.getConfig().saveToString();
           this.Manager.GetPlugin().saveConfig();
           
            p.sendMessage(Colorizer.Color("&aCheck the Config"));
        
     return true;
      }
      return true;
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void prepro(PlayerCommandPreprocessEvent event){
if(!perm.hasPerm(event.getPlayer(), "mineultra.admin")){
    return;
}
String msg = event.getMessage().toLowerCase();
if(!msg.startsWith("/hrc")){
    return;
}
Player p = event.getPlayer();
String[] parts = msg.split(" ");

if(parts.length <= 1){
         p.sendMessage(Colorizer.Line);
         p.sendMessage(Colorizer.Color(" &c/&ehrc map"));
         p.sendMessage(Colorizer.Color(" &c/&ehrc coins"));
         p.sendMessage(Colorizer.Color(" &c/&ehrc boxs"));
         p.sendMessage(Colorizer.Line);
         event.setCancelled(true);
         return;
} else 
if(parts.length >= 2){
    
    if(parts[1].equals("map")){
        event.setCancelled(this.mapConfig(p, parts));
    }
    else if(parts[1].equals("coins")){
        event.setCancelled(this.Coins(p, parts));
    }else if(parts[1].equals("boxs")){
        event.setCancelled(this.Boxs(p, parts));
    }
    
    
}



  }
  
  public static String getFriendlyItemName(Material m)
  {
    String str = m.toString();
    str = str.replace('_', ' ');
    str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    
    return str;
  }
  
HashMap<Integer,List<Location>> gamemap = new HashMap();
HashSet<Location> boxsplaced = new HashSet();
HashSet<Location> coinsplaced = new HashSet();

HashMap<Player,Double> laps = new HashMap();
HashMap<Player,Integer> completedlaps = new HashMap();

@EventHandler
public void nofall(CustomDamageEvent event){
    if(event.GetCause() == null){
        return;
    }
    if(event.GetCause() == DamageCause.FALL || event.GetCause() == DamageCause.SUFFOCATION){
        event.SetCancelled("NoFall");
    }

}

  public void loadMap(){
      try{
      for(int a = 0; a < 100;a++){
          gamemap.put(a, new ArrayList());
          String line = this.Manager.getConfig().getString(WorldData.MapName+".map"+".L"+a);
          
          String[] ph = line.split(":");
          for (String ph1 : ph) {
              String[] coords = ph1.split(",");
              gamemap.get(a).add(new Location(this.WorldData.World, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2])));
          
      }}}catch(Exception e){
              System.out.println("No se ha podido cargar las coordenadas del mapa -> WorldData.map.L<Number>");
              }
  }
  
  
  public void loadBoxs(){
      int size = 0;
      try{
      for(int a = 0;a < 500;a++){
          String line = this.Manager.getConfig().getString(WorldData.MapName+".boxs.L"+a);
          if(line != null){
          size++;
          String[] lines = line.split(",");
          this.boxsplaced.add(new Location(this.WorldData.World,Double.parseDouble(lines[0]),Double.parseDouble(lines[1]),Double.parseDouble(lines[2])+3));
          }else{
  System.out.println("Loaded "+size+" Magic Boxs");
  break;
          }
          
      }}catch(Exception e){

  return;
          
      }
  }
  
  public void loadCoins(){
      int size = 0;
      try{
      for(int a = 0;a < 500;a++){
          String line = this.Manager.getConfig().getString(WorldData.MapName+".coins.L"+a);
          if(line != null){
          size++;
          String[] lines = line.split(",");
          this.coinsplaced.add(new Location(this.WorldData.World,Double.parseDouble(lines[0])+3,Double.parseDouble(lines[1]),Double.parseDouble(lines[2])));
          }else{
  System.out.println("Loaded "+size+" Coins");
  break;
          }
          
      }}catch(Exception e){
  System.out.println("Loaded "+size+" Coins");
  return;
          
      }
  }
  
  @EventHandler
  public void onload(ChunkLoadEvent event){
      if(this.GetState() == GameState.Live){
          return;
      }
      if(event.getChunk().getEntities().length > 0){
          for(Entity et : event.getChunk().getEntities()){
              et.remove();
          }
      }
  }
  
  
  public void watchLap(Player p){
  
      if(flaps.containsKey(p)){
            if(flaps.get(p) == true){
                return;
            }
        }
    Location ploc = p.getLocation();
    Location closest = null;
    double distancesqua = 200;
    double value = 0;
    double now = laps.get(p);
     
    for(int a = (int)0; a <= now+20;a++){
     if(a < 0 || a > 99){
         continue;
     }
     
     for(Location loc : gamemap.get(a)){
       if(closest == null){
           closest = loc;
           distancesqua = loc.distance(ploc);
           value = a;
       continue;
       }
       
       double distance = loc.distance(ploc);
       if(distance < distancesqua){
           distancesqua = loc.distance(ploc);
           closest = loc;
           value = a;
       }
     }
    }
    
     if(value < now){
     UtilDisplay.sendTitle(p, Colorizer.Color("&cYou are going back!!"), Colorizer.Color("&7Press &cSHIFT &7if stuck!"));
       
     }

    if(value >= 99 ){   

        p.sendMessage(Colorizer.Color("&f&lLap Time: "+this.getLapTime(p)));
        laps.put(p, 0.0);
        this.addValue(p, columnType.Played, ""+1, true);
        completedlaps.put(p, completedlaps.get(p)+1);
        if(completedlaps.get(p) >= 4){
            this.afterLap(p);
        }else{
            int cclaps = completedlaps.get(p);
            if(cclaps == 1){
  this.playModdedSound(p, "announcer.lap.two", 1F, 1F);
UtilDisplay.sendTitle(p, Colorizer.Color("&fLap dos!"), " ");                
            }
            else if(cclaps == 2){
  this.playModdedSound(p, "announcer.lap.three", 1F, 1F);
UtilDisplay.sendTitle(p, Colorizer.Color("&fLap tres!"), " ");
            }else if(cclaps == 3){
  this.playModdedSound(p, "announcer.lap.final", 1F, 1F);
UtilDisplay.sendTitle(p, Colorizer.Color("&fFinal Lap!"), " ");
            }

        }
    }else if(value != 99){
    laps.put(p, value);        
    }

  }
  
  public void playModdedSound(Player p, String sound, Float pow, Float pitch){
p.playSound(new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()), sound, pow, pitch);

      int sizes = 48;

while(sizes > -48){
p.playSound(new Location(p.getWorld(),p.getLocation().getX()+sizes,p.getLocation().getY(),p.getLocation().getZ()), sound, pow, pitch);
p.playSound(new Location(p.getWorld(),p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()+sizes), sound, pow, pitch);
sizes-=16;
}


      
      
  }
  
  @EventHandler
  public void onLeave(PlayerQuitEvent event){

      if(this.completedlaps.containsKey(event.getPlayer())){
          completedlaps.remove(event.getPlayer());
      }
      if(this.laps.containsKey(event.getPlayer())){
          laps.remove(event.getPlayer());
      }
      if(this.customentitys.containsKey(event.getPlayer())){
          customentitys.remove(event.getPlayer());
      }
      if(this.position.containsKey(event.getPlayer())){
          position.remove(event.getPlayer());
      }
      if(this.points.containsKey(event.getPlayer().getName())){
          points.remove(event.getPlayer().getName());
      }
     
  }
  
  public HashMap<Player,Integer> position = new HashMap();
  Map<String,Integer> points = new TreeMap<>();
  public void updatePositions(){
      if(this.hasTimed()){
          return;
      }

      for(Player pe : this.GetPlayers(true)){
      if(!laps.containsKey(pe)){
          laps.put(pe, 0.0);
      }
      if(!completedlaps.containsKey(pe)){
          completedlaps.put(pe,0);
      }
          int ptotal = 0;
          if(completedlaps.get(pe) > 0){
             ptotal+=completedlaps.get(pe)*100;
          }
          ptotal+=laps.get(pe);
          points.put(pe.getName(), ptotal);
      }

           ValueComparator comp =  new ValueComparator(points);
            TreeMap<String,Integer> sorted_map = new TreeMap<>(comp);
            sorted_map.putAll(points);
            int pos = 1;
            for(String str : sorted_map.keySet()){
                Player pe = Bukkit.getPlayer(str);
                if(!position.containsKey(pe)){
                    position.put(pe, 10);
                }
                if(pos == 1){
                    if(position.get(pe) > pos){
                        this.playModdedSound(pe, "announcer.takenthelead", 1F, 1F);
                       pe.sendMessage(Colorizer.Color("&aYou take the lead!"));
                    }
                }
                position.put(pe, pos);
                pos++;
            }

  
  }
 
    
  
  @EventHandler
  public void updatePosz(UpdateEvent event){
      if(event.getType() !=  UpdateType.SEC){
          return;
      }
      if(!this.started){
          return;
      }
  
      this.updatePositions();
  }
      
  
  String actionbar = null;
  
  @EventHandler
  public void updatePos(UpdateEvent event){
      if(event.getType() !=  UpdateType.TICK){
          return;
      }
      if(!this.started){
          if(actionbar == null){
              actionbar = Manager.getConfig().getString("TurboRacers.actionbar");
          }
          for(Player p : Bukkit.getOnlinePlayers()){
              UtilDisplay.sendActionBar(p, Colorizer.Color(actionbar));
          }
          return;
      }
   
if(this.hasTimed()){
    return;
}
      
      
      for(Player pe : Bukkit.getOnlinePlayers()){
          if(!completedlaps.containsKey(pe)){
              completedlaps.put(pe, 0);
          }
       if(!laps.containsKey(pe)){
        laps.put(pe, 0.0);
    }
       if(!position.containsKey(pe)){
           position.put(pe, 16);
       }
          this.watchLap(pe);
          String claps = "";
          if(completedlaps.get(pe) > 50){
              claps = "ALL";
          }else{
              
              claps = ""+(completedlaps.get(pe)+1);
          }
          UtilDisplay.sendActionBar(pe, Colorizer.Color("&aPos: &f&l#"+position.get(pe) +"&7- &bLap: &f&l"+claps+" &7- &aProgress: &f&l"+(laps.get(pe)+1)+"% &7- &eCoins: &f&l"+this.winnedcoins.get(pe)));
      }
      
  }
  public boolean started = false;
  
  int prep = 0;
  
  @EventHandler
  public void onState(GameStateChangeEvent event){
      if(event.GetState() == GameState.Prepare){
                for(Player p : Bukkit.getOnlinePlayers()){

this.setCar(p);

      }
          prep = 20;
      }
  }
  
  
  
  
  @EventHandler
  public void onChan(UpdateEvent event){
      if(event.getType() !=UpdateType.SEC){
          return;
      }
      if(prep > 0){
          if(prep <= 5){
              for(Player pee : Bukkit.getOnlinePlayers()){
              this.playNote(pee, 2, 3, 1, 3);
              }
             
          }
          prep--;
          if(prep == 17){
for(Player pe : Bukkit.getOnlinePlayers()){
   this.setCar(pe);
this.playModdedSound(pe, "announcer.ready", 1F, 1F);
UtilDisplay.sendTitle(pe, Colorizer.Color("&cReady?"), " ");
          }
          }
          if(prep == 13){
for(Player pe : Bukkit.getOnlinePlayers()){
    this.setCar(pe);
    this.playModdedSound(pe, "announcer.set", 1F, 1F);
UtilDisplay.sendTitle(pe, Colorizer.Color("&aSET"), " ");
          }
          }
          if(prep == 9){
for(Player pe : Bukkit.getOnlinePlayers()){
this.playModdedSound(pe, "announcer.go", 1F, 1F);
UtilDisplay.sendTitle(pe, Colorizer.Color("&aGo!"), " ");
          }
          
started = true;
          }
          if(prep == 1){
for(Player pe : Bukkit.getOnlinePlayers()){
    
this.playModdedSound(pe, "announcer.lap.one", 1F, 1F);
UtilDisplay.sendTitle(pe, Colorizer.Color("&fLap one!"), " ");
          }
          }
      }


  }
@EventHandler
public void onJainend(GameStateChangeEvent event){
    if(event.GetState()  != GameState.End){
        return;
    }
    
    if(songplayers.isEmpty()){
     
        return;
    }
try{
   for(Player pe : songplayers.keySet()){
       if(!pe.isOnline()){
           songplayers.remove(pe);
       }
       songplayers.get(pe).destroy();
       if(songplayers.containsKey(pe)){
           songplayers.remove(pe);
       }
   }
}catch(Exception e){
    e.printStackTrace();
}}

@EventHandler
public void onJain(GameStateChangeEvent event){
    if(event.GetState()  != GameState.Live){
        return;
    }
    for(Player p : this.GetPlayers(true)){
   this.playMusic(p);
    }}

HashMap<Player,SongPlayer> songplayers = new HashMap();

public void playMusic(Player player){
Song s = NBSDecoder.parse(new File(plugin.GetPlugin().getDataFolder(), "Song.nbs"));
SongPlayer sp = new RadioSongPlayer(s);
sp.setAutoDestroy(true);
sp.addPlayer(player);
sp.setPlaying(true);
songplayers.put(player, sp);
((CraftWorld)player.getWorld()).getHandle().b();


}

  
  
HashMap<Player,CustomVehicleCar> customentitys = new HashMap();

     public void spawnCustomVehicle(Player p, CustomEntity entity, String name, Location loc, net.minecraft.server.v1_8_R2.World  world) {

    try {
        
CustomVehicleCar en = new CustomVehicleCar(world);

    en.setPosition(loc.getX(), loc.getY(), loc.getZ());
     
    en.setCustomName(name);
    en.setCustomNameVisible(false);
    en.setInvisible(true);
    
    world.addEntity(en);
    customentitys.put(p, en);
    
  } catch (IllegalArgumentException | SecurityException ex) {

    ex.printStackTrace();
    
   }
    //EntityInsentient e = (EntityInsentient) entity.getCustomClass().newInstance();
  }
     
     public void spawnCustomItemDrop(CustomEntity entity, String name, Location loc, net.minecraft.server.v1_8_R2.World  world, String type) {

    try {
CustomItemDrop en = new CustomItemDrop(world);
if(type.equalsIgnoreCase("coin")){
en.coin = true;
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.NOTE_BLOCK)));
}else if(type.equalsIgnoreCase("box")){
    en.box = true;
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.STAINED_GLASS,1,(short)12)));
}

en.getHandle().setHandle(en);

en.manager = this;
en.getHandle().getHandle().setInvisible(true);
en.getHandle().setCustomNameVisible(false);

en.setPosition(loc.getX(), loc.getY(), loc.getZ());
    
    en.setCustomName(name);
    en.setCustomNameVisible(false);
    en.setInvisible(true);
    
    world.addEntity(en);


  } catch (IllegalArgumentException | SecurityException ex) {

    ex.printStackTrace();
    
   }
  
    //EntityInsentient e = (EntityInsentient) entity.getCustomClass().newInstance();
   
     
     }
     
     

// Red Missil: 0
// Green Missil: 1
// Blue Missil: 2
// Fireball: 3
// SnowBall: 4

@EventHandler
public void onDamage(CustomDamageEvent event){
  if(event.GetDamageePlayer() == null){
      return;
  }
    if(event.GetCause() == DamageCause.CONTACT || event.GetCause() == DamageCause.ENTITY_ATTACK){
      event.GetDamageePlayer().playEffect(EntityEffect.HURT);
  }
    event.SetCancelled("NoDMG");
}
     
        public void spawnCustomMissile(CustomEntity entity, Player owner, Location loc, net.minecraft.server.v1_8_R2.World  world, int type) {

    try {
CustomMissile en = new CustomMissile(world);
en.directioned = true;
en.manager = this;
en.owner = owner;
en.missiltype = type;
if(type == 0){
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.RED_ROSE,1,(short)1)));
}else if(type == 1){
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.DEAD_BUSH,1)));
}else if(type == 2){
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.RED_ROSE,1)));
}else if(type == 3){
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.RED_ROSE,1,(short)2)));
}else if(type == 4){
en.setEquipment(4,CraftItemStack.asNMSCopy(new ItemStack(Material.SNOW_BALL,1)));

}
en.yaw = owner.getLocation().getYaw();
en.pitch = owner.getLocation().getPitch();
en.lastYaw = en.yaw;
en.lastPitch = en.pitch;

en.getHandle().setHandle(en);

en.getHandle().getHandle().setInvisible(true);
en.getHandle().setCustomNameVisible(false);

en.setPosition(loc.getX(), loc.getY(), loc.getZ());
    
    en.setCustomNameVisible(false);
    en.setInvisible(true);
    
    world.addEntity(en);


  } catch (IllegalArgumentException | SecurityException ex) {

    ex.printStackTrace();
    
   }
  
    //EntityInsentient e = (EntityInsentient) entity.getCustomClass().newInstance();
   
     
     }
     
     HashMap<Player,Integer> laptime = new HashMap();
     
  @EventHandler
  public void onUpda(UpdateEvent event){
      if(event.getType() != UpdateType.SEC){
          return;
      }
      if(!this.started){
          return;
      }
      for(Player p : this.GetPlayers(true)){
      if(!laptime.containsKey(p)){
          laptime.put(p, 0);
      }
      laptime.put(p, laptime.get(p)+1);
      
      }
  }
     
     
  public String getLapTime(Player p){
      if(!laptime.containsKey(p)){
          laptime.put(p, 0);
      }
      int lapt = laptime.get(p);
      int mn = 0;
      int sec = 0;
      while (lapt > 60){
         mn+=1;
         lapt-=60;
      }
      while(lapt > 0){
          lapt -=1;
          sec+=1;
      }
      
      String one = "0"+mn+":";
      
      if(sec < 10){
          one = one+"0"+sec;
      }else{
          one = one+sec;
      }
      
      if(laptime.get(p) < 50){
          one = "&b"+one;
      }
      else if(laptime.get(p) < 70){
          one = "&a"+one;
      }
      else if(laptime.get(p) < 90){
          one = "&6"+one;
      }
      else{
          one = "&c"+one;
      }
      
      this.laptime.put(p, 0);
      
     return one; 
  }
     
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onpick(PlayerPickupItemEvent event){

           event.setCancelled(true);
  
  }

  boolean delay = false;
  int delaysec = 25;
  
  @EventHandler
  public void upd(UpdateEvent event){
      if(event.getType() != UpdateType.SEC){
          return;
      }
      if(this.delay == false){
          return;
      }
      delaysec--;
      if(delaysec == 0){
          this.SetState(GameState.End);
          try {
              data.close();
          } catch (SQLException ex) {
              Logger.getLogger(TurboRacers.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
  }
  
  @EventHandler
  public void update(UpdateEvent event){
      if(event.getType() != UpdateType.SLOW){
          return;
      }
      for(List<Location> locs : this.gamemap.values()){
          for(Location loc : locs){
             
          }
      }
      
  }
  
  public HashMap<Player,Integer> winnedcoins = new HashMap();
  String hcmgame = null;
  boolean coinsgived = false;
  public void sendMoneyToALL(){
      if(hcmgame == null){
          hcmgame = this.Manager.getConfig().getString("TurboRacers.HCPuntosName");
      }
      if(coinsgived == true){
          return;
      }
          coinsgived = true;
      for(Player p : winnedcoins.keySet()){
          if(!winnedcoins.containsKey(p)){
              winnedcoins.put(p, 0);
          }
          
AddGems(p, this.winnedcoins.get(p), "Winned Coins", false);
      }
  }
  
  public void finish(){
  
   Player p1 = null;
   Player p2 = null;
   Player p3 = null;
timer = 0;
   for(Player p : position.keySet()){
       if(!p.isOnline()){
           position.remove(p);
       }
       if(!winnedcoins.containsKey(p)){
           winnedcoins.put(p, 0);
       }
       if(position.get(p) == 1){
       winnedcoins.put(p, winnedcoins.get(p)+25);
           p1 = p;

           this.addValue(p, columnType.FirstPlace, ""+1, true);
       }
       else
       if(position.get(p) == 2){
           p2 = p;


           winnedcoins.put(p, winnedcoins.get(p)+15);
           this.addValue(p, columnType.SecondPlace, ""+1, true);
       }
       else
       if(position.get(p) == 3){
      winnedcoins.put(p, winnedcoins.get(p)+10);
           p3 = p;

      this.addValue(p, columnType.ThirdPlace, ""+1, true);
       }
   }
   
   for(Player p : Bukkit.getOnlinePlayers()){
       this.addValue(p, columnType.Played, ""+1, true);
       if(p.getVehicle() != null){
           p.getVehicle().remove();
       }
       if(p == p1){
           try{
           this.sendRandomPart(p, 1);
           }catch(Exception e){
               
           }
           p.teleport(this.customLoc(this.WorldData.World, "pos1"));
         p.setItemInHand(ItemStackFactory.Instance.CreateStack(Material.GOLD_INGOT, (byte)0, 1, (short)0, String.valueOf(C.cDAqua) + "1st place", new String[] { "", ChatColor.RESET + "Congratulations!", ChatColor.RESET + "you won the game" }));
       }
       else if(p == p2){
           p.teleport(this.customLoc(this.WorldData.World, "pos2"));
           try{
           this.sendRandomPart(p, 2);}catch(Exception e){
               
           }
           p.setItemInHand(ItemStackFactory.Instance.CreateStack(Material.IRON_INGOT, (byte)0, 1, (short)0, String.valueOf(C.cDGreen) + "2nd place", new String[] { "", ChatColor.RESET + "Congratulations!", ChatColor.RESET + "you are 2nd in the game" }));
       }
       else if(p == p3){
           p.teleport(this.customLoc(this.WorldData.World, "pos3"));
           try{
           this.sendRandomPart(p, 3);}catch(Exception e){
               
           }
            p.setItemInHand(ItemStackFactory.Instance.CreateStack(Material.CLAY_BRICK, (byte)0, 1, (short)0, String.valueOf(C.cDRed) + "3rd place", new String[] { "", ChatColor.RESET + "Congratulations!", ChatColor.RESET + "you are 3rd in the game" }));     
       }else{
           p.teleport(this.customLoc(this.WorldData.World, "posother"));
       }
       
       p.sendMessage(Colorizer.Line);
       p.sendMessage("         Winners:         ");
       if(p1 != null){
       p.sendMessage(Colorizer.Color("         &f&l1st: &e"+p1.getName()));
       }
       if(p2 != null){
       p.sendMessage(Colorizer.Color("         &a2nd: &e"+p2.getName()));
       }
       
       if(p3 != null){
       p.sendMessage(Colorizer.Color("         &c3rd: &e"+p3.getName()));
       }
       
       p.sendMessage(Colorizer.Line);
this.sendMoneyToALL();
   }
   

this.delay = true;

  }
  
  int timer = -1;
  
  @EventHandler
  public void timing(UpdateEvent event){
      if(event.getType() == UpdateType.SEC){
          if(timer != -1){
            if(timer == 1 || timer == 0){
                timer = 0;
            }else{
              timer--;
              if(timer == 30){
                  for(Player p : Bukkit.getOnlinePlayers()){
                      this.playModdedSound(p, "announcer.thirtysecondsleft", 1F, 1F);
                  }
              }
              else if(timer <= 10 && timer > 0){
                  for(Player p : Bukkit.getOnlinePlayers()){
                      if(timer == 10){
                      this.playModdedSound(p, "announcer.tensecondsleft", 1F, 1F);    
                      }
                      this.playNote(p, 2, 3, 1, 3);
                      UtilDisplay.sendTitle(p, Colorizer.Color(" "), Colorizer.Color("&c"+timer));
                  }
              }
            }
          }
      }
     
     if(event.getType() != UpdateType.FAST){
         return;
     }
     if(this.started == true && timer == -1){
         timer = 300;
     }
  }
  
  public boolean hasTimed(){
      if(timer == -1){
         return false;
      }if(timer == 0){
          return true;
          
      }
      return false;
  }
  
  HashMap<Player,Boolean> flaps = new HashMap();
  
  @Override
  public void EndCheck(){
      if(this.delay == true){
          return;
      }
      if(this.GetState() != GameState.Live){
          return;
      }
      if(this.GetPlayers(true).size() < 2){
          this.finish();
      }
      if(this.hasTimed()){
          this.finish();
      }
  }
 int post = 100;
  Location lobby = null;
  
  public Location customLoc(World we,String path){
      String[] coord = this.Manager.getConfig().getString(this.WorldData.MapName+".map."+path).split(",");
       int x = Integer.parseInt(coord[0]);
       int y = Integer.parseInt(coord[1]);
       int z = Integer.parseInt(coord[2]);
       return new Location(we,x,y,z);
      
  }
  
  public void afterLap(Player p){
      if(this.hasTimed()){
          return;
      }
      for(Player pe : Bukkit.getOnlinePlayers()){
          pe.sendMessage(Colorizer.Color("&7"+p.getName()+" &fhas finished &6#&e"+position.get(p)));
      }
      if(lobby == null){
       lobby = this.customLoc(WorldData.World, "afterlaps");
      }
      this.completedlaps.put(p, post*100);
      post--;
      this.flaps.put(p, true);
      p.getVehicle().remove();
      p.teleport(lobby);
      
  }
  
 String border1 = null;
 String border2 = null;
 String border3 = null;
 String map = null;
 
HashMap<Player,String> resetpos = new HashMap();
 public void updatePos(Player p, Objective obj, int pos){
     String toupdate = Colorizer.Color(pos+" &7"+p.getName());
     if(toupdate.length() > 16){
         toupdate = toupdate.substring(0, 15);
     }

     resetpos.put(p, toupdate);
     obj.getScore(toupdate).setScore(14-pos);
     
     }
     
 
 String web = null;
 @EventHandler
  @Override
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    if(web == null){
        web = Manager.getConfig().getString("TurboRacers.website");
    }
  Objective obj = this.GetObjectiveSide();
  int next = 16;
  obj.getScore("  ").setScore(next--);
  
  obj.getScore(Colorizer.Color("&b&lPosition")).setScore(next--);
  for(String str : resetpos.values()){
      obj.getScoreboard().resetScores(str);
  }
if(!this.GetPlayers(true).isEmpty()){
    for(Player p : this.GetPlayers(true)){
        if(position.containsKey(p)){
           if(position.get(p) > 0 && position.get(p) < 6){
               this.updatePos(p,obj, position.get(p));
           }
        }
    }
}
next-=6;
obj.getScore("   ").setScore(next--);
obj.getScore(Colorizer.Color("&b&lMap:")).setScore(next--);


if(WorldData != null)
if(WorldData.MapName != null){
obj.getScore(WorldData.MapName).setScore(next--);
obj.getScore("     ").setScore(next--);
obj.getScore(Colorizer.Color(web)).setScore(next--);
}


    
  }

  
  

// Menu:
// - Particle Quality
// - Helmet Selector
// - Racing Suits
// - Kart customizer
// - Junkyard
// - Part roller
// - Part crafter
// - Horn selector
// - Kart skin selector
// - Particle trail selector
// - Music selector

// Stats:
// Gold
// Silver
// Bronze

// Box
// Laps
// Coins - HCPuntos


    String host = "127.0.0.1";
    String port = "3306";
    String db = "stats";
    String user = "root";
    String pwd = "test";
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
    

  
public HashMap<Integer,Integer> skinprice = new HashMap();
public HashMap<Integer,Integer> skinmat = new HashMap();
public HashMap<Integer,Short> skindura = new HashMap();
public HashMap<Integer,String> skinname = new HashMap();

public HashMap<Integer,Integer> helmprice = new HashMap();
public HashMap<Integer,Integer> helmmat = new HashMap();
public HashMap<Integer,Short> helmdura = new HashMap();
public HashMap<Integer,String> helmname = new HashMap();

public HashMap<Integer,Integer> hornprice = new HashMap();
public HashMap<Integer,Integer> hornmat = new HashMap();
public HashMap<Integer,Short> horndura = new HashMap();
public HashMap<Integer,String> hornname = new HashMap();
public HashMap<Integer,String> hornsound = new HashMap();

HashMap<String,HashMap<columnType,String>> cvalues = new HashMap();
HashMap<String,HashMap<columnType,Long>> cvaluesdelay = new HashMap();

public String getValue(Player p, columnType type) throws SQLException{
if(!cvalues.containsKey(p.getName())){
    cvalues.put(p.getName(), new HashMap());
}    
if(!cvaluesdelay.containsKey(p.getName())){
    cvaluesdelay.put(p.getName(), new HashMap());
}
if(cvaluesdelay.get(p.getName()).containsKey(type)){
    if(cvaluesdelay.get(p.getName()).get(type) > System.currentTimeMillis()){
        return cvalues.get(p.getName()).get(type);
    }
}
        

    if(enabled == false){return null;}
        this.checkData();
        if(p == null){return null;}
        if(p.getName() == null){return null;}
ResultSet res = null;
res = getStatement().executeQuery("SELECT * FROM "+table+" WHERE UUID = '"+p.getUniqueId().toString()+"'");
String value = null;

try{res.next(); 
value = res.getString(type.getName());
cvalues.get(p.getName()).put(type, value);
cvaluesdelay.get(p.getName()).put(type, System.currentTimeMillis()+5000);

res.close();} catch(Exception e){
  e.printStackTrace();}

   return value;
    }

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

     for(String str : plugin.getConfig().getStringList("TurboRacers.skins")){

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

     for(String str : plugin.getConfig().getStringList("TurboRacers.helmets")){

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

     for(String str : plugin.getConfig().getStringList("TurboRacers.horns")){

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

HashMap<Player,Boolean> mplayers = new HashMap();

@EventHandler
public void onMyMusic(UpdateEvent event){
    if(event.getType() != UpdateType.SLOW){
        return;
    }
    if(this.IsLive()){
        return;
    }
    for(Player p : Bukkit.getOnlinePlayers()){
        if(!mplayers.containsKey(p)){
            mplayers.put(p, true);
          
            PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(1005,new BlockPosition(p.getLocation().getX(),p.getLocation().getY(),p.getLocation().getZ()),2267,false);
   ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    
}

    @EventHandler
    public void insertStats(PlayerJoinEvent event){
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



  centerManager plugin = this.Manager;

public final void openData(){
            if(enabled == false){
            return;
        }
    try {
    MySQL MySQL = new MySQL(plugin.GetPlugin(), host, port, db, user, pwd);
    
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