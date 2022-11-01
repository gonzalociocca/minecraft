package me.gonzalociocca.minigame.misc;

import me.gonzalociocca.minigame.column.Perks.PerksManager;
import me.gonzalociocca.minigame.column.UserStats.StatsManager;
import me.gonzalociocca.minigame.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by noname on 24/3/2017.
 */
public class PlayerData {
    public Core core;
    String name;
    StatsManager statsmanager;
    PerksManager perksmanager;
    public boolean preventSave = false;

    public String initialData = "";
    public int playerid = -1;
    public PlayerData(String aname, Core main){
        core = main;
        name = aname;
        statsmanager = new StatsManager(this);
        perksmanager = new PerksManager(this);
        this.loadData();
    }
    public String getOnDatabaseName(){
        return name;
    }

    boolean hasBeenInserted = false;
    Player latest = null;
    public Player getPlayer(){
        if(latest == null){
            latest = Bukkit.getPlayer(name);
        } else if(!latest.isOnline()){
            latest = Bukkit.getPlayer(name);
        }
        return latest;
    }
    public boolean loadData() {
        preventSave = false;
        try {
            ResultSet res;
            res = core.getMySQL().getStatement().executeQuery("SELECT * FROM " + core.getMySQL().getUserTable() + " WHERE Name = '" + name + "'");

            res.next();
            playerid = res.getInt("ID");

            initialData = res.getString(StatsManager.getColumn());
            statsmanager.parseAllData(initialData);
            perksmanager.parseAllData(res.getString(PerksManager.getColumn()));


            res.close();
            return true;
        }catch(Exception e){
            if(!hasBeenInserted){
                hasBeenInserted = true;
                this.insertData();
                this.loadData();
                return false;
            }else {
                e.printStackTrace();
                preventSave = true;
            }
            return false;
        }
    }
    public boolean reLoadPerks() {
        ResultSet res;
        try {
            res = core.getMySQL().getStatement().executeQuery("SELECT "+perksmanager.getColumn()+" FROM " + core.getMySQL().getUserTable() + " WHERE Name = '" + name + "'");
            res.next();
            perksmanager.parseAllData(res.getString(PerksManager.getColumn()));
            res.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insertData() {

        String sqlinsert = "INSERT INTO `" + core.getMySQL().getUserTable() + "` ("
                + "Name" + ","
                + "UUID" + ","
                + StatsManager.getColumn() + ","
                + PerksManager.getColumn()
                + ") " +
                "  SELECT '" + name.toLowerCase() + "',"
                + "'" + "" + "',"
                + "'" + "" + "',"
                + "'" + "" + "'"
                + " FROM dual " +
                "WHERE NOT EXISTS " +
                "( SELECT Name FROM " + core.getMySQL().getUserTable() + " WHERE Name='" + name + "' );";

        try {
            core.getMySQL().getStatement().execute(sqlinsert);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean shouldSave(String latest){
        return initialData != null ? !initialData.equals(latest):false;
    }

    public StatsManager getStatsManager(){
        return statsmanager;
    }
    public PerksManager getPerksManager(){
        return perksmanager;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof PlayerData){
            PlayerData other = (PlayerData)o;
            return other.getOnDatabaseName().equals(getOnDatabaseName());
        }
        return false;
    }

}
