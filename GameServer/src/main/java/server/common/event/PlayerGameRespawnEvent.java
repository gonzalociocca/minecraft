package server.common.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;

public class PlayerGameRespawnEvent extends Event
{
    private static final HandlerList handlers;
    private final GameServer _game;
    private final Player _player;
    
    static {
        handlers = new HandlerList();
    }
    
    public PlayerGameRespawnEvent(final GameServer game, final Player player) {
        super();
        this._game = game;
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PlayerGameRespawnEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerGameRespawnEvent.handlers;
    }
    
    public GameServer GetGame() {
        return this._game;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
}
