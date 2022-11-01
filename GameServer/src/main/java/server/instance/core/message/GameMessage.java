package server.instance.core.message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import server.ServerPlugin;
import server.api.GameAPI;
import server.common.Rank;
import server.instance.GameServer;
import server.instance.misc.GameState;
import server.instance.misc.GameTeam;
import server.user.User;
import server.util.UtilMsg;

/**
 * Created by noname on 23/5/2017.
 */
public class GameMessage {

    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player sender = event.getPlayer();
        String dead = "";
        User pd = ServerPlugin.getPlayerData(sender.getName());
        Rank.RankType rank = pd.getValuableData().getGlobalRanks().getRank().getRankType();

        GameServer game = GameAPI.getGameOf(sender);
        GameTeam team = game != null ? game.getLogin().getTeam(sender) : null;


        if (game != null && team != null && !game.isAlive(sender)) {
            dead = UtilMsg.Gray + "Dead ";
        }

        event.setFormat(String.valueOf(dead) + rank.getChatPrefix() + rank.getColor() + " %1$s: " + ChatColor.WHITE + "%2$s");
        if (game != null && game.getState() == GameState.Live) {
            boolean globalMessage = false;
            if (team != null) {
                if (event.getMessage().charAt(0) == '@') {
                    event.setMessage(event.getMessage().substring(1, event.getMessage().length()));
                    event.setFormat(UtilMsg.WhiteBold + "Team" + " " + dead + rank.getChatPrefix() + team.getColor() + "%1$s " + UtilMsg.White + "%2$s");
                } else {
                    globalMessage = true;
                    event.setFormat(String.valueOf(dead) + rank.getChatPrefix() + team.getColor() + " %1$s: " + UtilMsg.White + "%2$s");
                }
            }
            if (globalMessage) {
                event.getRecipients().clear();
                event.getRecipients().addAll(game.getPlayers(false));
            } else {
                event.getRecipients().clear();
                event.getRecipients().addAll(team.getPlayers(true));
            }
        }
    }

}
