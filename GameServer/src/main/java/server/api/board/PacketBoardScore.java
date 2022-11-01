package server.api.board;


import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import server.util.UtilReflection;

public class PacketBoardScore {

    PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();

    public String getScoreName() {
        return (String) UtilReflection.get(packet, "a");
    }

    public String getId() {
        return (String) UtilReflection.get(packet, "b");
    }

    public int getScore() {
        return (int) UtilReflection.get(packet, "c");
    }

    public PacketPlayOutScoreboardScore.EnumScoreboardAction getAction() {
        return (PacketPlayOutScoreboardScore.EnumScoreboardAction) UtilReflection.get(packet, "d");
    }

    public void setScoreName(String str) {
        UtilReflection.set(packet, "a", str);
    }

    public void setId(String str) {
        UtilReflection.set(packet, "b", str);
    }

    public void setScore(int newId) {
        UtilReflection.set(packet, "c", newId);
    }

    public void setAction(PacketPlayOutScoreboardScore.EnumScoreboardAction value) {
        UtilReflection.set(packet, "d", value);
    }

    public Packet get() {
        return packet;
    }

}
