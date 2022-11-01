package mineultra.core.explosion;

import java.util.Collection;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExplosionEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private final Collection<Block> _blocks;

  public ExplosionEvent(Collection<Block> blocks)
  {
    _blocks = blocks;
  }

  @Override
  public HandlerList getHandlers()
  {
    return handlers;
  }

  public static HandlerList getHandlerList()
  {
    return handlers;
  }

  public Collection<Block> GetBlocks()
  {
    return _blocks;
  }
}