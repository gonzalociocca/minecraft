package server.common.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;
import server.instance.misc.GameState;

public class GameStateChangeEvent extends Event
{
    private static final HandlerList handlers;
    private final GameServer _game;
    private final GameState _to;
    
    static {
        handlers = new HandlerList();
    }
    
    public GameStateChangeEvent(final GameServer game, final GameState to) {
        super();
        this._game = game;
        this._to = to;
    }
    
    @Override
    public HandlerList getHandlers() {
        return GameStateChangeEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GameStateChangeEvent.handlers;
    }
    
    public GameServer getGame() {
        return this._game;
    }
    
    public GameState getState() {
        return this._to;
    }
}
