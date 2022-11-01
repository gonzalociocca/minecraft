package me.gonzalociocca.minigame.events;

import me.gonzalociocca.minigame.games.GameState;
import me.gonzalociocca.minigame.games.game.GameBase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by noname on 30/3/2017.
 */
public class GameStateChangeEvent extends Event {

    GameBase base;
    GameState old;
    GameState next;
    public GameStateChangeEvent(GameBase game, GameState original, GameState nextState){
        base = game;
        old = original;
        next = nextState;
    }
    public GameBase getGame(){
        return base;
    }
    public GameState getCurrentState(){
        return old;
    }
    public GameState getNewState(){
        return next;
    }

    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
