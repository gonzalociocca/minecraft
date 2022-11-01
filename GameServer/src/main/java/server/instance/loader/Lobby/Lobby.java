package server.instance.loader.Lobby;

import com.google.gson.annotations.Expose;
import org.bukkit.Location;
import server.common.CustomLocation;
import server.instance.GameServer;

import java.util.ArrayList;

public class Lobby extends GameServer {
    @Expose
    public String testValue = "Im Testing Bitch on Lobby";

    @Expose
    CustomLocation spawnLocation = null;
    /*
    * todo: Lobby
    * Scoreboard
    * SpawnPoint
    * Menu Linking to SubServers
    * Signs Linking to SubServers
    *
    * */
    @Override
    public void endCheck() {

    }

    @Override
    public ArrayList<Location> getSpawns() {
        ArrayList<Location> spawnList = super.getSpawns();
        if(spawnList != null && spawnList.isEmpty() && getMap() != null){
            CustomLocation loc = getMap().getBuildLocation();
            spawnList.add(new Location(getMap().getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
        }
        return spawnList;
    }
}
