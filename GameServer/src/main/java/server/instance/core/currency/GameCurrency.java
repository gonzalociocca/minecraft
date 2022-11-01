package server.instance.core.currency;

import com.google.gson.annotations.Expose;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import server.ServerPlugin;
import server.common.Rank;
import server.instance.GameServer;
import server.instance.core.combat.CombatComponent;
import server.instance.core.combat.event.CombatDeathEvent;
import server.common.event.GameStateChangeEvent;
import server.common.event.PlayerStateChangeEvent;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.user.User;
import server.util.F;
import server.util.UtilMsg;
import server.util.UtilPlayer;

import java.util.HashMap;

/**
 * Created by noname on 18/4/2017.
 */
public class GameCurrency {
    @Expose
    boolean doubleGems = false;
    private final HashMap<Player, HashMap<String, GameGemData>> _gemCount = new HashMap();

    public void addGems(Player player, double gems, String reason, boolean countAmount) {
        if ((!countAmount) && (gems < 1.0D)) {
            gems = 1.0D;
        }
        if (getGems(player).containsKey(reason)) {
            getGems(player).get(reason).AddGems(gems);
        } else {
            getGems(player).put(reason, new GameGemData(gems, countAmount));
        }
    }

    public HashMap<Player, HashMap<String, GameGemData>> getPlayerGems() {
        return _gemCount;
    }

    public void resetGems(Player player) {
        if (_gemCount.containsKey(player)) {
            _gemCount.put(player, new HashMap());
        }

    }

    public HashMap<String, GameGemData> getGems(Player player) {
        if (!_gemCount.containsKey(player)) {
            _gemCount.put(player, new HashMap());
        }
        return _gemCount.get(player);
    }

    public void AnnounceGems(GameServer game, final Player player, final HashMap<String, GameGemData> gems, final boolean give) {
        if (gems == null) {
            return;
        }
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 1.0f);
        UtilPlayer.message(player, "");
        UtilPlayer.message(player, UtilMsg.spacer);
        UtilPlayer.message(player, UtilMsg.GameName.replace("%s", game.getTitle()));
        UtilPlayer.message(player, "");
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
            UtilPlayer.message(player, String.valueOf(F.elem(new StringBuilder(String.valueOf(UtilMsg.Green)).append("+").append((int)(gemCount * game.GemMultiplier)).append(" Coins").toString())) + " for " + F.elem(String.valueOf(amountStr) + type));
        }
        earnedGems *= (int)game.GemMultiplier;

        User pd = ServerPlugin.getPlayerData(player.getName());
        Rank.RankType rankType = pd.getValuableData().getGlobalRanks().getRank().getRankType();

        if(rankType.isAtLeast(Rank.RankType.MVP)){
            UtilPlayer.message(player, String.valueOf(F.elem(new StringBuilder(String.valueOf(UtilMsg.Green)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(UtilMsg.Aqua + rankType.getChatPrefix() + " Rank 3x Coins"));
            earnedGems *= 2;

        }
        else if (rankType.isAtLeast(Rank.RankType.VIP)) {
            UtilPlayer.message(player, String.valueOf(F.elem(new StringBuilder(String.valueOf(UtilMsg.Green)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(UtilMsg.Aqua + rankType.getChatPrefix() + " Rank 2x Coins"));
            earnedGems *= 2;
        }
        if (doubleGems) {
            UtilPlayer.message(player, String.valueOf(F.elem(new StringBuilder(String.valueOf(UtilMsg.Green)).append("+").append(earnedGems).append(" Coins").toString())) + " for " + F.elem(UtilMsg.DarkGreen + "x2 Coins Weekend"));
            earnedGems *= 2;
        }
        UtilPlayer.message(player, "");
        if (give) {/*
            UtilPlayer.message(player, UtilMsg.CurrencyUpdate.replace("%s", ""+(pd.getDataManager().getMoney().getValue(getGame().getType()) + earnedGems)));*/
        }
        else {
            UtilPlayer.message(player, UtilMsg.GameIsInProgress);
        }
        UtilPlayer.message(player, UtilMsg.spacer);
    }

    public void GiveGems(final Player player, final HashMap<String, GameGemData> gems, final double gameMult) {
        if (gems == null) {
            return;
        }
        User pd = ServerPlugin.getPlayerData(player.getName());
        Rank.RankType rankType = pd.getValuableData().getGlobalRanks().getRank().getRankType();

        double base = 0;
        for (final GameGemData data : gems.values()) {
            base += data.Gems;
        }
        double mult = gameMult;

        if(rankType.isAtLeast(Rank.RankType.MVP)){
            mult+=1D;
        }
        if (rankType.isAtLeast(Rank.RankType.VIP)) {
            mult+=1D;
        }
        if (doubleGems) {
            mult+=2D;
        }

        double total = base * mult;

        //pd.getDataManager().getMoney().addToValue(getGame().getType(), total);

        resetGems(player);
    }

    public void RewardGems(GameServer game, final Player player, final boolean give) {
        this.AnnounceGems(game, player, getPlayerGems().get(player), give);
        if (give) {
            this.GiveGems(player, getPlayerGems().remove(player), game.GemMultiplier);
        }
    }

    public void onCombatDeath(GameServer game, final CombatDeathEvent event) {
        if (event.getEvent().getEntity() instanceof Player) {
            final Player killed = (Player) event.getEvent().getEntity();
            if (event.getLog().GetKiller() != null) {
                final Player killer = UtilPlayer.searchExact(event.getLog().GetKiller().GetName());
                if (killer != null && !killer.equals(killed)) {
                    addGems(killer, getKillsGems(game, killer, killed, false), "Kills", true);
                    if (game.FirstKill) {
                        addGems(killer, 10.0, "First blood", false);
                        game.FirstKill = false;
                        game.Announce(UtilMsg.FirstBlood.replace("%s", game.getColor(killer) + killer.getName()).replace("%g", game.getTitle()));
                    }
                }
            }
            for (final CombatComponent log : event.getLog().GetAttackers()) {
                if (event.getLog().GetKiller() != null && log.equals(event.getLog().GetKiller())) {
                    continue;
                }
                final Player assist = UtilPlayer.searchExact(log.GetName());
                if (assist == null) {
                    continue;
                }
                addGems(assist, getKillsGems(game, assist, killed, true), "Kill Assists", true);
            }
        }
    }

    public void onPlayerStateChange(GameServer game, final PlayerStateChangeEvent event) {
        if (event.getState() == GameTeam.PlayerState.OUT) {
            this.RewardGems(game, event.getPlayer(), false);
        }
    }

    public void onGameStateChange(GameServer game, final GameStateChangeEvent event) {
        if (event.getState() == GameState.Dead) {
            for (Player player : event.getGame().getPlayers(false)) {
                RewardGems(game, player, true);
            }
        }
    }

    public double getKillsGems(GameServer game, Player killer, Player killed, boolean assist) {
        if (!game.DeathOut) {
            return 0.5D;
        }

        if (!assist) {
            return 4.0D;
        }

        return 1.0D;
    }

    public void cleanAll(){
        _gemCount.clear();
    }

}
