package server.common.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;

public class PlayerDeathOutEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private final GameServer _game;
    private final Player _player;
    private boolean _cancelled;
    
    static {
        handlers = new HandlerList();
    }
    
    public PlayerDeathOutEvent(final GameServer game, final Player player) {
        super();
        this._cancelled = false;
        this._game = game;
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PlayerDeathOutEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerDeathOutEvent.handlers;
    }
    
    public GameServer GetGame() {
        return this._game;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
    
    @Override
    public boolean isCancelled() {
        return this._cancelled;
    }
    
    @Override
    public void setCancelled(final boolean cancelled) {
        this._cancelled = cancelled;
    }
}
