package mineultra.game.center.kit.perks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PerkLeapEvent extends Event
{
    private static final HandlerList handlers;
    private final Player _player;
    
    static {
        handlers = new HandlerList();
    }
    
    public PerkLeapEvent(final Player player) {
        super();
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PerkLeapEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PerkLeapEvent.handlers;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
}
