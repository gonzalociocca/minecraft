package me.gonzalociocca.minigame.map;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.events.Update.UpdateEvent;
import me.gonzalociocca.minigame.events.Update.UpdateType;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.map.Cipher.LobbyMap;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.misc.CustomLocation;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapManager implements Listener {
    /**
     * Map Types:
     * Team
     **/
    Core core;
    File mapFolder;
    HashMap<GameType, List<MapCipherBase>> activeMaps = new HashMap();
    HashMap<GameType, List<MapCipherBase>> maps = new HashMap();

    public MapManager(Core plugin) {
        core = plugin;
        for (GameType type : GameType.values()) {
            activeMaps.put(type, new ArrayList());
            maps.put(type, new ArrayList());
        }
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    public List<MapCipherBase> getActiveMaps(GameType type) {
        return activeMaps.get(type);
    }

    private List<MapCipherBase> getMaps(GameType type) {
        return maps.get(type);
    }

    public MapCipherBase getNewNextMapFor(GameType type) {
        MapCipherBase origin = maps.get(type).get(activeMaps.size() % maps.get(type).size());

        MapCipherBase copy = origin.clone();

        activeMaps.get(type).add(copy);
        return copy;
    }

    public void loadMapsForGameType(GameType gametype) {
        List<MapCipherBase> list = new ArrayList();
        core.getDataFolder().mkdir();
        mapFolder = new File(core.getDataFolder().getAbsolutePath() + "/" + gametype.getShortName() + "/");
        mapFolder.mkdir();

        String str = null;
        ResultSet res;
        try {

            res = core.getMySQL().getStatement().executeQuery("SELECT " + gametype.getShortName() + " FROM " + core.getMySQL().getMapTable());
            res.next();
            str = res.getString(gametype.getShortName());

            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (str != null && !str.isEmpty()) {
            String[] mapdata = str.split("/");
            for (String str2 : mapdata) {
                try {
                    MapCipherBase cipher = null;
                    try {
                        cipher = gametype.getMapType().getConstructor().newInstance();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    cipher.decode(mapFolder, str2);
                    list.add(cipher);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        maps.put(gametype, list);
        boolean shouldSave = false;
        if (mapFolder.isDirectory()) {
            File[] files = mapFolder.listFiles();
            for (File file : files) {
                String display = file.getName().substring(0, (int) file.getName().length() - 10);
                boolean loaded = false;
                List<MapCipherBase> maplist = maps.get(gametype);
                if (!maplist.isEmpty()) {
                    for (MapCipherBase base : maplist) {
                        if (base.getName().equals(display)) {
                            loaded = true;
                            break;
                        }
                    }
                }
                if (!loaded) {
                    MapCipherBase newmap = parseMap(file, gametype, display);
                    maps.get(gametype).add(newmap);
                    shouldSave = true;
                }
            }
        }
        if (shouldSave) {
            this.saveAllMaps(gametype);
        }
    }

    public MapCipherBase loadCustomMap(String schematicName, Class mapClass) {
        MapCipherBase myCustomMap;
        core.getDataFolder().mkdir();
        (new File(core.getDataFolder().getAbsolutePath() + "/" + "Custom" + "/")).mkdir();

        File file = new File(core.getDataFolder().getAbsolutePath() + "/" + "Custom" + "/" + schematicName + ".schematic");

        String display = file.getName().substring(0, file.getName().length() - 10);

        myCustomMap = parseCustomMap(file, mapClass, display);

        return myCustomMap;
    }

    public void saveAllMaps(GameType type) {
        try {
            String str = "";
            for (MapCipherBase base : maps.get(type)) {
                if (str.isEmpty()) {
                    str = str + base.encode();
                } else {
                    str = str + "/" + base.encode();
                }
            }
            String sql = "UPDATE " + core.getMySQL().getMapTable() + " SET " +
                    type.getShortName() + " ='" + str + "'" +
                    " WHERE ID = '1'";
            core.getMySQL().getStatement().execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MapCipherBase parseMap(File file, GameType gametype, String display) {
        MapCipherBase cipher = null;
        try {
            cipher = gametype.getMapType().getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (!cipher.loadFromSchematic(file, display)) {
            System.out.println("Map " + display + " couldnt be parsed.");
        } else {
            System.out.println("Map " + display + " parsed sucessfully");
        }

        return cipher;
    }
    public MapCipherBase parseCustomMap(File file, Class<? extends MapCipherBase> mapClass, String display) {
        MapCipherBase cipher = null;
        try {
            cipher = mapClass.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (!cipher.loadFromSchematic(file, display)) {
            System.out.println("Map " + display + " couldnt be parsed.");
        } else {
            System.out.println("Map " + display + " parsed sucessfully");
        }

        return cipher;
    }

    @EventHandler
    public void queueSchematicRebuild(UpdateEvent event) {
        if (event.getType().equals(UpdateType.Tick)) {
            int blockPer = 100000000;

            label:
            for (GameType type : GameType.values()) {
                for (MapCipherBase base : getActiveMaps(type)) {
                    if (base.getBuildLocation() != null && !base.isFullyLoaded() && !base.isLoading()) {
                        base.reBuildMap();
                        break label;
                    }
                    if (base.queueBuild(blockPer)) {
                        break label;
                    }
                }
            }
        }
    }

}
