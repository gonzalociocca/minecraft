package me.gonzalociocca.minigame.map.Cipher;

import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.TagEnum;
import org.bukkit.Material;

import java.util.*;

/**
 * Created by noname on 27/3/2017.
 */
public class SkyWarsSoloMap extends MapCipherBase {

    public SkyWarsSoloMap(){
        super();
        mapLocationList.put(TagEnum.ChestTag.getID(), new ArrayList());
        parseItems.add(new BlockParseData(TagEnum.ChestTag.getID(), 54, -1, BlockParseData.DataType.LocationList));

    }


    @Override
    public MapCipherBase clone() {
        SkyWarsSoloMap cloned = new SkyWarsSoloMap();
        cloned.loadFromBase(this);
        return cloned;
    }


}
