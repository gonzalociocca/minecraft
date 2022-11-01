package mineultra.game.center.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import me.gonzalociocca.mineultra.DBManager;
import mineultra.core.common.util.*;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.combat.DeathMessageType;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.events.PlayerGameRespawnEvent;
import mineultra.game.center.events.PlayerStateChangeEvent;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import mineultra.game.center.kit.Perk;
import mineultra.game.center.managers.GameLobbyManager;
import mineultra.game.center.world.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public abstract class Game
  implements Listener
{
  public centerManager Manager;
  private final GameType _gameType;
  private final String[] _gameDesc;
  private final ArrayList<String> _files;
  private GameState _gameState = GameState.Loading;
  private long _gameStateTime = System.currentTimeMillis();
    public boolean kitMenuEnabled = false;
  private boolean _prepareCountdown = false;

  private int _countdown = -1;
  private boolean _countdownForce = false;

  private String _customWinLine = "";
  private Kit[] _kits;
  private final ArrayList<GameTeam> _teamList = new ArrayList();

  protected NautHashMap<Player, Kit> _playerKit = new NautHashMap();
  private final NautHashMap<GameTeam, ArrayList<Player>> _teamPreference = new NautHashMap();
  private final NautHashMap<Player, HashMap<String, GemData>> _gemCount = new NautHashMap();

  private final NautHashMap<String, Location> _playerLocationStore = new NautHashMap();
  public Scoreboard _scoreboard;
  private final Objective _sideObjective;
  public WorldData WorldData = null;

  private long _helpTimer = 0L;
  private int _helpIndex = 0;
  private ChatColor _helpColor = ChatColor.YELLOW;
  protected String[] _help;
  public boolean Damage = true;
  public boolean DamagePvP = true;
  public boolean DamagePvE = true;
  public boolean DamageEvP = true;
  public boolean DamageSelf = true;
  public boolean DamageTeamSelf = false;
  public boolean DamageTeamOther = true;

  public boolean BlockBreak = false;
  public HashSet<Integer> BlockBreakAllow = new HashSet();
  public HashSet<Integer> BlockBreakDeny = new HashSet();

  public boolean BlockPlace = false;
  public HashSet<Integer> BlockPlaceAllow = new HashSet();
  public HashSet<Integer> BlockPlaceDeny = new HashSet();

  public boolean ItemPickup = false;
  public HashSet<Integer> ItemPickupAllow = new HashSet();
  public HashSet<Integer> ItemPickupDeny = new HashSet();

  public boolean ItemDrop = false;
  public HashSet<Integer> ItemDropAllow = new HashSet();
  public HashSet<Integer> ItemDropDeny = new HashSet();

  public boolean InventoryOpen = false;

  public boolean PrivateBlocks = false;

  public boolean DeathOut = true;
  public boolean DeathDropItems = false;
  public boolean DeathMessages = true;
  public double DeathSpectateSecs = 0.0D;

  public boolean QuitOut = true;

  public boolean IdleKick = true;

  public boolean CreatureAllow = false;
  public boolean CreatureAllowOverride = false;

  public int WorldTimeSet = -1;
  public boolean WorldWeatherEnabled = false;
  public int WorldWaterDamage = 0;
  public boolean WorldBoundaryKill = true;

  public int HungerSet = -1;
  public int HealthSet = -1;

  public int SpawnDistanceRequirement = 1;

  public boolean PrepareFreeze = true;

  public boolean RepairWeapons = true;

  public boolean AutoBalance = true;

  public boolean AnnounceStay = true;
  public boolean AnnounceJoinQuit = true;
  public boolean AnnounceSilence = true;

  public boolean DisplayLobbySide = true;

  public boolean AutoStart = true;

  public GameState KitRegisterState = GameState.Live;

  public boolean CompassEnabled = false;
  public boolean SoupEnabled = true;

  public boolean GiveClock = true;

  public double GemMultiplier = 1.0D;

  public HashMap<Location, Player> PrivateBlockMap = new HashMap();
  public HashMap<String, Integer> PrivateBlockCount = new HashMap();

  public Location SpectatorSpawn = null;

  public boolean FirstKill = true;

  public String Winner = "No one`s";
  public GameTeam WinnerTeam = null;
  
  public int chest1 = 0;
  public int chest2 = 0;
  public int chest3 = 0;
  
  public int time1 = 0;
  public int time2 = 0;
    public int time3 = 0;
    public DBManager db;
    public boolean canJoinAfterStart = false;

  public Game(centerManager manager, GameType gameType, Kit[] kits, String[] gameDesc)
  {
    Manager = manager;
db = manager.getDB();
    _gameType = gameType;
    _gameDesc = gameDesc;

    _kits = kits;

    _scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    _sideObjective = _scoreboard.registerNewObjective("Obj" + UtilMath.r(999999999), "dummy");
    _sideObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    _sideObjective.setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+web);

    _files = Manager.LoadFiles(GetName());
    WorldData = new WorldData(Manager,this);
    
    System.out.println("Loading " + GetName() + "...");
  }

  public ArrayList<String> GetFiles()
  {
    return _files;
  }
  
