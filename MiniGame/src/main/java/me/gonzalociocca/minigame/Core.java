package me.gonzalociocca.minigame;

import me.gonzalociocca.minigame.column.Perks.PerksManager;
import me.gonzalociocca.minigame.column.UserStats.StatsManager;
import me.gonzalociocca.minigame.commands.GameCommand;
import me.gonzalociocca.minigame.commands.RankCommand;
import me.gonzalociocca.minigame.commands.ResetCommand;
import me.gonzalociocca.minigame.commands.TeleportCommand;
import me.gonzalociocca.minigame.databases.MySQLManager;
import me.gonzalociocca.minigame.events.Update.Updater;
import me.gonzalociocca.minigame.games.GameManager;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.lobby.LobbyManager;
import me.gonzalociocca.minigame.map.MapManager;
import me.gonzalociocca.minigame.misc.PlayerData;
import me.gonzalociocca.minigame.misc.VoidGenerator;
import me.gonzalociocca.minigame.scoreboard.BoardManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class Core extends JavaPlugin implements Listener {

    /**
     * Plugin Description:
     * Modular
     * ✓ Perks Manager
     * ✓ Stats Manager
     * ✓ Player MySQL Load
     * x Lobby Manager
     * x Game Manager
     * x Games
     * x Stats
     * x Perks
     **/

    MapManager mapmanager;
    BoardManager scoreboardManager;
    LobbyManager lobbymanager;
    GameManager gamemanager;
    MySQLManager mysql;
    HashMap<String, PlayerData> playermap = new HashMap();

    /**
     * ServerType > Lobby & Games > Lobby Signs, Npc, Stats Update >
     **/
    @Override
    public void onEnable() {
        mysql = new MySQLManager(this);
        mysql.createDatabase("127.0.0.1", "3306", "minigame", "root", "notevinomas123-");
        this.createTable();
        new Updater(this);
        this.loadWorld(getWorldGameName());
        this.loadWorld(getWorldLobbyName());

        mapmanager = new MapManager(this);
        scoreboardManager = new BoardManager(this);
        gamemanager = new GameManager(this);
        lobbymanager = new LobbyManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        for(GameBase base : getGameManager().getGames()){
            for(PlayerData pd : base.getPlayersList(true, true)){
                base.removeFromGame(pd);
                pd.getStatsManager().saveAllData();
            }
        }
        deleteDirectory(new File(Core.getWorldGameName()));
        deleteDirectory(new File(Core.getWorldLobbyName()));
    }

    public void loadWorld(String worldname) {
        World newGameWorld = new WorldCreator(worldname).generator(generator).environment(World.Environment.NORMAL).type(WorldType.CUSTOMIZED).generateStructures(false).seed(0).createWorld();
        newGameWorld.setAutoSave(false);
        newGameWorld.setTicksPerAnimalSpawns(0);
        newGameWorld.setTicksPerMonsterSpawns(0);
        newGameWorld.setPVP(true);
    }

    public static boolean deleteDirectory(File path) {
        if (path == null) {
            return false;
        }
        if (path.exists()) {
            for (File file : path.listFiles()) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }

        }
        return (path.delete());
    }

    ChunkGenerator generator = new VoidGenerator();

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return this.generator;
    }

    public static String getWorldLobbyName() {
        return "gamelobby";
    }

    public static String getWorldGameName() {
        return "gamemaps";
    }

    public MapManager getMapManager() {
        return mapmanager;
    }

    public LobbyManager getLobbyManager() {
        return lobbymanager;
    }

    public MySQLManager getMySQL() {
        return mysql;
    }

    public GameManager getGameManager() {
        return gamemanager;
    }

    public BoardManager getScoreboardManager(){
        return scoreboardManager;
    }

    private void createTable() {

        String createUserTable = "CREATE TABLE IF NOT EXISTS `" + getMySQL().getUserTable() + "` ("
                + "`ID` int(11) NOT NULL auto_increment"
                + ",`" + "Name" + "` varchar(40) NOT NULL"
                + ",`" + "UUID" + "` varchar(255) NOT NULL"
                + ",`" + StatsManager.getColumn() + "` text(4999) NOT NULL"
                + ",`" + PerksManager.getColumn() + "` text(4999) NOT NULL"
                + ",PRIMARY KEY  (`ID`)"
                +
                ")";
        String createMapTable = "CREATE TABLE IF NOT EXISTS `" + getMySQL().getMapTable() + "` ("
                + "`ID` int(11) NOT NULL auto_increment";
        for (GameType type : GameType.values()) {
            createMapTable = createMapTable + ",`" + type.getShortName() + "` text(4999) NOT NULL";
        }
        createMapTable = createMapTable + ",PRIMARY KEY  (`ID`)" + ")";


        try {
            getMySQL().getStatement().execute(createUserTable);
            getMySQL().getStatement().execute(createMapTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(String name) {
        String str = name.toLowerCase();
        PlayerData pd = playermap.get(str);
        if (pd == null) {
            pd = new PlayerData(str, this);
            playermap.put(str, pd);
        }
        return pd;
    }

    public void removePlayerData(String name) {
        String str = name.toLowerCase();
        getPlayerData(str).getStatsManager().saveAllData();
        playermap.remove(str);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        getPlayerData(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        removePlayerData(event.getPlayer().getName());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isPlayer = sender instanceof Player;

        if (RankCommand.onCommand(this, isPlayer, isConsole, sender, command, label, args)
                || TeleportCommand.onCommand(this, isPlayer, isConsole, sender, command, label, args)
                || ResetCommand.onCommand(this, isPlayer, isConsole, sender, command, label, args)
                || GameCommand.onCommand(this, isPlayer, isConsole, sender, command, label, args)
                ) {
            return true;
        }
        return false;
    }

}
