package me.gonzalociocca.minigame.map.Cipher;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.TagEnum;
import me.gonzalociocca.minigame.misc.SchematicClipboard;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 Fast Map Options:
 Small Map with Small Build Size - Fastest
 Small Schematic with Big Map and Per Location Clear - Fast Sync
 Small Schematic with big Build Size and Change Location Every Build - Very Fast
 **/

public abstract class MapCipherBase {

    public static int nextX = 0;
    public static int nextZ = 0;

    public boolean isFullyLoaded = false;
    public boolean isLoading = false;

    public File mapfile;
    public long buildStartTime;
    public SchematicClipboard clipboard = null;
    public CustomLocation pasteLocation;

    public List<BlockParseData> parseItems = new ArrayList();

    public HashMap<String, CustomLocation> mapLocation = new HashMap();
    public HashMap<String, ArrayList<CustomLocation>> mapLocationList = new HashMap();
    public HashMap<String, String> nameString = new HashMap();

    public String worldName = Core.getWorldGameName();

    public MapCipherBase(){
        mapLocationList.put(TagEnum.PlayerLocationTag.getID(), new ArrayList());
        parseItems.add(new BlockParseData(TagEnum.PlayerLocationTag.getID(), 95, 7, BlockParseData.DataType.LocationList));

        mapLocationList.put(TagEnum.SpectatorLocationTag.getID(), new ArrayList());
        parseItems.add(new BlockParseData(TagEnum.SpectatorLocationTag.getID(), 95, 15, BlockParseData.DataType.LocationList));

        parseItems.add(new BlockParseData(TagEnum.Hall.getID(), Material.BEACON.getId(), -1, BlockParseData.DataType.Location));

        nameString.put(TagEnum.MapNameTag.getID(), "");
    }

    public CustomLocation getCustomLocation(String id){
        return mapLocation.get(id);
    }

    public ArrayList<CustomLocation> getCustomLocationList(String id){
        return mapLocationList.get(id);
    }

    public String getName() {
        return nameString.get(TagEnum.MapNameTag.getID());
    }

