package server.instance.core.map.base;

import server.common.CustomLocation;
import server.common.TagEnum;
import server.instance.core.map.misc.BlockParseData;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Created by noname on 6/4/2017.
 */
public class LobbyMap extends LobbyMapBase {


    public LobbyMap(){
        super();

        _parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 68, -1, BlockParseData.DataType.LocationList));

        _parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 63, -1, BlockParseData.DataType.LocationList));

        _parseItems.add(new BlockParseData(TagEnum.SingleLocation1.getID(), Material.ENDER_CHEST.getId(), -1, BlockParseData.DataType.LocationList));

    }

    public Location getMisteryBox(){
        CustomLocation loc = getCustomLocationList(TagEnum.SingleLocation1.getID()).get(0);
        return new Location(getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
