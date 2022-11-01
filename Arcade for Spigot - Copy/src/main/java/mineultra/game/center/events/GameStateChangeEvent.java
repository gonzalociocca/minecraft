package mineultra.game.center.events;

import mineultra.game.center.game.Game;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class GameStateChangeEvent extends Event
{
    private static final HandlerList handlers;
    private final Game _game;
    private final Game.GameState _to;
    
    static {
        handlers = new HandlerList();
    }
    
    public GameStateChangeEvent(final Game game, final Game.GameState to) {
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
    
    public Game GetGame() {
        return this._game;
    }
    
    public Game.GameState GetState() {
        return this._to;
    }
}
