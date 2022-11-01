package me.gonzalociocca.minigame.scoreboard;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.column.Perks.DataTypes.GlobalRanks;
import me.gonzalociocca.minigame.events.Update.UpdateEvent;
import me.gonzalociocca.minigame.events.Update.UpdateType;
import me.gonzalociocca.minigame.misc.PlayerData;
import me.gonzalociocca.minigame.misc.Rank;
import net.minecraft.server.v1_8_R3.ScoreboardStatisticCriteria;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noname on 31/3/2017.
 */
public class BoardManager implements Listener {
    Core core;
    /**
     Prefixs
     Suffixs
     Below Name Health
     SideScoreboard
     Team Color
     Lobby & Game Scoreboard
     **/
    /**
     * Player1(PrefixSuffix "&6VIP &cName &aGuildMax", Scoreboard: "&fKills: 5", HPBar: "&f10HP")
     * Player2(PrefixSuffix "&bMVP &cName &aMaxinos", Scoreboard: "&fKills: 1", HPBar: "&f20HP")
     **/

    HashMap<String, SingleBoard> scoreboards = new HashMap();
    public BoardManager(Core plugin) {
        core = plugin;
        Bukkit.getPluginManager().registerEvents(this, core);
    }

    public SingleBoard getOrCreateBoardFor(String playerName){
        SingleBoard board = scoreboards.get(playerName);
        if(board==null){
            board = new SingleBoard(core,Bukkit.getScoreboardManager().getNewScoreboard());
            scoreboards.put(playerName, board);
        }
        return board;
    }
    @EventHandler
    public void onTesting(PlayerJoinEvent event){
        getOrCreateBoardFor(event.getPlayer().getName());
    }

    /**
     Lobby Scoreboard:
     Type = Unique Scoreboard
     Create on Join Server
     Set Prefix and Suffix (Rank, Guild)
     Set sidescoreboard (Elo, Money, Winrate)
     Set PlayerList
     get Updated with other players data when they join the server.
     get Updated and remove players data when they quit the server.
     Remove on Quit Server

     GameScoreboard:
     Type = Scoreboard per Team, teams created by color
     Scoreboards created on game team registered.
     Prefix, suffix, playerlist get updated on all teams when player join the game.
     Below Name shows elo, updated on game join.


     **/
    @EventHandler
    public void updateScoreboards(UpdateEvent event){
        if(!event.getType().equals(UpdateType.Slow)){
            return;
        }
        for(Map.Entry<String, SingleBoard> entry : scoreboards.entrySet()){
            String str = entry.getKey();
            SingleBoard board = entry.getValue();

        }
        Runnable rTest = new Runnable() {
            String str;
            @Override
            public void run() {
                /*
                Team team = core.getScoreboardManager().getTeamFor(player.getName());
                board.setOptionsFor(team, true, true, NameTagVisibility.ALWAYS);
                board.setPrefixSuffixDisplayFor(team, "MEGADIOS", "Guild", "Test");*/
            }
        };
    }
}
