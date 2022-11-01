package server.api.board;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import server.api.board.misc.EnumBoardDisplayMode;
import server.api.board.misc.EnumBoardObjectiveMode;
import server.common.Code;
import server.util.UtilBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlayerBoard {
    /*
     Todo: Packet Schedule API, Weather-Biome-Time-Light API, PrefixSuffixTeam API,
     Todo: TabList, Sideboard, Bossbar, BelowName, Title / APIs
     Todo: Kits, DisplayAPI, ActionBar

     1 Game = 5 Players = 5 Unique API
     */

    private Map<Integer, String> sideScoreboard = new UnifiedMap<Integer, String>();
    private Map<String, String> prefixMap = new UnifiedMap<String, String>();
    private Map<String, String> suffixMap = new UnifiedMap<String, String>();
    private Map<String, String> tabListPlayers = new UnifiedMap<String, String>();

    private UnifiedMap<String, PacketBoardScore> belowName = new UnifiedMap();

    public void update(Player otherPlayer, EnumBoardDisplayMode mode, boolean force) {
        CraftPlayer craftPlayer = (CraftPlayer) otherPlayer;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection conn = entityPlayer.playerConnection;

        if (mode == EnumBoardDisplayMode.BelowName) {
            updateBelowName(otherPlayer, conn, force);
        }
    }

    public static void example(){
        /*

        BelowName | Sidebar:
        Estimated Size: 1-24 items per player
        * @GameServer
        * @OnLeave: Remove Board
        * @OnJoin: Create Board
        * @OnChange | @OnAnotherJoin: Update Board
        * */
        List<PlayerBoard> test = new ArrayList<>();
        for(PlayerBoard board : test) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                board.update(player, EnumBoardDisplayMode.BelowName, false);
            }
        }
    }

    public void updateBelowName(Player otherPlayer, PlayerConnection conn, boolean force) {
        PacketBoardScore score = belowName.get(otherPlayer.getName());
        if (score == null) {
            score = UtilBoard.createScore(onBelowName.getId(), (int) otherPlayer.getHealth(), PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, otherPlayer.getName());
            belowName.put(otherPlayer.getName(), score);
        } else if (force || otherPlayer.getHealth() != score.getScore()) {
            score.setScore((int) otherPlayer.getHealth());
        } else {
            return;
        }
        conn.sendPacket(score.get());
    }


    PacketBoardObjective onSidebar;
    PacketBoardDisplayObjective onSidebarDisplay = UtilBoard.createObjectiveDisplay("Sidebar", EnumBoardDisplayMode.Sidebar);

    PacketBoardObjective onBelowName;
    PacketBoardDisplayObjective onBelowNameDisplay = UtilBoard.createObjectiveDisplay("BelowName", EnumBoardDisplayMode.BelowName);

    PacketBoardObjective onList;
    PacketBoardDisplayObjective onListDisplay = UtilBoard.createObjectiveDisplay("List", EnumBoardDisplayMode.List);

    IntObjectHashMap<PacketBoardScore> scoreMap = new IntObjectHashMap();

    public PlayerBoard(Player player) {

        onSidebar = UtilBoard.createObjective("Sidebar", "Sidebar - " + player.getName(), EnumBoardObjectiveMode.Create, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        onBelowName = UtilBoard.createObjective("BelowName", Code.Color("&cHP"), EnumBoardObjectiveMode.Create, IScoreboardCriteria.EnumScoreboardHealthDisplay.HEARTS);
        onList = UtilBoard.createObjective("List", "List - " + player.getName(), EnumBoardObjectiveMode.Create, IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection conn = entityPlayer.playerConnection;

        conn.sendPacket(onSidebar.get());
        conn.sendPacket(onSidebarDisplay.get());

        conn.sendPacket(onBelowName.get());
        conn.sendPacket(onBelowNameDisplay.get());

        conn.sendPacket(onList.get());
        conn.sendPacket(onListDisplay.get());

        scoreMap.put(0, UtilBoard.createScore(onSidebar.getId(), 1, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, "Holiwis"));
        scoreMap.put(1, UtilBoard.createScore(onBelowName.getId(), 5, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, "TeaLover2"));
        scoreMap.put(3, UtilBoard.createScore(onBelowName.getId(), 6, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, "TeaLover"));

        scoreMap.put(2, UtilBoard.createScore(onList.getId(), conn.player.ping, PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE, player.getName()));
        PacketListHeadFooter packetListHeadFooter = new PacketListHeadFooter();
        packetListHeadFooter.setHeader(Code.Color("&a--------------------------------------------------------------------------------"));
        packetListHeadFooter.setFooter(Code.Color("&c--------------------------------------------------------------------------------"));
        conn.sendPacket(packetListHeadFooter.get());

        for (PacketBoardScore score : scoreMap.values()) {
            conn.sendPacket(score.get());
        }

        //There is no need to send scoreboard to the client.
    }

    public Scoreboard getBoard() {
        return null;
    }

    public Map<Integer, String> getData() {
        return sideScoreboard;
    }

    public void setSideLine(int line, String str) {

    }

    public void setTitle(String str) {

    }

    public void setPrefix(String str) {

    }

    public void setSuffix(String str) {

    }

    public void setBelowName(Object object, String value) {

    }

    public void setBelowName(Object object, int value) {

    }

    public Packet getPacketObjective() {
        PacketBoardObjective packet = new PacketBoardObjective();
        packet.setHealth(IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        packet.setMode(EnumBoardObjectiveMode.Create);
        packet.setId("None");
        packet.setDisplayName("Title");
        return packet.get();
    }

    public Packet getPacketDisplay() {
        PacketBoardDisplayObjective packet = new PacketBoardDisplayObjective();
        packet.setMode(EnumBoardDisplayMode.Sidebar);
        packet.setId("TestingTitle");
        return packet.get();
    }

    public Packet getPacketScore(PacketPlayOutScoreboardScore.EnumScoreboardAction action) {
        PacketBoardScore packet = new PacketBoardScore();
        packet.setAction(action);
        packet.setScore(1);
        packet.setScoreName("None");
        packet.setId("Test");
        return packet.get();
    }

    public Packet getPacketTeam() {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        return packet;
    }
}
