package mineultra.game.center.game.games.skywars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.events.PlayerDeathOutEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.kit.Kit;
import mineultra.minecraft.game.core.combat.DeathMessageType;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class SkyWars extends TeamGame
{

  private static long startTime = 0;
  public SkyWars(centerManager manager)
  {
    super(manager, GameType.SkyWars, 
      new Kit[] {
new SWKit(manager,0),new SWKit(manager,1),new SWKit(manager,2),new SWKit(manager,3),new SWKit(manager,4),new SWKit(manager,5),new SWKit(manager,6),new SWKit(manager,7),new SWKit(manager,8)
,new SWKit(manager,9),new SWKit(manager,10),new SWKit(manager,11),new SWKit(manager,12),new SWKit(manager,13),new SWKit(manager,14),new SWKit(manager,15),new SWKit(manager,16),new SWKit(manager,17)
,new SWKit(manager,18),new SWKit(manager,19),new SWKit(manager,20),new SWKit(manager,21),new SWKit(manager,22),new SWKit(manager,23),new SWKit(manager,24),new SWKit(manager,25),new SWKit(manager,26)
,new SWKit(manager,27),new SWKit(manager,28),new SWKit(manager,29),new SWKit(manager,30),new SWKit(manager,31),new SWKit(manager,32),new SWKit(manager,33),new SWKit(manager,34),new SWKit(manager,35)
,new SWKit(manager,36),new SWKit(manager,37),new SWKit(manager,38),new SWKit(manager,39),new SWKit(manager,40),new SWKit(manager,41),new SWKit(manager,42),new SWKit(manager,43),new SWKit(manager,44)
,new SWKit(manager,45),new SWKit(manager,46),new SWKit(manager,47),new SWKit(manager,48),new SWKit(manager,49),new SWKit(manager,50),new SWKit(manager,51),new SWKit(manager,52),new SWKit(manager,53)
      }, 
      new String[] {
 });

    this._help =  this.Manager.getConfig().getStringList("tips").toArray(new String[0]);

    HungerSet = 20;
    DeathOut = false;
    this.BlockBreak = true;
    this.BlockPlace = true;
    this.DeathDropItems = true;
    this.ItemDrop = true;
    this.ItemPickup = true;
    this.DeathOut = true;
    this.QuitOut = true;
    this.CreatureAllowOverride=false;
    this.CreatureAllow=false;

    startTime = System.currentTimeMillis();
      Manager.GetExplosion().SetRegenerate(false);
      this.ches = "chests.normal";

  }
 
String ches = "chests.normal";
 
@Override
  public void ParseData()
  {
      this.players.clear();
for(GameTeam t : this.GetTeamList()){
    t.SetVisible(false);
   t.SetColor(ChatColor.GREEN);
}
      Manager.GetExplosion().SetRegenerate(false);
  
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

        String s2 = Colorizer.Color(s1);
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
        im.setDisplayName(Colorizer.Color(nam));
        i.setItemMeta(im);
      }
    }
    return i;
  }
  
  @EventHandler
  public void Rayo(CombatDeathEvent event){
      event.SetBroadcastType(DeathMessageType.Absolute);
 LivingEntity et = event.GetEvent().getEntity();
 et.getWorld().strikeLightningEffect(et.getLocation());
  }
  public static String getFriendlyItemName(Material m)
  {
    String str = m.toString();
    str = str.replace('_', ' ');
    str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    
    return str;
  }
    public void fillAllChests(){
    for(Chunk chunk : WorldData.World.getLoadedChunks()){
    for(BlockState entities : chunk.getTileEntities()){
    if(entities instanceof Chest){
    Inventory inv = ((Chest) entities).getInventory();
    fillChests(inv);
    }
    }
    }
    }
Random r = new Random();
public void fillChests(Inventory inv){
inv.clear();


for(int a = 0;a< 25;a++){
inv.setItem(r.nextInt(inv.getSize() -1), nextitem());    
}
}

