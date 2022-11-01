package mineultra.game.center.game;

import mineultra.core.common.util.C;
import mineultra.game.center.events.PlayerStateChangeEvent;
import mineultra.game.center.game.games.oitq.QuiverScore;
import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import mineultra.game.center.events.GameStateChangeEvent;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.GameType;
import mineultra.game.center.centerManager;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;

public abstract class SoloGame extends Game
{
    protected ArrayList<Player> _places;
    
    public SoloGame(final centerManager manager, final GameType gameType, final Kit[] kits, final String[] gameDesc) {
        super(manager, gameType, kits, gameDesc);
        this._places = new ArrayList<Player>();
    }
    
    @EventHandler
    public void CustomTeamGeneration(final GameStateChangeEvent event) {
        if (event.GetState() != Game.GameState.Recruit) {
            return;
        }
        this.GetTeamList().get(0).SetColor(ChatColor.YELLOW);
        this.GetTeamList().get(0).SetName("Players");
    }
    
    @EventHandler
    public void EndStateChange(final PlayerStateChangeEvent event) {
        if (event.GetState() == GameTeam.PlayerState.OUT) {
            if (!this._places.contains(event.GetPlayer())) {
                this._places.add(0, event.GetPlayer());
            }
            else {
                this._places.remove(event.GetPlayer());
            }
        }
    }
    
    @Override
    public void EndCheck() {
        if (!this.IsLive()) {
            return;
        }
        if (this.GetPlayers(true).size() == 1) {
            this.SetPlayerState(this.GetPlayers(true).get(0), GameTeam.PlayerState.OUT);
            return;
        }
        if (this.GetPlayers(true).size() <= 0) {
            this.AnnounceEnd(this._places);
            if (this._places.size() >= 1) {
                this.AddGems(this._places.get(0), 20.0, "1er lugar", false);
                db.getPlayerData(this._places.get(0)).addFirstPlace(1);
            }
            if (this._places.size() >= 2) {
                this.AddGems(this._places.get(1), 15.0, "2do lugar", false);
                db.getPlayerData(this._places.get(1)).addSecondPlace(1);
            }
            if (this._places.size() >= 3) {
                this.AddGems(this._places.get(2), 10.0, "3er lugar", false);
                db.getPlayerData(this._places.get(2)).addThirdPlace(1);
            }
            for (final Player player : this.GetPlayers(false)) {
                if (player.isOnline()) {
                    this.AddGems(player, 10.0, "Participacion", false);
                }
            }
            this.SetState(Game.GameState.End);
        }
    }
    
    @EventHandler
    @Override
    public void ScoreboardUpdate(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        if (this.GetTeamList().isEmpty()) {
            return;
        }
        final GameTeam team = this.GetTeamList().get(0);
        this.GetObjectiveSide().getScore(team.GetColor() + "Vivos").setScore(team.GetPlayers(true).size());
        this.GetObjectiveSide().getScore(String.valueOf(C.cRed) + "Muertos").setScore(team.GetPlayers(false).size() - team.GetPlayers(true).size());
    }
    
    public ArrayList<Player> GetPlaces() {
        return this._places;
    }
    
    public int GetScoreboardScore(final Player player) {
        return 0;
    }
}
