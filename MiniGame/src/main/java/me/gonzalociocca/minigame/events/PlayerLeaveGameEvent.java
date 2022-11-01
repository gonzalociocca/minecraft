package me.gonzalociocca.minigame.events;

import me.gonzalociocca.minigame.games.game.GameBase;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by noname on 2/4/2017.
 */
public class PlayerLeaveGameEvent extends Event {

    GameBase base;
    Player player;

    public PlayerLeaveGameEvent(GameBase game, Player aplayer) {
        base = game;
        player = aplayer;
    }

    public GameBase getGame() {
        return base;
    }

    public Player getPlayer() {
        return player;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}