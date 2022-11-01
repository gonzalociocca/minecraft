package server;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import server.api.*;
import server.instance.GameServer;
import server.instance.misc.VoidGenerator;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.user.User;
import server.user.column.Simple.SimpleData;

import java.sql.SQLException;
import java.util.HashMap;

public class ServerPlugin extends JavaPlugin implements Listener {

    static ServerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        new BoardAPI();
        new DatabaseAPI();
        new GameAPI();
        new MenuAPI();
        new ChatAPI();
        new RunnableAPI();


        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Updater(this), 1L, 1L);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        GameServer mainServer = GameAPI.getServerInterface().getMainServer();
        finish(mainServer);
        if(mainServer.subServerList != null && !mainServer.subServerList.isEmpty()) {
            for (GameServer gameServer : mainServer.subServerList) {
                finish(gameServer);
            }
        }

        DatabaseAPI.getUpdater().forceAll();
    }

    public void finish(GameServer gameServer){
        for (Player player : gameServer.getPlayers(false)) {
            gameServer.getLogin().removePlayer(gameServer, player, true, true);
            User pd = getPlayerData(player.getName());
            forceSave(pd);
        }
    }


    public static ServerPlugin getInstance(){
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    static HashMap<String, User> playermap = new HashMap();

    public static User getPlayerData(String name) {
        String str = name.toLowerCase();
        User pd = playermap.get(str);
        if (pd == null) {
            pd = new User(str, name);
            playermap.put(str, pd);
        }
        return pd;
    }

    public void forceSave(User pd) {
        SimpleData simpleData = pd.getSimpleData();
        DatabaseAPI.getUpdater().forceUpdate(pd.getLowercaseName());
        if (pd.canSave() && simpleData.shouldSave()) {
            try {
                DatabaseAPI.getConnection().getStatement().executeUpdate(pd.getSimpleDataQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void removePlayerData(String name) {
        String str = name.toLowerCase();
        User pd = getPlayerData(name);
        forceSave(pd);
        playermap.remove(str);
    }

    @EventHandler
    public void databaseQueue(UpdateEvent event) {
        if (event.getType() != UpdateType.MIN_01) {
            return;
        }
        for (User pd : playermap.values()) {
            pd.saveCheck();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        removePlayerData(event.getPlayer().getName());
    }

    public ChunkGenerator generator = new VoidGenerator();

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return this.generator;
    }

}