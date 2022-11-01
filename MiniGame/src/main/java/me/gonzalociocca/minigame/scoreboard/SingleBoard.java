package me.gonzalociocca.minigame.scoreboard;

import me.gonzalociocca.minigame.Core;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * Created by noname on 2/4/2017.
 */
public class SingleBoard {
    Scoreboard scoreboard;
    Core core;
    /**
     Prefix/Suffix/TeamOptions: Done
     SideScores: Not yet
     BelowName: Not yet
     PlayerList: Not yet
     **/
    public SingleBoard(Core plugin, Scoreboard myscoreboard){
        scoreboard = myscoreboard;
        core = plugin;
    }

    public void setBelowNameFor(String scoreName, String displayName){
        Objective obj = scoreboard.getObjective(DisplaySlot.BELOW_NAME);
        obj.setDisplayName(displayName);
    }

    public void setPrefixSuffixDisplayFor(Team team, String prefix, String suffix, String displayName){
        if(prefix!=null){
            team.setPrefix(prefix);
        }
        if(suffix!=null){
            team.setSuffix(suffix);
        }
        if(displayName!=null){
            team.setDisplayName(displayName);
        }
    }
    public void setOptionsFor(Team team, boolean canSeeFriendlyInvisibles, boolean allowFriendlyFire, NameTagVisibility visibility){
        team.setCanSeeFriendlyInvisibles(canSeeFriendlyInvisibles);
        team.setAllowFriendlyFire(allowFriendlyFire);
        team.setNameTagVisibility(visibility);
    }
    public Team getTeamFor(String teamName){
        Team team = scoreboard.getTeam(teamName);
        if(team==null){
            team = scoreboard.registerNewTeam(teamName);
        }
        return team;
    }
}
