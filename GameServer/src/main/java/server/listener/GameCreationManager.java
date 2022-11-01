package server.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import server.ServerPlugin;
import server.api.GameAPI;
import server.instance.GameServer;
import server.instance.misc.GameState;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.UtilTime;

import java.util.HashSet;
import java.util.Iterator;

public class GameCreationManager
        implements Listener {
    private final HashSet<GameServer> _ended = new HashSet();

    public GameCreationManager() {
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    @EventHandler
    public void NextGame(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }

        GameServer mainServer = GameAPI.getServerInterface().getMainServer();
        if (mainServer.getState() == GameState.Dead) {
            _ended.add(mainServer);
        }
        if (mainServer.subServerList != null && !mainServer.subServerList.isEmpty()) {
            for (GameServer gameServer : mainServer.subServerList) {
                if (gameServer.getState() == GameState.Dead) {
                    _ended.add(gameServer);
                }
            }
        }

        Iterator gameIterator = _ended.iterator();

        while (gameIterator.hasNext()) {
            GameServer game = (GameServer) gameIterator.next();

            //HandlerList.unregisterAll(instance);

            if (game.getMap() == null) {
                gameIterator.remove();
            } else {
                if (UtilTime.elapsed(game.getStateTime(), 10000L)) {
                    for (Player player : game.getPlayers(false)) {
                        game.getLogin().removePlayer(game, player, true, true);
                    }
                }
                if (UtilTime.elapsed(game.getStateTime(), 12500L)
                        && game.getPlayers(true).isEmpty()) {
                    game.resetGame();
                    gameIterator.remove();
                }
            }
        }
    }
}