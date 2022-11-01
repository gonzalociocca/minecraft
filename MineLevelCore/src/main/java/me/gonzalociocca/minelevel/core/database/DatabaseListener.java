package me.gonzalociocca.minelevel.core.database;

import me.gonzalociocca.minelevel.core.Main;
import me.gonzalociocca.minelevel.core.enums.ColumnType;
import me.gonzalociocca.minelevel.core.enums.SvType;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import me.gonzalociocca.minelevel.core.user.ban.Ban;
import me.gonzalociocca.minelevel.core.user.rank.RankType;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

/**
 * Created by noname on 9/2/2017.
 */
public class DatabaseListener implements Listener {
    Main plugin;

    String address = "127.0.0.1";
    String port = "3306";
    String db = "minelevel";
    String user = "root";
    String password = "notevinomas123-";
    public String table = "data";

    boolean enabled = true;
    java.sql.Connection data = null;

    DatabaseUpdateThread updateThread;

    public DatabaseListener(Main main, String aaddress, String aport, String adb, String auser, String apassword) {
        address = aaddress;
        port = aport;
        db = adb;
        user = auser;
        password = apassword;
        plugin = main;
        updateThread = new DatabaseUpdateThread(main);
        updateThread.start();
        init();
        Bukkit.getPluginManager().registerEvents(this, main);

    }

    public Main getPlugin() {
        return plugin;
    }

    public DatabaseUpdateThread getThread(){
        return updateThread;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData pd = getPlayerData(event.getPlayer().getName());
        if (!Variable.ServerType.isReadOnly()) {
            if (pd.canSave()) {
                DatabaseUpdateThread.addToQueue(pd.getName(), pd.getSaveQuery());
                pd.changed = false;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void beforeJoin(PlayerLoginEvent event) {

        if (!Variable.DatabasePlayerMap.containsKey(event.getPlayer().getName().toLowerCase())) {
            this.insert(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
        }

        PlayerData pd = this.getPlayerData(event.getPlayer().getName());

        Ban ban = pd.getBan();
        if (ban != null && !Variable.ServerType.equals(SvType.Lobby)) {
            event.setKickMessage(Code.Color(ban.getMessage()));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            return;
        }

        if(MinecraftServer.getServer().recentTps[0] < 18 && !pd.getRank().getType().isAtLeast(RankType.VIP)){
            event.setKickMessage(Variable.MessageLagKickNonVip);
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            return;
        }

        pd.reSendPermissions(event.getPlayer());
    }

    public void checkData() {
        if (enabled == false) {
            return;
        }
        if (data == null) {
            openData();
        } else try {
            if (data.isClosed()) {
                openData();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public PlayerData getPlayerData(String name) {
        name = name.toLowerCase();
        PlayerData pd = Variable.DatabasePlayerMap.get(name);
        if (pd != null && !pd.preventsave) {
            return pd;
        } else {
            try {
                pd = new PlayerData(name, this);
                if (!pd.preventsave) {
                    Variable.DatabasePlayerMap.put(name, pd);
                } else {
                    pd = new PlayerData(name, this);
                    if (!pd.preventsave) {
                        Variable.DatabasePlayerMap.put(name, pd);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pd;
    }

    public boolean hasPlayerData(String name) {
        if (Variable.DatabasePlayerMap.containsKey(name.toLowerCase())) {
            return true;
        }
        return false;
    }

    private void createTable() throws SQLException {
        if (enabled == false) {
            return;
        }

        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS `" + table + "` ("
                        + "`ID` int(11) NOT NULL auto_increment"
                        + ",`" + ColumnType.Name.getName() + "` varchar(255) NOT NULL"
                        + ",`" + ColumnType.UUID.getName() + "` varchar(255) NOT NULL"
                        + ",`" + ColumnType.LastTransactions.getName() + "` text(4999) NOT NULL"
                        + ",`" + ColumnType.Ranks.getName() + "` text(4999) NOT NULL"
                        + ",`" + ColumnType.Diamonds.getName() + "` double NOT NULL"
                        + ",`" + ColumnType.Mutes.getName() + "` text(4999) NOT NULL"
                        + ",`" + ColumnType.Bans.getName() + "` text(4999) NOT NULL"
                        + ",PRIMARY KEY  (`ID`)"
                        +
                        ")";


        this.getStatement().execute(sqlCreate);
    }

    java.sql.Statement Statement = null;

    public java.sql.Statement getStatement() {
        if (enabled == false) {
            return null;
        }
        try {
            if (Statement == null) {
                Statement = data.createStatement();
            } else if (Statement.isClosed()) {
                Statement = data.createStatement();
            }


            return Statement;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Statement;
    }


    public final void openData() {
        if (enabled == false) {
            return;
        }
        MySQL mysql = new MySQL(plugin, address, port, db, user, password);
        try {
            data = mysql.openConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void init() {
        System.out.println("[MineLevel] Initializing");
        try {
            this.openData();
        } catch (Exception e) {
            e.printStackTrace();
            enabled = false;
            return;
        }

        try {
            this.createTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("MineLevel Initialized.");
        enabled = true;
    }

    public void insert(String name, String uuid) {
        String sqlinsert = "INSERT INTO `" + table + "` ("
                + ColumnType.Name.getName() + ","
                + ColumnType.UUID.getName() + ","
                + ColumnType.LastTransactions.getName() + ","
                + ColumnType.Ranks.getName() + ","
                + ColumnType.Diamonds.getName() + ","
                + ColumnType.Mutes.getName() + ","
                + ColumnType.Bans.getName()
                + ") " +
                "  SELECT '" + name.toLowerCase() + "',"
                + "'" + uuid + "',"
                + "'" + ColumnType.LastTransactions.getDefault() + "',"
                + "'" + ColumnType.Ranks.getDefault() + "',"
                + "'" + ColumnType.Diamonds.getDefault() + "',"
                + "'" + ColumnType.Mutes.getDefault() + "',"
                + "'" + ColumnType.Bans.getDefault() + "'"
                + " FROM dual " +
                "WHERE NOT EXISTS " +
                "( SELECT Name FROM " + table + " WHERE Name='" + name.toLowerCase() + "' );";

        try {
            this.getStatement().execute(sqlinsert);
            System.out.println("Record inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Record not inserted");
        }
    }

    @EventHandler
    public void queueUpdate(UpdateEvent event) {
        if (event.getType() == UpdateType.MIN10) {
            DatabaseUpdateThread.checkMap = true;
        }
    }

}
