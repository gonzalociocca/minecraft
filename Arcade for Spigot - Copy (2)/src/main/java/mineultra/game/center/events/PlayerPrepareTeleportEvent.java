package mineultra.game.center.events;

import org.bukkit.entity.Player;
import mineultra.game.center.game.Game;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PlayerPrepareTeleportEvent extends Event
{
    private static final HandlerList handlers;
    private final Game _game;
    private final Player _player;
    
    static {
        handlers = new HandlerList();
    }
    
    public PlayerPrepareTeleportEvent(final Game game, final Player player) {
        super();
        this._game = game;
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PlayerPrepareTeleportEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerPrepareTeleportEvent.handlers;
    }
    
    public Game GetGame() {
        return this._game;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
}
