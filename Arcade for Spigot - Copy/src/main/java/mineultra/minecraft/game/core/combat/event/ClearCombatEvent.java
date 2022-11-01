package mineultra.minecraft.game.core.combat.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class ClearCombatEvent extends Event
{
    private static final HandlerList handlers;
    private Player _player;
    
    static {
        handlers = new HandlerList();
    }
    
    public ClearCombatEvent(final Player player) {
        super();
        this._player = player;
    }
    
    @Override
    public HandlerList getHandlers() {
        return ClearCombatEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return ClearCombatEvent.handlers;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
}