public ItemStack nextitem(){
return read(this.Manager.getConfig().getStringList(ches).get(r.nextInt(this.Manager.getConfig().getStringList(ches).size() -1)));
}


  @EventHandler
  public void kitattack(CustomDamageEvent event){
      if(event.GetDamageePlayer() == null){
          return;
      }
      if(event.GetDamagerEntity(false) == null){
          return;
      }
      if(event.GetDamagerEntity(false).getType() == EntityType.PIG_ZOMBIE){
          event.SetCancelled("Bad GUY too");
      }
  }
  

  
  @EventHandler
  public void Fill(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Prepare) {
      return;
    }
    startTime = System.currentTimeMillis();
    if(this.Manager.GetGame().chest2 > this.Manager.GetGame().chest1){
        this.ches = "chests.medium";
    }
    if(this.Manager.GetGame().chest3 > this.Manager.GetGame().chest2){
        this.ches = "chests.opchest";
    }

int time1t = this.Manager.GetGame().time1;
int time2t = this.Manager.GetGame().time2;
int time3t = this.Manager.GetGame().time3;
int winner = Math.max(time1t, Math.max(time2t, time3t));
if(winner == time1t){
    this.WorldTimeSet = 1000;
}else if(winner == time2t){
    this.WorldTimeSet = 11500;
}else if(winner == time3t){
    this.WorldTimeSet = 13500;
}
    this.fillAllChests();    
  }
  
  

  @Override
  public void EndCheck()
  {
    if (!IsLive()){
        return;
    }
    
HashSet<GameTeam> teamcount = new HashSet();

for(GameTeam t : this.GetTeamList()){
if(t.GetPlayers(true).size() > 0){
    teamcount.add(t);
}
}




if(teamcount.size() == 1){
    try{
    this.WinnerTeam = teamcount.iterator().next();
 for(GameTeam gt : this.GetTeamList()){
 
     for(Player p : gt.GetPlayers(false)){
         if(gt == this.WinnerTeam){
this.Manager.GetStats().addValue(p, "Wins", 1);
this.AddGems(p, 25.0D, "Win the Game!", true);
         }
    if(p.isOnline()){
        this.Manager.GetStats().addValue(p, "Played", 1);
this.AddGems(p, 10.0D, "Participation", true);         
    
  
         }
   }
  }
try{
  SetCustomWinLine(C.cDGreen+"The player "+ChatColor.RED+""+ChatColor.BOLD+WinnerTeam.GetPlayers(false).get(0).getName()+ C.cDGreen + " wins the game!");  
}catch(Exception e){
    
}
  
  AnnounceEnd(WinnerTeam);   
    }catch(Exception e){
        
    }
    this.Manager.GetGame().chest1 = 0;
    this.Manager.GetGame().chest2 = 0;
    this.Manager.GetGame().chest3 = 0;
    this.Manager.GetGame().time1 = 0;
    this.Manager.GetGame().time2 = 0;
    this.Manager.GetGame().time3 = 0;
           for(Player p : Bukkit.getOnlinePlayers()){
         p.setAllowFlight(true);
               p.setFlying(true);
         
         p.setVelocity(new Vector(0,1.5,0));
       }  
   
    SetState(Game.GameState.End);
      
 } else if(teamcount.isEmpty()){
  for(GameTeam gt : this.GetTeamList()){
 
     for(Player p : gt.GetPlayers(false)){

    if(p.isOnline()){
this.AddGems(p, 10.0D, "Participation", true);         
        this.Manager.GetStats().addValue(p, "Played", 1);
  
         }
   }
  }

 try{
    
 }catch(Exception e){

 }
this.Manager.GetGame().chest1 = 0;
    this.Manager.GetGame().chest2 = 0;
    this.Manager.GetGame().chest3 = 0;
        this.Manager.GetGame().time1 = 0;
    this.Manager.GetGame().time2 = 0;
    this.Manager.GetGame().time3 = 0;
    
       SetState(Game.GameState.End);
       for(Player p : Bukkit.getOnlinePlayers()){
         p.setFlying(true);
         p.setVelocity(new Vector(0,1.5,0));
       }
 }
}

 String border1 = null;
 String border2 = null;
 String border3 = null;
 String map = null;
