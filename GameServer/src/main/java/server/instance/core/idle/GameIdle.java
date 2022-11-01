package server.instance.core.idle;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import server.instance.GameServer;
import server.instance.misc.GameState;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.util.UtilMsg;
import server.util.UtilPlayer;
import server.util.UtilTime;

import java.util.Map;

/**
 * Created by noname on 19/4/2017.
 */
public class GameIdle {

    private final Map<Player, Float> _yaw = new UnifiedMap();
    private final Map<Player, Long> _idle = new UnifiedMap();
    private final Map<Player, Integer> _beep = new UnifiedMap();

    public void onPlayerQuit(PlayerQuitEvent event){
        _yaw.remove(event.getPlayer());
        _idle.remove(event.getPlayer());
        _beep.remove(event.getPlayer());
    }

    public void onUpdate(GameServer game, UpdateEvent event)
    {
        if (event.getType() == UpdateType.FAST) {
            if (game.IdleKick) {
                for (Player player : game.getPlayers(false)) {
                    if (!this._yaw.containsKey(player) || !this._idle.containsKey(player)) {
                        this._yaw.put(player, player.getLocation().getYaw());
                        this._idle.put(player, System.currentTimeMillis());
                    }
                    if (this._yaw.get(player) == player.getLocation().getYaw()) {
                        if (UtilTime.elapsed(this._idle.get(player), 120000L)) {
                            if (game.getState() == GameState.Recruit || game.isAlive(player)) {
                                if (!this._beep.containsKey(player)) {
                                    this._beep.put(player, 20);
                                } else {
                                    int count = this._beep.get(player);
                                    if (count == 0) {
                                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0f, 1.0f);
                                        //Manager.GetLobby().sendToLobbyWithItems(player, true);
                                    } else {
                                        final float scale = (float) (0.8 + count / 20.0 * 1.2);
                                        player.playSound(player.getLocation(), Sound.NOTE_PLING, scale, scale);
                                        if (count % 2 == 0) {
                                            UtilPlayer.message(player, UtilMsg.AFKRemoved.replace("%s", "" + (count / 2)));
                                        }
                                        --count;
                                        this._beep.put(player, count);
                                    }
                                }

                            }
                        }
                    } else {
                        this._yaw.put(player, player.getLocation().getYaw());
                        this._idle.put(player, System.currentTimeMillis());
                        this._beep.remove(player);
                    }
                }
            }
        }
    }

    public void cleanAll(){
        _yaw.clear();
        _idle.clear();
        _beep.clear();
    }
}
