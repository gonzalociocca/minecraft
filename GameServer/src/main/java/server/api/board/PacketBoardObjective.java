package server.api.board;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import server.api.board.misc.EnumBoardObjectiveMode;
import server.util.UtilReflection;

public class PacketBoardObjective {

    PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

    public String getId(){
        return String.valueOf(UtilReflection.get(packet, "a"));
    }

    public String getDisplayName(){
        return String.valueOf(UtilReflection.get(packet, "b"));
    }

    public IScoreboardCriteria.EnumScoreboardHealthDisplay getHealth(){
        return (IScoreboardCriteria.EnumScoreboardHealthDisplay)UtilReflection.get(packet, "c");
    }

    public int getMode(){
        return (Integer)UtilReflection.get(packet, "d");
    }

    public void setId(String str){
        UtilReflection.set(packet, "a", str);
    }

    public void setDisplayName(String str){
        UtilReflection.set(packet, "b", str);
    }

    public void setHealth(IScoreboardCriteria.EnumScoreboardHealthDisplay value){
        UtilReflection.set(packet, "c", value);
    }

    /**
     * @param newId 0=Create 1=Remove 2=Update
     */

    public void setMode(EnumBoardObjectiveMode enumBoardObjectiveMode){
        UtilReflection.set(packet, "d", enumBoardObjectiveMode.ordinal());
    }

    public Packet get(){
        return packet;
    }

}
