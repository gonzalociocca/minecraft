package mineultra.minecraft.game.core.Buffer.events;

import mineultra.minecraft.game.core.Buffer.Buffer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class BufferApplyEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean _cancelled;
    private final Buffer _cond;
     
    static {
        handlers = new HandlerList();
    }
    
    public BufferApplyEvent(final Buffer cond) {
        super();
        this._cancelled = false;
        this._cond = cond;
    }
    
    @Override
    public HandlerList getHandlers() {
        return BufferApplyEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return BufferApplyEvent.handlers;
    }
    
    @Override
    public boolean isCancelled() {
        return this._cancelled;
    }
    
    @Override
    public void setCancelled(final boolean cancel) {
        this._cancelled = cancel;
    }
    
    public Buffer GetBuffer() {
        return this._cond;
    }
}
