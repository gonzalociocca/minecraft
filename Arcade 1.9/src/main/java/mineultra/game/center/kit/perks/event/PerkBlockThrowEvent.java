package mineultra.game.center.kit.perks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PerkBlockThrowEvent extends Event
{
    private static final HandlerList handlers;
    private final Player _player;
    
    static {
        handlers = new HandlerList();
    }
    
    public PerkBlockThrowEvent(final Player player) {
        super();
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PerkBlockThrowEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PerkBlockThrowEvent.handlers;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
}
