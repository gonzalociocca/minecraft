package server.instance.core.buffer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import server.ServerPlugin;

public class BufferEffect implements Listener
{
    protected GameBuffer buffer;
    
    public BufferEffect(final GameBuffer newBuffer) {
        super();
        this.buffer = newBuffer;
        Bukkit.getServer().getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }
}
