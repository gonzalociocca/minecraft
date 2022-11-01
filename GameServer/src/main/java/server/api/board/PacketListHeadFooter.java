package server.api.board;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import server.util.UtilReflection;

/**
 * Created by noname on 26/6/2017.
 */
public class PacketListHeadFooter {
    PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

    public void setHeader(String str){
        UtilReflection.set(packet, "a", new ChatMessage(str));
    }

    public void setFooter(String str){
        UtilReflection.set(packet, "b", new ChatMessage(str));
    }

    public PacketPlayOutPlayerListHeaderFooter get(){
        return packet;
    }
}
