package me.iiSnipez.CombatLog.Listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import me.gonzalociocca.minelevel.core.updater.UpdateEvent;
import me.gonzalociocca.minelevel.core.updater.UpdateType;
import me.iiSnipez.CombatLog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    CombatLog plugin;
    Faction factionIn;

    public PlayerMoveListener(CombatLog plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPlayerMove(UpdateEvent event) {
        if(event.getType() != UpdateType.SLOW){
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
        if(!player.getWorld().getPVP()){
            continue;
        }

        if (this.plugin.taggedPlayers.containsKey(player.getName())) {
            if (this.plugin.usesFactions) {
                Location l;

                if (this.plugin.useNewFaction) {
                    l = player.getLocation();
                    this.factionIn = Board.getFactionAt(l);
                    if (this.factionIn.getTag().equalsIgnoreCase("SafeZone")) {
                        this.plugin.taggedPlayers.remove(player.getName());
                        if (this.plugin.untagMessageEnabled) {
                            player.sendMessage(this.plugin.translateText(this.plugin.untagMessage));
                        }

                        return;
                    }
                }

                if (this.plugin.useOldFaction) {
                    l = player.getLocation();
                    this.factionIn = Board.getFactionAt(l);
                    if (this.factionIn.getTag().equalsIgnoreCase("SafeZone")) {
                        this.plugin.taggedPlayers.remove(player.getName());
                        if (this.plugin.untagMessageEnabled) {
                            player.sendMessage(this.plugin.translateText(this.plugin.untagMessage));
                        }

                        return;
                    }
                }

                if (this.plugin.useOldOldFaction) {
                    l = player.getLocation();
                    com.massivecraft.factions.Faction factionInOld = Board.getFactionAt(new FLocation(l));

                    if (factionInOld.getTag().equalsIgnoreCase("SafeZone")) {
                        this.plugin.taggedPlayers.remove(player.getName());
                        if (this.plugin.untagMessageEnabled) {
                            player.sendMessage(this.plugin.translateText(this.plugin.untagMessage));
                        }

                        return;
                    }
                }
            }

            if (this.plugin.removeFlyEnabled) {
                this.plugin.removeFly(player);
            }

            if (this.plugin.removeDisguiseEnabled) {
                this.plugin.removeDisguise(player);
            }
        }

    }}
}