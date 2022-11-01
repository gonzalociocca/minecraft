package server.common.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;

public class GamePrepareCountdownCommence extends Event
{
    private static final HandlerList handlers;
    private GameServer _game;
    
    static {
        handlers = new HandlerList();
    }
    
    public GamePrepareCountdownCommence(final GameServer game) {
        super();
        this._game = game;
    }
    
    @Override
    public HandlerList getHandlers() {
        return GamePrepareCountdownCommence.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return GamePrepareCountdownCommence.handlers;
    }
    
    public GameServer GetGame() {
        return this._game;
    }
}
