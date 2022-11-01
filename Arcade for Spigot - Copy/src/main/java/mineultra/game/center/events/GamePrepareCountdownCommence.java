package mineultra.game.center.events;

import mineultra.game.center.game.Game;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class GamePrepareCountdownCommence extends Event
{
    private static final HandlerList handlers;
    private Game _game;
    
    static {
        handlers = new HandlerList();
    }
    
    public GamePrepareCountdownCommence(final Game game) {
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
    
    public Game GetGame() {
        return this._game;
    }
}
