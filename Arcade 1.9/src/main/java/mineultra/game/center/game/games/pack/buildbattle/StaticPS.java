package mineultra.game.center.game.games.pack.buildbattle;

import mineultra.core.common.util.UtilParticle.PCType;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class StaticPS {
    Player owner = null;
    Location locplay = null;
    PCType type = null;
    public StaticPS(Player pe, Location loc, PCType ptype){
    owner = pe;
    locplay = loc;
    type = ptype;
    }
    
    public void rePlay(){

    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type.getType(),false,(float)locplay.getX(),(float)locplay.getY(),(float)locplay.getZ(),(float)1F,(float)1F,(float)1F,0,1);
    for(Player p : Bukkit.getOnlinePlayers()){
       if(p.getWorld()==locplay.getWorld()){
           if(p.getLocation().distanceSquared(locplay) < 100){
              ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
              
           }
       }
    }
    }
    
    public PCType getType(){
        return type;
    }
    public Location getLocation(){
        return locplay;
    }
    public Player getOwner(){
        return owner;
    }
    
    public double getX(){
        return locplay.getX();
    }
    public double getY(){
        return locplay.getY();
    }
    public double getZ(){
        return locplay.getZ();
    }
    
}
