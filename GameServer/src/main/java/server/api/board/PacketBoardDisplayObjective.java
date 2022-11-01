package server.api.board;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import server.api.board.misc.EnumBoardDisplayMode;
import server.util.UtilReflection;

public class PacketBoardDisplayObjective {
    PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

    public void setMode(EnumBoardDisplayMode enumBoardDisplayMode){
        UtilReflection.set(packet, "a", enumBoardDisplayMode.ordinal());
    }

    public void setId(String str){
        UtilReflection.set(packet, "b", str);
    }

    public Packet get(){
        return packet;
    }
}
