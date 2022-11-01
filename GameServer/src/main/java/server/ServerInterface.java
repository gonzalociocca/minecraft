package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.api.DatabaseAPI;
import server.api.database.SqlCallback;
import server.instance.GameServer;
import server.util.UtilGson;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;


public class ServerInterface {

    String _serverId;
    GameServer _mainServer = new GameServer();

    public ServerInterface() {
        try {
            updateFolders();
            _serverId = "TestServer1";
            loadServer(_serverId);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getServerId(){
        return _serverId;
    }

    public void loadServer(String serverId) {
        ServerPlugin.getInstance().getLogger().log(Level.WARNING, "Loading Server "+serverId);
        DatabaseAPI.addQuery(getQuery(serverId), new SqlCallback() {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException {
                if(resultSet.next()){
                    ServerPlugin.getInstance().getLogger().log(Level.WARNING, "Getting server");
                    String result = resultSet.getString("server");
                    if(result != null && !result.isEmpty()){
                        ServerPlugin.getInstance().getLogger().log(Level.WARNING, "Reading server");
                        try {
                            parseJson((JSONObject) new JSONParser().parse(result));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ServerPlugin.getInstance().getLogger().log(Level.WARNING, "Inserting server");
                    insert(serverId, new SqlCallback() {
                        @Override
                        public void onSuccess(ResultSet resultSet) throws SQLException {
                            loadServer(serverId);
                        }
                    });
                }
            }
        });
    }

    public void update(String serverId) {
        String sqlUpdate = "UPDATE " + DatabaseAPI.getServerListTable() + " SET server ='" + toJsonString() + "'" +
                " WHERE id = '" + serverId + "'";
        DatabaseAPI.addQuery(sqlUpdate);
    }

    public String getQuery(String serverId){
        return "SELECT * FROM " + DatabaseAPI.getServerListTable() + " WHERE id = '" + serverId + "'";
    }

    public String toJsonString(){
        return UtilGson.getServer().toJson(_mainServer, _mainServer.getClass());
    }

    public void parseJson(JSONObject jsonObject){
        setMainServer(UtilGson.getServer().fromJson(jsonObject.toString(), GameServer.class));
    }

    public void insert(String serverId, SqlCallback sqlCallback){
        String data = toJsonString();

        String sqlinsert = "INSERT INTO `" + DatabaseAPI.getServerListTable() + "` ("
                + "id" + ","
                + "server"
                + ") " +
                "  SELECT '" + serverId + "',"
                + "'" + data + "'"
                + " FROM dual " +
                "WHERE NOT EXISTS " +
                "( SELECT id FROM " + DatabaseAPI.getServerListTable() + " WHERE id='" + serverId + "' );";

            DatabaseAPI.addQuery(sqlinsert, sqlCallback);

    }

    public void updateFolders() {
        ServerPlugin.getInstance().getDataFolder().mkdir();
        File mapFolder = new File(ServerPlugin.getInstance().getDataFolder().getAbsolutePath() + "/map");
        mapFolder.mkdir();
    }

    public GameServer getMainServer() {
        return _mainServer;
    }

    public void setMainServer(final GameServer gameServer) {
        _mainServer = gameServer;
    }
}
