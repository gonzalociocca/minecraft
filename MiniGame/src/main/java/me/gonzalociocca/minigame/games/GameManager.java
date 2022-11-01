package me.gonzalociocca.minigame.games;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.events.GameStateChangeEvent;
import me.gonzalociocca.minigame.events.Update.UpdateEvent;
import me.gonzalociocca.minigame.events.Update.UpdateType;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noname on 26/3/2017.
 */
public class GameManager implements Listener {
    /**
     Per arena messages & chat
     Multiple arenas in one world
     Schematic Support
     **/

    /**
     * Map Table:
     **/
    Core core;
    List<GameBase> games = new ArrayList();

    public GameManager(Core plugin) {
        core = plugin;
        Bukkit.getPluginManager().registerEvents(this, core);
        core.getMapManager().loadMapsForGameType(GameType.SkyWarsSolo);

    }

    public List<GameBase> getGames() {
        return games;
    }

    public GameBase createGame(GameType type, GameModifier modifier) {
        MapCipherBase mapbase = core.getMapManager().getNewNextMapFor(type);
        GameBase game = null;
        try {
            Class[] constructor = new Class[4];
            constructor[0] = Core.class;
            constructor[1] = GameType.class;
            constructor[2] = MapCipherBase.class;
            constructor[3] = GameModifier.class;
            game = type.getGameType().getDeclaredConstructor(constructor).newInstance(core, type, mapbase, modifier);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        games.add(game);
        return game;
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        for (GameBase game : getGames()) {
            if (game.getTeam(core.getPlayerData(event.getPlayer().getName())) != null) {
                if (!game.canBuild()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBuild(BlockBreakEvent event) {
        GameBase base = getGameInBounds(event.getPlayer().getLocation());
        if(base!=null) {
            if (base.isPlayer(core.getPlayerData(event.getPlayer().getName()))) {
                if (!base.canBuild()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onState(GameStateChangeEvent event) {
        if (event.getNewState().equals(GameState.Loading)) {
            event.getGame().resetGame();
        } else if (event.getNewState().equals(GameState.InWait)) {
            event.getGame().setCountdown(-1);
            if(event.getCurrentState().equals(GameState.Loading)) {
                event.getGame().InitializeGameMap();
            }
        } else if (event.getNewState().equals(GameState.Starting)) {
            event.getGame().InitializeCountdown();
        } else if (event.getNewState().equals(GameState.InGame)) {
            event.getGame().startGame();
        } else if (event.getNewState().equals(GameState.Finishing)) {
            event.getGame().startGameEndFeast();
        }
    }

    @EventHandler
    public void onStateCheck(UpdateEvent event) {
        if (event.getType().equals(UpdateType.Slow)) {
            for (GameBase game : getGames()) {
                for (GameTeam team : game.getTeams()) {
                    game.updateScoreboard(team);
                }
            }
        }
        else if (event.getType().equals(UpdateType.Fast)) {
            for (GameBase game : getGames()) {
                if (game.getState().equals(GameState.Loading) && game.getMap().isFullyLoaded()) {
                    game.changeState(GameState.InWait);
                } else if (game.getState().equals(GameState.InWait) && game.canStartCountdown()) {
                    game.changeState(GameState.Starting);
                } else if (game.getState().equals(GameState.Starting) && game.getCountdown() == 0) {
                    game.changeState(GameState.InGame);
                } else if(game.getState().equals(GameState.Starting) && !game.canStartCountdown()){
                    game.changeState(GameState.InWait);
                }
                else if (game.getState().equals(GameState.InGame) && game.canFinish()) {
                    game.changeState(GameState.Finishing);
                } else if ((game.getState().equals(GameState.InGame) || game.getState().equals(GameState.Finishing)) && game.canReset()) {
                    game.changeState(GameState.Loading);
                }
            }
        }
        else if (event.getType().equals(UpdateType.Sec)) {
            for (GameBase game : getGames()) {
                if (game.getCountdown() > 0) {
                    game.countdown--;
                    if((game.countdown%5==0 || game.countdown < 5) && game.countdown > 0){
                        for(PlayerData pd : game.getPlayersList(true, true)){
                            if(pd.getPlayer()!=null){
                            pd.getPlayer().sendMessage(Code.Color("&eEl juego comienza en &4"+game.countdown+"&e "+(game.countdown>1 ? "segundos!":"segundo!")));
                            }
                        }
                    }
                }
                if(game.getEndGameCountdown() > 0){
                    game.endGameCountdown--;
                    if((game.endGameCountdown%5==0 || game.endGameCountdown < 5) && game.endGameCountdown > 0){
                        for(PlayerData pd : game.getPlayersList(true, true)) {
                            if (pd.getPlayer() != null) {
                                pd.getPlayer().sendMessage(Code.Color("&eEl juego termina en &4" + game.endGameCountdown + "&e " + (game.endGameCountdown > 1 ? "segundos!" : "segundo!")));
                            }
                        }
                    }
                }
            }
        } else if(event.getType().equals(UpdateType.FiveSec)){
            for(GameBase base : getGames()){
                base.checkSafePlay();
            }
        }
    }

    public GameBase getGameInBounds(Location loc) {
        for (GameBase base : getGames()) {
            if(base.isInBounds(loc)){
                return base;
            }
        }
        return null;
    }
    public GameBase getGameOf(PlayerData pd){
        for(GameBase base : getGames()){
            if(base.hasPlayer(pd)){
                return base;
            }
        }
        return null;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            GameBase game = event.getEntity() != null && event.getEntity() instanceof Player ? getGameInBounds(event.getEntity().getLocation()) : null;
            if(game != null){
                game.onDamage(event);
            }
        }
    }
    @EventHandler
    public void onPvP(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player || event.getDamager() instanceof Player){
            GameBase game = event.getEntity() != null && event.getEntity() instanceof Player ? getGameInBounds(event.getEntity().getLocation()):event.getDamager() != null && event.getDamager() instanceof Player ? getGameInBounds(event.getDamager().getLocation()) : null;
            if(game != null){
                game.onDamage(event);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

    }

    @EventHandler
    public void onChunkLoad(ChunkUnloadEvent event) {

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerLeave(PlayerQuitEvent event){
        PlayerData pd = core.getPlayerData(event.getPlayer().getName());
        GameBase base = getGameOf(pd);
        if(base!=null){
            base.removeFromGame(pd);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event){
        PlayerData pd = core.getPlayerData(event.getPlayer().getName());
        GameBase base = getGameOf(pd);
        if(base!=null && !base.isInBounds(pd.getPlayer().getLocation())){

        }
    }
}
