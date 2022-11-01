package mineultra.game.center.managers;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import mineultra.game.center.game.Game;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import java.util.HashMap;
import mineultra.core.common.Rank;
import mineultra.core.common.util.C;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;
import mineultra.game.center.centerManager;
import org.bukkit.event.Listener;

public class IdleManager implements Listener
{
    private centerManager Manager = null;
    private final HashMap<Player, Float> _yaw;
    private final HashMap<Player, Long> _idle;
    private final HashMap<Player, Integer> _beep;
    
    public IdleManager(final centerManager manager) {
        this._yaw = new HashMap<>();
        this._idle = new HashMap<>();
        this._beep = new HashMap<>();
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents((Listener)this, (Plugin)this.Manager.GetPlugin());
    }
    
    @EventHandler
    public void KickIdlePlayers(final UpdateEvent event) {
        if (event.getType() != UpdateType.FAST) {
            return;
        }
        if (this.Manager.GetGame() == null) {
            return;
        }
        if (this.Manager.GetGame() != null && !this.Manager.GetGame().IdleKick) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            if (!this._yaw.containsKey(player) || !this._idle.containsKey(player)) {
                this._yaw.put(player, player.getLocation().getYaw());
                this._idle.put(player, System.currentTimeMillis());
            }
            if (this._yaw.get(player) == player.getLocation().getYaw()) {
                if (UtilTime.elapsed(this._idle.get(player), 120000L)) {
                    if (this.Manager.GetGame().GetState() == Game.GameState.Recruit || this.Manager.GetGame().IsAlive(player)) {
                            if (!this._beep.containsKey(player)) {
                                this._beep.put(player, 20);
                            }
                            else {
                                int count = this._beep.get(player);
                                if (count == 0) {
                                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10.0f, 1.0f);
                                    this.Manager.GetPortal().SendPlayerToServer(player, this.Manager.getConfig().getString("GameConfig.bungeelobby","lobby"));
                                }
                                else {
                                    final float scale = (float)(0.8 + count / 20.0 * 1.2);
                                    player.playSound(player.getLocation(), Sound.NOTE_PLING, scale, scale);
                                    if (count % 2 == 0) {
                                        UtilPlayer.message((Entity)player, String.valueOf(C.cGold) + C.Bold + "You will be afk removed in " + count / 2 + " seconds...");
                                    }
                                    --count;
                                    this._beep.put(player, count);
                                }
                            }
                        
                    }
                }
            }
            else {
                this._yaw.put(player, player.getLocation().getYaw());
                this._idle.put(player, System.currentTimeMillis());
                this._beep.remove(player);
            }
        }
    }
    
    @EventHandler
    public void Quit(final PlayerQuitEvent event) {
        this._yaw.remove(event.getPlayer());
        this._idle.remove(event.getPlayer());
        this._beep.remove(event.getPlayer());
    }
}
