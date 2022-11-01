package server.util;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import server.api.BoardAPI;
import server.api.board.PlayerBoard;
import server.common.Code;
import server.common.Rank;
import server.instance.GameServer;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by noname on 12/4/2017.
 */
public class UtilScoreboard {
    public static void updateScoreOnBoard(Scoreboard scoreboard, Objective objective, Map<Integer, String> data, String value, int slotid){
        String str = data.get(slotid);
        String newStr = value;
        boolean different = str == null || !str.equals(newStr);
        if(different){
            if (str != null) {
                scoreboard.resetScores(str);
            }
            objective.getScore(newStr).setScore(slotid);
            data.put(slotid, newStr);
        }
    }
    public static void clearSideBoard(Scoreboard scoreboard, Map<Integer, String> data){
        for(Iterator<Map.Entry<Integer, String>> it = data.entrySet().iterator(); it.hasNext();){
            Map.Entry<Integer, String> entry = it.next();
            scoreboard.resetScores(entry.getValue());
            it.remove();
        }
    }
    public static Objective getSideBoardObjective(Scoreboard scoreboard, String defaultDisplay){
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = scoreboard.registerNewObjective("stats", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(defaultDisplay);
        }
        return objective;
    }
    public static void fillScoreboard(Scoreboard scoreboard, GameServer game){
        for (int a = 0; a < 48; a++) {
            String color = Code.getColorOfInteger(a);
            Team team = scoreboard.registerNewTeam(color);
            team.setPrefix(color);
            team.setDisplayName(color);
            team.setAllowFriendlyFire(game.DamageTeamSelf);
            team.setCanSeeFriendlyInvisibles(true);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }
        Team team = scoreboard.registerNewTeam("Spectator");
        team.setPrefix(UtilMsg.Gray);
        team.setDisplayName("Spectator");
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);

    }
    public static void fillScoreboard(Scoreboard scoreboard){
        for(Rank.RankType rankType : Rank.RankType.values()){
            Team team = scoreboard.registerNewTeam(rankType.getName());
            team.setPrefix(rankType.getScoreboardPrefix());
            team.setDisplayName(rankType.getColor());
            team.setAllowFriendlyFire(true);
            team.setCanSeeFriendlyInvisibles(true);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }
    }

    public static void clearServer(GameServer gameServer, boolean alsoTeams) {
        for (Player player : gameServer.getPlayers(false)) {
            PlayerBoard playerBoard = BoardAPI.getBoard(player);
            Scoreboard scoreboard = playerBoard.getBoard();
            Map<Integer, String> data = playerBoard.getData();
            if (alsoTeams) {
                for (Team te : scoreboard.getTeams()) {
                    for (String str : te.getEntries()) {
                        te.removeEntry(str);
                    }
                }
            }
            UtilScoreboard.clearSideBoard(scoreboard, data);
        }
    }

}
