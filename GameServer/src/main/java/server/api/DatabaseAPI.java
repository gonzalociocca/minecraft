package server.api;

import server.api.database.DatabaseConnection;
import server.api.database.DatabaseUpdaterThread;
import server.api.database.SqlCallback;
import server.api.database.SqlQuery;

import java.sql.SQLException;

public class DatabaseAPI {

    static DatabaseUpdaterThread databaseUpdaterThread = new DatabaseUpdaterThread();
    static DatabaseConnection database;

    public DatabaseAPI() {
        createDatabase("127.0.0.1", "3306", "minigame", "root", "notevinomas123-");
    }

    private static String server = "Server";

    public static void forceQuery(){
        getUpdater().forceUpdate(server);
    }

    public static void forceQuery(String origin){
        getUpdater().forceUpdate(origin);
    }


    public static void addQuery(String sqlQuery){
        getUpdater().add(server, new SqlQuery(sqlQuery, null));
    }

    public static void addQuery(String sqlQuery, SqlCallback sqlCallback){
        getUpdater().add(server, new SqlQuery(sqlQuery, sqlCallback));
    }

    public static void addQuery(String origin, String sqlQuery){
        getUpdater().add(origin, new SqlQuery(sqlQuery, null));
    }

    public static void addQuery(String origin, String sqlQuery, SqlCallback sqlCallback){
        getUpdater().add(origin, new SqlQuery(sqlQuery, sqlCallback));
    }

    public static DatabaseConnection getConnection(){
        return database;
    }

    public static DatabaseUpdaterThread getUpdater(){
        return databaseUpdaterThread;
    }

    public static String getUserTable() {
        return "gamedata";
    }

    public static String getServerListTable() {
        return "serverlist";
    }

    public static DatabaseConnection createDatabase(String host, String port, String db, String user, String pwd) {
        try {
            if (database != null && database.checkConnection()) {
                return database;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database = new DatabaseConnection(host, port, db, user, pwd);

        return database;
    }

}
