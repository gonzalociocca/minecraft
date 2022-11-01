package me.gonzalociocca.minigame.map.Cipher;

import me.gonzalociocca.minigame.misc.TagEnum;
import org.bukkit.Material;

import java.util.ArrayList;

/**
 * Created by noname on 6/4/2017.
 */
public class LobbyMap extends MapCipherBase {

    public LobbyMap(){
        super();
        parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 68, -1, BlockParseData.DataType.LocationList));

        parseItems.add(new BlockParseData(TagEnum.Signs.getID(), 63, -1, BlockParseData.DataType.LocationList));

        parseItems.add(new BlockParseData(TagEnum.SingleLocation1.getID(), Material.ENDER_CHEST.getId(), -1, BlockParseData.DataType.Location));

    }

    @Override
    public MapCipherBase clone() {
        LobbyMap cloned = new LobbyMap();
        cloned.loadFromBase(this);
        return cloned;
    }
}
