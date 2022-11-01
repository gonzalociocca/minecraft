package server.command.server;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import server.api.DatabaseAPI;
import server.api.GameAPI;
import server.api.database.SqlCallback;
import server.command.server.edit.onServerEdit;
import server.common.Code;
import server.common.LongMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by noname on 26/5/2017.
 */
public class ServerCommand {
    public static void onCommand(PlayerCommandPreprocessEvent event, String[] args) {
        if (args.length > 0 && args[0].equals("/server")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("load")) {
                    onServerLoad(event, args);
                } else if (args[1].equalsIgnoreCase("save")) {
                    onServerSave(event, args);
                } else if (args[1].equalsIgnoreCase("copy")) {
                    onServerCopy(event, args);
                } else if (args[1].equalsIgnoreCase("edit")) {
                    onServerEdit.run(event, args);
                } else if (args[1].equalsIgnoreCase("add")) {
                    onServerAdd(event, args);
                } else if (args[1].equalsIgnoreCase("clone")) {
                    onServerClone(event, args);
                } else {
                    displayServerCommands(event.getPlayer());
                }
            } else {
                displayServerCommands(event.getPlayer());
            }
            event.setCancelled(true);
        }
    }

    public static void onServerAdd(PlayerCommandPreprocessEvent event, String[] args) {
        //todo: create Server Add command
    }

    public static void onServerClone(PlayerCommandPreprocessEvent event, String[] args) {
        //todo: create Server Clone command

    }

    public static void onServerLoad(PlayerCommandPreprocessEvent event, String[] args) {
        if (args.length > 2) {
            String serverId = args[2];
            GameAPI.getServerInterface().loadServer(serverId);
            sendDefault(event.getPlayer());
        } else {
            sendServerList(event.getPlayer());
            sendDefault(event.getPlayer());
        }
    }

    public static void onServerCopy(PlayerCommandPreprocessEvent event, String[] args) {
        if (args.length > 2) {
            String serverId = args[2];
            GameAPI.getServerInterface().insert(serverId, new SqlCallback() {
                @Override
                public void onSuccess(ResultSet resultSet) throws SQLException {
                    GameAPI.getServerInterface().update(serverId);
                }
            });
        } else {
            displayServerCommands(event.getPlayer());
        }
    }

    public static void onServerSave(PlayerCommandPreprocessEvent event, String[] args) {
        GameAPI.getServerInterface().update(GameAPI.getServerInterface().getServerId());
        sendDefault(event.getPlayer());
    }

    public static void displayServerCommands(Player player) {
        player.sendMessage(Code.Color("&a/server load <id>"));
        player.sendMessage(Code.Color("&a/server save"));
        player.sendMessage(Code.Color("&a/server copy <newId>"));
        player.sendMessage(Code.Color("&a/server edit"));
    }

    List<String> serverType = Arrays.asList(
            "server.instance.loader.SkyWars.SkyWarsSolo"
    );
    List<String> mapType = Arrays.asList(
            "server.instance.loader.SkyWars.SkyWarsSoloMap"
    );

        /*
    * Select Server fromId
    * Select MainServer/SubServer
    * Create SubServer from IdList or Copy
    * Copy Server into new Server with Id;
    * */


    /*
    * /server
    * /server load
    * /server load <id> ( Load or create from database )
    * /server edit
    * /server edit <id> ( Load from the number in the displayed list )
    *
    * */


    public static void sendDefault(Player player) {
        player.sendMessage(Code.Color("&aComando Executado"));
    }

    public static void sendServerList(Player player) {
        String sqlQuery = "SELECT * FROM " + DatabaseAPI.getServerListTable();
        DatabaseAPI.addQuery(sqlQuery, new SqlCallback() {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException {
                player.sendMessage("Server List: ");
                LongMessage longMessage = new LongMessage();
                while (resultSet.next()) {
                    longMessage.add(resultSet.getString("id"));
                }
                longMessage.send(player);
            }
        });
    }
}
