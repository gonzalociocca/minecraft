package server.api.chat;

import org.bukkit.entity.Player;

/**
 * Created by noname on 26/5/2017.
 */
public abstract class ChatCallable {
    public abstract boolean onChat(Player player, String message);
}