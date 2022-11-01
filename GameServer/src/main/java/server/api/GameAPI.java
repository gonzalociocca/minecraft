package server.api;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import server.ServerInterface;
import server.ServerPlugin;
import server.common.Rank;
import server.instance.GameServer;
import server.instance.core.map.base.MapCipherBase;
import server.listener.GameCreationManager;
import server.listener.VanillaListener;
import server.user.User;
import server.user.column.Simple.SimpleData;
import server.user.column.Valuable.DataTypes.GlobalRanks;
import server.user.column.Valuable.ValuableData;
import server.util.UtilFile;
import server.util.UtilNetwork;
import server.util.UtilWorld;


public class GameAPI implements Listener {
    private static UtilNetwork _network = null;
    private static GameCreationManager _gameCreationManager;
    private static ServerInterface _serverInterface;
    private static FileConfiguration config;
    private static VanillaListener _vanillaListener;


    public GameAPI() {
        startWorlds();
        config = ServerPlugin.getInstance().getConfig();
        createUserTable();
        createServerTable();
        DatabaseAPI.forceQuery();

        _network = new UtilNetwork();
        _serverInterface = new ServerInterface();
        _gameCreationManager = new GameCreationManager();
        _vanillaListener = new VanillaListener();

        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    public void startWorlds(){
        UtilFile.removeBaseDirectory(MapCipherBase.defaultWorld);

        UtilWorld.load(MapCipherBase.defaultWorld, ServerPlugin.getInstance().generator);
    }

    public void createServerTable(){
        String newGameTable = "CREATE TABLE IF NOT EXISTS `" + DatabaseAPI.getServerListTable() + "` ("
                + "`id` varchar(255) NOT NULL"
                + ",`server` longtext NOT NULL"
                + ",PRIMARY KEY  (`id`))";
            DatabaseAPI.addQuery(newGameTable);
    }

    public void createUserTable() {

        String createUserTable = "CREATE TABLE IF NOT EXISTS `" + DatabaseAPI.getUserTable() + "` ("
                + "`ID` int(11) NOT NULL auto_increment"
                + ",`" + "Name" + "` varchar(40) NOT NULL"
                + ",`" + "UUID" + "` varchar(255) NOT NULL"
                + ",`" + SimpleData.getColumn() + "` text(4999) NOT NULL"
                + ",`" + ValuableData.getColumn() + "` text(4999) NOT NULL"
                + ",PRIMARY KEY  (`ID`)"
                +
                ")";

            DatabaseAPI.addQuery(createUserTable);
    }

    public static ServerInterface getServerInterface(){
        return _serverInterface;
    }

    public static UtilNetwork getNetwork() {
        return _network;
    }

    public static GameCreationManager GetGameCreationManager() {
        return _gameCreationManager;
    }

    public static GameServer getGameOf(Player player) {
        if (player != null) {
            GameServer mainServer = getServerInterface().getMainServer();
            if(mainServer.isPlaying(player)){
                return mainServer;
            }
            if(mainServer.subServerList != null && !mainServer.subServerList.isEmpty()) {
                for (GameServer gameServer : mainServer.subServerList) {
                    if (gameServer.isPlaying(player)) {
                        return gameServer;
                    }
                }
            }
        }
        return null;
    }

    public static GameServer getGameInBounds(Chunk chunk) {
        if (chunk != null) {
            return getGameInBounds(new Location(chunk.getWorld(), chunk.getX()*16, 128, chunk.getZ()*16));
        }
        return null;
    }

    public static GameServer getGameInBounds(Location location) {
        if (location != null) {
            GameServer mainServer = getServerInterface().getMainServer();
            if(mainServer.isInBounds(location)){
                return mainServer;
            }
            if(mainServer.subServerList != null && !mainServer.subServerList.isEmpty()) {
                for (GameServer gameServer : mainServer.subServerList) {
                    if (gameServer.isInBounds(location)) {
                        return gameServer;
                    }
                }
            }
        }
        return null;
    }

    public boolean onLoaded(final PlayerJoinEvent event) {
        User pd = ServerPlugin.getPlayerData(event.getPlayer().getName());

        GlobalRanks globalRanks = pd.getValuableData().getGlobalRanks();
        Rank.RankType rankType = globalRanks.getRank().getRankType();

        GameServer mainServer = getServerInterface().getMainServer();

        boolean isFull = false;
        if(isFull && !rankType.isAtLeast(Rank.RankType.VIP)){
            return false;
        }

        /*
        String message = classeditor.isAtLeast(Rank.RankType.VIP) ? Code.Color(UtilMsg.JoinVip.replace("%s", event.getPlayer().getId())) : Code.Color(UtilMsg.Join.replace("%s", event.getPlayer().getId()));

        for (Player player : MapCipherBase.getLobbyWorld().getPlayers()) {
            player.sendMessage(message);
        }*/
        event.setJoinMessage(null);
        return true;
    }

    public void onQuit(PlayerQuitEvent event){
        User pd = ServerPlugin.getPlayerData(event.getPlayer().getName());
        Rank.RankType type = pd.getValuableData().getGlobalRanks().getRank().getRankType();

        /*String message = classeditor.isAtLeast(Rank.RankType.VIP) ? Code.Color(UtilMsg.QuitVip.replace("%s", event.getPlayer().getId())) : Code.Color(UtilMsg.Quit.replace("%s", event.getPlayer().getId()));
        for (Player player : MapCipherBase.getLobbyWorld().getPlayers()) {
            player.sendMessage(message);
        }*/
        event.setQuitMessage(null);
    }

    @EventHandler
    public void Login(final PlayerLoginEvent event) {
        event.setResult(PlayerLoginEvent.Result.ALLOWED);
    }

}
