package server.instance.loader.SkyWars;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import server.common.CustomLocation;
import server.common.TagEnum;
import server.instance.core.map.base.MapCipherBase;
import server.instance.core.map.misc.BlockParseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 27/3/2017.
 */
public class SkyWarsSoloMap extends MapCipherBase {

    public SkyWarsSoloMap(){
        super();
        _parseItems.add(new BlockParseData(TagEnum.SoloSpawn.getID(), Material.BEACON.getId(), -1, BlockParseData.DataType.LocationList));
        _parseItems.add(new BlockParseData(TagEnum.ChestTag.getID(), Material.CHEST.getId(), -1, BlockParseData.DataType.LocationList));
        _parseItems.add(new BlockParseData(TagEnum.EnderChestTag.getID(), Material.ENDER_CHEST.getId(), -1, BlockParseData.DataType.LocationList));

    }

    public List<Location> getNormalChests(){
        List<Location> list = new ArrayList();
        World world = getWorld();
        for(CustomLocation loc : getCustomLocationList(TagEnum.ChestTag.getID())){
            list.add(new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
        }
        return list;
    }

    public List<Location> getMiddleChests(){
        List<Location> list = new ArrayList();
        World world = getWorld();
        for(CustomLocation loc : getCustomLocationList(TagEnum.EnderChestTag.getID())){
            list.add(new Location(world, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
        }
        return list;
    }

}
