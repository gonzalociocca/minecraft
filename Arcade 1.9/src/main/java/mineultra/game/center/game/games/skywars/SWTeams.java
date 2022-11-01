package mineultra.game.center.game.games.skywars;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
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
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SWTeams extends TeamGame
{

  private static long startTime = 0;
  public SWTeams(centerManager manager)
  {
    super(manager, GameType.SWTeams, 
      new Kit[] {
new SWKit(manager,0),new SWKit(manager,1),new SWKit(manager,2),new SWKit(manager,3),new SWKit(manager,4),new SWKit(manager,5),new SWKit(manager,6),new SWKit(manager,7),new SWKit(manager,8)
,new SWKit(manager,9),new SWKit(manager,10),new SWKit(manager,11),new SWKit(manager,12),new SWKit(manager,13),new SWKit(manager,14),new SWKit(manager,15),new SWKit(manager,16),new SWKit(manager,17)
,new SWKit(manager,18),new SWKit(manager,19),new SWKit(manager,20),new SWKit(manager,21),new SWKit(manager,22),new SWKit(manager,23),new SWKit(manager,24),new SWKit(manager,25),new SWKit(manager,26)
,new SWKit(manager,27),new SWKit(manager,28),new SWKit(manager,29),new SWKit(manager,30),new SWKit(manager,31),new SWKit(manager,32),new SWKit(manager,33),new SWKit(manager,34),new SWKit(manager,35)
,new SWKit(manager,36),new SWKit(manager,37),new SWKit(manager,38),new SWKit(manager,39),new SWKit(manager,40),new SWKit(manager,41),new SWKit(manager,42),new SWKit(manager,43),new SWKit(manager,44)
,new SWKit(manager,45),new SWKit(manager,46),new SWKit(manager,47),new SWKit(manager,48),new SWKit(manager,49),new SWKit(manager,50),new SWKit(manager,51),new SWKit(manager,52),new SWKit(manager,53)
      }, 
      new String[] {
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " Wins the last Team standing", 
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " play with your team, die is not an option"
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
    this.WorldTimeSet = 10000;
    startTime = System.currentTimeMillis();
      Manager.GetExplosion().SetRegenerate(false);
      this.ches = "chests.normal";
   
  }
 
String ches = "chests.normal";
  
@Override
  public void ParseData()
  {

      Manager.GetExplosion().SetRegenerate(false);
  
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
    this.fillAllChests();
    
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
             db.getPlayerData(p).addWinned(1);
this.AddGems(p, 25.0D, "Team Ganador", true);
         }
else if(p.isOnline()){
        db.getPlayerData(p).addLoss(1);
this.AddGems(p, 10.0D, "Participacion", true);
    
  
         }
   }
  }

  SetCustomWinLine(C.cDGreen+"The team "+WinnerTeam.GetColor()+""+ChatColor.BOLD+WinnerTeam.GetName()+ C.cDGreen + " wins the game!");
      AnnounceEnd(WinnerTeam);   
    }catch(Exception e){
        
    }
    this.Manager.GetGame().chest1 = 0;
    this.Manager.GetGame().chest2 = 0;
    this.Manager.GetGame().chest3 = 0;
    
      SetState(Game.GameState.End);
 } else if(teamcount.isEmpty()){
  for(GameTeam gt : this.GetTeamList()){
 
     for(Player p : gt.GetPlayers(false)){

    if(p.isOnline()){
        db.getPlayerData(p).addLoss(1);
this.AddGems(p, 10.0D, "Participation", true);         
    
  
         }
   }
  }

 try{
     SetCustomWinLine(C.cDGreen+"The team "+WinnerTeam.GetColor()+""+ChatColor.BOLD+WinnerTeam.GetName()+ C.cDGreen + " wins the game!");
      AnnounceEnd(WinnerTeam);        
 }catch(Exception e){

 }
this.Manager.GetGame().chest1 = 0;
    this.Manager.GetGame().chest2 = 0;
    this.Manager.GetGame().chest3 = 0;
    
       SetState(Game.GameState.End);
 }
}

 String border1 = null;
 String border2 = null;
 String border3 = null;
 String map = null;
 
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
 GetObjectiveSide().getScore(this.Color(border1)).setScore(-1 );       
}

   
   int next = 35;
    for(GameTeam t : this.GetTeamList()){
try{
GetObjectiveSide().getScore(this.Color(t.GetColor()+t.GetName()+"&c: ")).setScore(t.GetPlayers(true).size());
}catch(IllegalArgumentException | IllegalStateException e){
  System.out.println("is bigger than expected "+t.GetName());
}
}
   if(border2 == null){
  border2 = this.Manager.getConfig().getString("scoreboard.border2");
   }
   
if(border2 != null){
 GetObjectiveSide().getScore(this.Color(border2)).setScore(next );       
}

next = next+1;
if(WorldData.MapName != null){
    if(!WorldData.MapName.equalsIgnoreCase("null")){
GetObjectiveSide().getScore(this.Color("&e"+WorldData.MapName)).setScore(next );
        
    }
}

next = next+1;

   if(map == null){
  map = this.Manager.getConfig().getString("scoreboard.map");
   }
   
if(map != null){
 GetObjectiveSide().getScore(this.Color(map)).setScore(next );       
}

next = next+1;

   if(border3 == null){
  border3 = this.Manager.getConfig().getString("scoreboard.border3");
   }
   
if(border3 != null){
 GetObjectiveSide().getScore(this.Color(border3)).setScore(next );       
}


  for(GameTeam team : this.GetTeamList()){
    for(Player player : team.GetPlayers(false)){
    this.SetPlayerScoreboardTeam(player, team.GetName());
  } 
  }
  }
  

 
}

  
  
  
  

  

 