HashMap<Player,Boolean> players = new HashMap();
 
 @EventHandler
 public void onChange(GameStateChangeEvent event){
     if(event.GetState() != GameState.Prepare){
         return;
     }
     for(Player p : this.GetPlayers(true)){
players.put(p, true);
     }
 }
 
 @EventHandler
 public void onCombat(PlayerDeathOutEvent event){
     Player p = (Player)event.GetPlayer();
    if(players.containsKey(p)){
        players.put(p, false);
    }
 }
 
 @EventHandler
 public void onQuit(PlayerQuitEvent event){
    if(players.containsKey(event.getPlayer())){
        players.put(event.getPlayer(), false);
    }
 }
 
 
 
 @EventHandler
 public void tnt(BlockPlaceEvent event){
     if(Manager.GetGame() == null){
         return;
     }
     if(Manager.GetGame().GetType() != GameType.SkyWars){
         return;
     }
    if(event.getBlock().getType() != Material.TNT){
        return;
    }
    event.getBlockPlaced().setType(Material.AIR);
    event.getBlock().getLocation().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.PRIMED_TNT);
    
 }
 
 
 @EventHandler
 public void onSnowballICE(ProjectileHitEvent event){
     if(event.getEntity().getType() == EntityType.SNOWBALL){
         Location loc = event.getEntity().getLocation();
         loc.getBlock().setType(Material.WATER);
         loc.clone().add(0, 0, 1).getBlock().setType(Material.ICE);
         loc.clone().add(1, 0, 0).getBlock().setType(Material.ICE);
         loc.clone().subtract(0, 0, 1).getBlock().setType(Material.ICE);
         loc.clone().subtract(1, 0, 0).getBlock().setType(Material.ICE);
     }
 }
 
 HashMap<Player,Location> prevlocs = new HashMap();
 HashMap<Player,Long> nextlocs = new HashMap();
 
 @EventHandler
 public void updateEye(UpdateEvent event){
     if(event.getType() != UpdateType.SEC){
         return;
     }
     if(!this.IsLive()){
         return;
     }
     for(Player p : Bukkit.getOnlinePlayers()){
         if(!nextlocs.containsKey(p)){
             nextlocs.put(p, System.currentTimeMillis()+7500);
             prevlocs.put(p, p.getLocation());
         }
         if(nextlocs.get(p) < System.currentTimeMillis()){
             nextlocs.put(p, System.currentTimeMillis()+7500);
             prevlocs.put(p, p.getLocation());
         }
     }
     
 }
// 
 @EventHandler
 public void onEyeTeleport(PlayerInteractEvent event){
    if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() ==  Action.PHYSICAL){
        return;
    }
     if(event.getPlayer().getItemInHand()==null){
         return;
     }
     if(event.getPlayer().getItemInHand().getType() != Material.EYE_OF_ENDER){
         return;
     }
     if(!this.IsLive()){
         return;
     }

     Player p = event.getPlayer();
     event.setCancelled(true);
         if(!nextlocs.containsKey(p)){
             p.sendMessage(Colorizer.Color("&eTry again in few seconds."));
return;
         }
         Location loc = prevlocs.get(p);
        ((CraftPlayer)p).getHandle().enderTeleportTo(loc.getX(), loc.getY(), loc.getZ());
        p.sendMessage(Colorizer.Color("&aTeleported to &eX: "+loc.getX()+" / Y: "+loc.getY()+" / Z: "+loc.getZ()));
     
     if(p.getItemInHand().getAmount() <= 1){
     p.setItemInHand(new ItemStack(Material.AIR)); 
     }
     else{
     p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
     }
     }
 
 @EventHandler
  @Override
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
   if(border1 == null){
  border1 = this.Manager.getConfig().getString("scoreboard.border1");
   }
   
if(border1 != null){
 GetObjectiveSide().getScore(Colorizer.Color(border1)).setScore(-1 );       
}
this.GetObjectiveSide().getScore(Colorizer.Color("&fwww.mineultra.net")).setScore(-2);
   
   int next = 35;
