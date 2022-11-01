package mineultra.game.center.game.games.smash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import me.libraryaddict.disguise.DisguiseAPI;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilAction;
import mineultra.core.common.util.UtilBlock;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilServer;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.game.TeamGame;
import mineultra.game.center.game.games.smash.*;
import mineultra.game.center.kit.Kit;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

public class SmashGame extends TeamGame
{

  private static long startTime = 0;
  
  public SmashGame(centerManager manager)
  {
    super(manager, GameType.Smash, 
      new Kit[] { 
      new SmashKit(manager),
                    new BomberSmashKit(manager),
                    new WitherKit(manager),
                    new BatmanKit(manager),
                    new TryHardKit(manager)
              
      }, 
      new String[] {
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " Wins the last Team standing", 
      F.elem(new StringBuilder(String.valueOf(C.cAqua)).append("Teams").toString()) + C.cWhite + " play with your team, die is not an option"
 });

    this._help =  this.Manager.getConfig().getStringList("tips").toArray(new String[0]);

    HungerSet  = 20;
    DeathOut = false;
    this.BlockBreak = false;
    this.BlockPlace = false;
    this.DeathDropItems = false;
    this.ItemDrop = false;
    this.ItemPickup = false;
    this.DeathOut = true;
        this.WorldTimeSet = 10000;
    startTime = System.currentTimeMillis();
      Manager.GetExplosion().SetRegenerate(true);
  }
  /*
  @Override
  public void ParseData()
  {
      Manager.GetExplosion().SetRegenerate(true);
  }
  
 @EventHandler
 public void no(GameStateChangeEvent event){
     if(event.GetState() != GameState.Live){
         return;
     }
     for(Player p : this.GetPlayers(true)){
         p.removePotionEffect(PotionEffectType.INVISIBILITY);
     }
     startTime = System.currentTimeMillis();
 }
  
 HashMap<Player,Double> smash2 = new HashMap();
 HashMap<Player, Integer> strikes = new HashMap();
 HashMap<Player,Long> smashtime = new HashMap();
@EventHandler
public void smash(CustomDamageEvent event){
    if(event.GetDamageePlayer() != null){
     if((this.GetKit(event.GetDamageePlayer()) instanceof TryHardKit)){
    if(!DisguiseAPI.isDisguised(event.GetDamageePlayer())){
        event.GetDamageePlayer().setHealth(20);
    }
     }else{
     event.GetDamageePlayer().setHealth(20);         
     }

    }
    if(event.GetDamagerEntity(true) == null && event.GetDamagerPlayer(true) == null){
 return;       
    }
   if(event.GetDamageePlayer() == null){
       return;
   }
   if(!this.IsLive()){
       return;
   }
   if(event.GetDamage() > 10){
       event.AddMod("Smash", "Smash", -5, false);
   }
   
   if(smash2.containsKey(event.GetDamageePlayer())){
       double smashh = smash2.get(event.GetDamageePlayer());
       event.AddKnockback("Smash", smashh);
       smash2.put(event.GetDamageePlayer(), smashh+0.050D);
 if(!smashtime.isEmpty()){
     if(smashtime.containsKey(event.GetDamageePlayer())){
       if(smashtime.get(event.GetDamageePlayer()) > System.currentTimeMillis()){
           return;
       }}}
       event.GetDamageePlayer().sendMessage(Color("&cSMASH &a- "+(int)smashh*10+"%"));
smashtime.put(event.GetDamageePlayer(), System.currentTimeMillis()+10000);
   
   }else{
    smash2.put(event.GetDamageePlayer(), 0.050D);
   }
}
@EventHandler
public void upd(UpdateEvent event){
    if(event.getType() != UpdateType.SEC){
        return;
    }
    for(Player p : this.smash2.keySet())
    this.Manager.GetGame().smash.put(p, this.smash2.get(p));
}





  public static String Color(String s)
  {
    if (s == null) {
      return Color("&4&lError");
    }
    return s.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
     
 
    @EventHandler
    public void FlightHop(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (!this.IsLive()) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);
{
            UtilAction.velocity((Entity)player, player.getLocation().getDirection(), 1, true, 1, 0.0, 10, true);
        }
        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
    }
    
    @EventHandler
    public void FlightUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (player.getGameMode() != GameMode.CREATIVE) {
                if (this.IsLive()) {
                    if (UtilEnt.isGrounded((Entity)player) || UtilBlock.solid(player.getLocation().getBlock().getRelative(BlockFace.DOWN))) {
                        player.setAllowFlight(true);
                    }
                }
            }
        }
    }
  
Random r = new Random();


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


  @Override
  public void EndCheck()
  {
    if (!IsLive()){
        return;
    }
HashSet<GameTeam> teamcount = new HashSet();
for(GameTeam t : this.GetTeamList()){
   if(!t.GetPlayers(true).isEmpty()){
       teamcount.add(t);
   } 
}

if(teamcount.size() <= 1 || teamcount.isEmpty()){
    if(teamcount.size() == 1){
    this.WinnerTeam = teamcount.iterator().next();
 for(Player p : this.GetPlayers(false)){
    this.AddGems(p, 10, "Participation", false);
}
for(Player p : WinnerTeam.GetPlayers(false)){
    this.AddGems(p, 25, "Winner Team", false);
}

   this.AnnounceEnd(WinnerTeam);
   SetCustomWinLine(C.cDGreen+"The team "+WinnerTeam.GetName()+ C.cDGreen + " has smashed all!");
    }
    this.SetState(GameState.End);
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

for(Player player  : this.GetPlayers(true)){
if(!smash2.isEmpty()){
    if(smash2.containsKey(player)){
    this.GetObjectiveBelow().setDisplaySlot(DisplaySlot.BELOW_NAME);

    this.GetObjectiveBelow().setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+"%Smash");
 String group = player.getName();
 if(group.length() > 16){
     group = group.substring(0,15);
 }
Score score = this.GetObjectiveBelow().getScore(group);
    int sm = (int)(smash2.get(player)/1);
        score.setScore(sm*10);
    
}}}

  for(GameTeam team : this.GetTeamList()){
    for(Player player : team.GetPlayers(false)){
    this.SetPlayerScoreboardTeam(player, team.GetName());
  } 
  }
  }









  
  
@EventHandler
public void smash(UpdateEvent event){
    if(event.getType() != UpdateType.FAST){
        return;
    }

    for(Player p : this.GetPlayers(true)){
    if(this.GetKit(p) instanceof TryHardKit){
if(!DisguiseAPI.isDisguised(p)){
    p.setHealth(20);
}
    }else{
    p.setHealth(20);    
    }
    
    }
}
  @EventHandler
  public void nofall(CustomDamageEvent event){
      if(event.GetCause() == null){
          return;
      }
      if(event.GetCause() == DamageCause.FALL){
          event.SetCancelled("Fall");
      }
  }
  */
 
}

  
  
  
  

  

 


