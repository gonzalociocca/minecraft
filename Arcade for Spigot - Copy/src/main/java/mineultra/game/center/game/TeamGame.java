package mineultra.game.center.game;

import java.util.ArrayList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import java.util.Iterator;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import mineultra.game.center.kit.Kit;
import mineultra.game.center.GameType;
import mineultra.game.center.centerManager;
import mineultra.core.common.util.C;
import mineultra.core.common.util.NautHashMap;
import mineultra.core.common.util.UtilTime;
import mineultra.core.updater.UpdateType;
import mineultra.core.updater.event.UpdateEvent;

public abstract class TeamGame extends Game
{
    private NautHashMap<String, Long> _rejoinTime;
    protected NautHashMap<String, GameTeam> RejoinTeam;
    
    public TeamGame(final centerManager manager, final GameType gameType, final Kit[] kits, final String[] gameDesc) {
        super(manager, gameType, kits, gameDesc);
        this._rejoinTime = new NautHashMap<String, Long>();
        this.RejoinTeam = new NautHashMap<String, GameTeam>();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerQuit(final PlayerQuitEvent event) {
        if (!this.InProgress()) {
            return;
        }
        final Player player = event.getPlayer();
        final GameTeam team = this.GetTeam(player);
        if (team == null) {
            return;
        }
        if (!team.IsAlive(player)) {
            return;
        }
        team.RemovePlayer(player);
        if (player.isDead()) {
            return;
        }
        if (!this.QuitOut) {
            this._rejoinTime.put(player.getName(), System.currentTimeMillis());
            this.RejoinTeam.put(player.getName(), team);
            this.GetLocationStore().put(player.getName(), player.getLocation());
            this.Announce(team.GetColor() + C.Bold + player.getName() + " has disconnected, 3 minutes to come back.");
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerLoginAllow(final PlayerLoginEvent event) {
        if (!this.InProgress() || this.QuitOut) {
            return;
        }
        final GameTeam team = this.RejoinTeam.remove(event.getPlayer().getName());
        if (team != null && this._rejoinTime.remove(event.getPlayer().getName()) != null) {
            team.AddPlayer(event.getPlayer());
            this.Announce(team.GetColor() + C.Bold + event.getPlayer().getName() + " has reconnecting!");
        }
    }
    
    @EventHandler
    public void PlayerRejoinExpire(final UpdateEvent event) {
        if (event.getType() != UpdateType.SEC || this.QuitOut) {
            return;
        }
        final Iterator<String> rejoinIterator = this._rejoinTime.keySet().iterator();
        while (rejoinIterator.hasNext()) {
            final String name = rejoinIterator.next();
            if (!UtilTime.elapsed(this._rejoinTime.get(name), 180000L)) {
                continue;
            }
            rejoinIterator.remove();
            final GameTeam team = this.RejoinTeam.remove(name);
            if (team == null) {
                continue;
            }
            this.Announce(team.GetColor() + C.Bold + name + " not has reconnected in time!");
        }
    }
    
    @Override
    public void EndCheck() {
        if (!this.IsLive()) {
            return;
        }
        final ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();
        for (final GameTeam team : this.GetTeamList()) {
            if (team.GetPlayers(true).size() > 0) {
                teamsAlive.add(team);
            }
        }
        if (!this.QuitOut) {
            for (final GameTeam team : this.RejoinTeam.values()) {
                teamsAlive.add(team);
            }
        }
        if (teamsAlive.size() <= 1) {
            if (teamsAlive.size() > 0) {
                this.AnnounceEnd(teamsAlive.get(0));
            }
            for (final GameTeam team : this.GetTeamList()) {
                if (this.WinnerTeam != null && team.equals(this.WinnerTeam)) {
                    for (final Player player : team.GetPlayers(false)) {
                        this.AddGems(player, 10.0, "Winner Team", false);
                    }
                }
                for (final Player player : team.GetPlayers(false)) {
                    if (player.isOnline()) {
                        this.AddGems(player, 10.0, "Participation", false);
                    }
                }
            }
            this.SetState(Game.GameState.End);
        }
    }
}
