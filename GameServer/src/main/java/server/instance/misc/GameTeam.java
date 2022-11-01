package server.instance.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import server.common.Code;
import server.common.TeleportReason;
import server.common.event.PlayerStateChangeEvent;
import server.instance.GameServer;
import server.instance.core.kit.Kit;
import server.util.UtilMath;
import server.util.UtilMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GameTeam
{
  private final GameServer Host;
  private double _respawnTime = 0.0D;
  private String _name;
  private String _color;
  private final HashMap<Player, PlayerState> _players = new HashMap();
  private ArrayList<Location> _spawns;
  private Creature _teamEntity = null;
  public int TeamLifes = 30;
  private final HashSet<Kit> _kitRestrict = new HashSet();
  private int _spawnDistance = 0;
  private boolean _visible = true;
  Integer _teamID;

  Color rawColor;
  public GameTeam(GameServer host, Integer id, ArrayList<Location> spawns)
  {
    Host = host;
    _teamID = id;
    _name = ""+id;
    //_color = host.getType().isSolo() ? Code.getColorOfInteger(15) : Code.getColorOfInteger(id);
    _color = Code.getColorOfInteger(15);
    _spawns = spawns;
    TeamLifes = 30;
    maxTeamSize = spawns.size();

    //todo: implement scoreboard api
    //UtilScoreboard.fillScoreboard(getScoreboard().getBoard(), host);
    rawColor = Code.decodeRawColor(_teamID);
  }

  int maxTeamSize = 1;

  public int getMaxSize(){
    return maxTeamSize;
  }

  public String getName()
  {
    return _name;
  }

  public String getColor()
  {
    return _color;
  }
  public Color getRawColor(){
    return rawColor;
  }

  public ArrayList<Location> GetSpawns()
  {
    return _spawns;
  }

  public Location GetSpawn()
  {
    ArrayList valid = new ArrayList();

    Location best = null;
    double bestDist = 0.0D;

    for (Location loc : _spawns)
    {
      double closestPlayer = -1.0D;

      for (Player player : Host.getPlayers(true))
      {
        double playerDist = UtilMath.offset(player.getLocation(), loc);

        if ((closestPlayer == -1.0D) || (playerDist < closestPlayer)) {
          closestPlayer = playerDist;
        }
      }
      if ((best == null) || (closestPlayer > bestDist))
      {
        best = loc;
        bestDist = closestPlayer;
      }

      if (closestPlayer > _spawnDistance)
      {
        valid.add(loc);
      }
    }

    if (valid.size() > 0) {
      valid.get(UtilMath.r(valid.size()));
    }
    if (best != null) {
      return best;
    }
    return _spawns.get(UtilMath.r(_spawns.size()));
  }

  public void AddPlayer(Player player)
  {
    setPlayerState(player, PlayerState.IN);
    
    for (Player other : getPlayers(false))
    {
      if (!other.equals(player))
      {
        other.showPlayer(player);
      }
    }
  }

  public void RemovePlayer(Player player) {
    _players.remove(player);
  }

  public Player GetPlayer(String name)
  {
    for (Player player : _players.keySet()) {
      if (player.getName().equals(name))
        return player;
    }
    return null;
  }

  public boolean hasPlayer(Player player)
  {
    return _players.containsKey(player);
  }

  public boolean hasPlayer(String name, boolean alive)
  {
    for (Player player : _players.keySet()) {
      if (player.getName().equals(name) && (!alive || alive && _players.get(player) == PlayerState.IN))
        return true;
    }
    return false;
  }

  public int getSize()
  {
    return _players.size();
  }


  public boolean setPlayerState(Player player, PlayerState state) {

    GameTeam.PlayerState currentState = _players.get(player);

    _players.put(player, state);

    if(currentState == null || currentState != state) {
      PlayerStateChangeEvent playerStateEvent = new PlayerStateChangeEvent(Host, player, state);
      Bukkit.getServer().getPluginManager().callEvent(playerStateEvent);

      if (state == GameTeam.PlayerState.OUT) {
        Host.endCheck();
      }
    }

    return true;
  }

  public boolean isTeamAlive()
  {
    for (PlayerState state : _players.values()) {
      if (state == PlayerState.IN)
        return true;
    }
    return false;
  }

  public ArrayList<Player> getPlayers(boolean aliveOnly)
  {
    ArrayList alive = new ArrayList();
    if(aliveOnly){
      for(Map.Entry<Player, PlayerState> entry : _players.entrySet()){
        if(entry.getValue()==PlayerState.IN){
          alive.add(entry.getKey());
        }
      }
    }else{
      alive.addAll(_players.keySet());
    }
    return alive;
  }
  public int getPlayersCount(boolean aliveOnly){
    int alive = 0;
    if(aliveOnly){
      for(Map.Entry<Player, PlayerState> entry : _players.entrySet()){
        if(entry.getValue()==PlayerState.IN){
          alive++;
        }
      }
    }else{
      alive+=_players.size();
    }
    return alive;
}

  public String getFormattedName()
  {
    return getColor() + UtilMsg.Bold + getName();
  }

  public void spawnTeleport(Player player)
  {
    spawnTeleport(player, GetSpawn());
  }

  public void spawnTeleport(Player player, Location loc)
  {
    player.leaveVehicle();
    player.eject();
    Host.teleportPlayer(player, loc, TeleportReason.Spawn);
  }

  public void spawnTeleport()
  {
    for (Player player : getPlayers(true))
    {
      spawnTeleport(player);
    }
  }

  public HashSet<Kit> GetRestrictedKits()
  {
    return _kitRestrict;
  }

  public boolean isKitAllowed(Kit kit)
  {
    return !_kitRestrict.contains(kit);
  }

  public boolean isAlive(Player player)
  {
    if (!_players.containsKey(player)) {
      return false;
    }
    return _players.get(player) == PlayerState.IN;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public byte getColorData(){
    return (byte)(_teamID%16);
  }

  public void setTeamEntity(Creature ent)
  {
    _teamEntity = ent;
  }

  public LivingEntity getTeamEntity()
  {
    return _teamEntity;
  }

  public void setSpawns(ArrayList<Location> spawns)
  {
    _spawns = spawns;
  }

  public void setSpawnRequirement(int value)
  {
    _spawnDistance = value;
  }

  public void setVisible(boolean b)
  {
    _visible = b;
  }

  public boolean setVisible()
  {
    return _visible;
  }

  public void setRespawnTime(double i)
  {
    _respawnTime = i;
  }

  public double getRespawnTime()
  {
    return _respawnTime;
  }

  public boolean isTeamLiving(){
      return this.TeamLifes != 0;
  }
  
  public int damageTeam(){
      if(this.TeamLifes == 0){
          return TeamLifes;
      }
      this.TeamLifes = this.TeamLifes-1;
      return TeamLifes;
  }

  public enum PlayerState
  {
    IN("In", ChatColor.GREEN), 
    OUT("Out", ChatColor.RED);

    private final String name;
    private final ChatColor color;

    PlayerState(String name, ChatColor color) {
      this.name = name;
      this.color = color;
    }

    public String GetName()
    {
      return name;
    }

    public ChatColor GetColor()
    {
      return color;
    }
  }
}