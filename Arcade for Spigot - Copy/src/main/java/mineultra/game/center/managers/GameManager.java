package mineultra.game.center.managers;

import java.util.ArrayList;
import java.util.Iterator;
import me.libraryaddict.disguise.DisguiseAPI;
import mineultra.core.common.util.C;
import mineultra.core.common.util.Colorizer;
import mineultra.core.common.util.F;
import mineultra.core.common.util.UtilDisplay;
import mineultra.core.common.util.UtilInv;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.minecraft.game.core.Buffer.Buffer;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.events.GamePrepareCountdownCommence;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.events.PlayerPrepareTeleportEvent;
import mineultra.game.center.events.PlayerStateChangeEvent;
import mineultra.game.center.game.Game;
import mineultra.game.center.game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GameManager
  implements Listener
{
  centerManager Manager = null;
  private int _colorId = 0;
  
  public GameManager(centerManager manager)
  {
    this.Manager = manager;
    
    this.Manager.GetPluginManager().registerEvents(this, this.Manager.GetPlugin());
  }
  
  String web = null;
  
  @EventHandler
  public void DisplayIP(UpdateEvent event) throws IllegalArgumentException, IllegalAccessException
  {
    if (event.getType() != UpdateType.FASTER) {
      return;
    }
    if ((this.Manager.GetGame() != null) && (this.Manager.GetGame().GetState() != Game.GameState.Live))
    {
      ChatColor col = ChatColor.RED;
      if (this._colorId == 1) {
        col = ChatColor.YELLOW;
      } else if (this._colorId == 2) {
        col = ChatColor.GREEN;
      } else if (this._colorId == 3) {
        col = ChatColor.AQUA;
      }
      this._colorId = ((this._colorId + 1) % 4);
      
      if(web == null){
          web =  this.Manager.getConfig().getString("GameConfig.web");
      }
      String text = col + C.Bold + web;
      
      double health = 1.0D;
      if (this.Manager.GetGame().GetState() == Game.GameState.Prepare) {
        health = (9.0D - (System.currentTimeMillis() - this.Manager.GetGame().GetStateTime()) / 1000.0D) / 9.0D;
      } else if (this.Manager.GetGame().GetState() == Game.GameState.Recruit) {
        if (this.Manager.GetGame().GetCountdown() >= 0) {
          health = this.Manager.GetGame().GetCountdown() / 60.0D;
        }
      }
      Player[] arrayOfPlayer;
      int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
      for (int i = 0; i < j; i++)
      {
        Player player = arrayOfPlayer[i];
        UtilDisplay.displayTextBar(this.Manager.GetPlugin(), player, health, text);
      }
    }
  }

  
  @EventHandler
  public void StateUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (game.GetState() == Game.GameState.Loading)
    {
      if (UtilTime.elapsed(game.GetStateTime(), 30000L))
      {
        System.out.println("Game Load Expired.");
        game.SetState(Game.GameState.Dead);
      }
    }
    else if (game.GetState() == Game.GameState.Recruit)
    {
      if ((game.GetCountdown() != -1) && 
        (UtilServer.getPlayers().length < this.Manager.GetPlayerMin()) && 
        (!game.GetCountdownForce()))
      {
        game.SetCountdown(-1);
      }
      if (game.GetCountdown() != -1) {
        StateCountdown(game, -1, false);
      } else if (game.AutoStart) {
        if (UtilServer.getPlayers().length >= this.Manager.GetPlayerFull()) {
          StateCountdown(game, 20, false);
        } else if (UtilServer.getPlayers().length >= this.Manager.GetPlayerMin()) {
          StateCountdown(game, 60, false);
        }
      }
    }
    else if (game.GetState() == Game.GameState.Prepare)
    {
      if (game.CanStartPrepareCountdown())
      {
        Player[] arrayOfPlayer;
        int j;
        int i;
        if (UtilTime.elapsed(game.GetStateTime(), 9000L))
        {
          j = (arrayOfPlayer = UtilServer.getPlayers()).length;
          for (i = 0; i < j; i++)
          {
            Player player = arrayOfPlayer[i];
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2.0F, 2.0F);
          }
          if (game.GetPlayers(true).size() < 2)
          {
            game.Announce(C.cWhite + C.Bold + game.GetName() + " finished, not enough players");
            game.SetState(Game.GameState.Dead);
          }
          else
          {
            game.SetState(Game.GameState.Live);
          }
        }
        else
        {
          j = (arrayOfPlayer = UtilServer.getPlayers()).length;
          for (i = 0; i < j; i++)
          {
            Player player = arrayOfPlayer[i];
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
          }
        }
      }
    }
    else if (game.GetState() == Game.GameState.Live)
    {
        if (UtilTime.elapsed(game.GetStateTime(), this.Manager.getConfig().getLong("GameConfig.timelimit",3200L)*1000)) {
            game.SetState(Game.GameState.End);
        }
      
    }
    else if (game.GetState() == Game.GameState.End)
    {
      if (UtilTime.elapsed(game.GetStateTime(), 10000L)) {
        game.SetState(Game.GameState.Dead);
      }
    }
  }
  
  public void StateCountdown(Game game, int timer, boolean force)
  {
    if ((!game.GetCountdownForce()) && (!force) && (!UtilTime.elapsed(game.GetStateTime(), 15000L))) {
      return;
    }
    if (force) {
      game.SetCountdownForce(true);
    }
    TeamPreferenceJoin(game);
    

    TeamPreferenceSwap(game);
    

    TeamDefaultJoin(game);
    if (game.GetCountdown() == -1) {
      game.InformQueuePositions();
    }
    if (force) {
      game.SetCountdownForce(true);
    }
    if (game.GetCountdown() == -1) {
      game.SetCountdown(timer + 1);
    }
    if ((game.GetCountdown() > timer + 1) && (timer != -1)) {
      game.SetCountdown(timer + 1);
    }
    if (game.GetCountdown() > 0) {
      game.SetCountdown(game.GetCountdown() - 1);
    }
    if ((game.GetCountdown() > 0) && (game.GetCountdown() <= 10))
    {
      Player[] arrayOfPlayer;
      int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
      for (int i = 0; i < j; i++)
      {
        Player player = arrayOfPlayer[i];
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
      }
    }
    if (game.GetCountdown() == 0) {
      game.SetState(Game.GameState.Prepare);
    }
  }
  
  @EventHandler
  public void KitRegister(GameStateChangeEvent event)
  {
    if (event.GetState() != event.GetGame().KitRegisterState) {
      return;
    }
    event.GetGame().RegisterKits();
  }
  
  @EventHandler
  public void KitDeregister(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Dead) {
      return;
    }
    event.GetGame().DeregisterKits();
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void TeamGeneration(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Recruit) {
      return;
    }
    Game game = event.GetGame();
    for (String team : game.WorldData.SpawnLocs.keySet())
    {
      ChatColor color;

      if (team.equalsIgnoreCase("RED"))
      {
        color = ChatColor.RED;
      }
      else
      {
   
        if (team.equalsIgnoreCase("YELLOW"))
        {
          color = ChatColor.YELLOW;
        }
        else
        {

          if (team.equalsIgnoreCase("GREEN"))
          {
            color = ChatColor.GREEN;
          }
          else
          {
          
            if (team.equalsIgnoreCase("BLUE"))
            {
              color = ChatColor.AQUA;
            }
            else
            {
              color = ChatColor.DARK_GREEN;
              if ((game.GetTeamList().isEmpty()) && (game.WorldData.SpawnLocs.size() > 1)) {
                color = ChatColor.RED;
              }
              if (game.GetTeamList().size() == 1) {
                color = ChatColor.YELLOW;
              }
              if (game.GetTeamList().size() == 2) {
                color = ChatColor.GREEN;
              }
              if (game.GetTeamList().size() == 3) {
                color = ChatColor.AQUA;
              }
              if (game.GetTeamList().size() == 4) {
                color = ChatColor.GOLD;
              }
              if (game.GetTeamList().size() == 5) {
                color = ChatColor.LIGHT_PURPLE;
              }
              if (game.GetTeamList().size() == 6) {
                color = ChatColor.DARK_BLUE;
              }
              if (game.GetTeamList().size() == 7) {
                color = ChatColor.WHITE;
              }
              if (game.GetTeamList().size() == 8) {
                color = ChatColor.BLUE;
              }
              if (game.GetTeamList().size() == 9) {
                color = ChatColor.DARK_GREEN;
              }
              if (game.GetTeamList().size() == 10) {
                color = ChatColor.DARK_PURPLE;
              }
              if (game.GetTeamList().size() == 11) {
                color = ChatColor.DARK_GRAY;
              }
              if (game.GetTeamList().size() == 12) {
                color = ChatColor.DARK_RED;
              }
            }
          }
        }
      }
      GameTeam newTeam = new GameTeam(game, team, color, (ArrayList)game.WorldData.SpawnLocs.get(team));
      game.AddTeam(newTeam);
    }
    game.RestrictKits();
    

    game.ParseData();
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void TeamScoreboardCreation(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Recruit) {
      return;
    }
 
  }
  
  public void TeamPreferenceJoin(Game game)
  {
    for (GameTeam team : game.GetTeamPreferences().keySet())
    {
      Iterator<Player> queueIterator = ((ArrayList)game.GetTeamPreferences().get(team)).iterator();
      while (queueIterator.hasNext())
      {
        Player player = (Player)queueIterator.next();
        if (!game.CanJoinTeam(team)) {
          break;
        }
        queueIterator.remove();
        if (!game.IsPlaying(player)) {
          PlayerAdd(game, player, team);
        } else {
          game.SetPlayerTeam(player, team);
        }
      }
    }
  }
  
  public void TeamPreferenceSwap(Game game)
  {
    Iterator<Player> queueIterator;
    for (Iterator localIterator1 = game.GetTeamPreferences().keySet().iterator(); localIterator1.hasNext(); )
    {
      GameTeam team = (GameTeam)localIterator1.next();
      
      

      for(queueIterator = team.GetPlayers(false).iterator(); queueIterator.hasNext();){
      Player player = (Player)queueIterator.next();
      
      GameTeam currentTeam = game.GetTeam(player);
      if (currentTeam != null) {
        if (team == currentTeam) {
          queueIterator.remove();
        } else {
          for (Player other : team.GetPlayers(false)) {
            if (!other.equals(player))
            {
              GameTeam otherPref = game.GetTeamPreference(other);
              if (otherPref != null) {
                if (otherPref.equals(currentTeam))
                {
                  UtilPlayer.message(player, F.main("Team", Colorizer.Color(this.Manager.getConfig().getString("messages.changed"))+ F.elem(new StringBuilder().append(team.GetColor()).append(other.getName()).toString()) + "."));
                  UtilPlayer.message(other, F.main("Team", Colorizer.Color(this.Manager.getConfig().getString("messages.changed")) + F.elem(new StringBuilder().append(currentTeam.GetColor()).append(player.getName()).toString()) + "."));
                  

                  queueIterator.remove();
                  game.SetPlayerTeam(player, team);
                  game.SetPlayerTeam(other, currentTeam);
                }
              }
            }
          }
        }
      }
    }
    }
  }
  
  public void TeamDefaultJoin(Game game)
  {
    Player[] arrayOfPlayer;
    int j = (arrayOfPlayer = UtilServer.getPlayers()).length;
    for (int i = 0; i < j; i++)
    {
      Player player = arrayOfPlayer[i];
      if (player.isDead())
      {
        player.sendMessage(F.main("Afk Monitor", Colorizer.Color(this.Manager.getConfig().getString("messages.afk"))));
        this.Manager.GetPortal().SendPlayerToServer(player, this.Manager.getConfig().getString("lobby","lobby"));
      }
      else if (!game.IsPlaying(player))
      {
        PlayerAdd(game, player, null);
      }
    }
  }
  
  @EventHandler
  public void TeamQueueSizeUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    for (GameTeam team : game.GetTeamList())
    {
      int amount = 0;
      if (game.GetTeamPreferences().containsKey(team)) {
        amount = ((ArrayList)game.GetTeamPreferences().get(team)).size();
      }
      if (team.GetTeamEntity() != null) {
        if (game.GetCountdown() == -1) {
          team.GetTeamEntity().setCustomName(team.GetFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " in queue");
        } else {
          team.GetTeamEntity().setCustomName(team.GetPlayers(false).size() + " Players  " + team.GetFormattedName() + " Team" + ChatColor.RESET + "  " + amount + " in queue");
        }
      }
    }
  }
  
  public boolean PlayerAdd(Game game, Player player, GameTeam team)
  {
    if (team == null) {
      team = game.ChooseTeam(player);
    }
    if (team == null) {
      return false;
    }
    game.SetPlayerTeam(player, team);
    

    player.setGameMode(GameMode.SURVIVAL);
    
    return true;
  }
  
  @EventHandler(priority=EventPriority.LOWEST)
  public void PlayerPrepare(GameStateChangeEvent event)
  {
    final Game game = event.GetGame();
    if (event.GetState() != Game.GameState.Prepare) {
      return;
    }
    ArrayList<Player> players = game.GetPlayers(true);
    for (int i = 0; i < players.size(); i++)
    {
      final Player player = (Player)players.get(i);
      
      final GameTeam team = game.GetTeam(player);
      
      UtilServer.getServer().getScheduler().runTaskLater(this.Manager.GetPlugin(), new Runnable()
      {
        @Override
        public void run()
        {
          team.SpawnTeleport(player);
          
          GameManager.this.Manager.Clear(player);
          UtilInv.Clear(player);
          
          game.ValidateKit(player, game.GetTeam(player));
          if (game.GetKit(player) != null) {
            game.GetKit(player).ApplyKit(player);
          }
          PlayerPrepareTeleportEvent playerStateEvent = new PlayerPrepareTeleportEvent(game, player);
          UtilServer.getServer().getPluginManager().callEvent(playerStateEvent);
        }
      }, i);
    }
    UtilServer.getServer().getScheduler().runTaskLater(this.Manager.GetPlugin(), new Runnable()
    {
      @Override
      public void run()
      {
        game.AnnounceGame();
        
        game.StartPrepareCountdown();

        GamePrepareCountdownCommence event = new GamePrepareCountdownCommence(game);
        UtilServer.getServer().getPluginManager().callEvent(event);
      }
    }, players.size());
  }
  
  @EventHandler
  public void PlayerTeleportOut(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Dead) {
      return;
    }
    Player[] players = UtilServer.getPlayers();
    for (int i = 0; i < players.length; i++)
    {
      final Player player = players[i];
      
      UtilServer.getServer().getScheduler().runTaskLater(this.Manager.GetPlugin(), new Runnable()
      {
        @Override
        public void run()
        {
          GameManager.this.Manager.Clear(player);
          UtilInv.Clear(player);
          
          GameManager.this.Manager.GetBuffer().EndBuffer(player, Buffer.BufferType.CLOAK, "Spectator");
          
          player.eject();
          player.leaveVehicle();
          player.teleport(GameManager.this.Manager.GetLobby().GetSpawn());
        }
      }, i);
    }
  }
  
  @EventHandler
  public void disguiseClean(GameStateChangeEvent event)
  {
    if (event.GetState() != Game.GameState.Dead) {
      return;
    }
for(Player p : Bukkit.getOnlinePlayers()){
    if(DisguiseAPI.isDisguised(p)){
        DisguiseAPI.undisguiseToAll(p);
    }
}
  }
  
  @EventHandler
  public void WorldFireworksUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.FASTEST) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    if (game.GetState() != Game.GameState.End) {
      return;
    }
    Color color = Color.GREEN;
    if (game.WinnerTeam != null) {
      if (game.WinnerTeam.GetColor() == ChatColor.RED) {
        color = Color.RED;
      } else if (game.WinnerTeam.GetColor() == ChatColor.AQUA) {
        color = Color.BLUE;
      } else if (game.WinnerTeam.GetColor() == ChatColor.YELLOW) {
        color = Color.YELLOW;
      } else {
        color = Color.LIME;
      }
    }
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void EndUpdate(UpdateEvent event)
  {
    if (event.getType() != UpdateType.SEC2) {
      return;
    }
    Game game = this.Manager.GetGame();
    if (game == null) {
      return;
    }
    game.EndCheck();
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void EndStateChange(PlayerStateChangeEvent event)
  {
    event.GetGame().EndCheck();
  }
}
