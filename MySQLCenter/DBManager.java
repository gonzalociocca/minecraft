package me.gonzalociocca.mineultra;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by ciocca on 15/03/2016.
 */

public class DBManager implements Listener {
    HashMap<String,PlayerData> playerdata = new HashMap();
    MineUltra plugin;
    JavaPlugin core;

    DBManager(JavaPlugin plug){
        if(plug==null){
            return;
        }
        core = plug;
        plugin = (MineUltra) Bukkit.getPluginManager().getPlugin("MineUltra");
    }

    public void removePlayerData(String name){
        if(this.playerdata.containsKey(name.toLowerCase())){
            this.playerdata.remove(name.toLowerCase());}
    }

    public PlayerData getPlayerData(String name){
        if(playerdata.containsKey(name.toLowerCase())){
            return playerdata.get(name.toLowerCase());
        }else{
            PlayerData pd = new PlayerData(name.toLowerCase(),plugin);
            if(pd != null){
                playerdata.put(name.toLowerCase(), pd);
                return pd;
            }
        }
        return null;
    }
    @EventHandler
    public void onJoin(org.bukkit.event.player.AsyncPlayerPreLoginEvent event){
        //pre load player data
        this.getPlayerData(event.getName());
    }
    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event){
        //clear player cache, he is leaving!
        this.removePlayerData(event.getPlayer().getName());
    }

    //example
    @EventHandler
    public void onJoinTest(PlayerJoinEvent event){
        String name = event.getPlayer().getName();
        PlayerData pd = this.getPlayerData(name);
        if(!pd.getRank().isAtLeast(Rank.VIP)){
            event.getPlayer().kickPlayer(Colorizer.Color("&4&l(?)&c solo jugadores VIP en adelante."));
        }
    }

}
