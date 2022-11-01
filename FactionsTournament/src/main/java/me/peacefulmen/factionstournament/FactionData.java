package me.peacefulmen.factionstournament;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by ciocca on 07/04/2016.
 */
public class FactionData {
    String factionid;
    boolean inscripto;
    String tag;
    String leader;
    int elo;
    List<String> players;
    JavaPlugin plugin;
    ItemStack banner;

    public FactionData(JavaPlugin aplugin, String afactionid, boolean ainscripto, String atag, String aleader, int aelo, List<String> aplayers) {
        plugin = aplugin;
        factionid = afactionid;
        inscripto = ainscripto;
        tag = atag;
        leader = aleader;
        if(this.getMassiveFaction() != null){
            tag = this.getMassiveFaction().getTag();
            leader = this.getMassiveFaction().getFPlayerLeader().getTag();
            plugin.getConfig().set("FactionsTournament.ID"+factionid+".tag",tag);
            plugin.getConfig().set("FactionsTournament.ID"+factionid+".leader",leader);
        }
        elo = aelo;
        players = aplayers;
        ItemStack abanner = new ItemStack(Material.BANNER);
        BannerMeta meta = (BannerMeta) abanner.getItemMeta();
        meta.setDisplayName(Colorizer.Color("&c"+tag));
        abanner.setItemMeta(meta);

        banner = abanner;
    }

    public Faction getMassiveFaction(){
        return Factions.i.get(factionid);
    }

    public String getFactionID() {
        return factionid;
    }

    public boolean isInscripto() {
        return inscripto;
    }

    public String getTag() {
        return tag;
    }

    public String getLeader() {
        return leader;
    }

    public int getElo() {
        return elo;
    }

    public List<String> getPlayers() {
        return players;
    }

    public int addElo(int elopoints) {
        elo += elopoints;
        plugin.getConfig().set("FactionsTournament.ID" + factionid + ".elo", elo);
        return elo;
    }
    public int removeElo(int elopoints) {
        elo -= elopoints;
        plugin.getConfig().set("FactionsTournament.ID" + factionid + ".elo", elo);
        return elo;
    }

    public void addPlayer(String name) {
        if (this.players.contains(name)) {
            return;
        } else {
            this.players.add(name);
            this.savePlayers();
        }
    }

    public void savePlayers() {
        String ps = "";
        for (String str : players) {
            if (str.length() < 1) {
                ps = str;
            } else {
                ps = ps + "," + str;
            }
        }
        plugin.getConfig().set("FactionsTournament.ID" + factionid + ".players", ps);
    }

    public int getBattleWinElo(FactionData enemy) {
        int diff = Math.abs(elo - enemy.getElo());
        int elopoints = 0;
        if (diff < 25) {
            elopoints = 10;
        }
        else if(diff < 50){
            elopoints = 7;
        }
        else if(diff < 75){
            elopoints = 5;
        }
        else if(diff < 100){
            elopoints = 2;
        }else if(diff < 125){
            elopoints = 1;
        }
        return elopoints;
    }
    public ItemStack getBanner(){
        return this.banner;
    }
}