int color = 0;

@EventHandler
public void updateSco(UpdateEvent event){
    if(event.getType() != UpdateType.FAST){
        return;
    }
    nextObjective();
}
String web = null;
  public void nextObjective() {
      if(web== null){
          this.web = this.Manager.getConfig().getString("GameConfig.web");
      }
    if(color == 0){
    color = 1;
    _sideObjective.setDisplayName(ChatColor.YELLOW+""+ChatColor.BOLD+web);
    }else if(color == 1){
    _sideObjective.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+web);
    color = 2;
    }else if(color == 2){
    _sideObjective.setDisplayName(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+web);
        color = 3;
    }else if(color == 3){
     _sideObjective.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+web);
        color = 0;
    }else{
     _sideObjective.setDisplayName(ChatColor.GREEN+""+ChatColor.BOLD+web);
    
        color = 0;
    }
  }
  
  public final String GetName()
  {
    return _gameType.GetName();
  }

  public String GetMode()
  {
    return null;
  }

  public GameType GetType()
  {
    return _gameType;
  }

  public String[] GetDesc()
  {
    return _gameDesc;
  }

  public void SetCustomWinLine(String line)
  {
    _customWinLine = line;
  }

  public Scoreboard GetScoreboard()
  {
    return _scoreboard;
  }

  public Objective GetObjectiveSide()
  {
    return _sideObjective;
  }

  public ArrayList<GameTeam> GetTeamList()
  {
    return _teamList;
  }
  
  public int GetCountdown()
  {
    return _countdown;
  }

  public void SetCountdown(int time)
  {
    _countdown = time;
  }

  public boolean GetCountdownForce()
  {
    return _countdownForce;
  }

  public void SetCountdownForce(boolean value)
  {
    _countdownForce = value;
  }

  public NautHashMap<GameTeam, ArrayList<Player>> GetTeamPreferences()
  {
    return _teamPreference;
  }

  public NautHashMap<Player, Kit> GetPlayerKits()
  {
    return _playerKit;
  }

  public NautHashMap<Player, HashMap<String, GemData>> GetPlayerGems()
  {
    return _gemCount;
  }

  public NautHashMap<String, Location> GetLocationStore()
  {
    return _playerLocationStore;
  }

  public GameState GetState()
  {
    return _gameState;
  }

  public void SetState(GameState state)
  {
    _gameState = state;
    _gameStateTime = System.currentTimeMillis();

    if(this.GetType() != GameType.TurboRacers){
    for (Player player : UtilServer.getPlayers()) {
      player.leaveVehicle();
    }}

    GameStateChangeEvent stateEvent = new GameStateChangeEvent(this, state);
    UtilServer.getServer().getPluginManager().callEvent(stateEvent);

    System.out.println(GetName() + " state set to " + state.toString());
  }

  public long GetStateTime()
  {
    return _gameStateTime;
  }

  public boolean InProgress()
  {
    return (GetState() == GameState.Prepare) || (GetState() == GameState.Live);
  }

  public boolean IsLive()
  {
    return _gameState == GameState.Live;
  }

  public void AddTeam(GameTeam team)
  {
      
      if(this.GetType() == GameType.SWTeams ||  this.GetType() == GameType.Smash){
   for(ChatColor ca : ChatColor.values()){
       if(ca.name().equalsIgnoreCase(team.GetName())){
           team.SetColor(ca);
       }
   }
      }
      
      
    GetTeamList().add(team);

    team.SetSpawnRequirement(SpawnDistanceRequirement);

    System.out.println("Created Team: " + team.GetName());
  }

  
  public boolean HasTeam(GameTeam team)
  {
    for (GameTeam cur : GetTeamList()) {
      if (cur.equals(team))
        return true;
    }
    return false;
  }

  public void RestrictKits()
  {
  }

  public void RegisterKits()
  {
    for (Kit kit : _kits)
    {
      UtilServer.getServer().getPluginManager().registerEvents(kit, Manager.GetPlugin());

      for (Perk perk : kit.GetPerks())
        UtilServer.getServer().getPluginManager().registerEvents(perk, Manager.GetPlugin());
    }
  }
  
  

  public void DeregisterKits()
  {
    for (Kit kit : _kits)
    {
      HandlerList.unregisterAll(kit);

      for (Perk perk : kit.GetPerks())
        HandlerList.unregisterAll(perk);
    }
  }

  public void ParseData()
  {
  }

  public void SetPlayerTeam(Player player, GameTeam team)
  {
    GameTeam pastTeam = GetTeam(player);
    if (pastTeam != null)
    {
      pastTeam.RemovePlayer(player);
    }

    team.AddPlayer(player);

    ValidateKit(player, team);

    SetPlayerScoreboardTeam(player, team.GetName().toUpperCase());

    Manager.GetLobby().AddPlayerToScoreboards(player, team.GetName().toUpperCase());
  }
  Objective below = null;
  public Objective GetObjectiveBelow(){
      if(below == null){
  below = _scoreboard.registerNewObjective("Obj" + UtilMath.r(999999999), "dummy");
  
      }
      return below;
  }
  public HashMap<Player,Double> smash = new HashMap();

  
  public void SetPlayerScoreboardTeam(Player player, String teamName)
  {
    for (Team team : GetScoreboard().getTeams()) {
      team.removePlayer(player);
    }

    String rkname = db.getPlayerData(player).getRank().getScoreboardPrefix();
    if (teamName == null) {
      teamName = "";
    }
    String group = player.getName();
    if(group.length() > 16){
        group = group.substring(0,15);
    }
    if(teamName == "SPEC"){
        group = "SPEC";
    }

 if(rkname == null){
        rkname = "";
    }else{
           rkname = rkname.toUpperCase();
    }

     if(rkname.length() > 15){
         rkname = rkname.substring(0,14);
     }

    if(GetScoreboard().getTeam(group) == null){
        _scoreboard.registerNewTeam(group).setPrefix(rkname+this.GetTeam(player).GetColor());
    }
    
    GetScoreboard().getTeam(group).addPlayer(player);
  ;
  }

  public GameTeam ChooseTeam(Player player)
  {
    GameTeam team = null;

      for (GameTeam _teamList1 : _teamList) {
          if ((team == null) || (((GameTeam) _teamList1).GetSize() < team.GetSize())) {
              team = (GameTeam) _teamList1;
          }
      }

    return team;
  }

  public double GetKillsGems(Player killer, Player killed, boolean assist)
  {
    if (!DeathOut)
    {
      return 0.5D;
    }

    if (!assist)
    {
      return 4.0D;
    }

    return 1.0D;
  }
  
  public void resetGems(Player player)
  {
    if (_gemCount.containsKey(player)) {
      _gemCount.put(player, new HashMap());
    }

  }
  
  public HashMap<String, GemData> GetGems(Player player)
  {
    if (!_gemCount.containsKey(player)) {
      _gemCount.put(player, new HashMap());
    }
    return (HashMap)_gemCount.get(player);
  }

  public void AddGems(Player player, double gems, String reason, boolean countAmount)
  {
    if ((!countAmount) && (gems < 1.0D)) {
      gems = 1.0D;
    }
    if (GetGems(player).containsKey(reason))
    {
      ((GemData)GetGems(player).get(reason)).AddGems(gems);
    }
    else
    {
      GetGems(player).put(reason, new GemData(gems, countAmount));
    }
  }

  public void ValidateKit(Player player, GameTeam team)
  {
    if ((GetKit(player) == null) || (!team.KitAllowed(GetKit(player))))
    {
      for (Kit kit : _kits)
      {
        if ((kit.GetAvailability() != KitAvailability.Hide) && 
          (kit.GetAvailability() != KitAvailability.Null))
        {
          if (team.KitAllowed(kit))
          {
            SetKit(player, kit, false);
            break;
          }
        }
      }
    }
  }

  public void SetKit(Player player, Kit kit, boolean announce) {
    GameTeam team = GetTeam(player);
    if (team != null)
    {
      if (!team.KitAllowed(kit))
      {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2.0F, 0.5F);
        UtilPlayer.message(player, F.main("Kit", F.elem(team.GetFormattedName()) + " no puedes usar " + F.elem(new StringBuilder(String.valueOf(kit.GetFormattedName())).append(" Kit").toString()) + "."));
        return;
      }
    }

    if (_playerKit.get(player) != null)
    {
      ((Kit)_playerKit.get(player)).Deselected(player);
    }

    _playerKit.put(player, kit);

    kit.Selected(player);

    if (announce)
    {
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0F, 1.0F);
      UtilPlayer.message(player, F.main("Kit", Colorizer.Color("&eHas equipado &a&n") + kit._kitName+""));
    }

    if (InProgress())
      kit.ApplyKit(player);
  }

  public Kit GetKit(Player player)
  {
    return (Kit)_playerKit.get(player);
  }
  

  public Kit[] GetKits()
  {
    return _kits;
  }

  public boolean HasKit(Kit kit)
  {
    for (Kit cur : GetKits()) {
      if (cur.equals(kit))
        return true;
    }
    return false;
  }

  public boolean HasKit(Player player, Kit kit)
  {
    if (!IsAlive(player)) {
      return false;
    }
    if (GetKit(player) == null) {
      return false;
    }
    return GetKit(player).equals(kit);
  }

  public boolean SetPlayerState(Player player, GameTeam.PlayerState state)
  {
    GetScoreboard().resetScores(player.getName());

    GameTeam team = GetTeam(player);

    if (team == null) {
      return false;
    }
    team.SetPlayerState(player, state);

    PlayerStateChangeEvent playerStateEvent = new PlayerStateChangeEvent(this, player, GameTeam.PlayerState.OUT);
    UtilServer.getServer().getPluginManager().callEvent(playerStateEvent);

    return true;
  }
  public Boolean loadLoca(Location loca){
      return loca.getWorld().getChunkAt(new Location(loca.getWorld(), loca.getX(), 0.0D, loca.getZ())) != null;
  }

  public abstract void EndCheck();

  public void RespawnPlayer(final Player player)
  {
      if(this.GetType() != GameType.TurboRacers){
    player.eject();}
      
    Location loca = GetTeam(player).GetSpawn();

if(loadLoca(loca)){
      player.teleport(loca);       
}

    
    Manager.Clear(player);

    PlayerGameRespawnEvent event = new PlayerGameRespawnEvent(this, player);
    UtilServer.getServer().getPluginManager().callEvent(event);

    Manager.GetPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Manager.GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
        GetKit(player).ApplyKit(player);
      }
    }
    , 0L);
  }

  public boolean IsPlaying(Player player)
  {
    return GetTeam(player) != null;
  }

  public boolean IsAlive(Player player)
  {
    GameTeam team = GetTeam(player);

    if (team == null) {
      return false;
    }
    return team.IsAlive(player);
  }

  public ArrayList<Player> GetPlayers(boolean aliveOnly)
  {
    ArrayList players = new ArrayList();

    for (GameTeam team : _teamList) {
      players.addAll(team.GetPlayers(aliveOnly));
    }
    return players;
  }

  public GameTeam GetTeam(String player, boolean aliveOnly)
  {
    for (GameTeam team : _teamList) {
      if (team.HasPlayer(player, aliveOnly))
        return team;
    }
    return null;
  }

  public GameTeam GetTeam(Player player)
  {
    for (GameTeam team : _teamList) {
      if (team.HasPlayer(player))
        return team;
    }
    return null;
  }

  public GameTeam GetTeam(ChatColor color)
  {
    for (GameTeam team : _teamList) {
      if (team.GetColor() == color)
        return team;
    }
    return null;
  }

  public Location GetSpectatorLocation()
  {
    if (SpectatorSpawn != null) {
      return SpectatorSpawn;
    }
    Vector vec = new Vector(0, 0, 0);
    double count = 0.0D;

      for (GameTeam team : GetTeamList()) {
        for (Location spawn : team.GetSpawns()) {
            count += 1.0D;
            vec.add(spawn.toVector());
        }
}

    SpectatorSpawn = new Location(WorldData.World, 0.0D, 0.0D, 0.0D);

    vec.multiply(1.0D / count);

    SpectatorSpawn.setX(vec.getX());
    SpectatorSpawn.setY(vec.getY());
    SpectatorSpawn.setZ(vec.getZ());

    while ((!UtilBlock.airFoliage(SpectatorSpawn.getBlock())) || (!UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP))))
    {
      SpectatorSpawn.add(0.0D, 1.0D, 0.0D);
    }

    int Up = 0;

    for (int i = 0; i < 15; i++)
    {
      if (!UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP)))
        break;
      SpectatorSpawn.add(0.0D, 1.0D, 0.0D);
      Up++;
    }

    while (((Up > 0) && (!UtilBlock.airFoliage(SpectatorSpawn.getBlock()))) || (!UtilBlock.airFoliage(SpectatorSpawn.getBlock().getRelative(BlockFace.UP))))
    {
      SpectatorSpawn.subtract(0.0D, 1.0D, 0.0D);
      Up--;
    }

    SpectatorSpawn = SpectatorSpawn.getBlock().getLocation().add(0.5D, 0.1D, 0.5D);

    while ((SpectatorSpawn.getBlock().getTypeId() != 0) || (SpectatorSpawn.getBlock().getRelative(BlockFace.UP).getTypeId() != 0)) {
      SpectatorSpawn.add(0.0D, 1.0D, 0.0D);
    }
    return SpectatorSpawn;
  }
  
  
  public void SetFakeSpectator(Player player)
  {
    Manager.Clear(player);

    player.teleport(GetSpectatorLocation());
    player.setGameMode(GameMode.CREATIVE);
    player.setFlying(true);
    player.setFlySpeed(0.1F);
    
    ((CraftPlayer)player).getHandle().setInvisible(true);
    for(Player playin : this.GetPlayers(true)){
        playin.hidePlayer(player);
    }
    ((CraftPlayer)player).getHandle().setSprinting(false);;

    Manager.GetBuffer().Factory().Cloak("Spectator", player, player, 7777.0D, true, true);
    
  }
  
  public void SetSpectator(Player player)
  {
    Manager.Clear(player);

    player.teleport(GetSpectatorLocation());

    SetPlayerScoreboardTeam(player, "SPEC");
    
    player.setGameMode(GameMode.CREATIVE);
    player.setFlying(true);
    player.setFlySpeed(0.1F);
((CraftPlayer)player).getHandle().setInvisible(true);

 Manager.GetBuffer().Factory().Cloak("Spectator", player, player, 7777.0D, true, true);
player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,99999,1,true), true);
((CraftPlayer)player).getHandle().setSprinting(false);
for(Player playin : Bukkit.getOnlinePlayers()){
   playin.hidePlayer(player);
    }

    if ((GetTeam(player) != null) && (_scoreboard.getTeam(GetTeam(player).GetName().toUpperCase()) != null))
    {
      _scoreboard.getTeam(GetTeam(player).GetName().toUpperCase()).removePlayer(player);
    }

 



  }

  @EventHandler
  public void ScoreboardUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC2) {
      return;
    }
    for (GameTeam team : GetTeamList())
    {
      String name = team.GetColor() + team.GetName();
      if (name.length() > 16) {
        name = name.substring(0, 16);
      }
      Score score = GetObjectiveSide().getScore(name);
      score.setScore(team.GetPlayers(true).size());
      for(Player pe : team.GetPlayers(false))
      this.SetPlayerScoreboardTeam(pe, team.GetName().toUpperCase());
    }
  }

  public DeathMessageType GetDeathMessageType()
  {
    if (!DeathMessages) {
      return DeathMessageType.None;
    }
    if (DeathOut) {
      return DeathMessageType.Detailed;
    }
    return DeathMessageType.Simple;
  }

  public boolean CanJoinTeam(GameTeam team)
  {
    return team.GetSize() < Math.max(1, UtilServer.getPlayers().length / GetTeamList().size());
  }

  public GameTeam GetTeamPreference(Player player)
  {
    for (GameTeam team : _teamPreference.keySet())
    {
      if ((_teamPreference.get(team)).contains(player)) {
        return team;
      }
    }
    return null;
  }

  public void RemoveTeamPreference(Player player)
  {
    for (ArrayList queue : _teamPreference.values())
      queue.remove(player);
  }

  public String GetTeamQueuePosition(Player player)
  {
    ArrayList queue = new ArrayList();
    int i = 0;
    for (Iterator localIterator = _teamPreference.values().iterator(); i < queue.size();localIterator.hasNext() 
      )
    {
      queue = (ArrayList)localIterator.next();

      i = 0; 
      

      if (((Player)queue.get(i)).equals(player))
        return i + 1 + "/" + queue.size();
      i++;
    }

    return "First";
  }

  public void InformQueuePositions()
  {

      for (GameTeam team : _teamPreference.keySet()) {
          for (Player player : team.GetPlayers(false)) {
              UtilPlayer.message(player, F.main("Team", "You are " + F.elem(GetTeamQueuePosition(player)) + " in the queue for " + F.elem(new StringBuilder(String.valueOf(team.GetFormattedName())).append(" Team").toString()) + "."));
          }
}
  }

  public void AnnounceGame()
  {
    for (Player player : UtilServer.getPlayers()) {
      AnnounceGame(player);
    }
  }

  public void AnnounceGame(Player player)
  {
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 1.0F);

    for (int i = 0; i < 6 - GetDesc().length; i++) {
      UtilPlayer.message(player, "");
    }
    UtilPlayer.message(player, MSGUtil.getLineSpacer());

    UtilPlayer.message(player, C.cGreen + "Game - " + C.cYellow + C.Bold + GetName());
    UtilPlayer.message(player, "");

    for (String line : GetDesc())
    {
      UtilPlayer.message(player, C.cWhite + "- " + line);
    }

    UtilPlayer.message(player, "");
    UtilPlayer.message(player, C.cGreen + "Map - " + C.cYellow + C.Bold + WorldData.MapName + ChatColor.RESET + C.cGray + " created by " + C.cYellow + C.Bold + WorldData.MapAuthor);

    UtilPlayer.message(player, MSGUtil.getLineSpacer());
  }

  public void AnnounceEnd(GameTeam team)
  {
    if (!IsLive()) {
      return;
    }
    for (Player player : UtilServer.getPlayers())
    {
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 1.0F);

      UtilPlayer.message(player, "");
      UtilPlayer.message(player, MSGUtil.getLineSpacer());

      UtilPlayer.message(player, Colorizer.Color("&aGame - &f") + GetName());
      UtilPlayer.message(player, "");
      UtilPlayer.message(player, "");

      if (team != null)
      {
        WinnerTeam = team;
        Winner = (team.GetName() + " Team");
        if(this.Manager.GetGame().GetType() != GameType.SkyWars && this.Manager.GetGame().GetType() != GameType.BuildBattle)
        UtilPlayer.message(player, team.GetColor() + C.Bold + team.GetName() + "  Wins the game!!");
      }
      else
      {
        UtilPlayer.message(player, ChatColor.WHITE + "Nadie ha ganado el juego...");
      }

      UtilPlayer.message(player, _customWinLine);
      UtilPlayer.message(player, "");
      UtilPlayer.message(player, Colorizer.Color("&aMap - &f" + WorldData.MapName + C.cGray + " creado por " + "&f" + WorldData.MapAuthor));

      UtilPlayer.message(player, MSGUtil.getLineSpacer());
    }


  }

  public void AnnounceEnd(ArrayList<Player> places)
  {
    for (Player player : UtilServer.getPlayers())
    {
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0F, 1.0F);

      UtilPlayer.message(player, "");
      UtilPlayer.message(player, MSGUtil.getLineSpacer());

      UtilPlayer.message(player, Colorizer.Color("&aGame - &f") + GetName());
      UtilPlayer.message(player, "");

      if ((places == null) || (places.isEmpty()))
      {
        UtilPlayer.message(player, "");
        UtilPlayer.message(player, ChatColor.WHITE + "Nadie ha ganado el juego...");
        UtilPlayer.message(player, "");
      }
      else
      {
        if (places.size() >= 1)
        {
          Winner = ((Player)places.get(0)).getName();
          UtilPlayer.message(player, C.cRed + C.Bold + "1er lugar" + C.cWhite + " - " + ((Player)places.get(0)).getName());
        }

        if (places.size() >= 2) {
          UtilPlayer.message(player, C.cGold + C.Bold + "2do lugar" + C.cWhite + " - " + ((Player)places.get(1)).getName());
        }
        if (places.size() >= 3) {
          UtilPlayer.message(player, C.cYellow + C.Bold + "3er lugar" + C.cWhite + " - " + ((Player)places.get(2)).getName());
        }
      }
      UtilPlayer.message(player, "");
      UtilPlayer.message(player, Colorizer.Color("&aMap - &f" + WorldData.MapName + C.cGray + " creado por " + "&f" + WorldData.MapAuthor));

      UtilPlayer.message(player, MSGUtil.getLineSpacer());
    }

  }

  public void Announce(String message)
  {
    for (Player player : UtilServer.getPlayers())
    {
      player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F);

      UtilPlayer.message(player, message);
    }

    System.out.println("[Anuncio] " + message);
  }

  public boolean AdvertiseText(GameLobbyManager gameLobbyManager, int _advertiseStage)
  {
    return false;
  }

  public boolean CanThrowTNT(Location location)
  {
    return true;
  }

  @EventHandler
  public void HelpUpdate(UpdateEvent event)
  {
    if ((_help == null) || (_help.length == 0)) {
      return;
    }
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    if (GetState() != GameState.Recruit) {
      return;
    }
    if (!UtilTime.elapsed(_helpTimer, 8000L)) {
      return;
    }
    if (_helpColor == ChatColor.YELLOW)
      _helpColor = ChatColor.GREEN;
    else {
      _helpColor = ChatColor.YELLOW;
    }
    _helpTimer = System.currentTimeMillis();

    String msg = C.cWhite + C.Bold + "TIP " + ChatColor.RESET + _helpColor + _help[_helpIndex];

    for (Player player : UtilServer.getPlayers())
    {
      player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);

      UtilPlayer.message(player, msg);
    }

    _helpIndex = ((_helpIndex + 1) % _help.length);
  }

  public void StartPrepareCountdown()
  {
    _prepareCountdown = true;
  }

  public boolean CanStartPrepareCountdown()
  {
    return _prepareCountdown;
  }

  public static enum GameState
  {
    Loading, 
    Recruit, 
    Prepare, 
    Live, 
    End, 
    Dead;
  }
}