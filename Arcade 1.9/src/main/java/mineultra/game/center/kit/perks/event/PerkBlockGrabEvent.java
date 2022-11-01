package mineultra.game.center.kit.perks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PerkBlockGrabEvent extends Event
{
    private static final HandlerList handlers;
    private final Player _player;
    private final int _id;
    private final byte _data;
    
    static {
        handlers = new HandlerList();
    }
    
    public PerkBlockGrabEvent(final Player player, final int id, final byte data) {
        super();
        this._player = player;
        this._id = id;
        this._data = data;
    }
    
    @Override
    public HandlerList getHandlers() {
        return PerkBlockGrabEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PerkBlockGrabEvent.handlers;
    }
    
    public Player GetPlayer() {
        return this._player;
    }
    
    public int GetId() {
        return this._id;
    }
    
    public byte GetData() {
        return this._data;
    }
}
