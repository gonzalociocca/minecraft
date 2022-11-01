package me.gonzalociocca.mineultra;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ciocca on 15/03/2016.
 */

public class DBManager implements Listener {

    MineUltra plugin;
    JavaPlugin core;

    public DBManager(JavaPlugin plug){
        if(plug==null){
            return;
        }
        core = plug;
        plugin = (MineUltra) Bukkit.getPluginManager().getPlugin("MineUltra");
    }

    public void removePlayerData(String name){
        plugin.removePlayerData(name);
    }

    public void removePlayerData(Player player){
        plugin.removePlayerData(player.getName());
    }

    public PlayerData getPlayerData(String name){
        return plugin.getPlayerData(name);
    }

    public PlayerData getPlayerData(Player player){
        return plugin.getPlayerData(player.getName());
    }

    public void setServerStatus(String str){
        plugin.setServerStatus(str);
    }
    public void setServerMaxOnline(int maxonline){
        plugin.maxonline = maxonline;
    }

    public HashSet<ServerData> getServersEnabled(){ return plugin.getServersEnabled();}

    public LinkedHashMap<String,Integer> getTopKills() throws SQLException {
        return plugin.getTopKills();
    }

    public LinkedHashMap<String,Integer> getTopWins() throws SQLException {
        return plugin.getTopWins();
    }






    }
