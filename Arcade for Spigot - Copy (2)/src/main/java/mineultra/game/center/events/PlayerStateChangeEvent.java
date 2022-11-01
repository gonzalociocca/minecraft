package mineultra.game.center.events;

import mineultra.game.center.game.GameTeam;
import org.bukkit.entity.Player;
import mineultra.game.center.game.Game;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PlayerStateChangeEvent extends Event
{
    private static final HandlerList handlers;
    private final Game _game;
    private final Player _player;
    private final GameTeam.PlayerState _state;
    
    static {
        handlers = new HandlerList();
    }
    
    public PlayerStateChangeEvent(final Game game, final Player player, final GameTeam.PlayerState state) {
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
    
    public Game GetGame() {
        return this._game;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
    
    public GameTeam.PlayerState GetState() {
        return this._state;
    }
}