package me.hugmanrique.bpr.data.events;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class PlayerRestartEvent extends Event {
    private Plugin plugin;
    private ProxiedPlayer player;

    public PlayerRestartEvent(Plugin plugin, ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
    }

    public boolean shouldFire(Plugin plugin){
        return this.plugin.equals(plugin);
    }

    public boolean shouldFire(String pluginName){
        return shouldFire(ProxyServer.getInstance().getPluginManager().getPlugin(pluginName));
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }
}
