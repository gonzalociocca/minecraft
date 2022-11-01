package mineultra.core.updater.event;


import mineultra.core.updater.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private final UpdateType _type;

  public UpdateEvent(UpdateType example)
  {
    _type = example;
  }

  public UpdateType getType()
  {
    return _type;
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
}