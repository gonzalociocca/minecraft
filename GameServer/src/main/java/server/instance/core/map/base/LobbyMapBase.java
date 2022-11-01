package server.instance.core.map.base;

import server.common.CustomLocation;
import server.common.TagEnum;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

/**
 * Created by noname on 24/4/2017.
 */
public class LobbyMapBase extends MapCipherBase {

    public ArrayList<Location> getSigns(){
        ArrayList<Location> list = new ArrayList();
        World world = getWorld();
        for(CustomLocation loc : getCustomLocationList(TagEnum.Signs.getID())){
            list.add(new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
        }

        return list;
    }
}
