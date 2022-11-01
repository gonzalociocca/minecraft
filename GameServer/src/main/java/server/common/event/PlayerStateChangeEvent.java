package server.common.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;
import server.instance.misc.GameTeam;

public class PlayerStateChangeEvent extends Event
{
    private static final HandlerList handlers;
    private final GameServer _game;
    private final Player _player;
    private final GameTeam.PlayerState _state;
    
    static {
        handlers = new HandlerList();
    }
    
    public PlayerStateChangeEvent(final GameServer game, final Player player, final GameTeam.PlayerState state) {
        super();
        this._game = game;
        this._player = player;
        this._state = state;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PlayerStateChangeEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerStateChangeEvent.handlers;
    }
    
    public GameServer getGame() {
        return this._game;
    }
    
    public Player getPlayer() {
        return this._player;
    }
    
    public GameTeam.PlayerState getState() {
        return this._state;
    }
}