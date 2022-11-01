package mineultra.game.center;

import mineultra.game.center.game.Game;
import org.bukkit.ChatColor;
import java.util.HashMap;
import mineultra.game.center.game.games.pack.*;
import mineultra.game.center.game.games.pack.buildbattle.BuildBattle;
import mineultra.game.center.game.games.pack.turboracers.TurboRacers;
public class GameFactory
{
    private final centerManager _manager;
    
    public GameFactory(final centerManager gameManager) {
        super();
        this._manager = gameManager;
    }
    
    public Game CreateGame(final GameType gameType, final HashMap<String, ChatColor> pastTeams) {
        if(gameType == GameType.BuildBattle){
            return new BuildBattle(this._manager);
        }
        if(gameType == GameType.TurboRacers){
            return new TurboRacers(this._manager);
        }
        return null;
    }
}
