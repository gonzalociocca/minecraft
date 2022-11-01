package me.gonzalociocca.minigame.games;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * Created by noname on 31/3/2017.
 */
public class GameTeam {
    int maxSize;
    ArrayList<PlayerData> players = new ArrayList();
    ArrayList<CustomLocation> spawns;
    int teamID;
    boolean isTeam;
    Scoreboard teamScoreboard;
    HashMap<String, String> sidescoreboarddata = new HashMap();

    public GameTeam(int amaxSize, boolean aisTeam, ArrayList<CustomLocation> aspawns, int ateamID, boolean allowFriendlyFire, boolean canSeeFriendlyInvisibles, NameTagVisibility nameTagVisibility) {
        maxSize = amaxSize;
        isTeam = aisTeam;
        teamID = ateamID;
        spawns = aspawns;
        teamScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        for (int a = 0; a < 48; a++) {
            String color = Code.getColorOfInteger(a);
            Team team = teamScoreboard.registerNewTeam(color);
            if (maxSize == 1) {
                team.setPrefix("" + ChatColor.YELLOW);
            } else {
                team.setPrefix(color);
            }
            team.setAllowFriendlyFire(allowFriendlyFire);
            team.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
            team.setNameTagVisibility(nameTagVisibility);
        }
    }

    public Scoreboard getTeamScoreboard() {
        return teamScoreboard;
    }

    public void updateScoreOnBoard(Objective objective, String tag, String value, int slot){
        String str = sidescoreboarddata.get(tag);
        if (str != null) {
            getTeamScoreboard().resetScores(str);
        }
        String newStr = Code.Color(value);
        objective.getScore(newStr).setScore(slot);
        sidescoreboarddata.put(tag, newStr);
    }
    public void clearSideBoard(){
        for(Iterator<Map.Entry<String, String>> it = sidescoreboarddata.entrySet().iterator();it.hasNext();){
            Map.Entry<String, String> entry = it.next();
            getTeamScoreboard().resetScores(entry.getValue());
            it.remove();
        }
    }
    public Objective getSideBoardObjective(){
        Objective objective = getTeamScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            objective = getTeamScoreboard().registerNewObjective("stats", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        return objective;
    }
    public int getMaxSize() {
        return maxSize;
    }

    public void removePlayer(PlayerData pd) {
        players.remove(pd);
    }

    int nextSpawn = 0;

    public void addPlayer(PlayerData pd) {
        if (nextSpawn >= spawns.size()) {
            nextSpawn = 0;
        }
        players.add(pd);
        CustomLocation loc = spawns.get(nextSpawn);

        pd.getPlayer().teleport(new Location(Bukkit.getWorld(Core.getWorldGameName()), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5));
        nextSpawn++;
    }

    public boolean hasPlayer(PlayerData pd) {
        return players.contains(pd);
    }

    public ArrayList<PlayerData> getPlayers() {
        return players;
    }

    public ArrayList<CustomLocation> getSpawns() {
        return spawns;
    }

    public void clear() {
        for(Team team : teamScoreboard.getTeams()){
            for(String str : team.getEntries()){
                team.removeEntry(str);
            }
        }
        clearSideBoard();
        players.clear();
    }

    public String getColor() {
        return Code.getColorOfInteger(teamID);
    }

    public int getTeamID() {
        return teamID;
    }
}
