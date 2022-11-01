package server.instance.core.combat.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;

public class ClearCombatEvent extends Event
{
    private static final HandlerList handlers;
    private Player _player;
    GameServer _game;
    
    static {
        handlers = new HandlerList();
    }
    
    public ClearCombatEvent(GameServer game, final Player player) {
        super();
        _game = game;
        this._player = player;
    }

    public GameServer getGame(){
        return _game;
    }
    @Override
    public HandlerList getHandlers() {
        return ClearCombatEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return ClearCombatEvent.handlers;
    }
    
    public Player getPlayer() {
        return this._player;
    }
}
