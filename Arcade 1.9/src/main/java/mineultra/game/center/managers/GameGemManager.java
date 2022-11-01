package mineultra.game.center.managers;

import me.gonzalociocca.mineultra.DBManager;
import me.gonzalociocca.mineultra.Rank;
import mineultra.core.common.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import mineultra.game.center.game.GemData;
import java.util.HashMap;

import mineultra.game.center.events.GameStateChangeEvent;
import org.bukkit.event.EventPriority;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.events.PlayerStateChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import mineultra.game.center.game.Game;
import org.bukkit.entity.Player;
import mineultra.minecraft.game.core.combat.CombatComponent;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import org.bukkit.plugin.Plugin;
import mineultra.game.center.centerManager;
import org.bukkit.event.Listener;

public class GameGemManager implements Listener
{


    centerManager Manager = null;
    boolean DoubleGem;
    DBManager db = null;
    public GameGemManager(final centerManager manager) {
        super();
        db = manager.getDB();
        this.DoubleGem = false;
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents((Listener)this, (Plugin)this.Manager.GetPlugin());
    }

    @EventHandler
    public void PlayerKillAward(final CombatDeathEvent event) {
        final Game game = this.Manager.GetGame();
        if (game == null) {
            return;
        }
        if (!(event.GetEvent().getEntity() instanceof Player)) {
            return;
        }
        final Player killed = (Player)event.GetEvent().getEntity();
        if (event.GetLog().GetKiller() != null) {
            final Player killer = UtilPlayer.searchExact(event.GetLog().GetKiller().GetName());
            if (killer != null && !killer.equals(killed)) {
                game.AddGems(killer, game.GetKillsGems(killer, killed, false), "Kills", true);
                if (game.FirstKill) {
                    game.AddGems(killer, 10.0, "First blood", false);
                    game.FirstKill = false;
                    game.Announce(F.main("Game", this.Manager.GetColor(killer) + killer.getName() + " has making the first blood!"));
                }
            }
        }
        for (final CombatComponent log : event.GetLog().GetAttackers()) {
            if (event.GetLog().GetKiller() != null && log.equals(event.GetLog().GetKiller())) {
                continue;
            }
            final Player assist = UtilPlayer.searchExact(log.GetName());
            if (assist == null) {
                continue;
            }
            game.AddGems(assist, game.GetKillsGems(assist, killed, true), "Kill Assists", true);
        }
    }
    
    @EventHandler
    public void PlayerQuit(final PlayerQuitEvent event) {
        final Game game = this.Manager.GetGame();
        if (game == null) {
            return;
        }
        this.RewardGems(game, event.getPlayer(), true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void PlayerStateChange(final PlayerStateChangeEvent event) {
        if (event.GetState() != GameTeam.PlayerState.OUT) {
            return;
        }

        this.RewardGems(event.GetGame(), event.GetPlayer(), false);
    }
    
    @EventHandler
    public void GameStateChange(final GameStateChangeEvent event) {
        if (event.GetState() != Game.GameState.Dead) {
            return;
        }
        Player[] players;
        for (int length = (players = UtilServer.getPlayers()).length, i = 0; i < length; ++i) {
            final Player player = players[i];
            this.RewardGems(event.GetGame(), player, true);
        }
        if(players.length > 0){
            Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.GetPlugin(), new Runnable() {
                public void run() {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        Manager.GetPortal().SendPlayerToServer(p,"arcade");
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Manager.GetPlugin(), new Runnable() {
                        public void run() {
                            if(Manager.isEnding){
                            Bukkit.getServer().shutdown();}
                        }
                    }, 50L);
                }

            }, 100L);


        }
    }
    
    public void RewardGems(final Game game, final Player player, final boolean give) {
        this.AnnounceGems(game, player, game.GetPlayerGems().get(player), give);
        if (give) {
            this.GiveGems(game, player, game.GetPlayerGems().remove(player), game.GemMultiplier);
        }
    }

    public void GiveGems(final Game game, final Player player, final HashMap<String, GemData> gems, final double gameMult) {
        if (gems == null) {
            return;
        }
        int total = 0;
        for (final GemData data : gems.values()) {
            total += (int)data.Gems;
        }
        if (total <= 0) {
            total = 1;
        }
        else
        if (db.getPlayerData(player).getRank().isAtLeast(Rank.MVP)) {
            total *= 3;
        }else if (db.getPlayerData(player).getRank().isAtLeast(Rank.VIP)) {
            total *= 2;
        }
        total *= (int)gameMult;
        db.getPlayerData(player).addCoins(total);
      game.resetGems(player);
       
    }

    

    public void AnnounceGems(final Game game, final Player player, final HashMap<String, GemData> gems, final boolean give) {
        if (gems == null) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2.0f, 1.0f);
        UtilPlayer.message((Entity)player, "");
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
        UtilPlayer.message((Entity)player, Colorizer.Color("&aGame - &f&l" + game.GetName()));
        UtilPlayer.message((Entity)player, "");
        int earnedGems = 0;
        for (final String type : gems.keySet()) {
            int gemCount = (int)gems.get(type).Gems;
            if (gemCount <= 0) {
                gemCount = 1;
            }
            earnedGems += gemCount;
            final int amount = gems.get(type).Amount;
            String amountStr = "";
            if (amount > 0) {
                amountStr = String.valueOf(amount) + " ";
            }
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append((int)(gemCount * game.GemMultiplier)).append(" Coins").toString())) + " for " + F.elem(String.valueOf(amountStr) + type));
        }
        earnedGems *= (int)game.GemMultiplier;
        if(db.getPlayerData(player).getRank().isAtLeast(Rank.MVP)){
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(String.valueOf(C.cAqua) + db.getPlayerData(player).getRank().getChatPrefix()+" Rank"));
            earnedGems *= 3;
        }
        else if(db.getPlayerData(player).getRank().isAtLeast(Rank.VIP)){
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(String.valueOf(C.cAqua) + db.getPlayerData(player).getRank().getChatPrefix()+" Rank"));
            earnedGems *= 2;
        }

        UtilPlayer.message((Entity)player, "");
        if (give) {
            UtilPlayer.message((Entity)player, F.elem(String.valueOf(C.cWhite) + C.Bold+"Ahora tienes " + C.cGreen + C.Bold + (db.getPlayerData(player).getCoins() + earnedGems) + " Coins"));
        this.Manager.GetPortal().SendPlayerToServer(player,"arcade");
        }
        else {
            UtilPlayer.message((Entity)player, F.elem(String.valueOf(C.cWhite) + C.Bold+"El juego esta en progreso.."));
        }
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
    }
}
