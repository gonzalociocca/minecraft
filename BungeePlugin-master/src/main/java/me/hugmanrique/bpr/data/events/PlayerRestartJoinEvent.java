package me.hugmanrique.bpr.data.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Hugmanrique
 * @since 20/09/2016
 */
public class PlayerRestartJoinEvent extends PlayerRestartEvent {
    public PlayerRestartJoinEvent(Plugin plugin, ProxiedPlayer player) {
        super(plugin, player);
    }
}
