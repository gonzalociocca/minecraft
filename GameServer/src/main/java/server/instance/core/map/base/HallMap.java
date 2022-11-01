package server.instance.core.map.base;

import server.common.CustomLocation;
import server.common.TagEnum;
import server.instance.core.map.misc.BlockParseData;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

/**
 * Created by noname on 6/4/2017.
 */
public class HallMap extends LobbyMapBase {

    public HallMap(){
        super();

        _parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 68, -1, BlockParseData.DataType.LocationList));
        _parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 63, -1, BlockParseData.DataType.LocationList));
    }

    @Override
    public ArrayList<Location> getSigns(){
        ArrayList<Location> list = new ArrayList();
        World world = getWorld();
        for(CustomLocation loc : getCustomLocationList(TagEnum.Signs.getID())){
            list.add(new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
        }

        return list;
    }
}
