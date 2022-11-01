package server.common.event;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import server.instance.GameServer;

import java.util.Collection;

public class ExplosionEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private final Collection<Block> _blocks;
    private final GameServer currentGame;
    public ExplosionEvent(GameServer game, Collection<Block> blocks)
    {
        _blocks = blocks;
        currentGame = game;
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

    public GameServer getGame(){
        return currentGame;
    }
}