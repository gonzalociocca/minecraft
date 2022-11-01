package server.util;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import server.api.board.PacketBoardDisplayObjective;
import server.api.board.PacketBoardObjective;
import server.api.board.PacketBoardScore;
import server.api.board.misc.EnumBoardDisplayMode;
import server.api.board.misc.EnumBoardObjectiveMode;

/**
 * Created by noname on 26/6/2017.
 */
public class UtilBoard {

    public static PacketBoardObjective createObjective(
            String id, String displayName, EnumBoardObjectiveMode mode, IScoreboardCriteria.EnumScoreboardHealthDisplay health){
        PacketBoardObjective packetBoardObjective = new PacketBoardObjective();
        packetBoardObjective.setId(id);
        packetBoardObjective.setDisplayName(displayName); // Title
        packetBoardObjective.setHealth(health);
        packetBoardObjective.setMode(mode);

        return packetBoardObjective;
    }

    public static PacketBoardDisplayObjective createObjectiveDisplay(String id, EnumBoardDisplayMode mode){
        PacketBoardDisplayObjective packetBoardDisplayObjective = new PacketBoardDisplayObjective();
        packetBoardDisplayObjective.setId(id);
        packetBoardDisplayObjective.setMode(mode);
        return packetBoardDisplayObjective;
    }

    public static PacketBoardScore createScore(String id, int scoreId, PacketPlayOutScoreboardScore.EnumScoreboardAction action, String scoreName){
        PacketBoardScore packetBoardScore = new PacketBoardScore();
        packetBoardScore.setId(id);
        packetBoardScore.setScore(scoreId);
        packetBoardScore.setAction(action);
        packetBoardScore.setScoreName(scoreName);
        return packetBoardScore;
    }

}
