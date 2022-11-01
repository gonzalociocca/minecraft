/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gonzalociocca.mineultra;

import java.io.*;
import java.sql.ResultSet;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftOfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author ciocca
 */
public class ServerData implements Listener {
    //Store: ServerName, Broadcasts, Online/MaxOnline, Current PlayerList, Status, enabled
    int online = 0;
    int maxonline = 0;
    String playerlist;
    String status;
    boolean enabled = false;
    String servername;
    String broadcasts;
    serverType type;

    ServerData(int aonline, int amaxonline, String aplayerlist, String astatus, boolean aenabled, String aservername, String abroadcasts,serverType atype) {
        online = aonline;
        maxonline = amaxonline;
        playerlist = aplayerlist;
        status = astatus;
        enabled = aenabled;
        servername = aservername;
        broadcasts = abroadcasts;
        type = atype;
    }
    public int getOnline(){
        return online;
    }
    public int getMaxOnline(){
        return maxonline;
    }
    public boolean isEnabled(){
        return enabled;
    }
    public serverType getType(){
        return type;
    }
    public String getServerName(){
        return servername;
    }
    public String getStatus(){ return status;}


}
