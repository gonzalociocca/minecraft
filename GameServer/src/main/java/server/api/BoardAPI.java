package server.api;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import server.ServerPlugin;
import server.api.board.PlayerBoard;
import server.common.UpdateType;
import server.common.event.UpdateEvent;

import java.util.HashMap;

public class BoardAPI implements Listener {
    public BoardAPI() {
        Bukkit.getPluginManager().registerEvents(this, ServerPlugin.getInstance());
    }

    /*
    todo:
    Able to change
    SideScoreboard, Prefixes, Suffixes, TabList, Below Name
    with ease.
    */
    private static HashMap<String, PlayerBoard> playerBoardMap = new HashMap();

    public static PlayerBoard getBoard(Player player){
        return getBoard(player.getName());
    }

    public static PlayerBoard getBoard(String playerName){
        return playerBoardMap.get(playerName);
    }
/*
    public void addPlayerToScoreboard(Player player, GameTeam myteam, boolean isSpectator) {

        String color = myteam.getColor();

        for (GameTeam team : getLogin().getTeamList()) {
            Scoreboard otherScoreboard = team.getScoreboard().getBoard();
            Team otherScoreboardTeam = otherScoreboard.getTeam(color);
            if (!otherScoreboardTeam.hasEntry(player.getId())) {
                otherScoreboardTeam.addEntry(player.getId());
            }
        }

    }

    public void removePlayerFromScoreboard(GameTeam fromTeam, Player player) {
        for (GameTeam team : getLogin().getTeamList()) {
            Team scoreboardTeam = team.getScoreboard().getBoard().getTeam(fromTeam.getColor());
            scoreboardTeam.removeEntry(player.getId());
        }
    }*/


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event){
        PlayerBoard playerBoard = new PlayerBoard(event.getPlayer());
        playerBoardMap.put(event.getPlayer().getName(), playerBoard);






        //UtilScoreboard.fillScoreboard(getScoreboard().getBoard(), host);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event){
        playerBoardMap.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event){
        if(event.getType() != UpdateType.FAST){
            return;
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            PlayerBoard playerBoard = getBoard(player);
            CraftPlayer craftPlayer = (CraftPlayer)player;
            EntityPlayer entityPlayer = craftPlayer.getHandle();
            //entityPlayer.playerConnection.sendPacket(playerBoard.getPacketTeam());
        }
    }
}