    public boolean loadFromSchematic(File schematic, String name) {
        mapfile = schematic;
        nameString.put(TagEnum.MapNameTag.getID(), name);
        SchematicClipboard cc = Code.loadSchematicFromFile(mapfile);

        cc.setOrigin(new Vector(0, 0, 0));
        cc.setOffset(new Vector(0, 0, 0));
        if (cc != null) {

            for (int x = 0; x < cc.getWidth(); x++) {
                for (int y = 0; y < cc.getHeight(); y++) {
                    for (int z = 0; z < cc.getLength(); z++) {
                        BaseBlock block = cc.getBlock(x,y,z);
                        if (block != null && !block.isAir()) {
                            for(BlockParseData parse : parseItems){
                                if(block.getType() == parse.getID() && (parse.getData()== -1 || block.getData() == parse.getData())){
                                    if(parse.getType().equals(BlockParseData.DataType.Location)){
                                        mapLocation.put(parse.getIdentifier(), new CustomLocation(x,y,z));
                                    } else if(parse.getType().equals(BlockParseData.DataType.LocationList)){
                                        ArrayList<CustomLocation> myList = mapLocationList.get(parse.getIdentifier());
                                        if(myList==null){
                                            myList = new ArrayList();
                                            mapLocationList.put(parse.getIdentifier(), myList);
                                        }
                                        myList.add(new CustomLocation(x,y,z));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean loadFromData(File mapfolder, Map<String, Object> map) {

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String id = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof CustomLocation){
                mapLocation.put(id, (CustomLocation) value);
            } else if(value instanceof ArrayList){
                mapLocationList.put(id, (ArrayList<CustomLocation>)value);
            } else if(value instanceof String){
                nameString.put(id, (String)value);
            }
        }
        mapfile = new File(mapfolder.getAbsolutePath() + "/" + getName() + ".schematic");
        return true;
    }

    public void decode(File file, String content) {
        HashMap<String, Object> map = new HashMap();

        for (String str : content.split(";")) {
            if (!str.isEmpty()) {
                String[] str2 = str.split("=");
                String type = str2[0];
                String id = str2[1];
                String data = str2[2].replaceFirst("<", "").replaceFirst(">", "");

                if(type.equalsIgnoreCase("Location")){
                    map.put(id, Code.decodeLocation(data));
                } else if(type.equalsIgnoreCase("LocationList")){
                    map.put(id, Code.decodeLocations(data));
                } else if(type.equalsIgnoreCase("String")){
                    map.put(id, String.valueOf(data));
                }
            }
        }
        this.loadFromData(file, map);
    }

    public String encode() {
        String str = "";

        for(Map.Entry<String,CustomLocation> val : mapLocation.entrySet()){
            str = Code.addLocationToString(str, val.getKey(), val.getValue());
        }
        for(Map.Entry<String,ArrayList<CustomLocation>> val : mapLocationList.entrySet()){
            str = Code.addLocationListToString(str, val.getKey(), val.getValue());
        }
        for(Map.Entry<String,String> val : nameString.entrySet()){
            str = Code.addStringToString(str, val.getKey(), val.getValue());
        }
        return str;
    }


    public boolean queueBuild(Integer maxAmount) {
        if (pasteLocation == null || clipboard == null || clipboard.isQueueFinished()) {
            return false;
        }
        try {

            EditSession es = new EditSession(new BukkitWorld(Bukkit.getWorld(worldName)), Integer.MAX_VALUE);
            es.setFastMode(true);
            clipboard.runQueue(es, new BlockPosition(pasteLocation.getX(), pasteLocation.getY(), pasteLocation.getZ()), maxAmount);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        if (clipboard.isQueueFinished()) {
            clipboard = null;
            isFullyLoaded = true;
            isLoading = false;
            System.out.println("Took " + ((System.currentTimeMillis() - buildStartTime)) + "ms to build " + this.getName());
        }
        return true;
    }

    boolean firstLoad = false;
    public void reBuildMap() {
        long test1 = System.currentTimeMillis();
        if (pasteLocation != null) {
            if(!firstLoad){
                firstLoad = true;
            }else{
                setNextBuildLocation();
            }
            isLoading = true;
            isFullyLoaded = false;
            buildStartTime = System.currentTimeMillis();

            SchematicClipboard cc = Code.loadSchematicFromFile(mapfile);


            cc.setOrigin(new Vector(0, 0, 0));
            cc.setOffset(new Vector(0, 0, 0));
            clipboard = cc;
            System.out.println("Building Map " + getName() + " on World " + worldName + " at " + pasteLocation.toString() + " Size: X:" + cc.getSize().getBlockX() + " Y:" + cc.getSize().getBlockY() + " Z:" + cc.getSize().getBlockZ());
        }
        System.out.println("Took "+(System.currentTimeMillis()-test1+" ms to load schematic"));
    }


    public void setNextBuildLocation() {
        pasteLocation = new CustomLocation(nextX,0,nextZ);
        nextX+=500;
        nextZ+=500;
        for(Map.Entry<String, ArrayList<CustomLocation>> val : mapLocationList.entrySet()){
            for(CustomLocation loc : val.getValue()){
                loc.fuseWith(getBuildLocation());
            }
        }
        for(Map.Entry<String, CustomLocation> val : mapLocation.entrySet()){
                val.getValue().fuseWith(getBuildLocation());
        }
    }

    public CustomLocation getBuildLocation() {
        return pasteLocation;
    }
    public void setCustomBuildLocation(CustomLocation newLocation){
        pasteLocation = newLocation;
        for(Map.Entry<String, ArrayList<CustomLocation>> val : mapLocationList.entrySet()){
            for(CustomLocation loc : val.getValue()){
                loc.fuseWith(getBuildLocation());
            }
        }
        for(Map.Entry<String, CustomLocation> val : mapLocation.entrySet()){
            val.getValue().fuseWith(getBuildLocation());
        }
    }
    public void setCustomWorldName(String name){
        worldName = name;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isFullyLoaded() {
        return isFullyLoaded;
    }

    public void setFullyLoaded(boolean loaded) {
        isFullyLoaded = loaded;
    }

    public void loadFromBase(MapCipherBase base) {
        for(Map.Entry<String,CustomLocation> val : base.mapLocation.entrySet()){
            mapLocation.put(val.getKey(), val.getValue().clone());
        }
        for(Map.Entry<String,ArrayList<CustomLocation>> val : base.mapLocationList.entrySet()){
            ArrayList<CustomLocation> cleanArray = new ArrayList();
            for(CustomLocation loc  : val.getValue()){
                cleanArray.add(loc.clone());
            }
            mapLocationList.put(val.getKey(),cleanArray);
        }
        for(Map.Entry<String,String> val : base.nameString.entrySet()){
            nameString.put(val.getKey(),val.getValue());
        }
        setNextBuildLocation();
        this.mapfile = base.mapfile;
        isFullyLoaded = false;
        isLoading = false;
    }

    public abstract MapCipherBase clone();
}
