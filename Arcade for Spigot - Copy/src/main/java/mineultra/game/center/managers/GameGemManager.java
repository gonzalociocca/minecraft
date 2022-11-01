package mineultra.game.center.managers;

import org.bukkit.entity.Entity;
import org.bukkit.Sound;
import mineultra.game.center.game.GemData;
import java.util.HashMap;
import mineultra.core.common.CachedPerm;
import mineultra.game.center.events.GameStateChangeEvent;
import org.bukkit.event.EventPriority;
import mineultra.game.center.game.GameTeam;
import mineultra.game.center.events.PlayerStateChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import mineultra.game.center.game.Game;
import org.bukkit.entity.Player;
import mineultra.core.common.Rank;
import mineultra.core.common.util.C;
import mineultra.core.common.util.F;
import mineultra.core.common.util.MSGUtil;
import mineultra.core.common.util.UtilPlayer;
import mineultra.core.common.util.UtilServer;
import mineultra.minecraft.game.core.combat.CombatComponent;
import mineultra.minecraft.game.core.combat.event.CombatDeathEvent;
import org.bukkit.plugin.Plugin;
import mineultra.game.center.centerManager;
import org.bukkit.event.Listener;
import org.black_ixx.playerpoints.PlayerPoints;

import org.bukkit.Bukkit;

public class GameGemManager implements Listener
{
    centerManager Manager = null;
    boolean DoubleGem;
    public GameGemManager(final centerManager manager) {
        super();
        this.DoubleGem = false;
        this.Manager = manager;
        this.Manager.GetPluginManager().registerEvents((Listener)this, (Plugin)this.Manager.GetPlugin());
    }

private PlayerPoints playerPoints;

private boolean hookPlayerPoints() {
    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
    playerPoints = PlayerPoints.class.cast(plugin);
    return playerPoints != null; 
}

public PlayerPoints getPlayerPoints() {
    if(hookPlayerPoints()){
    return playerPoints;
}
    return null;
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
        else if (perm.hasPerm(player,"mineultra.vip")) {
            total *= 2;
        }
        if (this.DoubleGem) {
            total *= 2;
        }
        total *= (int)gameMult;
      getPlayerPoints().getAPI().give(player.getUniqueId(), total);
      game.resetGems(player);
       
    }
    CachedPerm perm = new CachedPerm();
    
    Rank myrank = new Rank();
    public void AnnounceGems(final Game game, final Player player, final HashMap<String, GemData> gems, final boolean give) {
        if (gems == null) {
            return;
        }
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 1.0f);
        UtilPlayer.message((Entity)player, "");
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
        UtilPlayer.message((Entity)player, "§aGame - §f§l" + game.GetName());
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
        if(perm.hasPerm(player,"mineultra.coinsx3")){
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(String.valueOf(C.cAqua) + myrank.prefix(player)+" Rank 3x Coins"));
            earnedGems *= 2;
            
        }
                else if (perm.hasPerm(player,"mineultra.coinsx2")) {
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(String.valueOf(C.cAqua) + myrank.prefix(player)+" Rank 2x Coins"));
            earnedGems *= 2;
        }
        if (this.DoubleGem) {
            UtilPlayer.message((Entity)player, String.valueOf(F.elem(new StringBuilder(String.valueOf(C.cGreen)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(String.valueOf(C.cDGreen) + "x2 Coins Weekend"));
            earnedGems *= 2;
        }
        UtilPlayer.message((Entity)player, "");
        if (give) {
            UtilPlayer.message((Entity)player, F.elem(String.valueOf(C.cWhite) + "§lNow you have " + C.cGreen + C.Bold + (getPlayerPoints().getAPI().look(player.getName()) + earnedGems) + " Coins"));

                    }
        else {
            UtilPlayer.message((Entity)player, F.elem(String.valueOf(C.cWhite) + "§lThe game is in progress.."));
            UtilPlayer.message((Entity)player, F.elem(String.valueOf(C.cWhite) + "§lBut wait, the next will start " + C.cGreen + C.Bold + "SOON" + C.cWhite + C.Bold + " when its finished."));
        }
        UtilPlayer.message((Entity)player, MSGUtil.getLineSpacer());
    }
}
