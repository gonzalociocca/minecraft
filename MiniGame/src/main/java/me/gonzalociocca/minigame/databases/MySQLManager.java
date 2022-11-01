package me.gonzalociocca.minigame.databases;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * column Scheme´s:
 * ID/Name/UUID/UserStats(ReadWrite)/Perks(Single Read, Must Re-Read to Write)
 **/
public class MySQLManager {
    JavaPlugin plugin = null;
    MySQL database;

    public MySQLManager(JavaPlugin pl) {
        plugin = pl;
    }

    /**
     * Creates a new MySQL instance
     *
     * @param host Name of the host
     * @param port Port number
     * @param db   Database name
     * @param user Username
     * @param pwd  Password
     */
    public MySQL createDatabase(String host, String port, String db, String user, String pwd) {
        try {
            if (database != null && database.checkConnection()) {
                return database;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database = new MySQL(plugin, host, port, db, user, pwd);

        return database;
    }


    public Connection getConnection() {
        try {
            return database.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserTable() {
        return "GameData";
    }

    public String getMapTable() {
        return "MapData";
    }

    Statement statement = null;

    public Statement getStatement() {
        try {
            if (statement != null && !statement.isClosed()) {
                return statement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = this.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }


}
