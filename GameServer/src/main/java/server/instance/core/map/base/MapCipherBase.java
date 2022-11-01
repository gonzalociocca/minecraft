package server.instance.core.map.base;

import com.google.gson.annotations.Expose;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import server.ServerPlugin;
import server.api.RunnableAPI;
import server.api.runnable.RunnableCallback;
import server.common.Code;
import server.common.CustomLocation;
import server.instance.core.map.misc.BlockParseData;
import server.instance.core.map.misc.MapState;
import server.instance.core.map.misc.SchematicClipboard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Fast Map Options:
 * Small Map with Small Build Size - Fastest
 * Small Schematic with Big Map and Per Location Clear - Fast Sync
 * Small Schematic with big Build Size and Change Location Every Build - Very Fast
 **/

public class MapCipherBase {

    public static int nextX = 0;
    public static int nextZ = 0;
    public static String defaultWorld = "loader";

    //Non-Expose Start
    protected List<BlockParseData> _parseItems = new ArrayList();
    MapState _mapState = MapState.Loading;
    private CustomLocation _currentBuildLocation;
    private SchematicClipboard _clipboard;
    private Map<String, List<CustomLocation>> locationListMap = new HashMap();
    //Non-Expose End

    // Expose Start
    @Expose
    public String _filePath = "Lobby.schematic";

    @Expose
    public String _authorName = "<Author>";

    @Expose
    public String _displayName = "<Display>";

    @Expose
    public String _worldName = defaultWorld;

    @Expose
    public String classIdentifier = getClass().getCanonicalName();
    // Expose End

    public File getFile() {
        return new File(ServerPlugin.getInstance().getDataFolder().getAbsolutePath() + "/map/" + _filePath);
    }

    public String getAuthor() {
        return _authorName;
    }

    public String getDisplay() {
        return _displayName;
    }

    public String getWorldName() {
        return _worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(_worldName);
    }

    public List<CustomLocation> getCustomLocationList(String id) {
        return locationListMap.get(id);
    }

    public MapState getState(){
        return _mapState;
    }

    public void setState(MapState newState){
        _mapState = newState;
        ServerPlugin.getInstance().getLogger().log(Level.INFO, "Map State Change, Display="+getDisplay()+" World="+getWorldName()+ " State="+getState() + " Author="+getAuthor() +( getBuildLocation() != null ? " Location="+getBuildLocation().toString() : "") + " FilePath="+_filePath + " ClassId="+classIdentifier+" Class="+getClass().getCanonicalName());
    }

    public boolean load() {
        SchematicClipboard cc = Code.loadSchematicFromFile(getFile());
        locationListMap.clear(); // Read again on every rebuild, files may change.
        if (cc != null) {
            double oriX = getBuildLocation().getX();
            double oriY = getBuildLocation().getY();
            double oriZ = getBuildLocation().getZ();

            for (int x = 0; x < cc.getWidth(); x++) {
                for (int y = 0; y < cc.getHeight(); y++) {
                    for (int z = 0; z < cc.getLength(); z++) {
                        BaseBlock block = cc.getBlock(x, y, z);
                        if (block != null && !block.isAir()) {
                            for (BlockParseData parse : _parseItems) {
                                if (block.getType() == parse.getID() && (parse.getData() == -1 || block.getData() == parse.getData())) {
                                    if (parse.getType().equals(BlockParseData.DataType.LocationList)) {
                                        List<CustomLocation> customLocationList = locationListMap.get(parse.getIdentifier());
                                        if (customLocationList == null) {
                                            customLocationList = new ArrayList();
                                            locationListMap.put(parse.getIdentifier(), customLocationList);
                                        }
                                        float yaw = 0;
                                        float pitch = 0;
                                        if (parse.getID() == 68 || parse.getID() == 63) {
                                            yaw = Code.getYawOfSignRotation((byte) block.getData());
                                        }
                                        customLocationList.add(new CustomLocation(oriX + x, oriY + y, oriZ + z, yaw, pitch));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        _clipboard = cc;
        return true;
    }

    public CustomLocation getBuildLocation() {
        return _currentBuildLocation;
    }

    public void setNextBuildLocation() {
        nextX+=1000;nextZ+=1000;
        _currentBuildLocation = new CustomLocation(nextX, 0, nextZ);

        for (Map.Entry<String, List<CustomLocation>> entry : locationListMap.entrySet()) {
            List<CustomLocation> locList = new ArrayList();
            for (CustomLocation customLocation : entry.getValue() ) {
                locList.add(customLocation.clone().addX(nextX).addZ(nextZ));
            }
            locationListMap.put(entry.getKey(), locList);
        }
    }

    public boolean queueBuild(Integer maxAmount) {
        if (_clipboard == null || _clipboard.isQueueFinished() || _currentBuildLocation == null) {
            return false;
        }
        try {
            _clipboard.runQueue(getWorld(), new BlockPosition(_currentBuildLocation.getX(), _currentBuildLocation.getY(), _currentBuildLocation.getZ()), maxAmount);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        if (_clipboard.isQueueFinished()) {
            _clipboard = null;
            setState(MapState.Build);
        }
        return true;
    }

    public void reBuildMap() {
            setNextBuildLocation();
            setState(MapState.Initializing);
        RunnableAPI.add(new RunnableCallback() {
            @Override
            public void onRun() {
                load();
                setState(MapState.Building);
            }
        });
    }

    public boolean tick(){
        if(getState() == MapState.Loading) {
            reBuildMap();
            return true;
        } else if(getState() == MapState.Building){
            queueBuild(12500);
            return true;
        }
        return false;
    }

    public void onChunkUnload(ChunkUnloadEvent event){
        event.setCancelled(true);
    }

    public void onChunkLoad(ChunkLoadEvent event){

    }

}