if(!players.isEmpty()){
    

for(Player pl : this.players.keySet()){
    String one = Colorizer.Color(ChatColor.GREEN+pl.getName());
    if(one.length() > 15){
        one = one.substring(0,15);
    }
    String two = Colorizer.Color(ChatColor.RED+pl.getName());
    if(two.length() > 15){
        two = two.substring(0,15);
    }
   
    if(this.players.get(pl) == true){
GetObjectiveSide().getScore(one).setScore(0);          
pl.setPlayerListName(one);
    }else{

GetScoreboard().resetScores(one);
GetObjectiveSide().getScore(two).setScore(0);          
        pl.setPlayerListName(two);
    }
    
  
}}


   if(border2 == null){
  border2 = this.Manager.getConfig().getString("scoreboard.border2");
   }
   
if(border2 != null){
 GetObjectiveSide().getScore(Colorizer.Color(border2)).setScore(next );       
}

next = next+1;
if(WorldData.MapName != null){
    if(!WorldData.MapName.equalsIgnoreCase("null")){
GetObjectiveSide().getScore(Colorizer.Color("&e"+WorldData.MapName)).setScore(next );
    }
}

next = next+1;

   if(map == null){
  map = this.Manager.getConfig().getString("scoreboard.map");
   }
   
if(map != null){
 GetObjectiveSide().getScore(Colorizer.Color(map)).setScore(next );       
}

next = next+1;

   if(border3 == null){
  border3 = this.Manager.getConfig().getString("scoreboard.border3");
   }
   
if(border3 != null){
 GetObjectiveSide().getScore(Colorizer.Color(border3)).setScore(next );       
}


  for(GameTeam team : this.GetTeamList()){
    for(Player player : team.GetPlayers(false)){
    this.SetPlayerScoreboardTeam(player, team.GetName());
  } 
  }
  }
  
  int actiontimer = 30;
  int actiontype = 0;
  
@EventHandler
public void sendActionBar(UpdateEvent event){
    if(event.getType()==UpdateType.MIN_01){
    if(this.IsLive()){
    for(Player pe : Bukkit.getOnlinePlayers()){
Long alertTime = startTime + (1000L*1000L);
Long finishtime = startTime+(1080L*1000L);
if(finishtime < System.currentTimeMillis()){
  for(GameTeam gt : this.GetTeamList()){
 
     for(Player p : gt.GetPlayers(false)){

    if(p.isOnline()){
p.sendMessage(Colorizer.Color("&eThe game was too long, because of AFKs, sry."));
this.AddGems(p, 10.0D, "Participation", true);         
        this.Manager.GetStats().addValue(p, "Played", 1);
  
         }
   }
  }

 try{
    
 }catch(Exception e){

 }
this.Manager.GetGame().chest1 = 0;
    this.Manager.GetGame().chest2 = 0;
    this.Manager.GetGame().chest3 = 0;
        this.Manager.GetGame().time1 = 0;
    this.Manager.GetGame().time2 = 0;
    this.Manager.GetGame().time3 = 0;
    
       SetState(Game.GameState.End);
       for(Player p : Bukkit.getOnlinePlayers()){
         p.setFlying(true);
         p.setVelocity(new Vector(0,1.5,0));
       }
}
        if(alertTime< System.currentTimeMillis()){
         pe.sendMessage(Colorizer.Color("&aGame will finish in "+(finishtime-System.currentTimeMillis())/1000+"seconds&e, Reason: AFKs"));
     }
    }
    
    }}
    
    if(event.getType() != UpdateType.SEC){
        return;
    }

    actiontimer--;
    if(actiontimer <= 0){
        actiontype++;
        actiontimer = 30;
    }
    if(actiontype > 2){
        actiontype = 0;
    }
            
    for(Player player : Bukkit.getOnlinePlayers()){
        if(actiontype == 0){
        UtilDisplay.sendActionBar(player, Colorizer.Color("&aHelp us upgrade the Server, buy a VIP in &ewww.mineultra.com"));
        }
        else if(actiontype == 1){
        UtilDisplay.sendActionBar(player, Colorizer.Color("&aRegister at our website: &ewww.mineranked.net"));
        }else  if(actiontype == 2){
        UtilDisplay.sendActionBar(player, Colorizer.Color("&aSee your ranking position in: &ewww.mineranked.net/ranking"));    
        }
    }
}

}

  
  
  
  

  

 


