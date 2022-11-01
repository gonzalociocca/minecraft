package server.api.board;


import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import server.util.UtilReflection;

public class PacketBoardTeam {

    PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

    public void setOrigin(String str){
        UtilReflection.set(packet, "a", str);
    }

    public void setValue(String str){
        UtilReflection.set(packet, "b", str);
    }

    public void setId(int newId){
        UtilReflection.set(packet, "c", newId);
    }

    public void setAction(PacketPlayOutScoreboardScore.EnumScoreboardAction value){
        UtilReflection.set(packet, "d", value);
    }

    public Packet get(){
        return packet;
    }

}
