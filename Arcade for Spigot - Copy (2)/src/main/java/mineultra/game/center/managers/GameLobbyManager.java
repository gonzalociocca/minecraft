package mineultra.game.center.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mineultra.core.common.CachedPerm;
import mineultra.core.common.Rank;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import static mineultra.core.common.util.Colorizer.Color;
import mineultra.core.common.util.MapUtil;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilEnt;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilReflection;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilWorld;

import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.GameType;
import mineultra.minecraft.game.core.damage.CustomDamageEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.Game.GameState;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.DataWatcher.WatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GameLobbyManager
  implements Listener
{
  public centerManager Manager = null;
  private final Location _kitDisplay;
  private final Location _teamDisplay;
  private final Location spawn;
  private final HashMap<Entity, LobbyEnt> _kits = new HashMap();
  private final HashMap<Block, Material> _kitBlocks = new HashMap();

  private final HashMap<Entity, LobbyEnt> _teams = new HashMap();
  private final HashMap<Block, Material> _teamBlocks = new HashMap();
  private int _advertiseStage = 0;

  private final HashMap<Player, Scoreboard> _scoreboardMap = new HashMap();
  private final HashMap<Player, Integer> _gemMap = new HashMap();
  private final HashMap<Player, String> _kitMap = new HashMap();

  private int _oldPlayerCount = 0;

  public GameLobbyManager(centerManager manager)
  {
    Manager = manager;

    World world = UtilWorld.getWorld("world");
FileConfiguration conf = this.Manager.getConfig();
    spawn = new Location(world, conf.getInt("lobby.spawnX"),conf.getInt("lobby.spawnY"),conf.getInt("lobby.spawnZ"));
    
    _kitDisplay = new Location(world, conf.getInt("kit.spawnX"),conf.getInt("kit.spawnY"),conf.getInt("kit.spawnZ"));
    _teamDisplay = new Location(world,conf.getInt("team.spawnX"),conf.getInt("team.spawnY"),conf.getInt("team.spawnZ"));

    Manager.GetPluginManager().registerEvents(this, Manager.GetPlugin());
  }

  private boolean HasScoreboard(Player player)
  {
    return _scoreboardMap.containsKey(player);
  }

  public void CreateScoreboards()
  {
    for (Player player : UtilServer.getPlayers())
      CreateScoreboard(player);
  }

  private void CreateScoreboard(Player player)
  {
    _scoreboardMap.put(player, Bukkit.getScoreboardManager().getNewScoreboard());

    Scoreboard scoreboard = (Scoreboard)_scoreboardMap.get(player);
    Objective objective = scoreboard.registerNewObjective("???lLobby", "dummy");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

 

    for (Player otherPlayer : UtilServer.getPlayers())
    {
      AddPlayerToScoreboards(otherPlayer, null);
    }
  }

  public Collection<Scoreboard> GetScoreboards()
  {
    return _scoreboardMap.values();
  }
  public Location GetSpawn()
  {
    return spawn;
  }

  @EventHandler(priority=EventPriority.MONITOR)
  public void TeamGeneration(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Recruit) {
      return;
    }
    CreateTeams(event.GetGame());
  }

  public void CreateTeams(Game game)
  {

    for (Entity ent : _teams.keySet())
      ent.remove();
    _teams.clear();

    for (Block block : _teamBlocks.keySet())
      block.setType((Material)_teamBlocks.get(block));
    _teamBlocks.clear();


    if ((game.GetKits().length > 1) )
    {
      ArrayList teams = new ArrayList();

      for (GameTeam team : game.GetTeamList()) {
        if (team.GetVisible()) {
          teams.add(team);
        }
      }
      double space = 6.0D;
      double offset = (teams.size() - 1) * space / 2.0D;

      for (int i = 0; i < teams.size(); i++)
      {
        Location entLoc = _teamDisplay.clone().subtract( i * space - offset, 0.0D,0.0D);

        SetKitTeamBlocks(entLoc.clone(), 35, ((GameTeam)teams.get(i)).GetColorData(), _teamBlocks);

        entLoc.add(0.0D, 1.5D, 0.0D);

        entLoc.getChunk().load();

        Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
        ent.setRemoveWhenFarAway(false);
        ent.setCustomNameVisible(true);

        ent.setColor(DyeColor.getByWoolData(((GameTeam)teams.get(i)).GetColorData()));

        UtilEnt.Vegetate(ent);

        ((GameTeam)teams.get(i)).SetTeamEntity(ent);

        _teams.put(ent, new LobbyEnt(ent, entLoc, (GameTeam)teams.get(i)));
      }

    }
    else
    {

      ArrayList teamsA = new ArrayList();
      ArrayList teamsB = new ArrayList();

      for (int i = 0; i < game.GetTeamList().size(); i++)
      {
        if (i < game.GetTeamList().size() / 2)
          teamsA.add((GameTeam)game.GetTeamList().get(i));
        else {
          teamsB.add((GameTeam)game.GetTeamList().get(i));
        }

      }

      double space = 6.0D;
      double offset = (teamsA.size() - 1) * space / 2.0D;

      for (int i = 0; i < teamsA.size(); i++)
      {
        Location entLoc = _teamDisplay.clone().subtract(i * space - offset, 0.0D, 0.0D);

        SetKitTeamBlocks(entLoc.clone(), 35, ((GameTeam)teamsA.get(i)).GetColorData(), _teamBlocks);

        entLoc.add(0.0D, 1.5D, 0.0D);

        entLoc.getChunk().load();

        Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
        ent.setRemoveWhenFarAway(false);
        ent.setCustomNameVisible(true);

        ent.setColor(DyeColor.getByWoolData(((GameTeam)teamsA.get(i)).GetColorData()));

        UtilEnt.Vegetate(ent);

        ((GameTeam)teamsA.get(i)).SetTeamEntity(ent);

        _teams.put(ent, new LobbyEnt(ent, entLoc, (GameTeam)teamsA.get(i)));
      }

     space = 6.0D;
      offset = (teamsB.size() - 1) * space / 2.0D;

      for (int i = 0; i < teamsB.size(); i++)
      {
        Location entLoc = _kitDisplay.clone().subtract( i * space - offset, 0.0D,0.0D);

        SetKitTeamBlocks(entLoc.clone(), 35, ((GameTeam)teamsB.get(i)).GetColorData(), _teamBlocks);

        entLoc.add(0.0D, 1.5D, 0.0D);

        entLoc.getChunk().load();

        Sheep ent = (Sheep)Manager.GetCreature().SpawnEntity(entLoc, EntityType.SHEEP);
        ent.setRemoveWhenFarAway(false);
        ent.setCustomNameVisible(true);

        ent.setColor(DyeColor.getByWoolData(((GameTeam)teamsB.get(i)).GetColorData()));

        UtilEnt.Vegetate(ent);

        ((GameTeam)teamsB.get(i)).SetTeamEntity(ent);

        _teams.put(ent, new LobbyEnt(ent, entLoc, (GameTeam)teamsB.get(i)));
      }

    }

    CreateScoreboards();
  }

  public void CreateKits(Game game)
  {
  
    for (Entity ent : _kits.keySet())
      ent.remove();
    _kits.clear();

    for (Block block : _kitBlocks.keySet()){
      block.setType((Material)_kitBlocks.get(block));}
    _kitBlocks.clear();


    ArrayList kits = new ArrayList();
    for (Kit kit : game.GetKits())
    {
      if (kit.GetAvailability() != KitAvailability.Hide) {
        kits.add(kit);
      }
    }



    double space = 4.0D;
    double offset = (kits.size() - 1) * space / 2.0D;

    for (int i = 0; i < kits.size(); i++)
    {
      Kit kit = (Kit)kits.get(i);

      if (kit.GetAvailability() != KitAvailability.Null)
      {
        Location entLoc = _kitDisplay.clone().subtract(i * space - offset, 0.0D, 0.0D);

        byte data = 4;
        if (kit.GetAvailability() == KitAvailability.Green){
            data = 5;
        }
        else if (kit.GetAvailability() == KitAvailability.Blue){
            data = 3;
        }
        SetKitTeamBlocks(entLoc.clone(), 35, data, _kitBlocks);

        entLoc.add(0.0D, 1.5D, 0.0D);

        entLoc.getChunk().load();

        Entity ent = kit.SpawnEntity(entLoc);
UtilEnt.Vegetate(ent);
        if (ent != null)
        {
          _kits.put(ent, new LobbyEnt(ent, entLoc, kit));
        }
      }
    }
  }

  public void SetKitTeamBlocks(Location loc, int id, byte data, HashMap<Block, Material> blockMap) {
    Block block = loc.clone().add(0.5D, 0.0D, 0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

    block = loc.clone().add(-0.5D, 0.0D, 0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

    block = loc.clone().add(0.5D, 0.0D, -0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

    block = loc.clone().add(-0.5D, 0.0D, -0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);

    block = loc.clone().add(0.5D, 1.0D, 0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

    block = loc.clone().add(-0.5D, 1.0D, 0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

    block = loc.clone().add(0.5D, 1.0D, -0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

    block = loc.clone().add(-0.5D, 1.0D, -0.5D).getBlock();
    blockMap.put(block, block.getType());
    MapUtil.QuickChangeBlockAt(block.getLocation(), 44, (byte)5);

    for (int x = -2; x < 2; x++)
    {
      for (int z = -2; z < 2; z++)
      {
        block = loc.clone().add(x + 0.5D, -1.0D, z + 0.5D).getBlock();

        blockMap.put(block, block.getType());
        MapUtil.QuickChangeBlockAt(block.getLocation(), id, data);
      }

    }

    for (int x = -3; x < 3; x++)
    {
      for (int z = -3; z < 3; z++)
      {
        block = loc.clone().add(x + 0.5D, -1.0D, z + 0.5D).getBlock();

        if (!blockMap.containsKey(block))
        {
          blockMap.put(block, block.getType());
          MapUtil.QuickChangeBlockAt(block.getLocation(), 35, (byte)15);
        }
      }
    }
  }

  public void AddKitLocation(Entity ent, Kit kit, Location loc) {
    _kits.put(ent, new LobbyEnt(ent, loc, kit));
  }

  @EventHandler
  public void PlayerQuit(PlayerQuitEvent event)
  {
    _scoreboardMap.remove(event.getPlayer());
    _gemMap.remove(event.getPlayer());
    _kitMap.remove(event.getPlayer());
  }
  Rank myrank = new Rank();
  
  @EventHandler
  public void PlayerJoin(PlayerJoinEvent event)
  {
    _kitMap.put(event.getPlayer(), myrank.prefix(event.getPlayer()));
  }
  @EventHandler(priority=EventPriority.LOWEST)
  public void DamageCancel(CustomDamageEvent event)
  {
    if (_kits.containsKey(event.GetDamageeEntity()))
      event.SetCancelled("Kit Cancel");
  }

  @EventHandler
  public void Update(UpdateEvent event)
  {
    if(event.getType() == UpdateType.SLOWEST){
        spawn.setX(this.Manager.getConfig().getInt("lobby.spawnX"));
        
        spawn.setY(this.Manager.getConfig().getInt("lobby.spawnY"));
                
        spawn.setZ(this.Manager.getConfig().getInt("lobby.spawnZ"));
    }
    if (event.getType() == UpdateType.FAST)
    {
      spawn.getWorld().setTime(6000L);
      spawn.getWorld().setStorm(false);
      spawn.getWorld().setThundering(false);
    }

    if (event.getType() == UpdateType.TICK) {
      UpdateEnts();
    }
    if (event.getType() == UpdateType.SEC) {
      RemoveInvalidEnts();
    }
    ScoreboardDisplay(event);
    ScoreboardSet(event);
  }

  private void RemoveInvalidEnts()
  {
    for (Entity ent : UtilWorld.getWorld("world").getEntities())
    {
      if (((ent instanceof org.bukkit.entity.Creature)) || ((ent instanceof Slime)))
      {
        if (!_kits.containsKey(ent))
        {
          if (!_teams.containsKey(ent))
          {
            if (ent.getPassenger() == null)
            {
              ent.remove(); } 
          }
        }
      }
    }
  }


  public void UpdateEnts()
  {
    for (Entity ent : _kits.keySet()) {
      ent.teleport(((LobbyEnt)_kits.get(ent)).GetLocation());
    }
    for (Entity ent : _teams.keySet())
      ent.teleport(((LobbyEnt)_teams.get(ent)).GetLocation());
  }

  public Kit GetClickedKit(Entity clicked)
  {
    for (LobbyEnt ent : _kits.values()) {
      if (clicked.equals(ent.GetEnt()))
        return ent.GetKit();
    }
    return null;
  }

  public GameTeam GetClickedTeam(Entity clicked)
  {
    for (LobbyEnt ent : _teams.values()) {
      if (clicked.equals(ent.GetEnt()))
        return ent.GetTeam();
    }
    return null;
  }

  

  @EventHandler
  public void Combust(EntityCombustEvent event)
  {
    for (LobbyEnt ent : _kits.values())
      if (event.getEntity().equals(ent.GetEnt()))
      {
        event.setCancelled(true);
        return;
      }
  }

  public void DisplayNext(Game game, HashMap<String, ChatColor> pastTeams)
  {
   
   
 

    CreateKits(game);
    CreateTeams(game);
  }

  

  @EventHandler
  public void ScoreboardDisplay(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    if ((Manager.GetGame() != null) && 
      (Manager.GetGame().GetState() != Game.GameState.Loading) && 
      (Manager.GetGame().GetState() != Game.GameState.Recruit)
     && Manager.GetGame().GetState() != GameState.Prepare)
    {
      for (Player player : UtilServer.getPlayers()) {
        player.setScoreboard(Manager.GetGame().GetScoreboard());
      }
    }
    else
    {
      for (Player player : UtilServer.getPlayers())
      {
        if (!HasScoreboard(player))
        {
          CreateScoreboard(player);
        }
        else
        {

String prxtag = Colorizer.Color(myrank.prefix(player)+" ");
if(this.Manager.GetGame() != null){
        if(this.Manager.GetGame().GetTeam(player) != null){
   prxtag = ""+prxtag+this.Manager.GetGame().GetTeam(player).GetColor();
        }}
        prxtag = Colorizer.Color(prxtag);
        if(prxtag.length() > 16){
            prxtag = prxtag.substring(0,15);
        }
        
          if(_scoreboardMap.get(player).getTeam(prxtag) != null){
            _scoreboardMap.get(player).getTeam(prxtag).addPlayer(player);
        }else{
       _scoreboardMap.get(player).registerNewTeam(prxtag).setPrefix(prxtag);
       _scoreboardMap.get(player).getTeam(prxtag).addPlayer(player);
          }
        
          player.setScoreboard((Scoreboard)_scoreboardMap.get(player));
        }
      }
    }
  }

public Location cube(Location loca, int next){
    if(next == 0){
        return new Location(loca.getWorld(),loca.getX(),loca.getY()-1,loca.getZ());
    }else if(next == 1){
        return new Location(loca.getWorld(),loca.getX(),loca.getY()+2,loca.getZ());
    }else if(next == 2){
        return new Location(loca.getWorld(),loca.getX()+1,loca.getY(),loca.getZ());
    }else if(next == 3){
        return new Location(loca.getWorld(),loca.getX(),loca.getY(),loca.getZ()+1);
    }else if(next == 4){
        return new Location(loca.getWorld(),loca.getX()-1,loca.getY(),loca.getZ());
    }else if(next == 5){
        return new Location(loca.getWorld(),loca.getX(),loca.getY(),loca.getZ()-1);
    }else if(next == 6){
        return new Location(loca.getWorld(),loca.getX()+1,loca.getY()+1,loca.getZ());
    }else if(next == 7){
        return new Location(loca.getWorld(),loca.getX(),loca.getY()+1,loca.getZ()+1);
    }else if(next == 8){
        return new Location(loca.getWorld(),loca.getX()-1,loca.getY()+1,loca.getZ());
    }else if(next == 9){
        return new Location(loca.getWorld(),loca.getX(),loca.getY()+1,loca.getZ()-1);
    }
    return null;
}
public void glassed(Location loca){
    for(int a = 0;a <= 9;a++){
        this.cube(loca, a).getBlock().setType(Material.GLASS);
    }
}
public void unglassed(Location loca){
    for(int a = 0;a <= 9;a++){
        this.cube(loca, a).getBlock().setType(Material.AIR);
    }
}
List<Player> oldplayers = null;

HashMap<Objective,List<String>> scoresets = new HashMap();

  @EventHandler
  public void ScoreboardSet(UpdateEvent event)
  {
      
    if (event.getType() != UpdateType.FAST) {
      return;
    }
    if ((Manager.GetGame() != null) && (!Manager.GetGame().DisplayLobbySide))
    {
      return;
    }
    if(Manager.GetGame()==null){
        return;
    }
    
    if(Manager.GetGame().GetType() == GameType.BuildBattle){
        String sv = Manager.getConfig().getString("BuildBattle.server");
        String web = Manager.getConfig().getString("BuildBattle.website");
        String title = Manager.getConfig().getString("BuildBattle.scoreboard");
       for(Map.Entry entry : _scoreboardMap.entrySet()){
           Objective objective = ((Scoreboard)entry.getValue()).getObjective("???lLobby");
if(!scoresets.containsKey(objective)){
    scoresets.put(objective, new ArrayList());
}
if(!scoresets.get(objective).isEmpty()){
    for(String str : scoresets.get(objective)){
        objective.getScoreboard().resetScores(str);
    }
}
           if(title != null){
              objective.setDisplayName(Colorizer.Color(title));
           }
          objective.getScore("   ").setScore(14);
          
          if(Bukkit.getOnlinePlayers().size() > 0){
           String players = Colorizer.Color("Players: &a"+Bukkit.getOnlinePlayers().size()+"/"+this.Manager.GetServerConfig().MaxPlayers);
          if(players.length() > 16){
              players = players.substring(0, 15);
          }
          scoresets.get(objective).add(players);
          objective.getScore(players).setScore(13);
          }
          
          objective.getScore(" ").setScore(12);
          
          String state = "Esperando...";
          if(Manager.GetGame().GetCountdown() > 0 && Manager.GetGame().GetCountdown() <= 45){
              state = Colorizer.Color("Empieza en &a"+Manager.GetGame().GetCountdown());
          }
          if(state.length() > 16){
              state = state.substring(0, 15);
          }
          scoresets.get(objective).add(state);
          objective.getScore(state).setScore(11);
          objective.getScore("      ").setScore(10);
          
          if(sv != null){
          objective.getScore(Colorizer.Color("Server: &a"+sv)).setScore(9);
          }
           
          objective.getScore("       ").setScore(8);
           
          if(web != null){
          objective.getScore(web).setScore(7);
          }
           
           
           
           
           
           
       }
       return;
    }
    
    else if(Manager.GetGame().GetType() == GameType.TurboRacers){
        String sv = Manager.getConfig().getString("TurboRacers.server");
        String web = Manager.getConfig().getString("TurboRacers.website");
        String title = Manager.getConfig().getString("TurboRacers.scoreboard");
       for(Map.Entry entry : _scoreboardMap.entrySet()){
           Objective objective = ((Scoreboard)entry.getValue()).getObjective("???lLobby");
if(!scoresets.containsKey(objective)){
    scoresets.put(objective, new ArrayList());
}
if(!scoresets.get(objective).isEmpty()){
    for(String str : scoresets.get(objective)){
        objective.getScoreboard().resetScores(str);
    }
}
           if(title != null){
              objective.setDisplayName(Colorizer.Color(title));
           }
          objective.getScore("   ").setScore(15);
          
          if(this.Manager.GetGame().WorldData.MapName != null){
          String map = Colorizer.Color("Mapa: &a"+this.Manager.GetGame().WorldData.MapName);
           if(map.length() > 16){
               map = map.substring(0, 15);
           }
           scoresets.get(objective).add(map);
           objective.getScore(map).setScore(14);
          }
          
          if(Bukkit.getOnlinePlayers().size() > 0){
           String players = Colorizer.Color("Players: &a"+Bukkit.getOnlinePlayers().size()+"/"+this.Manager.GetServerConfig().MaxPlayers);
          if(players.length() > 16){
              players = players.substring(0, 15);
          }
          scoresets.get(objective).add(players);
          objective.getScore(players).setScore(13);
          }
          
          objective.getScore(" ").setScore(12);
          
          String state = "Waiting...";
          if(Manager.GetGame().GetCountdown() > 0 && Manager.GetGame().GetCountdown() <= 45){
              state = Colorizer.Color("Start in &a"+Manager.GetGame().GetCountdown());
          }
          if(state.length() > 16){
              state = state.substring(0, 15);
          }
          scoresets.get(objective).add(state);
          objective.getScore(state).setScore(11);
          objective.getScore("      ").setScore(10);
          
          if(sv != null){
          objective.getScore(Colorizer.Color("Server: &a"+sv)).setScore(9);
          }
           
          objective.getScore("       ").setScore(8);
           
          if(web != null){
          objective.getScore(web).setScore(7);
          }
           
           
           
           
           
           
       }
       return;
    }
    
    for (Map.Entry entry : _scoreboardMap.entrySet())
    {
      Objective objective = ((Scoreboard)entry.getValue()).getObjective("???lLobby");

      if ((Manager.GetGame() != null) && (Manager.GetGame().GetCountdown() >= 0))
      {
        if (Manager.GetGame().GetCountdown() > 0){

objective.setDisplayName(Colorizer.Color(this.Manager.getConfig().getString("GameConfig.starting").replace("%s",""+Manager.GetGame().GetCountdown())));       


if(Manager.GetGame().GetCountdown() > 0 && Manager.GetGame().GetCountdown() <= 10){
          

for(Player pe : UtilServer.getPlayers())
          UtilDisplay.sendTitle(pe, Colorizer.Color(this.Manager.getConfig().getString("GameConfig.starting").replace("%s","")),""+Manager.GetGame().GetCountdown());
     }
     if(Manager.GetGame().GetCountdown() == 1){
                for(Player pe : UtilServer.getPlayers()){
UtilDisplay.sendTitle(pe, Color(this.Manager.getConfig().getString("GameConfig.start"))," ");
        
      }}
        }
        else if (Manager.GetGame().GetCountdown() == 0) {
            for(GameTeam te : this.Manager.GetGame().GetTeamList()){
                this.unglassed(te.GetSpawn());
            }
          objective.setDisplayName(Color(Manager.getConfig().getString("GameConfig.progress")));

        
        
        }
        
        
        
      }
      else {

objective.setDisplayName(Color(Manager.getConfig().getString("GameConfig.waiting")));
          
        
      }

      int line = 14;

      if(this.Manager.GetGame() != null){
{
      objective.getScore(Color(Manager.getConfig().getString("GameConfig.max"))).setScore(line--);
    
          objective.getScore(Manager.GetPlayerFull() + " ").setScore(line--);
      }}
      
  
      
      objective.getScore(" ").setScore(line--);
      objective.getScore(Color(Manager.getConfig().getString("GameConfig.min"))).setScore(line--);
      objective.getScore(Manager.GetPlayerMin() + "  ").setScore(line--);
      objective.getScore("   ").setScore(line--);
      objective.getScore(Color(Manager.getConfig().getString("GameConfig.total"))).setScore(line--);

      ((Scoreboard)entry.getValue()).resetScores(_oldPlayerCount + "   ");
      
          


      objective.getScore(UtilServer.getPlayers().length + "   ").setScore(line--);

      
      
      if (Manager.GetGame() != null)
      {
          if(Manager.GetGame().GetType() != GameType.BuildBattle
              ){
              
          
        ChatColor teamColor = ChatColor.GRAY;
        String kitName = "None";

        if (Manager.GetGame().GetTeam((Player)entry.getKey()) != null)
        {
          teamColor = Manager.GetGame().GetTeam((Player)entry.getKey()).GetColor();
        }

        if (Manager.GetGame().GetKit((Player)entry.getKey()) != null)
        {
          kitName = Manager.GetGame().GetKit((Player)entry.getKey()).GetName();
        }

        if (teamColor == null)
        {
          if (kitName.length() > 16) {
            kitName = kitName.substring(0, 16);
          }
        }
        ((Scoreboard)entry.getValue()).resetScores(C.cGray + C.Bold + "Kit");
        ((Scoreboard)entry.getValue()).resetScores(_kitMap.get((Player)entry.getKey()));

        objective.getScore("    ").setScore(line--);
        objective.getScore(teamColor + C.Bold + "Kit").setScore(line--);
        objective.getScore(kitName).setScore(line--);

        _kitMap.put((Player)entry.getKey(), kitName);
      }
}
      if(Manager.GetGame() != null){
          if(Manager.GetGame().GetType() != GameType.BuildBattle ){
      
      objective.getScore("     ").setScore(line--);
      objective.getScore(Color(Manager.getConfig().getString("GameConfig.coins"))).setScore(line--);

      ((Scoreboard)entry.getValue()).resetScores(_gemMap.get((Player)entry.getKey()) + "     ");

objective.getScore(cPoint((Player)entry.getKey()) + "     ").setScore(line--);

_gemMap.put((Player)entry.getKey(), cPoint((Player)entry.getKey()));
}}
    }

    _oldPlayerCount = UtilServer.getPlayers().length;
  }
  Long last = 0L;
   HashMap<String,Integer> money = new HashMap();
   
  public int cPoint(Player p){
       if(last < System.currentTimeMillis()){
           last = System.currentTimeMillis()+30000;
       money.clear();
       money = new HashMap();
       }
       if(money.containsKey(p.getName())){
           return money.get(p.getName());
       }
       money.put(p.getName(), getPlayerPoints().getAPI().look((p).getUniqueId()));
       return money.get(p.getName());
       
  }
          
  
CachedPerm perm = new CachedPerm();

  private String GetKitCustomName(Player player, Game game, LobbyEnt ent)
  {

    String entityName = ent.GetKit().GetName();

    if ((!player.isOnline()) || (player == null)) {
      return entityName;
    }
    if (myrank.prefix(player) == null)
    {
      System.out.println("client rank is null");
    }

    if (game == null)
    {
      System.out.println("game is null");
    }

    if (Manager == null)
    {
      System.out.println("Manager is null");
    }
 
    if (Manager.GetServerConfig() == null)
    {
      System.out.println("Manager.GetServerConfig() is null");
    }

    if (perm.hasPerm(player,"mineultra.vip") || (ent.GetKit().GetAvailability() == KitAvailability.Free))
    {
      entityName = ent.GetKit().GetAvailability().GetColor() + entityName;
    }
    else
    {
      entityName = ChatColor.RED + C.Bold + entityName;

      if (ent.GetKit().GetAvailability() != KitAvailability.Blue)
        entityName = entityName + ChatColor.RESET + " " + ChatColor.WHITE + C.Line + ent.GetKit().GetCost() + " Coins";
      else {
        entityName = entityName + ChatColor.RESET + " " + ChatColor.WHITE + C.Line + "Ultra";
      }
    }
    return entityName;
  }


  public void AddPlayerToScoreboards(Player player, String teamName)
  {
      for (Scoreboard scoreboard : GetScoreboards()) {
          for (Team team : scoreboard.getTeams()) {
              team.removePlayer(player);
             }
}

    if (teamName == null) {
      teamName = "";
    }
    for (Scoreboard scoreboard : GetScoreboards())
    {
      String rankName = myrank.prefix(player)+" ";
    String prxtag = Color(rankName + teamName);
    
    if(prxtag.length() > 16){
        prxtag = prxtag.substring(0, 15);
    }
    if(rankName.length() > 16){
        rankName = rankName.substring(0, 15);
    }
      if(scoreboard.getTeam(prxtag) == null){
    scoreboard.registerNewTeam(prxtag).setPrefix(rankName);
}

      try
      {
        scoreboard.getTeam(prxtag).addPlayer(player);
      }
      catch (IllegalArgumentException | IllegalStateException e)
      {
        System.out.println("GameLobbyManager AddPlayerToScoreboard Error");
        System.out.println("[" + rankName + teamName + "] adding [" + player.getName() + "]");
        System.out.println("Team is Null [" + (scoreboard.getTeam(rankName + teamName) == null) + "]");
      }
    }
  }
}