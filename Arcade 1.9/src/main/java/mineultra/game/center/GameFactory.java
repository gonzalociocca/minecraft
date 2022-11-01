package mineultra.game.center;

import mineultra.game.center.game.Game;
import mineultra.game.center.game.games.survivalgames.SurvivalGames;
import org.bukkit.ChatColor;
import java.util.HashMap;
import mineultra.game.center.game.games.dtnexus.DTN;
import mineultra.game.center.game.games.oitq.Quiver;
import mineultra.game.center.game.games.pack.*;
import mineultra.game.center.game.games.pack.buildbattle.BuildBattle;
import mineultra.game.center.game.games.pack.turboracers.TurboRacers;
import mineultra.game.center.game.games.skywars.SWTeams;
import mineultra.game.center.game.games.skywars.SkyWars;
import mineultra.game.center.game.games.smash.SmashGame;
import mineultra.game.center.game.games.spleef.Spleef;
import mineultra.game.center.game.games.zombiesurvival.ZombieSurvival;
public class GameFactory
{
    private final centerManager _manager;
    
    public GameFactory(final centerManager gameManager) {
        super();
        this._manager = gameManager;
    }
    
    public Game CreateGame(final GameType gameType, final HashMap<String, ChatColor> pastTeams) {
        if (gameType == GameType.DTN) {
            return new DTN(this._manager);
        }
        if (gameType == GameType.SWTeams) {
            return new SWTeams(this._manager);
        }
        if(gameType == GameType.Smash){
            return new SmashGame(this._manager);
        }
        if(gameType  == GameType.Quiver){
            return new Quiver(this._manager);
        }
        if(gameType == GameType.Spleef){
            return new Spleef(this._manager);
        }
        if(gameType == GameType.ZombieSurvival){
            return new ZombieSurvival(this._manager);
        }
        if(gameType == GameType.SkyWars){
            return new SkyWars(this._manager);
        }
        if(gameType == GameType.BuildBattle){
            return new BuildBattle(this._manager);
        }
        if(gameType == GameType.TurboRacers){
            return new TurboRacers(this._manager);
        }
        if(gameType == GameType.SurvivalGames){
            return new SurvivalGames(this._manager);
        }
        return null;
    }
}
