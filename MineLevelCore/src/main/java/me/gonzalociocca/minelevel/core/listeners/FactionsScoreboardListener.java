package me.gonzalociocca.minelevel.core.listeners;

import me.gonzalociocca.minelevel.core.*;
import me.gonzalociocca.minelevel.core.enums.EventType;
import me.gonzalociocca.minelevel.core.enums.UpdateType;
import me.gonzalociocca.minelevel.core.misc.Code;
import me.gonzalociocca.minelevel.core.misc.FactionsEvent;
import me.gonzalociocca.minelevel.core.misc.Variable;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.user.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by noname on 8/2/2017.
 */
public class FactionsScoreboardListener implements Listener {

    public HashMap<String, Scoreboard> _scoreboard = new HashMap();
    private HashMap<String, Objective> _sideObjective = new HashMap();
    public static DecimalFormat df = new DecimalFormat("0.00");
    int nextPrefix = 0;
    private Main plugin;

    public FactionsScoreboardListener(Main main){
        plugin=main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onScoreboardQuitUnload(PlayerQuitEvent event) {
            Scoreboard scoreboard = _sideObjective.get(event.getPlayer().getName()).getScoreboard();
            String name = event.getPlayer().getName();

            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
            scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
            scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equals(name)) {
                    continue;
                }
                try {
                    Team team = GetScoreboard(p).getTeam(name);
                    if (team!=null) {
                        team.unregister();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            _scoreboard.remove(name);
            _sideObjective.remove(name);
    }

    @EventHandler
    public void onScoreboardJoinLoad(PlayerJoinEvent event) {
            updateScoreboard(event.getPlayer(),true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p == event.getPlayer()) {
                    continue;
                }
                if (GetScoreboard(p).getTeam(event.getPlayer().getName()) == null) {
                    Team te = GetScoreboard(p).registerNewTeam(event.getPlayer().getName());
                    te.setAllowFriendlyFire(true);
                    te.setNameTagVisibility(NameTagVisibility.ALWAYS);
                    te.setPrefix(plugin.getDB().getPlayerData(event.getPlayer().getName()).getRank().getType().getScoreboardPrefix());
                }
                GetScoreboard(p).getTeam(event.getPlayer().getName()).addPlayer(event.getPlayer());
                if (GetScoreboard(event.getPlayer()).getEntryTeam(p.getName()) == null) {
                    Team te = GetScoreboard(event.getPlayer()).registerNewTeam(p.getName());
                    te.setAllowFriendlyFire(true);
                    te.setNameTagVisibility(NameTagVisibility.ALWAYS);
                    te.setPrefix(plugin.getDB().getPlayerData(p.getName()).getRank().getType().getScoreboardPrefix());
                } else {
                    GetScoreboard(event.getPlayer()).getTeam(p.getName()).setPrefix(plugin.getDB().getPlayerData(p.getName()).getRank().getType().getScoreboardPrefix());
                }
                GetScoreboard(event.getPlayer()).getTeam(p.getName()).addPlayer(p);
            }
    }

    public void updateScoreboard(Player player, boolean newScoreboard){
        if(!player.isOnline()){
            return;
        }
        Scoreboard scoreboard = GetScoreboard(player);
        PlayerData pd = plugin.getDB().getPlayerData(player.getName());
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        FactionsEvent currentEvent = Variable.currentEvent;
        String timeLeft = "";
        String currentgame = "";
        String diamonds = Code.Color("➡ &b" + df.format(pd.getDiamonds()));
        String ping = pd.getPing();
        if(objective==null){
            return;
        }
        if(!newScoreboard) {
            if (currentEvent != null) {
                if (currentEvent.getEventType().equals(EventType.Min5)) {
                    timeLeft = Code.Color("&aFalta ") + currentEvent.getTimeLeft();
                    currentgame = Code.Color("&eEvento: &f") + currentEvent.getNextGameEvent().getName();
                } else {
                    timeLeft = Code.Color("&cQueda ") + currentEvent.getTimeLeft();
                    currentgame = Code.Color("&eEvento: &f") + currentEvent.getEventType().getName();
                }
            }
            if (!pd.scoreboardValue1.equals(currentgame)) {
                scoreboard.resetScores(pd.scoreboardValue1);
                objective.getScore(currentgame).setScore(15);
                pd.scoreboardValue1 = currentgame;
            }
            if (!pd.scoreboardValue2.equals(timeLeft)) {
                scoreboard.resetScores(pd.scoreboardValue2);
                objective.getScore(timeLeft).setScore(14); // Should reset
                pd.scoreboardValue2 = timeLeft;
            }
            if (!pd.scoreboardValue3.equals(ping)) {
                scoreboard.resetScores(pd.scoreboardValue3);
                objective.getScore(ping).setScore(12); // Should reset
                pd.scoreboardValue3 = ping;
            }
            if (!pd.scoreboardValue4.equals(diamonds)) {
                scoreboard.resetScores(pd.scoreboardValue4);
                objective.getScore(diamonds).setScore(9); // Should Reset
                pd.scoreboardValue4 = diamonds;
            }
        }
        if(newScoreboard) {
            pd.scoreboardValue1 = "";
            pd.scoreboardValue2 = "";
            pd.scoreboardValue3 = "";
            pd.scoreboardValue4 = "";
            String playerScoreboardPrefix = pd.getRank().getType().getScoreboardPrefix();
            playerScoreboardPrefix = playerScoreboardPrefix.length()>16?playerScoreboardPrefix.substring(0,16):playerScoreboardPrefix;

            for (Team team : scoreboard.getTeams()) {
                team.removePlayer(player);
            }
            if (scoreboard.getTeam(playerScoreboardPrefix) == null) {
                Team team = scoreboard.registerNewTeam(playerScoreboardPrefix);
                team.setPrefix(playerScoreboardPrefix + " ");
                team.setAllowFriendlyFire(true);
                team.setNameTagVisibility(NameTagVisibility.ALWAYS);
            }

            objective.getScore(Variable.Color1).setScore(16);
            objective.getScore(Variable.Color2).setScore(13);
            objective.getScore(Variable.Color3).setScore(11);
            objective.getScore(Variable.Currency).setScore(10);
            objective.getScore(Variable.Color4).setScore(8);
            objective.getScore(Variable.Website).setScore(7);

            scoreboard.getTeam(playerScoreboardPrefix).addPlayer(player);
            player.setScoreboard(scoreboard);
        }
    }

    @EventHandler
    public void updateScoreboard(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {
            for(Player player : Bukkit.getOnlinePlayers()){
                this.updateScoreboard(player,false);
            }
            this.updateNextPrefix();
        }
    }

    public Scoreboard GetScoreboard(Player p)
    {
        Scoreboard value = _scoreboard.get(p.getName());
        if(value==null){
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("Obj"+Code.getRandom().nextInt(999999999),"dummy");
            _sideObjective.put(p.getName(), objective);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(Code.Color(Variable.White +"  "+"MineLevel"+"  "));

            _scoreboard.put(p.getName(), scoreboard);
            value = scoreboard;
        }

        return value;
    }


    public void loadP(Player p){
        this.updateScoreboard(p,true);

        Scoreboard scoreboard1 = GetScoreboard(p);

        for (Player pe : Bukkit.getOnlinePlayers()) {
            if (pe.getName().equals(p.getName())) {
                continue;
            }
            Scoreboard scoreboard2 = GetScoreboard(pe);
            if (scoreboard2.getTeam(p.getName()) == null) {
                Team te = scoreboard2.registerNewTeam(p.getName());
                te.setAllowFriendlyFire(true);
                te.setNameTagVisibility(NameTagVisibility.ALWAYS);
                te.setPrefix(plugin.getDB().getPlayerData(p.getName()).getRank().getType().getScoreboardPrefix());
            }
            scoreboard2.getTeam(p.getName()).addPlayer(p);
            if (scoreboard1.getEntryTeam(pe.getName()) == null) {
                Team te = scoreboard1.registerNewTeam(pe.getName());
                te.setAllowFriendlyFire(true);
                te.setNameTagVisibility(NameTagVisibility.ALWAYS);
                te.setPrefix(plugin.getDB().getPlayerData(pe.getName()).getRank().getType().getScoreboardPrefix());
            } else {
                scoreboard1.getTeam(pe.getName()).setPrefix(plugin.getDB().getPlayerData(pe.getName()).getRank().getType().getScoreboardPrefix());
            }
            scoreboard1.getTeam(pe.getName()).addPlayer(pe);
        }
    }

    public void updateNextPrefix(){
        if(nextPrefix>=Variable.scoreboardPrefixs.length){
            nextPrefix=0;
        }
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (!_sideObjective.containsKey(p.getName())) {
                this.loadP(p);
            }
            _sideObjective.get(p.getName()).setDisplayName(Variable.scoreboardPrefixs[nextPrefix]);
        }
        nextPrefix++;
    }

}
