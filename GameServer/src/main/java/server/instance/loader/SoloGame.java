package server.instance.loader;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import server.api.BoardAPI;
import server.api.board.PlayerBoard;
import server.common.event.GameStateChangeEvent;
import server.common.event.PlayerStateChangeEvent;
import server.instance.GameServer;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.util.UtilMsg;
import server.util.UtilScoreboard;

import java.util.ArrayList;
import java.util.Map;

public abstract class SoloGame extends GameServer {
    protected ArrayList<Player> _places = new ArrayList<Player>();

    public SoloGame() {
        super();
    }

    @Override
    public void resetGame() {
        super.resetGame();
        _places.clear();
    }

    @Override
    public void onGameStateChange(final GameStateChangeEvent event) {
        super.onGameStateChange(event);
        if (event.getState() == GameState.Recruit) {
            getLogin().getTeamList().get(0).setName("Players");
        }
    }

    @Override
    public void onPlayerStateChange(final PlayerStateChangeEvent event) {
        super.onPlayerStateChange(event);
        if (event.getState() == GameTeam.PlayerState.OUT) {
            _places.add(0, event.getPlayer());
        }
    }

    @Override
    public void endCheck() {
        if (!this.isLive()) {
            return;
        }
        if (this.getPlayers(true).size() == 1) {
            getLogin().removePlayer(this, getPlayers(true).get(0), false, false);
            return;
        }
        if (this.getPlayers(true).size() <= 0) {
            this.AnnounceEnd(this._places);
            if (this._places.size() >= 1) {
                getGems().addGems(this._places.get(0), 20.0, UtilMsg.FirstPlaceTag, false);
            }
            if (this._places.size() >= 2) {
                getGems().addGems(this._places.get(1), 15.0, UtilMsg.SecondPlaceTag, false);
            }
            if (this._places.size() >= 3) {
                getGems().addGems(this._places.get(2), 10.0, UtilMsg.ThirdPlaceTag, false);
            }
            for (final Player player : this.getPlayers(false)) {
                if (player.isOnline()) {
                    getGems().addGems(player, 10.0, "Participation", false);
                }
            }
            this.setState(GameState.End);
        }
    }

    public ArrayList<Player> getPlaces() {
        return this._places;
    }


    @Override
    public void updateScoreboard(GameTeam team) {
        for(Player player : team.getPlayers(false)){
            PlayerBoard playerBoard = BoardAPI.getBoard(player);
            Scoreboard scoreboard = playerBoard.getBoard();
            Map<Integer, String> data = playerBoard.getData();
            Objective objective = UtilScoreboard.getSideBoardObjective(scoreboard, UtilMsg.Green + getTitle());

            if (getState().equals(GameState.Recruit) || getState().equals(GameState.Prepare)) {
                String gameState = CanStartPrepareCountdown() && getAlternativeCountdown() != -1 ? UtilMsg.ScoreboardPrepare + getAlternativeCountdown() + "s"
                        : getCountdown() != -1 ? UtilMsg.ScoreboardStarting + getCountdown() + "s" : UtilMsg.ScoreboardRecruiting;
                String playersSize = getPlayersCount(true) + "/" + getMaxPlayers();

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.White, 9);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.ScoreboardPlayers + playersSize, 8);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.Purple, 7);

                UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + gameState, 6);
            }

            UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, "  ", 5);

            UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.ScoreboardMap + getMap().getDisplay(), 4);
            UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.ScoreboardMode + "Solo", 3);

            UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " ", 2);

            UtilScoreboard.updateScoreOnBoard(scoreboard, objective, data, " " + UtilMsg.Website, 1);
        }
    }
}
