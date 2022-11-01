package mineultra.game.center.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import mineultra.core.common.util.UtilMath;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.centerManager;
import mineultra.game.center.GameType;
import mineultra.game.center.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class GameCreationManager
  implements Listener
{
  centerManager Manager = null;
  private final ArrayList<Game> _ended = new ArrayList();

  private GameType _nextGame = null;
  private HashMap<String, ChatColor> _nextGameTeams = null;

  private String _lastMap = "";
  private final ArrayList<GameType> _lastGames = new ArrayList();

  
  public GameCreationManager(final centerManager manager)
  {

      Manager = manager;

    Manager.GetPluginManager().registerEvents(GameCreationManager.this, Manager.GetPlugin());        
        


  }

  public String GetLastMap()
  {
    return _lastMap;
  }

  public void SetLastMap(String file)
  {
    _lastMap = file;
  }

  @EventHandler
  public void NextGame(UpdateEvent event)
  {
    if (event.getType() != UpdateType.TICK) {
      return;
    }
    if (Manager.GetGameList().isEmpty()) {
      return;
    }
    while (_lastGames.size() > Manager.GetGameList().size() - 1) {
      _lastGames.remove(_lastGames.size() - 1);
    }
    if ((Manager.GetGame() == null) && (_ended.isEmpty()))
    {
      CreateGame(null);
    }

    if (Manager.GetGame() != null)
    {
      if (Manager.GetGame().GetState() == Game.GameState.Dead)
      {
        HandlerList.unregisterAll(Manager.GetGame());

        _ended.add(Manager.GetGame());



        Manager.SetGame(null);
      }

    }
    
    
    Iterator gameIterator = _ended.iterator();

    while (gameIterator.hasNext())
    {
      Game game = (Game)gameIterator.next();

      HandlerList.unregisterAll(game);

      if (game.WorldData == null)
      {
        gameIterator.remove();
      }
      else if (game.WorldData.World == null)
      {
        gameIterator.remove();
      }
      else
      {
        if (UtilTime.elapsed(game.GetStateTime(), 10000L))
        {
          for (Player player : game.WorldData.World.getPlayers()) {

          }
        }

        if (game.WorldData.World.getPlayers().isEmpty())
        {
          game.WorldData.Uninitialize();
          game.WorldData = null;
        }
      }
    }
  }
  
public void blockgame(){



        HandlerList.unregisterAll(Manager.GetGame());

        _ended.add(Manager.GetGame());

        Manager.SetGame(null);
      

    
    
    
    Iterator gameIterator = _ended.iterator();

    while (gameIterator.hasNext())
    {
      Game game = (Game)gameIterator.next();

      HandlerList.unregisterAll(game);

      if (game.WorldData == null)
      {
        gameIterator.remove();
      }
      else if (game.WorldData.World == null)
      {
        gameIterator.remove();
      }
      else
      {
        if (UtilTime.elapsed(game.GetStateTime(), 10000L))
        {
          for (Player player : game.WorldData.World.getPlayers()) {

          }
        }

        if (game.WorldData.World.getPlayers().isEmpty())
        {
          game.WorldData.Uninitialize();
          game.WorldData = null;
        }
      }
    }
}

  private void CreateGame(GameType gameType)
  {
    Manager.GetDamage().DisableDamageChanges = true;
    Manager.GetCreature().SetDisableCustomDrops(false);
    Manager.GetDamage().SetEnabled(true);

    Manager.GetExplosion().SetRegenerate(true);        
    

    Manager.GetExplosion().SetTNTSpread(true);

    HashMap pastTeams = null;

    if ((_nextGame != null) && (_nextGameTeams != null))
    {
      gameType = _nextGame;
      pastTeams = _nextGameTeams;

      _nextGame = null;
      _nextGameTeams = null;
    }

    if (gameType == null)
    {
      for (int i = 0; i < 50; i++)
      {
        gameType = (GameType)Manager.GetGameList().get(UtilMath.r(Manager.GetGameList().size()));

        if (!_lastGames.contains(gameType)) {
          break;
        }
      }
    }
    _lastGames.add(0, gameType);

    Manager.SetGame(Manager.GetGameFactory().CreateGame(gameType, pastTeams));

    if (Manager.GetGame() == null)
    {
      return;
    }

    Manager.GetLobby().DisplayNext(Manager.GetGame(), pastTeams);

    UtilServer.getServer().getPluginManager().registerEvents(Manager.GetGame(), Manager.GetPlugin());
  }
}