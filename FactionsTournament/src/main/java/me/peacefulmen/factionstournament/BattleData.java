package me.peacefulmen.factionstournament;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by ciocca on 07/04/2016.
 */
public class BattleData implements Listener {

    JavaPlugin plugin;
    FactionData fA;
    boolean fAReady;

    FactionData fB;
    boolean fBReady;
    int size;
    boolean isPlaying;
    Long start;
    public BattleData(JavaPlugin aplugin, FactionData afactionA, FactionData afactionB, int asize) {
        plugin = aplugin;
        fA = afactionA;
        fAReady=false;
        fB = afactionB;
        fBReady=false;
        int size = asize;
        start = System.currentTimeMillis();
    }

    public boolean hasExpired(){
        if(this.isPlaying){
            return false;
        }
        if(System.currentTimeMillis()-start > 60000L){
            return true;
        }
        return false;
    }
    public boolean isPlaying(){
        return isPlaying;
    }
    public FactionData getFactionA(){
        return fA;
    }
    public boolean isFactionAReady(){
        return fAReady;
    }
    public void setFactionAReady(){
        fAReady=true;
    }
    public FactionData getFactionB(){
        return fB;
    }
    public void setFactionBReady(){
        fBReady=true;
    }
    public boolean isFactionBReady(){
        return fBReady;
    }

}