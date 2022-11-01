package me.gonzalociocca.minigame.games.combat;

import me.gonzalociocca.minigame.Core;
import me.gonzalociocca.minigame.games.game.GameBase;
import me.gonzalociocca.minigame.misc.Code;
import me.gonzalociocca.minigame.misc.PlayerData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by noname on 4/4/2017.
 */
public class CombatHandler {

    Core core;
    boolean PvP = true;
    boolean PvE = true;
    boolean Damage = true;
    GameBase game;
    HashMap<PlayerData, CombatData> data = new HashMap();

    public CombatHandler(Core plugin, GameBase base) {
        core = plugin;
        game = base;
    }

    public CombatData getData(PlayerData pd) {
        CombatData mydata = data.get(pd);
        if (mydata == null) {
            mydata = new CombatData();
            data.put(pd, mydata);
        }
        return mydata;
    }

    public void sendCombatData(PlayerData pd) {
        CombatData myData = getData(pd);
        String kills = "";
        for (CombatInfo info : myData.getKills()) {
            kills = kills.isEmpty() ? info.getPlayer().getPlayer().getName() : ", " + info.getPlayer().getPlayer().getName();
        }
        if (!kills.isEmpty()) {
            pd.getPlayer().sendMessage(Code.Color("&aAsesinatos: &e" + kills));
        }
        String deaths = "";
        for (CombatInfo info : myData.getDeaths()) {
            deaths = deaths.isEmpty() ? info.getPlayer().getPlayer().getName() : ", " + info.getPlayer().getPlayer().getName();
        }
        if (!deaths.isEmpty()) {
            pd.getPlayer().sendMessage(Code.Color("&aAsesinado por &e" + deaths));
        }

    }

    public boolean canPvP() {
        return PvP;
    }

    public boolean canPvE() {
        return PvE;
    }

    public boolean canDamage() {
        return Damage;
    }

    public void setPvP(boolean value) {
        PvP = value;
    }

    public void setPvE(boolean value) {
        PvE = value;
    }

    public void setDamage(boolean value) {
        Damage = value;
    }

}
