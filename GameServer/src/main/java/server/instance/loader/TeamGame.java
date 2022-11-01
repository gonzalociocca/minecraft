package server.instance;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import server.util.UtilMsg;
import server.util.UtilTime;
import server.common.UpdateType;
import server.common.event.UpdateEvent;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class TeamGame extends GameServer
{
    private HashMap<String, Long> _rejoinTime = new HashMap<String, Long>();
    protected HashMap<String, GameTeam> RejoinTeam = new HashMap<String, GameTeam>();
    
    public TeamGame() {
        super();
    }

    @Override
    public void resetGame() {
        super.resetGame();
        _rejoinTime.clear();
        RejoinTeam.clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerQuit(final PlayerQuitEvent event) {
        if (!this.inProgress()) {
            return;
        }
        final Player player = event.getPlayer();
        final GameTeam team = getLogin().getTeam(player);
        if (team == null) {
            return;
        }
        if (!team.isAlive(player)) {
            return;
        }
        team.RemovePlayer(player);
        if (player.isDead()) {
            return;
        }
        if (!this.QuitOut) {
            this._rejoinTime.put(player.getName(), System.currentTimeMillis());
            this.RejoinTeam.put(player.getName(), team);
            this.getLocationStore().put(player.getName(), player.getLocation());
            this.Announce(team.getColor() + UtilMsg.Bold + player.getName() + " has disconnected, 3 minutes to come back.");
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerLoginAllow(final PlayerLoginEvent event) {
        if (!this.inProgress() || this.QuitOut) {
            return;
        }
        final GameTeam team = this.RejoinTeam.remove(event.getPlayer().getName());
        if (team != null && this._rejoinTime.remove(event.getPlayer().getName()) != null) {
            team.AddPlayer(event.getPlayer());
            this.Announce(team.getColor() + UtilMsg.Bold+ event.getPlayer().getName() + " has reconnecting!");
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
            this.Announce(team.getColor() + UtilMsg.Bold + name + " not has reconnected in time!");
        }
    }
    
    @Override
    public void endCheck() {
        if (!this.isLive()) {
            return;
        }
        final ArrayList<GameTeam> teamsAlive = new ArrayList<GameTeam>();
        for (final GameTeam team : getLogin().getTeamList()) {
            if (team.getPlayers(true).size() > 0) {
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
            for (final GameTeam team : getLogin().getTeamList()) {
                if (this.WinnerTeam != null && team.equals(this.WinnerTeam)) {
                    for (final Player player : team.getPlayers(false)) {
                        getGems().addGems(player, 10.0, "Winner Team", false);
                    }
                }
                for (final Player player : team.getPlayers(false)) {
                    if (player.isOnline()) {
                        getGems().addGems(player, 10.0, "Participation", false);
                    }
                }
            }
            this.setState(GameState.End);
        }
    }
}
