package server.command.server.edit;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import server.api.GameAPI;
import server.command.server.ServerCommand;
import server.command.server.edit.classeditor.ClassType;
import server.instance.GameServer;

public class onServerEdit {

    static int editingId = -1;

    public static void openEditMenu(Player player) {
        GameServer gameServer = null;
        if (editingId == 0) {
            gameServer = GameAPI.getServerInterface().getMainServer();
        }
        openClassEditor(gameServer, player);
    }

    public static void run(PlayerCommandPreprocessEvent event, String[] args) {
        if (args.length > 2) {
            editingId = Integer.parseInt(args[2]);
            openEditMenu(event.getPlayer());
        } else {
            ServerCommand.sendServerList(event.getPlayer());
        }
    }

    /*
    * /server edit <id>
    * <openClassEditor>
    * <listTypes>
    * Boolean Integer Double Class
    * <list>  <list>  <list> <close and openClassEditor>
    * <edit>  <edit>  <edit> <*>
    * */

    public static void openClassEditor(GameServer gameServer, Player player) {
        ClassType classType = ClassType.getByObject(gameServer);
        if(classType != null){
            classType.getEditor().edit(player, gameServer, null, null);
        }
    }


}
