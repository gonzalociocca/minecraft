package mineultra.game.center.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.game.center.GameType;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.kit.KitAvailability;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GameTeam
{
  private final Game Host;
  private double _respawnTime = 0.0D;
  private String _name;
  private ChatColor _color;
  private final HashMap<Player, PlayerState> _players = new HashMap();
  private ArrayList<Location> _spawns;
  private Creature _teamEntity = null;
  public int TeamLifes = 30;
  private final HashSet<Kit> _kitRestrict = new HashSet();

  private int _spawnDistance = 0;

  private boolean _visible = true;

  public GameTeam(Game host, String name, ChatColor color, ArrayList<Location> spawns)
  {
    Host = host;

    _name = name;
    _color = color;
    _spawns = spawns;
    this.TeamLifes = 30;
  }

  public String GetName()
  {
    return _name;
  }

  public ChatColor GetColor()
  {
    return _color;
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

      for (Player player : Host.GetPlayers(true))
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
    return (Location)_spawns.get(UtilMath.r(_spawns.size()));
  }

  public void AddPlayer(Player player)
  {
    _players.put(player, PlayerState.IN);
if(Host.GetType() != GameType.SkyWars && Host.GetType() != GameType.BuildBattle){
}
    
    for (Player other : UtilServer.getPlayers())
    {
      if (!other.equals(player))
      {
        other.hidePlayer(player);
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

  public boolean HasPlayer(Player player)
  {
    return _players.containsKey(player);
  }

  public boolean HasPlayer(String name, boolean alive)
  {
    for (Player player : _players.keySet()) {
      if ((player.getName().equals(name)) && (
        (!alive) || ((alive) && (_players.get(player) == PlayerState.IN))))
        return true;
    }
    return false;
  }

  public int GetSize()
  {
    return _players.size();
  }

  public void SetPlayerState(Player player, PlayerState state)
  {
    if (player == null) {
      return;
    }
    _players.put(player, state);
  }

  public boolean IsTeamAlive()
  {
    for (PlayerState state : _players.values()) {
      if (state == PlayerState.IN)
        return true;
    }
    return false;
  }

  public ArrayList<Player> GetPlayers(boolean playerIn)
  {
    ArrayList alive = new ArrayList();

    for (Player player : _players.keySet()) {
      if ((!playerIn) || (_players.get(player) == PlayerState.IN))
        alive.add(player);
    }
    return alive;
  }

  public String GetFormattedName()
  {
    return GetColor() + "§l" + GetName();
  }

  public void SpawnTeleport(Player player)
  {
    player.leaveVehicle();
    player.eject();
    player.teleport(GetSpawn());
  }

  public void SpawnTeleport()
  {
    for (Player player : GetPlayers(true))
    {
      SpawnTeleport(player);
    }
  }

  public HashSet<Kit> GetRestrictedKits()
  {
    return _kitRestrict;
  }

  public boolean KitAllowed(Kit kit)
  {
    if (kit.GetAvailability() == KitAvailability.Null) {
      return false;
    }
    return !_kitRestrict.contains(kit);
  }

  public boolean IsAlive(Player player)
  {
    if (!_players.containsKey(player)) {
      return false;
    }
    return _players.get(player) == PlayerState.IN;
  }

  public void SetColor(ChatColor color)
  {
    _color = color;
  }

  public void SetName(String name)
  {
    _name = name;
  }

  public byte GetColorData()
  {
    if (GetColor() == ChatColor.WHITE) return 0;
    if (GetColor() == ChatColor.GOLD) return 1;
    if (GetColor() == ChatColor.LIGHT_PURPLE) return 2;
    if (GetColor() == ChatColor.AQUA) return 3;
    if (GetColor() == ChatColor.YELLOW) return 4;
    if (GetColor() == ChatColor.GREEN) return 5;

    if (GetColor() == ChatColor.DARK_GRAY) return 7;
    if (GetColor() == ChatColor.GRAY) return 8;
    if (GetColor() == ChatColor.DARK_AQUA) return 9;
    if (GetColor() == ChatColor.DARK_PURPLE) return 10;
    if (GetColor() == ChatColor.BLUE) return 11;
    if (GetColor() == ChatColor.DARK_BLUE) return 12;

    if (GetColor() == ChatColor.DARK_GREEN) return 13;
    if (GetColor() == ChatColor.RED) return 14;
    return 15;
  }

  public Color GetColorBase()
  {
    if (GetColor() == ChatColor.WHITE) return Color.WHITE;
    if (GetColor() == ChatColor.GOLD) return Color.ORANGE;
    if (GetColor() == ChatColor.LIGHT_PURPLE) return Color.PURPLE;
    if (GetColor() == ChatColor.AQUA) return Color.AQUA;
    if (GetColor() == ChatColor.YELLOW) return Color.YELLOW;
    if (GetColor() == ChatColor.GREEN) return Color.GREEN;
    if (GetColor() == ChatColor.DARK_GRAY) return Color.GRAY;
    if (GetColor() == ChatColor.GRAY) return Color.GRAY;
    if (GetColor() == ChatColor.DARK_AQUA) return Color.AQUA;
    if (GetColor() == ChatColor.DARK_PURPLE) return Color.PURPLE;
    if (GetColor() == ChatColor.BLUE) return Color.BLUE;
    if (GetColor() == ChatColor.DARK_BLUE) return Color.BLUE;
    if (GetColor() == ChatColor.DARK_GREEN) return Color.GREEN;
    if (GetColor() == ChatColor.RED) return Color.RED;
    return Color.WHITE;
  }

  public void SetTeamEntity(Creature ent)
  {
    _teamEntity = ent;
  }

  public LivingEntity GetTeamEntity()
  {
    return _teamEntity;
  }

  public void SetSpawns(ArrayList<Location> spawns)
  {
    _spawns = spawns;
  }

  public void SetSpawnRequirement(int value)
  {
    _spawnDistance = value;
  }

  public void SetVisible(boolean b)
  {
    _visible = b;
  }

  public boolean GetVisible()
  {
    return _visible;
  }

  public void SetRespawnTime(double i)
  {
    _respawnTime = i;
  }

  public double GetRespawnTime()
  {
    return _respawnTime;
  }

  public boolean isTeamLiving(){
      if(this.TeamLifes == 0){
          return false;
      }
      return true;
  }
  
  public int damageTeam(){
      if(this.TeamLifes == 0){
          return TeamLifes;
      }
      this.TeamLifes = this.TeamLifes-1;
      return TeamLifes;
  }
  
  public static enum PlayerState
  {
    IN("In", ChatColor.GREEN), 
    OUT("Out", ChatColor.RED);

    private final String name;
    private final ChatColor color;

    private PlayerState(String name, ChatColor color) {
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