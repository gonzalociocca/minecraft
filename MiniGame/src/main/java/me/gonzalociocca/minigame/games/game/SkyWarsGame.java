package me.gonzalociocca.minigame.games.game;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.column.UserStats.DataTypes.SWDeaths;
import me.gonzalociocca.minigame.column.UserStats.DataTypes.SWElo;
import me.gonzalociocca.minigame.column.UserStats.DataTypes.SWKills;
import me.gonzalociocca.minigame.column.UserStats.StatsManager;
import me.gonzalociocca.minigame.games.GameModifier;
import me.gonzalociocca.minigame.games.GameState;
import me.gonzalociocca.minigame.games.GameTeam;
import me.gonzalociocca.minigame.games.GameType;
import me.gonzalociocca.minigame.games.combat.CombatData;
import me.gonzalociocca.minigame.games.combat.CombatInfo;
import me.gonzalociocca.minigame.map.Cipher.MapCipherBase;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.CustomLocation;
import me.gonzalociocca.minigame.misc.PlayerData;
import me.gonzalociocca.minigame.misc.TagEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.naming.NameNotFoundException;
import java.util.ArrayList;

/**
 * Created by noname on 30/3/2017.
 */
public class SkyWarsGame extends GameBase {

    public SkyWarsGame(Core plugin, GameType type, MapCipherBase mapbase, GameModifier modifier) {
        super(plugin, type, mapbase, modifier);
    }

    @Override
    public void InitializeGameMap() {
        super.InitializeGameMap();
        int teamId = 0;
        for (CustomLocation loc : map.getCustomLocationList(TagEnum.PlayerLocationTag.getID())) {
            ArrayList<CustomLocation> spawns = new ArrayList();
            spawns.add(loc);
            GameTeam team = new GameTeam(1, false, spawns, teamId++, false, true, NameTagVisibility.ALWAYS);
            teams.add(team);
            setGlassAt(loc, Material.GLASS, (byte) 0);
        }
    }

    @Override
    public void InitializeCountdown() {
        countdown = 10;
    }

    @Override
    public boolean canStartCountdown() {
        return getPlayersSize(true, false) >= 2;
    }

    @Override
    public void startGame() {
        int players = 0;
        int teams = 0;
        int avgElo = 0;
        boolean isSolo = false;
        for (GameTeam team : getTeams()) {
            if(team.getMaxSize()==1){
                isSolo=true;
            }
            teams++;
            for (PlayerData pd : team.getPlayers()) {
                players++;
                avgElo += pd.getStatsManager().getSWElo().getValue();
            }
        }
        if (players > 0 && avgElo > 0) {
            avgElo = avgElo / players;
        }

        for (PlayerData pd : getPlayersList(true, true)) {
            if(pd.getPlayer()!=null) {
                pd.getPlayer().sendMessage(Code.Color("&a&m&l------------------------"));
                pd.getPlayer().sendMessage(Code.Color("&a"));
                pd.getPlayer().sendMessage(Code.Color("&eMapa: &a" + getMap().getName()));
                if (isSolo) {
                    pd.getPlayer().sendMessage(Code.Color("&eJugadores: &a" + players));
                } else {
                    pd.getPlayer().sendMessage(Code.Color("&eJugadores: &a" + players + "&e Teams: &a" + teams));
                }
                pd.getPlayer().sendMessage(Code.Color("&ePromedio: &a" + avgElo + " Elo"));
                pd.getPlayer().sendMessage(Code.Color("&a"));
                pd.getPlayer().sendMessage(Code.Color("&a&m&l------------------------"));
            }
        }

        for (CustomLocation loc : map.getCustomLocationList(TagEnum.PlayerLocationTag.getID())) {
            setGlassAt(loc, Material.AIR, (byte) 0);
        }
    }

    @Override
    public void giveReward(PlayerData pd) {
        CombatData data = getCombatHandler().getData(pd);
        StatsManager stats = pd.getStatsManager();

        double VarA = 8;
        double VarB = 200;// Range: 50-1000
        SWElo myElo = stats.getSWElo();
        SWKills myKills = stats.getSWKills();
        SWDeaths myDeaths = stats.getSWDeaths();

        double KDR =  myKills.getValue()/myDeaths.getValue() > 0 ? myDeaths.getValue() : 1;
        if(KDR < 0.25){
            VarB*=0.25;
        }else if(KDR > 5){
            VarB*=5;
        }else {
            VarB*=KDR;
        }

        for(CombatInfo info : data.getKills()) {
            double VarC = info.getPlayer().getStatsManager().getSWElo().getValue()/(myElo.getValue() > 0 ? myElo.getValue() : 1);
            double VarD = (VarA*(VarB/100));
            double VarE = (VarD*VarC)*1.05;
           myElo.addToValue(VarE);
            myKills.addToValue(1);
        }
        for(CombatInfo info : data.getDeaths()) {
            double VarC = myElo.getValue()/info.getPlayer().getStatsManager().getSWElo().getValue();
            double VarD = (VarA*(VarB/100));
            double VarE = VarD*VarC;
            myElo.substractValue(VarE);
            myDeaths.addToValue(1);
        }
    }

    public void setGlassAt(CustomLocation loc, Material mat, byte color) {
        World world = Bukkit.getWorld(Core.getWorldGameName());
        world.getBlockAt((int) loc.getX(), (int) loc.getY() - 1, (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX(), (int) loc.getY() + 2, (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX() + 1, (int) loc.getY(), (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX() - 1, (int) loc.getY(), (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ() + 1).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ() - 1).setTypeIdAndData(mat.getId(), color, false);

        world.getBlockAt((int) loc.getX() + 1, (int) loc.getY() + 1, (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX() - 1, (int) loc.getY() + 1, (int) loc.getZ()).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX(), (int) loc.getY() + 1, (int) loc.getZ() + 1).setTypeIdAndData(mat.getId(), color, false);
        world.getBlockAt((int) loc.getX(), (int) loc.getY() + 1, (int) loc.getZ() - 1).setTypeIdAndData(mat.getId(), color, false);
    }
}
